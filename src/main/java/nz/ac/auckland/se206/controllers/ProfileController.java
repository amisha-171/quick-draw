package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class ProfileController {
  private @FXML Label userLabel;
  private @FXML Button nextUser;
  private @FXML Button prevUser;
  private @FXML ImageView userImage;
  private int userIndex = 0;
  private User[] users;

  /**
   * Initializes the first users profile information to the scene when it is loaded by accessing
   * setUserInfoToGui() method
   *
   * @throws IOException If accessing any of the profile related files causes an error
   */
  public void initialize() throws IOException {
    users = Database.getAllUsers();
    if (users.length != 0) {
      setUserInfoToGui();
    }
  }

  @FXML
  private void onToggleUsers(ActionEvent event) throws IOException {
    // Get the source button clicked by the user
    Button sourceButton = (Button) event.getSource();
    // Get all users
    // Go to next user if there are more by incrementing the index
    if (sourceButton.equals(nextUser)) {
      if (userIndex < users.length - 1) {
        userIndex++;
        setUserInfoToGui();
      }
    }
    // If possible decrement the index to go to the previous user
    if (sourceButton.equals(prevUser)) {
      if (userIndex > 0) {
        userIndex--;
        // Set the previous profile information to the scene
        setUserInfoToGui();
      }
    }
  }

  protected void setUserInfoToGui() throws IOException {
    users = Database.getAllUsers();
    // Helper method to set user info the scene depending on the current user index handled by
    // onToggleUsers
    Image img = new Image("/images/profilepics/" + users[userIndex].getImageName());
    userImage.setImage(img); // Set the current users corresponding image
    userLabel.setText(users[userIndex].getUserName()); // Set current users username

    // Handle button visibility based current profile
    if (users.length == 1) {
      prevUser.setVisible(false);
      nextUser.setVisible(false);
    } else if (userIndex == 0) {
      prevUser.setVisible(false);
      nextUser.setVisible(true);
    } else if (userIndex == users.length - 1) {
      prevUser.setVisible(true);
      nextUser.setVisible(false);
    } else {
      prevUser.setVisible(true);
      nextUser.setVisible(true);
    }
  }

  private void showAlert() {
    // If the user enters invalid login details we prompt the user
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Invalid");
    alert.setHeaderText("Invalid username or password");
    alert.showAndWait();
  }

  @FXML
  private void onLogin(ActionEvent event) throws IOException {
    User[] allUsers = Database.getAllUsers();
    if (Database.userExists(allUsers[userIndex].getUserName(), true)) {
      // Create MenuController instance and set the current users name in that scene
      MenuController menucontroller =
          (MenuController) SceneManager.getUiController(SceneManager.AppUi.USER_MENU);
      menucontroller.setName(allUsers[userIndex].getUserName());
      // Obtain current users corresponding image
      Image img = new Image("/images/profilepics/" + allUsers[userIndex].getImageName());
      // Set the required user details to the new scene we are about to load
      menucontroller.setUserDetails(img);
      // Create the new scene and switch out the root to that of the new scene we want to load
      Scene scene = ((Node) event.getSource()).getScene();
      scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));

    } else {
      // Otherwise show alert explaining what went wrong
      showAlert();
    }
  }

  @FXML
  private void onMainMenuSwitch(ActionEvent btnEvent) {
    // Logic to switch back to the main menu scene
    Scene scene = ((Node) btnEvent.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU));
  }
}
