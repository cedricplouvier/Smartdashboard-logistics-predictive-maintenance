package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.*;

@Entity
@DiscriminatorValue(value = "TRAILER")
public class Trailer extends Vehicle {
    @Enumerated(value = EnumType.STRING)
    private TrailerType trailerType;
    @Enumerated(value = EnumType.STRING)
    private TrailerBrand trailerBrand;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "trailer")
    private List<ActivityTrailersSensolus> activities;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "trailer")
    private List<ActivityTrailerStartToStop> activitiesStartToStop;
    @ElementCollection
    private Map<String, Double> idleLocationTimes = new HashMap<>();

    @Column
    private String sensolusSerial;

    private String state;

    public Trailer() {
    }

    public Trailer(String numberPlate) {
        super(numberPlate);
    }

    public Trailer(String numberPlate, String sensolusSerial) {
        super(numberPlate);
        this.sensolusSerial=sensolusSerial;
    }

    public Trailer(String numberPlate, String sensolusSerial, TrailerType trailerType) {
        super(numberPlate);
        this.trailerType=trailerType;
        this.sensolusSerial=sensolusSerial;
    }

    public Trailer(String numberPlate, String chassisNumber, String sensolusSerial, TrailerType trailerType) {
        super(numberPlate, chassisNumber);
        this.trailerType=trailerType;
        this.sensolusSerial=sensolusSerial;
    }

    public void addActivity(ActivityTrailersSensolus a){
        activities.add(a);
    }

    public List<ActivityTrailersSensolus> getActivities() {
        return activities;
    }

    public TrailerType getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(TrailerType trailerType) {
        this.trailerType = trailerType;
    }

    public void setActivities(List<ActivityTrailersSensolus> activities) {
        this.activities = activities;
    }

    public String getSensolusSerial() {
        return sensolusSerial;
    }

    public void setSensolusSerial(String sensolusSerial) {
        this.sensolusSerial = sensolusSerial;
    }

    public TrailerBrand getTrailerBrand() {
        return trailerBrand;
    }

    public void setTrailerBrand(TrailerBrand trailerBrand) {
        this.trailerBrand = trailerBrand;
    }

    public List<ActivityTrailerStartToStop> getActivitiesStartToStop() {
        return activitiesStartToStop;
    }

    public void setActivitiesStartToStop(List<ActivityTrailerStartToStop> activitiesStartToStop) {
        this.activitiesStartToStop = activitiesStartToStop;
    }

    public Map<String, Double> getIdleLocationTimes() {
        return idleLocationTimes;
    }

    public void setIdleLocationTimes(Map<String, Double> idleLocationTimes) {
        this.idleLocationTimes = idleLocationTimes;
    }
}
