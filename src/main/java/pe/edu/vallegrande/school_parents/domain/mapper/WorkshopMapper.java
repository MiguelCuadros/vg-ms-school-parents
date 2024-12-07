package pe.edu.vallegrande.school_parents.domain.mapper;

import org.mapstruct.*;
import pe.edu.vallegrande.school_parents.domain.dto.WorkshopRequest;
import pe.edu.vallegrande.school_parents.domain.dto.WorkshopResponse;
import pe.edu.vallegrande.school_parents.domain.model.Workshop;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkshopMapper {

    WorkshopResponse toWorkshopResponse(Workshop workshop);
    @Mappings({
            @Mapping(target = "teacher", ignore = true),
            @Mapping(target = "sessions", ignore = true),
            @Mapping(target = "families", ignore = true)
    })
    Workshop toWorkshop(WorkshopRequest workshopRequest);
    @Mappings({
            @Mapping(target = "teacher", ignore = true),
            @Mapping(target = "sessions", ignore = true),
            @Mapping(target = "families", ignore = true)
    })
    void updateWorkshopFromRequest(@MappingTarget Workshop workshop, WorkshopRequest workshopRequest);

}
