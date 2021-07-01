package com.example.demo.SmartDashboard.Model;

import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ActivityTrucks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int ID_activityTrucs;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column
    private double duration;
    @Column
    private String numberPlate;
    @Column
    private String startLocation;
    @Column
    private String stopLocation;
    @Column
    private double lat;
    @Column
    private double lon;
    @Column
    private int Distance;
    @Column
    private double averageSpeedActivity;
    @ManyToOne
    @JoinColumn(name = "Vehicle_id", nullable = false)
    private Truck truck;

    public ActivityTrucks() {
    }

    public int getID_activityTrucs() {
        return ID_activityTrucs;
    }

    public void setID_activityTrucs(int ID_activityTrucs) {
        this.ID_activityTrucs = ID_activityTrucs;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStopLocation() {
        return stopLocation;
    }

    public void setStopLocation(String stopLocation) {
        this.stopLocation = stopLocation;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public double getAverageSpeedActivity() {
        return averageSpeedActivity;
    }

    public void setAverageSpeedActivity(double averageSpeedActivity) {
        this.averageSpeedActivity = averageSpeedActivity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
