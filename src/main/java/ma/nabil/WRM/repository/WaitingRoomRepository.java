package ma.nabil.WRM.repository;

import ma.nabil.WRM.entity.WaitingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WaitingRoomRepository extends JpaRepository<WaitingRoom, Long> {
    boolean existsByDate(LocalDate date);
}
