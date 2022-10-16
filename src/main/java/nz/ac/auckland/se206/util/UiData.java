package nz.ac.auckland.se206.util;

import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.Controller;

public class UiData {
  private Parent root;
  private Controller controller;

  /**
   * This constructor method sets the specified scene's root node and controller, so that these
   * fields can easily be accessed.
   *
   * @param root the root (parent) node of this JavaFX scene.
   * @param controller the controller object of this specified JavaFX scene.
   */
  public UiData(Parent root, Controller controller) {
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
  public Controller getController() {
    return controller;
  }
}
