package rps.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class NameInput {
    public TextField nameInputField;
    public Label labelName;

    public void handlePlayGameBtn(ActionEvent actionEvent) throws IOException {
        if(nameInputField.getText().isEmpty()){
            labelName.setText("you must Choose a name, u stoopid..");
        }else{
            Stage s = (Stage) nameInputField.getScene().getWindow();
            s.close();

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rps/gui/view/GameView.fxml"));
            Parent root = loader.load();
            GameViewController controller = loader.getController();
            controller.setName(nameInputField.getText());
            stage.setTitle("Welcome to the not-implemented Rock-Paper-Scissor game!");
            stage.setScene(new Scene(root));
            stage.show();


        }

    }
}
