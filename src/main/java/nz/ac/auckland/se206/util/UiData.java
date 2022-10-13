package nz.ac.auckland.se206.util;

import javafx.scene.Parent;

public class UiData {
  private Parent root;
  private Object controller;

  /**
   * This constructor method sets the specified scene's root node and controller, so that these
   * fields can easily be accessed.
   *
   * @param root the root (parent) node of this JavaFX scene.
   * @param controller the controller object of this specified JavaFX scene.
   */
  public UiData(Parent root, Object controller) {
    this.root = root;
    this.controller = controller;
  }

  /**
   * This method returns the root (parent) node.
   *
   * @return the root (parent) node of this JavaFX scene.
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * This method returns the controller object.
   *
   * @return the controller object of this specified JavaFX scene.
   */
  public Object getController() {
    return controller;
  }
}
