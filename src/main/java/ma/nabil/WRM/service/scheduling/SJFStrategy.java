package ma.nabil.WRM.service.scheduling;

import ma.nabil.WRM.entity.Visit;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class SJFStrategy implements SchedulingStrategy {
    @Override
    public Visit getNextVisit(List<Visit> waitingVisits) {
        return waitingVisits.stream()
                .min(Comparator.comparing(Visit::getEstimatedProcessingTime))
                .orElse(null);
    }

    @Override
    public String getStrategyName() {
        return "SJF";
    }
}
