package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.ActivityTrucks;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitiesTrucksRepo extends CrudRepository<ActivityTrucks,Integer> {
    List<ActivityTrucks> findAll();
}
