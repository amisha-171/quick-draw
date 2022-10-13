package nz.ac.auckland.se206.filereader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import nz.ac.auckland.se206.util.enums.WordSettings;

public class CategorySelector {
  private Map<WordSettings, List<String>> difficultyCat;
  private static Map<String, String> wordDifficultyMap;

  /**
   * Instantiates CategorySelector object by reading words from the CSV and placing them in hashmap,
   * categorising them by the word setting. Also creates a map mapping every word to its difficulty.
   *
   * @throws IOException If there's an IO error in reading the CSV
   * @throws URISyntaxException If there's a URI error in reading the CSV
   * @throws CsvException If there's another error in reading the CSV
   */
  public CategorySelector() throws IOException, URISyntaxException, CsvException {
    // Initialize hashmaps which need to be used to create key value mapping for words -> difficulty
    difficultyCat = new HashMap<>();
    wordDifficultyMap = new HashMap<>();
    for (WordSettings wordSetting : WordSettings.values()) {
      difficultyCat.put(wordSetting, new ArrayList<>());
    }

    for (String[] line : getLines()) {
      switch (line[1]) {
        case "E":
          // if the word is easy, then it should be added to the easy, medium, and hard settings
          difficultyCat.get(WordSettings.EASY).add(line[0]);
          difficultyCat.get(WordSettings.MEDIUM).add(line[0]);
          difficultyCat.get(WordSettings.HARD).add(line[0]);
          break;
        case "M":
          // if the word is medium, then it should be added to the medium and hard settings
          difficultyCat.get(WordSettings.MEDIUM).add(line[0]);
          difficultyCat.get(WordSettings.HARD).add(line[0]);
          break;
        case "H":
          // if the word is hard, then it should be added to the
          difficultyCat.get(WordSettings.HARD).add(line[0]);
          difficultyCat.get(WordSettings.MASTER).add(line[0]);
          break;
      }
      wordDifficultyMap.put(line[0], line[1]);
    }
  }

  /**
   * Method to generate a random word for a particular word setting, ensuring that a word that was
   * previously played by the user is not generated again. If however the user has played all the
   * words for a given word setting, they may receive any one of those words again.
   *
   * @param wordSetting The word setting desired to select words from
   * @param userWordList List of words that the user has already played
   * @return Random word
   */
  public String getRandomDiffWord(WordSettings wordSetting, List<String> userWordList) {
    int wordPointer = new Random().nextInt(difficultyCat.get(wordSetting).size());
    int originalWordPointer =
        wordPointer; // keep a record of original pointer, in case all words played
    int passes = 0;

    // keep checking the list of words for the difficulty until a word that the user hasn't played
    // is reached
    while ((userWordList.contains(this.difficultyCat.get(wordSetting).get(wordPointer)))
        && (passes < 2)) {
      // wrap to start of the word list for the difficult if we've reached the end
      if (wordPointer < this.difficultyCat.get(wordSetting).size() - 1) {
        wordPointer++;
      } else {
        wordPointer = 0;
        passes++;
        // take the original word if we've searched the whole word list for the difficulty
        if (passes >= 2) {
          wordPointer = originalWordPointer;
        }
      }
    }

    return difficultyCat.get(wordSetting).get(wordPointer);
  }

  /**
   * Uses the difficulty hashmap to return the difficulty of a word.
   *
   * @param word The word for which we want to find the difficulty of
   * @return The difficulty of the word - Either Easy, Medium, or Hard
   */
  public static String getWordDifficulty(String word) {
    return wordDifficultyMap.get(word);
  }

  /**
   * Method to read the CSV and return all the lines from it so as the parse the words and
   * associated difficulties from them.
   *
   * @return List of strings representing individual lines from the CSV
   * @throws IOException If there's an IO error in reading the CSV
   * @throws CsvException If there's another type of error in reading the CSV
   * @throws URISyntaxException If there's a URI error in reading the CSV
   */
  private List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    // Get file instance for the category difficulty csv file
    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());
    // Read contents of the csv file and return List containing arrays of corresponding word and
    // difficulty
    try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }
}
