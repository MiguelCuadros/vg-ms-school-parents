package pe.edu.vallegrande.school_parents.domain.dto;

import lombok.*;
import pe.edu.vallegrande.school_parents.application.util.Family;
import pe.edu.vallegrande.school_parents.application.util.Teacher;
import pe.edu.vallegrande.school_parents.domain.model.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkshopResponse {

    private String id;
    private String name;
    private String grade;
    private String section;
    private String level;
    private Teacher teacher;
    private Set<Family> families;
    private List<Session> sessions;
    private LocalDateTime creationDate;
    private LocalDateTime writeDate;

}
