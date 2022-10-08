package nz.ac.auckland.se206.filereader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import nz.ac.auckland.se206.util.enums.WordSettings;

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

public class CategorySelector {
  private Map<WordSettings, List<String>> difficultyCat;

  public CategorySelector() throws IOException, URISyntaxException, CsvException {
    difficultyCat = new HashMap<>();
    for (WordSettings wordSetting : WordSettings.values()) {
      difficultyCat.put(wordSetting, new ArrayList<>());
    }

    for (String[] line : getLines()) {
      switch (line[1]) {
        case "E":
          //if the word is easy, then it should be added to the easy, medium, and hard settings
          difficultyCat.get(WordSettings.EASY).add(line[0]);
          difficultyCat.get(WordSettings.MEDIUM).add(line[0]);
          difficultyCat.get(WordSettings.HARD).add(line[0]);
          break;
        case "M":
          //if the word is medium, then it should be added to the medium and hard settings
          difficultyCat.get(WordSettings.MEDIUM).add(line[0]);
          difficultyCat.get(WordSettings.HARD).add(line[0]);
          break;
        case "H":
          //if the word is hard, then it should be added to the
          difficultyCat.get(WordSettings.HARD).add(line[0]);
          difficultyCat.get(WordSettings.MASTER).add(line[0]);
          break;
      }
    }
  }

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
      if (wordPointer < this.difficultyCat.get(wordSetting).size()) {
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

  private List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }
}
