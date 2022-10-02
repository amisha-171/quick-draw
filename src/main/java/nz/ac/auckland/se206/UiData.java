package nz.ac.auckland.se206;

import javafx.scene.Parent;

public class UiData {
  private Parent root;
  private Object controller;

  public UiData(Parent root, Object controller) {
    this.root = root;
    this.controller = controller;
  }

  public Parent getRoot() {
    return root;
  }

  public Object getController() {
    return controller;
  }
}
