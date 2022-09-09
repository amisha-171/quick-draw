package nz.ac.auckland.se206;

import ai.djl.ModelException;
import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

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
public class CanvasController {

  @FXML private Canvas canvas;
  @FXML private Label wordLabel;
  @FXML private Button readyButton;
  private GraphicsContext graphic;
  protected DoodlePrediction model;
  @FXML private Label predLabel;
  @FXML private Button eraseBtn;
  @FXML private Button onInk;
  @FXML private Label timerCount;
  private int counter;
  private String wordChosen;
  private boolean gameWon;
  @FXML private Button clearButton;
  protected FileChooser fileChooser;
  private Boolean isContent;
  private Color color;
  @FXML private Button newGameBtn;
  @FXML private Button saveImage;

  // mouse coordinates
  private double currentX;
  private double currentY;
  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize(boolean playerReady) throws ModelException, IOException {
    gameWon = false;
    isContent = false;
    color = Color.BLACK;
    if (playerReady) {
      // Enable buttons and disable buttons on ready
      clearButton.setDisable(false);
      disablestartButtons(false);
      readyButton.setDisable(true);
      graphic = canvas.getGraphicsContext2D();

      canvas.setOnMousePressed(
              e -> {
                currentX = e.getX();
                currentY = e.getY();
                isContent = true;
              });

      canvas.setOnMouseDragged(
              e -> {
                // Brush size (you can change this, it should not be too small or too large).
                final double size = 6;

                final double x = e.getX() - size / 2;
                final double y = e.getY() - size / 2;

                // This is the colour of the brush.
                graphic.setStroke(color);
                graphic.setLineWidth(size);

                // Create a line that goes from the point (currentX, currentY) and (x,y)
                graphic.strokeLine(currentX, currentY, x, y);

                // update the coordinates
                currentX = x;
                currentY = y;
              });
    }

    model = new DoodlePrediction();
  }
  protected void disablestartButtons(boolean btn) {
    // This method when called well disable or enable the required buttons on input
    onInk.setDisable(btn);
    clearButton.setDisable(btn);
    onInk.setDisable(btn);
    eraseBtn.setDisable(btn);
    saveImage.setDisable(true);
    newGameBtn.setDisable(true);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }
  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  public BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
    final Graphics2D graphics = imageBinary.createGraphics();
    graphics.drawImage(image, 0, 0, null);
    // To release memory we dispose.
    graphics.dispose();
    return imageBinary;
  }

  /**
   * Save the current snapshot on a bitmap file.
   *
   * @throws IOException If the image cannot be saved.
   */
  @FXML
  private void saveCurrentSnapshotOnFile() throws IOException {
    fileChooser = new FileChooser();
    fileChooser
        .getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png"));

    File file = fileChooser.showSaveDialog(new Stage());
    if (file != null) {
      ImageIO.write(getCurrentSnapshot(), "bmp", file);
    }
  }

  @FXML
  private void readyToDraw() throws ModelException, IOException {
    initialize(true);
    onInk.setDisable(true);
    runTimer();
  }

  protected void setWord(String wordDraw) {
    wordLabel.setText("Draw: " + wordDraw);
    wordChosen = wordDraw;
  }

  private void runTimer() {
    counter = 61;
    // Runs a 60 second timer countdown when timer is called and the task runs
    Timer timer = new Timer();
    TimerTask task =
        new TimerTask() {
          @Override
          public void run() {
            // Decrement the counter each second the timer task runs
            counter--;
            // When possible we set the label to update the counter
            Platform.runLater(() -> timerCount.setText(counter + " Seconds remaining"));
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
            // Conditionals to check if the user has won the game or time has finished etc and if
            // met we update the status label
            if (gameWon) {
              timer.cancel();
              enableEndButtons();
            }
            if (counter == 0) {
              timer.cancel();
              disableButtons();
              enableEndButtons();
              Platform.runLater(() -> wordLabel.setText("YOU LOST !!! TIMES UP"));
            }
            if (counter == 10) {
              Platform.runLater(() -> timerCount.setTextFill(Color.RED));
              textSpeak();
            }
          }
        };
    timer.scheduleAtFixedRate(task, 0, 1000);
  }

  private void enableEndButtons() {
    newGameBtn.setDisable(false);
    saveImage.setDisable(false);
  }

  private void textSpeak() {
    // Put the speech to text inside a thread to not freeze GUI at 10 seconds
    TextToSpeech speak = new TextToSpeech();
    Task<Void> speechTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Speak there is 10 seconds remaining
            speak.speak("10 Seconds");
            return null;
          }
        };
    // Begin the thread given the task
    Thread timeLeftThread = new Thread(speechTask);
    timeLeftThread.setDaemon(true);
    timeLeftThread.start();
  }

  private void onDraw() {
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
            // Loop to format the string so it can be displayed to the label
            for (int i = 0; i < 10; i++) {
              // Append the required formatting to sbf
              // The prediction number (10) being lowest (1) being the best prediction
              sbf.append("(")
                  .append(k)
                  .append(")")
                  .append(": ")
                  .append(
                      model
                          .getPredictions(image, 10)
                          .get(i)
                          .getClassName()); // Append the predictions themselves
              k++;

              sbf.append(System.getProperty("line.separator"));

              // Check if the word given to the user to draw is within the top 3 predictions of the
              // model, if it is
              // We set the game won status to be true.
              if (wordChosen.equals(
                      model.getPredictions(image, 10).get(i).getClassName().replace("_", " "))
                  && i < 3) {
                enableEndButtons();
                gameWon = true;
              }
            }

            // Set the predictions label in the GUI to the string builder sbf
            Platform.runLater(() -> predLabel.setText(sbf.toString()));

            // Check if the game is won and set the label in the GUI to display to the user they
            // have won
            if ((gameWon && counter > 0) || (gameWon && counter == 0)) {
              Platform.runLater(
                  () -> wordLabel.setText("YOU WON IN " + (60 - counter) + " " + "SECONDS!!!"));

              // Call method to disable the buttons as the game is over
              disableButtons();
              enableEndButtons();
            }

            return null;
          }
        };
    Thread backgroundThread = new Thread(backgroundTask);

    backgroundThread.setDaemon(true);
    backgroundThread.start(); // Begin the thread when called
  }

  private void disableButtons() {
    // Disable the required buttons when called
    canvas.setDisable(true);
    clearButton.setDisable(true);
    eraseBtn.setDisable(true);
    onInk.setDisable(true);
  }

  @FXML
  private void onErase(
      ActionEvent event) { // If the user wants to erase something we set the pen color to white
    this.color = Color.WHITESMOKE;
    eraseBtn.setDisable(true);
    onInk.setDisable(false);
  }

  @FXML
  private void onPen(
      ActionEvent
          event) { // If the user wants to switch back to pen we change the pen color to black
    color = Color.BLACK;
    eraseBtn.setDisable(false);
    onInk.setDisable(true);
  }

  @FXML
  public void onSpeakWord() {
    // Create text to speech instance
    TextToSpeech speech = new TextToSpeech();
    // Create task for thread and put speak inside
    Task<Void> bThread =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            speech.speak(wordChosen);
            return null;
          }
        };
    // Create thread for bThread and start it when this method is called
    Thread speechThread = new Thread(bThread);
    speechThread.setDaemon(true);
    speechThread.start();
  }

  @FXML
  private void onNewGame(ActionEvent event)
      // If the user wants to play a new game we clear the canvas and the user gets a clean canvas
      // with a new word to draw
      throws IOException, URISyntaxException, CsvException {
    MenuController newGameMenu = new MenuController();
    newGameMenu.onNewGame(event);
  }
}
