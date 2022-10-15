package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nz.ac.auckland.se206.userutils.User;

public class PopupController {
  @FXML private Button yesBtn;
  @FXML private Button noBtn;
  private User user;
  @FXML private Stage stage;
  @FXML private Stage currStage;

  public PopupController() {}

  public void currUser(User user) {
    this.user = user;
  }

  @FXML
  private void onGameSettingsChange(ActionEvent btn) throws IOException {
    currStage = (Stage) yesBtn.getScene().getWindow();
    if (btn.getSource().equals(yesBtn)) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gamesettings.fxml"));
      Parent root1 = loader.load();
      GameSettingsController controller = loader.getController();
      controller.setUserSettings(user, true);
      controller.setInitialInterface();
      stage = new Stage();
      stage.setTitle("Game Settings");
      stage.setScene(new Scene(root1));
      stage.show();
      currStage.close();
    } else {
      currStage.close();
    }
  }
}
