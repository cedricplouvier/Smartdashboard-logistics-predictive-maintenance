package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.*;
import com.example.demo.SmartDashboard.Repository.InvoiceRepo;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.LambdaConversionException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.demo.SmartDashboard.Model.MaintenanceType.*;
import static org.junit.Assert.assertTrue;

@Service
public class MaintenanceService {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private CumulativeDataBetweenTireRepairsTrucksService cumulativeDataBetweenTireRepairsTrucksService;

    private List<String[]> dataLines = new ArrayList<>();

    public void addMaintenanceExcel (MultipartFile maintenanceFile) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook(maintenanceFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        XSSFSheet worksheet2 = workbook.getSheetAt(1);

        String dateString = worksheet2.getRow(3).getCell(5).getStringCellValue();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateI = formatter.parse(dateString);
        Calendar c = Calendar.getInstance();
        c.setTime(dateI);
        c.set(Calendar.DAY_OF_MONTH, 01);
        Date date  = c.getTime();

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
                v.addMaintenanceCost(date, row.getCell(2).getNumericCellValue());
                vehicleService.save(v);
            }
        }

    }

    public Map<MaintenanceType, Double> getMaintenanceCostsInvoices(String np, Date dateStart, Date dateEnd){
        Vehicle v = vehicleService.findVehicleByNumberPlate(np);
        Map<Date, Invoice> invoices = v.getInvoices();
        Map<MaintenanceType, Double> costOverPeriodByClass =  new HashMap<>();
        for(Map.Entry<Date,Invoice> entry : invoices.entrySet()){
            Invoice invoice = entry.getValue();
            if((invoice.getDate().after(dateStart) && invoice.getDate().before(dateEnd)) || (DateUtils.isSameDay(invoice.getDate(), dateStart)) || (DateUtils.isSameDay(invoice.getDate(), dateEnd))){
                invoice.getMaintenanceTypes().forEach((k, val) -> costOverPeriodByClass.merge(k, val, Double::sum));
            }
        }
        return costOverPeriodByClass;
    }

    public Map<Date, Double> getMaintenanceCostsExcel(String np, Date dateStart, Date dateEnd){
        Vehicle v = vehicleService.findVehicleByNumberPlate(np);
        Map<Date, Double> maintenanceCosts = v.getMaintenanceCosts();
        Map<Date, Double> maintenanceCostsForPeriod =  new HashMap<>();
        for(Map.Entry<Date,Double> entry : maintenanceCosts.entrySet()){
            if(entry.getKey().after(dateStart) && entry.getKey().before(dateEnd)){
                maintenanceCostsForPeriod.put(entry.getKey(), entry.getValue());
            }
        }
        return maintenanceCostsForPeriod;
    }

    public CumulativeDataBetweenTireRepairsTrucks calculateCumulativeDataBetweenTireRepairsTrucks(Vehicle v, Date previousRepair, Date currentRepair){

        int kilometersTravelled=0;
        int startKilometerCounter=0;
        int endKilometerCounter=0;
        Duration motorTimeHours=Duration.ZERO;
        Duration drivingTimeHours=Duration.ZERO;
        double drivingTimePercentage=0.0;
        Duration stationaryTimeHours=Duration.ZERO;
        double stationaryTimePercentage=0.0;
        double averageSpeedDriving=0.0;

        double soloPercentageDriving=0.0;
        int soloKilometersDriving=0;
        double emptyPercentageDriving=0.0;
        int emptyKilometersDriving=0;
        double unknownPercentage=0.0;
        int unknownKilometers=0;
        int loadedKilometers=0;
        double loadedPercentage=0.0;

        System.out.println("previous repair data: "+previousRepair);
        System.out.println("current rapair date: "+currentRepair);

        DateTime previousDate = new DateTime(previousRepair);
        DateTime currentDate = new DateTime(currentRepair);
        int weeksBetween = Weeks.weeksBetween(previousDate, currentDate).getWeeks();

        TreeMap<Date, TruckDailyConsumptionData> truckDailyConsumptionDataMap = new TreeMap<>(((Truck) v).getTruckDailyConsumptionDataMap());
        SortedMap<Date, TruckDailyConsumptionData> periodTruckDailyConsumptionDataMap = truckDailyConsumptionDataMap.subMap(previousRepair, currentRepair);
        System.out.println("consumption size map: "+periodTruckDailyConsumptionDataMap.size());

        CumulativeDataBetweenTireRepairsTrucks cumulativeDataBetweenTireRepairsTrucks = new CumulativeDataBetweenTireRepairsTrucks();
        if(!periodTruckDailyConsumptionDataMap.isEmpty()) {
            startKilometerCounter = periodTruckDailyConsumptionDataMap.get(periodTruckDailyConsumptionDataMap.firstKey()).getStartKilometerCounter();
            endKilometerCounter = periodTruckDailyConsumptionDataMap.get(periodTruckDailyConsumptionDataMap.lastKey()).getEndKilometerCounter();

            for (Map.Entry<Date, TruckDailyConsumptionData> entry : periodTruckDailyConsumptionDataMap.entrySet()) {
                TruckDailyConsumptionData data = entry.getValue();

                kilometersTravelled += data.getKilometersTravelled();
                //kilometersTravelled = endKilometerCounter - startKilometerCounter;
                motorTimeHours = motorTimeHours.plus(data.getMotorTimeHours());
                drivingTimeHours = drivingTimeHours.plus(data.getDrivingTimeHours());
                stationaryTimeHours = stationaryTimeHours.plus(data.getStationaryTimeHours());
                drivingTimePercentage += data.getDrivingTimePercentage();
                stationaryTimePercentage += data.getStationaryTimePercentage();
                averageSpeedDriving += data.getAverageSpeedDriving();
            }

            drivingTimePercentage = drivingTimePercentage / periodTruckDailyConsumptionDataMap.size();
            stationaryTimePercentage = stationaryTimePercentage / periodTruckDailyConsumptionDataMap.size();
            averageSpeedDriving = averageSpeedDriving / periodTruckDailyConsumptionDataMap.size();

            TreeMap<Date, TruckDailyLoadedData> truckDailyLoadedDataMap = new TreeMap<>(((Truck) v).getTruckDailyLoadedDataMap());
            SortedMap<Date, TruckDailyLoadedData> periodTruckDailyLoadedDataMap = truckDailyLoadedDataMap.subMap(previousRepair, currentRepair);

            for (Map.Entry<Date, TruckDailyLoadedData> entry : periodTruckDailyLoadedDataMap.entrySet()) {
                TruckDailyLoadedData data = entry.getValue();

                soloPercentageDriving += data.getSoloPercentageDriving();
                soloKilometersDriving += data.getSoloKilometersDriving();
                emptyPercentageDriving += data.getEmptyPercentageDriving();
                emptyKilometersDriving += data.getEmptyKilometersDriving();
                unknownPercentage += data.getUnknownPercentage();
                unknownKilometers += data.getUnknownKilometers();
                loadedKilometers += data.getLoadedKilometers();
                loadedPercentage += data.getLoadedPercentage();
            }


            // don't divide by map size only by size where kilometerstravelle is not 0
            soloPercentageDriving = soloPercentageDriving / periodTruckDailyLoadedDataMap.size();
            emptyPercentageDriving = emptyPercentageDriving / periodTruckDailyLoadedDataMap.size();
            unknownPercentage = unknownPercentage / periodTruckDailyLoadedDataMap.size();
            loadedPercentage = loadedPercentage / periodTruckDailyLoadedDataMap.size();

            cumulativeDataBetweenTireRepairsTrucks.setWeeksBetween(weeksBetween);
            cumulativeDataBetweenTireRepairsTrucks.setKilometersTravelled(kilometersTravelled);
            cumulativeDataBetweenTireRepairsTrucks.setStartKilometerCounter(startKilometerCounter);
            cumulativeDataBetweenTireRepairsTrucks.setEndKilometerCounter(endKilometerCounter);
            cumulativeDataBetweenTireRepairsTrucks.setDrivingTimePercentage(drivingTimePercentage);
            cumulativeDataBetweenTireRepairsTrucks.setMotorTimeHours(motorTimeHours);
            cumulativeDataBetweenTireRepairsTrucks.setDrivingTimeHours(drivingTimeHours);
            cumulativeDataBetweenTireRepairsTrucks.setStationaryTimeHours(stationaryTimeHours);
            cumulativeDataBetweenTireRepairsTrucks.setStationaryTimePercentage(stationaryTimePercentage);
            cumulativeDataBetweenTireRepairsTrucks.setAverageSpeedDriving(averageSpeedDriving);

            cumulativeDataBetweenTireRepairsTrucks.setSoloPercentageDriving(soloPercentageDriving);
            cumulativeDataBetweenTireRepairsTrucks.setSoloKilometersDriving(soloKilometersDriving);
            cumulativeDataBetweenTireRepairsTrucks.setEmptyPercentageDriving(emptyPercentageDriving);
            cumulativeDataBetweenTireRepairsTrucks.setEmptyKilometersDriving(emptyKilometersDriving);
            cumulativeDataBetweenTireRepairsTrucks.setUnknownPercentage(unknownPercentage);
            cumulativeDataBetweenTireRepairsTrucks.setUnknownKilometers(unknownKilometers);
            cumulativeDataBetweenTireRepairsTrucks.setLoadedKilometers(loadedKilometers);
            cumulativeDataBetweenTireRepairsTrucks.setLoadedPercentage(loadedPercentage);

            cumulativeDataBetweenTireRepairsTrucks.setNumberplate(v.getNumberPlate());
            cumulativeDataBetweenTireRepairsTrucks.setDatePreviousRepair(previousRepair);
            cumulativeDataBetweenTireRepairsTrucks.setDateCurrentRepair(currentRepair);
            cumulativeDataBetweenTireRepairsTrucks.setTruck((Truck) v);
        }
        else{
            cumulativeDataBetweenTireRepairsTrucks.setNumberplate("noValidCumulationPeriod");
        }
        return cumulativeDataBetweenTireRepairsTrucks;
    }

    public void addMaintenanceInvoice(MultipartFile invoiceFile) throws IOException, ParseException {

        double prijsSproeier = 0.0;
        double prijsRadiator = 0.0;
        double prijsKleinMateriaal = 0.0;
        double prijsSpiegelhuis = 0.0;
        double prijsArbeid = 0.0;
        double prijsTraanplaat = 0.0;
        double branderwerken = 0.0;
        double prijsBand = 0.0;
        double prijsRemmenTest = 0.0;
        double prijsTrekVeer = 0.0;
        double prijsSmeerVet = 0.0;
        double prijsLastwiel = 0.0;
        double prijsTrekstang = 0.0;
        double prijsAs = 0.0;
        double prijsVorkwiel = 0.0;
        double prijsBuiscoupille = 0.0;
        double prijsBacheplank = 0.0;
        double prijsFietsbaar = 0.0;
        double prijsLamp = 0.0;
        double prijsWielmoer = 0.0;
        double prijsDiagnoseApparatuur = 0.0;
        double prijsMotorolie = 0.0;
        double prijsRegeneenheid = 0.0;
        double prijsSpatlap = 0.0;
        double prijsAluVijzen = 0.0;
        double prijsSpanbaar = 0.0;
        double prijsAchterlicht = 0.0;
        double prijsMistlicht = 0.0;
        double prijsPlaatverligting = 0.0;
        double prijsWerklicht = 0.0;
        double prijsKatoogDriehoek = 0.0;
        double prijstVerbindingsdoos = 0.0;
        double prijsZijlicht = 0.0;
        double prijsDraad = 0.0;
        double prijsLensFlatpoint = 0.0;
        double prijsPositielicht = 0.0;
        double prijsMeubeldop = 0.0;
        double prijsOpvouwDoos = 0.0;
        double prijsSchakelaar = 0.0;
        double prijsAfdekkapje = 0.0;
        double prijsReparatiezeil = 0.0;
        double prijsBachelijm = 0.0;
        double prijsWegwerpkwast = 0.0;
        double prijsopgegotenTek = 0.0;
        double prijsWielbouten = 0.0;
        double prijsStroomgroep = 0.0;
        double prijsPrimer = 0.0;
        double prijsZwarteMat = 0.0;
        double prijsRal = 0.0;
        double prijsElectrospiraal = 0.0;
        double prijsRemreiniger = 0.0;
        double prijsSpiraalKabel = 0.0;
        double prijsSlang = 0.0;
        double prijsEbskabel = 0.0;
        double prijsVerplaatsing = 0.0;
        double prijsOring = 0.0;
        double prijsDrukring = 0.0;
        double prijsTwistlock = 0.0;
        double prijsKoppelingServo = 0.0;
        double prijsEquivis = 0.0;
        double prijsBalanceercompound = 0.0;
        double prijsHijsband = 0.0;
        double prijsHelmWit = 0.0;
        double prijsDepannage = 0.0;
        double prijstLuchtketel = 0.0;
        double prijsKnie = 0.0;
        double prijsBreedtelicht = 0.0;
        double prijsTrommel = 0.0;
        double prijsRemblok = 0.0;
        double prijsKeerring = 0.0;
        double prijsRemveer = 0.0;
        double prijsRemsleutel = 0.0;
        double prijsReparatieset = 0.0;
        double prijsM16 = 0.0;
        double prijsVeerremcilinder = 0.0;
        double prijsFirestone = 0.0;
        double prijsNaafDop = 0.0;
        double prijsKinegrip = 0.0;
        double prijsAntispray = 0.0;
        double unclassifiedCosts = 0.0;

        PDDocument document = PDDocument.load(invoiceFile.getInputStream());
        if (!document.isEncrypted()) {
            Invoice invoice = new Invoice();
            boolean numberplateAndDateDetected = false;
            boolean tireRepair = false;
            boolean depannage = false;
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).toLowerCase();
            String[] lines = text.split(System.getProperty("line.separator"));
            Date date = new Date();
            String np = new String();
            Vehicle v3 = new Vehicle();

            for (int i = 0; i < lines.length; i++) {
                String[] words = lines[i].split(" ");
                boolean wordMatches21 = false;
                if (numberplateAndDateDetected) {
                    String sDate = words[0];
                    String clientNR = words[1];
                    if (sDate.contains("-")) {
                        date = new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
                    } else if (sDate.contains("/")) {
                        date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                    }
                    if (words.length >= 4) {
                        np = words[3];
                    } else {
                        np = "";
                    }
                    v3 = vehicleService.findVehicleByNumberPlate(np.toUpperCase());
                    invoice.setVehicle(v3);
                    invoice.setDate(date);
                    invoice.setClientNR(Integer.parseInt(clientNR));
                    numberplateAndDateDetected = false;
                } else if (lines[i].contains("nr. plaat")) {
                    numberplateAndDateDetected = true;
                } else if (lines[i].contains("factuur") && words.length == 2) {
                    int factuurNummer = Integer.parseInt(words[words.length - 1]);
                    invoice.setFactuurNR(factuurNummer);
                } else if (lines[i].contains("depannage") || lines[i].contains("depanage")) {
                    depannage = true;
                } else if (lines[i].contains("sproeier") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSproeier = prijsSproeier + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("radiator") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsRadiator = prijsRadiator + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("klein materiaal") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsKleinMateriaal = prijsKleinMateriaal + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("spiegelhuis") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSpiegelhuis = prijsSpiegelhuis + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("arbeid") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsArbeid = prijsArbeid + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("traanplaat") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsTraanplaat = prijsTraanplaat + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("branderwerken") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    branderwerken = branderwerken + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("band") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    for (String w : words) {
                        if (w.matches("21")) {
                            wordMatches21 = true;
                        }
                    }
                    if (wordMatches21 && lines[i].contains(",")) {
                        prijsBand = prijsBand + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                        tireRepair = true;
                        //System.out.println("TIRE REPAIR: "+lines[i]);
                    } else {
                        //System.out.println("REJECTRED AS TIRE CLASSIFICATION: "+lines[i]);
                    }
                } else if (lines[i].contains("remmentest") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    if (!words[1].matches("van")) {
                        prijsRemmenTest = prijsRemmenTest + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                    }
                } else if (lines[i].contains("trekveer") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsTrekVeer = prijsTrekVeer + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("smeervet") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSmeerVet = prijsSmeerVet + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("lastwiel") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsLastwiel = prijsLastwiel + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("trekstang") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsTrekstang = prijsTrekstang + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("as") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    for (int j = 0; j < words.length; j++) {
                        if (words[j].matches("as")) {
                            prijsAs = prijsAs + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                        }
                    }
                } else if (lines[i].contains("vorkwiel") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsVorkwiel = prijsVorkwiel + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("buiscoupille") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsBuiscoupille = prijsBuiscoupille + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("bacheplank") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsBacheplank = prijsBacheplank + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("fietsbaar") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsFietsbaar = prijsFietsbaar + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("lamp") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsLamp = prijsLamp + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("wielmoer") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsWielmoer = prijsWielmoer + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("diagnoseapparatuur") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsDiagnoseApparatuur = prijsDiagnoseApparatuur + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("motorolie") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsMotorolie = prijsMotorolie + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("regeneenheid") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsRegeneenheid = prijsRegeneenheid + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("spatlap") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSpatlap = prijsSpatlap + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("vijzen") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsAluVijzen = prijsAluVijzen + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("spanbaar") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSpanbaar = prijsSpanbaar + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("achterlicht") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsAchterlicht = prijsAchterlicht + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("mistlicht") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsMistlicht = prijsMistlicht + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("plaatverligting") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsPlaatverligting = prijsPlaatverligting + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("werklicht") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsWerklicht = prijsWerklicht + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("katoog") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsKatoogDriehoek = prijsKatoogDriehoek + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("verbindingsdoos") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijstVerbindingsdoos = prijstVerbindingsdoos + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("zijlicht") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsZijlicht = prijsZijlicht + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("draad") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsDraad = prijsDraad + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("lens") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsLensFlatpoint = prijsLensFlatpoint + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("positielicht") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsPositielicht = prijsPositielicht + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("meubeldop") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsMeubeldop = prijsMeubeldop + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("opvouwdoos") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsOpvouwDoos = prijsOpvouwDoos + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("schakelaar") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSchakelaar = prijsSchakelaar + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("afdekkap") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsAfdekkapje = prijsAfdekkapje + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("reparatiezeil") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsReparatiezeil = prijsReparatiezeil + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("bachelijm") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsBachelijm = prijsBachelijm + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("wegwerpkwast") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsWegwerpkwast = prijsWegwerpkwast + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("opgegoten") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsopgegotenTek = prijsopgegotenTek + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("wielbouten") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsWielbouten = prijsWielbouten + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("stroomgroep") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsStroomgroep = prijsStroomgroep + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("primer") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsPrimer = prijsPrimer + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("zwart mat") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsZwarteMat = prijsZwarteMat + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("ral") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsRal = prijsRal + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("electrospiraal") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsElectrospiraal = prijsElectrospiraal + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("remreiniger") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsRemreiniger = prijsRemreiniger + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("spiraalkabel") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSpiraalKabel = prijsSpiraalKabel + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("slang") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsSlang = prijsSlang + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("ebs") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsEbskabel = prijsEbskabel + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("verplaatsing") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsVerplaatsing = prijsVerplaatsing + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("oring") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsOring = prijsOring + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("drukring") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsDrukring = prijsDrukring + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("twistlock") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsTwistlock = prijsTwistlock + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("koppeling servo") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsKoppelingServo = prijsKoppelingServo + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("equivis") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsEquivis = prijsEquivis + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("balanceercompound") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsBalanceercompound = prijsBalanceercompound + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("hijsband") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsHijsband = prijsHijsband + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("helm") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsHelmWit = prijsHelmWit + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("depannage") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21")) && !(lines[i].contains("21/"))) {
                    prijsDepannage = prijsDepannage + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("luchtketel") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijstLuchtketel = prijstLuchtketel + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("knie") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsKnie = prijsKnie + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("breedtelicht") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsBreedtelicht = prijsBreedtelicht + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("trommel") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsTrommel = prijsTrommel + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("remblok") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsRemblok = prijsRemblok + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("keerring") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsKeerring = prijsKeerring + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("remveer") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsRemveer = prijsRemveer + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("remsleutel") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsRemsleutel = prijsRemsleutel + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("reparatieset") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsReparatieset = prijsReparatieset + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("M16") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsM16 = prijsM16 + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("veerremcilinder") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsVeerremcilinder = prijsVeerremcilinder + Double.parseDouble(words[words.length - 2].replace(",", "."));
                } else if (lines[i].contains("firestone") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsFirestone = prijsFirestone + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("naafdop") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsNaafDop = prijsNaafDop + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("kinegrip") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsKinegrip = prijsKinegrip + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("antispray") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                    prijsAntispray = prijsAntispray + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                } else if (lines[i].contains("21") && (!(lines[i].contains("controleren")))) {
                    for (int k = 0; k < words.length; k++) {
                        if (i > 1) {
                            if (lines[i - 2].contains("belastbaar")) {
                            } else if (words[0].matches("belcrownlaan")) {
                            } else if (words[k].matches("21")) {
                                unclassifiedCosts = unclassifiedCosts + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                            }
                        }
                    }
                }
            }
            invoice.getMaintenanceTypes().put(SPROEIER, prijsSproeier);
            invoice.getMaintenanceTypes().put(RADIATOR, prijsRadiator);
            invoice.getMaintenanceTypes().put(KLEIN_MATERIAAL, prijsKleinMateriaal);
            invoice.getMaintenanceTypes().put(SPIEGELHUIS, prijsSpiegelhuis);
            invoice.getMaintenanceTypes().put(ARBEID, prijsArbeid);
            invoice.getMaintenanceTypes().put(TRAANPLAAT, prijsTraanplaat);
            invoice.getMaintenanceTypes().put(LAS_EN_BRANDERWERKEN, branderwerken);
            invoice.getMaintenanceTypes().put(BAND, prijsBand);
            invoice.getMaintenanceTypes().put(REMMENTEST, prijsRemmenTest);
            invoice.getMaintenanceTypes().put(TREKVEER, prijsTrekVeer);
            invoice.getMaintenanceTypes().put(AS, prijsAs);
            invoice.getMaintenanceTypes().put(VORKWIEL, prijsVorkwiel);
            invoice.getMaintenanceTypes().put(BUISCOUPILLE, prijsBuiscoupille);
            invoice.getMaintenanceTypes().put(BACHEPLANK, prijsBacheplank);
            invoice.getMaintenanceTypes().put(FIETSBAAR, prijsFietsbaar);
            invoice.getMaintenanceTypes().put(LAMP, prijsLamp);
            invoice.getMaintenanceTypes().put(WIELMOER, prijsWielmoer);
            invoice.getMaintenanceTypes().put(DIAGNOSEAPPARATUUR, prijsDiagnoseApparatuur);
            invoice.getMaintenanceTypes().put(MOTOROLIE, prijsMotorolie);
            invoice.getMaintenanceTypes().put(REGENEENHEID_FCIOM, prijsRegeneenheid);
            invoice.getMaintenanceTypes().put(SPATLAP, prijsSpatlap);
            invoice.getMaintenanceTypes().put(ALU_VIJZEN, prijsAluVijzen);
            invoice.getMaintenanceTypes().put(SPANBAAR, prijsSpanbaar);
            invoice.getMaintenanceTypes().put(ACHTERLICHT, prijsAchterlicht);
            invoice.getMaintenanceTypes().put(MISTLICHT, prijsMistlicht);
            invoice.getMaintenanceTypes().put(PLAATVERLIGTING, prijsPlaatverligting);
            invoice.getMaintenanceTypes().put(WERKLICHT, prijsWerklicht);
            invoice.getMaintenanceTypes().put(KATOOG_DRIEHOEK, prijsKatoogDriehoek);
            invoice.getMaintenanceTypes().put(VERBINDINGSDOOS, prijstVerbindingsdoos);
            invoice.getMaintenanceTypes().put(ZJILICHT, prijsZijlicht);
            invoice.getMaintenanceTypes().put(DRAAD, prijsDraad);
            invoice.getMaintenanceTypes().put(LENS_FLATPOINT, prijsLensFlatpoint);
            invoice.getMaintenanceTypes().put(POSITIELICHT, prijsPositielicht);
            invoice.getMaintenanceTypes().put(MEUBELDOP, prijsMeubeldop);
            invoice.getMaintenanceTypes().put(OPVOUWDOOS, prijsOpvouwDoos);
            invoice.getMaintenanceTypes().put(SCHAKELAAR, prijsSchakelaar);
            invoice.getMaintenanceTypes().put(AFDEKKAPJE, prijsAfdekkapje);
            invoice.getMaintenanceTypes().put(REPARATIEZEIL, prijsReparatiezeil);
            invoice.getMaintenanceTypes().put(BACHELIJM, prijsBachelijm);
            invoice.getMaintenanceTypes().put(WEGWERPKWAST, prijsWegwerpkwast);
            invoice.getMaintenanceTypes().put(OPGEGOTEN_M729_TEK, prijsopgegotenTek);
            invoice.getMaintenanceTypes().put(WIELBOUTEN, prijsWielbouten);
            invoice.getMaintenanceTypes().put(STROOMGROEP, prijsStroomgroep);
            invoice.getMaintenanceTypes().put(PRIMER, prijsPrimer);
            invoice.getMaintenanceTypes().put(ZWART_MAT, prijsZwarteMat);
            invoice.getMaintenanceTypes().put(RAL, prijsRal);
            invoice.getMaintenanceTypes().put(ELECTROSPIRAAL, prijsElectrospiraal);
            invoice.getMaintenanceTypes().put(REMREINIGER, prijsRemreiniger);
            invoice.getMaintenanceTypes().put(SPIRAALKABEL, prijsSpiraalKabel);
            invoice.getMaintenanceTypes().put(SLANG, prijsSlang);
            invoice.getMaintenanceTypes().put(EBS_KABEL, prijsEbskabel);
            invoice.getMaintenanceTypes().put(VERPLAATSING, prijsVerplaatsing);
            invoice.getMaintenanceTypes().put(ORING, prijsOring);
            invoice.getMaintenanceTypes().put(DRUKRING, prijsDrukring);
            invoice.getMaintenanceTypes().put(TWISTLOCK, prijsTwistlock);
            invoice.getMaintenanceTypes().put(KOPPELING_SERVO, prijsKoppelingServo);
            invoice.getMaintenanceTypes().put(EQUIVIS, prijsEquivis);
            invoice.getMaintenanceTypes().put(BALANCEERCOMPOUND, prijsBalanceercompound);
            invoice.getMaintenanceTypes().put(HIJSBAND, prijsHijsband);
            invoice.getMaintenanceTypes().put(HELM_WIT, prijsHelmWit);
            invoice.getMaintenanceTypes().put(DEPANNAGE, prijsDepannage);
            invoice.getMaintenanceTypes().put(LUCHTKETEL, prijstLuchtketel);
            invoice.getMaintenanceTypes().put(KNIE, prijsKnie);
            invoice.getMaintenanceTypes().put(BREEDTELICHT, prijsBreedtelicht);
            invoice.getMaintenanceTypes().put(TROMMEL, prijsTrommel);
            invoice.getMaintenanceTypes().put(REMBLOK, prijsRemblok);
            invoice.getMaintenanceTypes().put(KEERRING, prijsKeerring);
            invoice.getMaintenanceTypes().put(REMVEER, prijsRemveer);
            invoice.getMaintenanceTypes().put(REMSLEUTEL, prijsRemsleutel);
            invoice.getMaintenanceTypes().put(REPARATIESET, prijsReparatieset);
            invoice.getMaintenanceTypes().put(M16, prijsM16);
            invoice.getMaintenanceTypes().put(VEERREMCILINDER, prijsVeerremcilinder);
            invoice.getMaintenanceTypes().put(FIRESTONE, prijsFirestone);
            invoice.getMaintenanceTypes().put(NAAFDOP, prijsNaafDop);
            invoice.getMaintenanceTypes().put(KINEGRIP, prijsKinegrip);
            invoice.getMaintenanceTypes().put(ANTISPRAY, prijsAntispray);
            invoice.getMaintenanceTypes().put(UNCLASSIFIED, unclassifiedCosts);
            invoice.setTireRepair(tireRepair);
            invoice.setDepannage(depannage);
            if (vehicleService.existVehicleByNumberPlatte(np.toUpperCase())) {
                v3.addInvoiceToVehicle(date, invoice);
                vehicleService.save(v3);
            }
        }
        document.close();
    }

    public void createTPOTCsvFile() throws IOException {
        List<CumulativeDataBetweenTireRepairsTrucks> cumulativeDataBetweenTireRepairsTrucks = cumulativeDataBetweenTireRepairsTrucksService.findAll();
        dataLines.add(new String[]{
                "kmTravelled",
                "kmCounterStart",
                "kmCounterEnd",
                "motorTime",
                "drivingTime",
                "drivingPercentage",
                "stationaryTime",
                "stationaryPercentage",
                "averageSpeed",
                "soloPercentage",
                "soloKm",
                "emptyPercentage",
                "emptyKm",
                "unknownPercentage",
                "UnknownKm",
                "LoadedKm",
                "LoadedPercentage",
                "target"
        });
        for(CumulativeDataBetweenTireRepairsTrucks data : cumulativeDataBetweenTireRepairsTrucks) {
            dataLines.add(new String[]{
                    String.valueOf(data.getKilometersTravelled()),
                    String.valueOf(data.getStartKilometerCounter()),
                    String.valueOf(data.getEndKilometerCounter()),
                    String.valueOf(data.getMotorTimeHours().toMinutes()),
                    String.valueOf(data.getDrivingTimeHours().toMinutes()),
                    String.valueOf(data.getDrivingTimePercentage()),
                    String.valueOf(data.getStationaryTimeHours().toMinutes()),
                    String.valueOf(data.getStationaryTimePercentage()),
                    String.valueOf(data.getAverageSpeedDriving()),
                    String.valueOf(data.getSoloPercentageDriving()),
                    String.valueOf(data.getSoloKilometersDriving()),
                    String.valueOf(data.getEmptyPercentageDriving()),
                    String.valueOf(data.getEmptyKilometersDriving()),
                    String.valueOf(data.getUnknownPercentage()),
                    String.valueOf(data.getUnknownKilometers()),
                    String.valueOf(data.getLoadedKilometers()),
                    String.valueOf(data.getLoadedPercentage()),
                    String.valueOf(data.getWeeksBetween())
            });
        }
        givenDataArray_whenConvertToCSV_thenOutputCreated();
    }
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public void givenDataArray_whenConvertToCSV_thenOutputCreated() throws IOException {
        File csvOutputFile = new File("TPOTDATAFILENewKilometerCalc.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        assertTrue(csvOutputFile.exists());
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
