package nz.ac.auckland.se206;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.json.JSONArray;

public class MenuController {
  private @FXML Button startGame;
  private Scene scene;
  private Stage stage;
  @FXML
  private Label userLabel;

  private String userName;

  @FXML
  private Label userStats;
  @FXML
  private Label userId;

  protected void getName(String userId) {
    this.userName = userId;
    userLabel.setText(this.userName);
  }

  protected void setStats() throws IOException {
    String currString = new String(Files.readAllBytes(Paths.get("users/" + userName + ".json")));
    JSONArray jsonObject = new JSONArray(currString);
    StringBuilder sb = new StringBuilder();
    sb.append("Won: ").append(jsonObject.getJSONObject(0).get("wins")).append(System.getProperty("line.separator")).append("Lost: ").append(jsonObject.getJSONObject(0).get("losses")).append(System.getProperty("line.separator")).append("Fastest Time: ").append(jsonObject.getJSONObject(0).get("fastest-time"));
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
    // Disable the buttons in the GUI as fit
    canvasController.disablestartButtons(true);

    // Set the stage and show it
    stage.setScene(scene);
    stage.show();
  }
}
