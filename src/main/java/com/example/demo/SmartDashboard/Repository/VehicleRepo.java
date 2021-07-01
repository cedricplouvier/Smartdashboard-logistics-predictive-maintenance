package com.example.demo.SmartDashboard.Repository;

import com.example.demo.SmartDashboard.Model.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepo extends CrudRepository <Vehicle,Integer>{
    List<Vehicle> findAll();
    Vehicle findVehicleByNumberPlate(String numberPlate);
    boolean existsByNumberPlate(String numberPlate);
}
