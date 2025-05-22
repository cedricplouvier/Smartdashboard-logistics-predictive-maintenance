package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.CumulativeDataBetweenTireRepairsTrucks;
import com.example.demo.SmartDashboard.Model.TruckDailyConsumptionData;
import com.example.demo.SmartDashboard.Repository.CumulativeDataBetweenTireRepairsTrucksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CumulativeDataBetweenTireRepairsTrucksService {

    @Autowired
    private CumulativeDataBetweenTireRepairsTrucksRepo cumulativeDataBetweenTireRepairsTrucksRepo;

    public List<CumulativeDataBetweenTireRepairsTrucks> findAll() {
        return this.cumulativeDataBetweenTireRepairsTrucksRepo.findAll();
    }

    public void save(CumulativeDataBetweenTireRepairsTrucks cumulativeDataBetweenTireRepairsTrucks){ cumulativeDataBetweenTireRepairsTrucksRepo.save(cumulativeDataBetweenTireRepairsTrucks);}
}
