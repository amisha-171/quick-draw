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
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

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

  @FXML
  protected void onNewNormalGame(ActionEvent event) throws IOException {
    // set the username in the canvas controller
    NormalCanvasController canvasController =
        (NormalCanvasController) SceneManager.getUiController(SceneManager.AppUi.NORMAL_CANVAS);
    canvasController.setUserName(this.userName);

    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.NORMAL_CANVAS));
  }

  @FXML
  protected void onNewHiddenGame(ActionEvent event) throws IOException, WordNotFoundException {
    HiddenCanvasController canvasController =
            (HiddenCanvasController) SceneManager.getUiController(SceneManager.AppUi.HIDDEN_CANVAS);
    canvasController.setUserName(this.userName);

    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.HIDDEN_CANVAS));
  }

  @FXML
  protected void onNewZenGame(ActionEvent event) throws IOException {
    ZenCanvasController canvasController =
            (ZenCanvasController) SceneManager.getUiController(SceneManager.AppUi.ZEN_CANVAS);
    canvasController.setUserName(this.userName);

    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ZEN_CANVAS));
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
