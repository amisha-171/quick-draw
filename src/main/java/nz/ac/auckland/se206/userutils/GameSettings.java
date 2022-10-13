package nz.ac.auckland.se206.userutils;

import nz.ac.auckland.se206.util.enums.AccuracySettings;
import nz.ac.auckland.se206.util.enums.ConfidenceSettings;
import nz.ac.auckland.se206.util.enums.TimeSettings;
import nz.ac.auckland.se206.util.enums.WordSettings;

public class GameSettings {
  private final AccuracySettings accuracy;
  private final TimeSettings time;
  private final ConfidenceSettings confidence;
  private final WordSettings words;

  /**
   * This constructor initializes an Object GameSettings which is then stored by the corresponding
   * user profile for which the settings were made.
   *
   * @param accuracy The current accuracy setting (E, M, H)
   * @param time The current time setting (E, M, H, MASTER)
   * @param confidence The current confidence setting (E, M, H, MASTER)
   * @param words The current word setting (E, M, H, MASTER)
   */
  public GameSettings(
      AccuracySettings accuracy,
      TimeSettings time,
      ConfidenceSettings confidence,
      WordSettings words) {
    // Set the required settings on this objects creation which on default are all EASY
    this.accuracy = accuracy;
    this.time = time;
    this.confidence = confidence;
    this.words = words;
  }

  // Getters for settings
  public AccuracySettings getAccuracy() {
    return this.accuracy;
  }

  public TimeSettings getTime() {
    return this.time;
  }

  public ConfidenceSettings getConfidence() {
    return this.confidence;
  }

  public WordSettings getWords() {
    return this.words;
  }

  /**
   * This method takes input some difficulty setting for a category and returns the correct toggle
   * index
   *
   * @param diff Difficulty setting of some category
   * @return Returns current index based on category
   */
  public int getIndexOnLoad(String diff) {
    return switch (diff) {
      case "EASY" -> 0;
      case "MEDIUM" -> 1;
      case "HARD" -> 2;
      case "MASTER" -> 3;
      default -> throw new IllegalArgumentException("Unexpected value");
    };
  }
}
