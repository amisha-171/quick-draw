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
import javafx.stage.Stage;
import nz.ac.auckland.se206.FileReader.CategorySelector;

public class MenuController {
  private @FXML Button startGame;
  private Scene scene;
  private Stage stage;

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
    canvasController.disableStartButtons(true);

    // Set the stage and show it
    stage.setScene(scene);
    stage.show();
  }
}
