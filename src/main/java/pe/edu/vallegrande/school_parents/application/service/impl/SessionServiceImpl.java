package pe.edu.vallegrande.school_parents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.school_parents.application.client.AuthServiceClient;
import pe.edu.vallegrande.school_parents.application.client.FamilyWebClient;
import pe.edu.vallegrande.school_parents.application.service.SessionService;
import pe.edu.vallegrande.school_parents.application.util.Family;
import pe.edu.vallegrande.school_parents.domain.dto.SessionRequest;
import pe.edu.vallegrande.school_parents.domain.dto.SessionResponse;
import pe.edu.vallegrande.school_parents.domain.mapper.SessionMapper;
import pe.edu.vallegrande.school_parents.domain.model.Session;
import pe.edu.vallegrande.school_parents.domain.model.Workshop;
import pe.edu.vallegrande.school_parents.domain.repository.SessionRepository;
import pe.edu.vallegrande.school_parents.domain.repository.WorkshopRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final WorkshopRepository workshopRepository;
    private final SessionMapper sessionMapper;
    private final FamilyWebClient familyWebClient;
    private final AuthServiceClient authServiceClient;

    @Override
    public Flux<SessionResponse> findAll() {
        return sessionRepository.findAllByOrderByCreationDate()
                .map(sessionMapper::toSessionResponse);
    }

    @Override
    public Flux<SessionResponse> findActive() {
        return sessionRepository.findByStatusOrderByCreationDate("A")
                .map(sessionMapper::toSessionResponse);
    }

    @Override
    public Flux<SessionResponse> findInactive() {
        return sessionRepository.findByStatusOrderByCreationDate("I")
                .map(sessionMapper::toSessionResponse);
    }

    @Override
    public Mono<SessionResponse> findById(String id) {
        return sessionRepository.findById(id)
                .map(sessionMapper::toSessionResponse);
    }

    @Override
    public Mono<SessionResponse> create(String workshopId, SessionRequest sessionRequest) {
        return workshopRepository.findById(workshopId)
                .flatMap(workshop -> {
                    if (workshop.getSessions() == null) {
                        workshop.setSessions(new ArrayList<>());
                    }

                    Session session = sessionMapper.toSession(sessionRequest);
                    session.setWorkshopId(workshop.getId());

                    if (sessionRequest.getFamiliesPresent() != null && !sessionRequest.getFamiliesPresent().isEmpty()) {
                        return handleAttendance(session, workshop, sessionRequest.getFamiliesPresent())
                                .flatMap(sessionResponse -> {
                                    if (workshop.getSessions().stream().noneMatch(s -> s.getId().equals(session.getId()))) {
                                        workshop.getSessions().add(session);
                                    }
                                    return workshopRepository.save(workshop)
                                            .then(Mono.just(sessionResponse));
                                });
                    } else {
                        session.setFamiliesPresent(null);
                        session.setFamiliesAbsent(null);
                        return sessionRepository.save(session)
                                .flatMap(savedSession -> {
                                    if (workshop.getSessions().stream().noneMatch(s -> s.getId().equals(savedSession.getId()))) {
                                        workshop.getSessions().add(savedSession);
                                    }
                                    return workshopRepository.save(workshop)
                                            .then(Mono.just(sessionMapper.toSessionResponse(savedSession)));
                                });
                    }
                });
    }

    @Override
    public Mono<SessionResponse> update(String id, SessionRequest sessionRequest) {
        return sessionRepository.findById(id)
                .flatMap(existingSession -> workshopRepository.findById(existingSession.getWorkshopId())
                        .flatMap(workshop -> {
                            sessionMapper.updateSession(existingSession, sessionRequest);

                            return handleAttendance(existingSession, workshop, new HashSet<>(sessionRequest.getFamiliesPresent()))
                                    .flatMap(updatedSession -> {
                                        workshop.getSessions().removeIf(s -> s.getId().equals(updatedSession.getId()));
                                        workshop.getSessions().add(existingSession);
                                        return workshopRepository.save(workshop)
                                                .thenReturn(updatedSession);
                                    });
                        }));
    }

    @Override
    public Mono<SessionResponse> manageAttendance(String sessionId, Set<String> presentFamilyIds) {
        return sessionRepository.findById(sessionId)
                .flatMap(session ->
                        workshopRepository.findById(session.getWorkshopId())
                                .flatMap(workshop -> handleAttendance(session, workshop, presentFamilyIds))
                );
    }

    @Override
    public Mono<Void> modifyStatus(String id, String status) {
        return sessionRepository.findById(id)
                .flatMap(session -> {
                    session.setStatus(status);
                    return sessionRepository.save(session)
                            .flatMap(updatedSession ->
                                    workshopRepository.findById(updatedSession.getWorkshopId())
                                            .flatMap(workshop -> {
                                                if ("I".equals(status)) {
                                                    workshop.setSessions(
                                                            workshop.getSessions().stream()
                                                                    .filter(s -> !s.getId().equals(updatedSession.getId()))
                                                                    .collect(Collectors.toList())
                                                    );
                                                } else if ("A".equals(status)) {
                                                    if (workshop.getSessions().stream().noneMatch(s -> s.getId().equals(updatedSession.getId()))) {
                                                        workshop.getSessions().add(updatedSession);
                                                    }
                                                }
                                                return workshopRepository.save(workshop);
                                            })
                            );
                })
                .then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return sessionRepository.findById(id)
                .flatMap(session -> workshopRepository.findById(session.getWorkshopId())
                        .flatMap(workshop -> {
                            workshop.getSessions().removeIf(s -> s.getId().equals(id));
                            return workshopRepository.save(workshop);
                        })
                        .then(sessionRepository.deleteById(id))
                );
    }

    @Override
    public Mono<Boolean> validateTokenAndRoles(String token, List<String> requiredRoles) {
        return authServiceClient.validateToken(token)
                .flatMap(validationResponse -> {
                    if (validationResponse.isValid() && requiredRoles.contains(validationResponse.getRole())) {
                        return Mono.just(true);
                    }
                    return Mono.just(false);
                });
    }

    private Mono<SessionResponse> handleAttendance(Session session, Workshop workshop, Set<String> presentFamilyIds) {
        Set<String> allFamilyIds = workshop.getFamilies().stream()
                .map(Family::getId)
                .collect(Collectors.toSet());

        Set<String> absentFamilyIds = new HashSet<>(allFamilyIds);
        absentFamilyIds.removeAll(presentFamilyIds);

        return familyWebClient.getFamiliesByIds(presentFamilyIds)
                .flatMap(familiesPresent -> familyWebClient.getFamiliesByIds(absentFamilyIds)
                        .flatMap(familiesAbsent -> {
                            session.setFamiliesPresent(new HashSet<>(familiesPresent));
                            session.setFamiliesAbsent(new HashSet<>(familiesAbsent));

                            return sessionRepository.save(session)
                                    .flatMap(savedSession -> {
                                        workshop.getSessions().removeIf(s -> s.getId().equals(savedSession.getId()));
                                        workshop.getSessions().add(savedSession);

                                        return workshopRepository.save(workshop)
                                                .thenReturn(sessionMapper.toSessionResponse(savedSession));
                                    });
                        }));
    }


}
