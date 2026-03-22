package ejb.remote;

import jakarta.ejb.Remote;

import java.util.Date;

@Remote
public interface IOTDevice {

    public void captureSensorData(Date dateTimestamp);

    public String getDeviceType();

    public double getVehicleSpeed();

    public String getTrafficLightStatus();
    public double getLatitude();

    public double getLongitude();

    public Date getTimestamp();
}

