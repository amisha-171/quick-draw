package nz.ac.auckland.se206.controllers;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.dict.DictionaryLookup;
import nz.ac.auckland.se206.dict.WordEntry;
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.filereader.CategorySelector;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class HiddenCanvasController extends CanvasController {
  private String wordDefinition;
  private int numCharactersShown;
  @FXML private Label definitionLabel;
  @FXML private Button hintButton;

  /**
   * Generates a word using the CategorySelector and also its associated definition using the
   * dictionary API.
   *
   * @throws IOException If there is an error in using the CategorySelector
   */
  @Override
  protected void generateWord() throws IOException {
    // Create an instance of category selector
    CategorySelector categorySelector = null;
    String randomWord = "";
    boolean wordDefined;
    try {
      categorySelector = new CategorySelector();
    } catch (IOException | URISyntaxException | CsvException e) {
      e.printStackTrace();
    }
    // Get a random word with user's preferred difficulty, and fetch definition until defined word
    // is chosen
    wordDefined = false;
    while (!wordDefined) {
      try {
        randomWord =
            categorySelector.getRandomDiffWord(
                this.user.getCurrentWordSetting(), this.user.getWordList());
        // fetch definition of word from dictionary api, but only if it's a noun
        for (WordEntry currentWordEntry :
            DictionaryLookup.searchWordInfo(randomWord).getWordEntries()) {
          wordDefined = true;
          this.wordDefinition = currentWordEntry.getDefinitions().get(0);
          break;
        }
      } catch (WordNotFoundException e) {
        wordDefined = false;
      }
    }

    this.wordChosen = randomWord;
    System.out.println(this.wordChosen);
    this.numCharactersShown = 0;
    this.definitionLabel.setText(this.wordDefinition);
    this.setWord();
  }

  /** Method to disable the start buttons when the user had clicked 'Ready' */
  @Override
  protected void disableStartButtons() {
    // This method when called well disable or enable the required buttons on input
    onInk.setDisable(true);
    hintButton.setDisable(true);
    clearButton.setDisable(true);
    eraseBtn.setDisable(true);
    saveImage.setDisable(true);
    newGameBtn.setDisable(true);
    mainMenuBtn.setDisable(true);
  }

  /**
   * Method run when user click "Ready" to disable buttons, set the pen colour, and run the timer.
   */
  @Override
  protected void onReady() throws MalformedURLException {
    // Play music associated with hidden game mode when user is ready
    playGameModeMusic("src/main/resources/sounds/detective.mp3");
    // When player is ready we start the game by enabling canvas, starting the timer etc
    canvas.setDisable(false);
    this.onInk.setDisable(true);
    this.readyButton.setDisable(true);
    this.clearButton.setDisable(false);
    this.eraseBtn.setDisable(false);
    this.hintButton.setDisable(false);
    timerCount.setVisible(true);
    this.runTimer();
    volumeSlider.setDisable(false);
  }

  /**
   * Method to generate a hint by revealing one letter at a time of the hidden word in a
   * hangman-style fashion, when the user clicks the 'Hint' button.
   */
  @FXML
  private void onGiveHint() {
    this.numCharactersShown++;
    this.counter -= 10;
    this.setWord();
    // if whole word is shown, disable the hint button
    if (this.numCharactersShown == this.wordChosen.length()) {
      this.hintButton.setDisable(true);
    }
  }

  /**
   * This method sets the word, depending on how many characters have been revealed, in the label.
   */
  protected void setWord() {
    // Create String builder object
    StringBuilder wordString = new StringBuilder();

    // Loop through and append Char at the corresponding index (i)
    for (int i = 0; i < this.numCharactersShown; i++) {
      wordString.append(this.wordChosen.charAt(i));
    }

    for (int i = this.numCharactersShown; i < this.wordChosen.length(); i++) {
      wordString.append("_");
    }
    // Set the word to its label in GUI
    wordLabel.setText("Word: " + wordString);
  }

  /**
   * Runs the timer for the drawing, in zen mode the counter just counts up because there is no time
   * limit.
   */
  @Override
  protected void runTimer() {
    counter = user.getCurrentTimeSetting() + 1;
    // Runs a 60-second timer countdown when timer is called and the task runs
    Timer timer = new Timer();
    TimerTask task =
        new TimerTask() {
          @Override
          public void run() {
            // Decrement the counter each second the timer task runs
            if (!gameWon) {
              counter--;
              // When possible we set the label to update the counter
              Platform.runLater(() -> timerCount.setText(counter + " Seconds remaining"));
            }
            // Here we call the predictions' method (onDraw()) only if user has actually begun
            // drawing on the canvas
            // Surrounded in try and catch for exception handling
            if (isContent && !gameWon) {
              Platform.runLater(
                  () -> {
                    try {
                      onDraw();
                      informUserOnCurrDrawing();
                    } catch (Exception e) {
                      throw new RuntimeException(e);
                    }
                  });
            }
            // Conditionals to check if the user has won the game or time has finished etc. and if
            // met we update the status label
            if (gameWon) {
              canvas.setOnMouseDragged(e -> canvas.setCursor(Cursor.DEFAULT));
              timer.cancel();

              user.incrementWins();
              user.updateWordList(wordChosen);
              user.saveSelf();
              checkPopUp();
              Platform.runLater(
                  () -> {
                    enableEndButtons();
                    definitionLabel.setText("The word was " + wordChosen + "!");
                  });
              songPlayer.stop();
              playNotification(true);
              App.playBackgroundMusic();
            }
            if (counter <= 0) {
              // If times up cancel the timer, disable canvas and change GUI state
              canvas.setOnMouseDragged(e -> canvas.setCursor(Cursor.DEFAULT));
              timer.cancel();
              user.incrementLosses();
              user.updateWordList(wordChosen);
              user.updateTotalSolveTime(60);
              user.saveSelf();
              songPlayer.stop();
              playNotification(false);
              App.playBackgroundMusic();
              // Inform user they have lost
              Platform.runLater(
                  () -> {
                    // If user has lost inform the user, reveal the word and save the game
                    // information to user file
                    wordLabel.setText("You lost, better luck next time!");
                    timerCount.setTextFill(Color.RED);
                    timerCount.setText("0 seconds remaining");
                    definitionLabel.setText("The word was " + wordChosen + "!");
                    disableButtons();
                    enableEndButtons();
                  });
            }
            if (counter == 10) {
              // If 10 seconds remain we change the timer to color to red instead of blue
              Platform.runLater(() -> timerCount.setTextFill(Color.RED));
            }
          }
        };
    timer.scheduleAtFixedRate(task, 0, 1000);
  }

  /**
   * Enables end buttons once the user completes the game (except they're enabled from the start).
   */
  @Override
  protected void enableEndButtons() {
    // Enable the available buttons user can interact with when the game has ended
    newGameBtn.setDisable(false);
    saveImage.setDisable(false);
    mainMenuBtn.setDisable(false);
    hintButton.setDisable(true);
    volumeSlider.setDisable(true);
  }
}
