package ma.nabil.WRM.dto;

import lombok.Data;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import ma.nabil.WRM.enums.WorkMode;

import java.time.LocalDate;
import java.util.List;

@Data
public class WaitingRoomDto {
    private Long id;
    private LocalDate date;
    private SchedulingAlgorithm algorithm;
    private Integer capacity;
    private WorkMode workMode;
    private List<VisitDto> visits;
    private WaitingRoomStats stats;

    @Data
    public static class WaitingRoomStats {
        private Double averageWaitingTime;
        private Double satisfactionRate;
        private Integer totalVisits;
        private Integer activeVisits;
    }
}