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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ALATUPDATEController implements Initializable {

    @FXML
    private TextField txtAlat;

    @FXML
    private TextField txtJenisAlat;

    @FXML
    private TextField txtNamaJenis;

    @FXML
    private TextField txtNamaAlat;

    @FXML
    private TextField txtMerk;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnBatal;

    @FXML
    private TableView<Alat> TBVAlat;

    @FXML
    private TableColumn<Alat, String> ColAlat, ColJenis, ColNama, ColMerk;

    @FXML
    private TextField txtCariID;

    @FXML
    private TextField txtCariNama;

    @FXML
    private RadioButton RBAlat;

    @FXML
    private RadioButton RBNama;

    private ObservableList<Alat> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
        setupListeners();
    }

    public static class Alat {
        String idAlat, idJnsAlat, namaAlat, Merk, Status;

        public Alat(String idAlat, String idJnsAlat, String namaAlat, String Merk, String Status) {
            this.idAlat = idAlat;
            this.idJnsAlat = idJnsAlat;
            this.namaAlat = namaAlat;
            this.Merk = Merk;
            this.Status = Status;
        }

        public String getIdAlat() { return idAlat; }
        public String getIdJnsAlat() { return idJnsAlat; }
        public String getNamaAlat() { return namaAlat; }
        public String getMerk() { return Merk; }
        public String getStatus() { return Status; }
    }

    private void initializeTableColumns() {
        ColAlat.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
        ColJenis.setCellValueFactory(new PropertyValueFactory<>("idJnsAlat"));
        ColNama.setCellValueFactory(new PropertyValueFactory<>("namaAlat"));
        ColMerk.setCellValueFactory(new PropertyValueFactory<>("Merk"));
    }

    private void setupListeners() {
        TBVAlat.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                Alat selectedAlat = TBVAlat.getSelectionModel().getSelectedItem();
                if (selectedAlat != null) {
                    txtAlat.setText(selectedAlat.getIdAlat());
                    txtJenisAlat.setText(selectedAlat.getIdJnsAlat());
                    txtNamaAlat.setText(selectedAlat.getNamaAlat());
                    txtMerk.setText(selectedAlat.getMerk());
                }
            }
        });

        txtCariID.setOnKeyTyped(event -> {
            RBAlat.setSelected(true);
            RBNama.setSelected(false);
            searchById();
        });

        txtCariNama.setOnKeyTyped(event -> {
            RBAlat.setSelected(false);
            RBNama.setSelected(true);
            searchByName();
        });
    }

    @FXML
    private void onBtnUpdateClick() {
        String idAlat = txtAlat.getText();
        String idJnsAlat = txtJenisAlat.getText();
        String namaAlat = txtNamaAlat.getText();
        String Merk = txtMerk.getText();
        String Status = "Aktif"; // Status defaults to "Aktif" on update

        String query = "EXEC sp_UpdateAlat ?,?,?,?,?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idAlat);
            preparedStatement.setString(2, idJnsAlat);
            preparedStatement.setString(3, namaAlat);
            preparedStatement.setString(4, Merk);
            preparedStatement.setString(5, Status);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Update data alat berhasil");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat update alat: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnDeleteClick() {
        String idAlat = txtAlat.getText();

        String query = "EXEC sp_DeleteAlat ?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idAlat);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Alat berhasil dinonaktifkan");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat menonaktifkan alat: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnBatalClick() {
        txtAlat.clear();
        txtJenisAlat.clear();
        txtNamaAlat.clear();
        txtMerk.clear();
    }

    private void loadData() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Alat";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Status").equalsIgnoreCase("aktif")){
                    oblist.add(new Alat(rs.getString("idAlat"),
                            rs.getString("idJnsAlat"),
                            rs.getString("namaAlat"),
                            rs.getString("Merk"),
                            rs.getString("Status")));
                }

            }
            TBVAlat.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat memuat data alat: " + e.getMessage());
        }
    }

    private void searchById() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Alat WHERE idAlat LIKE '%" + txtCariID.getText() + "%'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new Alat(rs.getString("idAlat"),
                        rs.getString("idJnsAlat"),
                        rs.getString("namaAlat"),
                        rs.getString("Merk"),
                        rs.getString("Status")));
            }
            TBVAlat.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari alat: " + e.getMessage());
        }
    }

    private void searchByName() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Alat WHERE namaAlat LIKE '%" + txtCariNama.getText() + "%'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new Alat(rs.getString("idAlat"),
                        rs.getString("idJnsAlat"),
                        rs.getString("namaAlat"),
                        rs.getString("Merk"),
                        rs.getString("Status")));
            }
            TBVAlat.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari alat: " + e.getMessage());
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
            // Load the FXML file for the "Desain" page
            FXMLLoader loader = new FXMLLoader(DashboardAdminController.class.getResource("DashboardAdmin.fxml"));
            Parent UpdateAlatRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(UpdateAlatRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
