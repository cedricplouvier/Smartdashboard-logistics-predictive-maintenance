package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.TruckDailyConsumptionData;
import com.example.demo.SmartDashboard.Model.TruckDailyLoadedData;
import com.example.demo.SmartDashboard.Repository.TruckDailyLoadedDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruckDailyLoadedDataService {

    @Autowired
    private TruckDailyLoadedDataRepo truckDailyLoadedDataRepo;

    public List<TruckDailyLoadedData> findAll() {
        return this.truckDailyLoadedDataRepo.findAll();
    }

    public void save(TruckDailyLoadedData truckDailyLoadedData){
        truckDailyLoadedDataRepo.save(truckDailyLoadedData);
    }
}
