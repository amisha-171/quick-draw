package nz.ac.auckland.se206.util;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class SceneManager {

  /**
   * the AppUi enum allows us to easily select which scene we want to show as each scene has a
   * related enum.
   */
  public enum AppUi {
    NORMAL_CANVAS,
    HIDDEN_CANVAS,
    ZEN_CANVAS,
    MAIN_MENU,
    USER_MENU,
    CREATE_PROFILE,
    STATS,
    SELECT_SETTING,
    SELECT_PROFILE,
    LEADERBOARD,
    BADGES
  }

  private static HashMap<AppUi, UiData> sceneMap = new HashMap<>();

  /**
   * This method stores the scene data associated with that scene. Specifically the fxml file and
   * the controller. It also enables preloading of the scenes when used in the application start
   * method.
   *
   * @param appUi an enum specifying the JavaFX scene to load.
   * @param loader the FXMLLoader associated with this scene.
   * @throws IOException when there is an error loading or locating the fxml file.
   */
  public static void addUi(AppUi appUi, FXMLLoader loader) throws IOException {
    sceneMap.put(appUi, new UiData(loader.load(), loader.getController()));
  }

  /**
   * This method returns the controller for a specific scene, to allow access to the controller
   * methods if we are not in that scene at the current moment.
   *
   * @param appUi an enum specifying the JavaFX scene.
   * @return the controller object for the specified JavaFX scene.
   */
  public static Object getUiController(AppUi appUi) {
    return sceneMap.get(appUi).getController();
  }

  /**
   * This method returns the root JavaFX node for a specific scene, so the scene can be switched to
   * the specified one with ease, and the GUI updates accordingly.
   *
   * @param appUi an enum specifying the JavaFX scene.
   * @return the root (parent) node for the specified JavaFX scene.
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi).getRoot();
  }
}
