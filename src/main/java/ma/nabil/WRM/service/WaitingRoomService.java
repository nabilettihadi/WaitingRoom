package ma.nabil.WRM.service;

import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.dto.response.WaitingRoomResponse;
import ma.nabil.WRM.enums.SchedulingAlgorithm;

public interface WaitingRoomService extends GenericService<WaitingRoomRequest, WaitingRoomResponse, Long> {
    WaitingRoomResponse updateAlgorithm(Long id, SchedulingAlgorithm algorithm);

    WaitingRoomResponse getStats(Long id);
}