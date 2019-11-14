package entities;

//import com.google.gson.Gson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dao.MongoDBUserDAO;

public class User {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private String password;
    private int wallet;

    public User(){
        username = "";
        firstName = "";
        lastName = "";
        email = "";
        mobileNo = "";
        password = "";
        wallet = 0;
    }

    public User(String a, String b, String c, String d, String e, String f, int g) {
        username = a;
        firstName = b;
        lastName = c;
        email = d;
        mobileNo = e;
        password = f;
        wallet = g;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public int getWallet() {
        return wallet;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void printUser() {
        System.out.println(username);
        System.out.println(password);
        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(mobileNo);
        System.out.println(email);
        System.out.println(wallet);
    }

    public void addMoney(int money){
        this.setWallet(this.getWallet() + money);
        MongoClient mongo = MongoClients.create("mongodb+srv://tejsukhatme:sukh2sukh@cluster0-hnxyp.mongodb.net/RydeDatabase?retryWrites=true&w=majority");
        MongoDBUserDAO userDAO = new MongoDBUserDAO(mongo);
        userDAO.updateUserWallet(this);
        mongo.close();
    }

    public void cutFare(int fare){
        this.setWallet(this.getWallet() - fare);
        MongoClient mongo = MongoClients.create("mongodb+srv://tejsukhatme:sukh2sukh@cluster0-hnxyp.mongodb.net/RydeDatabase?retryWrites=true&w=majority");
        MongoDBUserDAO userDAO = new MongoDBUserDAO(mongo);
        userDAO.updateUserWallet(this);
        mongo.close();
    }

    /*public String toJSON() {
        String userJsonString = new Gson().toJson(this);
        return userJsonString;
    }*/
}