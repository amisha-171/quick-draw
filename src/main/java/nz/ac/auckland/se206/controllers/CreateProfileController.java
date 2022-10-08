package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.GameSettings;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.enums.AccuracySettings;
import nz.ac.auckland.se206.util.enums.ConfidenceSettings;
import nz.ac.auckland.se206.util.enums.TimeSettings;
import nz.ac.auckland.se206.util.enums.WordSettings;

public class CreateProfileController {
  @FXML private TextField usernameField;
  @FXML private ImageView profPic;
  private int index;
  private Image img;
  private File[] allUserImages = new File("src/main/resources/images/profilepics").listFiles();

  public void initialize() throws IOException {
    index = Database.getAllUsers().length;
    if (index < 6) {
      img = new Image("/images/profilepics/" + allUserImages[index].getName());
      profPic.setImage((img));
    }
  }

  private void setAlert(String title, String header) {
    // Alert method where we show user an alert based on the input title and header messages
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.showAndWait(); // Show result in GUI
  }

  @FXML
  private void onCreateProfile(ActionEvent event) throws IOException {
    // Check if the username field contains a space as we do not allow this to be a valid char in
    // username
    if (usernameField.getText().contains(" ")) {
      setAlert("Username must not contain any spaces", "Invalid username format");
    }

    // Check if the username the user entered already exists, as we do not allow duplicate profiles
    // we inform the user
    else if (Database.userExists(usernameField.getText(), false)) {
      setAlert("A profile with this username already exists", "User already exists");
    }

    // If criteria is met then we use our Database class to write the user to system

    else {
      String imgName = allUserImages[index].getName();
      User newUser = new User(usernameField.getText(), imgName);
      Database.write(newUser);

      // Set the name of the current user in the menu scene
      MenuController menuController =
          (MenuController) SceneManager.getUiController(SceneManager.AppUi.USER_MENU);
      menuController.setName(usernameField.getText());
      menuController.setUserDetails(img);

      Scene scene = ((Node) event.getSource()).getScene();
      scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));

      usernameField.clear();
      index++;
      if (index < 6) {
        img = new Image("/images/profilepics/" + allUserImages[index].getName());
        profPic.setImage((img));
      }
    }
  }

  @FXML
  private void onMainMenuSwitch(ActionEvent btnEvent) {
    Scene scene = ((Node) btnEvent.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU));
  }
}
