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
import nz.ac.auckland.se206.filereader.CategorySelector;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class MenuController {
  private @FXML Button startGame;
  private Scene scene;
  private Stage stage;
  private String userName;
  @FXML private Label userStats;
  @FXML private Label wordListLabel;
  @FXML private Label userId;

  protected void getName(String userId) {
    this.userName = userId;
  }

  protected void setStats() throws IOException {
    // Create database instance and obtain the current user for which we set stats
    Database db = new Database();
    User currentUser = db.read(userName);
    // Create a stringbuilder to format the stats string
    StringBuilder sb = new StringBuilder();
    sb.append("Games Won: ") // Append wins
        .append(currentUser.getWins())
        .append(System.getProperty("line.separator"))
        .append("Games Lost: ") // Append losses
        .append(currentUser.getLosses())
        .append(System.getProperty("line.separator"))
        .append("Fastest Time: ") // Append fastest time
        .append(currentUser.getFastestTime());
    // Set the stats and the users username to their respective labels in the GUI
    userStats.setText(sb.toString());
    userId.setText(userName);
  }

  protected void setWordsPlayed() throws IOException {
    // Create database instance and obtain the current user for which we set stats
    Database db = new Database();
    User currentUser = db.read(userName);
    // Create a stringbuilder to format the stats string
    StringBuilder sb = new StringBuilder();
    sb.append("WORDS PLAYED: ") // Append wins
        .append(System.getProperty("line.separator"));

    for (String word : currentUser.getWordList()) {
      sb.append(word).append(System.getProperty("line.separator"));
    }

    // Set the stats and the users username to their respective labels in the GUI
    wordListLabel.setText(sb.toString());
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
    canvasController.disableStartButtons(true);
    // Set the stage and show it
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void onSwitchProfile(ActionEvent event) throws IOException {
    // Create the FXML loader with the profile scene
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set the scene and show the stage
    stage.setScene(scene);
    stage.show();
  }
}
