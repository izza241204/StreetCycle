package com.example.streetcycle.Transaksi;

import com.example.streetcycle.Connection.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TransaksiPenjualanController implements Initializable {
    @FXML
    private TextField txtTransaksiSprepart;

    @FXML
    private ComboBox<Karyawan> CbIdKaryawan;

    @FXML
    private ComboBox<Sparepart> CBIDSparepart;

    @FXML
    private DatePicker txtTanggal;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtTotal;

    @FXML
    private Label LabQty;

    @FXML
    private Button btnOk;

    @FXML
    private Button btnSave;

    @FXML
    private Label LabelTotal;

    @FXML
    private TableView<DetailTrsSparepart> tbvTransaksi;

    @FXML
    private TableColumn<DetailTrsSparepart, String> ColNamaSparepart;

    @FXML
    private TableColumn<DetailTrsSparepart, Double> ColQuanty;

    @FXML
    private TableColumn<DetailTrsSparepart, Double> ColTotal;

    @FXML
    private TextField txtNamaCus;

    private ObservableList<Karyawan> listkaryawan = FXCollections.observableArrayList();
    private ObservableList<Sparepart> listsparepart = FXCollections.observableArrayList();
    private ObservableList<DetailTrsSparepart> listkeranjang = FXCollections.observableArrayList();

    private DBConnect connection = new DBConnect();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadKaryawan();
        loadSparepart();

        ColNamaSparepart.setCellValueFactory(new PropertyValueFactory<>("namasparepart"));
        ColQuanty.setCellValueFactory(new PropertyValueFactory<>("jumlahquantity"));
        ColTotal.setCellValueFactory(new PropertyValueFactory<>("totalharga"));
        tbvTransaksi.setItems(listkeranjang);

        txtTransaksiSprepart.setText(generateID("Transaksi_Sparepart", "TSP", "idTrSparepart"));

        CbIdKaryawan.setCellFactory(param -> new ListCell<Karyawan>() {
            protected void updateItem(Karyawan item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNama());
                }
            }
        });
        CbIdKaryawan.setConverter(new StringConverter<Karyawan>() {
            @Override
            public String toString(Karyawan karyawan) {
                return karyawan == null ? null : karyawan.getNama();
            }

            @Override
            public Karyawan fromString(String s) {
                return null;
            }
        });

        CBIDSparepart.setConverter(new StringConverter<Sparepart>() {
            @Override
            public String toString(Sparepart sparepart) {
                return sparepart == null ? null : sparepart.getNama();
            }

            @Override
            public Sparepart fromString(String s) {
                return null;
            }
        });

        CBIDSparepart.setCellFactory(param -> new ListCell<Sparepart>() {
            protected void updateItem(Sparepart name, boolean empty) {
                super.updateItem(name, empty);
                if (name == null || empty) {
                    setText(null);
                } else {
                    setText(name.getNama());
                }
            }
        });

        // Tambahkan listener pada txtQuantity
        txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> calculateTotal());
    }

    public String generateID(String tableName, String formatID, String column) {
        String query;
        String imported;
        String base;

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

    public void OnBtnOk(ActionEvent actionEvent) {
        listkeranjang.add(new DetailTrsSparepart(
                CBIDSparepart.getSelectionModel().getSelectedItem().getId(),
                CBIDSparepart.getSelectionModel().getSelectedItem().getNama(),
                Double.parseDouble(txtQuantity.getText()),
                Double.parseDouble(txtTotal.getText())
        ));

        Double total = 0.0;
        for (DetailTrsSparepart item : listkeranjang) {
            total += item.getTotalharga();
        }
        LabelTotal.setText(total.toString());
        tbvTransaksi.refresh();
    }

    public static class Sparepart {
        private String id, nama;

        public Sparepart(String id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public String getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }
    }

    public static class Karyawan {
        private String id, nama;

        public Karyawan(String id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public String getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }
    }

    public static class DetailTrsSparepart {
        private String idsparepart, namasparepart;
        private Double jumlahquantity, totalharga;

        public DetailTrsSparepart(String idsparepart, String namasparepart, Double jumlahquantity, Double totalharga) {
            this.idsparepart = idsparepart;
            this.namasparepart = namasparepart;
            this.jumlahquantity = jumlahquantity;
            this.totalharga = totalharga;
        }

        public String getIdsparepart() {
            return idsparepart;
        }

        public String getNamasparepart() {
            return namasparepart;
        }

        public Double getJumlahquantity() {
            return jumlahquantity;
        }

        public Double getTotalharga() {
            return totalharga;
        }
    }

    private void loadKaryawan() {
        listkaryawan.clear();
        try {
            DBConnect connect = new DBConnect();
            String query = "SELECT idKaryawan, namaKaryawan FROM Karyawan";
            connect.stat = connect.conn.createStatement();
            connect.result = connect.stat.executeQuery(query);
            while (connect.result.next()) {
                listkaryawan.add(new Karyawan(connect.result.getString("idKaryawan"), connect.result.getString("namaKaryawan")));
            }
            connect.stat.close();
            connect.result.close();
            CbIdKaryawan.setItems(listkaryawan);
        } catch (SQLException ex) {
        }
    }

    private void loadSparepart() {
        listsparepart.clear();
        try {
            DBConnect connect = new DBConnect();
            String query = "SELECT idSparepart, namaSparepart FROM Sparepart";
            connect.stat = connect.conn.createStatement();
            connect.result = connect.stat.executeQuery(query);
            while (connect.result.next()) {
                listsparepart.add(new Sparepart(connect.result.getString("idSparepart"), connect.result.getString("namaSparepart")));
            }
            connect.stat.close();
            connect.result.close();
            CBIDSparepart.setItems(listsparepart);
        } catch (SQLException ex) {
        }
    }

    private double getHargaSatuan(String idSparepart) {
        double hargaSatuan = 0.0;
        try {
            DBConnect connect = new DBConnect();
            String query = "SELECT hargaSatuan FROM Sparepart WHERE idSparepart = ?";
            PreparedStatement preparedStatement = connect.conn.prepareStatement(query);
            preparedStatement.setString(1, idSparepart);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                hargaSatuan = resultSet.getDouble("hargaSatuan");
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return hargaSatuan;
    }

    private void calculateTotal() {
        try {
            double quantity = Double.parseDouble(txtQuantity.getText());
            Sparepart selectedSparepart = CBIDSparepart.getSelectionModel().getSelectedItem();

            if (selectedSparepart != null) {
                double hargaSatuan = getHargaSatuan(selectedSparepart.getId());
                double total = quantity * hargaSatuan;
                txtTotal.setText(String.valueOf(total));
            }
        } catch (NumberFormatException ex) {
            txtTotal.setText("");
        }
    }

    @FXML
    protected void onBtnAddClick() {
        String idTrSparepart = txtTransaksiSprepart.getText();
        String idKaryawan = CbIdKaryawan.getSelectionModel().getSelectedItem().getId();
        String namaCustomer =  txtNamaCus.getText();
        double grandTotal = Double.parseDouble(LabelTotal.getText());
        Date tanggalPembelian = Date.valueOf(txtTanggal.getValue());

        try {
            // Insert into Transaksi_Sparepart
            String queryTransaksi = "{CALL sp_InsertTransaksi(?, ?, ?, ?, ?)}";
            CallableStatement stmtTransaksi = connection.conn.prepareCall(queryTransaksi);
            stmtTransaksi.setString(1, idTrSparepart);
            stmtTransaksi.setString(2, idKaryawan);
            stmtTransaksi.setString(3, namaCustomer);
            stmtTransaksi.setDouble(4, grandTotal);
            stmtTransaksi.setDate(5, tanggalPembelian);
            stmtTransaksi.execute();
            stmtTransaksi.close();

            // Insert into Detail_Trs_Sparepart
            String queryDetail = "{CALL sp_InsertDetailTransaksi(?, ?, ?, ?)}";
            for (DetailTrsSparepart item : listkeranjang) {
                CallableStatement stmtDetail = connection.conn.prepareCall(queryDetail);
                stmtDetail.setString(1, idTrSparepart);
                stmtDetail.setString(2, item.getIdsparepart());
                stmtDetail.setInt(3, item.getJumlahquantity().intValue());
                stmtDetail.setDouble(4, item.getTotalharga());
                stmtDetail.execute();
                stmtDetail.close();
            }

            // Clear the form after saving
            txtTransaksiSprepart.setText(generateID("Transaksi_Sparepart", "TSP", "idTrSparepart"));
            CbIdKaryawan.getSelectionModel().clearSelection();
            CBIDSparepart.getSelectionModel().clearSelection();
            txtQuantity.clear();
            txtNamaCus.clear();
            txtTotal.clear();
            LabelTotal.setText("");
            listkeranjang.clear();
            tbvTransaksi.refresh();
            txtTanggal.setValue(null);

            // Show success message
            showAlert("Success", "Data has been saved successfully.");

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            showAlert("Error", "Failed to save data: " + ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
