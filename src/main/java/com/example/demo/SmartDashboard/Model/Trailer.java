package com.example.demo.SmartDashboard.Model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "TRAILER")
public class Trailer extends Vehicle {
    @Enumerated(value = EnumType.STRING)
    private TrailerType trailerType;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "trailer")
    private List<ActivityTrailers> activities;
    @Column
    private String sensolusSerial;

    private String state;

    public Trailer() {
    }

    public Trailer(String numberPlate, String chassisNumber, String sensolusSerial, TrailerType trailerType) {
        super(numberPlate, chassisNumber);
        this.trailerType=trailerType;
        this.sensolusSerial=sensolusSerial;
    }

    public Trailer(String numberPlate) {
        super(numberPlate);
    }

    public Trailer(String numberPlate, String sensolusSerial) {
        super(numberPlate);
        this.sensolusSerial=sensolusSerial;
    }

    public void addActivity(ActivityTrailers a){
        activities.add(a);
    }

    public List<ActivityTrailers> getActivities() {
        return activities;
    }

    public TrailerType getTrailerType() {
        return trailerType;
    }

    public void setTrailerType(TrailerType trailerType) {
        this.trailerType = trailerType;
    }

    public void setActivities(List<ActivityTrailers> activities) {
        this.activities = activities;
    }

    public String getSensolusSerial() {
        return sensolusSerial;
    }

    public void setSensolusSerial(String sensolusSerial) {
        this.sensolusSerial = sensolusSerial;
    }
}
