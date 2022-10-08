package nz.ac.auckland.se206.util.enums;

public enum AccuracySettings {
  EASY(3),
  MEDIUM(2),
  HARD(1);

  private final int accuracyLevel;

  AccuracySettings(int accuracyLevel) {
    this.accuracyLevel = accuracyLevel;
  }

  public int getAccuracyLevel() {
    return this.accuracyLevel;
  }
}
