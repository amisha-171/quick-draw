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
import nz.ac.auckland.se206.filereader.CategorySelector;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;

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

  /**
   * This method initialises helps to initialise the scene for the statistics page by setting the
   * displayed words in the word list to be all words, regardless of difficulty.
   */
  public void initialize() {
    wordDifficultyFilter.getItems().addAll("All Words", "Easy", "Medium", "Hard");
    wordDifficultyFilter.setValue("All Words");
  }

  /**
   * This method takes in the current user's name, and sets this in the title label for the scene.
   * It also allows other methods to access user information to set the rest of the statistics in
   * the GUI.
   *
   * @param userId the current user's name as a String.
   */
  protected void setName(String userId) {
    this.userName = userId;
    this.userLabel.setText(userId);
  }

  /**
   * This method sets the main user statistics (number of games won/lost), average and fastest time,
   * to the GUI.
   *
   * @throws IOException if an I/O error occurs reading from the stream when reading user data from
   *     Database.
   */
  protected void setStats() throws IOException {
    // Obtain user information from the current user
    User currentUser = Database.read(userName);
    int numWins = currentUser.getWins();
    int numLosses = currentUser.getLosses();
    int totalGames = numLosses + numWins;

    // setting total number of games played
    gamesPlayedLabel.setText(totalGames + " Games Played");

    // preparing an observable list object to insert into the wins/losses pie chart
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data(numWins + " WINS", numWins),
            new PieChart.Data(numLosses + " LOSSES", numLosses));
    gamesPlayedChart.setData(pieChartData);

    // setting the fastest and average time labels
    if (currentUser.getFastestTime() == 100) {
      fastestTimeLabel.setText("0.0 seconds");
    } else {
      fastestTimeLabel.setText(currentUser.getFastestTime() + ".0 seconds");
    }
    avgTimeLabel.setText(currentUser.getAverageSolveTime() + " seconds");
  }

  /**
   * This method sets the list of words played shown on the statistics page, depending on what
   * difficulty is selected through the difficultyFilter. It also updates the GUI to show a count of
   * these words.
   *
   * @throws IOException
   * @throws URISyntaxException
   * @throws CsvException
   */
  @FXML
  protected void onSetWordsPlayedList() throws IOException, URISyntaxException, CsvException {
    User currentUser = Database.read(userName);
    String difficultyFilter = wordDifficultyFilter.getValue();

    // Switch statement updates the word list shown based on the selected difficulty.
    // It also sets and shows the total count of words played depending on the selected difficulty.
    switch (difficultyFilter) {
      case "Easy" -> {
        updateWordsPlayedLabel("E", currentUser);
        wordsPlayedLabel.setText(currentUser.getNumEasyWords() + "/" + 142);
      }
      case "Medium" -> {
        updateWordsPlayedLabel("M", currentUser);
        wordsPlayedLabel.setText(currentUser.getNumMediumWords() + "/" + 130);
      }
      case "Hard" -> {
        updateWordsPlayedLabel("H", currentUser);
        wordsPlayedLabel.setText(currentUser.getNumHardWords() + "/" + 68);
      }
      default -> {
        updateWordsPlayedLabel("N", currentUser);
        wordsPlayedLabel.setText(currentUser.getNumWordsPlayed() + "/" + 340);
      }
    }
  }

  /**
   * This method switches back to the user menu page of the chosen user via a button click.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onUserMenuSwitch(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
  }

  /**
   * This method updates the user's badge display when they've earned new badges, before switching
   * to this page via a button click.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onBadgeDisplaySwitch(ActionEvent event) throws IOException {
    // Set the badges in the GUI display before switching scenes, by passing in the user's name
    BadgesController badgesController =
        (BadgesController) SceneManager.getUiController(SceneManager.AppUi.BADGES);
    badgesController.setBadgesForUser(userName);

    // Set the root to load the badges display page.
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.BADGES));
  }

  /**
   * This method will update the label of words played by checking which difficulty is selected and
   * formatting a string to print the user's played words of that difficulty.
   *
   * @param difficulty the chosen difficulty to filter words with.
   * @param currentUser the current user.
   */
  private void updateWordsPlayedLabel(String difficulty, User currentUser)
      throws IOException, URISyntaxException, CsvException {
    // Initialising category selector to have the chosen word be associated with a difficulty.
    new CategorySelector();

    // Build the label string using StringBuilder by appending all words the user has played in the
    // given difficulty.
    StringBuilder sb = new StringBuilder();
    for (String word : currentUser.getWordList()) {
      if (difficulty.equals("N") || CategorySelector.getWordDifficulty(word).equals(difficulty)) {
        sb.append(word).append(System.getProperty("line.separator"));
      }
    }
    // Update word list label accordingly on the statistics page itself.
    wordList.setText(sb.toString());
  }
}
