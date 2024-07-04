package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardAdminController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UpdateMotor implements Initializable {

    @FXML
    private TableView <MotorCustomer> tableMotor;
    @FXML
    private TableColumn<MotorCustomer, String> tblIDMotor;
    @FXML
    private TableColumn<MotorCustomer, String> tblJenisMotor;
    @FXML
    private TableColumn<MotorCustomer, String> tblNoPolisi;
    @FXML
    private TableColumn<MotorCustomer, String> tblNamaCustomer;
    @FXML
    private TableColumn<MotorCustomer, String> tblPabrikan;
    @FXML
    private TableColumn<MotorCustomer, String> ColNoTelp;

    @FXML
    private RadioButton radioButtonNama;
    @FXML
    private RadioButton radioButtonId;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtJenis;
    @FXML
    private TextField txtNoPolis;
    @FXML
    private TextField txtNamaC;
    @FXML
    private TextField txtNoTelp;

    @FXML
    private TextField txtPabrikan;
    @FXML
    private TextField txtStatus;
    @FXML
    private TextField txtSearchId;
    @FXML
    private TextField txtSearchnama;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnCancel;

    private ObservableList<MotorCustomer> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
        setupListeners();
    }


    public  class MotorCustomer {
        String idMotor, jenisMotor, namaCustomer, noHp, noPolisi, pabrikan, Status;

        public MotorCustomer(String idCustomer, String jenisMotor, String namaCustomer, String noHp, String noPolisi, String pabrikan, String Status) {
            this.idMotor = idCustomer;
            this.jenisMotor = jenisMotor;
            this.namaCustomer = namaCustomer;
            this.noHp = noHp;
            this.noPolisi = noPolisi;
            this.pabrikan = pabrikan;
            this.Status = Status;
        }

        public String getIdMotor() { return idMotor; }
        public String getJenisMotor() { return jenisMotor; }
        public String getNamaCustomer() { return namaCustomer; }
        public String getNoTelp() { return noHp; }
        public String getNoPolisi() { return noPolisi; }
        public String getPabrikan() { return pabrikan; }
        public String getStatus() { return Status; }
    }

    private void initializeTableColumns() {
        tblIDMotor.setCellValueFactory(new PropertyValueFactory<>("idMotor"));
        tblJenisMotor.setCellValueFactory(new PropertyValueFactory<>("jenisMotor"));
        tblNamaCustomer.setCellValueFactory(new PropertyValueFactory<>("namaCustomer"));
        ColNoTelp.setCellValueFactory(new PropertyValueFactory<>("noHp"));
        tblNoPolisi.setCellValueFactory(new PropertyValueFactory<>("noPolisi"));
        tblPabrikan.setCellValueFactory(new PropertyValueFactory<>("pabrikan"));
    }

    private void setupListeners() {
        tableMotor.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                MotorCustomer selectedAlat = tableMotor.getSelectionModel().getSelectedItem();
                if (selectedAlat != null) {
                    txtId.setText(selectedAlat.getIdMotor());
                    txtJenis.setText(selectedAlat.getJenisMotor());
                    txtNamaC.setText(selectedAlat.getNamaCustomer());
                    txtNoTelp.setText(selectedAlat.getNoTelp());
                    txtNoPolis.setText(selectedAlat.getNoPolisi());
                    txtPabrikan.setText(selectedAlat.getPabrikan());
                }
            }
        });

        txtSearchId.setOnKeyTyped(event -> {
            radioButtonId.setSelected(true);
            radioButtonNama.setSelected(false);
            searchById();
        });

        txtSearchnama.setOnKeyTyped(event -> {
            radioButtonId.setSelected(false);
            radioButtonNama.setSelected(true);
            searchByName();
        });
    }

    @FXML
    private void onBtnUpdateClick() {
        String idMotor = txtId.getText();
        String idJenisMotor = txtJenis.getText();
        String namaCustomer = txtNamaC.getText();
        String noHp = txtNoTelp.getText();
        String noPolisi = txtNoPolis.getText();
        String pabrikan = txtPabrikan.getText();
        String Status = "Active"; // Status defaults to "Aktif" on update

        String query = "EXEC Sp_UpdateMotorCustomer ?,?,?,?,?,?,?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idMotor);
            preparedStatement.setString(2, idJenisMotor);
            preparedStatement.setString(3, namaCustomer);
            preparedStatement.setString(4, noHp);
            preparedStatement.setString(5, noPolisi);
            preparedStatement.setString(6, pabrikan);
            preparedStatement.setString(7, Status);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Update data  berhasil");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat update : " + e.getMessage());
        }
    }

    @FXML
    private void onBtnDeleteClick() {
        String idMotor = txtId.getText();

        String query = "EXEC Sp_DeleteMotorCustomer ?,?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idMotor);
            preparedStatement.setString(2,"Tidak Aktif");
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", " berhasil dinonaktifkan");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat menonaktifkan alat: " + e.getMessage());
        }
    }


    private void loadData() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Motor_Customer";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Status").equalsIgnoreCase("Active")){
                    oblist.add(new MotorCustomer(rs.getString("idMotorCustomer"),
                            rs.getString("jenisMotor"),
                            rs.getString("namaCustomer"),
                            rs.getString("noHp"),
                            rs.getString("noPolisi"),
                            rs.getString("pabrikan"),
                            rs.getString("Status")));
                }

            }
            tableMotor.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat memuat data alat: " + e.getMessage());
        }
    }

    private void searchById() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Motor_Customer WHERE idMotorCustomer LIKE '%" + txtSearchId.getText() + "%'AND Status ='Active'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new MotorCustomer(rs.getString("idMotorCustomer"),
                        rs.getString("jenisMotor"),
                        rs.getString("namaCustomer"),
                        rs.getString("noHp"),
                        rs.getString("noPolisi"),
                        rs.getString("pabrikan"),
                        rs.getString("Status")));
            }

            tableMotor.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari data Motor Customer: " + e.getMessage());
        }
    }

    private void searchByName() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Motor_Customer WHERE jenisMotor LIKE '%" + txtSearchnama.getText() + "%' AND Status ='Active'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new MotorCustomer(rs.getString("idMotorCustomer"),
                        rs.getString("jenisMotor"),
                        rs.getString("namaCustomer"),
                        rs.getString("noHp"),
                        rs.getString("noPolisi"),
                        rs.getString("pabrikan"),
                        rs.getString("Status")));
            }
            tableMotor.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari data Motor Customer: " + e.getMessage());
        }
    }

    public void onBtnCancelClick(ActionEvent actionEvent) {
        txtId.clear();
        txtJenis.clear();
        txtNamaC.clear();
        txtNoTelp.clear();
        txtNoPolis.clear();
        txtPabrikan.clear();

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
            // Load the FXML file for the "Desain" page
            FXMLLoader loader = new FXMLLoader(DashboardAdminController.class.getResource("DashboardAdmin.fxml"));
            Parent UpdateMotorRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(UpdateMotorRoot );

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

