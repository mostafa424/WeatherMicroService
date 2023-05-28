package org.example;

public class Status {
    public long stationId;
    public long statusNo;
    public String batteryStatus;
    public long timestamp;
    public int[] weather = new int[3];  

    public Status() {

    }

    public Status(long stationId, long statusNo, String batteryStatus, long timestamp, int[] weather) {
        this.stationId = stationId;
        this.statusNo = statusNo;
        this.batteryStatus = batteryStatus;
        this.timestamp = timestamp;
        this.weather = weather;
    }

    @Override
    public String toString() {
        return stationId + " " + statusNo + " " + batteryStatus + " " + timestamp + " " + weather[0] + " " + weather[1] + " " + weather[2];
    }
}
