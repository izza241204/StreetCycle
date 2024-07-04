module com.example.streetcycle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    exports com.example.streetcycle;
    exports com.example.streetcycle.TampilanAwal;
    exports com.example.streetcycle.awal;
    exports com.example.streetcycle.Connection;
    exports com.example.streetcycle.CRUD;
    exports com.example.streetcycle.Transaksi;

    opens com.example.streetcycle.Connection to javafx.fxml;
    opens com.example.streetcycle.awal to javafx.fxml;
    opens com.example.streetcycle to javafx.fxml;
    opens com.example.streetcycle.TampilanAwal to javafx.fxml;
    opens com.example.streetcycle.CRUD to javafx.fxml;
    opens com.example.streetcycle.Transaksi to javafx.fxml;





}
