package pe.edu.vallegrande.school_parents.domain.dto;

import lombok.*;
import pe.edu.vallegrande.school_parents.domain.model.Session;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkshopRequest {

    private String name;
    private String grade;
    private String section;
    private String level;
    private String teacher;
    private Set<String> families;
    private List<Session> sessions;

}
