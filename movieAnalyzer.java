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
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MovieAnalyzer {

  class movieData {

    String Poster_Link;
    String Series_Title;
    int Released_Year;
    String Certificate;
    int Runtime; // todo: delete following min
    List<String> Genre;
    float IMDB_Rating;
    Long Overview;
    int Meta_score;
    String Director;
    List<String> Stars;
    long No_of_Votes;
    long Grosses;

    public void MovieAnalyzer() {}
  }

  List<movieData> allMovies = new ArrayList<movieData>();

  public MovieAnalyzer(String dataset_path) {
    BufferedReader reader = null;
    FileReader inputR = null;
    String line = null;
    try {
      inputR = new FileReader(dataset_path, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      reader = new BufferedReader(inputR);
    } catch (Exception e) {
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
      int lineNum = 0;
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
        int i = 0;
        try {
          thisMovie.Poster_Link = listField.get(i++).toString();
          thisMovie.Series_Title = listField.get(i++).toString();
          thisMovie.Released_Year =
            Integer.valueOf(listField.get(i++).toString());
          thisMovie.Certificate = listField.get(i++).toString();
          thisMovie.Runtime =
            Integer.valueOf(
              listField.get(i++).toString().substring(0, 3).trim()
            );
          String[] listCells = listField.get(i++).toString().split(", ");

          Arrays.sort(listCells, (str1, str2) -> str1.compareTo(str2));

          thisMovie.Genre = new ArrayList<String>(Arrays.asList(listCells));
          thisMovie.IMDB_Rating = Float.valueOf(listField.get(i++).toString());
          thisMovie.Overview = Long.valueOf(listField.get(i++).toString());
          thisMovie.Meta_score = Integer.valueOf(listField.get(i++).toString());
          thisMovie.Director = listField.get(i++).toString();
          thisMovie.Stars.add(listField.get(i++).toString());
          thisMovie.Stars.add(listField.get(i++).toString());
          thisMovie.Stars.add(listField.get(i++).toString());
          thisMovie.Stars.add(listField.get(i++).toString());
          thisMovie.No_of_Votes = Long.valueOf(listField.get(i++).toString());
          thisMovie.Grosses = Long.valueOf(listField.get(i++).toString());
        } catch (Exception e) {
          continue;
        }
        allMovies.add(thisMovie);
      }
      lineNum++;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<Integer, Integer> getMovieCountByYear() {
    Collections.sort(
      allMovies,
      (movieData movie1, movieData movie2) ->
        movie2.Released_Year - movie1.Released_Year
    );
    Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
    for (int i = 0; i < allMovies.size(); i++) {
      movieData thisMovie = allMovies.get(i);
      if (ret.containsKey(thisMovie.Released_Year)) {
        ret.put(thisMovie.Released_Year, ret.get(thisMovie.Released_Year) + 1);
      } else {
        ret.put(thisMovie.Released_Year, 1);
      }
    }
    // todo: convert to stream.groupingby
    return ret;
  }

  public Map<String, Integer> getMovieCountByGenre() {
    // List<String> labels = new ArrayList<String>();
    // for (Iterator<E> it = allMovies.iterator(); it.hasNext();) {
    //   movieData thisMovie = it.next();
    //   for (String label : thisMovie.Genre) {
    //     if (labels.contains(label)) {
    //       labels.add(label);
    //     }
    //   }
    // }

    // Map<String, Integer> res = allMovies.stream().collect(
    //   Collectors.groupingBy(
    //     movieData.Genre.stream().anyMatch(element -> labels.contains(element)),
    //     Collectors.counting()
    //   )
    // );

    // Map<String, Integer> res = allMovies
    //   .stream()
    //   .collect(
    //     Collectors.groupingBy(
    //       Collectors.mapping(
    //         movieData.Genre
    //           .stream()
    //           .anyMatch(element -> labels.contains(element)),
    //         movieData.Genre
    //       ),
    //       Collectors.counting()
    //     )
    //   );

    Map<String, Integer> res = new HashMap<String, Integer>();

    allMovies.forEach(
      movie ->
        movie.Genre.forEach(
          label -> {
            if (res.containsKey(label)) {
              res.put(label, res.get(label) + 1);
            } else {
              res.put(label, 1);
            }
          }
        )
    );

    Map<String, Integer> ret = new HashMap<String, Integer>();
    res
      .entrySet()
      .stream()
      .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
      .forEachOrdered(x -> ret.put(x.getKey(), x.getValue()));

    return ret;
  }

  public Map<List<String>, Integer> getCoStarCount() {
    List<String> stars = new ArrayList<String>();
    for (int i = 0; i < allMovies.size(); i++) {
      movieData thisMovie = allMovies.get(i);
      if (stars.contains(thisMovie.Stars.get(1))) {
        stars.add(thisMovie.Stars.get(1));
      }
      if (stars.contains(thisMovie.Stars.get(2))) {
        stars.add(thisMovie.Stars.get(2));
      }
      if (stars.contains(thisMovie.Stars.get(3))) {
        stars.add(thisMovie.Stars.get(3));
      }
      if (stars.contains(thisMovie.Stars.get(4))) {
        stars.add(thisMovie.Stars.get(4));
      }
    }
    Collections.sort(stars);

    List<List<String>> coStars = new ArrayList<>();
    for (int i = 0; i < stars.size() - 1; i++) {
      for (int j = i; j < stars.size(); j++) {
        List<String> starPair = new ArrayList<String>();
        starPair.add(stars.get(i));
        starPair.add(stars.get(j));
        coStars.add(starPair);
      }
    }
    // Map<List<String>, Integer> res = allMovies
    //   .stream()
    //   .flatMap(
    //     movie -> movie.Stars.containsAll(element -> coStars.contains(element))
    //   );
    Map<List<String>, Integer> res = new HashMap<List<String>, Integer>();
    allMovies.forEach(
      movie ->
        coStars.forEach(
          pair -> {
            if (movie.Stars.containsAll(pair)) {
              if (res.containsKey(pair)) {
                res.put(pair, res.get(pair) + 1);
              } else {
                res.put(pair, 1);
              }
            }
          }
        )
    );

    Map<List<String>, Integer> ret = new HashMap<List<String>, Integer>();
    res
      .entrySet()
      .stream()
      // .sorted(Map.Entry.comparingByKey())
      .forEachOrdered(x -> ret.put(x.getKey(), x.getValue()));

    return ret;
  }

  public List<String> getTopMovies(int top_k, String by) {
    Collections.sort(
      allMovies,
      (movieData movie1, movieData movie2) ->
        movie1.Series_Title.compareTo(movie2.Series_Title)
    );
    if (by.equals("runtime")) {
      Collections.sort(
        allMovies,
        (movieData movie1, movieData movie2) -> movie2.Runtime - movie1.Runtime
      );
    } else {
      Collections.sort(
        allMovies,
        (movieData movie1, movieData movie2) ->
          (int) (movie2.Overview - movie1.Overview)
      );
    }

    List<String> res = new ArrayList<String>();
    res =
      allMovies
        .stream()
        .map(movie -> movie.Series_Title)
        .limit(top_k)
        .collect(Collectors.toList());

    return res;
  }

  public List<String> getTopStars(int top_k, String by) {
    Collections.sort(
      allMovies,
      (movieData movie1, movieData movie2) ->
        movie1.Series_Title.compareTo(movie2.Series_Title)
    );
    if (by.equals("rating")) {
      Collections.sort(
        allMovies,
        (movieData movie1, movieData movie2) ->
          (int) (movie2.No_of_Votes - movie1.No_of_Votes)
      );
    } else {
      Collections.sort(
        allMovies,
        (movieData movie1, movieData movie2) ->
          (int) (movie2.Grosses - movie1.Grosses)
      );
    }

    List<String> res = new ArrayList<String>();
    res =
      allMovies
        .stream()
        .map(movie -> movie.Series_Title)
        .limit(top_k)
        .collect(Collectors.toList());

    return res;
  }

  public List<String> searchMovies(
    String genre,
    float min_rating,
    int max_runtime
  ) {
    List<String> res = new ArrayList<String>();
    res =
      allMovies
        .stream()
        .filter(
          movie ->
            movie.No_of_Votes >= min_rating &&
            movie.Runtime <= max_runtime &&
            movie.Genre.contains(genre)
        )
        .map(movie -> movie.Series_Title)
        .sorted(Collections.reverseOrder())
        .collect(Collectors.toList());
    return res;
  }
}
