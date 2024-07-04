package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardAdminController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InputMotorCustomer implements Initializable {
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtJenis;
    @FXML
    private TextField txtNoPolisi;
    @FXML
    private TextField txtNamaC;
    @FXML
    private TextField txtNoHp;
    @FXML
    private TextField txtPabrikan;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
    @FXML
    private ImageView imageView;

    String idCustomer, jenisMotor, namaCustomer, noHp, noPolisi, pabrikan, Status = "Active";
    DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtId.setText(generateNextId());
    }

    @FXML
    protected void onBtnSimpanClick() {
        idCustomer = txtId.getText();
        jenisMotor = txtJenis.getText();
        namaCustomer = txtNamaC.getText();
        noHp = txtNoHp.getText();
        noPolisi = txtNoPolisi.getText();
        pabrikan = txtPabrikan.getText();

        if (idCustomer.isEmpty() || jenisMotor.isEmpty() || namaCustomer.isEmpty() || noHp.isEmpty() || noPolisi.isEmpty() || pabrikan.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields must be filled");
            return;
        }

        try {
            String query = "EXEC Sp_InsertMotorCustomer ?,?,?,?,?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, idCustomer);
            connection.pstat.setString(2, jenisMotor);
            connection.pstat.setString(3, namaCustomer);
            connection.pstat.setString(4, noHp);
            connection.pstat.setString(5, noPolisi);
            connection.pstat.setString(6, pabrikan);
            connection.pstat.setString(7, Status);

            connection.pstat.executeUpdate();
            connection.pstat.close();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Data Saved Successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();  // Tambahkan ini untuk log error yang lebih baik
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while inserting data: " + ex.getMessage());
        }
    }

    @FXML
    public void onBtnClearClick(ActionEvent actionEvent) {
        txtId.clear();
        txtJenis.clear();
        txtNamaC.clear();
        txtNoHp.clear();
        txtNoPolisi.clear();
        txtPabrikan.clear();
        txtId.setText(generateNextId());
    }

    private int loadCount() {
        int count = 0;

        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT COUNT(*) FROM Motor_Customer";
            connection.result = connection.stat.executeQuery(query);
            if (connection.result.next()) {
                count = connection.result.getInt(1);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private String generateId(int number) {
        return String.format("MC%03d", number);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String generateNextId() {
        int count = loadCount();
        return generateId(count + 1);
    }

    @FXML
    private Button btnKeluar;
    @FXML
    public void onBtnKeluar(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the "Desain" page
            FXMLLoader loader = new FXMLLoader(DashboardAdminController.class.getResource("DashboardAdmin.fxml"));
            Parent InputMotorRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(InputMotorRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
