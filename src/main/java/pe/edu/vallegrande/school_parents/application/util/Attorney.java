package pe.edu.vallegrande.school_parents.application.util;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Attorney {

    @Field("_id")
    private String idAttorney;
    @Field("first_name")
    private String names;
    @Field("last_name")
    private String surnames;
    @Field("cellphone")
    private String cellphone;
    @Field("document_number")
    private String documentNumber;

}
