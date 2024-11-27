package ma.nabil.WRM.service;

import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.dto.response.WaitingRoomResponse;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import ma.nabil.WRM.enums.WorkMode;

public interface WaitingRoomService extends GenericService<WaitingRoomResponse, WaitingRoomRequest, Long> {
    WaitingRoomResponse updateAlgorithm(Long id, SchedulingAlgorithm algorithm);

    WaitingRoomResponse updateWorkMode(Long id, WorkMode workMode);

    WaitingRoomResponse.WaitingRoomStats getStats(Long id);
}