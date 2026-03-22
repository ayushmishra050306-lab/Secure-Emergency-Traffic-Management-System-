package ejb.impl;

import ejb.remote.AnalyticalServer;
import ejb.remote.TrafficData;
import ejb.remote.TrafficDataManager;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Lock(LockType.READ)

public class AnalyticalServerBean implements AnalyticalServer {

    @EJB
    TrafficDataManager trafficDataManager;

    private List<Map> trafficDataList = new ArrayList<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Map<LocalDateTime, List<Map>> groupedData = new TreeMap<>();


    @PostConstruct
    public void init() {
        receiveData();
        scheduleTrafficDataUpdate();
    }


    private void scheduleTrafficDataUpdate() {
        scheduler.scheduleAtFixedRate(this::receiveData, 0, 10, TimeUnit.MINUTES);
    }


    private void receiveData() {
        trafficDataList = trafficDataManager.getTrafficData();
        groupDataByTimeInterval();
    }


    private void groupDataByTimeInterval() {
        for (Map<String, Object> data : trafficDataList) {
            LocalDateTime timeInterval = (LocalDateTime) data.get("timestamp");
            groupedData.computeIfAbsent(timeInterval, k -> new ArrayList<>()).add(data);
        }
    }


    @Lock(LockType.WRITE)
    public double calculateAverageVehicleSpeed() {
        List<Map> tempTrafficDataList = trafficDataList;
        double totalSpeed = 0;
        for (Map data : tempTrafficDataList) {
            totalSpeed += Double.parseDouble(data.get("vehicle_speed").toString());
        }
        return totalSpeed / tempTrafficDataList.size();
    }


    @Lock(LockType.WRITE)
    public Map<LocalDateTime, Double> identifyTrafficPatterns() {

//        keep this because thread can change the map during calculation

        Map<LocalDateTime, List<Map>> tempGroupedData = groupedData;


        Map<LocalDateTime, Double> trafficPatterns = new LinkedHashMap<>();

        for (Map.Entry<LocalDateTime, List<Map>> entry : tempGroupedData.entrySet()) {

            double totalSpeed = 0;

            for (Map data : entry.getValue()) {

                totalSpeed += (Double) data.get("vehicle_speed");

            }

            trafficPatterns.put(entry.getKey(), totalSpeed / entry.getValue().size());

            System.out.println(entry.getKey() + "|" + totalSpeed + "|" + entry.getValue().size() + "|" + totalSpeed / entry.getValue().size());

        }

        return trafficPatterns;

    }


    @Lock(LockType.WRITE)

    public Map<LocalDateTime, Double> trafficDensity() {

        Map<LocalDateTime, List<Map>> tempGroupedData = groupedData;


        Map<LocalDateTime, Double> trafficFlow = new LinkedHashMap<>();

        for (Map.Entry<LocalDateTime, List<Map>> entry : tempGroupedData.entrySet()) {

            int totalVehicles = 0;

            for (Map data : entry.getValue()) {

                totalVehicles += 1;

            }


            double totalSpeed = 0;

            for (Map data : entry.getValue()) {

                totalSpeed += (Double) data.get("vehicle_speed");

            }


            double averageVehicleSpeed = totalSpeed / totalVehicles;


            double trafficDensity = totalVehicles * averageVehicleSpeed;


            trafficFlow.put(entry.getKey(), trafficDensity);

        }


        return trafficFlow;

    }


    @Lock(LockType.WRITE)

    public List<Map<LocalDateTime, Double>> mobilityOfTheArea() {

        Map<LocalDateTime, List<Map>> tempGroupedData = groupedData;


        Map<LocalDateTime, Double> tti = new LinkedHashMap<>();

        Map<LocalDateTime, Double> variancetti = new LinkedHashMap<>();

        Map<LocalDateTime, Double> standardDevtti = new LinkedHashMap<>();

        List<Map<LocalDateTime, Double>> ttiContainer = new ArrayList<>();


        for (Map.Entry<LocalDateTime, List<Map>> entry : tempGroupedData.entrySet()) {

            int totalVehicles = 0;

            for (Map data : entry.getValue()) {

                totalVehicles += 1;

            }


            double totalTTI = 0;

            for (Map data : entry.getValue()) {

                String trafficLightStatus = data.get("traffic_light_status").toString();

                double waitTime = 0;

                if (trafficLightStatus.equals("RED")) {

                    waitTime = 0.0167;//1 minute

                }

                totalTTI += Double.parseDouble(data.get("time_travel_index").toString());

            }

            double averageTTI = totalTTI / totalVehicles;


            tti.put(entry.getKey(), averageTTI);


            double sumSquaredDifferences = 0.0;

            for (Map data : entry.getValue()) {

                sumSquaredDifferences += Math.pow(Double.parseDouble(data.get("time_travel_index").toString()) - averageTTI, 2);

            }

            double varianceTTI = sumSquaredDifferences / totalVehicles;

            double standardDevTTI = Math.sqrt(varianceTTI);


            variancetti.put(entry.getKey(), varianceTTI);

            standardDevtti.put(entry.getKey(), standardDevTTI);


            ttiContainer.add(tti);

            ttiContainer.add(variancetti);

            ttiContainer.add(standardDevtti);


        }


        return ttiContainer;


    }


}
