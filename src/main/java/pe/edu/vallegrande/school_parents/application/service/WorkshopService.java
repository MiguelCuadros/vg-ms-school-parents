package pe.edu.vallegrande.school_parents.application.service;

import pe.edu.vallegrande.school_parents.domain.dto.WorkshopRequest;
import pe.edu.vallegrande.school_parents.domain.dto.WorkshopResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface WorkshopService {

    Flux<WorkshopResponse> findAll();
    Flux<WorkshopResponse> findActive();
    Flux<WorkshopResponse> findInactive();
    Mono<WorkshopResponse> findById(String id);
    Mono<WorkshopResponse> create(WorkshopRequest workshopRequest);
    Mono<WorkshopResponse> update(String id, WorkshopRequest workshopRequest);
    Mono<WorkshopResponse> assignFamilies(String id, Set<String> familiesIds);
    Mono<Void> modifyStatus(String id, String status);
    Mono<Void> delete(String id);
    Mono<Boolean> validateTokenAndRoles(String token, List<String> requiredRoles);

}
