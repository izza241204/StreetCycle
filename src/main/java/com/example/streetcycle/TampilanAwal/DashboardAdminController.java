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


public class DashboardAdminController {
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
    private Button btnCariAlat;

    @FXML
    private Button btnUpdateJenisAlat;

    @FXML
    private Button BtnInputJenisAlat;

    @FXML
    private Button btnUpdateAlat;


    @FXML
    private Button btnCariSparepart;

    @FXML
    private Button btnUpdateSparepart;

    @FXML
    private Button btnInputJenisSparepart;

    @FXML
    private Button btnUpdateJenisSparePart;

    @FXML
    private Button btnInputMotor;

    @FXML
    private Button btnUpdateMotor;

    @FXML
    private Button btnInputJenis;

    @FXML
    private Button btnUpdateJenis;



    public void OnbtnUpdateAlat(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/ALATUPDATE.fxml"));
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


    public void OnbtnCariAlat(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/ALATINPUT.fxml"));
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

    public void OnBtnInputJenisAlat(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/JenisAlatInput.fxml"));
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

    public void OnbtnUpdateJenisAlat(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/JenisAlatUpdate.fxml"));
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

    public void OnbtnCariSparepart(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/SparepartInput.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create Sparepart");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void OnbtnUpdateSparepart(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/SparepartUpdate.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Read, Update,Delete Sparepart");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnbtnInputJenisSparepart(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/JenisSparepartInput.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create Jenis Sparepart");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnbtnUpdateJenisSparePart(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/JenisSparepartUpdate.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Read, Update,Delete Jenis Sparepart");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void OnbtnInputMotor(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/InputMotorCustomer.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Create Motor Customer");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnbtnUpdateMotor(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/UpdateMotor.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Read, Update, dan Delete Motor Customer");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void OnbtnInputJenis(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/JenisServiceinput.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Input Jenis Service");
            stage.setScene(scene);

            // Set jendela menjadi fullscreen
            stage.setFullScreen(false);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OnbtnUpdateJenis(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/streetcycle/CRUD/JenisServiceUpdate.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Read, Update, dan Delete Jenis Service");
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