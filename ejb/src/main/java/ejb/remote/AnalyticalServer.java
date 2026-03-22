package ejb.remote;

import jakarta.ejb.Remote;

import java.time.LocalDateTime;
import java.util.*;

@Remote
public interface AnalyticalServer {

    public double calculateAverageVehicleSpeed();

    public Map<LocalDateTime, Double> identifyTrafficPatterns();
    public Map<LocalDateTime, Double> trafficDensity();
    public List<Map<LocalDateTime, Double>> mobilityOfTheArea();

}
