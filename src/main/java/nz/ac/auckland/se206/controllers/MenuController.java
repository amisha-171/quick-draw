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
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/zenmenu.css");
      }
      case "Start Zen Mode Game!" -> {
        startGame.setText("Start Hidden Word Game!");
        gameIcon.setImage(new Image("/images/dictionary.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/hiddenmenu.css");
      }
      case "Start Hidden Word Game!" -> {
        startGame.setText("Start Classic Game!");
        gameIcon.setImage(new Image("/images/pencil.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/menu.css");
      }
    }
  }

  @FXML
  protected void onNewGame(ActionEvent event) throws IOException {
    String gameMode = startGame.getText();

    switch (gameMode) {
      case "Start Classic Game!" -> {
        // set the username in the canvas controller
        NormalCanvasController canvasController =
                (NormalCanvasController) SceneManager.getUiController(SceneManager.AppUi.NORMAL_CANVAS);
        canvasController.setUserName(this.userName);
        canvasController.onNewGame();

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.NORMAL_CANVAS));
      }
      case "Start Zen Mode Game!" -> {
        ZenCanvasController canvasController =
                (ZenCanvasController) SceneManager.getUiController(SceneManager.AppUi.ZEN_CANVAS);
        canvasController.setUserName(this.userName);
        canvasController.onNewGame();

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ZEN_CANVAS));
      }
      case "Start Hidden Word Game!" -> {
        HiddenCanvasController canvasController =
                (HiddenCanvasController) SceneManager.getUiController(SceneManager.AppUi.HIDDEN_CANVAS);
        canvasController.setUserName(this.userName);
        canvasController.onNewGame();

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.HIDDEN_CANVAS));
      }
    }
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
