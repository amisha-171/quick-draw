package nz.ac.auckland.se206.userutils;

import java.io.IOException;
import java.util.ArrayList;

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
  private final String password;
  private int wins;
  private int consecutiveWins;
  private int losses;
  private int fastestTime;
  private int totalSolveTime;
  private int numEasyWords;
  private int numMediumWords;
  private int numHardWords;
  private GameSettings gameSettings;
  private ArrayList<String> wordList = new ArrayList<>();

  private String imageName;

  public User(String userName, String password, String img) {
    // Constructor for new user
    this.userName = userName;
    this.password = password;

    // set default stats
    this.wins = 0;
    this.losses = 0;
    this.consecutiveWins = 0;
    this.totalSolveTime = 0;
    this.numEasyWords = 0;
    this.numMediumWords = 0;
    this.numHardWords = 0;
    this.totalSolveTime = 0;
    this.fastestTime = 100; // 100 is default value because fastest time cannot be more than 60
    // set user image
    this.imageName = img;
    //set default game settings
    this.gameSettings = new GameSettings(
            AccuracySettings.EASY, TimeSettings.EASY, ConfidenceSettings.EASY, WordSettings.EASY
    );
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

  public void updateWordList(String newWord) {
    String difficulty = CategorySelector.getWordDifficulty(newWord);

    //update the user's word list with new word if it doesn't already contain it
    if (!this.wordList.contains(newWord)) {
      this.wordList.add(newWord);
    }

    //update the user's word count for the appropriate difficulty
    switch (difficulty) {
      case "E":
        this.numEasyWords++;
        break;
      case "M":
        this.numMediumWords++;
        break;
      case "H":
        this.numHardWords++;
        break;
    }
  }

  public String getPassword() {
    return password;
  }

  public int getWins() {
    return wins;
  }

  public void incrementWins() {
    this.wins++;
    this.consecutiveWins++;
  }

  public int getLosses() {
    return losses;
  }

  public void incrementLosses() {
    this.losses++;
    this.consecutiveWins = 0;
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

  public String getImageName() {
    return imageName;
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

  public boolean twoConsecutiveWins() {
    return this.consecutiveWins >= 2;
  }

  public boolean fiveConsecutiveWins() {
    return this.consecutiveWins >= 5;
  }

  public boolean tenConsecutiveWins() {
    return this.consecutiveWins >= 10;
  }

  public boolean twentyConsecutiveWins() {
    return this.consecutiveWins >= 20;
  }

  public void saveSelf() {
    try {
      Database.write(this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
