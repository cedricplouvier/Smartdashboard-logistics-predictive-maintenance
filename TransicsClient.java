package com.example.demo.SmartDashboard.TransicsClient;

import com.example.demo.SmartDashboard.Model.ActivityTrucks;
import com.example.demo.SmartDashboard.Model.Truck;
import com.example.demo.SmartDashboard.Service.ActivitiesTrucksService;
import com.example.demo.SmartDashboard.Service.VehicleService;
import com.example.demo.SmartDashboard.TransicsClient.gen.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.xml.transform.StringResult;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransicsClient extends WebServiceGatewaySupport {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ActivitiesTrucksService activitiesTrucksService;

    private static Login iwsLogin()
    {
        Login loginBlock = new Login();
        loginBlock.setDispatcher("UANTWERPEN");
        loginBlock.setPassword("UANTWERPEN_8462100607");
        loginBlock.setIntegrator("UANTWERPEN");
        loginBlock.setSystemNr(607);
        loginBlock.setApplicationName("myApllication");
        loginBlock.setLanguage("EN");
        return loginBlock;
    }

     public GetVehiclesV13Response getVehicles() throws IOException {
        InterfaceVehicleSelectionV6 vehSel = new InterfaceVehicleSelectionV6();
        vehSel.setIncludePosition(true);
        vehSel.setIncludeDrivers(true);

        List<IdentifierVehicle> vehicleIDs = new ArrayList<>();

        IdentifierVehicle vehicle1 = new IdentifierVehicle();
        vehicle1.setIdentifierVehicleType(EnumIdentifierVehicleType.LICENSE_PLATE);
        vehicle1.setId("1-AJY-385");
        vehicleIDs.add(vehicle1);

        ArrayOfIdentifierVehicle identifiers = new ArrayOfIdentifierVehicle();
        identifiers.getIdentifierVehicle().addAll(vehicleIDs);
        vehSel.setIdentifiers(identifiers);

        GetVehiclesV13 getvehiclesv13 = new GetVehiclesV13();
        getvehiclesv13.setLogin(iwsLogin());
        getvehiclesv13.setVehicleSelection(vehSel);

        StringResult result = new StringResult();
        getMarshaller().marshal(getvehiclesv13, result);
        System.out.println("print" + result);

        GetVehiclesV13Response response = (GetVehiclesV13Response) getWebServiceTemplate().marshalSendAndReceive(getvehiclesv13, new SoapActionCallback("http://transics.org/Get_Vehicles_V13"));
        return response;
    }

    public GetVehicleStatusResponse getVehicleStatus() throws IOException {
        VehicleStatusSelection vehSel = new VehicleStatusSelection();
        vehSel.setIncludeInactive(false);

        List<IdentifierVehicle> vehicleIDs = new ArrayList<>();

        IdentifierVehicle vehicle1 = new IdentifierVehicle();
        vehicle1.setIdentifierVehicleType(EnumIdentifierVehicleType.LICENSE_PLATE);
        vehicle1.setId("1-AJY-385");
        vehicleIDs.add(vehicle1);

        ArrayOfIdentifierVehicle identifiers = new ArrayOfIdentifierVehicle();
        identifiers.getIdentifierVehicle().addAll(vehicleIDs);
        vehSel.setIdentifiers(identifiers);

        GetVehicleStatus getVehicleStatus = new GetVehicleStatus();
        getVehicleStatus.setLogin(iwsLogin());
        getVehicleStatus.setVehicleStatusSelection(vehSel);

        GetVehicleStatusResponse response = (GetVehicleStatusResponse) getWebServiceTemplate().marshalSendAndReceive(getVehicleStatus, new SoapActionCallback("http://transics.org/Get_VehicleStatus"));
        GetVehicleStatusResult a =  response.getGetVehicleStatusResult();
        System.out.println(a.getVehicleStatuses().getVehicleStatusResult().get(0).getLicensePlate());
        return response;
    }

    public GetActivityReportDetailV12Response updateTruckActivity() throws DatatypeConfigurationException {
        ActivityReportSelectionDetailV5 selection = new ActivityReportSelectionDetailV5();
        TruckStrategySelection truckStrategySelection = new TruckStrategySelection();

        List<IdentifierVehicle> vehicleIDs = new ArrayList<>();
        IdentifierVehicle vehicle1 = new IdentifierVehicle();
        vehicle1.setIdentifierVehicleType(EnumIdentifierVehicleType.LICENSE_PLATE);
        vehicle1.setId("1-AJY-385");
        vehicleIDs.add(vehicle1);

        ArrayOfIdentifierVehicle identifiers = new ArrayOfIdentifierVehicle();
        identifiers.getIdentifierVehicle().addAll(vehicleIDs);
        truckStrategySelection.setIdentifierVehicleList(identifiers);
        selection.setResourceStrategySelection(truckStrategySelection);

        ChangedAfter changedAfter = new ChangedAfter();
        Period period = new Period();
        XMLGregorianCalendar until
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(DateTime.now().minusHours(2).toString());
        XMLGregorianCalendar from
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(DateTime.now().minusHours(10).toString());
        period.setFrom(from);
        period.setUntil(until);
        selection.setDateStrategySelection(period);
        selection.setIncludeRegistrations(true);
        selection.setPathInfoReturnType(EnumPathInfoReturnType.DETAIL_INFO);

        GetActivityReportDetailV12 getActivityReportDetail = new GetActivityReportDetailV12();
        getActivityReportDetail.setLogin(iwsLogin());
        getActivityReportDetail.setActivityReportSelection(selection);

        GetActivityReportDetailV12Response response = (GetActivityReportDetailV12Response) getWebServiceTemplate().marshalSendAndReceive(getActivityReportDetail, new SoapActionCallback("http://transics.org/Get_ActivityReportDetail_V12"));
        List<ActivityReportDetailItemV11> activities = response.getGetActivityReportDetailV12Result().getActivityReportDetailItems().getActivityReportDetailItemV11();

        for(int i=0; i< activities.size();i++) {
            if(activities.get(i).getActivity().getName().matches("Driving")){
                ActivityTrucks activity = new ActivityTrucks();
                Truck v = (Truck) vehicleService.findVehicleByNumberPlate(activities.get(i).getVehicle().getLicensePlate());
                activity.setTruck(v);
                v.setMileage(activities.get(i).getKmEnd());

                activity.setNumberPlate(activities.get(i).getVehicle().getLicensePlate());

                LocalDateTime begin = activities.get(i).getBeginDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
                LocalDateTime end = activities.get(i).getEndDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
                long minutes = ChronoUnit.MINUTES.between(begin, end);
                Integer duration = ((int) minutes);
                Double d = duration.doubleValue();
                activity.setDuration(d);

                Date startDate = Date.from(activities.get(i).getBeginDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
                activity.setStartDate(startDate);
                Date endDate = Date.from(activities.get(i).getEndDate().toGregorianCalendar().toZonedDateTime().toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
                activity.setStartDate(endDate);

                activity.setAverageSpeedActivity(activities.get(i).getSpeedAvg());
                activity.setDistance(activities.get(i).getKmEnd()-activities.get(i).getKmBegin());

                activity.setLat(activities.get(i).getPosition().getLatitude());
                activity.setLon(activities.get(i).getPosition().getLongitude());

                activitiesTrucksService.save(activity);
                vehicleService.save(v);
            }
        }
        return response;
    }

    public GetActivityReportDetailV12Response getTruckActivity(DateTime time) throws DatatypeConfigurationException {
        ActivityReportSelectionDetailV5 selection = new ActivityReportSelectionDetailV5();
        TruckStrategySelection truckStrategySelection = new TruckStrategySelection();
        selection.setResourceStrategySelection(truckStrategySelection);

        //ChangedAfter changedAfter = new ChangedAfter();
        Period period = new Period();
        XMLGregorianCalendar until
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(time.plusMinutes(15).toString());
        XMLGregorianCalendar from
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(time.minusMinutes(15).toString());
        period.setFrom(from);
        period.setUntil(until);
        System.out.println("transics activity from: "+from + " until: "+until);
        selection.setDateStrategySelection(period);
        selection.setIncludeRegistrations(true);
        selection.setPathInfoReturnType(EnumPathInfoReturnType.DETAIL_INFO);

        GetActivityReportDetailV12 getActivityReportDetail = new GetActivityReportDetailV12();
        getActivityReportDetail.setLogin(iwsLogin());
        getActivityReportDetail.setActivityReportSelection(selection);

        GetActivityReportDetailV12Response response = (GetActivityReportDetailV12Response) getWebServiceTemplate().marshalSendAndReceive(getActivityReportDetail, new SoapActionCallback("http://transics.org/Get_ActivityReportDetail_V12"));
        return response;
    }

    public GetActivityReportDetailV12Response getTruckActivitySelection(DateTime time, ArrayList<String> truckSelectionNumberPlates) throws DatatypeConfigurationException {
        ActivityReportSelectionDetailV5 selection = new ActivityReportSelectionDetailV5();
        TruckStrategySelection truckStrategySelection = new TruckStrategySelection();

        List<IdentifierVehicle> vehicleIDs = new ArrayList<>();
        for(String truckNumberPlate : truckSelectionNumberPlates) {
            IdentifierVehicle vehicle1 = new IdentifierVehicle();
            vehicle1.setIdentifierVehicleType(EnumIdentifierVehicleType.LICENSE_PLATE);
            vehicle1.setId(truckNumberPlate);
            vehicleIDs.add(vehicle1);
        }
        ArrayOfIdentifierVehicle identifiers = new ArrayOfIdentifierVehicle();
        identifiers.getIdentifierVehicle().addAll(vehicleIDs);
        truckStrategySelection.setIdentifierVehicleList(identifiers);

        selection.setResourceStrategySelection(truckStrategySelection);

        //ChangedAfter changedAfter = new ChangedAfter();
        Period period = new Period();
        XMLGregorianCalendar until
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(time.plusMinutes(15).toString());
        XMLGregorianCalendar from
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(time.minusMinutes(15).toString());
        period.setFrom(from);
        period.setUntil(until);
        System.out.println("transics activity from: "+from + " until: "+until);
        selection.setDateStrategySelection(period);
        selection.setIncludeRegistrations(true);
        selection.setPathInfoReturnType(EnumPathInfoReturnType.DETAIL_INFO);

        GetActivityReportDetailV12 getActivityReportDetail = new GetActivityReportDetailV12();
        getActivityReportDetail.setLogin(iwsLogin());
        getActivityReportDetail.setActivityReportSelection(selection);

        GetActivityReportDetailV12Response response = (GetActivityReportDetailV12Response) getWebServiceTemplate().marshalSendAndReceive(getActivityReportDetail, new SoapActionCallback("http://transics.org/Get_ActivityReportDetail_V12"));
        return response;
    }

    public GetActivityReportDetailV12Response getTruckActivitySelectionInBetweenTimes(DateTime startTime, DateTime stopTime, ArrayList<String> truckSelectionNumberPlates) throws DatatypeConfigurationException {
        ActivityReportSelectionDetailV5 selection = new ActivityReportSelectionDetailV5();
        TruckStrategySelection truckStrategySelection = new TruckStrategySelection();

        List<IdentifierVehicle> vehicleIDs = new ArrayList<>();
        for(String truckNumberPlate : truckSelectionNumberPlates) {
            IdentifierVehicle vehicle1 = new IdentifierVehicle();
            vehicle1.setIdentifierVehicleType(EnumIdentifierVehicleType.LICENSE_PLATE);
            vehicle1.setId(truckNumberPlate);
            vehicleIDs.add(vehicle1);
        }
        ArrayOfIdentifierVehicle identifiers = new ArrayOfIdentifierVehicle();
        identifiers.getIdentifierVehicle().addAll(vehicleIDs);
        truckStrategySelection.setIdentifierVehicleList(identifiers);

        selection.setResourceStrategySelection(truckStrategySelection);

        //ChangedAfter changedAfter = new ChangedAfter();
        Period period = new Period();
        XMLGregorianCalendar until
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(stopTime.toString());
        XMLGregorianCalendar from
                = DatatypeFactory.newInstance().newXMLGregorianCalendar(startTime.toString());
        period.setFrom(from);
        period.setUntil(until);
        System.out.println("transics activity from: "+from + " until: "+until);
        selection.setDateStrategySelection(period);
        selection.setIncludeRegistrations(true);
        selection.setPathInfoReturnType(EnumPathInfoReturnType.DETAIL_INFO);

        GetActivityReportDetailV12 getActivityReportDetail = new GetActivityReportDetailV12();
        getActivityReportDetail.setLogin(iwsLogin());
        getActivityReportDetail.setActivityReportSelection(selection);

        GetActivityReportDetailV12Response response = (GetActivityReportDetailV12Response) getWebServiceTemplate().marshalSendAndReceive(getActivityReportDetail, new SoapActionCallback("http://transics.org/Get_ActivityReportDetail_V12"));
        return response;
    }

}
