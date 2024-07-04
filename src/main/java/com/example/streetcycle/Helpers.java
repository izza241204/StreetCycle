package com.example.streetcycle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//class ini dibuat untuk memudahkan pindah halamam/form
public class Helpers {
    protected void changePage(ActionEvent event, String page){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();//buat stage

        //untuk memudahkan pindah page
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/example/streetcycle/TampilanAwal/"+page+".fxml"));
            System.out.println(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(new Scene(root,900,600));//atur scene
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();

    }
}
