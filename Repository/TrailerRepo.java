package com.example.demo.SmartDashboard.Repository;


import com.example.demo.SmartDashboard.Model.Trailer;
import com.example.demo.SmartDashboard.Model.Vehicle;
import org.springframework.data.repository.CrudRepository;

public interface TrailerRepo extends CrudRepository<Trailer,Integer> {
    boolean existsBySensolusSerial(String sensolusSerial);
}
