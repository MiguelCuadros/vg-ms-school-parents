package pe.edu.vallegrande.school_parents.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.school_parents.application.service.SessionService;
import pe.edu.vallegrande.school_parents.domain.dto.SessionRequest;
import pe.edu.vallegrande.school_parents.domain.dto.SessionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@RestController
@RequestMapping("/public/sessions${api.version}")
@RequiredArgsConstructor
public class SessionPublicController {

    private final SessionService sessionService;

    @GetMapping("/all")
    public Flux<SessionResponse> findAll() {
        return sessionService.findAll();
    }

    @GetMapping("/active")
    public Flux<SessionResponse> findActive() {
        return sessionService.findActive();
    }

    @GetMapping("/inactive")
    public Flux<SessionResponse> findInactive() {
        return sessionService.findInactive();
    }

    @GetMapping("/{id}")
    public Mono<SessionResponse> findById(@PathVariable String id) {
        return sessionService.findById(id);
    }

    @PostMapping("/create/{workshopId}")
    public Mono<SessionResponse> create(@PathVariable String workshopId, @RequestBody SessionRequest sessionRequest) {
        return sessionService.create(workshopId, sessionRequest);
    }

    @PutMapping("/update/{id}")
    public Mono<SessionResponse> update(@PathVariable String id, @RequestBody SessionRequest sessionRequest) {
        return sessionService.update(id, sessionRequest);
    }

    @PatchMapping("/manageAttendance/{id}")
    public Mono<SessionResponse> manageAttendance(@PathVariable String id, @RequestBody Set<String> presentFamilyIds) {
        return sessionService.manageAttendance(id, presentFamilyIds);
    }

    @PatchMapping("/{id}")
    public Mono<Void> modifyStatus(@PathVariable String id, @RequestBody String status) {
        return sessionService.modifyStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return sessionService.delete(id);
    }

}
