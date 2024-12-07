package pe.edu.vallegrande.school_parents.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.school_parents.domain.model.Session;
import reactor.core.publisher.Flux;

public interface SessionRepository extends ReactiveMongoRepository<Session, String> {

    Flux<Session> findAllByOrderByCreationDate();
    Flux<Session> findByStatusOrderByCreationDate(String status);

}
