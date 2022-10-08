package nz.ac.auckland.se206.util.enums;

public enum TimeSettings {
  EASY(60),
  MEDIUM(45),
  HARD(30),
  MASTER(15);

  private final int timeLevel;

  TimeSettings(int timeLevel) {
    this.timeLevel = timeLevel;
  }

  public int getTimeLevel() {
    return this.timeLevel;
  }
}
