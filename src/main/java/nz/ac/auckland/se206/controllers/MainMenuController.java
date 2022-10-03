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
  private final Database data = new Database();

  @FXML
  private void onSwitchProfile(ActionEvent event) throws IOException {
    // Create the FXML loader with the profile scene

    if (data.getAllUsers().length == 0) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText("No users found, please create a profile!");
      alert.setTitle("No Users");
      alert.show();
      return;
    }
    ProfileController profileController =
        (ProfileController) SceneManager.getUiController(SceneManager.AppUi.SELECT_PROFILE);
    profileController.setUserInfoToGui();
    profileController.initialView();
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_PROFILE));
  }

  @FXML
  private void onCreateProfile(ActionEvent event) throws IOException {
    if (data.getAllUsers().length == 6) {
      Alert maxUsers = new Alert(Alert.AlertType.INFORMATION);
      maxUsers.setHeaderText("Sorry but only 6 user accounts or less are allowed");
      maxUsers.setTitle("Unable to Create Profile");
      maxUsers.show();
      return;
    }

    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.CREATE_PROFILE));
  }
}
