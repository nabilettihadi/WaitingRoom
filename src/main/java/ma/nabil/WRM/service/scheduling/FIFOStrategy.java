package ma.nabil.WRM.service.scheduling;

import ma.nabil.WRM.entity.Visit;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class FIFOStrategy implements SchedulingStrategy {
    @Override
    public Visit getNextVisit(List<Visit> waitingVisits) {
        return waitingVisits.stream()
                .min(Comparator.comparing(Visit::getArrivalTime))
                .orElse(null);
    }

    @Override
    public String getStrategyName() {
        return "FIFO";
    }
}
