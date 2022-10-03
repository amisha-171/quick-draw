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
import nz.ac.auckland.se206.userutils.User;

public class CreateProfileController {
  @FXML private TextField usernameField;
  @FXML private TextField passwordField;

  @FXML private ImageView profPic;
  private final Database db = new Database();
  private int index;

  private Image img;

  private File[] allUserImages = new File("src/main/resources/images/profilepics").listFiles();

  public void initialize() throws IOException {
    index = db.getAllUsers().length;
    img = new Image("/images/profilepics/" + allUserImages[index].getName());
    profPic.setImage((img));
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
    // Check if the user tries to create a profile with either the password field or the username
    // field being empty
    if (passwordField.getText().isBlank() || usernameField.getText().isBlank()) {
      setAlert("Username or Password must not be empty", "Empty field");
    }

    // Check if the username field contains a space as we do not allow this to be a valid char in
    // username
    else if (usernameField.getText().contains(" ")) {
      setAlert("Username must not contain any spaces", "Invalid username format");
    }

    // Check if the username the user entered already exists, as we do not allow duplicate profiles
    // we inform the user
    else if (db.userExists(usernameField.getText(), false)) {
      setAlert("A profile with this username already exists", "User already exists");
    }

    // If criteria is met then we use our Database class to write the user to system

    else {
      String imgName = allUserImages[index].getName();
      User newUser = new User(usernameField.getText(), passwordField.getText(), imgName);
      db.write(newUser);

      // Set the name of the current user and the current users stats in the menu scene
      MenuController menuController =
          (MenuController) SceneManager.getUiController(SceneManager.AppUi.USER_MENU);
      menuController.getName(usernameField.getText());
      menuController.setStats();
      menuController.setWordsPlayed();
      menuController.setUserPic(img);

      Scene scene = ((Node) event.getSource()).getScene();
      scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
      ProfileController profileController =
          (ProfileController) SceneManager.getUiController(SceneManager.AppUi.SELECT_PROFILE);
      profileController.setUserInfoToGui();
      profileController.initialView();
      usernameField.clear();
      passwordField.clear();

      if (index < 5) {
        index++;
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
