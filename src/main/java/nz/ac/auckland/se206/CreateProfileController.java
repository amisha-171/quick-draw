package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class CreateProfileController {
  @FXML private TextField usernameField;
  @FXML private TextField passwordField;
  private final Database db = new Database();

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
      User newUser = new User(usernameField.getText(), passwordField.getText());
      db.write(newUser);
      // Load the fxml
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      // Set the name of the current user and the current users stats in the menu scene
      MenuController menucontroller = loader.getController();
      menucontroller.getName(usernameField.getText());
      menucontroller.setStats();
      stage.setScene(scene);
      stage.show();
    }
  }

  @FXML
  private void onMainMenuSwitch(ActionEvent btnEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainmenu.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) btnEvent.getSource()).getScene().getWindow();
    // show the scene in the GUI
    stage.setScene(scene);
    stage.show();
  }
}
