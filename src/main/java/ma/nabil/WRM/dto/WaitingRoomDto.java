package ma.nabil.WRM.dto;

import lombok.Data;
import ma.nabil.WRM.enums.SchedulingAlgorithm;

import java.time.LocalDate;
import java.util.List;

@Data
public class WaitingRoomDto {
    private Long id;
    private LocalDate date;
    private SchedulingAlgorithm algorithm;
    private List<VisitorDto> visitors;
    private WaitingRoomStats stats;

    @Data
    public static class WaitingRoomStats {
        private Double averageWaitingTime;
        private Double satisfactionRate;
        private Integer totalVisitors;
        private Integer activeVisitors;
    }
}