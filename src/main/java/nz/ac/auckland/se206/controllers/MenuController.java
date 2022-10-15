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
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;
import nz.ac.auckland.se206.util.enums.GameMode;

public class MenuController implements Initializable {
  private String userName;
  private GameMode currentGameMode;
  @FXML private Button iconButton;
  @FXML private Button startGame;
  @FXML private ImageView gameIcon;
  @FXML private Label userId;

  /**
   * Initialises the GUI by rendering GUI elements and setting the default value of the game mode to
   * normal/classic.
   *
   * @param url URL of GUI
   * @param resourceBundle Resource bundle of GUI
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.currentGameMode = GameMode.NORMAL;
  }

  /**
   * Used by other controllers to set the username from them.
   *
   * @param userId The username of the user
   */
  protected void setName(String userId) {
    this.userName = userId;
  }

  /**
   * Renders the profile image of the user on the menu page (which was set by default when profile
   * was created).
   *
   * @param image The profile image of the user
   */
  protected void setUserDetails(Image image) {
    ImageView img = new ImageView(image);
    img.setFitHeight(200);
    img.setFitWidth(200);
    iconButton.setGraphic(img);
    userId.setText(userName);
  }

  /**
   * Toggles the game mode to the right, allowing the user to choose between different game modes.
   */
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

  /**
   * Toggles the game mode to the left, allowing the user to choose between different game modes.
   */
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

  /**
   * Switches to the game/canvas page of the game mode requested by the user.
   *
   * @param event The object associated with button click allowing to switch windows
   * @throws IOException If there is an error in using dictionary API for hidden mode
   */
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

  /**
   * Switches to the profile selection page as requested by the user when clicking the 'Switch
   * Profile' button.
   *
   * @param event The object associated with button click allowing to switch windows
   * @throws IOException If there is an error in reading the profile image for the user
   */
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

  /**
   * Switches to the statistics page as requested by the user when clicking the 'My Statistics'
   * button.
   *
   * @param btnEvent The object associated with button click allowing to switch windows
   * @throws IOException If there is an error in reading the user json files
   * @throws URISyntaxException If there is another error in reading user data
   * @throws CsvException If there is an error in reading the words CSV
   */
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

  /**
   * Switches to the game settings page as requested by the user when clicking the 'Game Settings'
   * button.
   *
   * @param event The object associated with button click allowing to switch windows
   * @throws IOException If there is an error in reading the user json files for their current
   *     settings
   */
  @FXML
  private void onSwitchSettings(ActionEvent event) throws IOException {
    // Create our current User object and obtain gameSettingsController
    User user = Database.read(userName);
    GameSettingsController gameSettingsController =
        (GameSettingsController) SceneManager.getUiController(SceneManager.AppUi.SELECT_SETTING);
    // Set the current user and initial interface to the scene before loading
    gameSettingsController.setUserSettings(user, false);
    gameSettingsController.setInitialInterface();
    // Create the new scene and change the root of the scene to load
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.SELECT_SETTING));
  }

  /**
   * Helper method to set the styling for the UI elements of the menu when switching to hidden mode.
   */
  private void setHiddenStyle() {
    // Styling for hidden game mode
    gameIcon.setImage(new Image("/images/dictionary.png"));
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
    startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/hiddenmenu.css");
  }

  /**
   * Helper method to set the styling for the UI elements of the menu when switching to zen mode.
   */
  private void setZenStyle() {
    // Styling for zen game mode
    gameIcon.setImage(new Image("/images/leaf.png"));
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/menu.css");
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
    startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/zenmenu.css");
  }

  /**
   * Helper method to set the styling for the UI elements of the menu when switching to
   * normal/classic mode.
   */
  private void setClassicStyle() {
    // Styling for classic game mode
    gameIcon.setImage(new Image("/images/pencil.png"));
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/hiddenmenu.css");
    startGame.getScene().getRoot().getStylesheets().remove("/css/scene_css/zenmenu.css");
    startGame.getScene().getRoot().getStylesheets().add("/css/scene_css/menu.css");
  }
}
