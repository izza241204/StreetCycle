package com.example.streetcycle.TampilanAwal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TampilanAwalController implements Initializable {
    @FXML
    private ImageView Image;

    @FXML
    private Button btnEnter;

    Stage primaryStage;
    @FXML
/*    private void Image() {
        Image jpg = new Image(getClass().getResourceAsStream("/Image/awal.jpeg"));
        if (jpg != null) {
            Image.setImage(jpg);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Error");
        }
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      //Image();
    }

    @FXML
    private void btnEnter() {
        try {
            primaryStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/streetcycle/awal/Desain.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),570,444);
//            primaryStage.setX(0);
//            primaryStage.setY(0);
            primaryStage.setFullScreen(true);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.show();

            ((Stage) btnEnter.getScene().getWindow()).close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
