package ejb.remote;

import jakarta.ejb.Remote;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Remote
public interface TrafficData{
    public void saveTrafficData(LinkedHashMap trafficData);

//    public List<TrafficDataDTO> getTrafficData();
    public List<Map> getTrafficData();
}
