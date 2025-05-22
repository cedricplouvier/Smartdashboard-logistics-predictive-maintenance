package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;

@Entity
public class CumulativeDataBetweenTireRepairsTrucks {

    @Id
    @GeneratedValue
    private int id;
    @Column
    private  String numberplate;
    @Column
    private Date datePreviousRepair;
    @Column
    private Date dateCurrentRepair;
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
    @Column
    private Double soloPercentageDriving;
    @Column
    private int soloKilometersDriving;
    @Column
    private Double emptyPercentageDriving;
    @Column
    private int emptyKilometersDriving;
    @Column
    private Double unknownPercentage;
    @Column
    private int unknownKilometers;
    @Column
    private int loadedKilometers;
    @Column
    private Double LoadedPercentage;
    @Column
    int weeksBetween;
    @ManyToOne
    private Truck truck;

    public CumulativeDataBetweenTireRepairsTrucks() {
    }

    public String getNumberplate() {
        return numberplate;
    }

    public void setNumberplate(String numberplate) {
        this.numberplate = numberplate;
    }

    public Date getDatePreviousRepair() {
        return datePreviousRepair;
    }

    public void setDatePreviousRepair(Date datePreviousRepair) {
        this.datePreviousRepair = datePreviousRepair;
    }

    public Date getDateCurrentRepair() {
        return dateCurrentRepair;
    }

    public void setDateCurrentRepair(Date dateCurrentRepair) {
        this.dateCurrentRepair = dateCurrentRepair;
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

    public Double getSoloPercentageDriving() {
        return soloPercentageDriving;
    }

    public void setSoloPercentageDriving(Double soloPercentageDriving) {
        this.soloPercentageDriving = soloPercentageDriving;
    }

    public int getSoloKilometersDriving() {
        return soloKilometersDriving;
    }

    public void setSoloKilometersDriving(int soloKilometersDriving) {
        this.soloKilometersDriving = soloKilometersDriving;
    }

    public Double getEmptyPercentageDriving() {
        return emptyPercentageDriving;
    }

    public void setEmptyPercentageDriving(Double emptyPercentageDriving) {
        this.emptyPercentageDriving = emptyPercentageDriving;
    }

    public int getEmptyKilometersDriving() {
        return emptyKilometersDriving;
    }

    public void setEmptyKilometersDriving(int emptyKilometersDriving) {
        this.emptyKilometersDriving = emptyKilometersDriving;
    }

    public Double getUnknownPercentage() {
        return unknownPercentage;
    }

    public void setUnknownPercentage(Double unknownPercentage) {
        this.unknownPercentage = unknownPercentage;
    }

    public int getUnknownKilometers() {
        return unknownKilometers;
    }

    public void setUnknownKilometers(int unknownKilometers) {
        this.unknownKilometers = unknownKilometers;
    }

    public int getLoadedKilometers() {
        return loadedKilometers;
    }

    public void setLoadedKilometers(int loadedKilometers) {
        this.loadedKilometers = loadedKilometers;
    }

    public Double getLoadedPercentage() {
        return LoadedPercentage;
    }

    public void setLoadedPercentage(Double loadedPercentage) {
        LoadedPercentage = loadedPercentage;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeeksBetween() {
        return weeksBetween;
    }

    public void setWeeksBetween(int weeksBetween) {
        this.weeksBetween = weeksBetween;
    }
}
