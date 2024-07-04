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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JenisserviceUpdateController implements Initializable {
    @FXML
    private TextField txtCariID;
    @FXML
    private TextField txtCariNama;
    @FXML
    private RadioButton RBRBIDService;
    @FXML
    private RadioButton RBNama;

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtAlat;
    @FXML
    private TextField txtNamaJenis;
    @FXML
    private TextField txtHarga;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBatal;
    @FXML
    private TableView<JenisService> TBVJenisService;
    @FXML
    private TableColumn<JenisService, String> ColID, ColAlat, ColNama;
    @FXML
    private TableColumn<JenisService, Double> ColHarga;

    private ObservableList<JenisService> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();

    public static class JenisService {
        String idJnsService, idAlat, namaJnsService, status;
        double hargaService;

        public JenisService(String idJnsService, String idAlat, String namaJnsService, double hargaService, String status) {
            this.idJnsService = idJnsService;
            this.idAlat = idAlat;
            this.namaJnsService = namaJnsService;
            this.hargaService = hargaService;
            this.status = status;
        }

        public String getIdJnsService() { return idJnsService; }
        public String getIdAlat() { return idAlat; }
        public String getNamaJnsService() { return namaJnsService; }
        public double getHargaService() { return hargaService; }
        public String getStatus() { return status; }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
        setupListeners();
    }

    private void initializeTableColumns() {
        ColID.setCellValueFactory(new PropertyValueFactory<>("idJnsService"));
        ColAlat.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
        ColNama.setCellValueFactory(new PropertyValueFactory<>("namaJnsService"));
        ColHarga.setCellValueFactory(new PropertyValueFactory<>("hargaService"));
    }

    private void setupListeners() {
        TBVJenisService.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                JenisService selectedJenisService = TBVJenisService.getSelectionModel().getSelectedItem();
                if (selectedJenisService != null) {
                    txtID.setText(selectedJenisService.getIdJnsService());
                    txtAlat.setText(selectedJenisService.getIdAlat());
                    txtNamaJenis.setText(selectedJenisService.getNamaJnsService());
                    txtHarga.setText(String.valueOf(selectedJenisService.getHargaService()));
                }
            }
        });

        txtCariID.setOnKeyTyped(event -> {
            RBRBIDService.setSelected(true);
            RBNama.setSelected(false);
            searchById();
        });

        txtCariNama.setOnKeyTyped(event -> {
            RBRBIDService.setSelected(false);
            RBNama.setSelected(true);
            searchByName();
        });
    }

    @FXML
    private void onBtnUpdateClick() {
        String idJnsService = txtID.getText();
        String idAlat = txtAlat.getText();
        String namaJnsService = txtNamaJenis.getText();
        double hargaService = Double.parseDouble(txtHarga.getText());
        String status = "Aktif"; // Status defaults to "Aktif" on update

        String query = "EXEC sp_UpdateJenisService ?,?,?,?,?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idJnsService);
            preparedStatement.setString(2, idAlat);
            preparedStatement.setString(3, namaJnsService);
            preparedStatement.setDouble(4, hargaService);
            preparedStatement.setString(5, status);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Update data jenis service berhasil");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat update jenis service: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnDeleteClick() {
        String idJnsService = txtID.getText();

        String query = "EXEC sp_DeleteJenisService ?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idJnsService);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Jenis service berhasil dinonaktifkan");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat menonaktifkan jenis service: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnBatalClick() {
        txtID.clear();
        txtAlat.clear();
        txtNamaJenis.clear();
        txtHarga.clear();
    }

    private void loadData() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Jenis_Service WHERE Status = 'Aktif'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new JenisService(rs.getString("idJnsService"),
                        rs.getString("idAlat"),
                        rs.getString("namaJnsService"),
                        rs.getDouble("hargaService"),
                        rs.getString("Status")));
            }
            TBVJenisService.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat memuat data jenis service: " + e.getMessage());
        }
    }

    private void searchById() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Jenis_Service WHERE idJnsService LIKE '%" + txtCariID.getText() + "%' AND Status = 'Aktif'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new JenisService(rs.getString("idJnsService"),
                        rs.getString("idAlat"),
                        rs.getString("namaJnsService"),
                        rs.getDouble("hargaService"),
                        rs.getString("Status")));
            }
            TBVJenisService.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari jenis service: " + e.getMessage());
        }
    }

    private void searchByName() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Jenis_Service WHERE namaJnsService LIKE '%" + txtCariNama.getText() + "%' AND Status = 'Aktif'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new JenisService(rs.getString("idJnsService"),
                        rs.getString("idAlat"),
                        rs.getString("namaJnsService"),
                        rs.getDouble("hargaService"),
                        rs.getString("Status")));
            }
            TBVJenisService.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari jenis service: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(DashboardAdminController.class.getResource("DashboardAdmin.fxml"));
            Parent updateJenisServiceRoot = loader.load();

            Stage stage = (Stage) btnKeluar.getScene().getWindow();
            Scene scene = new Scene(updateJenisServiceRoot);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
