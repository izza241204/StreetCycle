package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardManagerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class updateJabatanController {

    @FXML
    private TableView<Jabatan> tableJabatan;
    @FXML
    private TableColumn<Jabatan, String> tblidJab;
    @FXML
    private TableColumn<Jabatan, String> tblnamaJab;
    @FXML
    private TextField txtidJab;
    @FXML
    private TextField txtnamaJabatan;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private RadioButton rbidJabatan;
    @FXML
    private RadioButton rbNama;
    @FXML
    private TextField txtSearchidJabatan;
    @FXML
    private TextField txtSearchnamaJabatan;
    @FXML
    private Button btnBatal;
    private ObservableList<Jabatan> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();

    public class Jabatan {
        String idJabatan, namaJabatan, Status;
           public Jabatan(String idJabatan, String namaJabatan) {
                this.idJabatan = idJabatan;
                this.namaJabatan = namaJabatan;
            }

        public String getIdJabatan() {
            return idJabatan;
        }

        public String getNamaJabatan() {
            return namaJabatan;
        }
    }

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadData();
        setupListeners();
    }

    private void initializeTableColumns() {
        tblidJab.setCellValueFactory(new PropertyValueFactory<>("idJabatan"));
        tblnamaJab.setCellValueFactory(new PropertyValueFactory<>("namaJabatan"));
    }

    private void setupListeners() {
        tableJabatan.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                Jabatan selectedJabatan = tableJabatan.getSelectionModel().getSelectedItem();
                if (selectedJabatan != null) {
                    txtidJab.setText(selectedJabatan.getIdJabatan());
                    txtnamaJabatan.setText(selectedJabatan.getNamaJabatan());
                }
            }
        });

        txtSearchidJabatan.setOnKeyTyped(event -> {
            rbidJabatan.setSelected(true);
            rbNama.setSelected(false);
            searchById();
        });

        txtSearchnamaJabatan.setOnKeyTyped(event -> {
            rbidJabatan.setSelected(false);
            rbNama.setSelected(true);
            searchByName();
        });
    }

    @FXML
    private void onbtnUpdateClick(ActionEvent event) {
        String idJabatan = txtidJab.getText();
        String namaJabatan = txtnamaJabatan.getText();

        String query = "EXEC sp_UpdateJabatan ?,?,?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idJabatan);
            preparedStatement.setString(2, namaJabatan);
            preparedStatement.setString(3, "Active");
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Job data update successful");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat update jabatan" + e.getMessage());
        }
    }

    @FXML
    private void onbtnDeleteClick(ActionEvent event) {
        String idJabatan = txtidJab.getText();

        String query = "EXEC sp_DeleteJabatan ?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idJabatan);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Position has been successfully deactivated");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deactivating the position" + e.getMessage());
        }
    }

    @FXML
    private void onbtnBatalClick(ActionEvent event) {
        txtidJab.clear();
        txtnamaJabatan.clear();
    }

    private void searchById() {
        oblist.clear();
        try {
            String query = "SELECT idJabatan, namaJabatan FROM Jabatan WHERE idJabatan LIKE ? AND Status = 'Active'";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + txtSearchidJabatan.getText() + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                oblist.add(new Jabatan(rs.getString("idJabatan"),
                        rs.getString("namaJabatan")));
            }
            tableJabatan.setItems(oblist);
            preparedStatement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for a job title" + e.getMessage());
        }
    }

    private void searchByName() {
        oblist.clear();
        try {
            String query = "SELECT idJabatan, namaJabatan FROM Jabatan WHERE namaJabatan LIKE ? AND Status = 'Active'";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + txtSearchnamaJabatan.getText() + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                oblist.add(new Jabatan(rs.getString("idJabatan"),
                        rs.getString("namaJabatan")));
            }
            tableJabatan.setItems(oblist);
            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for a job title: " + e.getMessage());
        }
    }

    private void loadData() {
        oblist.clear();
        try {
            Statement stat = connection.conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM Jabatan WHERE Status = 'Active'");

            while (result.next()) {
                oblist.add(new Jabatan(
                        result.getString("idJabatan"),
                        result.getString("namaJabatan")
                ));
            }
            tableJabatan.setItems(oblist);
            result.close();
            stat.close();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR,"Error", "An error occurred when loading job data: " + ex);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private Button btnKeluar;
    @FXML
    public void onBtnKeluar(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(DashboardManagerController.class.getResource("DashboardManager.fxml"));
            Parent inputkaryawanRoot = loader.load();

            Stage stage = (Stage) btnKeluar.getScene().getWindow();
            Scene scene = new Scene(inputkaryawanRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
