package ma.nabil.WRM.service.scheduling;

import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.enums.SchedulingAlgorithm;

import java.util.List;

public interface SchedulingStrategy {
    Visit getNextVisit(List<Visit> waitingVisits);

    SchedulingAlgorithm getAlgorithm();
}