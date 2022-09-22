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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.Userutil.Database;
import nz.ac.auckland.se206.Userutil.User;

public class ProfileController {
  private @FXML Button loginButton;
  private @FXML TextField username;
  private @FXML Button createProfile;
  private @FXML PasswordField password;
  private final Database data = new Database();
  private @FXML Label userLabel;
  private @FXML Button nextUser;
  private @FXML Button prevUser;
  private @FXML ImageView userImage;
  private int index = -1;
  private File[] allUserImages = new File("src/main/resources/profilepics").listFiles();

  @FXML
  private void nextUser(ActionEvent event) throws IOException {
    Button sourceButton = (Button) event.getSource();
    User[] users = data.getAllUsers();
    if (sourceButton.equals(nextUser)) {
      if (index < users.length - 1) {
        index++;
        setUserInfo(index);
      }
    }
    if (sourceButton.equals(prevUser)) {
      if (index > 0) {
        index--;
        setUserInfo(index);
      }
    }
  }

  private void setUserInfo(int currentUserIndex) throws IOException {
    User[] users = data.getAllUsers();
    Image img = new Image("/profilepics/" + allUserImages[index].getName());
    userImage.setImage(img);
    userLabel.setText(users[index].getUserName());
  }

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
    if (db.userExists(username.getText(), true)) {
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
    } else {
      showAlert();
    }
  }
}
