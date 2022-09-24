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

  public String getRandomDiffWord(Difficulty difficulty) {
    return difficultyCat
        .get(difficulty)
        .get(new Random().nextInt(difficultyCat.get(difficulty).size()));
  }

  private List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }
}
