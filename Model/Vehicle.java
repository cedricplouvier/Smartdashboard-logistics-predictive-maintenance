package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "VEHICLE_TYPE",discriminatorType = DiscriminatorType.STRING)
public class Vehicle {
    @Column
    private String chassisNumber;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int ID;
    @Column
    private String numberPlate;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "vehicle")
    private Map<Date, Invoice> invoices = new TreeMap<>();
    @Column
    private int mileage;
    @Column
    private int buildYear;
    @ElementCollection
    private Map<Date, Double> maintenanceCosts = new HashMap<>();
    
    public Vehicle() {
    }

    public Vehicle(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public Vehicle(String numberPlate, String chassisNumber) {
        this.numberPlate = numberPlate;
        this.chassisNumber = chassisNumber;
    }

    public Vehicle(String numberPlate, String chassisNumber, int buildYear) {
        this.numberPlate = numberPlate;
        this.chassisNumber = chassisNumber;
        this.buildYear = buildYear;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void addMaintenanceCost(Date date, double value){
        Double currentCost = 0.0;
        if(maintenanceCosts.containsKey(date)) {
            currentCost = maintenanceCosts.get(date);
        }
        maintenanceCosts.put(date, currentCost + value);
    }

    public Map<Date, Double> getMaintenanceCosts() {
        return maintenanceCosts;
    }

    public int getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(int buildYear) {
        this.buildYear = buildYear;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "numberPlate='" + numberPlate + '\'' +
                ", chassisNumber='" + chassisNumber + '\'' +
                ", ID=" + ID +
                '}';
    }

    public void addInvoiceToVehicle(Date date, Invoice i){
        invoices.put(date, i);
    }

    public Map<Date, Invoice> getInvoices() {
        return invoices;
    }
}
