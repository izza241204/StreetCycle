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
import java.sql.Statement;
import java.util.ResourceBundle;

public class JenisSparepartInput implements Initializable {
    @FXML
    public AnchorPane InputJenisSparepart;

    @FXML
    private TextField txtIDJenis;
    @FXML
    private TextField txtNama;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnExit;

    private final DBConnect connection = new DBConnect();
    private static final String STATUS_DEFAULT = "Aktif";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtIDJenis.setText(generateID("Jenis_Sparepart", "JS", "idJnsSparepart"));
        txtIDJenis.setEditable(false);
    }

    @FXML
    protected void onBtnAddClick() {
        String idJenisSparepart = txtIDJenis.getText();
        String namaJenisSparepart = txtNama.getText();

        String query = "EXEC sp_InsertJenisSparepart ?,?,?";
        try {
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, idJenisSparepart);
            connection.pstat.setString(2, namaJenisSparepart);
            connection.pstat.setString(3, STATUS_DEFAULT);
            connection.pstat.executeUpdate();
            connection.pstat.close();
            System.out.println("DATA BERHASIL DI TAMBAHKAN");
        } catch (SQLException ex) {
            System.err.println("Terjadi error saat insert data jenis sparepart: " + ex);
        }
    }

    @FXML
    protected void onBtnClearClick() {
        txtIDJenis.clear();
        txtNama.clear();
    }

    private String generateID(String tableName, String formatID, String column) {
        String query = "SELECT TOP 1 " + column + " FROM " + tableName + " ORDER BY " + column + " DESC";
        try (Statement stat = connection.conn.createStatement();
             var result = stat.executeQuery(query)) {

            if (result.next()) {
                String imported = result.getString(column).substring(2);
                int idNumber = Integer.parseInt(imported) + 1;
                return String.format("%s%03d", formatID, idNumber);
            }
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
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
            Parent inputJenisSPRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(inputJenisSPRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
