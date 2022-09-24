package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class ProfileController {
  private @FXML Button loginButton;
  private @FXML Button createProfile;
  private @FXML PasswordField password;
  private final Database data = new Database();
  private @FXML Label userLabel;
  private @FXML Button nextUser;
  private @FXML Button prevUser;
  private @FXML ImageView userImage;
  private int userIndex = -1;
  private File[] allUserImages = new File("src/main/resources/profilepics").listFiles();

  @FXML
  private void toggleThroughUsers(ActionEvent event) throws IOException {
    Button sourceButton = (Button) event.getSource();
    User[] users = data.getAllUsers();
    if (users.length == 0) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setHeaderText("No users found, please create a profile!");
      alert.setTitle("No users");
      alert.show();
      return;
    }
    password.setDisable(false);
    if (sourceButton.equals(nextUser)) {
      if (userIndex < users.length - 1) {
        userIndex++;
        setUserInfoToGui(userIndex);
      }
    }
    if (sourceButton.equals(prevUser)) {
      if (userIndex > 0) {
        userIndex--;
        setUserInfoToGui(userIndex);
      }
    }
  }

  private void setUserInfoToGui(int currentUserIndex) throws IOException {
    User[] users = data.getAllUsers();
    Image img = new Image("/profilepics/" + allUserImages[userIndex].getName());
    userImage.setImage(img);
    userLabel.setText(users[userIndex].getUserName());
    password.clear();
  }

  @FXML
  private void onCreateProfile(ActionEvent event) throws IOException {
    // If the user attempts to create a new account while 6 accounts are already active we show a
    // message as we limit maximum accounts to 6
    if (data.getAllUsers().length == 6) {
      Alert maxUsers = new Alert(Alert.AlertType.INFORMATION);
      maxUsers.setHeaderText("Sorry but only 6 user accounts or less are allowed");
      maxUsers.setTitle("Unable to create profile");
      maxUsers.show();
      return; // Return statement to prevent proceeding
    }
    // If < 6 users we show the create profile scene if the user wishes to make a new profile
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
  private void onLogin(ActionEvent event) throws IOException {
    // Create our database instance
    User[] allUsers = data.getAllUsers();
    Database db = new Database();
    if (db.userExists(allUsers[userIndex].getUserName(), true)) {
      User currentUser = allUsers[userIndex];
      // Check if the password associated user in our file is the same as what the user entered and
      // load the main menu
      if (currentUser.getPassword().equals(password.getText())) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MenuController menucontroller = loader.getController();
        menucontroller.getName(allUsers[userIndex].getUserName());
        menucontroller.setStats();
        stage.setScene(scene);
        stage.show();
      } else { // If there is a mismatch we inform the user the login details are invalid
        showAlert();
      }
    } else {
      showAlert();
    }
  }
}
