package pe.edu.vallegrande.school_parents.application.service;

import pe.edu.vallegrande.school_parents.domain.dto.SessionRequest;
import pe.edu.vallegrande.school_parents.domain.dto.SessionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface SessionService {

    Flux<SessionResponse> findAll();
    Flux<SessionResponse> findActive();
    Flux<SessionResponse> findInactive();
    Mono<SessionResponse> findById(String id);
    Mono<SessionResponse> create(String workshopId, SessionRequest sessionRequest);
    Mono<SessionResponse> update(String id, SessionRequest sessionRequest);
    Mono<SessionResponse> manageAttendance(String id, Set<String> presentFamilyIds);
    Mono<Void> modifyStatus(String id, String status);
    Mono<Void> delete(String id);
    Mono<Boolean> validateTokenAndRoles(String token, List<String> requiredRoles);

}
