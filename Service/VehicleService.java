package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.*;
import com.example.demo.SmartDashboard.Repository.TrailerRepo;
import com.example.demo.SmartDashboard.Repository.VehicleRepo;
import com.example.demo.SmartDashboard.TransicsClient.TransicsClient;
import com.example.demo.SmartDashboard.TransicsClient.gen.ActivityReportDetailItemV11;
import com.example.demo.SmartDashboard.TransicsClient.gen.GetActivityReportDetailV12Response;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.*;
import java.util.*;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepo vehicleRepository;
    @Autowired
    private ActivitiesTrailersService activitiesTrailersService;
    @Autowired
    private ActivitiesTrucksService activitiesTrucksService;
    @Autowired
    private TrailerRepo trailerRepository;
    @Autowired
    private TransicsClient transicsClient;

    public List<Vehicle> findAll() {
        return this.vehicleRepository.findAll();
    }

    public void save(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }
    
    public Vehicle findVehicleByNumberPlate(String numberPlate){
        return this.vehicleRepository.findVehicleByNumberPlate(numberPlate);
    }

    public boolean existVehicleByNumberPlatte(String numberPlate){
        return vehicleRepository.existsByNumberPlate(numberPlate);
    }

    public boolean existBySensolusSerial(String sensolusSerial){
        return trailerRepository.existsBySensolusSerial(sensolusSerial);
    }

    public void addVehicle(String numberPlate, String chassis, String sensolusSerial, String type) {
        for (TrailerType v : TrailerType.values()) {
            if (v.name().equals(type)) {
                Trailer trailer = new Trailer(numberPlate, chassis, sensolusSerial, TrailerType.valueOf(type));
                save(trailer);
                return;
            }
        }
        for (TruckBrand v : TruckBrand.values()) {
            if (v.name().equals(type)) {
                save(new Truck(numberPlate, chassis, TruckBrand.valueOf(type)));
                return;
            }
        }
    }


    public void coupleTrailersToTrucks() throws IOException, ParseException, DatatypeConfigurationException, InterruptedException {

        File[] files = new File("src/main/resources/DataTM/selectionJSONSorted").listFiles((dir, name) -> !name.equals(".DS_Store"));

        List<File> sortedFiles = new ArrayList<>();
        for(File f : files) {
            sortedFiles.add(f);
        }
        Collections.sort(sortedFiles);

        for(File file: sortedFiles) {

            try {
                String filePath = "src/main/resources/DataTM/selectionJSONSorted/" + file.getName();
                //String filePath = "src/main/resources/DataTM/selectionJSON/file3.json";

                JSONParser parser = new JSONParser();
                FileReader fileReader = new FileReader(filePath);
                Object obj = parser.parse(fileReader);
                JSONObject jsonObject = (JSONObject) obj;
                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                String state = dataObject.get("state").toString();
                String numberPlate = dataObject.get("thirdPartyId").toString();
                String address = dataObject.get("address").toString();
                String serial = dataObject.get("serial").toString();
                double lonTrailer = (Double) dataObject.get("lng");
                double latTrailer = (Double) dataObject.get("lat");
                Long accuracyLong = (Long) dataObject.get("accuracy");
                int accuracy = accuracyLong.intValue();
                //DateTime time = new DateTime(dataObject.get("time"));
                System.out.println(dataObject.get("time"));
                DateTime time = new DateTime(dataObject.get("time"));
                System.out.println("state: " + state + " time: " + time);
                fileReader.close();

                ActivityTrailersSensolus activityTrailer = new ActivityTrailersSensolus();
                Trailer trailer = (Trailer) findVehicleByNumberPlate(numberPlate);
                activityTrailer.setTrailer(trailer);
                activityTrailer.setAddress(address);
                activityTrailer.setAccuracy(accuracy);
                activityTrailer.setState(state);
                activityTrailer.setSerial(serial);
                activityTrailer.setLon(lonTrailer);
                activityTrailer.setLat(latTrailer);
                activityTrailer.setTimeStamp(time.toDate());
                activityTrailer.setSerial(((Trailer) findVehicleByNumberPlate(numberPlate)).getSensolusSerial());
                activityTrailer.setNumberPlate(numberPlate);

                System.out.println("___________________________________________________________________");
                if (state.matches("START")) {

                    GetActivityReportDetailV12Response response = transicsClient.getTruckActivity(time);
                    List<ActivityReportDetailItemV11> activities = response.getGetActivityReportDetailV12Result().getActivityReportDetailItems().getActivityReportDetailItemV11();

                    //Select all trucks trucks in an area of 20km
                    double maxRange = 20000.00;
                    Map<Truck, Double> possibleTrucksAndDistance = new HashMap<Truck, Double>();
                    for (int i = 0; i < activities.size(); i++) {
                        Double latTruck = activities.get(i).getPosition().getLatitude();
                        Double lonTruck = activities.get(i).getPosition().getLongitude();

                        //System.out.println("lontrailer: "+lonTrailer+ " lat trailer: "+ latTrailer+ " lon truck: "+lonTruck+" lat truck: "+latTruck);

                        if ((lonTruck != null) && (latTruck != null)) {
                            double distance = distancePoints(lonTrailer, latTrailer, lonTruck, latTruck);

                            if (distance < maxRange) {
                                Truck t = (Truck) findVehicleByNumberPlate(activities.get(i).getVehicle().getLicensePlate());
                                if (t != null) {
                                    System.out.println(t);
                                    if (possibleTrucksAndDistance.containsKey(t)) {
                                        if (distance < possibleTrucksAndDistance.get(t)) {
                                            possibleTrucksAndDistance.put(t, distance);
                                            System.out.println("New closest activity for possible truck");
                                        }
                                    } else {
                                        possibleTrucksAndDistance.put(t, distance);
                                    }
                                    System.out.println("distance: " + distance + " activity: " + activities.get(i).getActivity().getName() + " truck: " + activities.get(i).getVehicle().getLicensePlate() +
                                            " time: " + activities.get(i).getBeginDate());
                                }
                            }
                        }
                    }

                    System.out.println("\n Truck in range of 20km 15 min before and after start message " +
                            "\n ___________________________________________________________");
                    for (Map.Entry<Truck, Double> entry : possibleTrucksAndDistance.entrySet()) {
                        Truck t = entry.getKey();
                        System.out.println("truck: " + t);
                    }
                    activityTrailer.setPossibleTrucks(possibleTrucksAndDistance);
                    trailer.getActivities().add(activityTrailer);

                    activitiesTrailersService.save(activityTrailer);
                    save(trailer);
                }

                if (state.matches("STOP")) {

                    ActivityTrailersSensolus latestActivity = trailer.getActivities().get(trailer.getActivities().size() - 1);
                    DateTime startTime = new DateTime(latestActivity.getTimeStamp());
                    Map<Truck, Double> possibleTrucksAndDistance = latestActivity.getPossibleTrucks();
                    Map<Truck, Double> possibleTrucksAndDistanceStopMessage = new HashMap<Truck, Double>();
                    ArrayList<String> numberplatesPossibleTrucks = new ArrayList<>();
                    for (Map.Entry<Truck, Double> entry : possibleTrucksAndDistance.entrySet()) {
                        Truck truck = entry.getKey();
                        numberplatesPossibleTrucks.add(truck.getNumberPlate());
                    }

                    GetActivityReportDetailV12Response response = transicsClient.getTruckActivitySelection(time, numberplatesPossibleTrucks);
                    List<ActivityReportDetailItemV11> activities = response.getGetActivityReportDetailV12Result().getActivityReportDetailItems().getActivityReportDetailItemV11();
                    for (int i = 0; i < activities.size(); i++) {
                        Truck truck = (Truck) findVehicleByNumberPlate(activities.get(i).getVehicle().getLicensePlate());
                        Double latTruck = activities.get(i).getPosition().getLatitude();
                        Double lonTruck = activities.get(i).getPosition().getLongitude();
                        double distance = distancePoints(lonTrailer, latTrailer, lonTruck, latTruck);
                        //System.out.println("TEST: " + activities.get(i).getVehicle().getLicensePlate() + "   " + possibleTrucksAndDistance.get(truck));
                        //System.out.println("distance: " + distance);

                        // get smallest activity distance for STOP message for all possible trucks
                        if (possibleTrucksAndDistanceStopMessage.containsKey(truck)) {
                            if (distance < possibleTrucksAndDistanceStopMessage.get(truck)) {
                                possibleTrucksAndDistanceStopMessage.put(truck, distance);
                            }
                        } else {
                            possibleTrucksAndDistanceStopMessage.put(truck, distance);
                        }
                        System.out.println("distance: " + distance + " activity: " + activities.get(i).getActivity().getName() + " truck: " + activities.get(i).getVehicle().getLicensePlate() +
                                " time: " + activities.get(i).getBeginDate());
                    }

                    //Add stop distance to start distance for possible trucks and get closest average truck from start to stop
                    Truck closestTruck = new Truck();
                    double closestDistance = 20000.00;
                    for (Map.Entry<Truck, Double> entry : possibleTrucksAndDistanceStopMessage.entrySet()) {
                        Truck t = entry.getKey();
                        double totalDistance = (possibleTrucksAndDistance.get(t) + possibleTrucksAndDistanceStopMessage.get(t));
                        System.out.println("Truck: " + t.getNumberPlate() + " smallest stop message activity distance: " + entry.getValue() + " and smallest start message activity distance: " + possibleTrucksAndDistance.get(t) + " || total: " + totalDistance);
                        if (totalDistance < closestDistance) {
                            closestDistance = totalDistance;
                            closestTruck = t;
                        }
                    }
                    System.out.println("most likely truck coupled to trailer: " + closestTruck.getNumberPlate());
                    latestActivity.setTruck(closestTruck);
                    activityTrailer.setTruck(closestTruck);
                    trailer.getActivities().add(activityTrailer);

                    //Cumulate all data from driving between start and stop activity
                    System.out.println("CUMULATE DATA BETWEEN: " + startTime + "and " + time);
                    double averageSpeedTrailerActivity = 0;
                    int averagespeedTrailerActivityCounter = 0;
                    int kilometersTraveledActivity = 0;
                    ArrayList<String> numberplatesClosestTrucks = new ArrayList<>();
                    numberplatesClosestTrucks.add(closestTruck.getNumberPlate());
                    GetActivityReportDetailV12Response response2 = transicsClient.getTruckActivitySelectionInBetweenTimes(startTime, time, numberplatesClosestTrucks);
                    List<ActivityReportDetailItemV11> activities2 = response2.getGetActivityReportDetailV12Result().getActivityReportDetailItems().getActivityReportDetailItemV11();
                    for (int i = 0; i < activities2.size(); i++) {
                        System.out.println(activities2.get(i).getActivity().getName());
                        if (activities2.get(i).getActivity().getName().matches("Driving")) {
                            System.out.println("speed average: " + activities2.get(i).getSpeedAvg());
                            averagespeedTrailerActivityCounter++;
                            averageSpeedTrailerActivity += activities2.get(i).getSpeedAvg();
                            System.out.println("distance: " + (activities2.get(i).getKmEnd() - activities2.get(i).getKmBegin()));
                            kilometersTraveledActivity += (activities2.get(i).getKmEnd() - activities2.get(i).getKmBegin());
                        }
                    }

                    averageSpeedTrailerActivity = averageSpeedTrailerActivity / averagespeedTrailerActivityCounter;

                    ActivityTrailerStartToStop activityTrailerStartToStop = new ActivityTrailerStartToStop();
                    activityTrailerStartToStop.setStartSensolus(latestActivity);
                    activityTrailerStartToStop.setStopSensolus(activityTrailer);
                    activityTrailerStartToStop.setAverageSpeed(averageSpeedTrailerActivity);
                    activityTrailerStartToStop.setDistanceTravalled(kilometersTraveledActivity);
                    activityTrailerStartToStop.setTrailer(trailer);


                    activitiesTrailersService.save(activityTrailerStartToStop);
                    activitiesTrailersService.save(activityTrailer);
                    activitiesTrailersService.save(latestActivity);
                    save(trailer);
                }

                //Unused code in case we want to further improve the accuracy of the coupling algorithm instead of only looking at start and stop messages also on the move messages are tekane into account
            /*if(state.matches("ON_THE_MOVE")){

                Trailer trailer = (Trailer) vehicleRepository.findVehicleByNumberPlate(numberPlate);
                ActivityTrailers latestActivity = trailer.getActivities().get(trailer.getActivities().size()-1);
                Map<Truck, Double> possibleTrucks = latestActivity.getPossibleTrucks();
                ArrayList<String> numberplatesPossibleTrucks = new ArrayList<>();
                for (Map.Entry<Truck, Double> entry : possibleTrucks.entrySet()) {
                    Truck truck = entry.getKey();
                    numberplatesPossibleTrucks.add(truck.getNumberPlate());
                }

                GetActivityReportDetailV12Response response = transicsClient.getTruckActivitySelection(time,numberplatesPossibleTrucks);
                List<ActivityReportDetailItemV11> activities = response.getGetActivityReportDetailV12Result().getActivityReportDetailItems().getActivityReportDetailItemV11();
                for(int i=0; i<activities.size(); i++){
                    Truck truck = (Truck) findVehicleByNumberPlate(activities.get(i).getVehicle().getLicensePlate());
                    Double latTruck = activities.get(i).getPosition().getLatitude();
                    Double lonTruck = activities.get(i).getPosition().getLongitude();
                    double distance = distancePoints(lonTrailer, latTrailer, lonTruck, latTruck);
                    if(distance < possibleTrucks.get(truck)){
                        possibleTrucks.put((Truck) findVehicleByNumberPlate(activities.get(i).getVehicle().getLicensePlate()), distance);
                        System.out.println("distance: "+distance + " activity: " + activities.get(i).getActivity().getName() + " truck: "+ activities.get(i).getVehicle().getLicensePlate());
                    }
                }
            }*/
            }
            catch(NullPointerException e){
                System.out.println("nullpointerexception");
            }
        }
    }

    //Haversine method to calculate the distance between two points.
    public double distancePoints(double lonTrailer, double latTrailer, double lonTruck, double latTruck){
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latTrailer - latTruck);
        double lonDistance = Math.toRadians(lonTrailer - lonTruck);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latTruck)) * Math.cos(Math.toRadians(latTrailer))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
