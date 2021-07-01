package com.example.demo.SmartDashboard.Controller;

import com.example.demo.SmartDashboard.Model.*;

import com.example.demo.SmartDashboard.Service.ActivitiesTrailersService;
import com.example.demo.SmartDashboard.Service.ActivitiesTrucksService;
import com.example.demo.SmartDashboard.Service.VehicleService;
import com.example.demo.SmartDashboard.TransicsClient.TransicsClient;
import com.example.demo.SmartDashboard.TransicsClient.gen.GetActivityReportDetailV12Response;
import com.example.demo.SmartDashboard.TransicsClient.gen.GetVehicleStatusResponse;
import com.example.demo.SmartDashboard.TransicsClient.gen.GetVehiclesV13Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ActivitiesTrailersService activitiesTrailersService;
    @Autowired
    private ActivitiesTrucksService activitiesTrucksService;
    @Autowired
    private TransicsClient transicsClient;

    @GetMapping("/vehicles")
    public List<Vehicle> getVehicles() {
        return vehicleService.findAll();
    }

    @RequestMapping("/addVehicle")
    public void addVehicle(@RequestParam(required = false) String numberPlate, @RequestParam(required = false) String chassis,
                           @RequestParam(required = false) String sensolusSerial, @RequestParam(required = false) String type){

        vehicleService.addVehicle(numberPlate, chassis,sensolusSerial, type);
    }

    @RequestMapping("/addActivity")
    public String addActivity(@RequestParam(required = false) String np){
        List<Vehicle> vehicles = vehicleService.findAll();
        for(Vehicle v : vehicles) {
            if(v.getNumberPlate().matches(np)){
                System.out.println(v.toString());
                if(v instanceof Trailer){
                    ActivityTrailers a = new ActivityTrailers((Trailer) v);
                    ((Trailer) v).addActivity(a);
                    System.out.println("Trailer activity added");
                    vehicleService.save(v);
                }
                else if (v instanceof Truck){
                    ActivityTrucks a = new ActivityTrucks();
                    ((Truck) v).addActivity(a);
                    System.out.println("Add activity to truck");
                    vehicleService.save(v);
                }
            }
        }
        return "activity added";
    }

    @RequestMapping("/addActivitiesExcel")
    public void addActivitiesExcel(@RequestParam(required = false) MultipartFile activitiesDataFile ) throws IOException {
         activitiesTrailersService.addActivitiesTrailersExcel(activitiesDataFile);
    }

    @RequestMapping("/addTruckActivitiesExcel")
    public void addTruckActivitiesExcel(@RequestParam(required = false) MultipartFile activitiesDataFile) throws IOException {
        //vehicleService.addTruckActivitiesExcel(activitiesDataFile);
        activitiesTrucksService.addTruckActivitiesExcel(activitiesDataFile);
    }

    @RequestMapping("readMaintenance")
    public void readMaintenance() throws IOException{
        PDDocument document = PDDocument.load(new File("src/main/resources/DataTM/MaintenanceInvoices/factuur2.pdf"));
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            System.out.println("Text:" + text);
        }
        document.close();
    }

    @RequestMapping("addCost")
    public void addCost(@RequestParam(required = false) String np, @RequestParam(required = false) double cost, @RequestParam(required = false) String date){
        List<Vehicle> vehicles = vehicleService.findAll();
        for(Vehicle v : vehicles) {
            if(v.getNumberPlate().matches(np)) {
                v.addMaintenanceCost(date, cost);
                vehicleService.save(v);
            }
        }
    }

    @RequestMapping("getActivities")
    public void getActivities(@RequestParam(required = false) String numberPlate){
        List<Vehicle> vehicles = vehicleService.findAll();
        for(Vehicle v : vehicles) {
            if(v.getNumberPlate().matches(numberPlate)) {
                System.out.println(((Trailer) v).getActivities().size());
            }
        }
    }

    @RequestMapping(value = "getVehiclesAPI")
    public GetVehiclesV13Response getVehiclesAPI() throws IOException {
        return transicsClient.getVehicles();
    }

    @RequestMapping(value = "getVehicleStatusAPI")
    public GetVehicleStatusResponse getVehicleStatusAPI() throws IOException {
        return transicsClient.getVehicleStatus();
    }

    @RequestMapping(value = "updateTruckActivityAPI")
    public GetActivityReportDetailV12Response getActivityAPI() throws DatatypeConfigurationException {
        return transicsClient.updateTruckActivity();
    }

    @RequestMapping(value = "coupleTrailerToTrucks")
    public void coupleTrailersToTrucks() throws IOException, ParseException, DatatypeConfigurationException, InterruptedException {
        vehicleService.coupleTrailersToTrucks();
    }

    @RequestMapping("changeClass")
    public void changeClass(){
        Vehicle v = new Vehicle();
        Trailer t = (Trailer) v;
    }

    @RequestMapping(value = "filterJSONFiles")
    public void filterJSONFiles() throws IOException, ParseException {
        File[] files = new File("src/main/resources/DataTM/SensolusActivityWebhooks").listFiles((dir, name) -> !name.equals(".DS_Store"));
        int counter = 0;
        List<DateTime> insertTimes = new ArrayList<>();
        insertTimes.add(new DateTime("2021-06-26T16:56:52+0000"));
        for(File file: files) {
        String filePath = "src/main/resources/DataTM/SensolusActivityWebhooks/" + file.getName();

            JSONParser parser = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
            Object obj = parser.parse(fileReader);

            JSONObject jsonObject = (JSONObject) obj;
            JSONObject dataObject = (JSONObject) jsonObject.get("data");
            DateTime time = new DateTime(dataObject.get("time"));
            String numberPlate = dataObject.get("thirdPartyId").toString();
            if(numberPlate.matches("1-QFB-450")){
                if(!insertTimes.contains(time)) {
                    counter++;
                    Path src = Paths.get(filePath);
                    Path dest = Paths.get("src/main/resources/DataTM/selectionJSON/file" + counter + ".json");
                    Files.copy(src, dest);
                    insertTimes.add(time);
                }
            }
            fileReader.close();
        }
    }

    @RequestMapping(value = "sortJSONFiles")
    public void sortJsonFiles() throws IOException, ParseException {
        File[] files = new File("src/main/resources/DataTM/SelectionJSON").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file: files) {
            String filePath = "src/main/resources/DataTM/SelectionJSON/" + file.getName();

            JSONParser parser = new JSONParser();
            FileReader fileReader = new FileReader(filePath);
            Object obj = parser.parse(fileReader);

            JSONObject jsonObject = (JSONObject) obj;
            JSONObject dataObject = (JSONObject) jsonObject.get("data");
            DateTime time = new DateTime(dataObject.get("time"));
            Path src = Paths.get(filePath);
            Path dest = Paths.get("src/main/resources/DataTM/selectionJSONSorted/"+time+".json");
            Files.copy(src, dest);
            fileReader.close();
        }
    }
}
