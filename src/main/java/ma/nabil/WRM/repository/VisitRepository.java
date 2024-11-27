package ma.nabil.WRM.repository;

import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.entity.VisitId;
import ma.nabil.WRM.enums.VisitorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, VisitId> {
    Page<Visit> findAllByWaitingRoom_Id(Long waitingRoomId, Pageable pageable);

    long countByWaitingRoomIdAndStatus(Long waitingRoomId, VisitorStatus status);

    List<Visit> findAllByWaitingRoom_IdAndStatus(Long waitingRoomId, VisitorStatus status);

    long countByWaitingRoomIdAndStatusNot(Long waitingRoomId, VisitorStatus status);
}
