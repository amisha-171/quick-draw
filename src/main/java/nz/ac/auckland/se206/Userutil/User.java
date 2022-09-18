package nz.ac.auckland.se206.Userutil;

import java.util.ArrayList;

/** This class is for the User object which we can use to update stats etc. */
public class User {
  // Constructor for User
  public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }
  // Create the stats fields and user information fields
  private final String userName;
  private final String password;
  private int wins;
  private int losses;
  private int fastestTime;

  // Getters and setters for the User fields
  public ArrayList<String> getWordList() {
    return wordList;
  }

  public void setWordList(ArrayList<String> wordList) {
    this.wordList = wordList;
  }

  private ArrayList<String> wordList = new ArrayList<>();

  public String getPassword() {
    return password;
  }

  public int getWins() {
    return wins;
  }

  public void setWins(int wins) {
    this.wins = wins;
  }

  public int getLosses() {
    return losses;
  }

  public void setLosses(int losses) {
    this.losses = losses;
  }

  public int getFastestTime() {
    return fastestTime;
  }

  public void setFastestTime(int fastestTime) {
    this.fastestTime = fastestTime;
  }

  public String getUserName() {
    return userName;
  }
}
