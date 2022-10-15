package nz.ac.auckland.se206.userutils;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.filereader.CategorySelector;
import nz.ac.auckland.se206.util.enums.AccuracySettings;
import nz.ac.auckland.se206.util.enums.ConfidenceSettings;
import nz.ac.auckland.se206.util.enums.TimeSettings;
import nz.ac.auckland.se206.util.enums.WordSettings;

/** This class is for the User object which we can use to update stats etc. */
public class User {
  // Constructor for User

  // Create the stats fields and user information fields
  private final String userName;
  private int wins;
  private int consecutiveWins;
  private int losses;
  private int fastestTime;
  private int consecutiveWinsUnderTenSeconds;
  private int consecutiveWinsUnderFiveSeconds;
  private int totalSolveTime;
  private int numEasyWords;
  private int numMediumWords;
  private int numHardWords;

  private boolean hasFiveConsecutiveWins;

  private boolean hasTenConsecutiveWins;

  private boolean hasTwentyConsecutiveWins;

  private GameSettings gameSettings;
  private ArrayList<String> wordList = new ArrayList<>();

  private String imageName;

  /**
   * Constructor to initialize a new user object when a user first creates a profile
   *
   * @param userName The username/profile name of the user's account
   * @param img The users corresponding image associated with their profile
   */
  public User(String userName, String img) {
    // Constructor for new user
    this.userName = userName;
    // set default stats
    this.wins = 0;
    this.losses = 0;
    this.consecutiveWins = 0;
    this.numEasyWords = 0;
    this.numMediumWords = 0;
    this.numHardWords = 0;
    this.totalSolveTime = 0;
    this.hasTwentyConsecutiveWins = false;
    this.hasTenConsecutiveWins = false;
    this.hasFiveConsecutiveWins = false;
    this.fastestTime = 100; // 100 is default value because fastest time cannot be more than 60
    // set user image
    this.imageName = img;
    // set default game settings
    this.consecutiveWinsUnderTenSeconds = 0;
    this.consecutiveWinsUnderFiveSeconds = 0;
    this.gameSettings =
        new GameSettings(
            AccuracySettings.EASY, TimeSettings.EASY, ConfidenceSettings.EASY, WordSettings.EASY);
  }

  public void setGameSettings(GameSettings settings) {
    this.gameSettings = settings;
  }

  // Getters and setters for the User fields
  public ArrayList<String> getWordList() {
    return wordList;
  }

  public int getNumWordsPlayed() {
    return this.wordList.size();
  }

  /**
   * Takes input a string representing some word a user has played, we check the difficulty of the
   * word and if the user has not already played this word then we add it to the list
   *
   * @param newWord String of some word a user has played
   */
  public void updateWordList(String newWord) {
    String difficulty = CategorySelector.getWordDifficulty(newWord);

    // update the user's word list with new word if it doesn't already contain it
    if (!this.wordList.contains(newWord)) {
      this.wordList.add(newWord);
    }

    // update the user's word count for the appropriate difficulty
    switch (difficulty) {
      case "E" -> this.numEasyWords++;
      case "M" -> this.numMediumWords++;
      case "H" -> this.numHardWords++;
    }
  }

  public int getWins() {
    return wins;
  }

  /**
   * If the user wins a game we increment the current win field and also consecutive wins field with
   * extra logic to determine how many games in a row the user has won
   */
  public void incrementWins() {
    this.wins++;
    this.consecutiveWins++;

    // check if user has certain amount of consecutive wins
    if (this.consecutiveWins >= 5) {
      hasFiveConsecutiveWins = true;
    }
    if (this.consecutiveWins >= 10) {
      hasTenConsecutiveWins = true;
    }
    if (this.consecutiveWins >= 20) {
      hasTwentyConsecutiveWins = true;
    }
  }

  /**
   * Method checks if the time upon user winning a game is less than or equal to ten seconds, if so
   * we increment the consecutive wins for ten second category, otherwise reset it to zero.
   *
   * @param time Integer representing time taken to win a game
   */
  public void setConsecutiveWinsUnderTenSeconds(int time) {
    if (time <= 10) {
      consecutiveWinsUnderTenSeconds++;
    } else {
      consecutiveWinsUnderTenSeconds = 0;
    }
  }

  /**
   * This method takes in a integer time and checks if it is less than or equal to five seconds, if
   * so we increment the consecutive wins in the five second category, otherwise reset it to zero
   *
   * @param time An integer representing the time taken to complete a game if user has won
   */
  public void setConsecutiveWinsUnderFiveSeconds(int time) {
    if (time <= 5) {
      consecutiveWinsUnderFiveSeconds++;
    } else {
      consecutiveWinsUnderFiveSeconds = 0;
    }
  }

  public int getLosses() {
    return losses;
  }

  public void incrementLosses() {
    this.losses++;
    this.consecutiveWins = 0;
    this.consecutiveWinsUnderTenSeconds = 0;
    this.consecutiveWinsUnderFiveSeconds = 0;
  }

  public int getFastestTime() {
    return this.fastestTime;
  }

  public void updateTotalSolveTime(int timeToAdd) {
    this.totalSolveTime += timeToAdd;
  }

  public double getAverageSolveTime() {
    // use Math.round() alongside multiplying/dividing by 100 to round to 2 d.p.
    return Math.round(((double) this.totalSolveTime / (this.wins + this.losses)) * 100) / 100.0;
  }

  /**
   * This method takes input some integer representing time and if that time integer is smaller than
   * the current fastest time we set the current fastest time to the parameter
   *
   * @param fastestTime Some time value from a game played by the user
   */
  public void updateFastestTime(int fastestTime) {
    if (fastestTime < this.fastestTime) {
      this.fastestTime = fastestTime;
    }
  }

  public int getNumEasyWords() {
    return this.numEasyWords;
  }

  public int getNumMediumWords() {
    return this.numMediumWords;
  }

  public int getNumHardWords() {
    return this.numHardWords;
  }

  public String getUserName() {
    return userName;
  }

  public ImageView getUserIcon() {
    return new ImageView(new Image("/images/profilepics/" + imageName, 64, 64, true, true));
  }

  public Image getUserImage() {
    return new Image("/images/profilepics/" + imageName);
  }

  public int getCurrentAccuracySetting() {
    return gameSettings.getAccuracy().getAccuracyLevel();
  }

  public int getCurrentConfidenceSetting() {
    return gameSettings.getConfidence().getConfidenceLevel();
  }

  public int getCurrentTimeSetting() {
    return gameSettings.getTime().getTimeLevel();
  }

  public WordSettings getCurrentWordSetting() {
    return gameSettings.getWords();
  }

  public GameSettings getGameSettings() {
    return gameSettings;
  }

  public boolean underThirtySeconds() {
    return this.fastestTime < 30;
  }

  public boolean underTwentySeconds() {
    return this.fastestTime < 20;
  }

  public boolean underTenSeconds() {
    return this.fastestTime < 10;
  }

  public boolean twentyFiveGamesPlayed() {
    return this.wins + this.losses >= 25;
  }

  public boolean fiftyGamesPlayed() {
    return this.wins + this.losses >= 50;
  }

  public boolean hundredGamesPlayed() {
    return this.wins + this.losses >= 100;
  }

  public boolean fiveConsecutiveWins() {
    return hasFiveConsecutiveWins;
  }

  public boolean tenConsecutiveWins() {
    return hasTenConsecutiveWins;
  }

  public boolean twentyConsecutiveWins() {
    return hasTwentyConsecutiveWins;
  }

  public int getConsecutiveWinsUnderTenSeconds() {
    return consecutiveWinsUnderTenSeconds;
  }

  public int getConsecutiveWinsUnderFiveSeconds() {
    return consecutiveWinsUnderFiveSeconds;
  }

  /**
   * Automatically saves the current state of some Users object to its corresponding json file when
   * called
   */
  public void saveSelf() {
    try {
      Database.write(this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
