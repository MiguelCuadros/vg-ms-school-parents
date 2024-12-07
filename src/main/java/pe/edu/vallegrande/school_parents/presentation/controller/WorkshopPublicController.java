package pe.edu.vallegrande.school_parents.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.school_parents.application.service.WorkshopService;
import pe.edu.vallegrande.school_parents.domain.dto.WorkshopRequest;
import pe.edu.vallegrande.school_parents.domain.dto.WorkshopResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@RestController
@RequestMapping("/public/workshops${api.version}")
@RequiredArgsConstructor
public class WorkshopPublicController {

    private final WorkshopService workshopService;

    @GetMapping("/all")
    public Flux<WorkshopResponse> findAll() {
        return workshopService.findAll();
    }

    @GetMapping("/active")
    public Flux<WorkshopResponse> findActive() {
        return workshopService.findActive();
    }

    @GetMapping("/inactive")
    public Flux<WorkshopResponse> findInactive() {
        return workshopService.findInactive();
    }

    @GetMapping("/{id}")
    public Mono<WorkshopResponse> findById(@PathVariable String id) {
        return workshopService.findById(id);
    }

    @PostMapping("/create")
    public Mono<WorkshopResponse> create(@RequestBody WorkshopRequest workshop) {
        return workshopService.create(workshop);
    }

    @PutMapping("/update/{id}")
    public Mono<WorkshopResponse> update(@PathVariable String id, @RequestBody WorkshopRequest workshop) {
        return workshopService.update(id, workshop);
    }

    @PatchMapping("/assignFamilies/{id}")
    public Mono<WorkshopResponse> assignFamilies(@PathVariable String id, @RequestBody Set<String> familiesIds) {
        return workshopService.assignFamilies(id, familiesIds);
    }

    @PatchMapping("/{id}")
    public Mono<Void> modifyStatus(@PathVariable String id, @RequestBody String status) {
        return workshopService.modifyStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return workshopService.delete(id);
    }

}
