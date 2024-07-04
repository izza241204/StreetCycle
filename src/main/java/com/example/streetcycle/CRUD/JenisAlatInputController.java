package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardAdminController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JenisAlatInputController implements Initializable {
    @FXML
    public AnchorPane InputJenisAlat;

    @FXML
    private TextField txtJenisAlat;
    @FXML
    private TextField txtNamaJenis;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    String idJenisAlat, namaJenisAlat, Status = "Aktif"; // Default value for Status
    String imported, base, query;

    DBConnect connection = new DBConnect();

    @FXML
    protected void OnBtnSimpanClick() {
        idJenisAlat = txtJenisAlat.getText();
        namaJenisAlat = txtNamaJenis.getText();

        try {
            String query = "EXEC sp_InsertJenisAlat ?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, idJenisAlat);
            connection.pstat.setString(2, namaJenisAlat);
            connection.pstat.setString(3, Status); // Use the default Status value

            connection.pstat.executeUpdate();
            connection.pstat.close();
            System.out.println("DATA BERHASIL DI TAMBAHKAN");
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat insert data jenis alat: " + ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // No need to fill ComboBox since Status is always "Aktif"
        txtJenisAlat.setText(generateID("Jenis_Alat","JA","idJnsAlat"));
        txtJenisAlat.setEditable(false);
    }

    @FXML
    protected void OnBtnBatalClick() {
        txtJenisAlat.clear();
        txtNamaJenis.clear();
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
            Parent InputJenisAlatRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(InputJenisAlatRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
