package ma.nabil.WRM.mapper;

import ma.nabil.WRM.config.GlobalMapperConfig;
import ma.nabil.WRM.dto.request.VisitRequest;
import ma.nabil.WRM.dto.response.VisitResponse;
import ma.nabil.WRM.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface VisitMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visitor", ignore = true)
    @Mapping(target = "waitingRoom", ignore = true)
    Visit toEntity(VisitRequest request);

    VisitResponse toResponse(Visit visit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visitor", ignore = true)
    @Mapping(target = "waitingRoom", ignore = true)
    void updateEntity(VisitRequest request, @MappingTarget Visit visit);
}