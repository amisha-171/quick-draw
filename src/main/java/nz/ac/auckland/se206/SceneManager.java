package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class SceneManager {
  // store the roots of the UIs in a HashMap and associate them
  // with the enum value this allows us to look up the same root corresponding to
  // each UI instead of initialising a new one each time and not saving state

  public enum AppUi {
    CANVAS,
    MAIN_MENU,
    USER_MENU,
    CREATE_PROFILE,
    SELECT_PROFILE
  }

  private static HashMap<AppUi, UiData> sceneMap = new HashMap<AppUi, UiData>();

  public static void addUi(AppUi appUi, FXMLLoader loader) throws IOException {
    sceneMap.put(appUi, new UiData(loader.load(), loader.getController()));
  }

  public static Object getUiController(AppUi appUi) {
    return sceneMap.get(appUi).getController();
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi).getRoot();
  }
}
