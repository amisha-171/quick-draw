package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static FXMLLoader getFxmlLoader(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/normalCanvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {

    SceneManager.addUi(SceneManager.AppUi.NORMAL_CANVAS, getFxmlLoader("normalCanvas"));
    SceneManager.addUi(SceneManager.AppUi.HIDDEN_CANVAS, getFxmlLoader("hiddenCanvas"));
    SceneManager.addUi(SceneManager.AppUi.ZEN_CANVAS, getFxmlLoader("zenCanvas"));
    SceneManager.addUi(SceneManager.AppUi.USER_MENU, getFxmlLoader("menu"));
    SceneManager.addUi(SceneManager.AppUi.MAIN_MENU, getFxmlLoader("mainmenu"));
    SceneManager.addUi(SceneManager.AppUi.CREATE_PROFILE, getFxmlLoader("createprofile"));
    SceneManager.addUi(SceneManager.AppUi.SELECT_PROFILE, getFxmlLoader("profile"));
    SceneManager.addUi(SceneManager.AppUi.STATS, getFxmlLoader("stats"));
    SceneManager.addUi(SceneManager.AppUi.SELECT_SETTING, getFxmlLoader("gamesettings"));

    Scene scene = new Scene(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU), 1057, 703);
    stage.setScene(scene);
    stage.setTitle("Quick, Draw!");
    stage.getIcons().add(new Image("images/pencil.png"));
    stage.show();

    stage.setOnCloseRequest(
        event -> {
          Platform.exit();
          System.exit(0);
        });
  }
}
