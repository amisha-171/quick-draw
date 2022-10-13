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
import javafx.stage.Stage;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;

public class CreateProfileController {
  @FXML private TextField usernameField;
  @FXML private ImageView profPic;
  private int index;
  private Image img;
  private Alert alert;
  private File[] allUserImages = new File("src/main/resources/images/profilepics").listFiles();

  /**
   * This method initialises the scene with the first users username and their profile picture on
   * start up.
   *
   * @throws IOException If failed in obtaining the total users or failing in getting their profile
   *     picture.
   */
  public void initialize() throws IOException {
    index = Database.getAllUsers().length;
    if (index < 6) {
      img = new Image("/images/profilepics/" + allUserImages[index].getName());
      profPic.setImage((img));
    }
    // initialising styling for the pop-up alerts
    alert = initialiseAlert();
  }

  /**
   * This method initialises the alert used for this scene, including adding CSS styling and adding
   * the correct image graphics and icons.
   *
   * @return The alert that was created and styled.
   */
  protected Alert initialiseAlert() {
    Alert basicAlert = new Alert(Alert.AlertType.INFORMATION);
    // applying the stylesheet for alerts and the style class.
    basicAlert
        .getDialogPane()
        .getStylesheets()
        .add(getClass().getResource("/css/alert.css").toString());
    basicAlert.getDialogPane().getStyleClass().add("dialog");
    basicAlert.setTitle("Sorry!");
    // adding custom icon to the alert window & the top of the window
    basicAlert.setGraphic((new ImageView("/images/sad.png")));
    Stage stage = (Stage) basicAlert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image("images/pencil.png"));
    return basicAlert;
  }

  /**
   * This method sets the text within the alert message so the user is correctly informed.
   *
   * @param content a String containing the descriptive reason behind the alert
   * @param header a String which acts as a title for the alert
   */
  private void showAlert(String content, String header) {
    alert.setTitle("Sorry!");
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait(); // Show alert result in GUI
  }

  /**
   * This method will create a new user profile and assign them a profile picture if the username is
   * valid and does not already exist. It will then automatically log the user into the game by
   * switching to the User Menu scene. It finally tidies up the scene so a new user can be created
   * next time.
   *
   * @param event The (button) event which invokes this method.
   * @throws IOException if an I/O error occurs when writing the user file.
   */
  @FXML
  private void onCreateProfile(ActionEvent event) throws IOException {
    // Check if the username field contains a space as we do not allow this to be a valid char in
    // username
    if (usernameField.getText().contains(" ")) {
      showAlert("Username must not contain any spaces", "Invalid username format");
    }

    // Check if the username the user entered already exists, as we do not allow duplicate profiles
    // we inform the user
    else if (Database.userExists(usernameField.getText(), false)) {
      showAlert("A profile with this name already exists", "User already exists");
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

      // Create the scene and change the root
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

  /**
   * This method switches back to the main menu page of the game via a button click.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onMainMenuSwitch(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU));
  }
}
