package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import com.opencsv.exceptions.CsvException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.filereader.CategorySelector;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;

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
public class NormalCanvasController extends CanvasController {
  @FXML private Button speakWord;

  @Override
  protected void disableStartButtons() {
    // This method when called well disable or enable the required buttons on input
    onInk.setDisable(true);
    clearButton.setDisable(true);
    eraseBtn.setDisable(true);
    saveImage.setDisable(true);
    newGameBtn.setDisable(true);
    mainMenuBtn.setDisable(true);
    speakWord.setDisable(false);
  }

  @Override
  protected void onReady() {
    // When player is ready we start the game by enabling canvas, starting the timer etc
    canvas.setDisable(false);
    this.onInk.setDisable(true);
    this.readyButton.setDisable(true);
    this.clearButton.setDisable(false);
    this.eraseBtn.setDisable(false);
    timerCount.setVisible(true);
    this.runTimer();
  }

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
              enableEndButtons();
              user.incrementWins();
              user.updateWordList(wordChosen);
              user.saveSelf();
            }
            if (counter == 0) {
              // If times up cancel the timer, disable canvas and change GUI state
              canvas.setOnMouseDragged(e -> canvas.setCursor(Cursor.DEFAULT));
              timer.cancel();
              disableButtons();
              enableEndButtons();
              user.incrementLosses();
              user.updateWordList(wordChosen);
              user.updateTotalSolveTime(60);
              user.saveSelf();
              // Inform user they have lost
              Platform.runLater(
                  () -> {
                    wordLabel.setText("You lost, better luck next time!");
                  });
            }
            if (counter == 10) {
              // If 10 seconds remain we change the timer to color to red instead of blue
              Platform.runLater(() -> timerCount.setTextFill(Color.RED));
              textSpeak();
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
}
