package io.justride;

import java.util.Date;

class EnrichedFlaggedViolationEvent  {
    public EnrichedFlaggedViolationEvent(String key, long start, long end, long count, Double lastLatitude, Double lastLongitude, Double lastSpeed, Double maxSpeed, String uuid, Date violationTime) {
        this.key = key;
        this.start = start;
        this.end = end;
        this.count = count;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.lastSpeed = lastSpeed;
        this.maxSpeed = maxSpeed;
        this.uuid = uuid;
        this.violationTime = violationTime;
    }

    public EnrichedFlaggedViolationEvent() {
    }

    private String key, uuid;
    private long start, end, count;
    private Double lastLatitude, lastLongitude, lastSpeed, maxSpeed;
    private Date violationTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(Double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public Double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(Double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public Double getLastSpeed() {
        return lastSpeed;
    }

    public void setLastSpeed(Double lastSpeed) {
        this.lastSpeed = lastSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Date getViolationTime() {
        return violationTime;
    }

    public void setViolationTime(Date violationTime) {
        this.violationTime = violationTime;
    }

    @Override
    public String toString() {
        return "EnrichedFlaggedViolationEvent{" +
                "key='" + key + '\'' +
                ", uuid='" + uuid + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", count=" + count +
                ", lastLatitude=" + lastLatitude +
                ", lastLongitude=" + lastLongitude +
                ", lastSpeed=" + lastSpeed +
                ", maxSpeed=" + maxSpeed +
                ", violationTime=" + violationTime +
                '}';
    }
}