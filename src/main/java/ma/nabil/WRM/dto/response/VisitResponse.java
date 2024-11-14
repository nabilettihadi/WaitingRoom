package ma.nabil.WRM.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.nabil.WRM.enums.VisitorStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitResponse {
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