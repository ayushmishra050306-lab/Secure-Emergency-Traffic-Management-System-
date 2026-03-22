package ejb.remote;

import jakarta.ejb.Remote;

import java.time.LocalTime;
import java.util.Date;

@Remote
public interface TrafficEnvironment {

    void updatePeakHours();

    void updateTrafficDensity(Date dateTimestamp);

    double getTrafficDensity(Date dateTimestamp);

    private boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
        return false;
    }
}
