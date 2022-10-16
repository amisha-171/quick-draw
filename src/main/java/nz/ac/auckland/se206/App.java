package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.util.SceneManager;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  private static MediaPlayer backgroundSongPlayer;

  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   */
  private static FXMLLoader getFxmlLoader(final String fxml) {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/normalCanvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException, URISyntaxException {

    SceneManager.addUi(SceneManager.AppUi.NORMAL_CANVAS, getFxmlLoader("normalCanvas"));
    SceneManager.addUi(SceneManager.AppUi.HIDDEN_CANVAS, getFxmlLoader("hiddenCanvas"));
    SceneManager.addUi(SceneManager.AppUi.ZEN_CANVAS, getFxmlLoader("zenCanvas"));
    SceneManager.addUi(SceneManager.AppUi.USER_MENU, getFxmlLoader("menu"));
    SceneManager.addUi(SceneManager.AppUi.MAIN_MENU, getFxmlLoader("mainmenu"));
    SceneManager.addUi(SceneManager.AppUi.CREATE_PROFILE, getFxmlLoader("createprofile"));
    SceneManager.addUi(SceneManager.AppUi.SELECT_PROFILE, getFxmlLoader("profile"));
    SceneManager.addUi(SceneManager.AppUi.STATS, getFxmlLoader("stats"));
    SceneManager.addUi(SceneManager.AppUi.SELECT_SETTING, getFxmlLoader("gamesettings"));
    SceneManager.addUi(SceneManager.AppUi.BADGES, getFxmlLoader("badges"));
    SceneManager.addUi(SceneManager.AppUi.LEADERBOARD, getFxmlLoader("leaderboard"));

    Scene scene = new Scene(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU), 1057, 703);
    stage.setScene(scene);
    stage.setTitle("Quick, Draw!");
    stage.getIcons().add(new Image("images/pencil.png"));
    stage.show();

    // Set the media we wish to play for some game mode
    Media song = new Media(App.class.getResource("/sounds/jazz.mp3").toURI().toString());
    // Initialize the media player instance
    backgroundSongPlayer = new MediaPlayer(song);
    // If the current music is finished, we play the music again from the beginning, this only
    // applies to zen mode as user can draw for any amount of time however for the other game modes
    // the music duration is greater than any time limit
    backgroundSongPlayer.setOnEndOfMedia(() -> backgroundSongPlayer.seek(Duration.ZERO));
    // Play the song
    backgroundSongPlayer.play();

    stage.setOnCloseRequest(
            event -> {
              Platform.exit();
              System.exit(0);
            });
  }

  /**
   * Static method that can be used by other controllers to pause the background music.
   */
  public static void pauseBackgroundMusic() {
    backgroundSongPlayer.stop();
  }

  /**
   * Static method to mute/unmute the music depending on current playing status.
   */
  public static void toggleMusicPlaying() {
    if (isBackgroundMusicPlaying()) {
      backgroundSongPlayer.setMute(true);
    } else {
      backgroundSongPlayer.setMute(false);
    }

    SceneManager.toggleAllSpeakerIcons();
  }

  /**
   * Static method that can be used by other controllers to play the background music.
   */
  public static void playBackgroundMusic() {
    backgroundSongPlayer.play();
  }

  /**
   * Static method which checks if the background music is playing or is muted, will return true if
   * music is playing and false if it is muted.
   */
  public static boolean isBackgroundMusicPlaying() {
    return !backgroundSongPlayer.isMute();
  }
}
