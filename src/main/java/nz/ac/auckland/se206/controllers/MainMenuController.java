package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;

public class MainMenuController {

  @FXML Button switchProfile;
  @FXML Button createProfile;

  @FXML
  private void onSwitchProfile(ActionEvent event) throws IOException {
    // Create the FXML loader with the profile scene

    if (Database.getAllUsers().length == 0) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert
          .getDialogPane()
          .getStylesheets()
          .add(getClass().getResource("/css/alert.css").toString());
      alert.getDialogPane().getStyleClass().add("dialog");
      alert.setHeaderText("No users found!");
      alert.setContentText("Please create a new profile.");
      alert.setTitle("Sorry!");
      alert.show();
      return;
    }
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_PROFILE));
  }

  @FXML
  private void onCreateProfile(ActionEvent event) throws IOException {
    if (Database.getAllUsers().length == 6) {
      Alert maxUsers = new Alert(Alert.AlertType.INFORMATION);
      maxUsers
          .getDialogPane()
          .getStylesheets()
          .add(getClass().getResource("/css/alert.css").toString());
      maxUsers.getDialogPane().getStyleClass().add("dialog");
      maxUsers.setHeaderText("Unable to Create Profile!");
      maxUsers.setContentText("A max of 6 user accounts are allowed.");
      maxUsers.setTitle("Sorry!");
      maxUsers.show();
      return;
    }

    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.CREATE_PROFILE));
  }
}
