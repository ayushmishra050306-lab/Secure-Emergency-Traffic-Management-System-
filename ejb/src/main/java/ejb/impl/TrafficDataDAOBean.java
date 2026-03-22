package ejb.impl;

import db.DbConnectionManager;
import dto.TrafficDataDTO;
import ejb.remote.TrafficData;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class TrafficDataDAOBean implements TrafficData {

    private static final String QUERY3 = "INSERT INTO traffic_data(device_type,vehicle_speed,trafficLightStatus,latitude,longitude,dateTimestamp) VALUES(?,?,?,?,?,?)";
    private static final String QUERY4 = "SELECT * FROM traffic_data";
    private DbConnectionManager dbConnectionManager;
    private SimpleDateFormat inputFormat;
    private SimpleDateFormat outputFormat;

    @PostConstruct
    public void init() {
        try {
            dbConnectionManager = DbConnectionManager.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void saveTrafficData(LinkedHashMap trafficData) {
        try (Connection conn = dbConnectionManager.getConnection();
             PreparedStatement stmt3 = conn.prepareStatement(QUERY3)) {
            stmt3.setString(1, trafficData.get("deviceType").toString());
            stmt3.setDouble(2, Double.parseDouble(trafficData.get("vehicleSpeed").toString()));
            stmt3.setString(3, trafficData.get("trafficLightStatus").toString());
            stmt3.setDouble(4, Double.parseDouble(trafficData.get("latitude").toString()));
            stmt3.setDouble(5, Double.parseDouble(trafficData.get("longitude").toString()));
            stmt3.setTimestamp(6, Timestamp.valueOf(outputFormat.format(inputFormat.parse(trafficData.get("timestamp").toString()))));
            stmt3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

//    public List<TrafficDataDTO> getTrafficData() {
    public List<Map> getTrafficData() {

//        List<TrafficDataDTO> trafficDataList = new ArrayList<>();

        List<Map> trafficData =new ArrayList<>();

        try (Connection conn = dbConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QUERY4);
             ResultSet trafficDataResultSet = stmt.executeQuery()) {

//            int rowCount = 0;
//            while (trafficDataResultSet.next()) {
//                rowCount++;
//                // Process each row of the result set as needed
//            }
//            System.out.println("Number of rows in ResultSet: " + rowCount);

//            while (trafficDataResultSet.next()) {
//                // Create a new TrafficDataCollector object
//                TrafficDataDTO dto = new TrafficDataDTO();
//                dto.setDeviceType(trafficDataResultSet.getString("device_type"));
//                dto.setVehicleSpeed(trafficDataResultSet.getDouble("vehicle_speed"));
//                dto.setTrafficLightStatus(trafficDataResultSet.getString("trafficLightStatus"));
//                dto.setLatitude(trafficDataResultSet.getDouble("latitude"));
//                dto.setLongitude(trafficDataResultSet.getDouble("longitude"));
//                dto.setDateTimestamp(Date.valueOf(inputFormat.format(outputFormat.parse(String.valueOf(trafficDataResultSet.getTimestamp("dateTimestamp"))))));
//                trafficDataList.add(dto);
//            }

            try {
                while (trafficDataResultSet.next()) {
                    // Create a new TrafficDataCollector object
                    Map<String,Object> trafficDataMap= new LinkedHashMap<>();
                    // Set values from the ResultSet to the TrafficDataCollector object
                    trafficDataMap.put("device_type",trafficDataResultSet.getString("device_type"));
                    trafficDataMap.put("vehicle_speed",trafficDataResultSet.getDouble("vehicle_speed"));
                    trafficDataMap.put("traffic_light_status",trafficDataResultSet.getString("trafficLightStatus"));
                    trafficDataMap.put("latitude",trafficDataResultSet.getDouble("latitude"));
                    trafficDataMap.put("longitude",trafficDataResultSet.getDouble("longitude"));
                    trafficDataMap.put("timestamp",trafficDataResultSet.getTimestamp("dateTimestamp").toLocalDateTime());

                    double waitTime=0;
                    if(trafficDataResultSet.getString("trafficLightStatus").equals("RED")){
                        waitTime=0.0167;//1 minute
                    }
                    trafficDataMap.put("time_travel_index", ((1/trafficDataResultSet.getDouble("vehicle_speed"))+waitTime/(1.0 /50)));

                    // Add the TrafficDataCollector object to the trafficData list
                    trafficData.add(trafficDataMap);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("traffic data list size"+trafficData.size());

            return trafficData;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
//        catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

    }
}
