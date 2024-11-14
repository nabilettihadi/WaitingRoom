package ma.nabil.WRM.dto;

import lombok.Data;
import ma.nabil.WRM.enums.VisitorStatus;

import java.time.LocalDateTime;

@Data
public class VisitDto {
    private Long visitorId;
    private Long waitingRoomId;
    private LocalDateTime arrivalTime;
    private VisitorStatus status;
    private Integer priority;
    private Integer estimatedProcessingTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String visitorFirstName;
    private String visitorLastName;
}