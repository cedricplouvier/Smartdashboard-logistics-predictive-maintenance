package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.*;
import com.example.demo.SmartDashboard.Repository.InvoiceRepo;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
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
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.demo.SmartDashboard.Model.MaintenanceType.*;
import static com.example.demo.SmartDashboard.Model.TruckBrand.*;

@Service
public class DatabaseLoader {

    @Autowired
    private final VehicleService vehicleService;

    @Autowired
    private final InvoiceRepo invoiceRepo;

    @Autowired
    private final MaintenanceService maintenanceService;

    @Autowired
    private final ActivitiesTrailersService activitiesTrailersService;

    @Autowired
    private final ActivitiesTrucksService activitiesTrucksService;

    @Autowired
    private final TruckDailyConsumptionDataService truckDailyConsumptionDataService;

    @Autowired
    private final CumulativeDataBetweenTireRepairsTrucksService cumulativeDataBetweenTireRepairsTrucksService;

    @Autowired
    private final TruckDailyLoadedDataService truckDailyLoadedDataService;

    public DatabaseLoader(VehicleService vehicleService, ActivitiesTrailersService activitiesTrailersService,
                          ActivitiesTrucksService activitiesTrucksService, TruckDailyConsumptionDataService truckDailyConsumptionDataService,
                          TruckDailyLoadedDataService truckDailyLoadedDataService, MaintenanceService maintenanceService,
                          CumulativeDataBetweenTireRepairsTrucksService cumulativeDataBetweenTireRepairsTrucksService,
                          InvoiceRepo invoiceRepo) {
        this.vehicleService = vehicleService;
        this.activitiesTrailersService = activitiesTrailersService;
        this.activitiesTrucksService = activitiesTrucksService;
        this.truckDailyConsumptionDataService = truckDailyConsumptionDataService;
        this.truckDailyLoadedDataService = truckDailyLoadedDataService;
        this.maintenanceService = maintenanceService;
        this.cumulativeDataBetweenTireRepairsTrucksService = cumulativeDataBetweenTireRepairsTrucksService;
        this.invoiceRepo = invoiceRepo;
    }

    @PostConstruct
    public void initDatabase() throws IOException, ParseException {

        // Initialisation of the Trucks present in Transics

        System.out.println("Started initialising trucks in database");
        vehicleService.save(new Truck("1-AJY-385", "WDB9302051L537558" , MERCEDES, 2011));
        vehicleService.save(new Truck("1-AJY-424", "WDB9302051L537650", MERCEDES, 2011));
        vehicleService.save(new Truck("1-AJY-460", "WDB9302051L537008", MERCEDES, 2011));
        //vehicleService.save(new Truck("1-AMD-094"));
        //vehicleService.save(new Truck("1-BCD-407"));
        vehicleService.save(new Truck("1-BCD-449", "WDB9342231L562915", MERCEDES, 2011));
        vehicleService.save(new Truck("1-BCD-476", "WDB9340331L564733", MERCEDES, 2011));
        //vehicleService.save(new Truck("1-BJY-016"));
        vehicleService.save(new Truck("1-BLH-016", "WDB9342231L578301", MERCEDES, 2011));
        //vehicleService.save(new Truck("1-CAU-656"));
        vehicleService.save(new Truck("1-CMJ-555", "WDB9342411L626496", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CMJ-558", "WDB9342411L626497", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CMJ-560", "WDB9340331L628725", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CMJ-562", "WDB9340331L628727", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CMJ-564", "WDB9340421L628734", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CMJ-565", "WDB9340421L629064", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CMJ-567", "WDB9505371L633390", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CML-409", "WDB9340331L628009", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CML-431", "WDB9340331L628726", MERCEDES, 2012));
        vehicleService.save(new Truck("1-CMR-215", "WDB9340331L628008", MERCEDES, 2012));
        //vehicleService.save(new Truck("1-CSQ-755"));
        //vehicleService.save(new Truck("1-CSX-948"));
        vehicleService.save(new Truck("1-CVZ-357", "WMAN16ZZ89Y236162", MAN, 2009));
        vehicleService.save(new Truck("1-CYL-104", "VF620J863GB001920", RENAULT, 2016));
        vehicleService.save(new Truck("1-CYL-645", "YV2J1D1C0CA724776", VOLVO, 2012));
        vehicleService.save(new Truck("1-CYS-110", "YV2J1D1C0EB671784", VOLVO,2014));
        vehicleService.save(new Truck("1-CYS-557", "YV2J1D1C9B611614", VOLVO, 2012));
        vehicleService.save(new Truck("1-CYS-614", "YV2J1D1CXCB636036", VOLVO, 2012));
        vehicleService.save(new Truck("1-CYY-845", "VF611M166HD002148", RENAULT, 2017));
        vehicleService.save(new Truck("1-CYY-846","VF611M167HD002143", RENAULT, 2017));
        vehicleService.save(new Truck("1-CYY-875", "VF611M168HD002149", RENAULT, 2017));
        vehicleService.save(new Truck("1-CYZ-493", "VF611M16XHD002153", RENAULT, 2017));
        vehicleService.save(new Truck("1-DPO-957")); // enkel in afstanden, niet aanschaf en verz.
        vehicleService.save(new Truck("1-DYF-715","VF620J865HB003251", RENAULT, 2017));
        vehicleService.save(new Truck("1-EJV-057", "WDB9323151L716728", MERCEDES, 2013));
        vehicleService.save(new Truck("1-EJV-172", "WDB9340331L716385", MERCEDES, 2013));
        vehicleService.save(new Truck("1-EJV-206", "WDB9340331L716386", MERCEDES, 2013));
        vehicleService.save(new Truck("1-EJV-238", "WDB9340331L716387", MERCEDES, 2013));
        vehicleService.save(new Truck("1-EJV-371", "WDB9340331L716388", MERCEDES, 2013));
        vehicleService.save(new Truck("1-ENJ-231", "WDB9634031L677563", MERCEDES, 2013));
        //vehicleService.save(new Truck("1-ESV-320"));
        vehicleService.save(new Truck("1-FTG-299")); // enkel in afstanden, niet aanschaf en verz.
        vehicleService.save(new Truck("1-FYB-597","VF620J863LB007842", RENAULT, 2020));
        vehicleService.save(new Truck("1-FYB-598", "VF620J865LB007843", RENAULT, 2020));
        vehicleService.save(new Truck("1-FYB-697", "VF620J866LB007883", RENAULT, 2020));
        vehicleService.save(new Truck("1-FYB-699", "VF620J864LB007882", RENAULT, 2020));
        vehicleService.save(new Truck("1-FYB-848", "VF611M169LD003710", RENAULT, 2020));
        vehicleService.save(new Truck("1-FYB-849", "VF611M162LD003709", RENAULT, 2020));
        vehicleService.save(new Truck("1-FYN-382"));
        vehicleService.save(new Truck("1-FYN-383"));
        vehicleService.save(new Truck("1-GAD-267"));
        vehicleService.save(new Truck("1-GNZ-388"));
        vehicleService.save(new Truck("1-GTK-965", "VF624CPA000005157", RENAULT, 2014));
        vehicleService.save(new Truck("1-HAS-174", "VF624CPA000004120", RENAULT, 2011));
        vehicleService.save(new Truck("1-HDY-986", "VF611A16XED002342", RENAULT, 2014));
        vehicleService.save(new Truck("1-HDZ-006", "VF611A161ED002343", RENAULT, 2014));
        //vehicleService.save(new Truck("1-HYC-252"));
        vehicleService.save(new Truck("1-JJK-251", "VF611A169ED001361", RENAULT, 2015));
        vehicleService.save(new Truck("1-JJK-278", "VF611A168ED001447", RENAULT, 2015));
        vehicleService.save(new Truck("1-JJT-140")); // enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-JTP-518", "VF611A169FD006965", RENAULT, 2015));
        vehicleService.save(new Truck("1-JUR-059", "VF611A162FD006967", RENAULT, 2015));
        vehicleService.save(new Truck("1-JUR-087", "VF611A164FD006985", RENAULT, 2015));
        vehicleService.save(new Truck("1-JVD-472", "VF611A160FD006966", RENAULT, 2015));
        vehicleService.save(new Truck("1-KHJ-253", "VF611A168FD008545", RENAULT, 2015));
        //vehicleService.save(new Truck("1-KJE-804"));
        vehicleService.save(new Truck("1-LFH-745", "WDB9440331L606020", MERCEDES, 2011));
        //vehicleService.save(new Truck("1-LKF-702"));
        vehicleService.save(new Truck("1-NAD-469", "VF611A16XGD013974", RENAULT, 2016));
        vehicleService.save(new Truck("1-NAD-510", "VF611A160GD013997", RENAULT, 2016));
        vehicleService.save(new Truck("1-NAL-282", "XLER4X20005284967", SCANIA, 2012)); //In verzekeringskosten maar niet transicx
        vehicleService.save(new Truck("1-NAN-190", "VF611A162GD014326", RENAULT, 2016));
        vehicleService.save(new Truck("1-NAN-199", "VF611A160GD014325", RENAULT, 2016));
        vehicleService.save(new Truck("1-NBB-536", "VF630E166GD000089", RENAULT, 2016));
        vehicleService.save(new Truck("1-NBK-160", "VF611A164GD014411", RENAULT, 2016));
        vehicleService.save(new Truck("1-NBK-190", "VF611A166GD014359", RENAULT, 2016));
        vehicleService.save(new Truck("1-NCU-229")); // enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-NGN-109", "WDB9302051L626452", MERCEDES, 2012));
        //vehicleService.save(new Truck("1-NNX-077"));
        vehicleService.save(new Truck("1-PBB-127", "VF630M160FD000730", RENAULT, 2015));
        vehicleService.save(new Truck("1-PBV-686", "YV2AG10A0BB590258", VOLVO, 2011));
        vehicleService.save(new Truck("1-PEY-930", "YV2RG20A6DB662480", VOLVO, 2013));
        vehicleService.save(new Truck("1-PFH-424", "YV2AG10A5DB646455", VOLVO, 2013));
        vehicleService.save(new Truck("1-PJY-082", "YV2RG30AXEB675191", VOLVO, 2014));
        vehicleService.save(new Truck("1-PKF-787", "YV2J1D1C0EB671820", VOLVO, 2014));
        vehicleService.save(new Truck("1-PKU-465", "YS2R4X20005343425", SCANIA, 2014));
        vehicleService.save(new Truck("1-PLC-194", "YS2R4X20005325645", SCANIA, 2013));
        vehicleService.save(new Truck("1-PLN-850", "VF611M163GD001313", RENAULT, 2016));
        vehicleService.save(new Truck("1-PNX-263", "YV2RG30A6DB665384", VOLVO, 2013));
        vehicleService.save(new Truck("1-PRG-200", "YV2AG10A2CB613282", VOLVO, 2012));
        vehicleService.save(new Truck("1-PSA-348", "YV2AG20A7CB634750", VOLVO, 2012));
        vehicleService.save(new Truck("1-PYD-675", "YV2RG30A1EB676861", VOLVO, 2014));
        //vehicleService.save(new Truck("1-PZR-797"));
        vehicleService.save(new Truck("1-RAA-510", "XLER4X20005296042", SCANIA,2012));
        vehicleService.save(new Truck("1-RGB-122", "YV2JFY0G48A663176", VOLVO, 2008));
        vehicleService.save(new Truck("1-RHV-185", "WMA06SZZ1DW181367", MAN, 2013));
        vehicleService.save(new Truck("1-RHV-217", "YV2J1E1A7DB646318", VOLVO, 2013));
        vehicleService.save(new Truck("1-RJZ-840", "YV2ASG0A18B520908", VOLVO, 2008));
        vehicleService.save(new Truck("1-RKP-040", "VF611A16XHD020974", RENAULT, 2017));
        vehicleService.save(new Truck("1-RKP-047", "VF611A163HD020976", RENAULT, 2017));
        vehicleService.save(new Truck("1-RKP-053", "VF611A160HD020949", RENAULT, 2017));
        vehicleService.save(new Truck("1-RKT-117", "YV2JG00AXBB602149", VOLVO, 2011));
        vehicleService.save(new Truck("1-RKV-011", "VF611A164HD021053", RENAULT, 2017));
        vehicleService.save(new Truck("1-RLJ-454", "VF611A169HD020948", RENAULT, 2017));
        vehicleService.save(new Truck("1-RLJ-496", "VF611A160HD021065", RENAULT, 2017));
        vehicleService.save(new Truck("1-RLJ-506", "VF611A161HD020975", RENAULT, 2017));
        //vehicleService.save(new Truck("1-RLX-885"));
        vehicleService.save(new Truck("1-RNH-779", "VF611A162HD021150", RENAULT, 2017));
        vehicleService.save(new Truck("1-RNN-213", "WMA06SZZ9EP051753", MAN, 2014));
        vehicleService.save(new Truck("1-RPL-123", "YV2XZ22A3GB766914", VOLVO, 2016));
        //vehicleService.save(new Truck("1-RSN-160"));
        //vehicleService.save(new Truck("1-RSS-676"));
        vehicleService.save(new Truck("1-RTH-212", "WDB9340421L583660", MERCEDES, 2011));
        //vehicleService.save(new Truck("1-RTY-224"));
        vehicleService.save(new Truck("1-RUN-232", "YV2RTY0AFB731039", VOLVO, 2015));
        vehicleService.save(new Truck("1-RVK-593", "YV2J1E1AXDB652257", VOLVO, 2013));
        vehicleService.save(new Truck("1-RVT-314", "YV2XZ22A3GB767027", VOLVO, 2016));
        vehicleService.save(new Truck("1-RXK-029", "YV2ASG0A68B520905", VOLVO, 2008));
        vehicleService.save(new Truck("1-SFA-763", "VF630V163HD000150", RENAULT, 2017));
        vehicleService.save(new Truck("1-SGD-087", "VF611M16XHD002122", RENAULT, 2017));
        vehicleService.save(new Truck("1-STY-980", "VF611M169HD002192", RENAULT, 2017));
        vehicleService.save(new Truck("1-SUR-020", "VF624CPA000004266", RENAULT, 2012));
        vehicleService.save(new Truck("1-TAE-712", "VF610A369HD007858", RENAULT, 2017));
        vehicleService.save(new Truck("1-TAY-626", "VF640J567HB007640", RENAULT, 2017));
        vehicleService.save(new Truck("1-TES-509", "VF640J563HB007635", RENAULT, 2017));
        vehicleService.save(new Truck("1-TPJ-930", "VF611M165JD002535", RENAULT, 2018));
        vehicleService.save(new Truck("1-TPJ-951", "VF611A165JD024310", RENAULT, 2018));
        vehicleService.save(new Truck("1-TVV-595", "VF611A161JD026622", RENAULT, 2018));
        vehicleService.save(new Truck("1-TVV-630", "VF611A160JD026613", RENAULT, 2018));
        vehicleService.save(new Truck("1-UAE-428", "VF611M160JD002636", RENAULT, 2018));
        vehicleService.save(new Truck("1-UCK-371", "VF611A165JD026767", RENAULT, 2018));
        vehicleService.save(new Truck("1-UCN-603", "VF611A168JD026858", RENAULT, 2018));
        vehicleService.save(new Truck("1-UER-402", "VF611M167JD002763", RENAULT, 2018));
        vehicleService.save(new Truck("1-UER-418", "VF640J562JB009611", RENAULT, 2018));
        vehicleService.save(new Truck("1-UER-435", "VF640J564JB009612", RENAULT, 2018));
        vehicleService.save(new Truck("1-UFN-942")); //// enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-URK-559", "VF611M160JD002801", RENAULT, 2018));
        vehicleService.save(new Truck("1-URP-720", "VF611M162JD002749", RENAULT, 2018));
        vehicleService.save(new Truck("1-VAU-129", "VF611A169GD013996", RENAULT, 2016));
        vehicleService.save(new Truck("1-VBR-109", "VF611A160JD024358", RENAULT, 2017));
        vehicleService.save(new Truck("1-VBV-568", "VF611A168FD009260", RENAULT, 2016));
        //vehicleService.save(new Truck("1-VCQ-653 (Smits Transport Logistics"));
        vehicleService.save(new Truck("1-VDK-362 (Smits Transport Logistics")); // enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-VQW-435", "VF611A169KD030676", RENAULT, 2019));
        vehicleService.save(new Truck("1-VQW-442", "VF611A164KD030696", RENAULT, 2019));
        vehicleService.save(new Truck("1-VRH-703", "VF611A167KD030658", RENAULT, 2019));
        vehicleService.save(new Truck("1-VRH-710", "VF611A165KD030643", RENAULT, 2019));
        vehicleService.save(new Truck("1-VRH-724", "VF611A165KD030657", RENAULT, 2019));
        vehicleService.save(new Truck("1-VRK-426 (Smits Transport Logistics")); //// enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-VWU-464")); // enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-WEN-832", "VF611A162JD026614", RENAULT, 2018));
        vehicleService.save(new Truck("1-WFA-741", "WDB9630201L798109", MERCEDES, 2013));
        vehicleService.save(new Truck("1-WFZ-344", "XLRAS85MC0E856586", DAF, 2009));
        vehicleService.save(new Truck("1-WHV-090 (Smits Transport Logistics")); // enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-WUB-814", "VF611A161LD032682", RENAULT, 2019));
        vehicleService.save(new Truck("1-WUB-828", "VF611A16XLD032695", RENAULT, 2019));
        vehicleService.save(new Truck("1-WUR-125", "VF630E168KD000135", RENAULT, 2019));
        vehicleService.save(new Truck("1-WXQ-593", "VF630V164KD000312", RENAULT, 2019));
        vehicleService.save(new Truck("1-XBG-081", "VF611M160LD003563", RENAULT, 2019));
        vehicleService.save(new Truck("1-XCG-114", "VF611A167FD009234", RENAULT, 2015));
        vehicleService.save(new Truck("1-XCG-118", "VF611A160GD011425", RENAULT, 2015));
        vehicleService.save(new Truck("1-XCH-299", "VF611A160FD009236", RENAULT, 2015));
        vehicleService.save(new Truck("1-XDN-355", "VF611A162GD011426", RENAULT, 2015));
        vehicleService.save(new Truck("1-XEA-406", "VF611M162LD003564", RENAULT, 2019));
        vehicleService.save(new Truck("1-XER-314", "VF611A16XGD017409", RENAULT, 2016));
        vehicleService.save(new Truck("1-XER-318", "VF611A166GD015396", RENAULT, 2016));
        vehicleService.save(new Truck("1-XER-329", "VF611A16XGD015398", RENAULT, 2016));
        vehicleService.save(new Truck("1-XER-337", "VF611A168GD017408", RENAULT, 2016));
        vehicleService.save(new Truck("1-XER-353", "VF611A161GD015399", RENAULT, 2016));
        vehicleService.save(new Truck("1-XER-361", "VF611A168GD015397", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFT-994", "VF611A169GD015019", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-005", "VF611A16XGD014946", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-017", "VF611A162GD014942", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-023", "VF611A166GD014944", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-029", "VF611A168GD014945", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-039", "VF611A161GD014978", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-049", "VF611A163GD014979", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-066", "VF611A164GD014943", RENAULT, 2016));
        vehicleService.save(new Truck("1-XFU-077", "VF611A165GD016071", RENAULT, 2016));
        vehicleService.save(new Truck("1-XGP-732", "VF611A165HD020039", RENAULT, 2016));
        vehicleService.save(new Truck("1-XJD-449", "VF611A164GD011427", RENAULT, 2015));
        vehicleService.save(new Truck("1-XJD-459", "VF611A165FD009331", RENAULT, 2015));
        vehicleService.save(new Truck("1-XKL-832")); //// enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("1-XKV-189", "VF630M16XHD001418", RENAULT, 2017));
        vehicleService.save(new Truck("1-XLZ-934", "VF611A162KD029420", RENAULT, 2018));
        vehicleService.save(new Truck("1-XNC-070", "VF611M162FD000443", RENAULT, 2015));
        vehicleService.save(new Truck("1-XSA-364", "WMA06SZZ3DW181399", MAN, 2013));
        vehicleService.save(new Truck("1-XSH-675", "VF611M368LD001525", RENAULT, 2020));
        vehicleService.save(new Truck("1-XVD-733", "VF630E369LD000417", RENAULT, 2020));
        vehicleService.save(new Truck("1-YCR-446", "WMA89SZZ5BW146080", MAN, 2011));
        vehicleService.save(new Truck("1-YGY-340", "WMA30SZZ9DW180725", MAN, 2013));
        vehicleService.save(new Truck("1-YHR-569", "VF611M364LD001490", RENAULT, 2020));
        vehicleService.save(new Truck("1-YJP-482", "VF624CPA000004935", RENAULT, 2013));
        vehicleService.save(new Truck("1-YPE-940", "VF640J568LB015142", RENAULT, 2020));
        vehicleService.save(new Truck("2-AAW-450")); // enkel in afstanden niet aanschaf of verz
        vehicleService.save(new Truck("2-AGG-933"));
        vehicleService.save(new Truck("2-AGG-951"));
        vehicleService.save(new Truck("2-AJE-438"));
        vehicleService.save(new Truck("2-AJE-461"));
        vehicleService.save(new Truck("2-ANP-687"));
        vehicleService.save(new Truck("185-BTW"));
        vehicleService.save(new Truck("272-BQY"));
        vehicleService.save(new Truck("275-BQY"));
        vehicleService.save(new Truck("405-BND"));
        vehicleService.save(new Truck("433-BND"));
        vehicleService.save(new Truck("439-BND", "WDB9702781L474830", MERCEDES, 2010));
        vehicleService.save(new Truck("442-CBK"));
        vehicleService.save(new Truck("591-BMP"));
        vehicleService.save(new Truck("802-BNG"));
        vehicleService.save(new Truck("833-CBA"));
        vehicleService.save(new Truck("835-CBA"));
        vehicleService.save(new Truck("838-CBA"));
        vehicleService.save(new Truck("935-BJL"));
        vehicleService.save(new Truck("954-BUE"));
        //vehicleService.save(new Truck("MOB1")); //only found in transics
        //vehicleService.save(new Truck("MOB2")); //only found in transics
        //vehicleService.save(new Truck("MOB3")); //only found in transics
        //vehicleService.save(new Truck("MOB4")); //only found in transics
        //vehicleService.save(new Truck("MOB5")); //only found in transics
        //vehicleService.save(new Truck("MOB6")); //only found in transics
        //vehicleService.save(new Truck("MOB7")); //only found in transics
        //vehicleService.save(new Truck("MOB8")); //only found in transics
        //vehicleService.save(new Truck("MOB11")); //only found in transics
        //vehicleService.save(new Truck("MOB12")); //only found in transics
        //vehicleService.save(new Truck("MOB13")); //only found in transics
        //vehicleService.save(new Truck("PDA96823")); //only found in transics
        //vehicleService.save(new Truck("PDA96849")); //only found in transics
        vehicleService.save(new Truck("SEE-729", "WDB9702551K573041", MERCEDES, 2001));
        //vehicleService.save(new Truck("TRX_V")); //only found in transics
        //vehicleService.save(new Truck("XFV-539")); //only found in transics
        //vehicleService.save(new Truck("XIC-790")); //only found in transics
        //vehicleService.save(new Truck("XJY-459")); //only found in transics
        //vehicleService.save(new Truck("XJY-618")); //only found in transics
        vehicleService.save(new Truck("YJQ-568", "WDB9323171L307943", MERCEDES, 2008));
        //vehicleService.save(new Truck("YRM-983")); //only found in transics
        //vehicleService.save(new Truck("YRT-645")); //only found in transics
        //vehicleService.save(new Truck("YUC-744")); //only found in transics
        //vehicleService.save(new Truck("YUV-814")); //only found in transics
        //vehicleService.save(new Truck("YYA-870")); //only found in transics
        //vehicleService.save(new Truck("YYX-718")); //only found in transics
        //vehicleService.save(new Truck("YYX-719")); //only found in transics
        //vehicleService.save(new Truck("YZD-211")); //only found in transics
        //vehicleService.save(new Truck("YZP-598")); //only found in transics
        System.out.println("Finished loading in trucks");

        // Initialisation of Trailers from Sensolus application (all Trailers with a relevent thrid party ID are inserted)

        System.out.println("Started loading trailers into database");
        vehicleService.save(new Trailer("ZU4J7J", "ZU4J7J", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("W76HCN", "W76HCN", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("VWADQ3", "VWADQ3", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("UVE-408", "22KCAR", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("UTW-870", "EPUJJ3", TrailerType.GESLOTEN_KAST));
        vehicleService.save(new Trailer("UTU-037", "KGRCMX", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("UTT12X", "UTT12X", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("UTK-217", "WRCU1R", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("USU-824", "YLTNAA", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("UPK-208", "WLDTYZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("UNZ-992", "3DTNN9", TrailerType.FRIGO));
        vehicleService.save(new Trailer("UN4F7E", "UN4F7E", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("UFC-846", "NNJM2E", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("UAL-786", "V9QR62", TrailerType.FRIGO));
        vehicleService.save(new Trailer("QLB-419", "99PHVM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QLB-418", "NJMUXF", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QKX-797", "4NXKNZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QKU-622", "6HRDKQ", TrailerType.HUIF));
        vehicleService.save(new Trailer("QKU-620", "V171UY", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QKU-618", "P1AMLW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QKF-123", "LHQDGN", TrailerType.KRAANWAGEN));
        vehicleService.save(new Trailer("QKE-516", "TL7E3F", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QKD-932", "H12EMM", TrailerType.ARBOR));
        vehicleService.save(new Trailer("QKD-567", "RUWMNP", TrailerType.ARBOR));
        vehicleService.save(new Trailer("QKD-090", "T1WK3M", TrailerType.KRAANWAGEN));
        vehicleService.save(new Trailer("QKA-302", "2FKNK6", TrailerType.ARBOR));
        vehicleService.save(new Trailer("QJZ-306", "2TQRLC", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QJY-671", "FQFWMX", TrailerType.ARBOR));
        vehicleService.save(new Trailer("QJY-046", "Q7AU6R", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QJX-850", "L6QLNF", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QJX-782", "7NVDF6", TrailerType.KRAANWAGEN));
        vehicleService.save(new Trailer("QJX-716", "T4AAAF", TrailerType.ARBOR));
        vehicleService.save(new Trailer("QJV-624", "V73PRV", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QJS-567", "PKVRUW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QJQ-706", "6WVWKD", TrailerType.ARBOR));
        vehicleService.save(new Trailer("QJM-800", "ECTQ97", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QJM-797", "TAFUNF", TrailerType.VAN_LEEUWEN));
        vehicleService.save(new Trailer("QJM-424", "V7PTAT", TrailerType.HUIF));
        vehicleService.save(new Trailer("QJM-423", "CHCH2G", TrailerType.HUIF));
        vehicleService.save(new Trailer("QJM-422", "4LQDYT", TrailerType.HUIF));
        vehicleService.save(new Trailer("QJM-421", "3LJN2N", TrailerType.HUIF));
        vehicleService.save(new Trailer("QJL-003", "CZMEEE", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QJJ-768", "Q6NHFJ", TrailerType.VAN_LEEUWEN));
        vehicleService.save(new Trailer("QJJ-161", "T9LR7D", TrailerType.KRAANWAGEN));
        vehicleService.save(new Trailer("QJH-220", "RX3C94", TrailerType.ARBOR));
        vehicleService.save(new Trailer("QII-764", "ZWJLU3", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QII-520", "RZHMZ2", TrailerType.KRAANWAGEN));
        vehicleService.save(new Trailer("QIC-270", "VKKLVR", TrailerType.HUIF));
        vehicleService.save(new Trailer("QHZ-762", "2E1CTM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QHY-194", "EWWDVP", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QHW-078", "HXL2KL", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QHW_002", "QRHEDP", TrailerType.PLATEAU)); //3rd party ID (numberplaat) staat zo in Sensolus met _
        vehicleService.save(new Trailer("QHC-126", "FPVXLZ", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("QGW-241", "KUF6FK", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QGN-289", "GRMMPL", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QGM-768", "9DE9VF", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QGJ-881", "DUGDJD", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("QGJ-660", "K2FWJV", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QGJ-543", "AG21D2", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("QGH-719", "QJ4CJC", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QGH-617", "KN3EH6", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QGH-304", "PF99Y6", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("QGH-050", "7THEVR", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QGF-384", "X97GJM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QFJ-074", "C7JKNH", TrailerType.FRIGO));
        vehicleService.save(new Trailer("QFE-537", "UJ4G36", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QFE-340", "9CECHK", TrailerType.HUIF));
        vehicleService.save(new Trailer("QDV-373", "XR743J", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QDF-769", "EVUVN9", TrailerType.HUIF));
        vehicleService.save(new Trailer("QDF-768", "QVVVXK", TrailerType.HUIF));
        vehicleService.save(new Trailer("QDF-767", "QD1ZJW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QDF-765", "3LMAQW", TrailerType.HUIF));
        vehicleService.save(new Trailer("QBI-990", "YVYGMC", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QBI-598", "GL3N2Q", TrailerType.HUIF));
        vehicleService.save(new Trailer("QBI-588", "C2NC7Y", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QBI-586", "NXZT3T", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("QBG-056", "ZEYHEX", TrailerType.FRIGO));
        vehicleService.save(new Trailer("QBG-055", "VDQADT", TrailerType.FRIGO));
        vehicleService.save(new Trailer("QBG-052", "7YN6VE", TrailerType.FRIGO));
        vehicleService.save(new Trailer("QBE-307", "3J9MT7", TrailerType.FRIGO));
        vehicleService.save(new Trailer("Q-AYF-393", "M1LPHF", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AYD-951", "WMMZNH", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AYC-514", "R7NQ2Y", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AYB-647", "VCRFHX", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AYB-373", "CKAL3Q", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AMN-969", "WMR2JP", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AMN-230", "JGTZTU", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AMN-228", "2GGZ46", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AMN-226", "4WQZ1A", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AMN-220", "3CXXF7", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AMF-723", "G49R7W", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALZ-009", "1E7FN2", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ALZ-008", "QUZEHY", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ALZ-007", "VLQ77L", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ALZ-005", "NZ7LLC", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ALY-448", "DQ3JAG", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALY-447", "4W1NMW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALY-444", "Y7Z1RW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALY-442", "7LD1ZX", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALY-441", "YNT3MU", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALY-438", "CRARXK", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALY-434", "U9V3CM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALY-433", "QHDZM9", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALL-653", "LMFNXU", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-604", "RWTAXD", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-008", "1CDGFL", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-007", "EVVZ2C", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q)ALG-006", "PKJTAT", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-005", "W37NNZR", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-004", "CXKEYM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-003", "M7ZGPM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-002", "NHXKHP", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALG-001", "MKTG3X", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALF-998", "WDAU7H", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALF-997", "GHLPH7", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ALE-838", "PKPXQY", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AKU-725", "PQ3TMD", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AKF-444", "E2TC7E", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AKF-441", "3HN7DW", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AKE-455", "GN9NWE", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AJZ-232", "7EQ4MD", TrailerType.GESLOTEN_KAST));
        vehicleService.save(new Trailer("Q-AJY-524", "EQD7M2", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AJX-474", "LKDZHT", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AJV-195", "VEZZ3R", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-194", "AUCVFW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-193", "Y9Q3GH", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-192", "6T7EEK", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-191", "XXTG2A", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-187", "V3JHXZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-186", "26E7E4", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-185", "U7XPQ1", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-184", "ER3UMJ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-182", "13C2AA", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJV-177", "N2U6ZF", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AJU-087", "9PX4AM", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AJD-364", "ENCG1H", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AJD-266", "KAZG1P", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AJD-265", "GVTPYR", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AJD-263", "CNVKCK", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AJD-262", "6AZDU6", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHW-487", "WYJGFF", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AHP-840", "KTA9U4", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHP-839", "TQMTC3", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHP-836", "W2HWD1", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHP-834", "ECQLQ6", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHP-385", "NETYMM", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHK-103", "DV3A6H", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AHK-097", "QMKQ79", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AHK-092", "1Y7EYQ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AHK-060", "34CZM1", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AHC-406", "Y7JZ7F", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AHB-396", "W3UY2T", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHB-394", "V9ARH6", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHB-393", "XAAYQ6", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHB-390", "GTQPJP", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHB-388", "R9J6YH", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHB-387", "KAM11G", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AHB-032", "VWW1MQ", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AGW-052", "G12KZP", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AGV-169", "6V6EZD", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AGS-013", "1LE7LG", TrailerType.FRIGO));
        vehicleService.save(new Trailer("Q-AGS-012", "YG41D3", TrailerType.FRIGO));
        vehicleService.save(new Trailer("Q-AGR-941", "W69GXM", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AGQ-097", "FDCJEJ", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AGP-863", "R4AT36", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AGK-426", "CLQHJ3", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGG-366", "PTG3J7", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGG-038", "6GLEYW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGG-037", "4JHX12", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGG-009", "M9XGWW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGG-006", "CXX9XY", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGG-004", "XEHLQM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGG-002", "TRZ1HL", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AGC-888", "XHGWP2", TrailerType.TESTAS));
        vehicleService.save(new Trailer("Q-AGC-886", "A7JPU7", TrailerType.TESTAS));
        vehicleService.save(new Trailer("Q-AGA-978", "RW3P7N", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFX-441", "N9TWP7", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AFT-571", "1ZHFFE", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFS-767", "UZWMJL", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFS-719", "Q14XTP", TrailerType.TESTAS));
        vehicleService.save(new Trailer("Q-AFS-615", "9HGG3V", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFS-612", "2CVCRE", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFS-611", "629FJY", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFR-987", "LPMZ2Q", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFR-396", "AUCFHX", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFR-321", "F34X1U", TrailerType.TESTAS));
        vehicleService.save(new Trailer("Q-AFL-628", "XHL3QV", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFL-035", "UE7WVR", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFK-397", "KP3JYC", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFK-068", "3D6XM7", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFJ-300", "F7L17H", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFJ-297", "ZYV13Y", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFJ-157", "UCJR69", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFH-738", "V4UNME", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFH-736", "9XQQAY", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFH-034", "6ZL1ED", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFG-310", "U7JX22", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFG-309", "FZJPVD", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFG-305", "GTH7M2", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFG-304", "VDWNY7", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFG-173", "CQ7DTE", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFF-909", "G6Q4EL", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFF-686", "DFKZ7H", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFE-333", "Q6NH4T", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFD-341", "9CWT99", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AFB-994", "PJG449", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("Q-AFB-992", "EEDCVT", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("Q-AFB-625", "TGTVV1", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AFA-514", "LKMGQG", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AEZ-168", "L4CVPR", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AEZ-057", "EJ4AV1", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AEV-272", "TF9QQT", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AET-957", "MVDPCW", TrailerType.KIPPER));
        vehicleService.save(new Trailer("Q-AEN-094", "LQ717D", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AEK-150", "YQF3LE", TrailerType.TESTAS));
        vehicleService.save(new Trailer("Q-AEJ-823", "V771MM", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("Q-AEJ-392", "4T6HT3", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AEH-353", "YXXF9A", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AEH-347", "NDEEDZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AEG-249", "M4RVQD", TrailerType.TESTAS));
        vehicleService.save(new Trailer("Q-AEE-297", "PKZ2E6", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AEE-099", "9MZ29D", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AED-806", "V29JMC", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("Q-AED-534", "N4WF46", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AED-532", "JD9QQZ", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AED-530", "H2LGM2", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AED-527", "VT2T6Z", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AED-526", "EKFMD7", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-AED-101", "2NQV2Y", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AED-005", "CQ3KM1", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AEC-852", "QKH732", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AEC-850", "DWZNLR", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AEC-373", "1HMPHA", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AEB-850", "GFFACT", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-AEB-296", "UTGARX", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADU-521", "266TUG", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADT-700", "1PLZUJ", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADT-516", "QW2HZA", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADT-399", "9HFKZM", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADT-169", "LP4K1G", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADR-428", "WUWUQY", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADR-427", "94GA6M", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADR-139", "PWXAMZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADR-133", "3HRFJE", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADR-081", "7UVLPF", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADP-875", "4L7P6P", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADP-874", "Q47UCU", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADP-873", "GDXZX6", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADP-549", "TYPP2Y", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADP-547", "V1KPH2", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADN-819", "W6Y3JD", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADN-537", "NWACKM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADN-299", "UR7JEU", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADL-048", "FT34HF", TrailerType.KIPPER));
        vehicleService.save(new Trailer("Q-ADJ-725", "HWRULT", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADJ-723", "JHMQFZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADJ-722", "UDKVFT", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADJ-475", "MMHGC3", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADJ-473", "LZ29WD", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ADJ-186", "3A4ZHJ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADJ-185", "3DJE3N", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADH-605", "DTKEFF", TrailerType.GESLOTEN_KAST));
        vehicleService.save(new Trailer("Q-ADG-841", "WYCEQ4", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADG-839", "Z7AZAK", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADG-639", "VQKZZ9", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ADG-344", "AA2CN9", TrailerType.GESLOTEN_KAST));
        vehicleService.save(new Trailer("Q-ADG-047", "4Q1REF", TrailerType.KIPPER));
        vehicleService.save(new Trailer("Q-ADG-046", "EPW3P9", TrailerType.KIPPER));
        vehicleService.save(new Trailer("Q-ADE-669", "XZMTNV", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADE-602", "42GBY1", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ADE-126", "VZRRET", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-ADB-034", "XKGULQ", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACX-521", "PALQFA", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACX-480", "R7XPA2", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACX-376", "YTV14D", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACX-332", "PGJNP3", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-ACV-106", "NFTJ3W", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACU-773", "KUAG4D", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACU-765", "NNQMKT", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACU-764", "M3PZME", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACU-760", "KHAUNV", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACU-759", "FMHV2G", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACU-756", "G4VDNT", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACU-752", "YGWZKH", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACT-546", "ZAR6X4", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACK-017", "4XNL7P", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-ACJ-013", "QWN1AU", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACG-952", "DAEA3Z", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACF-603", "3ND9N1", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACF-600", "KEMN4J", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACE-973", "LHYUWT", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-ACE-972", "UMTFA4", TrailerType.FRIGO));
        vehicleService.save(new Trailer("Q-ACE-923", "GKPXYU", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ACC-909", "6V2T4Q", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ACC-908", "74UUXK", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ACC-907", "2DFJJA", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ACB-907", "KC9Z2H", TrailerType.HUIF));
        vehicleService.save(new Trailer("Q-ACB-525", "E3F6AP", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACB-524", "GDPPLJ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ACB-523", "QQAGJM", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABZ-825", "GQC7P1", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABZ-187", "F7DQ4F", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABZ-185", "KH1D2F", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABZ-184", "4VU1LL", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABY-641", "JUTZRM", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ABY-627", "EYUKGV", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ABX-501", "MQXPP7", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-ABU-131", "ZHALHA", TrailerType.KRAANWAGEN));
        vehicleService.save(new Trailer("Q-ABR-989", "EPVDWE", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABR-288", "W96J7Y", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ABL-281", "ULH7VD", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABL-11", "M2CTZW", TrailerType.ARBOR)); //nummerplaat staat zo (fout?) in sensolus
        vehicleService.save(new Trailer("Q-ABG-803", "YCL4RX", TrailerType.HUIF_MEGA));
        vehicleService.save(new Trailer("Q-ABG-509", "F19DF2", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-ABD-958", "F2YGFL", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-ABC-526", "XKML49", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABA-124", "LPXP99", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-ABA-123", "QH4J3V", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-377", "GVL9KW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-375", "KUFC92", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-372", "TJRX64", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-361", "UYLQF4", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-359", "FWZWHV", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-358", "3U2U1G", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-355", "EX77EK", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAY-353", "CUQE39", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("Q-AAU-840", "C12M9G", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AAU-075", "2UTW99", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AAU-073", "L7HZVZ", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AAT-686", "U7MVDZ", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AAT-685", "3FHYCK", TrailerType.WALKING_FLOOR));
        vehicleService.save(new Trailer("Q-AAR-619", "WXYJ2X", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("Q-AAR-388", "T4ALRU", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("Q-AAJ-484", "VE3MDD", TrailerType.FRIGO));
        vehicleService.save(new Trailer("Q-AAB-014", "6NXLA9", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("PZXJCJ", "PZXJCJ", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("PANECR", "PANECR", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("P91C47", "P91C47", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("P2QH11", "P2QH11", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("MX4PLM", "MX4PLM", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("KKX3EP", "KKX3EP", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("KJQZW6", "KJQZW6", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("JM42FT", "JM42FT", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("HQP977", "HQP977", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("GJURPH", "GJURPH", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("FZRJVA", "FZRJVA", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("CJWTA", "CJCWTA", TrailerType.CONTAINER));     //staat zo (fout?) in
        vehicleService.save(new Trailer("AZHMHU", "AZHMHU", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("ADZUDE", "ADZUDE", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("77J2QV", "77J2QV", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("6LRFTK", "6LRFTK", TrailerType.HUIF));
        vehicleService.save(new Trailer("4K7U44", "4K7U44", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("3JA7XE", "3JA7XE", TrailerType.CONTAINER));
        vehicleService.save(new Trailer("2W94DR", "2W94DR", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QYB-833", "WHMV3A", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("1-QYB-572", "44TM2Z", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("1-QFH-066", "MPG2N7", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("1-QFF-884", "AZA6MF", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QFF-727", "71P9CH", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QFF-669", "XD43Y", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QFB-450", "RQ9AL2", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("1-QEZ-638", "MPV293", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QEV-844", "RN373E", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-843", "GGT7VZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-842", "R9GGXQ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-840", "ZYX1PP", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-838", "9G4U22", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-834", "177J6H", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-832", "P3LRQD", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-829", "1K4M4V", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEV-824", "KJCY61", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QEQ-957", "7YR77E", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("1-QEQ-512", "9ERL4V", TrailerType.KIPPER));
        vehicleService.save(new Trailer("1-QEL-154", "3Q19Y6", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("1-QEK-399", "DAGW1L", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("1-QED-072", "73D4UA", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QDS-151", "9X3N2L", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("1-QDQ-001", "GEM4XJ", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("1-QDM-465", "29UY3M", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QDM-202", "TMZPUV", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QDM-200", "EXGQFM", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QDG-093", "DPEC7M", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("1-QDD-791", "MWCYW7", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QDD-790", "9ZADUY", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QDD-789", "LTUEWW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QDD-788", "URTAAD", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QDD-085", "LNGYEX", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QDC-582", "2MJ3CF", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QDB-211", "1GDJMY", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QCZ-412", "9MDP2N", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QCU-841", "ZFZHNW", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QCT-771", "FUUUQZ", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("1-QCT-769", "P9JLUP", TrailerType.KIPPER));
        vehicleService.save(new Trailer("1-QCS-972", "UDLP6C", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("1-QCR-620", "2MPNUT", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QCR-617", "EUZHME", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QCR-615", "NDAKG4", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QCR-612", "3XKZPU", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QCQ-952", "3XKZPU", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QCP-003", "QDUPAU", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QCO-935", "YRDJW6", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QCO-925", "RWYGL9", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("1-QCO-314", "9H977C", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QCO-313", "VE22J2", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QCJ-993", "3NKUWJ", TrailerType.DIEPLADER));
        vehicleService.save(new Trailer("1-QCI-176", "Y43EKY", TrailerType.KIPPER));
        vehicleService.save(new Trailer("1-QBZ-578", "H3UMLE", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QBY-747", "VH7Q7F", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBX-983", "ZH72N9", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBW-445", "KUN2ZZ", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBW-444", "EAG36W", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBV-096", "LWK7YV", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBU-064", "MWLRQG", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBQ-639", "CCDCPE", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QBI-133", "VEJE34", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBH-040", "NXPZD2", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QBE-313", "ELFUM4", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-307", "R7NP37", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-302", "33Y4LD", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-300", "T63Z23", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-298", "23LXWY", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-297", "D23AJL", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-294", "UW12HX", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-293", "E9W139", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-292", "NCQU96", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBE-291", "9VYNW3", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBD-085", "9VF4CA", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBD-084", "RLEG1V", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBD-083", "JKQA2K", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QBA-016", "UENE79", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QAZ-880", "NN4L7A", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QAX-451", "9P14JD", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("1-QAV-589", "ERG92U", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QAV-588", "XQ31YG", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QAS-684", "UQV7CR", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("1-QAS-683", "2662FF", TrailerType.CNT_CHASSIS));
        vehicleService.save(new Trailer("1-QAR-891", "1VZXU7", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QAQ-391", "GYCCHY", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QAQ-390", "UFRFZC", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QAQ-389", "VEZHK7", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QAQ-388", "YKWWT9", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QAQ-387", "ZJJJMX", TrailerType.FRIGO));
        vehicleService.save(new Trailer("1-QAP-234", "D6CJ3K", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QAM-142", "TDA2JY", TrailerType.PLATEAU));
        vehicleService.save(new Trailer("1-QAK-616", "XMYHDE", TrailerType.AANHANGWAGEN));
        vehicleService.save(new Trailer("1-QAI-707", "NF3ETW", TrailerType.HUIF));
        vehicleService.save(new Trailer("1-QAF-305", "UDNKYJ", TrailerType.KRAANWAGEN));
        vehicleService.save(new Trailer("1-AQP-235", "FECLAP", TrailerType.PLATEAU));
        System.out.println("Finished loading in trailers in database");

        // Initialise database with activity of Trailers from Sensolus generated Excels

        /*System.out.println("Started fillTrailerActivityDatabase");
        File[] files = new File("src/main/resources/DataTM/TrailerLocationExcels").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file : files) {
            System.out.println("src/main/resources/DataTM/TrailerLocationExcels/" + file.getName());
            String filePath = "src/main/resources/DataTM/TrailerLocationExcels/" + file.getName();
            MultipartFile activitiesDataFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePath)));
            XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(1);

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);

                if(i % 1000 == 0) {
                    System.out.println("progress file " + file.getName() + " row: " + i + " / " + worksheet.getPhysicalNumberOfRows());
                }
                String numberPlateCell = row.getCell(2).getStringCellValue();
                if(vehicleService.existVehicleByNumberPlatte(numberPlateCell)) {
                    ActivityTrailers tempActivity = new ActivityTrailers();
                    Vehicle v = vehicleService.findVehicleByNumberPlate(numberPlateCell);
                    ((Trailer) v).setSensolusSerial(row.getCell(0).getStringCellValue());
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
        System.out.println("Finished filling trailer activity database");*/

        // Fill database with historic truck activity from transics history
        /*
        System.out.println("Started filling truck activity database");
        File[] files = new File("src/main/resources/DataTM/TrekkerActivityExcels").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file : files){
            System.out.println("src/main/resources/DataTM/TrekkerActivityExcels/" + file.getName());
            String filePath = "src/main/resources/DataTM/TrekkerActivityExcels/" + file.getName();
            MultipartFile activitiesDataFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePath)));
            XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++){
                XSSFRow row = worksheet.getRow(i);

                String numberPlateCell = row.getCell(6).getStringCellValue();

                if(i % 10000 == 0) {
                    System.out.println("progress file " + file.getName() + " row: " + i + " / " + worksheet.getPhysicalNumberOfRows());
                }

                if(row.getCell(11).getStringCellValue().matches("Rijden")) {
                    if(vehicleService.existVehicleByNumberPlatte(numberPlateCell)){
                        ActivityTrucks activityTruck = new ActivityTrucks();
                        Vehicle v = vehicleService.findVehicleByNumberPlate(numberPlateCell);
                        activityTruck.setTruck((Truck) v);
                        activityTruck.setNumberPlate(numberPlateCell);

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
        System.out.println("Finished filling truck activity in database");
        */

        // Fill database with maintenance reports from excels which give a monthly overview
        /*
        System.out.println("Started filling database with maintenance reports");
        File[] filesMaintenanceReports = new File("src/main/resources/DataTM/MaintenanceReports").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file : filesMaintenanceReports) {
            System.out.println("src/main/resources/DataTM/MaintenanceReports/" + file.getName());
            String filePath = "src/main/resources/DataTM/MaintenanceReports/" + file.getName();
            MultipartFile activitiesDataFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePath)));
            XSSFWorkbook workbook = new XSSFWorkbook(activitiesDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            XSSFSheet worksheet2 = workbook.getSheetAt(1);

            String dateString = worksheet2.getRow(3).getCell(5).getStringCellValue();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateI = formatter.parse(dateString);
            Calendar c = Calendar.getInstance();
            c.setTime(dateI);
            c.set(Calendar.DAY_OF_MONTH, 01);
            Date date = c.getTime();
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
        System.out.println("Finished filling database with maintenance reports");
        */

        // Fill database with historic truck consumption data from transics

        System.out.println("FillDatabaseDailyConsumptionTrucks started");
        File[] filesConsumptionTrucks = new File("src/main/resources/DataTM/VerbruikTruckExcel2020").listFiles((dir, name) -> !name.equals(".DS_Store"));
        //File[] filesConsumptionTrucks = new File("src/main/resources/DataTM/VerbruikTruckExcel").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file: filesConsumptionTrucks) {
            //System.out.println("src/main/resources/DataTM/VerbruikTruckExcel2020/" + file.getName());
            String filePathConsumptionTrucks = "src/main/resources/DataTM/VerbruikTruckExcel2020/" + file.getName();
            System.out.println("src/main/resources/DataTM/VerbruikTruckExcel/" + file.getName());
            //String filePathConsumptionTrucks = "src/main/resources/DataTM/VerbruikTruckExcel/" + file.getName();
            long startTime = System.nanoTime();
            MultipartFile activitiesDataFileConsumptionTrucks = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePathConsumptionTrucks)));
            XSSFWorkbook workbookConsumptionTrucks = new XSSFWorkbook(activitiesDataFileConsumptionTrucks.getInputStream());
            XSSFSheet worksheetConsumptionTrucks = workbookConsumptionTrucks.getSheetAt(0);
            activitiesDataFileConsumptionTrucks.getInputStream().close();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
            System.out.println("duration of getting workbook sheet: "+duration+ " ms");

            long startTimeProcessSheet = System.nanoTime();
            for(int i = 1; i < worksheetConsumptionTrucks.getPhysicalNumberOfRows(); i++){
                XSSFRow row = worksheetConsumptionTrucks.getRow(i);
                String numberPlate = row.getCell(0).getStringCellValue();
                if(vehicleService.existVehicleByNumberPlatte(numberPlate)) {
                    TruckDailyConsumptionData truckDailyConsumptionData = new TruckDailyConsumptionData();
                    Vehicle v = vehicleService.findVehicleByNumberPlate(numberPlate);
                    truckDailyConsumptionData.setTruck((Truck) v);
                    truckDailyConsumptionData.setNumberplate(numberPlate);

                    truckDailyConsumptionData.setDate(row.getCell(1).getDateCellValue());
                    truckDailyConsumptionData.setKilometersTravelled((int) row.getCell(3).getNumericCellValue());
                    truckDailyConsumptionData.setStartKilometerCounter((int) row.getCell(4).getNumericCellValue());
                    truckDailyConsumptionData.setEndKilometerCounter((int) row.getCell(5).getNumericCellValue());

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                    Date timeMotor = row.getCell(6).getDateCellValue();
                    String timeMotorString = format.format(timeMotor);
                    String[] valuesMotor = timeMotorString.split(":");
                    // get the hours, minutes and seconds value and add it to the duration
                    Duration durationMotor = Duration.ofHours(Integer.parseInt(valuesMotor[0]));
                    durationMotor = durationMotor.plusMinutes(Integer.parseInt(valuesMotor[1]));
                    durationMotor = durationMotor.plusSeconds(Integer.parseInt(valuesMotor[2]));
                    truckDailyConsumptionData.setMotorTimeHours(durationMotor);

                    Date timeDriving = row.getCell(7).getDateCellValue();
                    String timeDrivingString = format.format(timeDriving);
                    String[] valuesDriving = timeDrivingString.split(":");
                    // get the hours, minutes and seconds value and add it to the duration
                    Duration durationDriving = Duration.ofHours(Integer.parseInt(valuesDriving[0]));
                    durationDriving = durationDriving.plusMinutes(Integer.parseInt(valuesDriving[1]));
                    durationDriving = durationDriving.plusSeconds(Integer.parseInt(valuesDriving[2]));
                    truckDailyConsumptionData.setDrivingTimeHours(durationDriving);

                    if(row.getCell(8).getCellType() == CellType.NUMERIC) {
                        truckDailyConsumptionData.setDrivingTimePercentage(row.getCell(8).getNumericCellValue());
                    }
                    else if(row.getCell(8).getCellType() == CellType.STRING && row.getCell(8).getStringCellValue().matches("")){
                        truckDailyConsumptionData.setDrivingTimePercentage(0.0);
                    }
                    else{
                        System.out.println("ERROR in reading driving time % Databaseloader truck consumption: " + row.getCell(8).getStringCellValue());
                    }

                    Date timeStationary = row.getCell(9).getDateCellValue();
                    String timeStringStationary = format.format(timeStationary);
                    String[] valuesStationary = timeStringStationary.split(":");
                    // get the hours, minutes and seconds value and add it to the duration
                    Duration durationStationary = Duration.ofHours(Integer.parseInt(valuesStationary[0]));
                    durationStationary = durationStationary.plusMinutes(Integer.parseInt(valuesStationary[1]));
                    durationStationary = durationStationary.plusSeconds(Integer.parseInt(valuesStationary[2]));
                    truckDailyConsumptionData.setStationaryTimeHours(durationStationary);

                    if(row.getCell(10).getCellType() == CellType.NUMERIC) {
                        truckDailyConsumptionData.setStationaryTimePercentage(row.getCell(10).getNumericCellValue());
                    }
                    else if(row.getCell(10).getCellType() == CellType.STRING && row.getCell(10).getStringCellValue().matches("")){
                        truckDailyConsumptionData.setStationaryTimePercentage(0.0);
                    }
                    else{
                        System.out.println("ERROR in reading stationary time % Databaseloader truck consumption: " + row.getCell(10).getStringCellValue());
                    }

                    if(row.getCell(11).getCellType() == CellType.NUMERIC) {
                        truckDailyConsumptionData.setAverageSpeedDriving(row.getCell(11).getNumericCellValue());
                    }
                    else if(row.getCell(11).getCellType() == CellType.STRING && row.getCell(11).getStringCellValue().matches("")){
                        truckDailyConsumptionData.setAverageSpeedDriving(0.0);
                    }
                    else{
                        System.out.println("ERROR in reading average time Databaseloader truck consumption: " + row.getCell(11).getStringCellValue());
                    }
                    ((Truck) v).addConsumptionDailyDataToMap(row.getCell(1).getDateCellValue(), truckDailyConsumptionData);
                    vehicleService.save(v);

                    //TreeMap<Date, TruckDailyConsumptionData> truckDailyConsumptionDataMap = new TreeMap<>(((Truck) v).getTruckDailyConsumptionDataMap());
                    //if(v.getNumberPlate().contains("1-FYB-849")) {
                    //    Date previousRepair = new SimpleDateFormat("dd-MM-yyyy").parse("30-03-2020");
                    //    Date currentRepair = new SimpleDateFormat("dd-MM-yyyy").parse("31-03-2020");
                    //    SortedMap<Date, TruckDailyConsumptionData> periodTruckDailyConsumptionDataMap = truckDailyConsumptionDataMap.subMap(previousRepair, currentRepair);
                    //    for(Map.Entry<Date, TruckDailyConsumptionData> entry : periodTruckDailyConsumptionDataMap.entrySet()) {
                    //        System.out.println(entry.getKey().toString());
                    //    }
                    //    System.out.println("consumption size map: " + periodTruckDailyConsumptionDataMap.size());
                    //}
                }
            }
            long endTimeProcessSheet = System.nanoTime();
            long durationProcessSheet = (endTimeProcessSheet - startTimeProcessSheet)/1000000;
            System.out.println("duration of processing sheet: " + durationProcessSheet+ " ms");
            workbookConsumptionTrucks.close();
        }
        System.out.println("Finished filling database with truck consumption data");



        // Fill database with historical truck data of load from transics

        System.out.println("FillDatabaseDailyLoadedTrucks started");
        File[] filesLoadTrucks = new File("src/main/resources/DataTM/GeladenTruckExcels2020").listFiles((dir, name) -> !name.equals(".DS_Store"));
        //File[] filesLoadTrucks = new File("src/main/resources/DataTM/GeladenTruckExcels").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file: filesLoadTrucks) {
            System.out.println("src/main/resources/DataTM/GeladenTruckExcels2020/" + file.getName());
            String filePathLoadTrucks = "src/main/resources/DataTM/GeladenTruckExcels2020/" + file.getName();
            //System.out.println("src/main/resources/DataTM/GeladenTruckExcels/" + file.getName());
            //String filePathLoadTrucks = "src/main/resources/DataTM/GeladenTruckExcels/" + file.getName();
            MultipartFile activitiesDataFileLoadTrucks = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePathLoadTrucks)));
            XSSFWorkbook workbookLoadTrucks = new XSSFWorkbook(activitiesDataFileLoadTrucks.getInputStream());
            XSSFSheet worksheetLoadTrucks = workbookLoadTrucks.getSheetAt(0);



            for(int i = 1; i < worksheetLoadTrucks.getPhysicalNumberOfRows(); i++){
                XSSFRow row = worksheetLoadTrucks.getRow(i);
                String numberPlate = row.getCell(0).getStringCellValue();
                if(vehicleService.existVehicleByNumberPlatte(numberPlate)) {
                    TruckDailyLoadedData truckDailyLoadedData = new TruckDailyLoadedData();
                    Vehicle v = vehicleService.findVehicleByNumberPlate(numberPlate);
                    truckDailyLoadedData.setTruck((Truck) v);
                    truckDailyLoadedData.setNumberplate(numberPlate);

                    truckDailyLoadedData.setDate(row.getCell(1).getDateCellValue());
                    truckDailyLoadedData.setUnknownKilometers((int) row.getCell(3).getNumericCellValue());
                    truckDailyLoadedData.setSoloKilometersDriving((int) row.getCell(4).getNumericCellValue());

                    if(row.getCell(5).getCellType() == CellType.NUMERIC) {
                        truckDailyLoadedData.setSoloPercentageDriving(row.getCell(5).getNumericCellValue());
                    }
                    else if(row.getCell(5).getCellType() == CellType.STRING && row.getCell(5).getStringCellValue().matches("")){
                        truckDailyLoadedData.setSoloPercentageDriving(0.0);
                    }
                    else{
                        System.out.println("ERROR in reading solo driving % Databaseloader truck loaded: " + row.getCell(5).getStringCellValue());
                    }

                    truckDailyLoadedData.setEmptyKilometersDriving((int) row.getCell(6).getNumericCellValue());

                    if(row.getCell(7).getCellType() == CellType.NUMERIC) {
                        truckDailyLoadedData.setUnknownPercentage(row.getCell(7).getNumericCellValue());
                    }
                    else if(row.getCell(7).getCellType() == CellType.STRING && row.getCell(7).getStringCellValue().matches("")){
                        truckDailyLoadedData.setUnknownPercentage(0.0);
                    }
                    else{
                        System.out.println("ERROR in reading unknown % Databaseloader truck loaded: " + row.getCell(7).getStringCellValue());
                    }

                    if(row.getCell(8).getCellType() == CellType.NUMERIC) {
                        truckDailyLoadedData.setEmptyPercentageDriving(row.getCell(8).getNumericCellValue());
                    }
                    else if(row.getCell(8).getCellType() == CellType.STRING && row.getCell(8).getStringCellValue().matches("")){
                        truckDailyLoadedData.setEmptyPercentageDriving(0.0);
                    }
                    else{
                        System.out.println("ERROR in reading empty % Databaseloader truck loaded: " + row.getCell(8).getStringCellValue());
                    }

                    truckDailyLoadedData.setLoadedKilometers((int) row.getCell(9).getNumericCellValue());

                    if(row.getCell(10).getCellType() == CellType.NUMERIC) {
                        truckDailyLoadedData.setLoadedPercentage(row.getCell(10).getNumericCellValue());
                    }
                    else if(row.getCell(10).getCellType() == CellType.STRING && row.getCell(10).getStringCellValue().matches("")){
                        truckDailyLoadedData.setLoadedPercentage(0.0);
                    }
                    else{
                        System.out.println("ERROR in reading loaded % Databaseloader truck loaded: " + row.getCell(10).getStringCellValue());
                    }

                    truckDailyLoadedData.setTotalKilometers((int) row.getCell(11).getNumericCellValue());

                    ((Truck) v).addLoadedDailyDataToMap(row.getCell(1).getDateCellValue(), truckDailyLoadedData);
                    vehicleService.save(v);

                }
            }
            workbookLoadTrucks.close();
        }
        System.out.println("Finished loading truck load data into database");



        // Process Maintenance Invoices

        System.out.println("started parsing maintenace invoices");
        //File[] filesMaintenanceInvoices = new File("src/main/resources/DataTM/DigitaleOnderhoudsFacturen20162020").listFiles((dir, name) -> !name.equals(".DS_Store"));
        File[] filesMaintenanceInvoices = new File("src/main/resources/DataTM/DigitaleOnderhoudsFacturen2020").listFiles((dir, name) -> !name.equals(".DS_Store"));
        //File[] filesMaintenanceInvoices = new File("src/main/resources/DataTM/DigitaleOnderhoudsFacturenMei2021").listFiles((dir, name) -> !name.equals(".DS_Store"));

        for(File file: filesMaintenanceInvoices) {
            //System.out.println("src/main/resources/DataTM/DigitaleOnderhoudsFacturen20162020/" + file.getName());
            //String filePath = "src/main/resources/DataTM/DigitaleOnderhoudsFacturen20162020/" + file.getName();
            //System.out.println("src/main/resources/DataTM/DigitaleOnderhoudsFacturen2020/" + file.getName());
            String filePath = "src/main/resources/DataTM/DigitaleOnderhoudsFacturen2020/" + file.getName();
            //System.out.println("src/main/resources/DataTM/DigitaleOnderhoudsFacturenMei2021/" + file.getName());
            //String filePath = "src/main/resources/DataTM/DigitaleOnderhoudsFacturenMei2021/"+file.getName();
            MultipartFile invoiceFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File(filePath)));
            PDDocument document = PDDocument.load(invoiceFile.getInputStream());

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
                        if(sDate.contains("-")) {
                            date = new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
                        }
                        else if(sDate.contains("/")){
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                        }
                        if(words.length >= 4) {
                            np = words[3];
                        }
                        else{
                            np = "";
                        }
                        v3 = vehicleService.findVehicleByNumberPlate(np.toUpperCase());
                        invoice.setVehicle(v3);
                        invoice.setDate(date);
                        invoice.setClientNR(Integer.parseInt(clientNR));
                        numberplateAndDateDetected = false;
                    } else if (lines[i].contains("nr. plaat")) {
                        numberplateAndDateDetected = true;
                    } else if (lines[i].contains("factuur") && words.length==2) {
                        int factuurNummer = Integer.parseInt(words[words.length - 1]);
                        invoice.setFactuurNR(factuurNummer);
                    } else if (lines[i].contains("depannage") || lines[i].contains("depanage")) {
                        depannage = true;
                    } else if (lines[i].contains("sproeier") && (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                        prijsSproeier = prijsSproeier + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("radiator")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsRadiator = prijsRadiator + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("klein materiaal")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsKleinMateriaal = prijsKleinMateriaal + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("spiegelhuis")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsSpiegelhuis = prijsSpiegelhuis + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("arbeid")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsArbeid = prijsArbeid + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("traanplaat")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsTraanplaat = prijsTraanplaat + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("branderwerken")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        branderwerken = branderwerken + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("band")&& (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                        for(String w : words){
                            if(w.matches("21")) {
                                wordMatches21 = true;
                            }
                        }
                        if(wordMatches21 && lines[i].contains(",")) {
                            prijsBand = prijsBand + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                            tireRepair = true;
                            //System.out.println("TIRE REPAIR: "+lines[i]);
                        }
                        else{
                            //System.out.println("REJECTRED AS TIRE CLASSIFICATION: "+lines[i]);
                        }
                    } else if (lines[i].contains("remmentest")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        if(!words[1].matches("van")) {
                            prijsRemmenTest = prijsRemmenTest + Double.parseDouble(words[words.length - 2].replace(".", "").replace(",", "."));
                        }
                    } else if (lines[i].contains("trekveer")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsTrekVeer = prijsTrekVeer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("smeervet")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsSmeerVet = prijsSmeerVet + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("lastwiel")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsLastwiel = prijsLastwiel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("trekstang")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsTrekstang = prijsTrekstang + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("as")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        for (int j = 0; j < words.length; j++) {
                            if (words[j].matches("as")) {
                                prijsAs = prijsAs + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                            }
                        }
                    } else if (lines[i].contains("vorkwiel")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsVorkwiel = prijsVorkwiel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("buiscoupille")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsBuiscoupille = prijsBuiscoupille + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("bacheplank")&& (!(lines[i].contains("controleren"))) && (lines[i].contains("21"))) {
                        prijsBacheplank = prijsBacheplank + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("fietsbaar")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsFietsbaar = prijsFietsbaar + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("lamp")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsLamp = prijsLamp + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("wielmoer")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsWielmoer = prijsWielmoer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("diagnoseapparatuur")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsDiagnoseApparatuur = prijsDiagnoseApparatuur + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("motorolie")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsMotorolie = prijsMotorolie + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("regeneenheid")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsRegeneenheid = prijsRegeneenheid + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("spatlap")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsSpatlap = prijsSpatlap + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("vijzen")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsAluVijzen = prijsAluVijzen + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("spanbaar")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsSpanbaar = prijsSpanbaar + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("achterlicht")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsAchterlicht = prijsAchterlicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("mistlicht")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsMistlicht = prijsMistlicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("plaatverligting")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsPlaatverligting = prijsPlaatverligting + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("werklicht")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsWerklicht = prijsWerklicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("katoog")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsKatoogDriehoek = prijsKatoogDriehoek + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("verbindingsdoos")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijstVerbindingsdoos = prijstVerbindingsdoos + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("zijlicht")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsZijlicht = prijsZijlicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("draad")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsDraad = prijsDraad + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("lens")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsLensFlatpoint = prijsLensFlatpoint + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("positielicht")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsPositielicht = prijsPositielicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("meubeldop")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsMeubeldop = prijsMeubeldop + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("opvouwdoos")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsOpvouwDoos = prijsOpvouwDoos + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("schakelaar")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsSchakelaar = prijsSchakelaar + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("afdekkap")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsAfdekkapje = prijsAfdekkapje + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("reparatiezeil")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsReparatiezeil = prijsReparatiezeil + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("bachelijm")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsBachelijm = prijsBachelijm + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("wegwerpkwast")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsWegwerpkwast = prijsWegwerpkwast + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("opgegoten")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsopgegotenTek = prijsopgegotenTek + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("wielbouten")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsWielbouten = prijsWielbouten + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("stroomgroep")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsStroomgroep = prijsStroomgroep + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("primer")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsPrimer = prijsPrimer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("zwart mat")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsZwarteMat = prijsZwarteMat + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("ral")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsRal = prijsRal + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("electrospiraal")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsElectrospiraal = prijsElectrospiraal + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("remreiniger")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsRemreiniger = prijsRemreiniger + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("spiraalkabel")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsSpiraalKabel = prijsSpiraalKabel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("slang")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsSlang = prijsSlang + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("ebs")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsEbskabel = prijsEbskabel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("verplaatsing")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsVerplaatsing = prijsVerplaatsing + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("oring")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsOring = prijsOring + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("drukring")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsDrukring = prijsDrukring + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("twistlock")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsTwistlock = prijsTwistlock + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("koppeling servo")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsKoppelingServo = prijsKoppelingServo + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("equivis")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsEquivis = prijsEquivis + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("balanceercompound")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsBalanceercompound = prijsBalanceercompound + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("hijsband")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsHijsband = prijsHijsband + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("helm")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsHelmWit = prijsHelmWit + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("depannage")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21")) && !(lines[i].contains("21/"))) {
                        prijsDepannage = prijsDepannage + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("luchtketel")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijstLuchtketel = prijstLuchtketel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("knie")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsKnie = prijsKnie + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("breedtelicht")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsBreedtelicht = prijsBreedtelicht + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("trommel")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsTrommel = prijsTrommel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("remblok")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsRemblok = prijsRemblok + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("keerring")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsKeerring = prijsKeerring + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("remveer")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsRemveer = prijsRemveer + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("remsleutel")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsRemsleutel = prijsRemsleutel + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("reparatieset")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsReparatieset = prijsReparatieset + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("M16")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsM16 = prijsM16 + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("veerremcilinder")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsVeerremcilinder = prijsVeerremcilinder + Double.parseDouble(words[words.length - 2].replace(",", "."));
                    } else if (lines[i].contains("firestone")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsFirestone = prijsFirestone + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("naafdop")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsNaafDop = prijsNaafDop + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("kinegrip")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsKinegrip = prijsKinegrip + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("antispray")&& (!(lines[i].contains("controleren")))&& (lines[i].contains("21"))) {
                        prijsAntispray = prijsAntispray + Double.parseDouble(words[words.length - 2].replace(".","").replace(",", "."));
                    } else if (lines[i].contains("21")&& (!(lines[i].contains("controleren")))) {
                        for (int k = 0; k < words.length; k++) {
                            if(i > 1) {
                                if (lines[i - 2].contains("belastbaar")) {
                                }
                                else if(words[0].matches("belcrownlaan")){
                                }
                                else if (words[k].matches("21")) {
                                    unclassifiedCosts = unclassifiedCosts + Double.parseDouble(words[words.length - 2].replace(".","").replace(",","."));
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
                if(vehicleService.existVehicleByNumberPlatte(np.toUpperCase())) {
                    v3.addInvoiceToVehicle(date, invoice);
                    vehicleService.save(v3);
                }
            }
            document.close();
        }
        System.out.println("Finished parsing maintenace invoices");



        //Cummulate all data between two tire repairs to create a data point (don't forget to change first tire repair date)

        System.out.println("Started data cumulative tire repairs");
        List<Vehicle> vehicles = vehicleService.findAll();
        for(Vehicle v: vehicles){
            if(v instanceof Truck) {
                System.out.println(v.getNumberPlate());
                TreeMap<Date, Invoice> vehicleInvoices = new TreeMap<>(v.getInvoices());
                System.out.println("Size invoices: "+ vehicleInvoices.size());
                TreeMap<Date, Invoice> tireRepairInvoices = new TreeMap<>();

                for(Map.Entry<Date, Invoice> entry : vehicleInvoices.entrySet()){
                    if(entry.getValue().getTireRepair()){
                        tireRepairInvoices.put(entry.getKey(),entry.getValue());
                    }
                }
                System.out.println("Size tire repair invoices" + tireRepairInvoices.size());
                System.out.println("tire repair invoices keys");
                for (Map.Entry<Date, Invoice> entry : tireRepairInvoices.entrySet()) {
                    System.out.println(entry.getKey().toString());
                }

                for (Map.Entry<Date, Invoice> entry : tireRepairInvoices.entrySet()) {
                    System.out.println(entry.getKey().toString());

                    Date date = entry.getKey();
                    TreeMap<Date, CumulativeDataBetweenTireRepairsTrucks> currentRepairMap = new TreeMap<>(((Truck) v).getTireRepairs());
                    if(!currentRepairMap.isEmpty()) {
                        System.out.println("not first tire repair");
                        Date previousRepair = currentRepairMap.lastKey();
                        CumulativeDataBetweenTireRepairsTrucks cumulativeDataBetweenTireRepairsTrucks = maintenanceService.calculateCumulativeDataBetweenTireRepairsTrucks(v, previousRepair, date);

                        ((Truck) v).addTireRepair(date, cumulativeDataBetweenTireRepairsTrucks);
                    }
                    else{
                        System.out.println("first tire repair");
                        String firstDateString = "01-01-2020";
                        Date firstDate = new SimpleDateFormat("dd-MM-yyyy").parse(firstDateString);
                        Date previousRepair = firstDate;

                        CumulativeDataBetweenTireRepairsTrucks cumulativeDataBetweenTireRepairsTrucks = maintenanceService.calculateCumulativeDataBetweenTireRepairsTrucks(v, previousRepair, date);

                        if(!cumulativeDataBetweenTireRepairsTrucks.getNumberplate().matches("noValidCumulationPeriod")) {
                            ((Truck) v).addTireRepair(date, cumulativeDataBetweenTireRepairsTrucks);
                        }
                    }
                }
            }
            vehicleService.save(v);
        }

        maintenanceService.createTPOTCsvFile();

    }
}
