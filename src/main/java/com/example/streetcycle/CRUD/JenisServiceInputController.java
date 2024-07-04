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

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JenisServiceInputController implements Initializable {

    @FXML
    private TextField txtId;

    @FXML
    private ComboBox<String> cbIdAlat;
    @FXML
    private TextField txtNamaJenis;

    @FXML
    private TextField txtHarga;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;

    private DBConnect connection = new DBConnect();

    String idJnsService, idAlat, namaJnsService;
    String status;
    String query, base, imported;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillComboBox();

        txtId.setEditable(false);
        txtId.setText(generateID("Jenis_Service","JS","idJnsService"));
    }

    @FXML
    protected void onBtnSaveClick() {
        idJnsService = txtId.getText();
        idAlat = cbIdAlat.getValue();
        namaJnsService = txtNamaJenis.getText();
        double hargaService = Double.parseDouble(txtHarga.getText());
        status = "Aktif"; // Default status

        try {
            String query = "EXEC sp_InsertJenisService ?, ?, ?, ?, ?";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, idJnsService);
            preparedStatement.setString(2, idAlat);
            preparedStatement.setString(3, namaJnsService);
            preparedStatement.setDouble(4, hargaService);
            preparedStatement.setString(5, status);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            showAlert("Success", "Data jenis service berhasil disimpan.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Terjadi error saat insert data jenis service: " + ex.getMessage());
        }
    }

    @FXML
    protected void onBtnClearClick() {
        txtId.clear();
        cbIdAlat.getSelectionModel().clearSelection();
        txtNamaJenis.clear();
        txtHarga.clear();
    }

    private void fillComboBox() {
        try {
            String query ="SELECT idAlat FROM Alat";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.result = connection.pstat.executeQuery();

            while(connection.result.next()){
                String idAlat = connection.result.getString("idAlat");
                cbIdAlat.getItems().add(idAlat);
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
            FXMLLoader loader = new FXMLLoader(DashboardAdminController.class.getResource("DashboardAdmin.fxml"));
            Parent inputJenisServiceRoot = loader.load();

            Stage stage = (Stage) btnKeluar.getScene().getWindow();
            Scene scene = new Scene(inputJenisServiceRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
