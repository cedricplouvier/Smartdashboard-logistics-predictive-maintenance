package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Invoice> invoices;
    @Column
    private int mileage;


    @ElementCollection
    private Map<String, Double> maintenanceCosts = new HashMap<>();
    
    public Vehicle() {
    }

    public Vehicle(String numberPlate, String chassisNumber) {
        this.numberPlate = numberPlate;
        this.chassisNumber = chassisNumber;
    }

    public Vehicle(String numberPlate) {
        this.numberPlate = numberPlate;
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

    public void addMaintenanceCost(String date, double value){
        Double currentCost = 0.0;
        if(maintenanceCosts.containsKey(date)) {
            currentCost = maintenanceCosts.get(date);
        }
        maintenanceCosts.put(date, currentCost + value);
    }

    public Map<String, Double> getMaintenanceCosts() {
        return maintenanceCosts;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "numberPlate='" + numberPlate + '\'' +
                ", chassisNumber='" + chassisNumber + '\'' +
                ", ID=" + ID +
                '}';
    }
}
