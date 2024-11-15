package ma.nabil.WRM.dto.mapper;

import ma.nabil.WRM.config.GlobalMapperConfig;
import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.dto.response.VisitorResponse;
import ma.nabil.WRM.entity.Visitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface VisitorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visits", ignore = true)
    Visitor toEntity(VisitorRequest request);

    VisitorResponse toResponse(Visitor visitor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visits", ignore = true)
    void updateEntity(VisitorRequest request, @MappingTarget Visitor visitor);
}