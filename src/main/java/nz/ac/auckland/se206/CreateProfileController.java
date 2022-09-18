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
import nz.ac.auckland.se206.Userutil.Database;
import nz.ac.auckland.se206.Userutil.User;

public class CreateProfileController {
  @FXML private TextField usernameField;
  @FXML private TextField passwordField;

  @FXML
  private void makeNewProfile(ActionEvent event) throws IOException {
    if (passwordField.getText().equals("") || usernameField.getText().equals("")) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Username or Password must not be empty");
      alert.setHeaderText("Empty field");
      alert.showAndWait();
    } else {
      Database db = new Database();
      User newUser = new User(usernameField.getText(), passwordField.getText());
      db.write(newUser);

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      MenuController menucontroller = loader.getController();
      menucontroller.getName(usernameField.getText());
      menucontroller.setStats();
      stage.setScene(scene);
      stage.show();
    }
  }
}
