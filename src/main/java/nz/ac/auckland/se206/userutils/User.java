package nz.ac.auckland.se206.userutils;

import java.util.ArrayList;

/** This class is for the User object which we can use to update stats etc. */
public class User {
  // Constructor for User

  // Create the stats fields and user information fields
  private final String userName;
  private final String password;
  private int wins;
  private int losses;
  private int fastestTime;

  public User(String userName, String password) {
    // Constructor for new user
    this.userName = userName;
    this.password = password;
    //set default stats
    this.wins = 0;
    this.losses = 0;
    this.fastestTime = 100; //100 is default value because fastest time cannot be more than 60
  }

  // Getters and setters for the User fields
  public ArrayList<String> getWordList() {
    return wordList;
  }

  public void updateWordList(String newWord) {
    if (!this.wordList.contains(newWord)) {
      this.wordList.add(newWord);
    }
  }

  private ArrayList<String> wordList = new ArrayList<>();

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

  public void updateFastestTime(int fastestTime) {
    if (fastestTime < this.fastestTime) {
      this.fastestTime = fastestTime;
    }
  }

  public String getUserName() {
    return userName;
  }
}
