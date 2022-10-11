package nz.ac.auckland.se206.controllers;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.enums.GameMode;

public class MenuController implements Initializable {
  private String userName;
  private GameMode currentGameMode;
  @FXML private Button iconButton;
  @FXML private Button startGame;
  @FXML private ImageView gameIcon;
  @FXML private Label userId;

  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.currentGameMode = GameMode.NORMAL;
  }

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
  private void onGameModeToggleRight() {
    switch (this.currentGameMode) {
      case NORMAL -> {
        startGame.setText("Start Zen Mode Game!");
        this.currentGameMode = GameMode.ZEN;
        gameIcon.setImage(new Image("/images/leaf.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/zenmenu.css");
      }
      case ZEN -> {
        startGame.setText("Start Hidden Word Game!");
        this.currentGameMode = GameMode.HIDDEN;
        gameIcon.setImage(new Image("/images/dictionary.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/hiddenmenu.css");
      }
      case HIDDEN -> {
        startGame.setText("Start Classic Game!");
        this.currentGameMode = GameMode.NORMAL;
        gameIcon.setImage(new Image("/images/pencil.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/menu.css");
      }
    }
  }

  @FXML
  private void onGameModeToggleLeft() {
    switch (this.currentGameMode) {
      case NORMAL -> {
        startGame.setText("Start Hidden Word Game!");
        this.currentGameMode = GameMode.HIDDEN;
        gameIcon.setImage(new Image("/images/dictionary.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/hiddenmenu.css");
      }
      case ZEN -> {
        startGame.setText("Start Classic Game!");
        this.currentGameMode = GameMode.NORMAL;
        gameIcon.setImage(new Image("/images/pencil.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/menu.css");
      }
      case HIDDEN -> {
        startGame.setText("Start Zen Mode Game!");
        this.currentGameMode = GameMode.ZEN;
        gameIcon.setImage(new Image("/images/leaf.png"));
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
        startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
        startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/zenmenu.css");
      }
    }
  }

  @FXML
  protected void onNewGame(ActionEvent event) throws IOException {
    switch (this.currentGameMode) {
      case NORMAL -> {
        // set the username in the canvas controller
        NormalCanvasController canvasController =
                (NormalCanvasController) SceneManager.getUiController(SceneManager.AppUi.NORMAL_CANVAS);
        canvasController.setUserName(this.userName);
        canvasController.onNewGame();

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.NORMAL_CANVAS));
      }
      case ZEN -> {
        ZenCanvasController canvasController =
                (ZenCanvasController) SceneManager.getUiController(SceneManager.AppUi.ZEN_CANVAS);
        canvasController.setUserName(this.userName);
        canvasController.onNewGame();

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ZEN_CANVAS));
      }
      case HIDDEN -> {
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
