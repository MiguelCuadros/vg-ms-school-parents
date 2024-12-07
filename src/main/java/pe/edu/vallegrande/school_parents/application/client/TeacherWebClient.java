package pe.edu.vallegrande.school_parents.application.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.school_parents.application.util.Teacher;
import reactor.core.publisher.Mono;

@Service
public class TeacherWebClient {

    private final WebClient.Builder webClientBuilder;
    private final String teacherServiceUrl;

    @Autowired
    public TeacherWebClient(WebClient.Builder webClientBuilder, @Value("${spring.microservices.teacher.url}") String teacherServiceUrl) {
        this.webClientBuilder = webClientBuilder;
        this.teacherServiceUrl = teacherServiceUrl;
    }

    public Mono<Teacher> getTeacherById(String teacherId) {
        return webClientBuilder.build()
                .get()
                .uri(teacherServiceUrl + "/{teacherId}", teacherId)
                .retrieve()
                .bodyToMono(Teacher.class);
    }

}
