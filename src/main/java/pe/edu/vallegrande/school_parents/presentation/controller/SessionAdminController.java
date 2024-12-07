package pe.edu.vallegrande.school_parents.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.school_parents.application.service.SessionService;
import pe.edu.vallegrande.school_parents.domain.dto.SessionRequest;
import pe.edu.vallegrande.school_parents.domain.dto.SessionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/directives/sessions${api.version}")
@RequiredArgsConstructor
public class SessionAdminController {

    private final SessionService sessionService;

    private static final List<String> ALLOWED_ROLES = List.of("DEVELOP", "PROFESOR", "SECRETARIO", "DIRECTOR");

    @GetMapping("/all")
    public Mono<ResponseEntity<Flux<SessionResponse>>> findAll(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(sessionService.findAll()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @GetMapping("/active")
    public Mono<ResponseEntity<Flux<SessionResponse>>> findActive(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(sessionService.findActive()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @GetMapping("/inactive")
    public Mono<ResponseEntity<Flux<SessionResponse>>> findInactive(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok(sessionService.findInactive()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<SessionResponse>> findById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return sessionService.findById(id)
                                .map(ResponseEntity::ok)
                                .defaultIfEmpty(ResponseEntity.notFound().build());
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PostMapping("/create/{workshopId}")
    public Mono<ResponseEntity<SessionResponse>> create(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String workshopId, @RequestBody SessionRequest sessionRequest) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return sessionService.create(workshopId, sessionRequest)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<SessionResponse>> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody SessionRequest sessionRequest) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return sessionService.update(id, sessionRequest)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PatchMapping("/manageAttendance/{id}")
    public Mono<ResponseEntity<SessionResponse>> manageAttendance(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody Set<String> presentFamilyIds) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return sessionService.manageAttendance(id, presentFamilyIds)
                                .map(ResponseEntity::ok);
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Void>> modifyStatus(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id, @RequestBody String status) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return sessionService.modifyStatus(id, status)
                                .then(Mono.just(ResponseEntity.ok().build()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String id) {
        String token = authorizationHeader.substring(7);
        return sessionService.validateTokenAndRoles(token, ALLOWED_ROLES)
                .flatMap(isValid -> {
                    if (isValid) {
                        return sessionService.delete(id)
                                .then(Mono.just(ResponseEntity.ok().build()));
                    } else {
                        return Mono.just(ResponseEntity.status(403).build());
                    }
                });
    }

}
