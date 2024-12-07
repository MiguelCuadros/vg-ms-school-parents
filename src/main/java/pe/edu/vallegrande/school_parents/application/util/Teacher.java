package pe.edu.vallegrande.school_parents.application.util;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Field("_id")
    private String id;
    @Field("first_name")
    private String firstName;
    @Field("last_name")
    private String lastName;
    @Field("cellphone")
    private String cellPhone;

}
