package pe.edu.vallegrande.school_parents.domain.mapper;

import org.mapstruct.*;
import pe.edu.vallegrande.school_parents.domain.dto.SessionRequest;
import pe.edu.vallegrande.school_parents.domain.dto.SessionResponse;
import pe.edu.vallegrande.school_parents.domain.model.Session;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SessionMapper {

    SessionResponse toSessionResponse(Session session);
    @Mappings({
            @Mapping(target = "familiesPresent", ignore = true),
            @Mapping(target = "familiesAbsent", ignore = true)
    })
    Session toSession(SessionRequest sessionRequest);
    @Mappings({
            @Mapping(target = "familiesPresent", ignore = true),
            @Mapping(target = "familiesAbsent", ignore = true)
    })
    void updateSession(@MappingTarget Session session, SessionRequest sessionRequest);
    @Mapping(target = "familiesPresent", ignore = true)
    SessionRequest toSessionRequest(Session session);

}
