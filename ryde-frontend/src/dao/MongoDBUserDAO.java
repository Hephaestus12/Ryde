package dao;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import converters.UserConverter;
import entities.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.combine;

import static com.mongodb.client.model.Updates.set;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;

import java.util.ArrayList;
import java.util.List;

//DAO class for different MongoDB CRUD operations
//take special note of "id" String to ObjectId conversion and vice versa
//also take note of "_id" key for primary key
public class MongoDBUserDAO {

    private MongoCollection col;

    public MongoDBUserDAO(MongoClient mongo) {

        this.col = mongo.getDatabase("RydeDatabase").getCollection("Users");
    }

    public User createUser(User u) {
        //DBObject doc = UserConverter.toDBObject(u);
        Document doc = UserConverter.toDocument(u);
        this.col.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        u.setId(id.toString());
        return u;
    }

    public void updateUserWallet(User u) {
        //DBObject query = BasicDBObjectBuilder.start()
                //.append("_id", new ObjectId(u.getId())).get();
        //this.col.updateOne(query, UserConverter.toDBObject(u));
        //ObjectId id = (ObjectId) u.getId();
        this.col.updateOne(
                eq("username",  u.getUsername()),
                combine(set("wallet", u.getWallet())));
    }

    /* public List<User> readAllUser() {
        List<User> data = new ArrayList<User>();
        DBCursor cursor = col.find();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            User u = UserConverter.toUser(doc);
            data.add(u);
        }
        return data;
    }*/

    /*public void deleteUser(User u) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(u.getId())).get();
        this.col.remove(query);
    }*/

    /*public User readUser(User u) {
        //DBObject query = BasicDBObjectBuilder.start()
          //      .append("_id", new ObjectId(u.getId())).get();
        //DBObject data = this.col.findOne(query);
        DBObject data = (DBObject) this.col.find(eq("_id", new ObjectId(u.getId()))).first();
        return UserConverter.toUser(data);
    }*/

    public User findByUsername(String s) {
        //DBObject query = BasicDBObjectBuilder.start()
        //      .append("username", s)
        //    .get();
        //DBObject data = this.col.findOne(query);
        Document data = (Document) this.col.find(eq("username", s)).first();
        if(data == null) return null;
        return UserConverter.toUser(data);
    }

}