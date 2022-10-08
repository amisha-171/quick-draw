package nz.ac.auckland.se206.util.enums;

public enum ConfidenceSettings {
  EASY(1),
  MEDIUM(10),
  HARD(25),
  MASTER(50);
  private final int confidenceLevel;

  ConfidenceSettings(int confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
  }

  public int getConfidenceLevel() {
    return this.confidenceLevel;
  }
}
