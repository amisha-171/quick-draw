package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;

public class ProfileController {
  private @FXML Label userLabel;
  private @FXML Button nextUser;
  private @FXML Button prevUser;
  private @FXML ImageView userImage;
  private @FXML ImageView speakerIcon;
  private int userIndex = 0;
  private User[] users;

  /**
   * Initialises the first users profile information to the scene when it is loaded by accessing
   * setUserInfoToGui() method
   *
   * @throws IOException If accessing any of the profile related files causes an error.
   */
  public void initialize() throws IOException {
    users = Database.getAllUsers();
    if (users.length != 0) {
      setUserInfoToGui();
    }
  }

  /**
   * This method toggles between each user shown on the screen if there is more than 1 user, and
   * displays the appropriate username and profile picture.
   *
   * @param event The (button) event which invokes this method.
   * @throws IOException If accessing any of the profile related files causes an error.
   */
  @FXML
  private void onToggleUsers(ActionEvent event) throws IOException {
    // Get the source button clicked by the user
    Button sourceButton = (Button) event.getSource();
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

  /**
   * This method updates the profile picture and username of the current selected user, sending this
   * information to be displayed on the GUI.
   *
   * @throws IOException if there is an issue accessing any particular user files.
   */
  protected void setUserInfoToGui() throws IOException {
    users = Database.getAllUsers();
    // Helper method to set user info the scene depending on the current user index handled by
    // onToggleUsers
    userImage.setImage(
        users[userIndex].getUserImage()); // Set the current users corresponding image
    userLabel.setText(users[userIndex].getUserName()); // Set current users username

    // Handle button visibility based on the current profile
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

  /**
   * This method allows the user to successfully log into the game upon pressing the login button.
   * This will in turn switch to the user profile menu, and update all user information within that
   * scene.
   *
   * @param event The (button) event which invokes this method.
   * @throws IOException if there is an issue accessing any particular user files.
   */
  @FXML
  private void onLogin(ActionEvent event) throws IOException {
    User[] allUsers = Database.getAllUsers();
    if (Database.userExists(allUsers[userIndex].getUserName(), true)) {
      // Create MenuController instance and set the current users name in that scene
      MenuController menucontroller =
          (MenuController) SceneManager.getUiController(SceneManager.AppUi.USER_MENU);
      // Set the required user details to the new scene we are about to load
      menucontroller.setName(allUsers[userIndex].getUserName());
      // Set the current users corresponding image
      menucontroller.setUserDetails(allUsers[userIndex].getUserImage());
      // Create the new scene and switch out the root to that of the new scene we want to load
      Scene scene = ((Node) event.getSource()).getScene();
      scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
    }
  }

  /**
   * This method switches back to the main menu page of the game via a button click.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onMainMenuSwitch(ActionEvent event) throws IOException {
    // setting appropriate functionality based on new number of users going back to main menu
    MainMenuController mainMenuController =
        (MainMenuController) SceneManager.getUiController(SceneManager.AppUi.MAIN_MENU);
    mainMenuController.setUsableButtons();

    // Logic to switch back to the main menu scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU));
  }

  /**
   * This method mutes or unmutes the main background music and will also toggle the mute icon
   * symbol accordingly based on the state of the music.
   */
  @FXML
  private void onToggleMute() {
    App.changeSpeakerIcon(speakerIcon);
  }
}
