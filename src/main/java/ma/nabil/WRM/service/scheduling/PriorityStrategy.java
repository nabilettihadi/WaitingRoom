package ma.nabil.WRM.service.scheduling;

import ma.nabil.WRM.entity.Visit;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class PriorityStrategy implements SchedulingStrategy {
    @Override
    public Visit getNextVisit(List<Visit> waitingVisits) {
        return waitingVisits.stream()
                .max(Comparator
                        .comparing(Visit::getPriority)
                        .thenComparing(Visit::getArrivalTime))
                .orElse(null);
    }

    @Override
    public String getStrategyName() {
        return "PRIORITY";
    }
}