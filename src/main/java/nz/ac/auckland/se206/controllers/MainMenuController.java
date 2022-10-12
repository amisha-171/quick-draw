package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;

public class MainMenuController {
  private Alert alert;

  /** Initialize the alert of this scene and associate it with relevant css styling */
  public void initialize() {
    alert = new Alert(Alert.AlertType.INFORMATION);
    alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/alert.css").toString());
    alert.getDialogPane().getStyleClass().add("dialog");
    alert.setTitle("Sorry!");
    // adding custom icon to the alert window & the top of the window
    alert.setGraphic((new ImageView("/images/sad.png")));
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image("images/pencil.png"));
  }

  @FXML
  private void onSwitchProfile(ActionEvent event) throws IOException {
    if (Database.getAllUsers().length == 0) {
      // showing alert warning to user if no user profiles exist.
      alert.setHeaderText("No Users Found!");
      alert.setContentText("Please create a new profile.");
      alert.show();
      return;
    }
    // Set scene and switch the root of the scene to the new scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_PROFILE));
  }

  @FXML
  private void onCreateProfile(ActionEvent event) throws IOException {
    if (Database.getAllUsers().length == 6) {
      // showing alert warning to user if max number of user profiles exist.
      alert.setHeaderText("Unable to Create Profile!");
      alert.setContentText("A max of 6 user accounts are allowed.");
      alert.show();
      return;
    }
    // Set scene and switch scene root
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.CREATE_PROFILE));
  }
}
