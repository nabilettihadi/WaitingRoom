package ma.nabil.WRM.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.nabil.WRM.enums.VisitorStatus;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {
    @EmbeddedId
    private VisitId id;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    private VisitorStatus status;

    private Integer priority;
    private Integer estimatedProcessingTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("visitorId")
    private Visitor visitor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("waitingRoomId")
    private WaitingRoom waitingRoom;
}