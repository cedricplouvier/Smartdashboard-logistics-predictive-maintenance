package com.example.demo.SmartDashboard.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class ActivityTrailers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int ID_activityTrailer;
    @Column
    private String serial;
    @Column
    private String numberPlate;
    @Column
    private Date timeStamp;
    @Column
    private String stateActivity;
    @Column
    private double lat;
    @Column
    private double lon;
    @Column
    private String Geozone;
    @Column
    private int accuracy;
    @Column
    private String address;
    @ManyToOne
    @JoinColumn(name = "Vehicle_id", nullable = false)
    private Trailer trailer;
    @ElementCollection
    private Map<Truck, Double> possibleTrucks = new HashMap<Truck, Double>();

    @OneToOne
    private Truck truck;

    public ActivityTrailers() {
    }

    public ActivityTrailers(Trailer v) {
        this.serial = "testserial";
        this.numberPlate = v.getNumberPlate();
        this.lat = 10;
        this.lon = 10;
        this.timeStamp = new Date();
        this.Geozone = "Antwerpne";
        this.accuracy= 10;
        this.address = "schoten";
        setTrailer(v);
    }

    public ActivityTrailers(String serial, String numberPlate, Date timeStamp, double lat, double lon, String geozone, int accuracy, String address) {
        this.serial = serial;
        this.numberPlate = numberPlate;
        this.timeStamp = timeStamp;
        this.lat = lat;
        this.lon = lon;
        this.Geozone = geozone;
        this.accuracy = accuracy;
        this.address = address;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getGeozone() {
        return Geozone;
    }

    public void setGeozone(String geozone) {
        Geozone = geozone;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    public String getState() {
        return stateActivity;
    }

    public void setState(String state) {
        stateActivity = state;
    }

    public Trailer getTrailer() {
        return trailer;
    }


    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Map<Truck, Double> getPossibleTrucks() {
        return possibleTrucks;
    }

    public void setPossibleTrucks(Map<Truck, Double> possibleTrucks) {
        this.possibleTrucks = possibleTrucks;
    }
}
