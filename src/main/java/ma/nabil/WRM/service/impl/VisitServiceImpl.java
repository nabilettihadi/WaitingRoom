package ma.nabil.WRM.service.impl;

import lombok.RequiredArgsConstructor;
import ma.nabil.WRM.dto.request.VisitRequest;
import ma.nabil.WRM.dto.response.VisitResponse;
import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.entity.VisitId;
import ma.nabil.WRM.entity.Visitor;
import ma.nabil.WRM.entity.WaitingRoom;
import ma.nabil.WRM.enums.VisitorStatus;
import ma.nabil.WRM.exception.BusinessException;
import ma.nabil.WRM.exception.ResourceNotFoundException;
import ma.nabil.WRM.mapper.VisitMapper;
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
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final VisitorRepository visitorRepository;
    private final WaitingRoomRepository waitingRoomRepository;
    private final List<SchedulingStrategy> strategies;

    @Override
    public VisitResponse create(VisitRequest request) {
        Visitor visitor = visitorRepository.findById(request.getVisitorId())
                .orElseThrow(() -> new ResourceNotFoundException("Visiteur non trouvé"));

        WaitingRoom waitingRoom = waitingRoomRepository.findById(request.getWaitingRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Salle d'attente non trouvée"));

        if (isWaitingRoomFull(waitingRoom)) {
            throw new BusinessException("La salle d'attente est pleine");
        }

        try {
            Visit visit = visitMapper.toEntity(request);
            visit.setId(new VisitId(
                    request.getVisitorId(),
                    request.getWaitingRoomId()
            ));
            visit.setVisitor(visitor);
            visit.setWaitingRoom(waitingRoom);
            visit.setArrivalTime(LocalDateTime.now());
            visit.setStatus(VisitorStatus.WAITING);

            Visit savedVisit = visitRepository.save(visit);
            return visitMapper.toResponse(savedVisit);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la visite", e);
        }
    }

    private boolean isWaitingRoomFull(WaitingRoom waitingRoom) {
        long activeVisits = visitRepository.countByWaitingRoomIdAndStatusNot(
                waitingRoom.getId(),
                VisitorStatus.FINISHED
        );
        return activeVisits >= waitingRoom.getCapacity();
    }

    @Override
    public VisitResponse findById(VisitId id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));
        return visitMapper.toResponse(visit);
    }

    @Override
    public Page<VisitResponse> findAll(Pageable pageable) {
        return visitRepository.findAll(pageable).map(visitMapper::toResponse);
    }

    @Override
    public VisitResponse update(VisitId id, VisitRequest request) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));

        visitMapper.updateEntity(request, visit);
        visit = visitRepository.save(visit);
        return visitMapper.toResponse(visit);
    }

    @Override
    public void delete(VisitId id) {
        if (!visitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Visit not found");
        }
        visitRepository.deleteById(id);
    }

    @Override
    public VisitResponse updateStatus(VisitId visitId, VisitorStatus newStatus) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found"));

        validateStatusTransition(visit.getStatus(), newStatus);

        visit.setStatus(newStatus);

        if (newStatus == VisitorStatus.IN_PROGRESS) {
            visit.setStartTime(LocalDateTime.now());
        } else if (newStatus == VisitorStatus.FINISHED || newStatus == VisitorStatus.CANCELLED) {
            visit.setEndTime(LocalDateTime.now());
        }

        return visitMapper.toResponse(visitRepository.save(visit));
    }

    @Override
    public Page<VisitResponse> findAllByWaitingRoomId(Long waitingRoomId, Pageable pageable) {
        return visitRepository.findAllByWaitingRoom_Id(waitingRoomId, pageable).map(visitMapper::toResponse);
    }

    @Override
    public void processNextVisit(Long waitingRoomId) {
        WaitingRoom waitingRoom = waitingRoomRepository.findById(waitingRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Salle d'attente non trouvée"));

        long activeVisits = visitRepository.countByWaitingRoomIdAndStatus(
                waitingRoomId,
                VisitorStatus.IN_PROGRESS
        );

        if (activeVisits > 0) {
            throw new BusinessException("Il y a déjà une visite en cours");
        }

        List<Visit> waitingVisits = visitRepository
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