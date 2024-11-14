package ma.nabil.WRM.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import ma.nabil.WRM.enums.WorkMode;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingRoomResponse {
    private Long id;
    private LocalDate date;
    private SchedulingAlgorithm algorithm;
    private Integer capacity;
    private WorkMode workMode;
    private List<VisitResponse> visits;
    private WaitingRoomStats stats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaitingRoomStats {
        private Double averageWaitingTime;
        private Double satisfactionRate;
        private Integer totalVisits;
        private Integer activeVisits;
    }
}