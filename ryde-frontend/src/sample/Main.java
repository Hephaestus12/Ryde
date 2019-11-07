package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.control.CheckBox;

public class Main extends Application {

    private Stage window;
    Button login;
    Button register;

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
        TextField username = new TextField();
        username.setPromptText("username");
        GridPane.setConstraints(username, 80, 47);

        //password
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        GridPane.setConstraints(password, 80, 48);

        // line between username and password
        Line line = new Line(500, 500, 700 ,500);
        GridPane.setConstraints(line, 80, 49);
        line.setStroke(Color.LIGHTGREY);

        // login button
        login = new Button("Login");

        //remember me option
        CheckBox remember = new CheckBox("Remember me");
        VBox log = new VBox();
        log.setSpacing(5);
        log.getChildren().addAll(login, remember);

        GridPane.setConstraints(log, 80, 50);

        grid.getChildren().addAll(username, password, line, log);

        /*   Register as a new user   */

        Line line4 = new Line(500, 500, 700 ,500); line4.setStroke(Color.LIGHTGREY);

        // First name
        TextField fName = new TextField();
        fName.setPromptText("First Name");

        // Last name
        TextField lName = new TextField();
        lName.setPromptText("Last Name");

        // E-mail
        TextField mail = new TextField();
        mail.setPromptText("E-mail");

        // Phone no,
        TextField phone = new TextField();
        phone.setPromptText("Mobile no.");

        // username
        TextField newUser = new TextField();
        newUser.setPromptText("Create a username");

        // password
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Create a password");

        // confirm your password
        PasswordField confPassword = new PasswordField();
        confPassword.setPromptText("Confirm your password");

        // register button
        register = new Button("Register");

        GridPane.setConstraints(fName, 50, 46);
        GridPane.setConstraints(lName, 50, 47);
        GridPane.setConstraints(mail, 50, 48);
        GridPane.setConstraints(phone, 50, 49);
        GridPane.setConstraints(line4, 50, 50);
        GridPane.setConstraints(newUser, 50, 51);
        GridPane.setConstraints(newPassword, 50, 52);
        GridPane.setConstraints(confPassword, 50, 53);
        GridPane.setConstraints(register, 50, 55);
        grid.getChildren().addAll(fName, lName, mail, phone, newUser, newPassword, confPassword, line4, register);

        //action on clicking login/register

        login.setOnAction(e -> loginClick());
        register.setOnAction(e -> registerClick());

        Scene scene = new Scene(grid, 1300, 1020);
        scene.getStylesheets().add("FrontStylesheet.css");
        window.setScene(scene);

        window.show();
    }

    // When the user clicks login
    private void loginClick(){

    }

    // When the user clicks register
    private void registerClick(){
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
