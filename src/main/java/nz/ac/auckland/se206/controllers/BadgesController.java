package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;

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
      bronzeWins.getTooltip().setText("JUNIOR ARTIST! \n For winning 5 games in a row.");
      bronzeWins.setDisable(false);
    }

    if (currentUser.tenConsecutiveWins()) {
      silverWins.getTooltip().setText("INTERMEDIATE ARTIST! \n For winning 10 games in a row.");
      silverWins.setDisable(false);
    }

    if (currentUser.twentyConsecutiveWins()) {
      goldWins.getTooltip().setText("MASTER ARTIST! \n For winning 20 games in a row.");
      goldWins.setDisable(false);
    }

    // Conditionals used to check current user's games played and display badges accordingly
    if (currentUser.twentyFiveGamesPlayed()) {
      bronzeGames.getTooltip().setText("EXPERIENCED SKETCHER! \n For playing 25 games.");
      bronzeGames.setDisable(false);
    }

    if (currentUser.fiftyGamesPlayed()) {
      silverGames.getTooltip().setText("DEDICATED SKETCHER! \n For playing 50 games.");
      silverGames.setDisable(false);
    }

    if (currentUser.hundredGamesPlayed()) {
      goldGames.getTooltip().setText("SUPERSTAR SKETCHER! \n For playing 100 games.");
      goldGames.setDisable(false);
    }

    // Conditionals used to check current user's fastest time and display badges accordingly
    if (currentUser.underThirtySeconds()) {
      bronzeTime.getTooltip().setText("QUICK DRAWER! \n For winning a game under 30 seconds.");
      bronzeTime.setDisable(false);
    }

    if (currentUser.underTwentySeconds()) {
      silverTime.getTooltip().setText("SPEEDY SKETCHER! \n For winning a game under 20 seconds.");
      silverTime.setDisable(false);
    }

    if (currentUser.underTenSeconds()) {
      goldTime
          .getTooltip()
          .setText("SUPER SPEEDY SKETCHER! \n For winning a game under 10 seconds.");
      goldTime.setDisable(false);
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
