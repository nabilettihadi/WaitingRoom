package ma.nabil.WRM.service.impl;

import jakarta.transaction.Transactional;
import ma.nabil.WRM.dto.mapper.WaitingRoomMapper;
import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.dto.response.WaitingRoomResponse;
import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.entity.WaitingRoom;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import ma.nabil.WRM.enums.VisitorStatus;
import ma.nabil.WRM.exception.ResourceNotFoundException;
import ma.nabil.WRM.repository.WaitingRoomRepository;
import ma.nabil.WRM.service.WaitingRoomService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Transactional
public class WaitingRoomServiceImpl extends GenericServiceImpl<WaitingRoom, WaitingRoomRequest, WaitingRoomResponse, Long> implements WaitingRoomService {
    private final WaitingRoomMapper waitingRoomMapper;

    public WaitingRoomServiceImpl(WaitingRoomRepository waitingRoomRepository, WaitingRoomMapper waitingRoomMapper) {
        super(waitingRoomRepository);
        this.waitingRoomMapper = waitingRoomMapper;
    }

    @Override
    protected WaitingRoom toEntity(WaitingRoomRequest request) {
        return waitingRoomMapper.toEntity(request);
    }

    @Override
    protected WaitingRoomResponse toResponse(WaitingRoom waitingRoom) {
        WaitingRoomResponse response = waitingRoomMapper.toResponse(waitingRoom);
        response.setStats(calculateStats(waitingRoom));
        return response;
    }

    @Override
    protected void updateEntity(WaitingRoomRequest request, WaitingRoom waitingRoom) {
        waitingRoomMapper.updateEntity(request, waitingRoom);
    }

    @Override
    protected String getEntityName() {
        return "WaitingRoom";
    }

    @Override
    public WaitingRoomResponse updateAlgorithm(Long id, SchedulingAlgorithm algorithm) {
        WaitingRoom waitingRoom = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting room not found"));
        
        waitingRoom.setAlgorithm(algorithm);
        return toResponse(repository.save(waitingRoom));
    }

    @Override
    public WaitingRoomResponse getStats(Long id) {
        WaitingRoom waitingRoom = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting room not found"));
                
        return toResponse(waitingRoom);
    }

    private WaitingRoomResponse.WaitingRoomStats calculateStats(WaitingRoom waitingRoom) {
        List<Visit> visits = waitingRoom.getVisits();
        
        long totalVisits = visits.size();
        long activeVisits = visits.stream()
                .filter(v -> v.getStatus() == VisitorStatus.WAITING || v.getStatus() == VisitorStatus.IN_PROGRESS)
                .count();
                
        double avgWaitingTime = visits.stream()
                .filter(v -> v.getStartTime() != null)
                .mapToLong(v -> Duration.between(v.getArrivalTime(), v.getStartTime()).toMinutes())
                .average()
                .orElse(0.0);
                
        long satisfiedVisits = visits.stream()
                .filter(v -> v.getStatus() == VisitorStatus.FINISHED)
                .filter(v -> Duration.between(v.getArrivalTime(), v.getEndTime()).toMinutes() <= v.getEstimatedProcessingTime() * 1.5)
                .count();
                
        double satisfactionRate = totalVisits > 0 ? (double) satisfiedVisits / totalVisits * 100 : 0.0;
        
        return WaitingRoomResponse.WaitingRoomStats.builder()
                .averageWaitingTime(avgWaitingTime)
                .satisfactionRate(satisfactionRate)
                .totalVisits((int) totalVisits)
                .activeVisits((int) activeVisits)
                .build();
    }
}