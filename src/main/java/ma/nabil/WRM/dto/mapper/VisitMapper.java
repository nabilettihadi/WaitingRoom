package ma.nabil.WRM.dto.mapper;

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
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    Visit toEntity(VisitRequest request);

    @Mapping(source = "visitor.id", target = "visitorId")
    @Mapping(source = "waitingRoom.id", target = "waitingRoomId")
    @Mapping(source = "visitor.firstName", target = "visitorFirstName")
    @Mapping(source = "visitor.lastName", target = "visitorLastName")
    VisitResponse toResponse(Visit visit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visitor", ignore = true)
    @Mapping(target = "waitingRoom", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    void updateEntity(VisitRequest request, @MappingTarget Visit visit);
}