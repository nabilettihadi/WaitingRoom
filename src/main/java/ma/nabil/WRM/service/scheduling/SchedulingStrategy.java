package ma.nabil.WRM.service.scheduling;

import ma.nabil.WRM.entity.Visit;

import java.util.List;

public interface SchedulingStrategy {
    Visit getNextVisit(List<Visit> waitingVisits);
    String getStrategyName();
}