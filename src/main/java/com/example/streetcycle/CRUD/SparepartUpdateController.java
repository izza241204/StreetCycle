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

public class SparepartUpdateController implements Initializable {
    @FXML
    private TextField txtCariID;
    @FXML
    private TextField txtCariNama;
    @FXML
    private RadioButton RBSparepart;
    @FXML
    private RadioButton RBNama;

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtJenisSparepart;
    @FXML
    private TextField txtNamaSparepart;
    @FXML
    private TextField txtMerk;
    @FXML
    private TextField txtStok;
    @FXML
    private TextField txtHarga;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBatal;
    @FXML
    private TableView<Sparepart> TBVSparepart;
    @FXML
    private TableColumn<Sparepart, String> ColID, ColJenis, ColNama, ColMerk;
    @FXML
    private TableColumn<Sparepart, Integer> ColStok;
    @FXML
    private TableColumn<Sparepart, Double> ColHarga;

    private ObservableList<Sparepart> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
        setupListeners();
    }

    public static class Sparepart {
        String idSparepart, idJnsSparepart, namaSparepart, merk, status;
        int stok;
        double hargaSatuan;

        public Sparepart(String idSparepart, String idJnsSparepart, String namaSparepart, String merk, int stok, double hargaSatuan, String status) {
            this.idSparepart = idSparepart;
            this.idJnsSparepart = idJnsSparepart;
            this.namaSparepart = namaSparepart;
            this.merk = merk;
            this.stok = stok;
            this.hargaSatuan = hargaSatuan;
            this.status = status;
        }

        public String getIdSparepart() { return idSparepart; }
        public String getIdJnsSparepart() { return idJnsSparepart; }
        public String getNamaSparepart() { return namaSparepart; }
        public String getMerk() { return merk; }
        public int getStok() { return stok; }
        public double getHargaSatuan() { return hargaSatuan; }
        public String getStatus() { return status; }
    }

    private void initializeTableColumns() {
        ColID.setCellValueFactory(new PropertyValueFactory<>("idSparepart"));
        ColJenis.setCellValueFactory(new PropertyValueFactory<>("idJnsSparepart"));
        ColNama.setCellValueFactory(new PropertyValueFactory<>("namaSparepart"));
        ColMerk.setCellValueFactory(new PropertyValueFactory<>("merk"));
        ColStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        ColHarga.setCellValueFactory(new PropertyValueFactory<>("hargaSatuan"));

    }

    private void setupListeners() {
        TBVSparepart.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                Sparepart selectedSparepart = TBVSparepart.getSelectionModel().getSelectedItem();
                if (selectedSparepart != null) {
                    txtID.setText(selectedSparepart.getIdSparepart());
                    txtJenisSparepart.setText(selectedSparepart.getIdJnsSparepart());
                    txtNamaSparepart.setText(selectedSparepart.getNamaSparepart());
                    txtMerk.setText(selectedSparepart.getMerk());
                    txtStok.setText(String.valueOf(selectedSparepart.getStok()));
                    txtHarga.setText(String.valueOf(selectedSparepart.getHargaSatuan()));
                }
            }
        });

        txtCariID.setOnKeyTyped(event -> {
            RBSparepart.setSelected(true);
            RBNama.setSelected(false);
            searchById();
        });

        txtCariNama.setOnKeyTyped(event -> {
            RBSparepart.setSelected(false);
            RBNama.setSelected(true);
            searchByName();
        });
    }

    @FXML
    private void onBtnUpdateClick() {
        String idSparepart = txtID.getText();
        String idJnsSparepart = txtJenisSparepart.getText();
        String namaSparepart = txtNamaSparepart.getText();
        String merk = txtMerk.getText();
        int stok = Integer.parseInt(txtStok.getText());
        double hargaSatuan = Double.parseDouble(txtHarga.getText());
        String status = "Aktif"; // Status defaults to "Aktif" on update

        String query = "EXEC sp_UpdateSparepart ?,?,?,?,?,?,?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idSparepart);
            preparedStatement.setString(2, idJnsSparepart);
            preparedStatement.setString(3, namaSparepart);
            preparedStatement.setString(4, merk);
            preparedStatement.setInt(5, stok);
            preparedStatement.setDouble(6, hargaSatuan);
            preparedStatement.setString(7, status);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Update data sparepart berhasil");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat update sparepart: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnDeleteClick() {
        String idSparepart = txtID.getText();

        String query = "EXEC sp_DeleteSparepart ?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idSparepart);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Sparepart berhasil dinonaktifkan");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat menonaktifkan sparepart: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnBatalClick() {
        txtID.clear();
        txtJenisSparepart.clear();
        txtNamaSparepart.clear();
        txtMerk.clear();
        txtStok.clear();
        txtHarga.clear();
    }

    private void loadData() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Sparepart";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Status").equalsIgnoreCase("Active")) {
                    oblist.add(new Sparepart(rs.getString("idSparepart"),
                            rs.getString("idJnsSparepart"),
                            rs.getString("namaSparepart"),
                            rs.getString("merk"),
                            rs.getInt("stok"),
                            rs.getDouble("hargaSatuan"),
                            rs.getString("status")));
                }
            }
            TBVSparepart.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat memuat data sparepart: " + e.getMessage());
        }
    }

    private void searchById() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Sparepart WHERE idSparepart LIKE '%" + txtCariID.getText() + "%'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new Sparepart(rs.getString("idSparepart"),
                        rs.getString("idJnsSparepart"),
                        rs.getString("namaSparepart"),
                        rs.getString("merk"),
                        rs.getInt("stok"),
                        rs.getDouble("hargaSatuan"),
                        rs.getString("status")));
            }
            TBVSparepart.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari sparepart: " + e.getMessage());
        }
    }

    private void searchByName() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Sparepart WHERE namaSparepart LIKE '%" + txtCariNama.getText() + "%'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new Sparepart(rs.getString("idSparepart"),
                        rs.getString("idJnsSparepart"),
                        rs.getString("namaSparepart"),
                        rs.getString("merk"),
                        rs.getInt("stok"),
                        rs.getDouble("hargaSatuan"),
                        rs.getString("status")));
            }
            TBVSparepart.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari sparepart: " + e.getMessage());
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
            Parent UpdateSparepartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(UpdateSparepartRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
