package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;

public class MenuController {
  private String userName;
  @FXML private Button iconButton;
  @FXML private Label userId;

  protected void setName(String userId) {
    this.userName = userId;
  }

  protected void setUserDetails(Image image) {
    ImageView img = new ImageView(image);
    img.setFitHeight(200);
    img.setFitWidth(200);
    iconButton.setGraphic(img);
    userId.setText(userName);
}

  protected void setStats() throws IOException {
    // Create database instance and obtain the current user for which we set stats
    User currentUser = Database.read(userName);
    // Create a stringbuilder to format the stats string
    StringBuilder sb = new StringBuilder();
    sb.append("Games Won: ") // Append wins
        .append(currentUser.getWins())
        .append(System.getProperty("line.separator"))
        .append("Games Lost: ") // Append losses
        .append(currentUser.getLosses())
        .append(System.getProperty("line.separator"))
        .append("Fastest Time: "); // Append the fastest time
    if (currentUser.getFastestTime() == 100) {
      sb.append("N/A");
    } else {
      sb.append(currentUser.getFastestTime()).append(" s");
    }
    sb.append(System.getProperty("line.separator")).append("Average Time: "); // append average time
    if (currentUser.getFastestTime() == 100) {
      sb.append("N/A");
    } else {
      sb.append(currentUser.getAverageSolveTime()).append(" s");
    }
    sb.append(System.getProperty("line.separator"))
        .append("Words Played: ")
        .append(currentUser.getNumWordsPlayed())
        .append(("/345"));

    // Set the stats and the users username to their respective labels in the GUI
    userStats.setText(sb.toString());
  }

  @FXML
  protected void onNewGame(ActionEvent event) throws IOException {
    // set the username in the canvas controller
    CanvasController canvasController =
        (CanvasController) SceneManager.getUiController(SceneManager.AppUi.CANVAS);
    canvasController.setUserName(this.userName);
    canvasController.onNewGame();

    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.CANVAS));
  }

  @FXML
  private void onSwitchProfile(ActionEvent event) throws IOException {
    ProfileController profileController =
        (ProfileController) SceneManager.getUiController(SceneManager.AppUi.SELECT_PROFILE);
    profileController.setUserInfoToGui();

    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_PROFILE));
  }

  @FXML
  private void onSwitchStats(ActionEvent btnEvent) throws IOException {
    StatsController statsController =
        (StatsController) SceneManager.getUiController(SceneManager.AppUi.STATS);
    statsController.setName(userName);
    statsController.setStats();
    statsController.setWordsPlayed();

    Scene scene = ((Node) btnEvent.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.STATS));
  }

  @FXML
  private void onSwitchSettings(ActionEvent event) throws IOException {
    User user = Database.read(userName);
    GameSettingsController gameSettingsController =
        (GameSettingsController) SceneManager.getUiController(SceneManager.AppUi.SELECT_SETTING);
    gameSettingsController.currentUser(user);
    gameSettingsController.setInitialInterface();
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_SETTING));
  }
}
