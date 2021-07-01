package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Invoice {
    @Id
    @Column
    private int factuurNR;
    @Column
    private Date date;
    @Column
    private int clientNR;
    @ManyToOne
    @JoinColumn(name = "Vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ElementCollection
    private Map<MaintenanceType, Double> maintenanceTypes =  new HashMap<>();

    public Invoice(){
    }

    public Invoice(int factuurNR){
        this.factuurNR = factuurNR;
    }

    public int getFactuurNR() {
        return factuurNR;
    }

    public void setFactuurNR(int factuurNR) {
        this.factuurNR = factuurNR;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getClientNR() {
        return clientNR;
    }

    public void setClientNR(int clientNR) {
        this.clientNR = clientNR;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Map<MaintenanceType, Double> getMaintenanceTypes() {
        return maintenanceTypes;
    }

    public void setMaintenanceTypes(Map<MaintenanceType, Double> maintenanceTypes) {
        this.maintenanceTypes = maintenanceTypes;
    }
}
