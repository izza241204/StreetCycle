package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardAdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SparepartInputController implements Initializable {
    @FXML
    public TextField txtID;
    @FXML
    public ComboBox<String> CBIDJenis;
    @FXML
    public TextField txtNama;
    @FXML
    public TextField txtMerk;
    @FXML
    public TextField txtStok;
    @FXML
    public TextField txtHarga;
    @FXML
    public Button btnAdd;
    @FXML
    public Button btnClear;

    private DBConnect connection = new DBConnect();

    String idSparepart, idJnsSparepart, namaSparepart, merk;
    String status;
    String query, base, imported;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBox();

        txtID.setEditable(false);
        txtID.setText(generateID("Sparepart","SP","idSparepart"));
    }

    @FXML
    protected void onBtnAddClick() {
        idSparepart = txtID.getText();
        idJnsSparepart = CBIDJenis.getValue();
        namaSparepart = txtNama.getText();
        merk = txtMerk.getText();
        int stok = Integer.parseInt(txtStok.getText());
        double hargaSatuan = Double.parseDouble(txtHarga.getText());
        status = "Aktif"; // Default status

        try {
            String query = "EXEC sp_InsertSparepart ?, ?, ?, ?, ?, ?, ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, idSparepart);
            preparedStatement.setString(2, idJnsSparepart);
            preparedStatement.setString(3, namaSparepart);
            preparedStatement.setString(4, merk);
            preparedStatement.setInt(5, stok);
            preparedStatement.setDouble(6, hargaSatuan);
            preparedStatement.setString(7, status);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            showAlert("Success", "Data sparepart berhasil disimpan.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Terjadi error saat insert data sparepart: " + ex.getMessage());
        }
    }

    @FXML
    protected void onBtnClearClick() {
        txtID.clear();
        CBIDJenis.getSelectionModel().clearSelection();
        txtNama.clear();
        txtMerk.clear();
        txtStok.clear();
        txtHarga.clear();
    }

    private void fillComboBox() {
        try {
            String query ="SELECT idJnsSparepart FROM Jenis_Sparepart";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.result = connection.pstat.executeQuery();

            while(connection.result.next()){
                String idsparepart = connection.result.getString("idJnsSparepart");
                CBIDJenis.getItems().add(idsparepart);
            }

            connection.result.close();
            connection.pstat.close();

        } catch (SQLException ex) {
            System.out.println("Gagal saat mengambil data " +ex);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class JenisSparepart {
        private String idJenisSparepart;
        private String namaJenisSparepart;

        public JenisSparepart(String idJenisSparepart, String namaJenisSparepart) {
            this.idJenisSparepart = idJenisSparepart;
            this.namaJenisSparepart = namaJenisSparepart;
        }

        public String getIdJenisSparepart() {
            return idJenisSparepart;
        }

        public String getNamaJenisSparepart() {
            return namaJenisSparepart;
        }

        @Override
        public String toString() {
            return namaJenisSparepart;
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
                for (int i = 2; i < imported.length(); i++) {
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
            Parent inputSparepartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(inputSparepartRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
