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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class ProfileController {
  private @FXML PasswordField password;
  private final Database data = new Database();
  private @FXML Label userLabel;
  private @FXML Button nextUser;
  private @FXML Button prevUser;
  private @FXML ImageView userImage;
  private int userIndex = 0;
  private File[] allUserImages = new File("src/main/resources/images/profilepics").listFiles();

  private Image img;

  public void initialize() throws IOException {
    setUserInfoToGui(userIndex);
    if (data.getAllUsers().length > 1) {
      nextUser.setVisible(true);
    }
  }

  @FXML
  private void toggleThroughUsers(ActionEvent event) throws IOException {
    Button sourceButton = (Button) event.getSource();

    User[] users = data.getAllUsers();

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

    if (userIndex == users.length - 1) {
      prevUser.setVisible(true);
      nextUser.setVisible(false);
    } else if (userIndex == 0) {
      prevUser.setVisible(false);
      nextUser.setVisible(true);
    } else {
      prevUser.setVisible(true);
      nextUser.setVisible(true);
    }
  }

  private void setUserInfoToGui(int currentUserIndex) throws IOException {
    User[] users = data.getAllUsers();
    img = new Image("/images/profilepics/" + allUserImages[userIndex].getName());
    userImage.setImage(img);
    userLabel.setText(users[userIndex].getUserName());
    password.clear();
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
        menucontroller.setWordsPlayed();
        menucontroller.setUserPic(img);
        stage.setScene(scene);
        stage.show();
      } else { // If there is a mismatch we inform the user the login details are invalid
        showAlert();
      }
    } else {
      showAlert();
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
