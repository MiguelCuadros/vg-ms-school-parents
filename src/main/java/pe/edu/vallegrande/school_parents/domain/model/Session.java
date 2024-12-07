package pe.edu.vallegrande.school_parents.domain.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;
import pe.edu.vallegrande.school_parents.application.util.Family;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "session")
public class Session {

    @MongoId
    @Field("_id")
    private String id;
    @Field("workshop_id")
    private String workshopId;
    @Field("name")
    private String name;
    @Field("description")
    private String description;
    @Field("date")
    private LocalDate date;
    @Field("family_present")
    private Set<Family> familiesPresent;
    @Field("family_absent")
    private Set<Family> familiesAbsent;
    @Field("creation_date")
    private LocalDateTime creationDate=LocalDateTime.now();
    @Field("write_date")
    private LocalDateTime writeDate=LocalDateTime.now();
    @Field("status")
    private String status="A"; // A: Active, I: Inactive, T: Terminated

}
