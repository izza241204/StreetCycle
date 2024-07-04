package com.example.streetcycle.CRUD;

import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardManagerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class inputKaryawanController implements Initializable {
    @FXML
    private TextField txtidKaryawan;
    @FXML
    private ComboBox<String> cbidJabatan;
    @FXML
    private TextField txtnamaKaryawan;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtnoHp;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtAlamat;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;

    private DBConnect connection = new DBConnect();
    private Map<String, String> jabatanMap = new HashMap<>();
    private static final String STATUS_ACTIVE = "Aktif";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtidKaryawan.setText(generateNextId());
        populateCbidJabatan();

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z\\s]*")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        txtnamaKaryawan.setTextFormatter(textFormatter);
    }

    @FXML
    protected void onbtnSimpanClick() {
        String idKaryawan = txtidKaryawan.getText();
        String idJabatan = jabatanMap.get(cbidJabatan.getValue());
        String namaKaryawan = txtnamaKaryawan.getText();
        String Username = txtUsername.getText();
        String Password = txtPassword.getText();
        String noHp = txtnoHp.getText();
        String Email = txtEmail.getText();
        String Alamat = txtAlamat.getText();

        if (!validateInputs(noHp, Alamat, Password, Email, idJabatan, namaKaryawan)) {
            return;
        }

        try {
            String query = "EXEC sp_InsertKaryawan ?,?,?,?,?,?,?,?,?";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.pstat.setString(1, idKaryawan);
            connection.pstat.setString(2, idJabatan);
            connection.pstat.setString(3, namaKaryawan);
            connection.pstat.setString(4, Username);
            connection.pstat.setString(5, Password);
            connection.pstat.setString(6, noHp);
            connection.pstat.setString(7, Email);
            connection.pstat.setString(8, Alamat);
            connection.pstat.setString(9, STATUS_ACTIVE);

            connection.pstat.executeUpdate();
            connection.pstat.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data Saved Successfully");
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred when inserting employee data: " + ex.getMessage());
        }
    }

    private int jabatanCount() {
        int count = 0;
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT COUNT(*) FROM Karyawan";
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

    private String generateId(int number) {
        return String.format("KRY%03d", number);
    }

    public String generateNextId() {
        int count = jabatanCount();
        return generateId(count + 1);
    }

    private void populateCbidJabatan() {
        ObservableList<String> jabatanList = FXCollections.observableArrayList();
        try {
            String query = "SELECT idJabatan, namaJabatan FROM Jabatan WHERE status = 'Active'";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                String id = connection.result.getString("idJabatan");
                String name = connection.result.getString("namaJabatan");
                jabatanMap.put(name, id);
                jabatanList.add(name);
            }
            cbidJabatan.setItems(jabatanList);
            connection.result.close();
            connection.stat.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading job data: " + e.getMessage());
        }
    }

    private boolean validateInputs(String noHp, String Alamat, String Password, String Email, String idJabatan, String namaKaryawan) {
        if (noHp.length() < 11 || noHp.length() > 13) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Max length for Phone Number is 13 characters");
            return false;
        }
        if (Alamat.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Max length for Address is 100 characters");
            return false;
        }
        if (Password.length() < 8) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Password must be at least 8 characters long");
            return false;
        }
        if (!isValidEmail(Email)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid email format");
            return false;
        }
        if (idJabatan == null || idJabatan.isEmpty() || namaKaryawan.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields must be filled");
            return false;
        }
        return true;
    }

    @FXML
    protected void onbtnBatalClick() {
        txtidKaryawan.clear();
        cbidJabatan.setValue(null);
        txtnamaKaryawan.clear();
        txtUsername.clear();
        txtPassword.clear();
        txtnoHp.clear();
        txtEmail.clear();
        txtAlamat.clear();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
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
