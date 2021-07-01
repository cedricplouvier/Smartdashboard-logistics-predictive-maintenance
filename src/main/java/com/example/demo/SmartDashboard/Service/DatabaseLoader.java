package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DatabaseLoader {

    @Autowired
    private final VehicleService vehicleService;

    @Autowired
    private final ActivitiesTrailersService activitiesTrailersService;

    @Autowired
    private final ActivitiesTrucksService activitiesTrucksService;

    public DatabaseLoader(VehicleService vehicleService, ActivitiesTrailersService activitiesTrailersService,
                          ActivitiesTrucksService activitiesTrucksService) {
        this.vehicleService = vehicleService;
        this.activitiesTrailersService = activitiesTrailersService;
        this.activitiesTrucksService = activitiesTrucksService;
    }

    @PostConstruct
    public void addTrucks(){
        vehicleService.save(new Truck("1-AJY-385"));
        vehicleService.save(new Truck("1-AJY-424"));
        vehicleService.save(new Truck("1-AJY-460"));
        vehicleService.save(new Truck("1-AMD-094"));
        vehicleService.save(new Truck("1-BCD-407"));
        vehicleService.save(new Truck("1-BCD-449"));
        vehicleService.save(new Truck("1-BCD-476"));
        vehicleService.save(new Truck("1-BJY-016"));
        vehicleService.save(new Truck("1-BLH-016"));
        vehicleService.save(new Truck("1-CAU-656"));
        vehicleService.save(new Truck("1-CMJ-555"));
        vehicleService.save(new Truck("1-CMJ-558"));
        vehicleService.save(new Truck("1-CMJ-560"));
        vehicleService.save(new Truck("1-CMJ-562"));
        vehicleService.save(new Truck("1-CMJ-564"));
        vehicleService.save(new Truck("1-CMJ-565"));
        vehicleService.save(new Truck("1-CMJ-567"));
        vehicleService.save(new Truck("1-CMJ-409"));
        vehicleService.save(new Truck("1-CML-409"));
        vehicleService.save(new Truck("1-CML-431"));
        vehicleService.save(new Truck("1-CMR-215"));
        vehicleService.save(new Truck("1-CSQ-755"));
        vehicleService.save(new Truck("1-CSX-948"));
        vehicleService.save(new Truck("1-CVZ-357"));
        vehicleService.save(new Truck("1-CYL-104"));
        vehicleService.save(new Truck("1-CYL-645"));
        vehicleService.save(new Truck("1-CYS-110"));
        vehicleService.save(new Truck("1-CYS-557"));
        vehicleService.save(new Truck("1-CYS-614"));
        vehicleService.save(new Truck("1-CYY-845"));
        vehicleService.save(new Truck("1-CYY-846"));
        vehicleService.save(new Truck("1-CYY-875"));
        vehicleService.save(new Truck("1-CYZ-493"));
        vehicleService.save(new Truck("1-DPO-957"));
        vehicleService.save(new Truck("1-DYF-715"));
        vehicleService.save(new Truck("1-EJV-057"));
        vehicleService.save(new Truck("1-EJV-172"));
        vehicleService.save(new Truck("1-EJV-206"));
        vehicleService.save(new Truck("1-EJV-238"));
        vehicleService.save(new Truck("1-EJV-371"));
        vehicleService.save(new Truck("1-ENJ-231"));
        vehicleService.save(new Truck("1-ESV-320"));
        vehicleService.save(new Truck("1-FTG-299"));
        vehicleService.save(new Truck("1-FYB-597"));
        vehicleService.save(new Truck("1-FYB-598"));
        vehicleService.save(new Truck("1-FYB-697"));
        vehicleService.save(new Truck("1-FYB-699"));
        vehicleService.save(new Truck("1-FYB-848"));
        vehicleService.save(new Truck("1-FYB-849"));
        vehicleService.save(new Truck("1-FYN-382"));
        vehicleService.save(new Truck("1-FYN-383"));
        vehicleService.save(new Truck("1-GAD-267"));
        vehicleService.save(new Truck("1-GNZ-388"));
        vehicleService.save(new Truck("1-GTK-965"));
        vehicleService.save(new Truck("1-HAS-174"));
        vehicleService.save(new Truck("1-HDY-986"));
        vehicleService.save(new Truck("1-HDZ-006"));
        vehicleService.save(new Truck("1-HYC-252"));
        vehicleService.save(new Truck("1-JJK-251"));
        vehicleService.save(new Truck("1-JJK-278"));
        vehicleService.save(new Truck("1-JJT-140"));
        vehicleService.save(new Truck("1-JTP-518"));
        vehicleService.save(new Truck("1-JUR-059"));
        vehicleService.save(new Truck("1-JUR-087"));
        vehicleService.save(new Truck("1-JVD-472"));
        vehicleService.save(new Truck("1-KHJ-253"));
        vehicleService.save(new Truck("1-KJE-804"));
        vehicleService.save(new Truck("1-LFH-745"));
        vehicleService.save(new Truck("1-LKF-702"));
        vehicleService.save(new Truck("1-NAD-469"));
        vehicleService.save(new Truck("1-NAD-510"));
        vehicleService.save(new Truck("1-NAN-190"));
        vehicleService.save(new Truck("1-NAN-199"));
        vehicleService.save(new Truck("1-NBB-536"));
        vehicleService.save(new Truck("1-NBK-160"));
        vehicleService.save(new Truck("1-NBK-190"));
        vehicleService.save(new Truck("1-NCU-229"));
        vehicleService.save(new Truck("1-NGN-109"));
        vehicleService.save(new Truck("1-NNX-077"));
        vehicleService.save(new Truck("1-PBB-127"));
        vehicleService.save(new Truck("1-PBV-686"));
        vehicleService.save(new Truck("1-PEY-930"));
        vehicleService.save(new Truck("1-PFH-424"));
        vehicleService.save(new Truck("1-PJY-082"));
        vehicleService.save(new Truck("1-PKF-787"));
        vehicleService.save(new Truck("1-PKU-465"));
        vehicleService.save(new Truck("1-PLC-194"));
        vehicleService.save(new Truck("1-PLN-850"));
        vehicleService.save(new Truck("1-PNX-263"));
        vehicleService.save(new Truck("1-PRG-200"));
        vehicleService.save(new Truck("1-PSA-348"));
        vehicleService.save(new Truck("1-PYD-675"));
        vehicleService.save(new Truck("1-PZR-797"));
        vehicleService.save(new Truck("1-RAA-510"));
        vehicleService.save(new Truck("1-RGB-122"));
        vehicleService.save(new Truck("1-RHV-185"));
        vehicleService.save(new Truck("1-RHV-217"));
        vehicleService.save(new Truck("1-RJZ-840"));
        vehicleService.save(new Truck("1-RKP-040"));
        vehicleService.save(new Truck("1-RKP-047"));
        vehicleService.save(new Truck("1-RKP-053"));
        vehicleService.save(new Truck("1-RKT-117"));
        vehicleService.save(new Truck("1-RKV-011"));
        vehicleService.save(new Truck("1-RLJ-454"));
        vehicleService.save(new Truck("1-RLJ-496"));
        vehicleService.save(new Truck("1-RLJ-506"));
        vehicleService.save(new Truck("1-RLX-885"));
        vehicleService.save(new Truck("1-RNH-779"));
        vehicleService.save(new Truck("1-RNN-213"));
        vehicleService.save(new Truck("1-RPL-123"));
        vehicleService.save(new Truck("1-RSN-160"));
        vehicleService.save(new Truck("1-RSS-676"));
        vehicleService.save(new Truck("1-RTH-212"));
        vehicleService.save(new Truck("1-RTY-224"));
        vehicleService.save(new Truck("1-RUN-232"));
        vehicleService.save(new Truck("1-RVK-593"));
        vehicleService.save(new Truck("1-RVT-314"));
        vehicleService.save(new Truck("1-RXK-029"));
        vehicleService.save(new Truck("1-SFA-763"));
        vehicleService.save(new Truck("1-SGD-087"));
        vehicleService.save(new Truck("1-STY-980"));
        vehicleService.save(new Truck("1-SUR-020"));
        vehicleService.save(new Truck("1-TAE-712"));
        vehicleService.save(new Truck("1-TAY-626"));
        vehicleService.save(new Truck("1-TES-509"));
        vehicleService.save(new Truck("1-TPJ-930"));
        vehicleService.save(new Truck("1-TPJ-951"));
        vehicleService.save(new Truck("1-TVV-595"));
        vehicleService.save(new Truck("1-UAE-428"));
        vehicleService.save(new Truck("1-UCK-371"));
        vehicleService.save(new Truck("1-UCN-603"));
        vehicleService.save(new Truck("1-UER-402"));
        vehicleService.save(new Truck("1-UER-418"));
        vehicleService.save(new Truck("1-UER-435"));
        vehicleService.save(new Truck("1-UFN-942"));
        vehicleService.save(new Truck("1-URK-559"));
        vehicleService.save(new Truck("1-URP-720"));
        vehicleService.save(new Truck("1-VAU-129"));
        vehicleService.save(new Truck("1-VBR-109"));
        vehicleService.save(new Truck("1-VBV-568"));
        vehicleService.save(new Truck("1-VCQ-653 (Smits Transport Logistics"));
        vehicleService.save(new Truck("1-VDK-362 (Smits Transport Logistics"));
        vehicleService.save(new Truck("1-VQW-435"));
        vehicleService.save(new Truck("1-VQW-442"));
        vehicleService.save(new Truck("1-VRH-703"));
        vehicleService.save(new Truck("1-VRH-710"));
        vehicleService.save(new Truck("1-VRH-724"));
        vehicleService.save(new Truck("1-VRK-426 (Smits Transport Logistics"));
        vehicleService.save(new Truck("1-VWU-464"));
        vehicleService.save(new Truck("1-WEN-832"));
        vehicleService.save(new Truck("1-WFA-741"));
        vehicleService.save(new Truck("1-WFZ-344"));
        vehicleService.save(new Truck("1-WHV-090 (Smits Transport Logistics"));
        vehicleService.save(new Truck("1-WUB-814"));
        vehicleService.save(new Truck("1-WUB-828"));
        vehicleService.save(new Truck("1-WUR-125"));
        vehicleService.save(new Truck("1-WXQ-593"));
        vehicleService.save(new Truck("1-XBG-081"));
        vehicleService.save(new Truck("1-XCG-114"));
        vehicleService.save(new Truck("1-XCG-118"));
        vehicleService.save(new Truck("1-XCH-299"));
        vehicleService.save(new Truck("1-XDN-355"));
        vehicleService.save(new Truck("1-XEA-406"));
        vehicleService.save(new Truck("1-XER-314"));
        vehicleService.save(new Truck("1-XER-318"));
        vehicleService.save(new Truck("1-XER-329"));
        vehicleService.save(new Truck("1-XER-337"));
        vehicleService.save(new Truck("1-XER-353"));
        vehicleService.save(new Truck("1-XER-361"));
        vehicleService.save(new Truck("1-XFT-994"));
        vehicleService.save(new Truck("1-XFU-005"));
        vehicleService.save(new Truck("1-XFU-017"));
        vehicleService.save(new Truck("1-XFU-023"));
        vehicleService.save(new Truck("1-XFU-029"));
        vehicleService.save(new Truck("1-XFU-039"));
        vehicleService.save(new Truck("1-XFU-049"));
        vehicleService.save(new Truck("1-XFU-066"));
        vehicleService.save(new Truck("1-XFU-077"));
        vehicleService.save(new Truck("1-XGP-732"));
        vehicleService.save(new Truck("1-XJD-449"));
        vehicleService.save(new Truck("1-XJD-459"));
        vehicleService.save(new Truck("1-XKL-832"));
        vehicleService.save(new Truck("1-XKV-189"));
        vehicleService.save(new Truck("1-XLZ-934"));
        vehicleService.save(new Truck("1-XNC-070"));
        vehicleService.save(new Truck("1-XSA-364"));
        vehicleService.save(new Truck("1-XSH-675"));
        vehicleService.save(new Truck("1-XVD-733"));
        vehicleService.save(new Truck("1-YCR-446"));
        vehicleService.save(new Truck("1-YGY-340"));
        vehicleService.save(new Truck("1-YHR-569"));
        vehicleService.save(new Truck("1-YJP-482"));
        vehicleService.save(new Truck("1-YPE-940"));
        vehicleService.save(new Truck("2-AAW-450"));
        vehicleService.save(new Truck("2-AGG-933"));
        vehicleService.save(new Truck("2-AGG-951"));
        vehicleService.save(new Truck("2-AJE-438"));
        vehicleService.save(new Truck("2-AJE-461"));
        vehicleService.save(new Truck("2-ANP-687"));
        vehicleService.save(new Truck("184-BTW"));
        vehicleService.save(new Truck("272-BQY"));
        vehicleService.save(new Truck("275-BQY"));
        vehicleService.save(new Truck("405-BND"));
        vehicleService.save(new Truck("433-BND"));
        vehicleService.save(new Truck("439-BND"));
        vehicleService.save(new Truck("442-CBK"));
        vehicleService.save(new Truck("591-BMP"));
        vehicleService.save(new Truck("802-BNG"));
        vehicleService.save(new Truck("833-CBA"));
        vehicleService.save(new Truck("835-CBA"));
        vehicleService.save(new Truck("838-CBA"));
        vehicleService.save(new Truck("935-BJL"));
        vehicleService.save(new Truck("954-BUE"));
        vehicleService.save(new Truck("MOB1"));
        vehicleService.save(new Truck("MOB2"));
        vehicleService.save(new Truck("MOB3"));
        vehicleService.save(new Truck("MOB4"));
        vehicleService.save(new Truck("MOB5"));
        vehicleService.save(new Truck("MOB6"));
        vehicleService.save(new Truck("MOB7"));
        vehicleService.save(new Truck("MOB8"));
        vehicleService.save(new Truck("MOB11"));
        vehicleService.save(new Truck("MOB12"));
        vehicleService.save(new Truck("MOB13"));
        vehicleService.save(new Truck("PDA96823"));
        vehicleService.save(new Truck("PDA96849"));
        vehicleService.save(new Truck("SEE-729"));
        vehicleService.save(new Truck("TRX_V"));
        vehicleService.save(new Truck("XFV-539"));
        vehicleService.save(new Truck("XIC-790"));
        vehicleService.save(new Truck("XJY-459"));
        vehicleService.save(new Truck("XJY-618"));
        vehicleService.save(new Truck("YJQ-568"));
        vehicleService.save(new Truck("YRM-983"));
        vehicleService.save(new Truck("YRT-645"));
        vehicleService.save(new Truck("YUC-744"));
        vehicleService.save(new Truck("YUV-814"));
        vehicleService.save(new Truck("YYA-870"));
        vehicleService.save(new Truck("YYX-718"));
        vehicleService.save(new Truck("YYX-719"));
        vehicleService.save(new Truck("YZD-211"));
        vehicleService.save(new Truck("YZP-598"));
    }


    @PostConstruct
    public void fillTrailerLocationDatabase() throws IOException {

        File[] files = new File("src/main/resources/DataTM/TrailerLocationExcels").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file : files) {
            System.out.println("src/main/resources/DataTM/TrailerLocationExcels/" + file.getName());
            String filePath = "src/main/resources/DataTM/TrailerLocationExcels/" + file.getName();
            MultipartFile activitiesDataFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePath)));
            XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(1);

            // Load new trailers found in excel file.

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);
                String numberPlateCell = row.getCell(2).getStringCellValue();
                String sensolusSerial = row.getCell(0).getStringCellValue();

                if ((!vehicleService.existBySensolusSerial(sensolusSerial)) && !sensolusSerial.matches("")) {
                    vehicleService.save(new Trailer(numberPlateCell, sensolusSerial));
                }
            }

            List<Vehicle> vehicles = vehicleService.findAll();

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                ActivityTrailers tempActivity = new ActivityTrailers();
                XSSFRow row = worksheet.getRow(i);

                if(i % 1000 == 0) {
                    System.out.println("progress file " + file.getName() + " row: " + i + " / " + worksheet.getPhysicalNumberOfRows());
                }
                String numberPlateCell = row.getCell(2).getStringCellValue();
                String sensolusSerial = row.getCell(0).getStringCellValue();
                for (Vehicle v : vehicles) {
                    if (v instanceof Trailer) {
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
                            activitiesTrailersService.save(tempActivity);
                        }
                    }
                }
            }
        }
    }

    @PostConstruct
    public void fillMaintenanceReportsDatabase() throws IOException, ParseException {

        File[] files = new File("src/main/resources/DataTM/MaintenanceReports").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file : files) {
            System.out.println("src/main/resources/DataTM/MaintenanceReports/" + file.getName());
            String filePath = "src/main/resources/DataTM/MaintenanceReports/" + file.getName();
            MultipartFile activitiesDataFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePath)));
            XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            XSSFSheet worksheet2 = workbook.getSheetAt(1);

            // Problem of creating new: from maintenanceFile unable to know is Trailer or Truck
            /*for (int i = 4; i < worksheet.getPhysicalNumberOfRows(); i++) {
                List<Vehicle> vehicles = vehicleService.findAll();
                boolean duplicate = false;
                XSSFRow row = worksheet.getRow(i);
                String numberPlateCell = row.getCell(0).getStringCellValue();
                if (!(vehicleService.existVehicleByNumberPlatte(numberPlateCell))) {
                    vehicleService.save(new Vehicle(numberPlateCell));
                }
            }*/

            String dateString = worksheet2.getRow(3).getCell(5).getStringCellValue();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(dateString);
            String parsedDateString = new SimpleDateFormat("MM-yyyy").format(date);

            for(int i = 4; i < worksheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = worksheet.getRow(i);
                String numberPlate = row.getCell(0).getStringCellValue();
                if(vehicleService.existVehicleByNumberPlatte(numberPlate)) {
                    Vehicle v = vehicleService.findVehicleByNumberPlate(numberPlate);
                    if (row.getCell(1).getCellType().name().matches("STRING") && !(v.getNumberPlate().matches(""))) {
                        v.setChassisNumber((row.getCell(1).getStringCellValue()));
                    }
                    else if (row.getCell(1).getCellType().name().matches("NUMERIC") && !(v.getNumberPlate().matches(""))){
                        v.setChassisNumber(String.valueOf((int) row.getCell(1).getNumericCellValue()));
                    }
                    v.addMaintenanceCost(parsedDateString, row.getCell(2).getNumericCellValue());
                    vehicleService.save(v);
                }
            }
        }
    }

    //@PostConstruct
    public void fillActivitiesTrucksDatabase() throws IOException, ParseException {
        File[] files = new File("src/main/resources/DataTM/TrekkerActivityExcels").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file : files){
            System.out.println("src/main/resources/DataTM/TrekkerActivityExcels/" + file.getName());
            String filePath = "src/main/resources/DataTM/TrekkerActivityExcels/" + file.getName();
            MultipartFile activitiesDataFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePath)));
            XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            // Takes too long
            /*
            //Check if there are nay new vehicles that are not yet registered
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);
                String numberPlateCell = row.getCell(6).getStringCellValue();

                if ((!vehicleService.existVehicleByNumberPlatte(numberPlateCell)) && !numberPlateCell.matches("")) {
                    vehicleService.save(new Truck(numberPlateCell));
                }
            }
            */

            List<Vehicle> vehicles = vehicleService.findAll();

            for(int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++){
                ActivityTrucks activityTruck = new ActivityTrucks();
                XSSFRow row = worksheet.getRow(i);

                String numberPlateCell = row.getCell(6).getStringCellValue();

                if(i % 10000 == 0) {
                    System.out.println("progress file " + file.getName() + " row: " + i + " / " + worksheet.getPhysicalNumberOfRows());
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

                                activitiesTrucksService.save(activityTruck);
                                vehicleService.save(v);

                            }
                        }
                    }
                }
            }
        }
    }
}
