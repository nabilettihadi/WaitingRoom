package ma.nabil.WRM.dto.mapper;

import ma.nabil.WRM.config.GlobalMapperConfig;
import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.dto.response.WaitingRoomResponse;
import ma.nabil.WRM.entity.WaitingRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface WaitingRoomMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visits", ignore = true)
    WaitingRoom toEntity(WaitingRoomRequest request);

    WaitingRoomResponse toResponse(WaitingRoom waitingRoom);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visits", ignore = true)
    void updateEntity(WaitingRoomRequest request, @MappingTarget WaitingRoom waitingRoom);
}