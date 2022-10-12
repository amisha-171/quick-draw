package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class ZenCanvasController extends CanvasController {
  @FXML private ColorPicker colourSwitcher;
  @FXML private Button speakWord;

  @Override
  protected void startGame() {
    // Disable the buttons in the GUI as fit
    this.disableStartButtons();

    isContent = false;
    color = Color.WHITE;

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

  @Override
  protected void disableStartButtons() {
    // This method when called well disable or enable the required buttons on input
    onInk.setDisable(true);
    eraseBtn.setDisable(true);
    speakWord.setDisable(false);
    clearButton.setDisable(true);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  @Override
  protected void onReady() {
    // When player is ready we start the game by enabling canvas, starting the timer etc
    this.color = this.colourSwitcher.getValue();
    this.canvas.setDisable(false);
    this.onInk.setDisable(true);
    this.readyButton.setDisable(true);
    this.clearButton.setDisable(false);
    this.eraseBtn.setDisable(false);
    this.timerCount.setVisible(true);
    this.runTimer();
  }

  @Override
  protected void runTimer() {
    counter = -1;
    Timer timer = new Timer();
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
                    } catch (Exception e) {
                      throw new RuntimeException(e);
                    }
                  });
            }
          }
        };
    timer.scheduleAtFixedRate(task, 0, 1000);
  }

  @Override
  protected void enableEndButtons() {
    // Enable the available buttons user can interact with when the game has ended
    newGameBtn.setDisable(false);
    saveImage.setDisable(false);
    mainMenuBtn.setDisable(false);
    speakWord.setDisable(true);
  }

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
            Platform.runLater(() -> predLabel.setText(sbf.toString()));

            return null;
          }
        };
    Thread backgroundThread = new Thread(backgroundTask);

    backgroundThread.setDaemon(true);
    backgroundThread.start(); // Begin the thread when called
  }

  @FXML
  private void onErase() { // If the user wants to erase something we set the pen color to white
    this.color = Color.WHITE;
    eraseBtn.setDisable(true);
    onInk.setDisable(false);
  }

  @FXML
  private void onPen() { // If the user wants to switch back to pen we change the pen color to black
    this.color = this.colourSwitcher.getValue();
    eraseBtn.setDisable(false);
    onInk.setDisable(true);
  }

  @FXML
  private void switchPenColour() {
    if (!this.eraseBtn.isDisabled()) { // only switch colour if we are currently on ink (not eraser)
      this.color = this.colourSwitcher.getValue();
    }
  }

  @FXML
  private void onSpeakWord() {
    // Create text to speech instance
    TextToSpeech speech = new TextToSpeech();
    // Create task for thread and put speak inside
    new Task<>() {
      @Override
      protected Void call() {
        speech.speak(wordChosen);
        return null;
      }
    };
  }
}
