package ma.nabil.WRM.service.scheduling;

import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class SJFStrategy implements SchedulingStrategy {
    @Override
    public Visit getNextVisit(List<Visit> waitingVisits) {
        return waitingVisits.stream()
                .min(Comparator
                        .comparing(Visit::getEstimatedProcessingTime)
                        .thenComparing(Visit::getArrivalTime))
                .orElse(null);
    }

    @Override
    public SchedulingAlgorithm getAlgorithm() {
        return SchedulingAlgorithm.SJF;
    }
}