package pe.edu.vallegrande.school_parents.domain.dto;

import lombok.*;
import pe.edu.vallegrande.school_parents.application.util.Family;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

    private String id;
    private String name;
    private String description;
    private LocalDate date;
    private Set<Family> familiesPresent;
    private Set<Family> familiesAbsent;
    private LocalDateTime creationDate;
    private LocalDateTime writeDate;

}
