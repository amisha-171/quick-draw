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
import nz.ac.auckland.se206.speech.userutils.Database;
import nz.ac.auckland.se206.speech.userutils.User;
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
    // Handle user toggling through the game modes, for each different game mode we set a different
    // style and update currentGameMode
    switch (this.currentGameMode) {
      case NORMAL -> {
        // Zen Mode styling
        startGame.setText("Start Zen Mode Game!");
        this.currentGameMode = GameMode.ZEN;
        setZenStyle();
      }
      case ZEN -> {
        // Hidden mode styling
        startGame.setText("Start Hidden Word Game!");
        this.currentGameMode = GameMode.HIDDEN;
        setHiddenStyle();
      }
      case HIDDEN -> {
        // Classic mode styling
        startGame.setText("Start Classic Game!");
        this.currentGameMode = GameMode.NORMAL;
        setClassicStyle();
      }
    }
  }

  @FXML
  private void onGameModeToggleLeft() {
    // Handling user toggling to the left
    switch (this.currentGameMode) {
        // If user toggles to NORMAL mode
      case NORMAL -> {
        startGame.setText("Start Hidden Word Game!");
        this.currentGameMode = GameMode.HIDDEN;
        setHiddenStyle();
      }
        // If user toggles to ZEN mode
      case ZEN -> {
        startGame.setText("Start Classic Game!");
        this.currentGameMode = GameMode.NORMAL;
        setClassicStyle();
      }
      case HIDDEN -> {
        startGame.setText("Start Zen Mode Game!");
        this.currentGameMode = GameMode.ZEN;
        setZenStyle();
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
        // Set scene and change root of the scene
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.NORMAL_CANVAS));
      }
      case ZEN -> {
        // If zen mode create corresponding canvasController and set username and other things
        ZenCanvasController canvasController =
            (ZenCanvasController) SceneManager.getUiController(SceneManager.AppUi.ZEN_CANVAS);
        canvasController.setUserName(this.userName);
        canvasController.onNewGame();
        // Set scene and change root of the scene
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ZEN_CANVAS));
      }
      case HIDDEN -> {
        // If hidden mode create canvasController for hidden mode, again set username.
        HiddenCanvasController canvasController =
            (HiddenCanvasController) SceneManager.getUiController(SceneManager.AppUi.HIDDEN_CANVAS);
        canvasController.setUserName(this.userName);
        canvasController.onNewGame();
        // Create scene and set root  of current scene
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.HIDDEN_CANVAS));
      }
    }
  }

  @FXML
  private void onSwitchProfile(ActionEvent event) throws IOException {
    // Create ProfileController object and set the user info to gui before we load the scene
    ProfileController profileController =
        (ProfileController) SceneManager.getUiController(SceneManager.AppUi.SELECT_PROFILE);
    profileController.setUserInfoToGui();
    // Set the scene and change the root of the scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_PROFILE));
  }

  @FXML
  private void onSwitchStats(ActionEvent btnEvent)
      throws IOException, URISyntaxException, CsvException {
    // Create statsController object and set the current name of the user along with the users stats
    StatsController statsController =
        (StatsController) SceneManager.getUiController(SceneManager.AppUi.STATS);
    statsController.setName(userName);
    statsController.setStats();
    statsController.onSetWordsPlayedList();
    // Obtain scene and change the root of the scene
    Scene scene = ((Node) btnEvent.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.STATS));
  }

  @FXML
  private void onSwitchSettings(ActionEvent event) throws IOException {
    // Create our current User object and obtain gameSettingsController
    User user = Database.read(userName);
    GameSettingsController gameSettingsController =
        (GameSettingsController) SceneManager.getUiController(SceneManager.AppUi.SELECT_SETTING);
    // Set the current user and initial interface to the scene before loading
    gameSettingsController.currentUser(user);
    gameSettingsController.setInitialInterface();
    // Create the new scene and change the root of the scene to load
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_SETTING));
  }

  private void setHiddenStyle() {
    // Styling for hidden game mode
    gameIcon.setImage(new Image("/images/dictionary.png"));
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
    startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/hiddenmenu.css");
  }

  private void setZenStyle() {
    // Styling for zen game mode
    gameIcon.setImage(new Image("/images/leaf.png"));
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
    startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/zenmenu.css");
  }

  private void setClassicStyle() {
    // Styling for classic game mode
    gameIcon.setImage(new Image("/images/pencil.png"));
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
    startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/menu.css");
  }
}
