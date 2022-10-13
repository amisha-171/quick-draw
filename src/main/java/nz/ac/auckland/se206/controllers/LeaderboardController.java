package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;

public class LeaderboardController {
  @FXML private TableView<User> leaderboard;
  @FXML private TableColumn<User, String> usernameColumn;
  @FXML private TableColumn<User, Integer> winsColumn;
  @FXML private TableColumn<User, Integer> fastestTimeColumn;
  @FXML private TableColumn<User, Integer> averageTimeColumn;
  @FXML private TableColumn<User, Integer> badgesWonColumn;

  /**
   * Initialise the user stats on the leaderboard upon app start-up if users currently exist.
   *
   * @throws IOException if user data cannot be read in correctly.
   */
  public void initialize() throws IOException {
    setUserInformation();
  }

  /**
   * Updates the leaderboard with the most up to date and accurate statistics of each user based on
   * their current statistics.
   *
   * @throws IOException if user data cannot be read in correctly.
   */
  private void setUserInformation() throws IOException {
    User[] userList = Database.getAllUsers();

    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
    winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
    fastestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("fastestTime"));
    averageTimeColumn.setCellValueFactory(new PropertyValueFactory<>("averageSolveTime"));

    for (User user : userList) {
      leaderboard.getItems().add(user);
    }
  }

  /**
   * This method switches back to the main menu page of the game via a button click.
   *
   * @param event The (button) event which invokes this method.
   */
  @FXML
  private void onMainMenuSwitch(ActionEvent event) {
    Scene scene = ((Node) event.getSource()).getScene();
    scene.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MAIN_MENU));
  }
}
