package nz.ac.auckland.se206;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateProfileController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private void makeNewProfile(ActionEvent event) throws IOException {
        JSONObject userDetails = new JSONObject();
        userDetails.put("username", usernameField.getText());
        userDetails.put("password", passwordField.getText());
        userDetails.put("wins", 0);
        userDetails.put("losses", 0);
        userDetails.put("fastest-time", 0);


        JSONArray wordsDrawn = new JSONArray();
        userDetails.put("words", wordsDrawn);
        JSONArray userObject = new JSONArray();
        userObject.put(userDetails);
        try {
            FileWriter file = new FileWriter("users/" + usernameField.getText() + ".json");
            file.write(String.valueOf(userObject.toString()));
            file.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
