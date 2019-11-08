package sample;

import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.service.geocoding.GeocodingServiceCallback;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class home implements MapComponentInitializedListener {

    AnchorPane anchorPane;

    GoogleMap googleMap;
    GoogleMapView googleMapView;

    TextField dropLocationTextField;
    StringProperty dropLocationStringProperty;
    GeocodingService geocodingService;

    public home() {
        anchorPane = new AnchorPane();

        // initialize the map
        googleMapView = new GoogleMapView();
        googleMapView.addMapInializedListener(this);
        googleMapView.setPrefSize(1600,1600);

        anchorPane.getChildren().add(googleMapView);

        // drop location search option
        dropLocationTextField = new TextField();
        dropLocationTextField.setPromptText("Enter Drop Location");

        AnchorPane.setLeftAnchor(dropLocationTextField, 70.0);
        AnchorPane.setTopAnchor(dropLocationTextField, 300.0);

        anchorPane.getChildren().add(dropLocationTextField);

        // search button to search the drop location
        Button searchDropLocation = new Button("Search");
        anchorPane.getChildren().add(searchDropLocation);
        AnchorPane.setLeftAnchor(searchDropLocation, 215.0);
        AnchorPane.setTopAnchor(searchDropLocation,300.0);
        searchDropLocation.setOnAction( e -> {searchDrop(dropLocationTextField.getText());});
    }

    private void searchDrop(String searchedLocation) {

    }

    public AnchorPane getAnchorPane(){
        return anchorPane;
    }

    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();

        // initializing the map
        mapOptions.center(new LatLong(17.5488, 78.5719))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(15);

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
            double distance = latLong.distanceFrom(new LatLong(17.5488, 78.5719));
            this.labelDistance(distance);
        });

        googleMap.addMarker(marker);

    }

    private void labelDistance(double distance) {
        String d = distance + "km";
        Label label = new Label(d);
       // anchorPane.getChildren().addAll(label);

    }

}
