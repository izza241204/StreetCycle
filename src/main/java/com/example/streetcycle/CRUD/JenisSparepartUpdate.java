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

public class JenisSparepartUpdate implements Initializable {

    @FXML
    private AnchorPane UpdateJSparePart;

    @FXML
    private TextField txtJenisSparePart;
    @FXML
    private TextField txtNamaJenis;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBatal;

    @FXML
    private TableView<JenisSparepart> TBVJenisSparepart;
    @FXML
    private TableColumn<JenisSparepart, String> colIdJenisSparepart;
    @FXML
    private TableColumn<JenisSparepart, String> colNamaJenisSparepart;

    @FXML
    private TextField txtCariID;
    @FXML
    private TextField txtCariNama;

    @FXML
    private RadioButton RBJenis;
    @FXML
    private RadioButton RBNama;

    private ObservableList<JenisSparepart> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
        setupListeners();
    }

    private void initializeTableColumns() {
        colIdJenisSparepart.setCellValueFactory(new PropertyValueFactory<>("idJenisSparepart"));
        colNamaJenisSparepart.setCellValueFactory(new PropertyValueFactory<>("namaJenisSparepart"));
    }

    private void setupListeners() {
        TBVJenisSparepart.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                JenisSparepart selected = TBVJenisSparepart.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    txtJenisSparePart.setText(selected.getIdJenisSparepart());
                    txtNamaJenis.setText(selected.getNamaJenisSparepart());
                }
            }
        });

        txtCariID.setOnKeyTyped(event -> {
            RBJenis.setSelected(true);
            RBNama.setSelected(false);
            searchByField("idJnsSparepart", txtCariID.getText());
        });

        txtCariNama.setOnKeyTyped(event -> {
            RBJenis.setSelected(false);
            RBNama.setSelected(true);
            searchByField("namaJnsSparepart", txtCariNama.getText());
        });
    }

    @FXML
    private void onBtnUpdateClick() {
        updateOrDeleteData("EXEC sp_UpdateJenisSparepart ?,?,?", "Update data jenis sparepart berhasil");
    }

    @FXML
    private void onBtnDeleteClick() {
        updateOrDeleteData("EXEC sp_DeleteJenisSparepart ?", "Jenis Sparepart berhasil dinonaktifkan");
    }

    @FXML
    private void onBtnBatalClick() {
        clearFields();
    }

    private void loadData() {
        oblist.clear();
        String query = "SELECT * FROM Jenis_Sparepart WHERE Status = 'Aktif'";
        try (ResultSet rs = connection.stat.executeQuery(query)) {
            while (rs.next()) {
                oblist.add(new JenisSparepart(rs.getString("idJnsSparepart"), rs.getString("namaJnsSparepart"), rs.getString("Status")));
            }
            TBVJenisSparepart.setItems(oblist);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat memuat data jenis sparepart: " + e.getMessage());
        }
    }

    private void searchByField(String field, String value) {
        oblist.clear();
        String query = "SELECT * FROM Jenis_Sparepart WHERE " + field + " LIKE '%" + value + "%'";
        try (ResultSet rs = connection.stat.executeQuery(query)) {
            while (rs.next()) {
                oblist.add(new JenisSparepart(rs.getString("idJnsSparepart"), rs.getString("namaJnsSparepart"), rs.getString("Status")));
            }
            TBVJenisSparepart.setItems(oblist);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari jenis sparepart: " + e.getMessage());
        }
    }

    private void updateOrDeleteData(String query, String successMessage) {
        String idJenisSparepart = txtJenisSparePart.getText();
        String namaJenisSparepart = txtNamaJenis.getText();

        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idJenisSparepart);
            preparedStatement.setString(2, namaJenisSparepart);
            preparedStatement.setString(3, "Aktif");
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", successMessage);
            loadData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtJenisSparePart.clear();
        txtNamaJenis.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class JenisSparepart {
        private final String idJenisSparepart;
        private final String namaJenisSparepart;
        private final String status;

        public JenisSparepart(String idJenisSparepart, String namaJenisSparepart, String status) {
            this.idJenisSparepart = idJenisSparepart;
            this.namaJenisSparepart = namaJenisSparepart;
            this.status = status;
        }

        public String getIdJenisSparepart() {
            return idJenisSparepart;
        }

        public String getNamaJenisSparepart() {
            return namaJenisSparepart;
        }

        public String getStatus() {
            return status;
        }
    }

    @FXML
    private Button btnKeluar;
    @FXML
    public void onBtnKeluar(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the "Desain" page
            FXMLLoader loader = new FXMLLoader(DashboardAdminController.class.getResource("DashboardAdmin.fxml"));
            Parent UpdateJenisSPRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(UpdateJenisSPRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
