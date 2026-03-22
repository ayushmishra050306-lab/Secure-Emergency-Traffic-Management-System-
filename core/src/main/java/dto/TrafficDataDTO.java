package dto;

import java.io.Serializable;
import java.util.Date;

public class TrafficDataDTO implements Serializable {
    private String deviceType;
    private double vehicleSpeed;
    private String trafficLightStatus;
    private double latitude;
    private double longitude;
    private Date dateTimestamp;

    public TrafficDataDTO() {
    }

    public TrafficDataDTO(String deviceType, double vehicleSpeed, String trafficLightStatus, double latitude, double longitude, Date dateTimestamp) {
        this.deviceType = deviceType;
        this.vehicleSpeed = vehicleSpeed;
        this.trafficLightStatus = trafficLightStatus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTimestamp = dateTimestamp;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public double getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(double vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public String getTrafficLightStatus() {
        return trafficLightStatus;
    }

    public void setTrafficLightStatus(String trafficLightStatus) {
        this.trafficLightStatus = trafficLightStatus;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(Date dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }
}
