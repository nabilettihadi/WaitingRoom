package ma.nabil.WRM.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class VisitRequest {
    @NotNull(message = "Visitor ID is required")
    private Long visitorId;

    @NotNull(message = "Waiting room ID is required")
    private Long waitingRoomId;

    @NotNull(message = "Priority is required")
    @Positive(message = "Priority must be positive")
    private Integer priority;

    @NotNull(message = "Estimated processing time is required")
    @Positive(message = "Estimated processing time must be positive")
    private Integer estimatedProcessingTime;
}