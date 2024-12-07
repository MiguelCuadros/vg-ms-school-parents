package pe.edu.vallegrande.school_parents.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.school_parents.application.service.WorkshopService;
import pe.edu.vallegrande.school_parents.domain.dto.WorkshopRequest;
import pe.edu.vallegrande.school_parents.domain.dto.WorkshopResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/shared/workshops${api.version}")
@RequiredArgsConstructor
public class WorkshopUserController {

    private final WorkshopService workshopService;

    private static final List<String> ALLOWED_ROLES = List.of("DEVELOP", "ESTUDIANTE", "PROFESOR");

    @GetMapping("/all")
    public Mono<ResponseEntity<Flux<WorkshopResponse>>> findAll(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(workshopService.findAll()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @GetMapping("/active")
    public Mono<ResponseEntity<Flux<WorkshopResponse>>> findActive(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(workshopService.findActive()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @GetMapping("/inactive")
    public Mono<ResponseEntity<Flux<WorkshopResponse>>> findInactive(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(workshopService.findInactive()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<WorkshopResponse>> findById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return workshopService.findById(id)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<WorkshopResponse>> create(@RequestHeader("Authorization") String authorizationHeader, @RequestBody WorkshopRequest workshop) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return workshopService.create(workshop)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<WorkshopResponse>> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody WorkshopRequest workshop) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return workshopService.update(id, workshop)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PatchMapping("/assignFamilies/{id}")
    public Mono<ResponseEntity<WorkshopResponse>> assignFamilies(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody Set<String> familiesIds) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return workshopService.assignFamilies(id, familiesIds)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Void>> modifyStatus(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody String status) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return workshopService.modifyStatus(id, status)
                                .then(Mono.just(ResponseEntity.ok().build()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        String token = authorizationHeader.substring(7);
        return workshopService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return workshopService.delete(id)
                                .then(Mono.just(ResponseEntity.ok().build()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

}
