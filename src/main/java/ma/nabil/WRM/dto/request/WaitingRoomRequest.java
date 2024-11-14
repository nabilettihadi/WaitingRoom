package ma.nabil.WRM.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import ma.nabil.WRM.enums.WorkMode;

import java.time.LocalDate;

@Data
public class WaitingRoomRequest {
    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Algorithm is required")
    private SchedulingAlgorithm algorithm;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @NotNull(message = "Work mode is required")
    private WorkMode workMode;
}