package ma.nabil.WRM.service.impl;

import jakarta.transaction.Transactional;
import ma.nabil.WRM.dto.mapper.VisitMapper;
import ma.nabil.WRM.dto.request.VisitRequest;
import ma.nabil.WRM.dto.response.VisitResponse;
import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.entity.VisitId;
import ma.nabil.WRM.entity.Visitor;
import ma.nabil.WRM.entity.WaitingRoom;
import ma.nabil.WRM.enums.VisitorStatus;
import ma.nabil.WRM.exception.BusinessException;
import ma.nabil.WRM.exception.ResourceNotFoundException;
import ma.nabil.WRM.repository.VisitRepository;
import ma.nabil.WRM.repository.VisitorRepository;
import ma.nabil.WRM.repository.WaitingRoomRepository;
import ma.nabil.WRM.service.VisitService;
import ma.nabil.WRM.service.scheduling.SchedulingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VisitServiceImpl extends GenericServiceImpl<Visit, VisitRequest, VisitResponse, VisitId> implements VisitService {
    private final VisitMapper visitMapper;
    private final VisitorRepository visitorRepository;
    private final WaitingRoomRepository waitingRoomRepository;
    private final List<SchedulingStrategy> strategies;

    public VisitServiceImpl(
            VisitRepository visitRepository,
            VisitMapper visitMapper,
            VisitorRepository visitorRepository,
            WaitingRoomRepository waitingRoomRepository,
            List<SchedulingStrategy> strategies) {
        super(visitRepository);
        this.visitMapper = visitMapper;
        this.visitorRepository = visitorRepository;
        this.waitingRoomRepository = waitingRoomRepository;
        this.strategies = strategies;
    }

    @Override
    protected Visit toEntity(VisitRequest request) {
        Visit visit = visitMapper.toEntity(request);

        Visitor visitor = visitorRepository.findById(request.getVisitorId())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found"));

        WaitingRoom waitingRoom = waitingRoomRepository.findById(request.getWaitingRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Waiting room not found"));

        if (waitingRoom.getVisits().size() >= waitingRoom.getCapacity()) {
            throw new BusinessException("La salle d'attente est pleine");
        }

        visit.setId(new VisitId(request.getVisitorId(), request.getWaitingRoomId()));
        visit.setVisitor(visitor);
        visit.setWaitingRoom(waitingRoom);
        visit.setArrivalTime(LocalDateTime.now());
        visit.setStatus(VisitorStatus.WAITING);

        return visit;
    }

    @Override
    protected VisitResponse toResponse(Visit visit) {
        return visitMapper.toResponse(visit);
    }

    @Override
    protected void updateEntity(VisitRequest request, Visit visit) {
        visitMapper.updateEntity(request, visit);
    }

    @Override
    protected String getEntityName() {
        return "Visit";
    }

    @Override
    public VisitResponse create(VisitRequest request) {
        Visit visit = toEntity(request);
        visit = repository.save(visit);
        return toResponse(visit);
    }

    @Override
    public Page<VisitResponse> findAllByWaitingRoomId(Long waitingRoomId, Pageable pageable) {
        return ((VisitRepository) repository).findAllByWaitingRoom_Id(waitingRoomId, pageable)
                .map(this::toResponse);
    }

    @Override
    public VisitResponse updateStatus(VisitId id, VisitorStatus newStatus) {
        Visit visit = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));

        validateStatusTransition(visit.getStatus(), newStatus);

        visit.setStatus(newStatus);

        if (newStatus == VisitorStatus.IN_PROGRESS) {
            visit.setStartTime(LocalDateTime.now());
        } else if (newStatus == VisitorStatus.FINISHED || newStatus == VisitorStatus.CANCELLED) {
            visit.setEndTime(LocalDateTime.now());
        }

        return toResponse(repository.save(visit));
    }

    @Override
    public void delete(VisitId visitId) {
        deleteById(visitId);
    }

    @Override
    public void processNextVisit(Long waitingRoomId) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(waitingRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting room not found"));

        List<Visit> waitingVisits = ((VisitRepository) repository)
                .findAllByWaitingRoom_IdAndStatus(waitingRoomId, VisitorStatus.WAITING);

        if (waitingVisits.isEmpty()) {
            throw new BusinessException("Aucune visite en attente");
        }

        SchedulingStrategy strategy = strategies.stream()
                .filter(s -> s.getAlgorithm() == waitingRoom.getAlgorithm())
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        "Stratégie de planification non trouvée pour l'algorithme: " + waitingRoom.getAlgorithm()));

        Visit nextVisit = strategy.getNextVisit(waitingVisits);
        if (nextVisit != null) {
            updateStatus(nextVisit.getId(), VisitorStatus.IN_PROGRESS);
        }
    }

    private void validateStatusTransition(VisitorStatus currentStatus, VisitorStatus newStatus) {
        if (currentStatus == newStatus) {
            return;
        }

        switch (currentStatus) {
            case WAITING:
                if (newStatus != VisitorStatus.IN_PROGRESS && newStatus != VisitorStatus.CANCELLED) {
                    throw new BusinessException("Transition de statut invalide");
                }
                break;
            case IN_PROGRESS:
                if (newStatus != VisitorStatus.FINISHED && newStatus != VisitorStatus.CANCELLED) {
                    throw new BusinessException("Transition de statut invalide");
                }
                break;
            case FINISHED:
            case CANCELLED:
                throw new BusinessException("Impossible de changer le statut d'une visite terminée ou annulée");
            default:
                throw new BusinessException("Statut non reconnu");
        }
    }
}