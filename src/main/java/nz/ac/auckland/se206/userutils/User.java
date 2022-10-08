package nz.ac.auckland.se206.userutils;

import java.io.IOException;
import java.util.ArrayList;

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

  // Getters and setters for the User fields
  public ArrayList<String> getWordList() {
    return wordList;
  }

  public int getNumWordsPlayed() {
    return this.wordList.size();
  }

  public void updateWordList(String newWord) {
    if (!this.wordList.contains(newWord)) {
      this.wordList.add(newWord);
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

  public String getUserName() {
    return userName;
  }

  public String getImageName() {
    return imageName;
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
