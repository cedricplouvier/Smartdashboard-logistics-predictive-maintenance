package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.Invoice;
import com.example.demo.SmartDashboard.Model.MaintenanceType;
import com.example.demo.SmartDashboard.Model.Vehicle;
import com.example.demo.SmartDashboard.Repository.InvoiceRepo;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.LambdaConversionException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.SmartDashboard.Model.MaintenanceType.*;

@Service
public class MaintenanceService {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private InvoiceRepo invoiceRepo;

    public void addMaintenanceExcel (MultipartFile maintenanceFile) throws IOException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook(maintenanceFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        XSSFSheet worksheet2 = workbook.getSheetAt(1);

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

    public Map<MaintenanceType, Double> getMaintenanceCosts(String np, Date dateStart, Date dateEnd){
        Vehicle v = vehicleService.findVehicleByNumberPlate(np);
        List<Invoice> invoices = v.getInvoices();
        Map<MaintenanceType, Double> costOverPeriodByClass =  new HashMap<>();
        for(Invoice invoice: invoices){
            System.out.println(invoice.getDate());
            if((invoice.getDate().after(dateStart) && invoice.getDate().before(dateEnd)) || (DateUtils.isSameDay(invoice.getDate(), dateStart)) || (DateUtils.isSameDay(invoice.getDate(), dateEnd))){
                System.out.println("Invoice is between requested dates");
                invoice.getMaintenanceTypes().forEach((k, val) -> costOverPeriodByClass.merge(k, val, Double::sum));
                System.out.println("cost over period: " + costOverPeriodByClass);
            }
        }
        return costOverPeriodByClass;
    }

    public Map<MaintenanceType, Double> getMaintenanceCostsMultipleVehicles(String np, Date dateStart, Date dateEnd){
        Vehicle v = vehicleService.findVehicleByNumberPlate(np);
        List<Invoice> invoices = v.getInvoices();
        Map<MaintenanceType, Double> costOverPeriodByClass =  new HashMap<>();
        for(Invoice invoice: invoices){
            System.out.println(invoice.getDate());
            if((invoice.getDate().after(dateStart) && invoice.getDate().before(dateEnd)) || (DateUtils.isSameDay(invoice.getDate(), dateStart)) || (DateUtils.isSameDay(invoice.getDate(), dateEnd))){
                invoice.getMaintenanceTypes().forEach((k, val) -> costOverPeriodByClass.merge(k, val, Double::sum));
            }
        }
        return costOverPeriodByClass;
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
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).toLowerCase();
            String[] lines = text.split(System.getProperty("line.separator"));

            for (int i = 0; i < lines.length; i++) {
                System.out.println("line: " + lines[i]);
                if (numberplateAndDateDetected) {
                    String[] words = lines[i].split(" ");
                    System.out.println("numberPlate " + words[3]);
                    System.out.println("date: " + words[0]);
                    String sDate = words[0];
                    Date date=new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
                    String np = words[3];
                    Vehicle v3 = vehicleService.findVehicleByNumberPlate(np.toUpperCase());
                    invoice.setVehicle(v3);
                    invoice.setDate(date);
                    numberplateAndDateDetected = false;
                } else if (lines[i].contains("nr. plaat")) {
                    System.out.println("NUmberplaat detected");
                    numberplateAndDateDetected = true;
                } else if (lines[i].contains("factuur")) {
                    System.out.println("factuur detected");
                    String[] words = lines[i].split(" ");
                    int factuurNummer = Integer.parseInt(words[words.length - 1]);
                    invoice.setFactuurNR(factuurNummer);
                } else if (lines[i].contains("sproeier")) {
                    String[] words = lines[i].split(" ");
                    System.out.println("price: " + words[words.length - 2]);
                    prijsSproeier = prijsSproeier + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("radiator")) {
                    String[] words = lines[i].split(" ");
                    System.out.println("price: " + words[words.length - 2]);
                    prijsRadiator = prijsRadiator + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("klein materiaal")) {
                    String[] words = lines[i].split(" ");
                    System.out.println("klein materiaal price: " + words[words.length - 2]);
                    prijsKleinMateriaal = prijsKleinMateriaal + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("spiegelhuis")) {
                    String[] words = lines[i].split(" ");
                    System.out.println("spiegelhuis price: " + words[words.length - 2]);
                    prijsSpiegelhuis = prijsSpiegelhuis + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("arbeid")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" arbeid price: " + words[words.length - 2]);
                    prijsArbeid = prijsArbeid + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("traanplaat")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" traanplaat price: " + words[words.length - 2]);
                    prijsTraanplaat = prijsTraanplaat + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("branderwerken")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" branderwerken price: " + words[words.length - 2]);
                    branderwerken = branderwerken + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("band")) {
                    String[] words = lines[i].split(" ");
                    System.out.println("band price: " + words[words.length - 2]);
                    prijsBand = prijsBand + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("remmentest")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" remmentest price: " + words[words.length - 2]);
                    prijsRemmenTest = prijsRemmenTest + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("trekveer")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" trekveer price: " + words[words.length - 2]);
                    prijsTrekVeer = prijsTrekVeer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("smeervet")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" smeervet price: " + words[words.length - 2]);
                    prijsSmeerVet = prijsSmeerVet + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("lastwiel")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" lastwiel price: " + words[words.length - 2]);
                    prijsLastwiel = prijsLastwiel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("trekstang")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" trekwiel price: " + words[words.length - 2]);
                    prijsTrekstang = prijsTrekstang + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("as")) {
                    String[] words = lines[i].split(" ");
                    for (int j = 0; j < words.length; j++) {
                        if (words[j].matches("as")) {
                            System.out.println(" as price: " + words[words.length - 2]);
                            prijsAs = prijsAs + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                        }
                    }
                } else if (lines[i].contains("vorkwiel")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" vorkwiel price: " + words[words.length - 2]);
                    prijsVorkwiel = prijsVorkwiel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("buiscoupille")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" buiscoupille price: " + words[words.length - 2]);
                    prijsBuiscoupille = prijsBuiscoupille + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("bacheplank")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" bacheplank price: " + words[words.length - 2]);
                    prijsBacheplank = prijsBacheplank + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("fietsbaar")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" fietsbaar price: " + words[words.length - 2]);
                    prijsFietsbaar = prijsFietsbaar + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("lamp")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" lamp price: " + words[words.length - 2]);
                    prijsLamp = prijsLamp + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("wielmoer")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" wielmoer price: " + words[words.length - 2]);
                    prijsWielmoer = prijsWielmoer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("diagnoseapparatuur")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" diagnoseapparatuur price: " + words[words.length - 2]);
                    prijsDiagnoseApparatuur = prijsDiagnoseApparatuur + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("motorolie")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" motorolie price: " + words[words.length - 2]);
                    prijsMotorolie = prijsMotorolie + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("regeneenheid")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" regeneenheid price: " + words[words.length - 2]);
                    prijsRegeneenheid = prijsRegeneenheid + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("spatlap")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" spatlap price: " + words[words.length - 2]);
                    prijsSpatlap = prijsSpatlap + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("vijzen")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" alu vijzen price: " + words[words.length - 2]);
                    prijsAluVijzen = prijsAluVijzen + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("spanbaar")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" spanbaar price: " + words[words.length - 2]);
                    prijsSpanbaar = prijsSpanbaar + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("achterlicht")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" achterlicht price: " + words[words.length - 2]);
                    prijsAchterlicht = prijsAchterlicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("mistlicht")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" mistlicht price: " + words[words.length - 2]);
                    prijsMistlicht = prijsMistlicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("plaatverligting")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" plaatverligting price: " + words[words.length - 2]);
                    prijsPlaatverligting = prijsPlaatverligting + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("werklicht")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" werklicht price: " + words[words.length - 2]);
                    prijsWerklicht = prijsWerklicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("katoog")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" katoog price: " + words[words.length - 2]);
                    prijsKatoogDriehoek = prijsKatoogDriehoek + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("verbindingsdoos")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" verbindingsdoos price: " + words[words.length - 2]);
                    prijstVerbindingsdoos = prijstVerbindingsdoos + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("zijlicht")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" zijlicht price: " + words[words.length - 2]);
                    prijsZijlicht = prijsZijlicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("draad")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" draad price: " + words[words.length - 2]);
                    prijsDraad = prijsDraad + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("lens")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" lens price: " + words[words.length - 2]);
                    prijsLensFlatpoint = prijsLensFlatpoint + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("positielicht")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" positielicht price: " + words[words.length - 2]);
                    prijsPositielicht = prijsPositielicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("meubeldop")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" meubel price: " + words[words.length - 2]);
                    prijsMeubeldop = prijsMeubeldop + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("opvouwdoos")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" opvouwdoos price: " + words[words.length - 2]);
                    prijsOpvouwDoos = prijsOpvouwDoos + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("schakelaar")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" schakelaar price: " + words[words.length - 2]);
                    prijsSchakelaar = prijsSchakelaar + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("afdekkap")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" afdekkap price: " + words[words.length - 2]);
                    prijsAfdekkapje = prijsAfdekkapje + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("reparatiezeil")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" reparatiezeil price: " + words[words.length - 2]);
                    prijsReparatiezeil = prijsReparatiezeil + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("bachelijm")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" bachelijm price: " + words[words.length - 2]);
                    prijsBachelijm = prijsBachelijm + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("wegwerpkwast")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" wegwerpkwast price: " + words[words.length - 2]);
                    prijsWegwerpkwast = prijsWegwerpkwast + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("opgegoten")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" opgegoten price: " + words[words.length - 2]);
                    prijsopgegotenTek = prijsopgegotenTek + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("wielbouten")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" wielbouten price: " + words[words.length - 2]);
                    prijsWielbouten = prijsWielbouten + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("stroomgroep")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" strommgroep price: " + words[words.length - 2]);
                    prijsStroomgroep = prijsStroomgroep + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("primer")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" primer price: " + words[words.length - 2]);
                    prijsPrimer = prijsPrimer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("zwart mat")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" zwart mat price: " + words[words.length - 2]);
                    prijsZwarteMat = prijsZwarteMat + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("ral")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" ral price: " + words[words.length - 2]);
                    prijsRal = prijsRal + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("electrospiraal")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" electrospiraal price: " + words[words.length - 2]);
                    prijsElectrospiraal = prijsElectrospiraal + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("remreiniger")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" remneiniger price: " + words[words.length - 2]);
                    prijsRemreiniger = prijsRemreiniger + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("spiraalkabel")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" spiraalkabel price: " + words[words.length - 2]);
                    prijsSpiraalKabel = prijsSpiraalKabel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("slang")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" slang price: " + words[words.length - 2]);
                    prijsSlang = prijsSlang + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("ebs")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" ebs price: " + words[words.length - 2]);
                    prijsEbskabel = prijsEbskabel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("verplaatsing")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" verplaatsing price: " + words[words.length - 2]);
                    prijsVerplaatsing = prijsVerplaatsing + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("oring")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" oring price: " + words[words.length - 2]);
                    prijsOring = prijsOring + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("drukring")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" drukring price: " + words[words.length - 2]);
                    prijsDrukring = prijsDrukring + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("twistlock")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" twistlock price: " + words[words.length - 2]);
                    prijsTwistlock = prijsTwistlock + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("koppeling servo")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" koppeling servo price: " + words[words.length - 2]);
                    prijsKoppelingServo = prijsKoppelingServo + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("equivis")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" equivis price: " + words[words.length - 2]);
                    prijsEquivis = prijsEquivis + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("balanceercompound")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" balanceercompound price: " + words[words.length - 2]);
                    prijsBalanceercompound = prijsBalanceercompound + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("hijsband")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" hijsband price: " + words[words.length - 2]);
                    prijsHijsband = prijsHijsband + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("helm")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" helmp price: " + words[words.length - 2]);
                    prijsHelmWit = prijsHelmWit + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("depannage")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" depannage price: " + words[words.length - 2]);
                    prijsDepannage = prijsDepannage + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("luchtketel")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" luchtketel price: " + words[words.length - 2]);
                    prijstLuchtketel = prijstLuchtketel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("knie")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" knie price: " + words[words.length - 2]);
                    prijsKnie = prijsKnie + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("breedtelicht")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" breedtelicht price: " + words[words.length - 2]);
                    prijsBreedtelicht = prijsBreedtelicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("trommel")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" trommel price: " + words[words.length - 2]);
                    prijsTrommel = prijsTrommel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("remblok")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" remblok price: " + words[words.length - 2]);
                    prijsRemblok = prijsRemblok + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("keerring")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" keerring price: " + words[words.length - 2]);
                    prijsKeerring = prijsKeerring + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("remveer")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" remveer price: " + words[words.length - 2]);
                    prijsRemveer = prijsRemveer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("remsleutel")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" remsleutel price: " + words[words.length - 2]);
                    prijsRemsleutel = prijsRemsleutel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("reparatieset")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" reparatieset price: " + words[words.length - 2]);
                    prijsReparatieset = prijsReparatieset + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("M16")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" M16 price: " + words[words.length - 2]);
                    prijsM16 = prijsM16 + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("veerremcilinder")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" veerremcilinder price: " + words[words.length - 2]);
                    prijsVeerremcilinder = prijsVeerremcilinder + Double.parseDouble(words[words.length - 2].replace(",", "."));
                } else if (lines[i].contains("firestone")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" firestone price: " + words[words.length - 2]);
                    prijsFirestone = prijsFirestone + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("naafdop")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" naafdop price: " + words[words.length - 2]);
                    prijsNaafDop = prijsNaafDop + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("kinegrip")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" kinegrip price: " + words[words.length - 2]);
                    prijsKinegrip = prijsKinegrip + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("antispray")) {
                    String[] words = lines[i].split(" ");
                    System.out.println(" antispray price: " + words[words.length - 2]);
                    prijsAntispray = prijsAntispray + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                } else if (lines[i].contains("21")) {
                    String[] words = lines[i].split(" ");
                    System.out.println("21 deteceted");
                    for (int k = 0; k < words.length; k++) {
                        if(i > 1) {
                            if (lines[i - 2].contains("belastbaar")) {
                                System.out.println("belastbaar niet meerekenen");
                            }
                            else if (words[k].matches("21")) {
                                System.out.println(" Unclassified cost: ");
                                unclassifiedCosts = unclassifiedCosts + Double.parseDouble(words[words.length - 2].replace(".","").replace(",","."));
                                System.out.println(unclassifiedCosts);
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
            invoiceRepo.save(invoice);
            }
            document.close();
    }
}
