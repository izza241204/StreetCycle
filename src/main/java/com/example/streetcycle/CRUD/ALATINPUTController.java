package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardAdminController;
import com.example.streetcycle.awal.desainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ALATINPUTController implements Initializable {

    @FXML
    private TextField txtAlat;
    @FXML
    private ComboBox<JenisAlat> cbJenisAlat;
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtMerk;

    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    @FXML
    private Button btnkembali;

    private DBConnect connection = new DBConnect();
    String imported, query, base;

    @FXML
    protected void onBtnSimpanClick() {
        String idAlat = txtAlat.getText();
        String idJenisAlat = cbJenisAlat.getValue().getIdJenisAlat();
        String namaAlat = txtNama.getText();
        String merk = txtMerk.getText();
        String status = "Aktif"; // Default status

        try {
            String query = "EXEC sp_InsertAlat ?, ?, ?, ?, ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, idAlat);
            preparedStatement.setString(2, idJenisAlat);
            preparedStatement.setString(3, namaAlat);
            preparedStatement.setString(4, merk);
            preparedStatement.setString(5, status);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            showAlert("Success", "Data alat berhasil disimpan.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Terjadi error saat insert data alat: " + ex.getMessage());
        }
    }

    private void fillComboBox() {
        ObservableList<JenisAlat> jenisAlatList = FXCollections.observableArrayList();
        try {
            String query = "SELECT idJnsAlat, namaJenisAlat FROM Jenis_Alat";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                jenisAlatList.add(new JenisAlat(rs.getString("idJnsAlat"), rs.getString("namaJenisAlat")));
            }
            cbJenisAlat.setItems(jenisAlatList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Terjadi error saat memuat data jenis alat: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBox();
        txtAlat.setText(generateID("Alat","A","idAlat"));

        txtAlat.setEditable(false);
        txtNama.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getText().matches("[a-zA-z]*")) {
                return change;
            }
            return null;
        }));

        txtMerk.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getText().matches("[a-zA-Z]*")) {
                return change;
            }
            return null;
        }));
    }

    @FXML
    protected void onBtnBatalClick() {
        txtAlat.clear();
        cbJenisAlat.getSelectionModel().clearSelection();
        txtNama.clear();
        txtMerk.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class JenisAlat {
        private String idJenisAlat;
        private String namaJenisAlat;

        public JenisAlat(String idJenisAlat, String namaJenisAlat) {
            this.idJenisAlat = idJenisAlat;
            this.namaJenisAlat = namaJenisAlat;
        }

        public String getIdJenisAlat() {
            return idJenisAlat;
        }

        public String getNamaJenisAlat() {
            return namaJenisAlat;
        }

        @Override
        public String toString() {
            return namaJenisAlat;
        }
    }

    public String generateID(String tableName,String formatID,String column) {
        try {
            DBConnect connect = new DBConnect();
            connect.stat = connect.conn.createStatement();
            query = "SELECT TOP 1 " + column + " FROM " + tableName + " ORDER BY " + column + " DESC";
            connect.result = connect.stat.executeQuery(query);
            if (connect.result.next()) {
                imported = connect.result.getString(column);
                connect.result.close();
                connect.stat.close();
                base = "";
                for (int i = 1; i < imported.length(); i++) {
                    base += imported.charAt(i);
                }
                return String.format("%s%03d", formatID, Integer.parseInt(base) + 1);
            }
            connect.stat.close();
            connect.result.close();
            return formatID + "001";
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return formatID + "001";
    }

    @FXML
    private Button btnKeluar;
    @FXML
    public void onBtnKeluar(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the "Desain" page
            FXMLLoader loader = new FXMLLoader(DashboardAdminController.class.getResource("DashboardAdmin.fxml"));
            Parent InputAlatRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(InputAlatRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
