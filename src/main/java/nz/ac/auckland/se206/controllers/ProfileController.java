package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class ProfileController {
  private @FXML PasswordField password;
  private final Database data = new Database();
  private @FXML Label userLabel;
  private @FXML Button nextUser;
  private @FXML Button prevUser;
  private @FXML ImageView userImage;
  private int userIndex = 0;
  private User[] users;

  public void initialize() throws IOException {
    initialView();
  }

  protected void initialView() throws IOException {
    users = data.getAllUsers();
    if (users.length > 1) {
      nextUser.setVisible(true);
    }
  }

  @FXML
  private void onToggleUsers(ActionEvent event) throws IOException {
    // Get the source button clicked by the user
    Button sourceButton = (Button) event.getSource();
    // Get all of the users
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
    // Handle button visibility based current profile
    if (userIndex == users.length - 1) {
      prevUser.setVisible(true);
      nextUser.setVisible(false);
    } else if (userIndex == 0) {
      prevUser.setVisible(false);
      nextUser.setVisible(true);
    } else {
      prevUser.setVisible(true);
      nextUser.setVisible(true);
    }
  }

  protected void setUserInfoToGui() throws IOException {
    users = data.getAllUsers();
    // Helper method to set user info the scene depending on the current user index handled by
    // onToggleUsers
    Image img = new Image("/images/profilepics/" + users[userIndex].getImageName());
    userImage.setImage(img); // Set the current users corresponding image
    userLabel.setText(users[userIndex].getUserName()); // Set current users username
    password.clear();
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
    // Create our database instance
    User[] allUsers = data.getAllUsers();
    Database db = new Database();
    if (db.userExists(allUsers[userIndex].getUserName(), true)) {
      User currentUser = allUsers[userIndex];
      // Check if the password associated user in our file is the same as what the user entered and
      // load the main menu
      if (currentUser.getPassword().equals(password.getText())) {
        // setting user stats in the user menu before the scene switches to show it
        MenuController menucontroller =
            (MenuController) SceneManager.getUiController(SceneManager.AppUi.USER_MENU);
        menucontroller.getName(allUsers[userIndex].getUserName());
        menucontroller.setStats();
        menucontroller.setWordsPlayed();
        Image img = new Image("/images/profilepics/" + allUsers[userIndex].getImageName());
        menucontroller.setUserPic(img);

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
      } else { // If there is a mismatch we inform the user the login details are invalid
        showAlert();
      }
    } else {
      showAlert();
    }
    password.clear();
  }

  @FXML
  private void onMainMenuSwitch(ActionEvent btnEvent) {
    Scene scene = ((Node) btnEvent.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU));
  }
}
