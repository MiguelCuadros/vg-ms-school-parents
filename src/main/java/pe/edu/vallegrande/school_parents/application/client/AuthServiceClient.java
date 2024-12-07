package pe.edu.vallegrande.school_parents.application.client;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.school_parents.application.util.TokenValidation;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder.baseUrl("https://profound-lark-web-maria-enriqueta-dominicci-7da37999.koyeb.app/firebase-users");
    }

    public Mono<TokenValidation> validateToken(String token) {
        return webClientBuilder.build()
                .get()
                .uri("/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(TokenValidation.class);
    }

}
