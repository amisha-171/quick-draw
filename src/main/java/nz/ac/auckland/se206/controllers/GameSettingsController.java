package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.userutils.GameSettings;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.enums.AccuracySettings;
import nz.ac.auckland.se206.util.enums.ConfidenceSettings;
import nz.ac.auckland.se206.util.enums.TimeSettings;
import nz.ac.auckland.se206.util.enums.WordSettings;

public class GameSettingsController {
  private @FXML Label accuracyLabel;
  private @FXML Label wordLabel;
  private @FXML Label timeLabel;
  private @FXML Label confidenceLabel;

  private @FXML Button upAccuracy;
  private @FXML Button upTime;
  private @FXML Button upConfidence;
  private @FXML Button upWords;
  private int[] diffIndex = {0, 0, 0, 0};
  private AccuracySettings accuracySettings;
  private ConfidenceSettings confidenceSettings;
  private TimeSettings timeSettings;
  private WordSettings wordSettings;

  private final String[] accuracyList =
      Arrays.stream(AccuracySettings.values())
          .map(AccuracySettings::toString)
          .toArray(String[]::new);
  private final String[] timeList =
      Arrays.stream(TimeSettings.values()).map(TimeSettings::toString).toArray(String[]::new);
  private final String[] confidenceList =
      Arrays.stream(ConfidenceSettings.values())
          .map(ConfidenceSettings::toString)
          .toArray(String[]::new);
  private final String[] wordList =
      Arrays.stream(WordSettings.values()).map(WordSettings::toString).toArray(String[]::new);
  private User currentUser;

  public void currentUser(User user) {
    // Initialize the users current settings so settings page always maintains state
    this.currentUser = user;
    this.accuracySettings = user.getGameSettings().getAccuracy();
    this.confidenceSettings = user.getGameSettings().getConfidence();
    this.timeSettings = user.getGameSettings().getTime();
    this.wordSettings = user.getGameSettings().getWords();
  }

  public void setInitialInterface() {
    // Set the current index to match the difficulty, this is to help maintain state of this scene
    diffIndex[0] = currentUser.getGameSettings().getIndexOnLoad(accuracySettings.toString());
    diffIndex[1] = currentUser.getGameSettings().getIndexOnLoad(wordSettings.toString());
    diffIndex[2] = currentUser.getGameSettings().getIndexOnLoad(timeSettings.toString());
    diffIndex[3] = currentUser.getGameSettings().getIndexOnLoad(confidenceSettings.toString());
    // Set all the difficulty labels to their corresponding difficulty based on current index
    accuracyLabel.setText(accuracyList[diffIndex[0]]);
    wordLabel.setText(wordList[diffIndex[1]]);
    timeLabel.setText(timeList[diffIndex[2]]);
    confidenceLabel.setText(confidenceList[diffIndex[3]]);
  }

  private void setDifficultyLabelToScene(
      int currIndex, String[] difficultyList, Label labelToSet, boolean toggle) {
    // If user wants to up the difficulty we check if the current index is less than the sum of all
    // difficulties for that setting, if so we increment the index for that setting
    if (toggle) {
      if (diffIndex[currIndex] < difficultyList.length - 1) {
        diffIndex[currIndex]++;
      }
    } else {
      // If the user wants to toggle down check current index is greater than zero and decrement the
      // index for that setting
      if (diffIndex[currIndex] > 0) {
        diffIndex[currIndex]--;
      }
    }
    // Set the corresponding difficulty label for the setting based on the incremented/decremented
    // index
    labelToSet.setText(difficultyList[diffIndex[currIndex]]);
  }

  @FXML
  private void onAccuracy(ActionEvent a) {
    // Get the action button
    Button accuracyToggleButton = (Button) a.getSource();
    // Set boolean based on if user would like to toggle up or down the difficulties
    boolean toggleUp = accuracyToggleButton.equals(upAccuracy);
    // Call method to set the label for setting when user attempts to toggle
    setDifficultyLabelToScene(0, accuracyList, accuracyLabel, toggleUp);
    this.accuracySettings = AccuracySettings.valueOf(accuracyList[diffIndex[0]]);
  }

  @FXML
  private void onWords(ActionEvent w) {
    Button wordToggleButton = (Button) w.getSource();
    boolean toggleUp = wordToggleButton.equals(upWords);
    setDifficultyLabelToScene(1, wordList, wordLabel, toggleUp);
    this.wordSettings = WordSettings.valueOf(wordList[diffIndex[1]]);
  }

  @FXML
  private void onTime(ActionEvent t) {
    Button timeToggleButton = (Button) t.getSource();
    boolean toggleUp = timeToggleButton.equals(upTime);
    setDifficultyLabelToScene(2, timeList, timeLabel, toggleUp);
    this.timeSettings = TimeSettings.valueOf(timeList[diffIndex[2]]);
  }

  @FXML
  private void onConfidence(ActionEvent c) {
    Button confidenceToggleButton = (Button) c.getSource();
    boolean toggleUp = confidenceToggleButton.equals(upConfidence);
    setDifficultyLabelToScene(3, confidenceList, confidenceLabel, toggleUp);
    this.confidenceSettings = ConfidenceSettings.valueOf(confidenceList[diffIndex[3]]);
  }

  @FXML
  private void onConfirmSettings(ActionEvent e) throws IOException {
    // Update current users game settings to the updated settings.
    GameSettings gameSettings =
        new GameSettings(
            this.accuracySettings, this.timeSettings, this.confidenceSettings, this.wordSettings);
    // Set the new game settings in the user profile class
    currentUser.setGameSettings(gameSettings);
    // Write information to user file
    currentUser.saveSelf();
    // Set scene
    Scene scene = ((Node) e.getSource()).getScene();
    // Set the scene and show the stage
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
  }
}
