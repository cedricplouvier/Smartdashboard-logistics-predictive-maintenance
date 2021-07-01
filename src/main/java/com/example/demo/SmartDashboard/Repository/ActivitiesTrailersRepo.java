package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.ActivityTrailers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitiesTrailersRepo extends CrudRepository<ActivityTrailers,Integer>{
    List<ActivityTrailers> findAll();
}
