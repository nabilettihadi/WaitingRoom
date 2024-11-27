package ma.nabil.WRM.service.impl;

import lombok.RequiredArgsConstructor;
import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.dto.response.WaitingRoomResponse;
import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.entity.WaitingRoom;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import ma.nabil.WRM.enums.VisitorStatus;
import ma.nabil.WRM.enums.WorkMode;
import ma.nabil.WRM.exception.ResourceNotFoundException;
import ma.nabil.WRM.mapper.WaitingRoomMapper;
import ma.nabil.WRM.repository.WaitingRoomRepository;
import ma.nabil.WRM.service.WaitingRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingRoomServiceImpl implements WaitingRoomService {
    private final WaitingRoomRepository waitingRoomRepository;
    private final WaitingRoomMapper waitingRoomMapper;

    @Override
    public WaitingRoomResponse create(WaitingRoomRequest request) {
        WaitingRoom waitingRoom = waitingRoomMapper.toEntity(request);
        waitingRoom = waitingRoomRepository.save(waitingRoom);
        return waitingRoomMapper.toResponse(waitingRoom);
    }

    @Override
    public WaitingRoomResponse findById(Long id) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting Room not found"));
        return waitingRoomMapper.toResponse(waitingRoom);
    }

    @Override
    public Page<WaitingRoomResponse> findAll(Pageable pageable) {
        return waitingRoomRepository.findAll(pageable).map(waitingRoomMapper::toResponse);
    }

    @Override
    public WaitingRoomResponse update(Long id, WaitingRoomRequest request) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting Room not found"));
        waitingRoomMapper.updateEntity(request, waitingRoom);
        waitingRoom = waitingRoomRepository.save(waitingRoom);
        return waitingRoomMapper.toResponse(waitingRoom);
    }

    @Override
    public void delete(Long id) {
        if (!waitingRoomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Waiting Room not found");
        }
        waitingRoomRepository.deleteById(id);
    }

    @Override
    public WaitingRoomResponse updateAlgorithm(Long id, SchedulingAlgorithm algorithm) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting Room not found"));
        waitingRoom.setAlgorithm(algorithm);
        waitingRoom = waitingRoomRepository.save(waitingRoom);
        return waitingRoomMapper.toResponse(waitingRoom);
    }

    @Override
    public WaitingRoomResponse updateWorkMode(Long id, WorkMode workMode) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting Room not found"));
        waitingRoom.setWorkMode(workMode);
        waitingRoom = waitingRoomRepository.save(waitingRoom);
        return waitingRoomMapper.toResponse(waitingRoom);
    }

    @Override
    public WaitingRoomResponse.WaitingRoomStats getStats(Long id) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting Room not found"));

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