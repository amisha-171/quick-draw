package nz.ac.auckland.se206.FileReader;

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

public class CategorySelector {

  public enum Difficulty {
    E,
    M,
    H
  }

  private Map<Difficulty, List<String>> difficultyCat;

  public CategorySelector() throws IOException, URISyntaxException, CsvException {
    difficultyCat = new HashMap<>();
    for (Difficulty difficulty : Difficulty.values()) {
      difficultyCat.put(difficulty, new ArrayList<>());
    }

    for (String[] line : getLines()) {
      difficultyCat.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  public String getRandomDiffWord(Difficulty difficulty, List<String> userWordList) {
    int wordPointer = new Random().nextInt(difficultyCat.get(difficulty).size());
    int originalWordPointer = wordPointer; //keep a record of original pointer, in case all words played
    int passes = 0;

    //keep checking the list of words for the difficulty until a word that the user hasn't played is reached
    while ((userWordList.contains(this.difficultyCat.get(difficulty).get(wordPointer))) && (passes < 2)) {
      //wrap to start of the word list for the difficult if we've reached the end
      if (wordPointer < this.difficultyCat.get(difficulty).size()) {
        wordPointer++;
      } else {
        wordPointer = 0;
        passes++;
        //take the original word if we've searched the whole word list for the difficulty
        if (passes >= 2) {
          wordPointer = originalWordPointer;
        }
      }
    }

    return difficultyCat.get(difficulty).get(wordPointer);
  }

  private List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }
}
