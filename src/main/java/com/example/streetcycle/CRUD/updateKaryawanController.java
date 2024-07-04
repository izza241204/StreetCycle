package com.example.streetcycle.CRUD;


import com.example.streetcycle.Connection.DBConnect;
import com.example.streetcycle.TampilanAwal.DashboardManagerController;
import com.example.streetcycle.awal.desainController;
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
import java.util.function.UnaryOperator;

public class updateKaryawanController {
    @FXML
    private TableView<Karyawan> tableKaryawan;
    @FXML
    private TableColumn<Karyawan, String> tblidKaryawan;
    @FXML
    private TableColumn<Karyawan, String> tblidJabatan;
    @FXML
    private TableColumn<Karyawan, String> tblnamaKaryawan;
    @FXML
    private TableColumn<Karyawan, String> tblUsername;
    @FXML
    private TableColumn<Karyawan, String> tblPassword;
    @FXML
    private TableColumn<Karyawan, String> tblnoHp;
    @FXML
    private TableColumn<Karyawan, String> tblEmail;
    @FXML
    private TableColumn<Karyawan, String> tblAlamat;
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
    private TextField txtSearchidKaryawan;
    @FXML
    private TextField txtSearchnamaKaryawan;
    @FXML
    private RadioButton rbidKaryawan;
    @FXML
    private RadioButton rbnamaKaryawan;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnBatal;
    private ObservableList<Karyawan> oblist = FXCollections.observableArrayList();
    private DBConnect connection = new DBConnect();

    public class Karyawan {
        private String idKaryawan, idJabatan, namaKaryawan, Username, Password, noHp, Email, Alamat, Status;

        public Karyawan(String idKaryawan, String idJabatan, String namaKaryawan, String Username, String Password, String noHp, String Email, String Alamat) {
            this.idKaryawan = idKaryawan;
            this.idJabatan = idJabatan;
            this.namaKaryawan = namaKaryawan;
            this.Username = Username;
            this.Password = Password;
            this.noHp = noHp;
            this.Email = Email;
            this.Alamat = Alamat;
        }

        public String getIdKaryawan() {
            return idKaryawan;
        }

        public String getIdJabatan() {
            return idJabatan;
        }

        public String getNamaKaryawan() {
            return namaKaryawan;
        }

        public String getUsername() {
            return Username;
        }

        public String getPassword() {
            return Password;
        }

        public String getNoHp() {
            return noHp;
        }

        public String getEmail() {
            return Email;
        }

        public String getAlamat() {
            return Alamat;
        }
    }

    @FXML
    public void initialize() {
        initializeTableColumns();
        loadData();
        setupListeners();
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

    private void initializeTableColumns() {
        tblidKaryawan.setCellValueFactory(new PropertyValueFactory<>("idKaryawan"));
        tblidJabatan.setCellValueFactory(new PropertyValueFactory<>("idJabatan"));
        tblnamaKaryawan.setCellValueFactory(new PropertyValueFactory<>("namaKaryawan"));
        tblUsername.setCellValueFactory(new PropertyValueFactory<>("Username"));
        tblPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));
        tblnoHp.setCellValueFactory(new PropertyValueFactory<>("noHp"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tblAlamat.setCellValueFactory(new PropertyValueFactory<>("Alamat"));
    }

    private void populateCbidJabatan() {
        ObservableList<String> jabatanList = FXCollections.observableArrayList();
        try {
            String query = "SELECT idJabatan, namaJabatan FROM Jabatan WHERE status = 'Aktif'";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                String id = connection.result.getString("idJabatan");
                String name = connection.result.getString("namaJabatan");
                jabatanList.add(name);
            }
            cbidJabatan.setItems(jabatanList);
            connection.result.close();
            connection.stat.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading job data: " + e.getMessage());
        }
    }


    private void setupListeners() {
        tableKaryawan.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                Karyawan selectedKaryawan = tableKaryawan.getSelectionModel().getSelectedItem();
                if (selectedKaryawan != null) {
                    txtidKaryawan.setText(selectedKaryawan.getIdKaryawan());
                    cbidJabatan.setValue(selectedKaryawan.getIdJabatan());
                    txtnamaKaryawan.setText(selectedKaryawan.getNamaKaryawan());
                    txtUsername.setText(selectedKaryawan.getUsername());
                    txtPassword.setText(selectedKaryawan.getPassword());
                    txtnoHp.setText(selectedKaryawan.getNoHp());
                    txtEmail.setText(selectedKaryawan.getEmail());
                    txtAlamat.setText(selectedKaryawan.getAlamat());
                }
            }
        });

        txtSearchidKaryawan.setOnKeyTyped(event -> {
            rbidKaryawan.setSelected(true);
            rbnamaKaryawan.setSelected(false);
            searchById();
        });

        txtSearchnamaKaryawan.setOnKeyTyped(event -> {
            rbidKaryawan.setSelected(false);
            rbnamaKaryawan.setSelected(true);
            searchByName();
        });
    }

    @FXML
    private void onbtnUpdateClick(ActionEvent event) {
        String idKaryawan = txtidKaryawan.getText();
        String idJabatan = cbidJabatan.getValue();
        String namaKaryawan = txtnamaKaryawan.getText();
        String Username = txtUsername.getText();
        String Password = txtPassword.getText();
        String noHp = txtnoHp.getText();
        String Email = txtEmail.getText();
        String Alamat = txtAlamat.getText();

        String query = "EXEC sp_UpdateKaryawan ?,?,?,?,?,?,?,?,?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idKaryawan);
            preparedStatement.setString(2, idJabatan);
            preparedStatement.setString(3, namaKaryawan);
            preparedStatement.setString(4, Username);
            preparedStatement.setString(5, Password);
            preparedStatement.setString(6, noHp);
            preparedStatement.setString(7, Email);
            preparedStatement.setString(8, Alamat);
            preparedStatement.setString(9, "Active");
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Update employee data successfully");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating employees" + e.getMessage());
        }
    }

    @FXML
    private void onbtnDeleteClick(ActionEvent event) {
        String idKaryawan = txtidKaryawan.getText();

        String query = "EXEC sp_DeleteKaryawan ?";
        try (PreparedStatement preparedStatement = connection.conn.prepareStatement(query)) {
            preparedStatement.setString(1, idKaryawan);
            preparedStatement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee has been successfully deactivated");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deactivating Employee" + e.getMessage());
        }
    }

    @FXML
    private void onbtnBatalClick(ActionEvent event) {
        txtidKaryawan.clear();
        txtnamaKaryawan.clear();
        txtUsername.clear();
        txtPassword.clear();
        txtnoHp.clear();
        txtEmail.clear();
        txtAlamat.clear();
    }

    private void searchById() {
        oblist.clear();
        try {
            String query = "SELECT idKaryawan, idJabatan, namaKaryawan, Username, Password, noHp, Email, Alamat FROM Karyawan WHERE idKaryawan LIKE ? AND Status = 'Aktif'";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + txtSearchidKaryawan.getText() + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                oblist.add(new Karyawan(rs.getString("idKaryawan"),
                        rs.getString("idJabatan"),
                        rs.getString("namaKaryawan"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("noHp"),
                        rs.getString("Email"),
                        rs.getString("Alamat")));
            }
            tableKaryawan.setItems(oblist);
            preparedStatement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for employee ID" + e.getMessage());
        }
    }

    private void searchByName() {
        oblist.clear();
        try {
            String query = "SELECT idKaryawan, idJabatan, namaKaryawan, Username, Password, noHp, Email, Alamat FROM Karyawan WHERE namaKaryawan LIKE ? AND Status = 'Aktif'";
            PreparedStatement preparedStatement = connection.conn.prepareStatement(query);
            preparedStatement.setString(1,"%" + txtSearchnamaKaryawan.getText() + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                oblist.add(new Karyawan(rs.getString("idKaryawan"),
                        rs.getString("idJabatan"),
                        rs.getString("namaKaryawan"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("noHp"),
                        rs.getString("Email"),
                        rs.getString("Alamat")));
            }
            tableKaryawan.setItems(oblist);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for the employee's name" + e.getMessage());
        }
    }

    private void loadData() {
        oblist.clear();
        try {
            Statement stat = connection.conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT idKaryawan, idJabatan, namaKaryawan, Username, Password, noHp, Email, Alamat FROM Karyawan WHERE Status = 'Aktif'");

            while (result.next()) {
                oblist.add(new Karyawan(
                        result.getString("idKaryawan"),
                        result.getString("idJabatan"),
                        result.getString("namaKaryawan"),
                        result.getString("Username"),
                        result.getString("Password"),
                        result.getString("noHp"),
                        result.getString("Email"),
                        result.getString("Alamat")));
            }
            tableKaryawan.setItems(oblist);
            stat.close();
            result.close();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR,"Error","An error occurred while loading employee data" + ex);
        }
    }

    @FXML
    private Button btnKeluar;
    @FXML
    public void onBtnKeluar(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the "Desain" page
            FXMLLoader loader = new FXMLLoader(DashboardManagerController.class.getResource("DashBoardManager.fxml"));
            Parent dashboardRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) btnKeluar.getScene().getWindow();

            // Create a new scene with the "Desain" page
            Scene scene = new Scene(dashboardRoot);

            // Set the new scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
