package ma.nabil.WRM.dto;

import lombok.Data;
import ma.nabil.WRM.enums.VisitorStatus;

import java.time.LocalDateTime;

@Data
public class VisitorDto {
    private Long id;
    private String name;
    private LocalDateTime arrivalTime;
    private VisitorStatus status;
    private Integer priority;
    private Integer estimatedProcessingTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}