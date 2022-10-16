package nz.ac.auckland.se206.controllers;

import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.userutils.GameSettings;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;
import nz.ac.auckland.se206.util.enums.AccuracySettings;
import nz.ac.auckland.se206.util.enums.ConfidenceSettings;
import nz.ac.auckland.se206.util.enums.TimeSettings;
import nz.ac.auckland.se206.util.enums.WordSettings;

public class GameSettingsController {
  private @FXML Label accuracyLabel;
  private @FXML Label wordLabel;
  private @FXML Label timeLabel;
  private @FXML Label confidenceLabel;
  private @FXML Label accuracyDesc;
  private @FXML Label wordsDesc;
  private @FXML Label timeDesc;
  private @FXML Label confidenceDesc;
  private @FXML Button upAccuracy;
  private @FXML Button upTime;
  private @FXML Button upConfidence;
  private @FXML Button upWords;
  @FXML private ImageView speakerIcon;
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

  /**
   * This method takes a user as input, and we initialize this class fields to the user attributes
   * to maintain state of the scene
   *
   * @param user object of type User
   */
  public void setUserSettings(User user) {
    // Initialize the users current settings
    this.currentUser = user;
    this.accuracySettings = user.getGameSettings().getAccuracy();
    this.confidenceSettings = user.getGameSettings().getConfidence();
    this.timeSettings = user.getGameSettings().getTime();
    this.wordSettings = user.getGameSettings().getWords();
  }

  /**
   * This method is used to maintain state of a scene when the game is closed and opened. We allow
   * each setting index to match the current users difficulty settings and then set each label for
   * the setting based on the current index.
   */
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
    // updating the labels desc and word colours based on difficulty.
    updateDifficultyLabel(accuracyLabel);
    updateDifficultyLabel(wordLabel);
    updateDifficultyLabel(confidenceLabel);
    updateDifficultyLabel(timeLabel);
  }

  /**
   * This method sets the difficulty label contents in the GUI itself based on the toggle of the
   * buttons.
   *
   * @param currIndex an integer representing the index in the difficulty array.
   * @param difficultyList the string array containing the different difficulties for that setting.
   * @param labelToSet the specific setting label which should be updated with a different
   *     difficulty.
   * @param toggle a boolean indicating whether the toggle up or down button was pressed.
   */
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

  /**
   * This method will update the current accuracy settings as chosen by the user and will update
   * this information visually in the GUI.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onToggleAccuracy(ActionEvent event) {
    // Get the action button
    Button accuracyToggleButton = (Button) event.getSource();
    // Set boolean based on if user would like to toggle up or down the difficulties
    boolean toggleUp = accuracyToggleButton.equals(upAccuracy);
    // Call method to set the label for setting when user attempts to toggle
    setDifficultyLabelToScene(0, accuracyList, accuracyLabel, toggleUp);
    this.accuracySettings = AccuracySettings.valueOf(accuracyList[diffIndex[0]]);
    updateDifficultyLabel(accuracyLabel);
  }

  /**
   * This method will update the settings for difficulty of words chosen to draw as chosen by the
   * user, and will update this information visually in the GUI.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onToggleWords(ActionEvent event) {
    // Get the action button
    Button wordToggleButton = (Button) event.getSource();
    // Set boolean based on if user would like to toggle up or down the difficulties
    boolean toggleUp = wordToggleButton.equals(upWords);
    // Call method to set the label for setting when user attempts to toggle
    setDifficultyLabelToScene(1, wordList, wordLabel, toggleUp);
    this.wordSettings = WordSettings.valueOf(wordList[diffIndex[1]]);
    updateDifficultyLabel(wordLabel);
  }

  /**
   * This method will update the settings for maximum game time as chosen by the user, and will
   * update this information visually in the GUI.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onToggleTime(ActionEvent event) {
    // Get the action button
    Button timeToggleButton = (Button) event.getSource();
    // Set boolean based on if user would like to toggle up or down the difficulties
    boolean toggleUp = timeToggleButton.equals(upTime);
    // Call method to set the label for setting when user attempts to toggle
    setDifficultyLabelToScene(2, timeList, timeLabel, toggleUp);
    this.timeSettings = TimeSettings.valueOf(timeList[diffIndex[2]]);
    updateDifficultyLabel(timeLabel);
  }

  /**
   * This method will update the settings for the machine learning model's confidence in their
   * predictions, as chosen by the user, and will update this information visually in the GUI.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onToggleConfidence(ActionEvent event) {
    // Get the action button
    Button confidenceToggleButton = (Button) event.getSource();
    // Set boolean based on if user would like to toggle up or down the difficulties
    boolean toggleUp = confidenceToggleButton.equals(upConfidence);
    // Call method to set the label for setting when user attempts to toggle
    setDifficultyLabelToScene(3, confidenceList, confidenceLabel, toggleUp);
    this.confidenceSettings = ConfidenceSettings.valueOf(confidenceList[diffIndex[3]]);
    updateDifficultyLabel(confidenceLabel);
  }

  /**
   * This method will update the settings for the user specifically by saving them to the User
   * object itself. This will allow it to be accessed to invoke the right settings when the user
   * plays the game.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onConfirmSettings(ActionEvent event) {
    // Update current users game settings to the updated settings.
    GameSettings gameSettings =
        new GameSettings(
            this.accuracySettings, this.timeSettings, this.confidenceSettings, this.wordSettings);
    // Set the new game settings in the user profile class
    currentUser.setGameSettings(gameSettings);
    // Write information to user file
    currentUser.saveSelf();
    // Set scene
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
  }

  /**
   * This method will update the difficulty displayed depending on what label is passed as input. It
   * will recolour the label text and change the corresponding tool tip associated with that
   * setting, so the user understands what it means to them.
   *
   * @param label The difficulty label which needs to be updated.
   */
  private void updateDifficultyLabel(Label label) {
    String difficulty = label.getText();

    // Switch statement to handle the label color based on difficulty setting.
    switch (difficulty) {
      case "EASY" -> label.setTextFill(Color.web("#41b208"));
      case "MEDIUM" -> label.setTextFill(Color.web("#e1ad01"));
      case "HARD" -> label.setTextFill(Color.web("#ea7c00"));
      case "MASTER" -> label.setTextFill(Color.web("#b70000"));
    }

    if (label.equals(accuracyLabel)) {
      // Logic to set description text based on difficulty for accuracy
      int topCount = 0;
      switch (difficulty) {
        case "EASY" -> topCount = 3;
        case "MEDIUM" -> topCount = 2;
        case "HARD" -> topCount = 1;
      }
      if (!difficulty.equals("HARD")) {
        accuracyDesc.setText(
            "You can win if the word to draw is in the Top " + topCount + " Guesses!");
      } else {
        accuracyDesc.setText("You can win if the word to draw is the Top Guess!");
      }

    } else if (label.equals(wordLabel)) {
      // Logic to set text based on difficulty for word setting
      String wordDifficulties = null;
      switch (difficulty) {
        case "EASY" -> wordDifficulties = "Easy";
        case "MEDIUM" -> wordDifficulties = "Easy and Medium";
        case "HARD" -> wordDifficulties = "Easy, Medium and Hard";
        case "MASTER" -> wordDifficulties = "Hard";
      }
      wordsDesc.setText("Only " + wordDifficulties + " words are chosen to draw!");

    } else if (label.equals(timeLabel)) {
      // Logic to set desc text based on difficulty for time setting
      int timeLimit = 0;
      switch (difficulty) {
        case "EASY" -> timeLimit = 60;
        case "MEDIUM" -> timeLimit = 45;
        case "HARD" -> timeLimit = 30;
        case "MASTER" -> timeLimit = 15;
      }
      timeDesc.setText("You have a " + timeLimit + " second time limit to draw!");

    } else {
      // Logic to set description text based on difficulty for confidence setting
      String confidence = null;
      switch (difficulty) {
        case "EASY" -> confidence = "not very";
        case "MEDIUM" -> confidence = "fairly";
        case "HARD" -> confidence = "";
        case "MASTER" -> confidence = "super";
      }
      confidenceDesc.setText("We'll make guesses we are " + confidence + " confident about!");
    }
  }

  /**
   * This method mutes or unmutes the main background music and will also toggle the mute icon
   * symbol accordingly based on the state of the music.
   */
  @FXML
  private void onToggleMute() {
    App.changeSpeakerIcon(speakerIcon);
  }
}
