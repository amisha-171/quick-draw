package nz.ac.auckland.se206;

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
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProfileController {
    private @FXML Button loginButton;
    private @FXML TextField username;
    private @FXML Button createProfile;
    private @FXML PasswordField password;

    @FXML
    private void createProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createprofile.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid");
        alert.setHeaderText("Invalid username or password");
        alert.showAndWait();
    }
    @FXML
    private void loginButton(ActionEvent event) throws IOException {
        JSONArray currUser = null;
        try {
            Files.exists(Path.of("users/" + username.getText() + ".json"));
            String currString = new String(Files.readAllBytes(Paths.get("users/" + username.getText() + ".json")));
            currUser = new JSONArray(currString);
        } catch (Exception e) {
            showAlert();
        }

        if (Files.exists(Path.of("users/" + username.getText() + ".json"))) {
            assert currUser != null;
            if (currUser.getJSONObject(0).get("password").equals(password.getText())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                MenuController menucontroller = loader.getController();
                menucontroller.getName(username.getText());
                menucontroller.setStats();
                stage.setScene(scene);
                stage.show();
            }
            else {
                showAlert();
            }
        }

    }

}
