package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.TruckDailyConsumptionData;
import com.example.demo.SmartDashboard.Model.TruckDailyLoadedData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruckDailyLoadedDataRepo extends CrudRepository<TruckDailyLoadedData, Integer> {
    List<TruckDailyLoadedData> findAll();
}
