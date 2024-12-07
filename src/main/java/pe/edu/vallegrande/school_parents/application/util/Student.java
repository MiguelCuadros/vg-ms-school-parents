package pe.edu.vallegrande.school_parents.application.util;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Field("_id")
    private String idStudent;
    @Field("first_name")
    private String name;
    @Field("last_name")
    private String lastName;
    @Field("document_number")
    private String documentNumber;

}
