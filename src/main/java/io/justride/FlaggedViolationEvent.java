package io.justride;

import java.util.Date;

public class FlaggedViolationEvent {

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public FlaggedViolationEvent() {
        this.uuid = "0";
        this.maxSpeed = 0D;
        this.lastSpeed = 0D;
        this.count = 0L;

    }

    public FlaggedViolationEvent addPodEvent(PodEvent pe) {
        this.uuid = pe.getUuid();
        this.lastSpeed = pe.getSpeed();
        this.lastLatitude = pe.getLatitude();
        this.lastLongitude = pe.getLongitude();
        this.maxSpeed = (this.maxSpeed > pe.getSpeed())
                ? this.getMaxSpeed() : pe.getSpeed();
        this.violationTime = new Date();
        this.count++;

        return this;
    }

    public FlaggedViolationEvent(String uuid, Double maxSpeed, Double lastSpeed, long count, Date violationTime) {
        this.uuid = uuid;
        this.maxSpeed = maxSpeed;
        this.lastSpeed = lastSpeed;
        this.count = count;
        this.violationTime = violationTime;
    }

    @Override
    public String toString() {
        return "FlaggedViolationEvent{" +
                "uuid=" + uuid +
                ", count=" + count +
                ", lastSpeed=" + lastSpeed +
                ", maxSpeed=" + maxSpeed +
                ", lastLatitude=" + lastLatitude +
                ", lastLongitude=" + lastLongitude +
                '}';
    }

    public String getUuid() {
        return uuid;
    }



    private String uuid;

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getLastSpeed() {
        return lastSpeed;
    }

    public void setLastSpeed(Double lastSpeed) {
        this.lastSpeed = lastSpeed;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    private Double maxSpeed;
    private Double lastSpeed;
    private long count;

    public Double getLastLatitude() {
        return lastLatitude;
    }

    public Double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLatitude(Double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    private Double lastLatitude;
    private Double lastLongitude;

    public Date getViolationTime() {
        return violationTime;
    }

    public void setViolationTime(Date violationTime) {
        this.violationTime = violationTime;
    }

    private Date violationTime;
}
