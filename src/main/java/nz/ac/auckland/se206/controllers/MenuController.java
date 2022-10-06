package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class MenuController {
  private String userName;
  @FXML private ImageView userPic;
  @FXML private Label userStats;
  @FXML private Label wordList;
  @FXML private Label userId;

  protected void getName(String userId) {
    this.userName = userId;
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
        .append("Words Played (Easy): ")
        .append(currentUser.getNumWordsPlayed())
        .append(("/144"));

    // Set the stats and the users username to their respective labels in the GUI
    userStats.setText(sb.toString());
    userId.setText(userName);
  }

  protected void setWordsPlayed() throws IOException {
    // Create database instance and obtain the current user for which we set stats
    User currentUser = Database.read(userName);
    // Create a stringbuilder to format the stats string
    StringBuilder sb = new StringBuilder();
    for (String word : currentUser.getWordList()) {
      sb.append(word).append(System.getProperty("line.separator"));
    }
    // Set the stats and the users username to their respective labels in the GUI
    wordList.setText(sb.toString());
  }

  protected void setUserPic(Image image) {
    userPic.setImage(image);
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
}
