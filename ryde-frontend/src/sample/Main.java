package sample;

import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dao.MongoDBUserDAO;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.control.CheckBox;

import entities.*;

public class Main extends Application {

    private static Stage window;
    static Scene scene;
    Button login;
    Button register;

    // Layout variables for Maps
    GridPane gridPane;
    AnchorPane anchorPane;
    Scene gMapView;

    User user;

    private String id1;
    private String username1;
    private String firstName1;
    private String lastName1;
    private String email1;
    private String mobileNo1;
    private String password1;
    private String password3;
    private int wallet1;

    private String username2;
    private String password2;

    private TextField username;
    private PasswordField password;
    Label wrong;
    Label wrongRegister;

    private TextField fName;
    private TextField lName;
    private TextField phone;
    private TextField newUser;
    private TextField newPassword;
    private TextField confPassword;
    private TextField mail;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Ryde");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 0, 0, 0));
        grid.setVgap(6);
        grid.setHgap(10);

        /*   login   */

        //username
        username = new TextField();
        username.setPromptText("username");
        GridPane.setConstraints(username, 80, 47);


        //password
        password = new PasswordField();
        password.setPromptText("password");
        GridPane.setConstraints(password, 80, 48);

        // wrong username / password label

        wrong = new Label();
        GridPane.setConstraints(wrong, 80, 50);


        // line between username and password
        Line line = new Line(500, 500, 700 ,500);
        GridPane.setConstraints(line, 80, 49);
        line.setStroke(Color.LIGHTGREY);

        // login button
        login = new Button("Login");

        VBox log = new VBox();
        log.setSpacing(5);
        log.getChildren().addAll(login, wrong);

        GridPane.setConstraints(log, 80, 50);

        grid.getChildren().addAll(username, password, line, log);

        /*   Register as a new user   */

        Line line4 = new Line(500, 500, 700 ,500); line4.setStroke(Color.LIGHTGREY);

        // First name
        fName = new TextField();
        fName.setPromptText("First Name");


        // Last name
        lName = new TextField();
        lName.setPromptText("Last Name");


        // E-mail
        mail = new TextField();
        mail.setPromptText("E-mail");


        // Phone no,
        phone = new TextField();
        phone.setPromptText("Mobile no.");


        // username
        newUser = new TextField();
        newUser.setPromptText("Create a username");


        // password
        newPassword = new PasswordField();
        newPassword.setPromptText("Create a password");


        // confirm your password
        confPassword = new PasswordField();
        confPassword.setPromptText("Confirm your password");


        // register buttonsest(Stri
        register = new Button("Register");

        // label for wrong entries
        wrongRegister = new Label();

        fName.setId("registration");
        lName.setId("registration");
        phone.setId("registration");
        mail.setId("registration");
        newUser.setId("registration");
        newPassword.setId("registration");
        confPassword.setId("registration");

        username.setId("login");
        password.setId("login");




        GridPane.setConstraints(fName, 40, 46);
        GridPane.setConstraints(lName, 40, 47);
        GridPane.setConstraints(mail, 40, 48);
        GridPane.setConstraints(phone, 40, 49);
        GridPane.setConstraints(line4, 40, 50);
        GridPane.setConstraints(newUser, 40, 51);
        GridPane.setConstraints(newPassword, 40, 52);
        GridPane.setConstraints(confPassword, 40, 53);
        GridPane.setConstraints(register, 40, 55);
        GridPane.setConstraints(wrongRegister, 40, 56);
        grid.getChildren().addAll(fName, lName, mail, phone, newUser, newPassword, confPassword, line4, register, wrongRegister);

        DropShadow dropShadow = new DropShadow();
        //Adding the shadow when the mouse cursor is on
        login.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        login.setEffect(dropShadow);
                    }
                });

        login.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        login.setEffect(null);
                    }
                });

        register.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        register.setEffect(dropShadow);
                    }
                });

        register.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        register.setEffect(null);
                    }
                });


        //action on clicking login/register

        login.setOnAction(e -> {
            if(loginClick()) {
                System.out.println("Calling maps");
                callMaps();
            }
        });
        register.setOnAction(e -> {
            if(registerClick()) {
                System.out.println("Calling maps");
                callMaps();
            }
        });

        scene = new Scene(grid, 1300, 1020);
        scene.getStylesheets().add("FrontStylesheet.css");
        window.setScene(scene);

        window.show();
    }


    // create a map
    private void callMaps() {
        gridPane = new GridPane();
        anchorPane = new AnchorPane();

        home maps = new home(user);
        anchorPane = maps.getAnchorPane();

        gridPane.getChildren().addAll(anchorPane);

        gMapView = new Scene(gridPane, 1300, 1020);

        gMapView.getStylesheets().add("Home.css");

        window.setScene(gMapView);
    }

    // When the user clicks login
    private boolean loginClick() {

        username2 = username.getText().trim();
        password2 = password.getText().trim();

        MongoClient mongo = MongoClients.create("mongodb+srv://tejsukhatme:sukh2sukh@cluster0-hnxyp.mongodb.net/RydeDatabase?retryWrites=true&w=majority");
        MongoDBUserDAO userDAO = new MongoDBUserDAO(mongo);

        user = userDAO.findByUsername(username2);
        mongo.close();
        //user.printUser();
        System.out.println("username:\t" + username2);
        System.out.println("password:\t" + password2);
        if(user == null) {
            System.out.println("NOT FOUND");
            //TODO display 'wrong username'
            username.setId("wrong");
            wrong.setText("Wrong Username");
            wrong.setId("label");
            return false;
        }
        else {
            if(user.getPassword().equals(password2)){
                System.out.println("CORRECT");
                //correct password
                return true;
            }
            else {
                //TODO display 'incorrect password'
                password.setId("wrong");
                wrong.setText("Wrong Password");
                wrong.setId("label");
                username.setId("nothing");
                System.out.println("INCORRECT PASSWORD");
                return false;
            }
        }
    }

    // When the user clicks register
    private boolean registerClick() {
        firstName1 = fName.getText().trim();
        lastName1 = lName.getText().trim();
        email1 = mail.getText().trim();
        mobileNo1 = phone.getText().trim();
        username1 = newUser.getText().trim();
        password1 = newPassword.getText().trim();
        password3 = confPassword.getText().trim();

        //TODO check if fields are valid and if password is equal to confirm password
        user = new User();
        user.setUsername(username1);
        user.setEmail(email1);
        user.setFirstName(firstName1);
        user.setLastName(lastName1);
        user.setMobileNo(mobileNo1);
        user.setPassword(password1);
        //user.setWallet(0);
        //user.printUser();
        MongoClient mongo = MongoClients.create("mongodb+srv://tejsukhatme:sukh2sukh@cluster0-hnxyp.mongodb.net/RydeDatabase?retryWrites=true&w=majority");
        MongoDBUserDAO userDAO = new MongoDBUserDAO(mongo);

        if(firstName1 == null || lastName1 == null || email1 == null || mobileNo1 == null || username1 == null || password1 == null || password3 == null) {
            wrongRegister.setId("label");
            wrongRegister.setText("Fill in all fields");
            newPassword.setId("nothing");
            confPassword.setId("nothing");
            newUser.setId("nothing");
            mail.setId("nothing");
            phone.setId("nothing");
            return false;
        }
        else if(!password1.equals(password3)){
            wrongRegister.setId("label");
            wrongRegister.setText("Passwords don't match");
            newPassword.setId("wrong");
            confPassword.setId("wrong");
            newUser.setId("nothing");
            mail.setId("nothing");
            phone.setId("nothing");
            return false;
        }
        else if(!email1.substring(email1.length() - 4).equals(".com")) {
            wrongRegister.setId("label");
            wrongRegister.setText("Invalid e-mail");
            newPassword.setId("nothing");
            confPassword.setId("nothing");
            newUser.setId("nothing");
            mail.setId("wrong");
            phone.setId("nothing");
            return false;
        }
        else if(mobileNo1.length() != 10 ) {
                wrongRegister.setId("label");
                wrongRegister.setText("Wrong phone no.");
                newPassword.setId("nothing");
                confPassword.setId("nothing");
                newUser.setId("nothing");
                mail.setId("nothing");
                phone.setId("wrong");
                return false;
        }
        else if(userDAO.findByUsername(user.getUsername()) == null) {
            userDAO.createUser(user);
            System.out.println("User Added Successfully with id=" + user.getId());

            mongo.close();
            return true;
        }
        else {
            //TODO display 'user already exists'
            wrongRegister.setId("label");
            wrongRegister.setText("Username already exists");
            newUser.setId("wrong");
            newPassword.setId("nothing");
            confPassword.setId("nothing");
            mail.setId("nothing");
            System.out.println("User already exists");
            mongo.close();
            return false;
        }


    }


    // When the user clicks logout
    public static void logoutClick() {
        window.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
