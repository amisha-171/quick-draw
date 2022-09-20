package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nz.ac.auckland.se206.FileReader.CategorySelector;
import nz.ac.auckland.se206.Userutil.Database;
import nz.ac.auckland.se206.Userutil.User;

public class MenuController {
  private @FXML Button startGame;
  private Scene scene;
  private Stage stage;
  @FXML private Label userLabel;

  private String userName;

  @FXML private Label userStats;
  @FXML private Label userId;

  protected void getName(String userId) {
    this.userName = userId;
    if (userLabel != null) {
      userLabel.setText(this.userName);
    }
  }

  protected void setStats() throws IOException {
    Database db = new Database();
    User currentUser = db.read(userName);
    StringBuilder sb = new StringBuilder();
    sb.append("Won: ")
        .append(currentUser.getWins())
        .append(System.getProperty("line.separator"))
        .append("Lost: ")
        .append(currentUser.getLosses())
        .append(System.getProperty("line.separator"))
        .append("Fastest Time: ")
        .append(currentUser.getFastestTime());
    userStats.setText(sb.toString());
    userId.setText(userName);
  }

  @FXML
  protected void onNewGame(ActionEvent event) throws IOException, URISyntaxException, CsvException {
    // Load the fxml file for the scene which we wish to display and assign it to the parent root
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/canvas.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Create an instance of category selector
    CategorySelector categorySelector = new CategorySelector();
    // Get a random word with Easy difficulty and set the word to be displayed to the user in the
    // GUI
    String randomWord = categorySelector.getRandomDiffWord(CategorySelector.Difficulty.E);
    CanvasController canvasController = loader.getController();
    canvasController.setWord(randomWord);
    canvasController.setUserName(this.userName);
    // Disable the buttons in the GUI as fit
    canvasController.disablestartButtons(true);
    // Set the stage and show it
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void switchProfile(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }
}