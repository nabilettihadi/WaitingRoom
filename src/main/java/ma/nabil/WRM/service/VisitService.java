package ma.nabil.WRM.service;

import ma.nabil.WRM.dto.request.VisitRequest;
import ma.nabil.WRM.dto.response.VisitResponse;
import ma.nabil.WRM.entity.VisitId;
import ma.nabil.WRM.enums.VisitorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VisitService extends GenericService<VisitResponse, VisitRequest, VisitId> {
    VisitResponse updateStatus(VisitId visitId, VisitorStatus status);

    Page<VisitResponse> findAllByWaitingRoomId(Long waitingRoomId, Pageable pageable);

    void processNextVisit(Long waitingRoomId);
}