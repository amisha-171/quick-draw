package nz.ac.auckland.se206.controllers;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class MenuController {
  private String userName;
  @FXML private Button iconButton;
  @FXML private Button startGame;
  @FXML private ImageView gameIcon;
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

  @FXML
  private void onGameModeToggle() {
    String gameMode = startGame.getText();

    switch (gameMode) {
      case "Start Classic Game!" -> {
        startGame.setText("Start Zen Mode Game!");
        gameIcon.setImage(new Image("/images/leaf.png"));
        startGame.getStyleClass().remove("definitions-button");
        startGame.getStyleClass().remove("classic-button");
        startGame.getStyleClass().add("zen-button");
      }
      case "Start Zen Mode Game!" -> {
        startGame.setText("Start Hidden Word Game!");
        gameIcon.setImage(new Image("/images/dictionary.png"));
        startGame.getStyleClass().remove("zen-button");
        startGame.getStyleClass().remove("classic-button");
        startGame.getStyleClass().add("definitions-button");
      }
      case "Start Hidden Word Game!" -> {
        startGame.setText("Start Classic Game!");
        gameIcon.setImage(new Image("/images/pencil.png"));
        startGame.getStyleClass().remove("zen-button");
        startGame.getStyleClass().remove("definitions-button");
        startGame.getStyleClass().add("classic-button");
      }
    }
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
  private void onSwitchStats(ActionEvent btnEvent)
      throws IOException, URISyntaxException, CsvException {
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
