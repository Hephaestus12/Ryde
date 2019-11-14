package sample;

import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.service.geocoding.GeocodingServiceCallback;
import com.lynden.gmapsfx.util.MarkerImageFactory;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dao.MongoDBDriverDAO;
import dao.MongoDBUserDAO;
import entities.Driver;
import entities.User;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import javafx.stage.Window;

import java.awt.*;
import java.io.BufferedReader;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class home implements Initializable, MapComponentInitializedListener{

    AnchorPane anchorPane;

    GoogleMap googleMap;
    GoogleMapView googleMapView;

    Button searchDropLocation;
    TextField dropLocationTextField;
    GeocodingService geocodingServiceDrop;
    GeocodingService geocodingServicePickup;
    LatLong dropLatLong;
    LatLong pickupLatLong;

    TextField recenterTextField;
    Button recenterButton;

    String duration;
    String distanceString;
    int distance;
    int fare;

    Label fareLabel;
    Label distanceLabel;
    Label durationLabel;
    Button confirmTrip;
    String name;
    Button logout;
    int walletBalance;
    Button addMoney;
    TextField addMoneyBy;
    Button addToWallet;
    Label wallet;
    Label insufficientLabel;
    Label routeNotFound1;
    Label routeNotFound2;

    // driver details
    Label heading;
    Label driverName;
    Label driverPhone;
    Label driverRating;
    Label carNo;

    Button startTrip;
    Button endTrip;

    boolean pickup = true;
    boolean fixed = false;
    boolean notFound = false;

    User user;
    Driver driver;


    public home() {
        anchorPane = new AnchorPane();

        // initialize the map
        googleMapView = new GoogleMapView();
        googleMapView.addMapInializedListener(this);
        googleMapView.setPrefSize(1600,1600);

        anchorPane.getChildren().add(googleMapView);

        // drop location search option
        dropLocationTextField = new TextField();
        dropLocationTextField.setPromptText("Enter pickup Location");

        AnchorPane.setLeftAnchor(dropLocationTextField, 70.0);
        AnchorPane.setTopAnchor(dropLocationTextField, 300.0);

        anchorPane.getChildren().add(dropLocationTextField);

        // search button to search the location
        searchDropLocation = new Button("Search Pickup Location");
        searchDropLocation.setId("searchLocationButton");
        anchorPane.getChildren().add(searchDropLocation);
        AnchorPane.setLeftAnchor(searchDropLocation, 70.0);
        AnchorPane.setTopAnchor(searchDropLocation,328.0);

        // recenter the map
        recenterTextField = new TextField();
        recenterTextField.setPromptText("Recenter the map");
        recenterButton = new Button("Recenter");
        recenterButton.setId("recenterButton");
        recenterButton.setOnAction(e -> this.recenter(recenterTextField.getText()));

        AnchorPane.setLeftAnchor(recenterTextField, 70.0);
        AnchorPane.setLeftAnchor(recenterButton, 70.0);

        AnchorPane.setTopAnchor(recenterTextField, 200.0);
        AnchorPane.setTopAnchor(recenterButton, 228.0);

        anchorPane.getChildren().addAll(recenterTextField, recenterButton);

        this.displayName();

        searchDropLocation.setOnAction( e -> {
            if(fixed){
                pickupLatLong = null;
                dropLatLong = null;
                fixed = false;
                pickup = true;
                dropLocationTextField.setPromptText("Enter Pickup Location");
                googleMap.clearMarkers();
                searchDropLocation.setText("Search Pickup Location");
                anchorPane.getChildren().removeAll(distanceLabel, durationLabel, fareLabel, confirmTrip);

                if(notFound) {
                    anchorPane.getChildren().removeAll(routeNotFound1, routeNotFound2);
                    notFound = false;
                }
            }
            else if(pickup)
                searchPickupLocation(dropLocationTextField.getText());
            else {
                try {
                    searchDrop(dropLocationTextField.getText());
                } catch (IOException | JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public home(User u){
        user = u;

        anchorPane = new AnchorPane();

        // initialize the map
        googleMapView = new GoogleMapView();
        googleMapView.addMapInializedListener(this);
        googleMapView.setPrefSize(1600,1600);

        anchorPane.getChildren().add(googleMapView);

        // drop location search option
        dropLocationTextField = new TextField();
        dropLocationTextField.setPromptText("Enter pickup Location");

        AnchorPane.setLeftAnchor(dropLocationTextField, 70.0);
        AnchorPane.setTopAnchor(dropLocationTextField, 300.0);

        anchorPane.getChildren().add(dropLocationTextField);

        // search button to search the location
        searchDropLocation = new Button("Search Pickup Location");
        searchDropLocation.setId("searchLocationButton");
        anchorPane.getChildren().add(searchDropLocation);
        AnchorPane.setLeftAnchor(searchDropLocation, 70.0);
        AnchorPane.setTopAnchor(searchDropLocation,328.0);

        // recenter the map
        recenterTextField = new TextField();
        recenterTextField.setPromptText("Recenter the map");
        recenterButton = new Button("Recenter");
        recenterButton.setId("recenterButton");
        recenterButton.setOnAction(e -> this.recenter(recenterTextField.getText()));

        AnchorPane.setLeftAnchor(recenterTextField, 70.0);
        AnchorPane.setLeftAnchor(recenterButton, 70.0);

        AnchorPane.setTopAnchor(recenterTextField, 200.0);
        AnchorPane.setTopAnchor(recenterButton, 228.0);

        anchorPane.getChildren().addAll(recenterTextField, recenterButton);

        this.displayName();

        searchDropLocation.setOnAction( e -> {
            if(fixed){
                pickupLatLong = null;
                dropLatLong = null;
                fixed = false;
                pickup = true;
                dropLocationTextField.setPromptText("Enter Pickup Location");
                googleMap.clearMarkers();
                searchDropLocation.setText("Search Pickup Location");
                anchorPane.getChildren().removeAll(distanceLabel, durationLabel, fareLabel, confirmTrip);

                if(notFound) {
                    anchorPane.getChildren().removeAll(routeNotFound1, routeNotFound2);
                    notFound = false;
                }
            }
            else if(pickup)
                searchPickupLocation(dropLocationTextField.getText());
            else {
                try {
                    searchDrop(dropLocationTextField.getText());
                } catch (IOException | JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    // recenter the map to the specified address
    private void recenter(String recenterText) {

        recenterTextField.setText("");

        geocodingServiceDrop.geocode(recenterText, (GeocodingResult[] geocodingResults, GeocoderStatus geocoderStatus) -> {

            LatLong recenterLatLong = null;

            if( geocodingResults.length > 0 )
                recenterLatLong = new LatLong(geocodingResults[0].getGeometry().getLocation().getLatitude(), geocodingResults[0].getGeometry().getLocation().getLongitude());

            if(recenterLatLong != null)
                googleMap.setCenter(recenterLatLong);
        });
    }

    private void confirm() {
        if(user.getWallet() < 300) {
            insufficientLabel = new Label("You need a minimum balance of 300 to make a booking.");
            insufficientLabel.setId("walletBalanceInsufficient");

            AnchorPane.setLeftAnchor(insufficientLabel, 70.0);
            AnchorPane.setTopAnchor(insufficientLabel, 470.0);

            anchorPane.getChildren().add(insufficientLabel);
        }
        else if(fare > user.getWallet()) {
            insufficientLabel = new Label("Wallet Balance insufficient for the trip");
            insufficientLabel.setId("walletBalanceInsufficient");

            AnchorPane.setLeftAnchor(insufficientLabel, 70.0);
            AnchorPane.setTopAnchor(insufficientLabel, 470.0);

            anchorPane.getChildren().add(insufficientLabel);
        }
        else {
            confirmTrip.setText("Trip Confirmed");
            confirmTrip.setId("addMoneyButton");
            confirmTrip.setOnAction(e -> {});
            this.driverDetails();
        }
    }

    private void driverDetails() {
        MongoClient mongo = MongoClients.create("mongodb+srv://tejsukhatme:sukh2sukh@cluster0-hnxyp.mongodb.net/RydeDatabase?retryWrites=true&w=majority");
        MongoDBDriverDAO driverDAO = new MongoDBDriverDAO(mongo);

        final Driver driver = driverDAO.findClosest(pickupLatLong);

        mongo.close();

        MarkerOptions driverMarkerOptions = new MarkerOptions();
        driverMarkerOptions.position(new LatLong(Double.parseDouble(driver.getLatitude()), Double.parseDouble(driver.getLongitude())))
                .visible(Boolean.TRUE)
                .title("Drop Marker");

        Marker driverMarker = new Marker(driverMarkerOptions);
        googleMap.addMarker(driverMarker);

        heading = new Label("Driver Details");
        driverName = new Label("Name : " + driver.getName().substring(0,1).toUpperCase() + driver.getName().substring(1));
        driverPhone = new Label("Phone : " + driver.getPhoneNo());
        driverRating = new Label("Rating : " + driver.getRating() + " / 5");
        carNo = new Label("Vehicle no. : " + driver.getCarNo());

        heading.setId("heading");

        driverName.setId("driverDetailsText");
        driverPhone.setId("driverDetailsText");
        driverRating.setId("driverDetailsText");
        carNo.setId("driverDetailsText");

        startTrip = new Button("Start Trip");
        endTrip = new Button("End Trip");

        startTrip.setId("tripToggle");
        endTrip.setId("tripToggle");

        HBox hBox = new HBox();
        hBox.setId("hBox");
        hBox.setSpacing(57.0);

        hBox.getChildren().addAll(startTrip, endTrip);

        VBox vBox = new VBox();
        vBox.setSpacing(3.0);
        vBox.getChildren().addAll(heading, driverName, driverPhone, driverRating, carNo, hBox);
        vBox.setId("driverDetails");


        AnchorPane.setLeftAnchor(vBox, 700.0);

        anchorPane.getChildren().add(vBox);

        startTrip.setOnAction(e -> {
            MongoClient mongoClient = MongoClients.create("mongodb+srv://tejsukhatme:sukh2sukh@cluster0-hnxyp.mongodb.net/RydeDatabase?retryWrites=true&w=majority");
            MongoDBDriverDAO mongoDBDriverDAO = new MongoDBDriverDAO(mongoClient);
            mongoDBDriverDAO.makeUnavailable(driver);

            startTrip.setId("tripStarted");
            startTrip.setText("Trip Started");
        });

        endTrip.setOnAction(e -> {
            MongoClient mongoClient = MongoClients.create("mongodb+srv://tejsukhatme:sukh2sukh@cluster0-hnxyp.mongodb.net/RydeDatabase?retryWrites=true&w=majority");
            MongoDBDriverDAO mongoDBDriverDAO = new MongoDBDriverDAO(mongoClient);
            mongoDBDriverDAO.makeAvailable(driver);

            walletBalance = walletBalance - fare;
            user.cutFare(fare);
            wallet.setText("Wallet Balance : " + '\u20B9' + user.getWallet());
            anchorPane.getChildren().removeAll(vBox);
            anchorPane.getChildren().removeAll(distanceLabel, fareLabel, durationLabel, confirmTrip);

            pickupLatLong = null;
            dropLatLong = null;
            fixed = false;
            pickup = true;
            dropLocationTextField.setPromptText("Enter Pickup Location");
            googleMap.clearMarkers();
            searchDropLocation.setText("Search Pickup Location");

            if(walletBalance < 300)
                wallet.setId("walletBalanceInsufficient");
        });

    }

    private void displayName() {
        name = user.getFirstName() + " " + user.getLastName();
        Label nameLabel = new Label("Welcome, " + name);

        logout = new Button("Logout");


        wallet = new Label("Wallet Balance : " + '\u20B9' + user.getWallet());

        if(user.getWallet() < 300)
            wallet.setId("walletBalanceInsufficient");

        addMoney = new Button("Add money");
        addMoney.setId("addMoneyButton");

        VBox vBox = new VBox();
        vBox.setSpacing(3.0);

        logout.setId("logout");

        vBox.getChildren().addAll(nameLabel, wallet, addMoney);

        AnchorPane.setRightAnchor(vBox, 70.0);
        AnchorPane.setTopAnchor(vBox, 50.0);

        AnchorPane.setRightAnchor(logout, 255.0);
        AnchorPane.setTopAnchor(logout, 127.0);

        anchorPane.getChildren().addAll(vBox, logout);

        addMoney.setOnAction(e -> this.addMoney());
        logout.setOnAction(e -> Main.logoutClick());
    }

    private void addMoney() {
        addMoneyBy = new TextField();
        addMoneyBy.setPromptText("Amount");

        addMoneyBy.setId("amount");

        addToWallet = new Button("Add");

        AnchorPane.setRightAnchor(addMoneyBy, 147.0);
        AnchorPane.setTopAnchor(addMoneyBy, 126.0);

        AnchorPane.setRightAnchor(addToWallet, 147.0);
        AnchorPane.setTopAnchor(addToWallet, 126.0);

        addToWallet.setId("addMoneyButton");

        AnchorPane.setTopAnchor(logout, 155.0);

        anchorPane.getChildren().addAll(addMoneyBy, addToWallet);

        addToWallet.setOnAction(e -> addToWallet());
    }

    private void addToWallet() {
        try {
            //walletBalance = walletBalance + Integer.parseInt(addMoneyBy.getText());
            user.addMoney(Integer.parseInt(addMoneyBy.getText()));
            wallet.setText("Wallet Balance : " + '\u20B9' + user.getWallet());
            anchorPane.getChildren().removeAll(addToWallet, addMoneyBy, insufficientLabel);
            AnchorPane.setTopAnchor(logout, 127.0);
            if(user.getWallet() >= 300)
                wallet.setId("walletBalanceSufficient");
        } catch (Exception e) {
            addMoneyBy.setId("walletError");
        }
    }

    private void searchDrop(String searchedLocation) throws IOException, JSONException {
        googleMap.clearMarkers();
        pickup = true;

        dropLocationTextField.setText("");

        geocodingServiceDrop.geocode(searchedLocation, (GeocodingResult[] geocodingResults, GeocoderStatus geocoderStatus) -> {

            MarkerOptions pickupMarkerOptions = new MarkerOptions();
            pickupMarkerOptions.position(pickupLatLong)
                    .visible(true)
                    .title("Pickup Marker");

            Marker pickupMarker = new Marker(pickupMarkerOptions);
            googleMap.addMarker(pickupMarker);

            if( geocodingResults.length > 0 )
                dropLatLong = new LatLong(geocodingResults[0].getGeometry().getLocation().getLatitude(), geocodingResults[0].getGeometry().getLocation().getLongitude());


            googleMap.setCenter(dropLatLong);

            MarkerOptions dropMarkerOptions = new MarkerOptions();
            dropMarkerOptions.position(dropLatLong)
                    .visible(Boolean.TRUE)
                    .title("Drop Marker");

            Marker dropMarker = new Marker(dropMarkerOptions);
            googleMap.addMarker(dropMarker);
            String url = /*"https://maps.googleapis.com/maps/api/distancematrix/json?origins="+*/pickupLatLong.getLatitude()+","+pickupLatLong.getLongitude()+"&destinations="+dropLatLong.getLatitude()+","+dropLatLong.getLongitude()+"&mode=driving&key=";
            try {
                this.getDistanceAndDuration(url);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        });
        dropLocationTextField.setPromptText("Locations fixed");
        searchDropLocation.setText("Reset");
        fixed = true;
        this.distanceDisplay();
    }

    private void distanceDisplay() {
        // this.driverDetails();
        if(distanceString != null) {
            distanceLabel = new Label("Distance : " + distanceString);

            if (distance <= 5) {
                fare = 50;
            } else {
                fare = 50 + 20 * (distance - 5);
            }

            durationLabel = new Label("Duration : " + duration);

            fareLabel = new Label("Fare : " + '\u20B9' + fare);

            distanceLabel.setId("rideInfo");
            fareLabel.setId("rideInfo");
            durationLabel.setId("rideInfo");

            confirmTrip = new Button("Confirm The Trip");
            confirmTrip.setId("confirmTripButton");

            confirmTrip.setOnAction(e -> this.confirm());

            AnchorPane.setLeftAnchor(distanceLabel, 70.0);
            AnchorPane.setLeftAnchor(fareLabel, 70.0);
            AnchorPane.setLeftAnchor(durationLabel, 70.0);
            AnchorPane.setLeftAnchor(confirmTrip, 70.0);
            AnchorPane.setTopAnchor(distanceLabel, 390.0);
            AnchorPane.setTopAnchor(durationLabel, 408.0);
            AnchorPane.setTopAnchor(fareLabel, 426.0);
            AnchorPane.setTopAnchor(confirmTrip, 444.0);


            anchorPane.getChildren().addAll(distanceLabel, fareLabel, durationLabel, confirmTrip);
        }
        else {
            routeNotFound1 = new Label("Route not Found between the specified locations");
            routeNotFound2 = new Label("Pick the nearest road or try another set of locations");
            AnchorPane.setLeftAnchor(routeNotFound1, 70.0);
            AnchorPane.setTopAnchor(routeNotFound1, 370.0);

            AnchorPane.setLeftAnchor(routeNotFound2, 70.0);
            AnchorPane.setTopAnchor(routeNotFound2, 390.0);

            routeNotFound1.setId("walletBalanceInsufficient");
            routeNotFound2.setId("walletBalanceInsufficient");

            anchorPane.getChildren().addAll(routeNotFound1, routeNotFound2);
            notFound = true;
        }
    }


    private void getDistanceAndDuration(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JSONObject json = new JSONObject(jsonText);

        JSONArray json1 = (JSONArray) json.get("rows");
        JSONObject json1obj = (JSONObject)json1.get(0);
        JSONArray json2 = (JSONArray) json1obj.get("elements");
        JSONObject json2obj = (JSONObject)json2.get(0);
        JSONObject distanceObj = (JSONObject) json2obj.get("distance");
        JSONObject durationObj = (JSONObject) json2obj.get("duration");

        distance = (int) distanceObj.get("value") / 1000;
        distanceString = (String) distanceObj.get("text");
        duration = (String) durationObj.get("text");
    }

    private String readAll(BufferedReader rd) throws IOException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int a;
        while ((a = rd.read()) != -1) {
            stringBuilder.append((char) a);
        }
        return stringBuilder.toString();
    }

    private void searchPickupLocation(String searchedLocation) {
        googleMap.clearMarkers();
        pickup = false;

        geocodingServicePickup.geocode(searchedLocation, (GeocodingResult[] results, GeocoderStatus status) -> {

            if( results.length > 0 )
                pickupLatLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());

            googleMap.setCenter(pickupLatLong);

            MarkerOptions pickupMarkerOptions = new MarkerOptions();
            pickupMarkerOptions.position(pickupLatLong)
                    .visible(Boolean.TRUE)
                    .title("Drop Marker");

            Marker pickupMarker = new Marker(pickupMarkerOptions);
            googleMap.addMarker(pickupMarker);

        });
        dropLocationTextField.setPromptText("Enter Drop Location");
        searchDropLocation.setText("Search Drop Location");
        dropLocationTextField.setText("");
    }

    public AnchorPane getAnchorPane(){
        return anchorPane;
    }

    @Override
    public void mapInitialized() {

        geocodingServiceDrop = new GeocodingService();
        geocodingServicePickup = new GeocodingService();

        MapOptions mapOptions = new MapOptions();

        // initializing the map
        mapOptions.center(new LatLong(17.5488, 78.5719))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(12);

        googleMap = googleMapView.createMap(mapOptions);

        // initializing Marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLong(17.5448, 78.5719))
                .visible(Boolean.TRUE)
                .title("Marker");

        Marker marker = new Marker(markerOptions);

        googleMap.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            if(!fixed) {
                LatLong latLong = event.getLatLong();

                MarkerOptions dropMarkerOptions = new MarkerOptions();
                dropMarkerOptions.position(new LatLong(latLong.getLatitude(), latLong.getLongitude()))
                        .visible(Boolean.TRUE)
                        .title("Drop Location");

                Marker dropMarker = new Marker(dropMarkerOptions);
                googleMap.clearMarkers();
                googleMap.addMarker(dropMarker);

                if(pickup){
                    pickupLatLong = latLong;
                    pickup = false;
                    dropLocationTextField.setPromptText("Enter Drop Location");
                    searchDropLocation.setText("Search Drop Location");
                    dropLocationTextField.setText("");
                }
                else {
                    MarkerOptions pickupMarkerOptions = new MarkerOptions();
                    pickupMarkerOptions.position(pickupLatLong)
                            .visible((Boolean.TRUE))
                            .title("Pickup Location");
                    Marker pickupMarker = new Marker(pickupMarkerOptions);
                    googleMap.addMarker(pickupMarker);

                    dropLatLong = latLong;
                    fixed = true;
                    pickup = true;
                    dropLocationTextField.setPromptText("Locations fixed");
                    searchDropLocation.setText("Reset");
                    fixed = true;
                    String url = /*"https://maps.googleapis.com/maps/api/distancematrix/json?origins="+*/pickupLatLong.getLatitude()+","+pickupLatLong.getLongitude()+"&destinations="+dropLatLong.getLatitude()+","+dropLatLong.getLongitude()+"&mode=driving&key=";
                    try {
                        this.getDistanceAndDuration(url);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    this.distanceDisplay();
                }
            }

        });

        googleMap.addMarker(marker);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
