package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.filereader.CategorySelector;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;

public abstract class CanvasController {
  protected String userName;
  @FXML protected Canvas canvas;
  @FXML protected Label wordLabel;
  @FXML protected Button readyButton;
  protected GraphicsContext graphic;

  protected DoodlePrediction model;
  @FXML protected Label predLabel;
  @FXML private ImageView speakerIcon;
  @FXML protected Button eraseBtn;
  @FXML protected Button onInk;
  @FXML protected Label timerCount;
  protected int counter;
  protected String wordChosen;
  protected boolean gameWon;
  @FXML protected Button clearButton;
  protected FileChooser fileChooser;
  protected Boolean isContent;
  protected Color color;
  @FXML protected Button newGameBtn;
  @FXML protected Button saveImage;
  @FXML protected Button mainMenuBtn;
  protected User user;
  @FXML protected ProgressBar predBar;
  @FXML protected Slider volumeSlider;
  protected double currProgress = 0.0;
  protected Media song;
  protected MediaPlayer songPlayer;

  // mouse coordinates
  protected double currentX;
  protected double currentY;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   */
  public void initialize() {
    volumeSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                songPlayer.setVolume(volumeSlider.getValue() * 0.01));
    startGame();
  }

  /**
   * This method initialises the canvas page by initialising the pen, and creating a guard against
   * making predictions until the canvas has been drawn on. It also initialises the prediction
   * model.
   */
  protected void startGame() {
    this.disableStartButtons();

    gameWon = false;
    isContent = false;
    color = Color.BLACK;
    currProgress = 0;
    predBar.setProgress(currProgress);

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
          // Brush size (editable, should not be too small or too large)
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          if (!gameWon && !(counter == 0)) {
            graphic.setStroke(color);
            graphic.setLineWidth(size);
            // Create a line that goes from the point (currentX, currentY) and (x,y)
            graphic.strokeLine(currentX, currentY, x, y);

            // update the coordinates
            currentX = x;
            currentY = y;
          }
        });

    try {
      model = new DoodlePrediction();
    } catch (ModelException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method obtains the user's name as a string and uses this to obtain relevant user data, as
   * well as generating a word for the user to draw.
   *
   * @param userId the current user's name as a string.
   * @throws IOException if an I/O error occurs reading from the stream when reading user data from
   *     Database.
   */
  protected void setUserName(String userId) throws IOException {
    // Obtaining user data and statistics from the Database class.
    this.userName = userId;
    this.user = Database.read(this.userName);
    this.generateWord();
  }

  /**
   * This method generates the word that the user must draw on the canvas, by creating a category
   * selector instance and selecting a new word based on the user's current word difficulty settings
   * and ensuring they do not repeat a previously played word.
   */
  protected void generateWord() throws IOException {
    // Create an instance of category selector
    CategorySelector categorySelector = null;
    try {
      categorySelector = new CategorySelector();
    } catch (IOException | URISyntaxException | CsvException e) {
      e.printStackTrace();
    }
    // Get a random word with Easy difficulty and set the word to be displayed to the user in the
    // GUI
    String randomWord =
        categorySelector.getRandomDiffWord(
            this.user.getCurrentWordSetting(), this.user.getWordList());
    this.setWord(randomWord);
  }

  /**
   * This method sets the word to be drawn in the canvas page GUI so the user can see it.
   *
   * @param wordDraw a String representation of the word to draw.
   */
  private void setWord(String wordDraw) {
    wordLabel.setText("Draw: " + wordDraw);
    wordChosen = wordDraw;
  }

  /** This method is called when the "Clear" button is pressed to clear the drawing canvas. */
  @FXML
  protected void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    currProgress = 0;
    predBar.setProgress(currProgress);
  }

  protected void informUserOnCurrDrawing() throws TranslateException {
    Task<Void> queryTopHundredPredictions =
        new Task<>() {
          final List<Classifications.Classification> predList =
              model.getPredictions(getCurrentSnapshot(), 100);

          @Override
          protected Void call() {
            List<String> preds =
                new ArrayList<>(
                    predList.stream().map(Classifications.Classification::getClassName).toList());
            // Replace each instance of _ with space
            preds.replaceAll(randomWord -> randomWord.replace("_", " "));
            // If the current list of top 100 predictions contains the current given word to draw
            // then
            // execute logic update progress bar
            if (preds.contains(wordChosen)) {
              double num = predList.size() - preds.indexOf(wordChosen);
              double predSize = preds.size();
              currProgress = num / predSize;
              Platform.runLater(() -> predBar.setProgress(currProgress));
            }
            // If the "current progress" value is at least 0.9 this means user has reached top 10
            // predictions so progress bar should be full
            if (currProgress >= 0.9) {
              Platform.runLater(() -> predBar.setProgress(1));
            }
            return null;
          }
        };
    Thread queryThread = new Thread(queryTopHundredPredictions);
    queryThread.setDaemon(true);
    queryThread.start();
  }

  /**
   * This method is called when the user begins to draw on the canvas. It queries the machine
   * learning model in a background thread to obtain predictions every second, based on the contents
   * of the canvas. It checks if the user has won the game and will act accordingly.
   */
  protected void onDraw() {
    // Set the image to be the current snapshot which is called every second, image is final for
    // predictions
    final BufferedImage image = getCurrentSnapshot();

    // Begin the background thread so the GUI does not freeze when being used
    Task<Void> backgroundTask =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            // Initialise a string builder to format the predictions when printed to the GUI
            StringBuilder sbf = new StringBuilder();
            int k = 1;
            // Append predictions to the string builder and format as needed
            for (int i = 0; i < 10; i++) {
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
              // model, if it is, we set the game won status to be true.
              if (wordChosen.equals(
                      model.getPredictions(image, 10).get(i).getClassName().replace("_", " "))
                  && i < user.getCurrentAccuracySetting()
                  && model.getPredictions(image, 10).get(i).getProbability() * 100
                      >= (double) user.getCurrentConfidenceSetting()) {
                enableEndButtons();
                gameWon = true;
              }
            }

            // Set the predictions label in the GUI to the string builder sbf
            Platform.runLater(
                () -> {
                  predLabel.setText(sbf.toString());
                  changeProgressBarColour();
                });

            // Check if the game is won and set the label in the GUI to display to the user they
            // have won
            if ((gameWon && counter > 0) || (gameWon && counter == 0)) {
              Platform.runLater(
                  () -> {
                    wordLabel.setText(
                        "You won in " + (user.getCurrentTimeSetting() - counter) + " seconds!");
                    // update user statistics
                    user.updateFastestTime(user.getCurrentTimeSetting() - counter);
                    user.updateTotalSolveTime(user.getCurrentTimeSetting() - counter);
                  });

              // disable and enable appropriate buttons for game over
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

  /**
   * This method re-initialises the canvas scene for a new game to be played, i.e. it clears the
   * canvas, disables the timer button and allows the user to indicate when they are ready again.
   */
  @FXML
  protected void onNewGame() throws IOException {
    // If the user wants to play a new game we clear the canvas and the user gets a new word to draw
    onClear();
    timerCount.setVisible(false);
    readyButton.setDisable(false);
    setUserName(userName);
    startGame();
    predLabel.setText("Click the \"Start!\" button to start drawing and view the guesses made!");
    timerCount.setTextFill(Color.color(0.8, 0.6, 0.06));
    changeProgressBarColour();
  }

  /**
   * This method switches back to the user menu page of the chosen user via a button click.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onUserMenuSwitch(ActionEvent event) {
    // Check if any music is playing if so stop it and change the scene and its root
    if (songPlayer != null) {
      songPlayer.stop();
    }
    App.playBackgroundMusic();
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_MENU));
  }

  /**
   * This method obtains the current snapshot of the canvas.
   *
   * @return the BufferedImage corresponding to the current canvas content.
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
  private void onSaveSnapshot() throws IOException {
    // Create a file chooser
    fileChooser = new FileChooser();
    // Set the title
    fileChooser.setTitle("Save Your Image");
    // Add all the extensions for saving the drawing
    fileChooser
        .getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png"));
    // Set the initial file name
    fileChooser.setInitialFileName(wordChosen);
    File file = fileChooser.showSaveDialog(new Stage());
    if (file != null) {
      ImageIO.write(getCurrentSnapshot(), "bmp", file);
    }
  }

  /**
   * This method disables certain GUI buttons when it is inappropriate for a user to access them.
   */
  protected void disableButtons() {
    canvas.setDisable(true);
    clearButton.setDisable(true);
    eraseBtn.setDisable(true);
    onInk.setDisable(true);
  }

  /**
   * This method changes the pen to an eraser so users can erase parts of their drawing if needed.
   */
  @FXML
  private void onErase() { // If the user wants to erase, we set the pen color to white
    this.color = Color.WHITE;
    eraseBtn.setDisable(true);
    onInk.setDisable(false);
  }

  /** This method changes the eraser to a pen so users can draw on the canvas when needed. */
  @FXML
  private void onPen() { // If the user wants to switch back to pen we change the pen color to black
    color = Color.BLACK;
    eraseBtn.setDisable(false);
    onInk.setDisable(true);
  }

  /**
   * This method is responsible for playing the music associated with some game mode, it is accessed
   * by its child classes which pass it the path of the music for that game mode.
   *
   * @param currModeSongPath String of the current path of the game mode we wish to play music for
   * @throws MalformedURLException When a malformed URL is generated from the string path of the
   *     music
   */
  public void playGameModeMusic(String currModeSongPath) throws MalformedURLException {
    App.pauseBackgroundMusic(); // pause background music of app
    // Set the media we wish to play for some game mode
    song = new Media(new File(currModeSongPath).toURI().toURL().toString());
    // Initialize the media player instance
    songPlayer = new MediaPlayer(song);
    // If the current music is finished, we play the music again from the beginning, this only
    // applies to zen mode as user can draw for any amount of time however for the other game modes
    // the music duration is greater than any time limit
    songPlayer.setOnEndOfMedia(() -> songPlayer.seek(Duration.ZERO));
    // Play the song
    songPlayer.play();
  }

  /**
   * This method plays the win/lose notification sound upon conclusion of a game.
   *
   * @param won Boolean indicating whether the user won or not
   * @throws URISyntaxException If there's an exception in converting to URI
   */
  public void playNotification(boolean won) {
    String soundFilePath = won ? "/sounds/win.wav" : "/sounds/lose.wav";
    Media notification = null;
    // catch exception if error in reading the sound file
    try {
      notification = new Media(App.class.getResource(soundFilePath).toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    MediaPlayer notificationPlayer = new MediaPlayer(notification);
    notificationPlayer.play();
  }

  /**
   * This method simply changes the speaker icon to indicate to the user whether the background
   * music is muted or can be heard.
   */
  @FXML
  private void onSlider() {
    if (volumeSlider.getValue() == 0) {
      speakerIcon.setImage(new Image("/images/mute.png"));
    } else {
      speakerIcon.setImage(new Image("/images/speaker.png"));
    }
  }

  /**
   * This method changes the colour of the progress bar based on how close the user is to getting
   * specified words into the prediction list.
   */
  private void changeProgressBarColour() {
    if (currProgress >= 0 && currProgress <= 0.33) {
      predBar.getStyleClass().remove("red");
      predBar.getStyleClass().remove("orange");
      predBar.getStyleClass().add("blue");
    } else if (currProgress > 0.33 && currProgress <= 0.66) {
      predBar.getStyleClass().remove("red");
      predBar.getStyleClass().remove("blue");
      predBar.getStyleClass().add("orange");
    } else if (currProgress > 0.66) {
      predBar.getStyleClass().remove("blue");
      predBar.getStyleClass().remove("orange");
      predBar.getStyleClass().add("red");
    }
  }

  /**
   * Abstract method implemented by different game mode controllers that sets up the canvas once
   * user is ready, does slightly different things depending on game mode.
   */
  @FXML
  protected abstract void onReady() throws MalformedURLException;

  /**
   * Abstract method implemented by different game mode controllers that disables the start buttons
   * when the user has started, disables slightly different buttons depending on game mode.
   */
  protected abstract void disableStartButtons();

  /**
   * Abstract method implemented by different game mode controllers that runs the timer (which
   * contains tasks for making the user win/lose, different depending on game mode).
   */
  protected abstract void runTimer();

  /**
   * Abstract method implemented by different game mode controllers that enables the end buttons
   * once a game is complete, enables slightly different buttons depending on game modes.
   */
  protected abstract void enableEndButtons();
}
