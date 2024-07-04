package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardAdminController;
import com.example.streetcycle.TampilanAwal.DashboardManagerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class inputJabatanController implements Initializable {
    @FXML
    private TextField txtidJab;
    @FXML
    private TextField txtnamaJab;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    String idJabatan, namaJabatan, status="Active";
    String imported, base, query;
    DBConnect connection = new DBConnect();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtidJab.setText(generateID("Jabatan","JB","idJabatan"));

        txtidJab.setEditable(false);
        txtnamaJab.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getText().matches("[a-zA-z]*")) {
                return change;
            }
            return null;
        }));
    }

    public class Jabatan {
        private String idJabatan, namaJabatan, status = "Aktif";
        public Jabatan(String idJabatan, String namaJabatan){
            this.idJabatan = idJabatan;
            this.namaJabatan = namaJabatan;
        }

    }
    @FXML
    protected void onbtnSimpanClick(){
        idJabatan = txtidJab.getText();
        namaJabatan = txtnamaJab.getText();

        if (idJabatan.isEmpty() || namaJabatan.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All field must be filled");
            return;
        }

        try {
            String query="EXEC sp_InsertJabatan ?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1,idJabatan);
            connection.pstat.setString(2, namaJabatan);
            connection.pstat.setString(3, status);

            connection.pstat.executeUpdate();
            connection.pstat.close();
            txtnamaJab.clear();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Data Saved Succesfully");
        }catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR,"Error", "An error occurred while inserting data "+ex);
        }
    }

    @FXML
    protected void onbtnBatalClick(){
        txtidJab.clear();
        txtnamaJab.clear();
    }

    private int karayawanCount() {
        int count = 0;

        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT COUNT(*) FROM Jabatan";
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

    public String generateNextId() {
        int count = karayawanCount();
        return generateId(count + 1);
    }

    private String generateId(int number) {
        return String.format("JB%03d", number);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
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
            FXMLLoader loader = new FXMLLoader(DashboardManagerController.class.getResource("DashboardManager.fxml"));
            Parent updateJenisServiceRoot = loader.load();

            Stage stage = (Stage) btnKeluar.getScene().getWindow();
            Scene scene = new Scene(updateJenisServiceRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
