package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

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

  protected void setBadgesForUser(String userName) throws IOException {
    User currentUser = Database.read(userName);

    if (currentUser.fiveConsecutiveWins()) {
      bronzeWins.setVisible(true);
    }

    if (currentUser.tenConsecutiveWins()) {
      silverWins.setVisible(true);
    }

    if (currentUser.twentyConsecutiveWins()) {
      goldWins.setVisible(true);
    }

    if (currentUser.twentyFiveGamesPlayed()) {
      bronzeGames.setVisible(true);
    }

    if (currentUser.fiftyGamesPlayed()) {
      silverGames.setVisible(true);
    }

    if (currentUser.hundredGamesPlayed()) {
      goldGames.setVisible(true);
    }

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

  @FXML
  private void onStatsSwitch(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.STATS));
  }
}
