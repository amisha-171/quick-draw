package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.util.SceneManager;

public class MainMenuController {

  @FXML private Button switchProfile;
  @FXML private Button createProfile;
  @FXML private Button viewLeaderboard;

  /**
   * Initialise the usable buttons of this scene depending on the number of users created
   *
   * @throws IOException if user data cannot be read in correctly.
   */
  public void initialize() throws IOException {
    setUsableButtons();
  }

  /**
   * This method initialises the accessible buttons by the user when on the main menu, if max users
   * are created you cannot create more, while if none are created, you cannot switch users or view
   * leaderboard.
   *
   * @throws IOException if user data cannot be read in correctly.
   */
  protected void setUsableButtons() throws IOException {
    if (Database.getAllUsers().length != 0) {
      switchProfile.setDisable(false);
      viewLeaderboard.setDisable(false);
    }

    if (Database.getAllUsers().length == 6) {
      createProfile.setDisable(true);
    }
  }

  /**
   * This method switches to the user profile select screen upon clicking the switch profile button.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onSwitchProfile(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_PROFILE));
  }

  /**
   * This method switches to the leaderboard screen upon clicking the corresponding button. It will
   * also call methods to update information in the leaderboard if required.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onSwitchLeaderboard(ActionEvent event) throws IOException {
    // pass in user information to the leaderboard, so it's updated
    LeaderboardController leaderboardController =
        (LeaderboardController) SceneManager.getUiController(SceneManager.AppUi.LEADERBOARD);
    leaderboardController.setLeaderboardContents();
    // switch the scene root to show the leaderboard scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.LEADERBOARD));
  }

  /**
   * This method switches back to the new user profile creation screen upon pressing the create
   * profile button.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onCreateProfile(ActionEvent event) {
    // switch the scene root to show the profile creation scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.CREATE_PROFILE));
  }
}
