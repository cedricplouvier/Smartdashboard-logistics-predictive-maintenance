package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int ID;

    @Column
    private int factuurNR;
    @Column
    private Date date;
    @Column
    private int clientNR;
    @ManyToOne
    @JoinColumn(name = "Vehicle_id", nullable = false)
    private Vehicle vehicle;
    @Column
    Boolean tireRepair;
    @Column
    Boolean depannage;

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

    public Boolean getTireRepair() {
        return tireRepair;
    }

    public void setTireRepair(Boolean tireRepair) {
        this.tireRepair = tireRepair;
    }

    public boolean isDepannage() {
        return depannage;
    }

    public void setDepannage(Boolean depannage) {
        this.depannage = depannage;
    }
}
