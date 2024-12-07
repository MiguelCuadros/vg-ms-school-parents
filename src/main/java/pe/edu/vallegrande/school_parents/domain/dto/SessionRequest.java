package pe.edu.vallegrande.school_parents.domain.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionRequest {

    private String name;
    private String description;
    private LocalDate date;
    private Set<String> familiesPresent;

}
