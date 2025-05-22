package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.TruckDailyConsumptionData;
import com.example.demo.SmartDashboard.Repository.TruckDailyConsumptionDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckDailyConsumptionDataService {

    @Autowired
    private TruckDailyConsumptionDataRepo truckDailyConsumptionDataRepo;

    public List<TruckDailyConsumptionData> findAll() {
        return this.truckDailyConsumptionDataRepo.findAll();
    }

    public void save(TruckDailyConsumptionData truckDailyConsumptionData){ truckDailyConsumptionDataRepo.save(truckDailyConsumptionData);}
}
