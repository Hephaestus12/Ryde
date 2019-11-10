package sample;

import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.service.geocoding.GeocodingServiceCallback;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import javafx.stage.Window;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class home implements Initializable, MapComponentInitializedListener{

    AnchorPane anchorPane;

    GoogleMap googleMap;
    GoogleMapView googleMapView;

    TextField dropLocationTextField;
    StringProperty dropLocationStringProperty;
    GeocodingService geocodingService;
    LatLong dropLatLong;
    LatLong pickupLatLong;

    boolean pickup = true;


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
        Button searchDropLocation = new Button("Search");
        anchorPane.getChildren().add(searchDropLocation);
        AnchorPane.setLeftAnchor(searchDropLocation, 215.0);
        AnchorPane.setTopAnchor(searchDropLocation,300.0);
        searchDropLocation.setOnAction( e -> {
            if(pickup)
                searchPickupLocation(dropLocationTextField.getText());
            else
                searchDrop(dropLocationTextField.getText());
        });
    }

    private void searchDrop(String searchedLocation) {
        googleMap.clearMarkers();
        pickup = true;

        MarkerOptions pickupMarkerOptions = new MarkerOptions();
        pickupMarkerOptions.position(pickupLatLong)
                .visible(true)
                .title("Pickup Marker");

        Marker pickupMarker = new Marker(pickupMarkerOptions);
        googleMap.addMarker(pickupMarker);

        geocodingService.geocode(searchedLocation, (GeocodingResult[] results, GeocoderStatus status) -> {

            if( results.length > 0 )
                dropLatLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());

            googleMap.setCenter(dropLatLong);

            MarkerOptions dropMarkerOptions = new MarkerOptions();
            dropMarkerOptions.position(dropLatLong)
                    .visible(Boolean.TRUE)
                    .title("Drop Marker");

            Marker dropMarker = new Marker(dropMarkerOptions);
            googleMap.addMarker(dropMarker);

        });
        dropLocationTextField.setPromptText("Locations fixed");
    }

    private void searchPickupLocation(String searchedLocation) {
        googleMap.clearMarkers();
        pickup = false;

        geocodingService.geocode(searchedLocation, (GeocodingResult[] results, GeocoderStatus status) -> {

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
        this.route();
    }

    private void route() {

    }

    public AnchorPane getAnchorPane(){
        return anchorPane;
    }

    @Override
    public void mapInitialized() {

        geocodingService = new GeocodingService();

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
            LatLong latLong = event.getLatLong();

            MarkerOptions dropMarkerOptions = new MarkerOptions();
            dropMarkerOptions.position(new LatLong(latLong.getLatitude(), latLong.getLongitude()))
                    .visible(Boolean.TRUE)
                    .title("Drop Location");

            Marker dropMarker = new Marker(dropMarkerOptions);
            googleMap.clearMarkers();
            googleMap.addMarker(marker);
            googleMap.addMarker(dropMarker);
            
        });

        googleMap.addMarker(marker);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
