package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class PopupController {
  @FXML private Button yesBtn;
  private User user;
  @FXML private Stage stage;
  @FXML private Stage currStage;

  /**
   * This method initializes the user for which we are loading the popup window for
   *
   * @param user Object of type User
   */
  public void currUser(User user) {
    this.user = user;
  }

  /**
   * This method loads the popup for the game settings scene and is also in charge of resetting the
   * consecutive win statistics for a given user based on factors
   *
   * @param btn The event button (YES) or (NO)
   * @throws IOException If reading or riding causes error
   */
  @FXML
  private void onGameSettingsChange(ActionEvent btn) throws IOException {
    // Get the current stage via some node
    currStage = (Stage) yesBtn.getScene().getWindow();
    // If the yes button is clicked user would like to change game settings so we load the game
    // settings window
    if (btn.getSource().equals(yesBtn)) {
      // Reset the tracking for the consecutive wins if the user wants to change settings
      user.setConsecutiveWinsUnderTwentySeconds(21);
      user.setConsecutiveWinsUnderTenSeconds(11);
      Database.write(user);
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gamesettings.fxml"));
      Parent root1 = loader.load();
      // Obtain instance of the GameSettingsController via FXML loader
      GameSettingsController controller = loader.getController();
      // Set the current user details in that scene
      controller.setUserSettings(user, true);
      // Set the state of the scene
      controller.setInitialInterface();
      // Create scene and set the scene
      stage = new Stage();
      stage.setTitle("Game Settings");
      stage.setScene(new Scene(root1));
      // Show the game settings scene and close the popup window
      stage.show();
      currStage.close();
    } else {
      // If the user does not wish to change settings upon meeting the requirement for popup then
      // reset the tracking for
      // The particular stat that caused the popup
      if (user.getConsecutiveWinsUnderTwentySeconds() >= 10) {
        user.setConsecutiveWinsUnderTwentySeconds(21);
      }
      if (user.getConsecutiveWinsUnderTenSeconds() >= 5) {
        user.setConsecutiveWinsUnderTenSeconds(11);
      }
      Database.write(user);
      // If the user pressed no then just close the popup window
      currStage.close();
    }
  }
}
