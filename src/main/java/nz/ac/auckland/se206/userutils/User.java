package nz.ac.auckland.se206.userutils;

import java.io.IOException;
import java.util.ArrayList;

import nz.ac.auckland.se206.filereader.CategorySelector;
import nz.ac.auckland.se206.util.enums.WordSettings;

/** This class is for the User object which we can use to update stats etc. */
public class User {
  // Constructor for User

  // Create the stats fields and user information fields
  private final String userName;
  private final String password;
  private int wins;
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
    this.totalSolveTime = 0;
    this.fastestTime = 100; // 100 is default value because fastest time cannot be more than 60
    // set user image
    this.imageName = img;
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
  }

  public int getLosses() {
    return losses;
  }

  public void incrementLosses() {
    this.losses++;
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

  public void saveSelf() {
    try {
      Database.write(this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
