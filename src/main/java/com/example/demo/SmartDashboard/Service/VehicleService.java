package com.example.demo.SmartDashboard.Service;

import com.example.demo.SmartDashboard.Model.*;
import com.example.demo.SmartDashboard.Repository.TrailerRepo;
import com.example.demo.SmartDashboard.Repository.VehicleRepo;
import com.example.demo.SmartDashboard.TransicsClient.TransicsClient;
import com.example.demo.SmartDashboard.TransicsClient.gen.ActivityReportDetailItemV11;
import com.example.demo.SmartDashboard.TransicsClient.gen.GetActivityReportDetailV12Response;
import com.example.demo.SmartDashboard.TransicsClient.gen.Period;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        for (TruckType v : TruckType.values()) {
            if (v.name().equals(type)) {
                save(new Truck(numberPlate, chassis, TruckType.valueOf(type)));
                return;
            }
        }
    }

    public void coupleTrailersToTrucks() throws IOException, ParseException, DatatypeConfigurationException, InterruptedException {

        File[] files = new File("src/main/resources/DataTM/selectionJSONSortedTest").listFiles((dir, name) -> !name.equals(".DS_Store"));
        //Arrays.sort(files);
        for(File file: files) {
            String filePath = "src/main/resources/DataTM/selectionJSONSortedTest/" + file.getName();
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
            DateTime time = new DateTime(dataObject.get("time")).minusHours(2);
            System.out.println("state: " + state+ "time: " +time);
            fileReader.close();

            ActivityTrailers activityTrailer = new ActivityTrailers();
            activityTrailer.setTrailer((Trailer) findVehicleByNumberPlate(numberPlate));
            activityTrailer.setAddress(address);
            activityTrailer.setAccuracy(accuracy);
            activityTrailer.setState(state);
            activityTrailer.setLon(lonTrailer);
            activityTrailer.setLat(latTrailer);
            activityTrailer.setTimeStamp(time.toDate());
            activityTrailer.setSerial(((Trailer) findVehicleByNumberPlate(numberPlate)).getSensolusSerial());
            activityTrailer.setNumberPlate(numberPlate);

            if(state.matches("START")){

                GetActivityReportDetailV12Response response = transicsClient.getTruckActivity(time);
                List<ActivityReportDetailItemV11> activities = response.getGetActivityReportDetailV12Result().getActivityReportDetailItems().getActivityReportDetailItemV11();

                //Select all trucks trucks in an area of 10km
                double maxRange = 20000.00;
                Map<Truck, Double> possibleTrucks = new HashMap<Truck, Double>();
                for(int i=0; i<activities.size(); i++){
                    Double latTruck = activities.get(i).getPosition().getLatitude();
                    Double lonTruck = activities.get(i).getPosition().getLongitude();
                    double distance = distancePoints(lonTrailer, latTrailer, lonTruck, latTruck);
                    if(distance < maxRange){
                        possibleTrucks.put((Truck) findVehicleByNumberPlate(activities.get(i).getVehicle().getLicensePlate()), distance);
                        System.out.println("distance: "+distance + " activity: " + activities.get(i).getActivity().getName() + " truck: "+ activities.get(i).getVehicle().getLicensePlate()+
                        " time: "+ activities.get(i).getBeginDate());
                    }
                }
                activityTrailer.setPossibleTrucks(possibleTrucks);
                activitiesTrailersService.save(activityTrailer);
            }

            if(state.matches("STOP") || state.matches("ON_THE_MOVE")){

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
            }

            /*double shortestDistance = 100000;
            for(int i=0; i<activities.size(); i++){
                Double latTruck = activities.get(i).getPosition().getLatitude();
                Double lonTruck = activities.get(i).getPosition().getLongitude();
                double distance = distancePoints(lonTrailer, latTrailer, lonTruck, latTruck);
                if(distance < shortestDistance){
                    shortestDistance=distance;
                    System.out.println(shortestDistance + " meters");
                    System.out.println(activities.get(i).getVehicle().getLicensePlate());
                    activityTrailer.setTruck((Truck) findVehicleByNumberPlate(activities.get(i).getVehicle().getLicensePlate()));
                }
            }*/
            //TimeUnit.SECONDS.sleep(5);
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
