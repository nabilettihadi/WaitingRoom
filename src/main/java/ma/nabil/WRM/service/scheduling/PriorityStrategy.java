package ma.nabil.WRM.service.scheduling;

import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class PriorityStrategy implements SchedulingStrategy {
    @Override
    public Visit getNextVisit(List<Visit> waitingVisits) {
        return waitingVisits.stream()
                .min(Comparator
                        .comparing(Visit::getPriority)
                        .thenComparing(Visit::getArrivalTime))
                .orElse(null);
    }

    @Override
    public SchedulingAlgorithm getAlgorithm() {
        return SchedulingAlgorithm.PRIORITY;
    }
}