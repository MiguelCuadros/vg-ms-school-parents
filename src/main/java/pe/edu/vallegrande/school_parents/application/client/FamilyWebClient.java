package pe.edu.vallegrande.school_parents.application.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.school_parents.application.util.Family;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FamilyWebClient {

    private final WebClient.Builder webClientBuilder;
    private final String familyServiceUrl;

    @Autowired
    public FamilyWebClient(WebClient.Builder webClientBuilder, @Value("${spring.microservices.family.url}") String familyServiceUrl) {
        this.webClientBuilder = webClientBuilder;
        this.familyServiceUrl = familyServiceUrl;
    }

    public Mono<Family> getFamilyById(String familyId) {
        return webClientBuilder.build()
                .get()
                .uri(familyServiceUrl + "/{familyId}", familyId)
                .retrieve()
                .bodyToMono(Family.class)
                .doOnError(error -> log.error("Error al obtener la familia con ID: " + familyId, error));
    }

    public Mono<Set<Family>> getFamiliesByIds(Set<String> familyIds) {
        return Flux.fromIterable(familyIds)
                .flatMap(this::getFamilyById)
                .collect(Collectors.toSet());
    }

}
