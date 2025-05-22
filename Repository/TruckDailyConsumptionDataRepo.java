package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.TruckDailyConsumptionData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruckDailyConsumptionDataRepo extends CrudRepository<TruckDailyConsumptionData, Integer> {
    List<TruckDailyConsumptionData> findAll();
}
