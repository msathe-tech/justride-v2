package io.justride.model;

import java.io.Serializable;

public class PodEvent implements Serializable {

    public PodEvent(String uuid, Double latitude, Double longitude, Double speed) {
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public PodEvent() {
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "PodEvent{" +
                "uuid=" + uuid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", speed=" + speed +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    private String uuid;
    private Double latitude;
    private Double longitude;
    private Double speed;

}
