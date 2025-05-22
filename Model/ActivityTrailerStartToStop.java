package com.example.demo.SmartDashboard.Model;

import com.example.demo.SmartDashboard.Service.ActivitiesTrailersService;

import javax.persistence.*;

@Entity
public class ActivityTrailerStartToStop implements Comparable<ActivityTrailerStartToStop> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int ActivityTrailerStartStop_ID;

    @Column
    private double averageSpeed;

    @Column
    private int distanceTravalled;

    @OneToOne
    private ActivityTrailersSensolus startSensolus;

    @OneToOne
    private ActivityTrailersSensolus stopSensolus;

    @ManyToOne
    @JoinColumn(name = "Vehicle_id", nullable = false)
    private Trailer trailer;

    public ActivityTrailerStartToStop() {
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getDistanceTravalled() {
        return distanceTravalled;
    }

    public void setDistanceTravalled(int distanceTravalled) {
        this.distanceTravalled = distanceTravalled;
    }

    public ActivityTrailersSensolus getStartSensolus() {
        return startSensolus;
    }

    public void setStartSensolus(ActivityTrailersSensolus startSensolus) {
        this.startSensolus = startSensolus;
    }

    public ActivityTrailersSensolus getStopSensolus() {
        return stopSensolus;
    }

    public void setStopSensolus(ActivityTrailersSensolus stopSensolus) {
        this.stopSensolus = stopSensolus;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    @Override
    public int compareTo(ActivityTrailerStartToStop o) {
        return getStartSensolus().getTimeStamp().compareTo(o.getStartSensolus().getTimeStamp());
    }
}
