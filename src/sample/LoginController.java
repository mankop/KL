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
    @FXML
    TextField player;
    @FXML
    TextField IP_address;
    @FXML
    CheckBox Offline;
    @FXML
    Button play;

    @FXML
    void initialize() {
    }


    public static String name;
    public void Play() throws IOException {
        name = player.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.settName(name);
        controller.setHS();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("KL");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        Stage stage = (Stage) play.getScene().getWindow();
        stage.close();
    }
    public void PlayOnline() throws IOException {
        name = player.getText();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("1v1.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.settName(name);
        controller.setHS();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("KL");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        Stage stage = (Stage) play.getScene().getWindow();
        stage.close();
    }
}
