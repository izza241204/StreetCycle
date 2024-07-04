package com.example.streetcycle.TampilanAwal;

import com.example.streetcycle.MainApplication;
import com.example.streetcycle.awal.desainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardManagerController {
    @FXML
    private Button btnKeluar;
    @FXML
    public void onBtnKeluar(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the "Desain" page
            FXMLLoader loader = new FXMLLoader(desainController.class.getResource("Desain.fxml"));
            Parent desainRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(desainRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Button btnInputKaryawan;
    public void OnbtnInputKaryawan(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/inputKaryawan.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create Alat");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button btnUpdateKaryawan;
    public void OnbtnUpdateKaryawan(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/updateKaryawan.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create Alat");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button btnInputJabatan;
    public void OnbtnInputJabatan(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/Jabatan.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create Alat");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Button btnUpdateJabatan;
    public void OnbtnUpdateJabatan(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/updateJabatan.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create Alat");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
