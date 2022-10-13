package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.userutils.Database;
import nz.ac.auckland.se206.userutils.User;
import nz.ac.auckland.se206.util.SceneManager;

public class LeaderboardController {
  @FXML private TableView<User> leaderboard;
  @FXML private TableColumn<User, String> usernameColumn;
  @FXML private TableColumn<User, Integer> winsColumn;
  @FXML private TableColumn<User, Integer> fastestTimeColumn;
  @FXML private TableColumn<User, Integer> averageTimeColumn;
  @FXML private TableColumn<User, ImageView> iconColumn;

  public void initialize() throws IOException {
    setUserInformation();
  }

  /**
   * Updates the leaderboard with the most up to date and accurate statistics of each user based on
   * their current statistics.
   *
   * @throws IOException if user data cannot be read in correctly.
   */
  protected void setUserInformation() throws IOException {

    // initialising contents of each section of the leaderboard
    User[] userList = Database.getAllUsers();
    iconColumn.setCellValueFactory(new PropertyValueFactory<>("userIcon"));
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
    winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));
    fastestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("fastestTime"));
    averageTimeColumn.setCellValueFactory(new PropertyValueFactory<>("averageSolveTime"));

    // ensuring users cannot edit the leaderboard contents
    winsColumn.setReorderable(false);
    usernameColumn.setReorderable(false);
    fastestTimeColumn.setReorderable(false);
    averageTimeColumn.setReorderable(false);
    leaderboard.setEditable(false);

    //  adding user information into the leaderboard itself
    for (User user : userList) {
      leaderboard.getItems().add(user);
    }
    leaderboard.sort();
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
