/*
 Copyright (c) 2022 SUSTech - JustLittleFive

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieAnalyzer {
  class movieData {
    String Poster_Link;
    String Series_Title;
    int Released_Year;
    String Certificate;
    int Runtime;
    String[] Genre; // todo: delete following min
    float IMDB_Rating;
    String Overview;
    int Meta_score;
    String Director;
    String Star1;
    String Star2;
    String Star3;
    String Star4;
    long No_of_Votes;
    long Grosses;

    public MovieAnalyzer() {}
  }

  List<movieData> allMovies = new ArrayList<movieData>();

  public MovieAnalyzer(String dataset_path) {
    BufferedReader reader = null;
    String line = null;
    try {
      reader = new BufferedReader(new FileReader(dataset_path));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // (?:,|\n|^)      # all values must start at the beginning of the file,
    //                 #   the end of the previous line, or at a comma
    // (               # single capture group for ease of use; CSV can be either...
    //   "             # ...(A) a double quoted string, beginning with a double quote (")
    //     (?:         #        character, containing any number (0+) of
    //       (?:"")*   #          escaped double quotes (""), or
    //       [^"]*     #          non-double quote characters
    //     )*          #        in any order and any number of times
    //   "             #        and ending with a double quote character

    //   |             # ...or (B) a non-quoted value

    //   [^",\n]*      # containing any number of characters which are not
    //                 # double quotes ("), commas (,), or newlines (\n)

    //   |             # ...or (C) a single newline or end-of-file character,
    //                 #           used to capture empty values at the end of
    //   (?:\n|$)      #           the file or at the ends of lines
    // )
    Pattern REGEX = Pattern.compile(
      "(?:,|\\n|^)(\"(?:(?:\"\")*[^\"]*)*\"|[^\",\\n]*|(?:\\n|$))"
    );

    try {
      List listField;
      while ((line = reader.readLine()) != null) {
        if (lineNum == 0) {
          continue;
        }
        listField = new ArrayList<>();
        String str;
        Matcher eachCell = REGEX.matcher(line);
        while (eachCell.find()) {
          str = eachCell.group();
          if (str.startsWith("\"")) {
            str = str.substring(1, str.length() - 1);
            if (str.length() == 1) {
              str = "";
            } else {
              str = str.substring(0, str.length() - 1);
            }
            str = str.replaceAll("\"\"", "\"");
            listField.add(str);
          }
        }
        movieData thisMovie = new movieData();
        for (int i = 0; i < listField.size(); i++) {
          thisMovie.Poster_Link = listField.toString();
        }
      }
      lineNum++;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<Integer, Integer> getMovieCountByYear() {}

  public Map<String, Integer> getMovieCountByGenre() {}

  public Map<List<String>, Integer> getCoStarCount() {}

  public List<String> getTopMovies(int top_k, String by) {}

  public List<String> getTopStars(int top_k, String by) {}

  public List<String> searchMovies(
    String genre,
    float min_rating,
    int max_runtime
  ) {}
}
