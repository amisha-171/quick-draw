package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class ZenCanvasController extends CanvasController {
  @FXML private ColorPicker colourSwitcher;
  @FXML private Button speakWord;
  private boolean timerRunning;

  /** Sets up the game by setting up the canvas and deep learning model. */
  @Override
  protected void startGame() {
    // Disable the buttons in the GUI as fit
    this.disableStartButtons();

    isContent = false;
    color = Color.WHITE;
    timerRunning = false;

    canvas.setOnMouseEntered(e -> canvas.setCursor(Cursor.HAND));

    graphic = canvas.getGraphicsContext2D();

    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
          // Set content boolean to true when user has drawn, we will use this field as a guard
          // for predictions
          isContent = true;
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          graphic.setStroke(color);
          graphic.setLineWidth(size);
          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });

    try {
      model = new DoodlePrediction();
    } catch (ModelException | IOException e) {
      e.printStackTrace();
    }
  }

  /** Method to disable the start buttons when the user had clicked 'Ready' */
  @Override
  protected void disableStartButtons() {
    // This method when called well disable or enable the required buttons on input
    onInk.setDisable(true);
    eraseBtn.setDisable(true);
    speakWord.setDisable(false);
    clearButton.setDisable(true);
    colourSwitcher.setDisable(true);
  }

  /**
   * Method run when user click "Ready" to disable buttons, set the pen colour, and run the timer.
   */
  @Override
  protected void onReady() throws MalformedURLException {
    // Start playing the song associated with zen mode when user is ready
    playGameModeMusic("src/main/resources/sounds/zen.mp3");
    songPlayer.setVolume(volumeSlider.getValue() * 0.01);
    // When player is ready we start the game by enabling canvas, starting the timer etc
    this.color = this.colourSwitcher.getValue();
    this.canvas.setDisable(false);
    this.onInk.setDisable(true);
    colourSwitcher.setDisable(false);
    this.readyButton.setDisable(true);
    this.clearButton.setDisable(false);
    this.eraseBtn.setDisable(false);
    this.timerCount.setVisible(true);
    this.runTimer();
    volumeSlider.setDisable(false);
  }

  /**
   * Runs the timer for the drawing, in zen mode the counter just counts up because there is no time
   * limit.
   */
  @Override
  protected void runTimer() {
    counter = -1;
    Timer timer = new Timer();
    timerRunning = true;
    TimerTask task =
        new TimerTask() {
          @Override
          public void run() {
            // Increment the counter each second the timer task runs
            counter++;
            // When possible we set the label to update the counter
            Platform.runLater(() -> timerCount.setText(counter + " Seconds elapsed"));
            // Here we call the predictions' method (onDraw()) only if user has actually begun
            // drawing on the canvas
            // Surrounded in try and catch for exception handling
            if (isContent) {
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

            if (!timerRunning) {
              timer.cancel();
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
  }

  /** Runs the threads for generating the predictions once the user starts drawing. */
  @Override
  protected void onDraw() {
    // Set the image to be the current snapshot which is called every second, image is final for
    // predictions
    final BufferedImage image = getCurrentSnapshot();
    // Begin the background thread so the GUI does not freeze when being used
    Task<Void> backgroundTask =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            // Create a scene builder instance which is how the predictions will be formatted
            StringBuilder sbf = new StringBuilder();
            int k = 1;
            // Loop to format the string, so it can be displayed to the label
            for (int i = 0; i < 10; i++) {
              // Append the required formatting to sbf
              // The prediction number (10) being lowest (1) being the best prediction
              sbf.append(k)
                  .append(") ")
                  .append(
                      model
                          .getPredictions(image, 10)
                          .get(i)
                          .getClassName()
                          .replace("_", " ")); // Append the predictions themselves
              k++;

              sbf.append(System.getProperty("line.separator"));

              // Check if the word given to the user to draw is within the top 3 predictions of the
              // model, if it is
              // We set the game won status to be true.
              if (wordChosen.equals(
                      model.getPredictions(image, 10).get(i).getClassName().replace("_", " "))
                  && i < user.getCurrentAccuracySetting()
                  && model.getPredictions(image, 10).get(i).getProbability() * 100
                      >= (double) user.getCurrentConfidenceSetting()) {
                enableEndButtons();
              }
            }

            // Set the predictions label in the GUI to the string builder sbf
            Platform.runLater(
                () -> {
                  predLabel.setText(sbf.toString());
                  changeProgressBarColour();
                });

            return null;
          }
        };
    Thread backgroundThread = new Thread(backgroundTask);

    backgroundThread.setDaemon(true);
    backgroundThread.start(); // Begin the thread when called
  }

  /**
   * Switches to the eraser tool and disables the pen tool when the user clicks the eraser button.
   */
  @FXML
  private void onErase() { // If the user wants to erase something we set the pen color to white
    this.color = Color.WHITE;
    eraseBtn.setDisable(true);
    onInk.setDisable(false);
  }

  /** Switches to the pen tool and disables the eraser tool when the user clicks the pen button. */
  @FXML
  private void onPen() { // If the user wants to switch back to pen we change the pen color to black
    this.color = this.colourSwitcher.getValue();
    eraseBtn.setDisable(false);
    onInk.setDisable(true);
  }

  /**
   * Changes the pen colour using the JavaFX colour picker widget, to allow user to draw with
   * different colours.
   */
  @FXML
  private void switchPenColour() {
    if (!this.eraseBtn.isDisabled()) { // only switch colour if we are currently on ink (not eraser)
      this.color = this.colourSwitcher.getValue();
    }
  }

  /**
   * Uses to text to speech model to speak the word that has been generated for the user to draw.
   */
  @FXML
  private void onSpeakWord() {
    // Create text to speech instance
    TextToSpeech speech = new TextToSpeech();
    // Create task for thread and put speak inside
    Task<Void> voiceThread =
        new Task<>() {
          @Override
          protected Void call() {
            speech.speak(wordChosen);
            return null;
          }
        };
    // Create thread for bThread and start it when this method is called
    Thread speechThread = new Thread(voiceThread);
    speechThread.setDaemon(true);
    speechThread.start();
  }

  /**
   * Method to create a new game when the user clicks "New Game". Overrides parent method because
   * this method is used to cancel the timer for zen mode
   *
   * @throws IOException If there is an error in regenerating the word
   */
  @Override
  protected void onNewGame() throws IOException {
    // change song to the background song, if we're currently on zen mode
    if (songPlayer
        != null) { // check that we're currently still on zen mode (since the song player will be
      // not null)
      songPlayer.stop();
      App.playBackgroundMusic();
    }
    // If the user wants to play a new game we clear the canvas and the user gets a new word to draw
    timerRunning = false; // cancel the timer
    onClear();
    timerCount.setVisible(false);
    readyButton.setDisable(false);
    setUserName(userName);
    startGame();
    predLabel.setText("Click the \"Start!\" button to start drawing and view the guesses.");
    timerCount.setTextFill(Color.color(0.8, 0.6, 0.06));
    // On a new game we stop the song playing if the user has pressed ready, and disable the volume
    // slider until they press ready on the new game again
    if (this.songPlayer != null) {
      this.songPlayer.stop();
    }
    volumeSlider.setDisable(true);
    volumeSlider.adjustValue(50.0);
  }
}
