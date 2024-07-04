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

public class JenisAlatUpdateController implements Initializable {
    @FXML
    public AnchorPane UpdateJAlat;

    @FXML
    private TextField txtJenisAlat;
    @FXML
    private TextField txtNamaJenis;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBatal;

    @FXML
    private TableView<JenisAlat> TBVJenisAlat;
    @FXML
    private TableColumn<JenisAlat, String> colIdJenisAlat, colNamaJenisAlat;

    @FXML
    private TextField txtCariID;
    @FXML
    private TextField txtCariNama;

    @FXML
    private RadioButton RBJenis;
    @FXML
    private RadioButton RBNama;

    private ObservableList<JenisAlat> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();
    String query, base, imported;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();
        loadData();
        setupListeners();
    }

    public static class JenisAlat {
        String idJenisAlat, namaJenisAlat, status;

        public JenisAlat(String idJenisAlat, String namaJenisAlat, String status) {
            this.idJenisAlat = idJenisAlat;
            this.namaJenisAlat = namaJenisAlat;
            this.status = status;
        }

        public String getIdJenisAlat() { return idJenisAlat; }
        public String getNamaJenisAlat() { return namaJenisAlat; }
        public String getStatus() { return status; }
    }

    private void initializeTableColumns() {
        colIdJenisAlat.setCellValueFactory(new PropertyValueFactory<>("idJenisAlat"));
        colNamaJenisAlat.setCellValueFactory(new PropertyValueFactory<>("namaJenisAlat"));

    }

    private void setupListeners() {
        TBVJenisAlat.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                JenisAlat selectedJenisAlat = TBVJenisAlat.getSelectionModel().getSelectedItem();
                if (selectedJenisAlat != null) {
                    txtJenisAlat.setText(selectedJenisAlat.getIdJenisAlat());
                    txtNamaJenis.setText(selectedJenisAlat.getNamaJenisAlat());
                }
            }
        });

        txtCariID.setOnKeyTyped(event -> {
            RBJenis.setSelected(true);
            RBNama.setSelected(false);
            searchById();
        });

        txtCariNama.setOnKeyTyped(event -> {
            RBJenis.setSelected(false);
            RBNama.setSelected(true);
            searchByName();
        });
    }



    @FXML
    private void onBtnUpdateClick() {
        String idJenisAlat = txtJenisAlat.getText();
        String namaJenisAlat = txtNamaJenis.getText();

        String query = "EXEC sp_UpdateJenisAlat ?,?,? ";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idJenisAlat);
            preparedStatement.setString(2, namaJenisAlat);
            preparedStatement.setString(3, "Aktif"); // Status defaults to "Aktif" on update
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Update data jenis alat berhasil");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat update jenis alat: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnDeleteClick(ActionEvent event) {
        String idJenisAlat = txtJenisAlat.getText();

        String query = "EXEC sp_DeleteJenisAlat ?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idJenisAlat);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Jenis Alat berhasil dinonaktifkan");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat menonaktifkan jenis alat: " + e.getMessage());
        }
    }

    @FXML
    private void onBtnBatalClick(ActionEvent event) {
        txtJenisAlat.clear();
        txtNamaJenis.clear();
    }

    private void loadData() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Jenis_Alat";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("Status").equalsIgnoreCase("aktif")) {
                    oblist.add(new JenisAlat(rs.getString("idJnsAlat"),
                            rs.getString("namaJenisAlat"),
                            rs.getString("Status")));
                }
            }
            TBVJenisAlat.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat memuat data jenis alat: " + e.getMessage());
        }
    }

    private void searchById() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Jenis_Alat WHERE idJnsAlat LIKE '%" + txtCariID.getText() + "%'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new JenisAlat(rs.getString("idJnsAlat"),
                        rs.getString("namaJenisAlat"),
                        rs.getString("Status")));
            }
            TBVJenisAlat.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari jenis alat: " + e.getMessage());
        }
    }

    private void searchByName() {
        oblist.clear();
        try {
            String query = "SELECT * FROM Jenis_Alat WHERE namaJenisAlat LIKE '%" + txtCariNama.getText() + "%'";
            ResultSet rs = connection.stat.executeQuery(query);
            while (rs.next()) {
                oblist.add(new JenisAlat(rs.getString("idJnsAlat"),
                        rs.getString("namaJenisAlat"),
                        rs.getString("Status")));
            }
            TBVJenisAlat.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi error saat mencari jenis alat: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
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
                for (int i = 3; i < imported.length(); i++) {
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
            Parent UpdateJenisAlatRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(UpdateJenisAlatRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
