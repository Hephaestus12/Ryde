package converters;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import entities.Driver;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DriverConverter {

    // convert Driver Object to MongoDB DBObject
    // take special note of converting id String to ObjectId
    /*public static Document toDocument(Driver d) {

        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
                .append("username", u.getUsername())
                .append("firstName", u.getFirstName())
                .append("lastName", u.getLastName())
                .append("email", u.getEmail())
                .append("mobileNo", u.getMobileNo())
                .append("password", u.getPassword())
                .append("wallet", u.getWallet());
        if (u.getId() != null)
            builder = builder.append("_id", new ObjectId(u.getId()));
        return builder.get();
        Document doc = new Document("username", u.getUsername())
                .append("username", u.getUsername())
                .append("firstName", u.getFirstName())
                .append("lastName", u.getLastName())
                .append("email", u.getEmail())
                .append("mobileNo", u.getMobileNo())
                .append("password", u.getPassword())
                .append("wallet", u.getWallet());
        return doc;
    } */

    // convert Document to Person
    // take special note of converting ObjectId to String
    public static Driver toDriver(Document doc) {
        Driver d = new Driver();
        d.setName((String) doc.get("Name"));
        d.setAvailability(((String) doc.get("availability")).equals("yes"));
        d.setPhoneNo(((long)doc.get("Phone no.")+""));
        d.setLatitude((String) doc.get("latitude"));
        d.setLongitude((String) doc.get("longitude"));
        d.setCarNo((String) doc.get("car no"));
        d.setRating( doc.get("rating")+"");
        ObjectId id = (ObjectId) doc.get("_id");
        d.setId(id.toString());
        return d;

    }

}