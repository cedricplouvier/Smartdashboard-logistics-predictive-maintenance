package com.example.demo.SmartDashboard.Model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Entity
@DiscriminatorValue(value = "TRUCK")
public class Truck extends Vehicle {
    @Enumerated(value = EnumType.STRING)
    private TruckBrand truckBrand;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truck")
    private List<ActivityTrucks> activities;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truck")
    @LazyCollection(LazyCollectionOption.TRUE)
    private Map<Date, TruckDailyConsumptionData> truckDailyConsumptionDataMap = new TreeMap<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truck")
    private Map<Date, TruckDailyLoadedData> truckDailyLoadedDataMap = new TreeMap<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truck")
    private Map<Date, CumulativeDataBetweenTireRepairsTrucks> tireRepairs = new TreeMap<>();

    public Truck() {
    }

    public Truck(String numberPlate) {
        super(numberPlate);
    }

    public Truck(String numberPlate, String chassisNumber) {
        super(numberPlate, chassisNumber);
    }

    public Truck(String numberPlate, String chassisNumber, TruckBrand truckBrand) {
        super(numberPlate, chassisNumber);
        this.truckBrand = truckBrand;
    }

    public Truck(String numberPlate, String chassisNumber, TruckBrand truckBrand, int buildYear) {
        super(numberPlate, chassisNumber, buildYear);
        this.truckBrand = truckBrand;
    }

    public void addActivity(ActivityTrucks a){
        activities.add(a);
    }

    public Map<Date, TruckDailyConsumptionData> getTruckDailyConsumptionDataMap() {
        return truckDailyConsumptionDataMap;
    }

    public void setTruckDailyConsumptionDataMap(Map<Date, TruckDailyConsumptionData> truckDailyConsumptionDataMap) {
        this.truckDailyConsumptionDataMap = truckDailyConsumptionDataMap;
    }

    public Map<Date, TruckDailyLoadedData> getTruckDailyLoadedDataMap() {
        return truckDailyLoadedDataMap;
    }

    public void setTruckDailyLoadedDataMap(Map<Date, TruckDailyLoadedData> truckDailyLoadedDataMap) {
        this.truckDailyLoadedDataMap = truckDailyLoadedDataMap;
    }

    public List<ActivityTrucks> getActivities() {
        return activities;
    }

    public void addLoadedDailyDataToMap(Date key, TruckDailyLoadedData data){
        truckDailyLoadedDataMap.put(key, data);
    }

    public void addConsumptionDailyDataToMap(Date key, TruckDailyConsumptionData data){
        truckDailyConsumptionDataMap.put(key, data);
    }

    public void addTireRepair(Date key, CumulativeDataBetweenTireRepairsTrucks data){
        tireRepairs.put(key,data);
    }

    public Map<Date, CumulativeDataBetweenTireRepairsTrucks> getTireRepairs() {
        return tireRepairs;
    }

}
