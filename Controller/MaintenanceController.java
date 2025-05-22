package com.example.demo.SmartDashboard.Controller;

import com.example.demo.SmartDashboard.Model.MaintenanceType;
import com.example.demo.SmartDashboard.Service.MaintenanceService;
import com.example.demo.SmartDashboard.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @RequestMapping("/importMaintenanceExcel")
    public void importMaintenanceExcel(@RequestParam(required = false) MultipartFile maintenanceFile) throws IOException, ParseException {
        maintenanceService.addMaintenanceExcel(maintenanceFile);
    }

    @RequestMapping("/addMaintenanceInvoice")
    public void addMaintenanceInvoice(@RequestParam(required = false) MultipartFile invoiceFile) throws IOException, ParseException {
        maintenanceService.addMaintenanceInvoice(invoiceFile);
    }

    @RequestMapping("/getMaintenanceCostsInvoices")
    public Map<String, Map<MaintenanceType, Double>> getMaintenanceCostsInvoices( @RequestParam(required = false) List<String> numberplates, @RequestParam(required = false) String sdate1, @RequestParam(required = false) String sdate2) throws ParseException {
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(sdate1);
        Date date2=new SimpleDateFormat("dd-MM-yyyy").parse(sdate2);
        Map<String, Map<MaintenanceType, Double>> costVehicles = new HashMap<>();
        for(String np : numberplates){
            Map<MaintenanceType, Double> cost = maintenanceService.getMaintenanceCostsInvoices(np, date1, date2);
            costVehicles.put(np, cost);
        }
        return costVehicles;
    }

    @RequestMapping("/getMaintenanceCostsExcel")
    public Map<String, Map<Date, Double>> getMaintenanceCostsExcel( @RequestParam(required = false) List<String> numberplates, @RequestParam(required = false) String sdate1, @RequestParam(required = false) String sdate2) throws ParseException {
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(sdate1);
        Date date2=new SimpleDateFormat("dd-MM-yyyy").parse(sdate2);
        Map<String, Map<Date, Double>> costVehicles = new HashMap<>();
        for(String np : numberplates){
            Map<Date, Double> cost = maintenanceService.getMaintenanceCostsExcel(np, date1, date2);
            costVehicles.put(np, cost);
        }
        return costVehicles;
    }

    @RequestMapping("/CreateTPOTCsvFile")
    public void CreateTPOTCsvFile() throws IOException {
        maintenanceService.createTPOTCsvFile();
    }
}
