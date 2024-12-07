package pe.edu.vallegrande.school_parents.application.util;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Family {

    @Field("_id")
    private String id;
    @Field("students")
    private Set<Student> students;
    @Field("mother")
    private Attorney mother;
    @Field("father")
    private Attorney father;

}
