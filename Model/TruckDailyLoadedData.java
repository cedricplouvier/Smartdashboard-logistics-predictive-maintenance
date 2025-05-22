package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TruckDailyLoadedData {

    @Id
    @GeneratedValue
    private int id;
    @Column
    Date date;
    @Column
    private Double soloPercentageDriving;
    @Column
    private  String numberplate;
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
    private int totalKilometers;
    @ManyToOne
    private Truck truck;

    public TruckDailyLoadedData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
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

    public int getTotalKilometers() {
        return totalKilometers;
    }

    public void setTotalKilometers(int totalKilometersLoaded) {
        this.totalKilometers = totalKilometersLoaded;
    }
}
