package nz.ac.auckland.se206;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nz.ac.auckland.se206.Userutil.Database;
import nz.ac.auckland.se206.Userutil.User;

public class ProfileController {
  private @FXML Button loginButton;
  private @FXML TextField username;
  private @FXML Button createProfile;
  private @FXML PasswordField password;

  @FXML
  private void createProfile(ActionEvent event) throws IOException {
    // Show the create profile scene if the user wishes to make a new profile
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createprofile.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  private void showAlert() {
    // If the user enters invalid login details we prompt the user
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Invalid");
    alert.setHeaderText("Invalid username or password");
    alert.showAndWait();
  }

  @FXML
  private void loginButton(ActionEvent event) throws IOException {
    // Create our database instance
    Database db = new Database();
    try {
      db.read(username.getText()); // Check if such user actually exists and prompt user if not
    } catch (Exception e) {
      showAlert();
    }
    if (Files.exists(Path.of("users/" + username.getText() + ".json"))) {
      User currentUser = db.read(username.getText());
      // Check if the password associated user in our file is the same as what the user entered and
      // load the main menu
      if (currentUser.getPassword().equals(password.getText())) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MenuController menucontroller = loader.getController();
        menucontroller.getName(username.getText());
        menucontroller.setStats();
        stage.setScene(scene);
        stage.show();
      } else { // If there is a mismatch we inform the user the login details are invalid
        showAlert();
      }
    }
  }
}
