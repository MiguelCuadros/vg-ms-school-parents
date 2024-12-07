package pe.edu.vallegrande.school_parents.domain.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;
import pe.edu.vallegrande.school_parents.application.util.Family;
import pe.edu.vallegrande.school_parents.application.util.Teacher;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "workshop")
public class Workshop {

    @MongoId
    @Field("_id")
    private String id;
    @Field("name")
    private String name;
    @Field("grade")
    private String grade;
    @Field("section")
    private String section;
    @Field("level")
    private String level;
    @Field("teacher")
    private Teacher teacher;
    @Field("families")
    private Set<Family> families;
    @Field("sessions")
    private List<Session> sessions;
    @Field("creation_date")
    private LocalDateTime creationDate=LocalDateTime.now();
    @Field("write_date")
    private LocalDateTime writeDate=LocalDateTime.now();
    @Field("status")
    private String status="A";

}
