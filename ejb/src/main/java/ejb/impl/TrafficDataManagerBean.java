package ejb.impl;

import db.DbConnectionManager;
import dto.TrafficDataDTO;
import ejb.remote.TrafficData;
import ejb.remote.TrafficDataManager;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.DependsOn;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
//@DependsOn("TrafficDataDAOBean")
public class TrafficDataManagerBean implements TrafficDataManager {

    @EJB
    TrafficData trafficDatad;

    private static final String QUERY1 = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
    private static final String QUERY2 = "CREATE TABLE IF NOT EXISTS traffic_data (id INTEGER PRIMARY KEY AUTOINCREMENT, device_type VARCHAR(10), vehicle_speed DOUBLE, trafficLightStatus VARCHAR(5), latitude DOUBLE,longitude DOUBLE,dateTimestamp DATETIME )";
    private static final String QUERY3 = "INSERT INTO traffic_data(device_type,vehicle_speed,trafficLightStatus,latitude,longitude,dateTimestamp) VALUES(?,?,?,?,?,?)";
    private DbConnectionManager dbConnectionManager;
    private boolean isTableExists;

    @PostConstruct
    public void init() {
        try {
            dbConnectionManager = DbConnectionManager.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        checkTable();
    }

    private void checkTable() {
        try (Connection conn = dbConnectionManager.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(QUERY1)) {
            stmt1.setString(1, "traffic_data");
            try (ResultSet rs = stmt1.executeQuery()) {
                isTableExists = rs.next();
                if (!isTableExists) {
                    try (PreparedStatement stmt2 = conn.prepareStatement(QUERY2)) {
                        stmt2.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTrafficData(LinkedHashMap trafficData) {

        trafficDatad.saveTrafficData(trafficData);

    }


    public List<Map> getTrafficData() {
        return trafficDatad.getTrafficData();
//        return null;
    }

}
