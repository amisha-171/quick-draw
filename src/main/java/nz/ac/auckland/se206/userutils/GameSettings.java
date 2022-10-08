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

  // Constructor for some users game settings
  public GameSettings(
      AccuracySettings accuracy,
      TimeSettings time,
      ConfidenceSettings confidence,
      WordSettings words) {
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
