package nz.ac.auckland.se206.controllers;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.filereader.CategorySelector;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

public class StatsController {
  private String userName;
  @FXML private Label wordList;
  @FXML private Label userLabel;
  @FXML private Label gamesPlayedLabel;
  @FXML private Label wordsPlayedLabel;
  @FXML private Label fastestTimeLabel;
  @FXML private Label avgTimeLabel;
  @FXML private PieChart gamesPlayedChart;
  @FXML private ComboBox<String> wordDifficultyFilter;

  public void initialize() {
    wordDifficultyFilter.getItems().addAll("All Words", "Easy", "Medium", "Hard");
    wordDifficultyFilter.setValue("All Words");
  }

  protected void setName(String userId) {
    this.userName = userId;
    this.userLabel.setText(userId);
  }

  protected void setStats() throws IOException {
    // Create database instance and obtain the current user for which we set stats
    User currentUser = Database.read(userName);
    int numWins = currentUser.getWins();
    int numLosses = currentUser.getLosses();
    int totalGames = numLosses + numWins;
    gamesPlayedLabel.setText(totalGames + " Games Played");

    // preparing observable list object to insert into pie chart
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data(numWins + " WINS", numWins),
            new PieChart.Data(numLosses + " LOSSES", numLosses));
    gamesPlayedChart.setData(pieChartData);

    if (currentUser.getFastestTime() == 100) {
      fastestTimeLabel.setText("0.0 seconds");
    } else {
      fastestTimeLabel.setText(currentUser.getFastestTime() + ".0 seconds");
    }
    avgTimeLabel.setText(currentUser.getAverageSolveTime() + " seconds");
  }

  @FXML
  protected void setWordsPlayed() throws IOException, URISyntaxException, CsvException {
    // Create database instance and obtain the current user for which we set stats
    User currentUser = Database.read(userName);
    String difficultyFilter = wordDifficultyFilter.getValue();
    switch (difficultyFilter) {
      case "Easy" -> updateWordsPlayedLabel("E", 142, currentUser);
      case "Medium" -> updateWordsPlayedLabel("M", 130, currentUser);
      case "Hard" -> updateWordsPlayedLabel("H", 68, currentUser);
      default -> updateWordsPlayedLabel("N", 340, currentUser);
    }
  }

  @FXML
  private void onUserMenuSwitch(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
  }

  @FXML
  private void onBadgeDisplaySwitch(ActionEvent event) throws IOException {
    // Create a BadgesController class object
    BadgesController badgesController =
        (BadgesController) SceneManager.getUiController(SceneManager.AppUi.BADGES);
    // Set the badges in the class for the current user by passing in the username
    badgesController.setBadgesForUser(userName);
    // Create scene
    Scene scene = ((Node) event.getSource()).getScene();
    // Set the root to load the badges to user
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.BADGES));
  }

  private void updateWordsPlayedLabel(String difficulty, int numWords, User currentUser)
      throws IOException, URISyntaxException, CsvException {
    // Create CategorySelector instance
    new CategorySelector();
    // Create StringBuilder instance
    StringBuilder sb = new StringBuilder();
    // Loop through each word in the current users word list and build the label string
    for (String word : currentUser.getWordList()) {
      if (difficulty.equals("N") || CategorySelector.getWordDifficulty(word).equals(difficulty)) {
        sb.append(word).append(System.getProperty("line.separator"));
      }
    }
    // Switch statement to set lavel based on current difficulty of word
    switch (difficulty) {
      case "E" -> wordsPlayedLabel.setText(currentUser.getNumEasyWords() + "/" + numWords);
      case "M" -> wordsPlayedLabel.setText(currentUser.getNumMediumWords() + "/" + numWords);
      case "H" -> wordsPlayedLabel.setText(currentUser.getNumHardWords() + "/" + numWords);
      case "N" -> wordsPlayedLabel.setText(currentUser.getNumWordsPlayed() + "/" + numWords);
    }
    // Set the wordList label to our constructed StringBuilder object
    wordList.setText(sb.toString());
  }
}
