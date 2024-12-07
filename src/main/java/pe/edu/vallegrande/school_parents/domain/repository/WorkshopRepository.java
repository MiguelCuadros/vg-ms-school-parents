package pe.edu.vallegrande.school_parents.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.school_parents.domain.model.Workshop;
import reactor.core.publisher.Flux;

public interface WorkshopRepository extends ReactiveMongoRepository<Workshop, String> {

    Flux<Workshop> findAllByOrderByCreationDate();
    Flux<Workshop> findByStatusOrderByCreationDate(String status);

}
