package dao;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import converters.DriverConverter;
import converters.UserConverter;
import entities.Driver;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;

import static com.mongodb.client.model.Updates.set;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//DAO class for different MongoDB CRUD operations
//take special note of "id" String to ObjectId conversion and vice versa
//also take note of "_id" key for primary key
public class MongoDBDriverDAO {

    private MongoCollection col;

    public MongoDBDriverDAO(MongoClient mongo) {

        this.col = mongo.getDatabase("RydeDatabase").getCollection("Drivers");
    }

    public void makeAvailable(Driver d){
        this.col.updateOne(
                eq("car no", d.getCarNo()),
                combine(set("availability", "yes")));
    }
    public void makeUnavailable(Driver d){
        this.col.updateOne(
                eq("car no", d.getCarNo()),
                combine(set("availability", "no")));
    }
    public Driver findClosest(LatLong pickupLatLong){
         Iterator iterator = this.col.find().iterator();
         double min = 20000.0;
         String minCarNo = "";
         Document doc;
        while (iterator.hasNext()){
            doc = (Document) iterator.next();
            System.out.println(doc);
            String lat2 = (String) doc.get("latitude");
            String lon2 = (String) doc.get("longitude");

            String avail = (String) doc.get("availability");

            LatLong driverLatLong = new LatLong(Double.parseDouble(lat2), Double.parseDouble(lon2));

            double distance = findDistance(pickupLatLong, driverLatLong);
            if(min>distance && avail.equals("yes")){
                min = distance;
                minCarNo = (String) doc.get("car no");
            } else if (min == distance && avail.equals("yes")) {
                minCarNo = (String) doc.get("car no");
            }
        }
        if(min < 20000.0) {
            doc = (Document) this.col.find(eq("car no", minCarNo)).first();

            Driver d = DriverConverter.toDriver(doc);
            return DriverConverter.toDriver(doc);
        }
        else {
            System.out.println("Not found");
            return null;
        }
    }


    //TODO write this method
    private static double findDistance(LatLong pickupLatLong, LatLong driverLatLong){
        return pickupLatLong.distanceFrom(driverLatLong);
    }
}


