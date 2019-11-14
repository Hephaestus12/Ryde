package converters;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import entities.User;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserConverter {

    // convert User Object to MongoDB DBObject
    // take special note of converting id String to ObjectId
    public static Document toDocument(User u) {
    /*
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
        return builder.get();*/
        Document doc = new Document("username", u.getUsername())
                .append("username", u.getUsername())
                .append("firstName", u.getFirstName())
                .append("lastName", u.getLastName())
                .append("email", u.getEmail())
                .append("mobileNo", u.getMobileNo())
                .append("password", u.getPassword())
                .append("wallet", u.getWallet());
        return doc;
    }

    // convert Document to Person
    // take special note of converting ObjectId to String
    public static User toUser(Document doc) {
        User u = new User();
        u.setUsername((String) doc.get("username"));
        u.setFirstName((String) doc.get("firstName"));
        u.setLastName((String) doc.get("lastName"));
        u.setEmail((String) doc.get("email"));
        u.setMobileNo((String) doc.get("mobileNo"));
        u.setPassword((String) doc.get("password"));
        u.setWallet((int) doc.get("wallet"));
        ObjectId id = (ObjectId) doc.get("_id");
        u.setId(id.toString());
        return u;

    }

}