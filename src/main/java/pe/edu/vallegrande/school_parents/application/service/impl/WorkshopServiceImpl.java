package pe.edu.vallegrande.school_parents.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.school_parents.application.client.*;
import pe.edu.vallegrande.school_parents.application.service.SessionService;
import pe.edu.vallegrande.school_parents.application.service.WorkshopService;
import pe.edu.vallegrande.school_parents.application.util.Family;
import pe.edu.vallegrande.school_parents.application.util.Teacher;
import pe.edu.vallegrande.school_parents.domain.dto.*;
import pe.edu.vallegrande.school_parents.domain.mapper.SessionMapper;
import pe.edu.vallegrande.school_parents.domain.mapper.WorkshopMapper;
import pe.edu.vallegrande.school_parents.domain.model.Session;
import pe.edu.vallegrande.school_parents.domain.model.Workshop;
import pe.edu.vallegrande.school_parents.domain.repository.WorkshopRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;
    private final SessionMapper sessionMapper;
    private final TeacherWebClient teacherWebClient;
    private final FamilyWebClient familyWebClient;
    private final SessionService sessionService;
    private final AuthServiceClient authServiceClient;

    @Override
    public Flux<WorkshopResponse> findAll() {
        return workshopRepository.findAllByOrderByCreationDate()
                .map(workshopMapper::toWorkshopResponse);
    }

    @Override
    public Flux<WorkshopResponse> findActive() {
        return workshopRepository.findByStatusOrderByCreationDate("A")
                .map(workshopMapper::toWorkshopResponse);
    }

    @Override
    public Flux<WorkshopResponse> findInactive() {
        return workshopRepository.findByStatusOrderByCreationDate("I")
                .map(workshopMapper::toWorkshopResponse);
    }

    @Override
    public Mono<WorkshopResponse> findById(String id) {
        return workshopRepository.findById(id)
                .map(workshopMapper::toWorkshopResponse);
    }

    @Override
    public Mono<WorkshopResponse> create(WorkshopRequest workshopRequest) {
        Mono<Teacher> teacherMono = teacherWebClient.getTeacherById(workshopRequest.getTeacher());

        Flux<Family> familiesFlux = Flux.fromIterable(workshopRequest.getFamilies())
                .flatMap(familyWebClient::getFamilyById)
                .collectList()
                .flatMapMany(Flux::fromIterable);

        return Mono.zip(teacherMono, familiesFlux.collectList())
                .flatMap(tuple -> {
                    Teacher teacher = tuple.getT1();
                    Set<Family> families = new HashSet<>(tuple.getT2());

                    Workshop workshop = workshopMapper.toWorkshop(workshopRequest);
                    workshop.setTeacher(teacher);
                    workshop.setFamilies(families);

                    return workshopRepository.save(workshop)
                            .flatMap(savedWorkshop -> createSessionsIfNeeded(savedWorkshop, workshopRequest.getSessions())) // Crear sesiones si es necesario
                            .map(workshopMapper::toWorkshopResponse);
                });
    }

    @Override
    public Mono<WorkshopResponse> update(String id, WorkshopRequest workshopRequest) {
        return workshopRepository.findById(id)
                .flatMap(existingWorkshop -> {
                    Mono<Teacher> teacherMono = teacherWebClient.getTeacherById(workshopRequest.getTeacher());
                    Flux<Family> familiesFlux = Flux.fromIterable(workshopRequest.getFamilies())
                            .flatMap(familyWebClient::getFamilyById)
                            .collectList()
                            .flatMapMany(Flux::fromIterable);

                    return Mono.zip(teacherMono, familiesFlux.collectList())
                            .flatMap(tuple -> {
                                Teacher teacher = tuple.getT1();
                                Set<Family> families = new HashSet<>(tuple.getT2());

                                workshopMapper.updateWorkshopFromRequest(existingWorkshop, workshopRequest);
                                existingWorkshop.setTeacher(teacher);
                                existingWorkshop.setFamilies(families);

                                return workshopRepository.save(existingWorkshop)
                                        .flatMap(updatedWorkshop -> createSessionsIfNeeded(updatedWorkshop, workshopRequest.getSessions())) // Crear o actualizar sesiones si es necesario
                                        .map(workshopMapper::toWorkshopResponse);
                            });
                });
    }

    @Override
    public Mono<WorkshopResponse> assignFamilies(String id, Set<String> familiesIds) {
        return workshopRepository.findById(id)
                .flatMap(workshop -> {
                    Set<Family> currentFamilies = new HashSet<>(workshop.getFamilies());

                    return Flux.fromIterable(familiesIds)
                            .flatMap(familyWebClient::getFamilyById)
                            .doOnNext(family -> {
                                if (!currentFamilies.contains(family)) {
                                    currentFamilies.add(family);
                                }
                            })
                            .then(Mono.defer(() -> {
                                workshop.setFamilies(currentFamilies);
                                return workshopRepository.save(workshop);
                            }));
                })
                .map(workshopMapper::toWorkshopResponse);
    }

    @Override
    public Mono<Void> modifyStatus(String id, String status) {
        return workshopRepository.findById(id)
                .flatMap(workshop -> {
                    workshop.setStatus(status);
                    return workshopRepository.save(workshop);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return workshopRepository.deleteById(id);
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

    private Mono<Workshop> createSessionsIfNeeded(Workshop workshop, List<Session> sessions) {
        if (sessions != null && !sessions.isEmpty()) {
            return Flux.fromIterable(sessions)
                    .flatMap(session -> {
                        SessionRequest sessionRequest = sessionMapper.toSessionRequest(session);
                        return sessionService.create(workshop.getId(), sessionRequest);
                    })
                    .then(Mono.just(workshop));
        } else {
            return Mono.just(workshop);
        }
    }

}
