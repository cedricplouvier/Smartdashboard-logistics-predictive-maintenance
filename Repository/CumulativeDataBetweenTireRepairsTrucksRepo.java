package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.CumulativeDataBetweenTireRepairsTrucks;
import com.example.demo.SmartDashboard.Model.TruckDailyConsumptionData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CumulativeDataBetweenTireRepairsTrucksRepo extends CrudRepository<CumulativeDataBetweenTireRepairsTrucks, Integer> {
    List<CumulativeDataBetweenTireRepairsTrucks> findAll();
}
