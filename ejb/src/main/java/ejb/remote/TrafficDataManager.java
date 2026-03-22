package ejb.remote;

import dto.TrafficDataDTO;
import jakarta.ejb.Remote;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Remote
public interface TrafficDataManager {
    private void checkTable() {}

    public void saveTrafficData(LinkedHashMap trafficData);

    public List<Map> getTrafficData();
}
