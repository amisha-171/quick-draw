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
import nz.ac.auckland.se206.speech.userutils.Database;

public class MainMenuController {
  private Alert alert;

  /** Initialise the alert of this scene and associate it with relevant CSS styling */
  public void initialize() {
    this.alert = initialiseAlert();
  }

  /**
   * This method initialises the alert used for this scene, including adding CSS styling and adding
   * the correct image graphics and icons.
   *
   * @return The alert that was created and styled.
   */
  protected Alert initialiseAlert() {
    Alert basicAlert = new Alert(Alert.AlertType.INFORMATION);
    // applying the stylesheet for alerts and the style class.
    basicAlert
        .getDialogPane()
        .getStylesheets()
        .add(getClass().getResource("/css/alert.css").toString());
    basicAlert.getDialogPane().getStyleClass().add("dialog");
    basicAlert.setTitle("Sorry!");
    // adding custom icon to the alert window & the top of the window
    basicAlert.setGraphic((new ImageView("/images/sad.png")));
    Stage stage = (Stage) basicAlert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image("images/pencil.png"));
    return basicAlert;
  }

  /**
   * This method switches to the user profile select screen upon clicking the switch profile button.
   * It will alert the user if no profiles have been created yet.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onSwitchProfile(ActionEvent event) throws IOException {
    if (Database.getAllUsers().length == 0) {
      // showing alert warning to user if no user profiles exist.
      alert.setHeaderText("No Users Found!");
      alert.setContentText("Please create a new profile.");
      alert.show();
      return;
    }
    // switch the scene root to show the select profile scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_PROFILE));
  }

  /**
   * This method switches back to the new user profile creation screen upon pressing the create
   * profile button. It will show an alert if the max number of user profiles (6) have been created.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onCreateProfile(ActionEvent event) throws IOException {
    if (Database.getAllUsers().length == 6) {
      // showing alert warning to user if max number of user profiles exist.
      alert.setHeaderText("Unable to Create Profile!");
      alert.setContentText("A max of 6 user accounts are allowed.");
      alert.show();
      return;
    }
    // switch the scene root to show the profile creation scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.CREATE_PROFILE));
  }
}
