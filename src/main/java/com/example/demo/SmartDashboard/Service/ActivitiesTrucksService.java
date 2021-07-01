package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.ActivityTrailers;
import com.example.demo.SmartDashboard.Model.ActivityTrucks;
import com.example.demo.SmartDashboard.Model.Truck;
import com.example.demo.SmartDashboard.Model.Vehicle;
import com.example.demo.SmartDashboard.Repository.ActivitiesTrucksRepo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ActivitiesTrucksService {

    @Autowired
    private ActivitiesTrucksRepo activitiesTrucksRepo;

    @Autowired
    private VehicleService vehicleService;

    public List<ActivityTrucks> findAll() {
        return this.activitiesTrucksRepo.findAll();
    }

    public void save(ActivityTrucks activityTrucks){ activitiesTrucksRepo.save(activityTrucks);}

    public void addTruckActivitiesExcel(MultipartFile activitiesDataFile) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        List<Vehicle> vehicles = vehicleService.findAll();

        for(int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++){
            ActivityTrucks activityTruck = new ActivityTrucks();
            XSSFRow row = worksheet.getRow(i);
            String numberPlateCell = row.getCell(6).getStringCellValue();

            if(i % 10000 == 0) {
                System.out.println(" row: " + i + " / " + worksheet.getPhysicalNumberOfRows());
            }

            for(Vehicle v : vehicles){
                if(v instanceof Truck){
                    if(v.getNumberPlate().matches(numberPlateCell)){
                        if(row.getCell(11).getStringCellValue().matches("Rijden")){
                            activityTruck.setTruck((Truck) v);

                            Date beginDate = row.getCell(3).getDateCellValue();
                            Date endDate = row.getCell(4).getDateCellValue();
                            activityTruck.setStartDate(beginDate);
                            activityTruck.setEndDate(endDate);
                            activityTruck.setDistance((int) row.getCell(7).getNumericCellValue());
                            activityTruck.setAverageSpeedActivity(row.getCell(12).getNumericCellValue());
                            v.setMileage((int) row.getCell(9).getNumericCellValue());

                            Long duration =  Math.abs(endDate.getTime() - beginDate.getTime());
                            Long diffDuration = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
                            Double durationDouble = diffDuration.doubleValue();
                            activityTruck.setDuration(durationDouble);

                            save(activityTruck);
                            vehicleService.save(v);
                        }
                    }
                }
            }
        }
    }
}
