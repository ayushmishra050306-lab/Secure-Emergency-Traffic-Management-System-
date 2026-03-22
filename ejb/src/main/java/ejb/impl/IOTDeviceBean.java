package ejb.impl;

import ejb.remote.IOTDevice;
import ejb.remote.TrafficEnvironment;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Stateless
public class IOTDeviceBean implements Serializable, IOTDevice {
    private String deviceType;
    private double vehicleSpeed;
    private String trafficLightStatus;
    private double latitude;
    private double longitude;
    private Date dateTimestamp;
    private final Random random = new Random();

    @EJB
    TrafficEnvironment trafficEnvironment;


    public void captureSensorData(Date dateTimestamp) {

        this.dateTimestamp=dateTimestamp;

        this.deviceType = random.nextBoolean() ? "VEHICLE" : "TRAFFIC_LIGHT";

        double trafficDensity = trafficEnvironment.getTrafficDensity(dateTimestamp);
//        System.out.println(dateTimestamp+"|"+trafficDensity);
        double baseVehicleSpeed = (50 * (1 - trafficDensity));
        double randomVariation = random.nextDouble() * 10 - 5;
        this.vehicleSpeed=Double.parseDouble(String.format("%.2f", baseVehicleSpeed + randomVariation));


        switch (random.nextInt(3)) {
            case 0:
                this.trafficLightStatus = "RED";
                break;
            case 1:
                this.trafficLightStatus = "YELLOW";
                break;
            default:
                this.trafficLightStatus = "GREEN";
        }

        double[][] roadAreas = {

                {6.866668, 79.86283},
                {6.866218, 79.862910},
                {6.865781, 79.862999},
                {6.865349, 79.863108},
                {6.864904, 79.863208},
                {6.864470, 79.863305},
                {6.864022, 79.863406},
                {6.863588, 79.863506},
                {6.863150, 79.863586},
                {6.862707, 79.863680},

                {6.861829, 79.863868},
                {6.861390, 79.863980},
                {6.860955, 79.864081},
                {6.860690, 79.864146},
                {6.860256, 79.864253},
                {6.859816, 79.864360},
                {6.859381, 79.864458},
                {6.858979, 79.864554},
                {6.858541, 79.864652},
                {6.8581616, 79.8647901},
        };

        double[][] trafficIntersections = {
                {6.862265, 79.863778}
        };


        int randomIndex = random.nextInt(roadAreas.length);

        if (deviceType.equals("VEHICLE")) {
            double[] selectedRoadArea = roadAreas[randomIndex];
            this.latitude = selectedRoadArea[0] + ((double) random.nextInt(50) / 1000000);  // road width = 50
            this.longitude = selectedRoadArea[1] + ((double) random.nextInt(99) / 1000000);  // road length = 1km
        } else {
            double[] selectedIntersectionArea = trafficIntersections[0];
            this.latitude = selectedIntersectionArea[0];
            this.longitude = selectedIntersectionArea[1];
        }


    }

    public String getDeviceType() {
        return deviceType;
    }

    public double getVehicleSpeed() {
        return vehicleSpeed;
    }

    public String getTrafficLightStatus() {
        return trafficLightStatus;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getTimestamp() {
        return dateTimestamp;
    }

    @Override
    public String toString() {
        return "cls.TrafficDataCollectorBean{" +
                ", deviceType='" + deviceType + '\'' +
                ", vehicleSpeed=" + vehicleSpeed +
                ", trafficLightStatus='" + trafficLightStatus + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(dateTimestamp) +
                '}';
    }
}
