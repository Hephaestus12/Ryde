package entities;

import javax.xml.namespace.QName;

public class Driver {
    private String id;
    private String name;
    private String phoneNo;
    private String carNo;
    private String latitude;
    private String longitude;
    private String rating;
    private String availability;

    //getters and setters

    public String getAvailability(){
        return this.availability;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getCarNo() {
        return carNo;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getRating() {
        return (rating + "");
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
