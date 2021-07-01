package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "TRUCK")
public class Truck extends Vehicle {
    @Enumerated(value = EnumType.STRING)
    private TruckType truckType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truck")
    private List<ActivityTrucks> activities;

    public Truck() {
    }

    public Truck(String numberPlate, String chassisNumber, TruckType truckType) {
        super(numberPlate, chassisNumber);
        this.truckType=truckType;
    }

    public Truck(String numberPlate, String chassisNumber) {
        super(numberPlate, chassisNumber);
    }

    public Truck(String numberPlate) {
        super(numberPlate);
    }

    public void addActivity(ActivityTrucks a){
        activities.add(a);
    }
}
