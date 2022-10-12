package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.speech.userutils.Database;
import nz.ac.auckland.se206.speech.userutils.User;

public class BadgesController {
  @FXML private Label goldTime;
  @FXML private Label goldGames;
  @FXML private Label goldWins;
  @FXML private Label silverTime;
  @FXML private Label silverGames;
  @FXML private Label silverWins;
  @FXML private Label bronzeTime;
  @FXML private Label bronzeWins;
  @FXML private Label bronzeGames;

  /**
   * This method takes in a username as input and will display the badges earned for that user to
   * the GUI.
   *
   * @param userName the current user's name as a string.
   * @throws IOException if an I/O error occurs reading from the stream when reading user data from
   *     Database.
   */
  protected void setBadgesForUser(String userName) throws IOException {
    User currentUser = Database.read(userName);

    // Conditionals used to check current user's wins and display badges accordingly
    if (currentUser.fiveConsecutiveWins()) {
      bronzeWins.setVisible(true);
    }

    if (currentUser.tenConsecutiveWins()) {
      silverWins.setVisible(true);
    }

    if (currentUser.twentyConsecutiveWins()) {
      goldWins.setVisible(true);
    }

    // Conditionals used to check current user's games played and display badges accordingly
    if (currentUser.twentyFiveGamesPlayed()) {
      bronzeGames.setVisible(true);
    }

    if (currentUser.fiftyGamesPlayed()) {
      silverGames.setVisible(true);
    }

    if (currentUser.hundredGamesPlayed()) {
      goldGames.setVisible(true);
    }

    // Conditionals used to check current user's fastest time and display badges accordingly
    if (currentUser.underThirtySeconds()) {
      bronzeTime.setVisible(true);
    }

    if (currentUser.underTwentySeconds()) {
      silverTime.setVisible(true);
    }

    if (currentUser.underTenSeconds()) {
      goldTime.setVisible(true);
    }
  }

  /**
   * This method switches back to the statistics page of the chosen user via a button click.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onStatsSwitch(ActionEvent event) {
    // Obtain scene and set the statistics scene root.
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.STATS));
  }
}
