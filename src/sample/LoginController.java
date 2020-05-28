package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    //objects from fxml
    @FXML
    TextField player;
    @FXML
    Button play;

    @FXML
    void initialize() {
    }

//basic play button handle
    public static String name;
    public void Play() throws IOException {

        //getting name inputed

        name = player.getText();

        //loading next fxml window

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        //geting it's controller

        Controller controller = loader.getController();

        //parsing anem var using settName method in Controller

        controller.settName(name);
        controller.setHS();

        //Starting new window

        Stage primaryStage = new Stage();
        primaryStage.setTitle("KL");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);

        //opening new window

        primaryStage.show();
        Stage stage = (Stage) play.getScene().getWindow();
        stage.close();
    }
    public void PlayOnline() throws IOException {

        //getting name inputed

        name = player.getText();

        //loading next fxml window

        FXMLLoader loader = new FXMLLoader(getClass().getResource("1v1.fxml"));
        Parent root = loader.load();

        //geting it's controller

        Controller controller = loader.getController();

        //parsing anem var using settName method in Controller

        controller.settName(name);
        controller.setHS();

        //Starting new window

        Stage primaryStage = new Stage();
        primaryStage.setTitle("KL");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);

        //opening new window

        primaryStage.show();
        Stage stage = (Stage) play.getScene().getWindow();
        stage.close();
    }
}
