package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.*;
import com.example.demo.SmartDashboard.Repository.ActivitiesTrailerStartToStopRepo;
import com.example.demo.SmartDashboard.Repository.ActivitiesTrailersSensolusRepo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ActivitiesTrailersService {

    @Autowired
    private ActivitiesTrailersSensolusRepo activitiesTrailersSensolusRepo;

    @Autowired
    private ActivitiesTrailerStartToStopRepo activitiesTrailerStartToStopRepo;

    @Autowired
    private VehicleService vehicleService;

    public List<ActivityTrailersSensolus> findAll() {
        return this.activitiesTrailersSensolusRepo.findAll();
    }

    public void save(ActivityTrailersSensolus activityTrailer){ activitiesTrailersSensolusRepo.save(activityTrailer);}

    public void save(ActivityTrailerStartToStop activityTrailerStartToStop){
        activitiesTrailerStartToStopRepo.save(activityTrailerStartToStop);
    }

    public void addActivitiesTrailersExcel(MultipartFile activitiesDataFile) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(1);

        // Load new trailers found in excel file.
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++){
            XSSFRow row = worksheet.getRow(i);
            String numberPlateCell = row.getCell(2).getStringCellValue();
            String sensolusSerial = row.getCell(0).getStringCellValue();
            if ((!vehicleService.existBySensolusSerial(sensolusSerial)) && !sensolusSerial.matches("")) {
                vehicleService.save(new Trailer(numberPlateCell, sensolusSerial));
            }
        }

        List<Vehicle> vehicles = vehicleService.findAll();
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            ActivityTrailersSensolus tempActivity = new ActivityTrailersSensolus();
            XSSFRow row = worksheet.getRow(i);

            if(i % 100 == 0) {
                System.out.println("progress file row: " + i + " / " + worksheet.getPhysicalNumberOfRows());
            }

            String numberPlateCell = row.getCell(2).getStringCellValue();
            String sensolusSerial = row.getCell(0).getStringCellValue();
            for (Vehicle v : vehicles) {
                if(v instanceof Trailer) {
                    if (((Trailer) v).getSensolusSerial().matches(sensolusSerial) && (v.getNumberPlate().matches(numberPlateCell))) {
                        tempActivity.setTrailer((Trailer) v);
                        tempActivity.setSerial(row.getCell(0).getStringCellValue());
                        tempActivity.setNumberPlate(row.getCell(2).getStringCellValue());
                        tempActivity.setTimeStamp(row.getCell(3).getDateCellValue());
                        tempActivity.setState(row.getCell(4).getStringCellValue());
                        tempActivity.setLat(row.getCell(5).getNumericCellValue());
                        tempActivity.setLon(row.getCell(6).getNumericCellValue());
                        tempActivity.setGeozone(row.getCell(7).getStringCellValue());
                        tempActivity.setAccuracy((int) row.getCell(8).getNumericCellValue());
                        tempActivity.setAddress(row.getCell(11).getStringCellValue());
                        tempActivity.setTrailer((Trailer) v);
                        save(tempActivity);
                    }
                }
            }
        }
    }

    public Map<String, Double> trailerIdleTime(Trailer t, Date startDate, Date stopDate){
        Trailer trailer = (Trailer) vehicleService.findVehicleByNumberPlate(t.getNumberPlate());
        List<ActivityTrailerStartToStop> activitiesStartToStop =  trailer.getActivitiesStartToStop();
        List<ActivityTrailerStartToStop> activitiesStartToStopInPeriod = new ArrayList<>();
        Map<String, Double> idleTimeMap = new HashMap<>();
        Map<Date, Invoice> invoices = new TreeMap<>();
        double totalIdleMinutes = 0;
        double idleTimeMinutes = 0;
        String idleAddress = "";

        for(ActivityTrailerStartToStop activityStartToStop : activitiesStartToStop){
            Date startTimeActivity = activityStartToStop.getStartSensolus().getTimeStamp();
            if(startTimeActivity.after(startDate) && startTimeActivity.before(stopDate)){
                activitiesStartToStopInPeriod.add(activityStartToStop);
            }
        }

        Collections.sort(activitiesStartToStopInPeriod);
        boolean firstActivity = true;
        ActivityTrailerStartToStop previousActivity = null;
        for(ActivityTrailerStartToStop a : activitiesStartToStopInPeriod){
            Double durationDouble;
            System.out.println(a.getStartSensolus().getTimeStamp().toString());

            if(firstActivity){
                Date startTimeActivity = a.getStartSensolus().getTimeStamp();
                idleAddress = a.getStartSensolus().getAddress();
                Long duration =  Math.abs(startTimeActivity.getTime() - startDate.getTime());
                Long diffDuration = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
                durationDouble = diffDuration.doubleValue();
                trailer.getIdleLocationTimes().put(idleAddress,durationDouble);
                vehicleService.save(trailer);
                previousActivity = a;
                firstActivity = false;
            }
            else{
                Date startTimeActivity = a.getStartSensolus().getTimeStamp();
                idleAddress = a.getStartSensolus().getAddress();
                System.out.println("start idle Time: "+previousActivity.getStopSensolus().getTimeStamp());
                System.out.println("start idle time getTime: "+previousActivity.getStopSensolus().getTimeStamp());
                System.out.println("start time activity time: "+startTimeActivity.getTime());
                Long duration =  Math.abs(startTimeActivity.getTime() - previousActivity.getStopSensolus().getTimeStamp().getTime());
                System.out.println("duration: "+duration);
                Long diffDuration = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
                System.out.println("diffDuration: "+diffDuration);
                durationDouble = diffDuration.doubleValue();
                System.out.println("durationDouble: "+durationDouble);

                trailer.getIdleLocationTimes().put(idleAddress,durationDouble);
                vehicleService.save(trailer);

                previousActivity = a;
            }

            totalIdleMinutes += durationDouble;
            idleTimeMap.put(idleAddress, durationDouble);
        }

        return idleTimeMap;


    }
}
