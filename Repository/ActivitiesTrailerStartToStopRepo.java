package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.ActivityTrailerStartToStop;
import com.example.demo.SmartDashboard.Model.ActivityTrailersSensolus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitiesTrailerStartToStopRepo extends CrudRepository<ActivityTrailerStartToStop,Integer> {
}
