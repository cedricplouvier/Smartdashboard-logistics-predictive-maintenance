package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;

@Entity
public class TruckDailyConsumptionData {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private  String numberplate;
    @Column
    Date date;
    @Column
    private int kilometersTravelled;
    @Column
    private int startKilometerCounter;
    @Column
    private  int endKilometerCounter;
    @Column
    private Duration motorTimeHours;
    @Column
    private Duration drivingTimeHours;
    @Column
    private Double drivingTimePercentage;
    @Column
    private Duration stationaryTimeHours;
    @Column
    private Double stationaryTimePercentage;
    @Column
    private Double averageSpeedDriving;
    @ManyToOne
    private Truck truck;


    public TruckDailyConsumptionData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberplate() {
        return numberplate;
    }

    public void setNumberplate(String numberplate) {
        this.numberplate = numberplate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getKilometersTravelled() {
        return kilometersTravelled;
    }

    public void setKilometersTravelled(int kilometersTravelled) {
        this.kilometersTravelled = kilometersTravelled;
    }

    public int getStartKilometerCounter() {
        return startKilometerCounter;
    }

    public void setStartKilometerCounter(int startKilometerCounter) {
        this.startKilometerCounter = startKilometerCounter;
    }

    public int getEndKilometerCounter() {
        return endKilometerCounter;
    }

    public void setEndKilometerCounter(int endKilometerCounter) {
        this.endKilometerCounter = endKilometerCounter;
    }

    public Duration getMotorTimeHours() {
        return motorTimeHours;
    }

    public void setMotorTimeHours(Duration motorTimeHours) {
        this.motorTimeHours = motorTimeHours;
    }

    public Duration getDrivingTimeHours() {
        return drivingTimeHours;
    }

    public void setDrivingTimeHours(Duration drivingTimeHours) {
        this.drivingTimeHours = drivingTimeHours;
    }

    public Double getDrivingTimePercentage() {
        return drivingTimePercentage;
    }

    public void setDrivingTimePercentage(Double drivingTimePercentage) {
        this.drivingTimePercentage = drivingTimePercentage;
    }

    public Duration getStationaryTimeHours() {
        return stationaryTimeHours;
    }

    public void setStationaryTimeHours(Duration stationaryTimeHours) {
        this.stationaryTimeHours = stationaryTimeHours;
    }

    public Double getStationaryTimePercentage() {
        return stationaryTimePercentage;
    }

    public void setStationaryTimePercentage(Double stationaryTimePercentage) {
        this.stationaryTimePercentage = stationaryTimePercentage;
    }

    public Double getAverageSpeedDriving() {
        return averageSpeedDriving;
    }

    public void setAverageSpeedDriving(Double averageSpeedDriving) {
        this.averageSpeedDriving = averageSpeedDriving;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }
}
