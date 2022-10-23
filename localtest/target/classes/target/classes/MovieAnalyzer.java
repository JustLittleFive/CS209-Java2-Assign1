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

import static java.util.Map.Entry.comparingByValue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.Math;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MovieAnalyzer {

  class MovieData {

    String posterLink;
    String seriesTitle;
    int releasedYear;
    String certificate;
    int runtime;
    List<String> genre;
    double imdbRating;
    String overviewDescribe;
    int metaScore;
    String director;
    List<String> stars = new ArrayList<>();
    long noofVotes;
    long grs;

    MovieData(
        String posterLink,
        String seriesTitle,
        String releaseYear,
        String certificate,
        String runTime,
        String genre,
        String imdbRating,
        String overviewDescribe,
        String metaScore,
        String director,
        String star1,
        String star2,
        String star3,
        String star4,
        String votes,
        String grs
    ) {
      this.posterLink = posterLink.trim();
      this.seriesTitle = seriesTitle.trim();
      this.releasedYear = Integer.parseInt(releaseYear.trim());
      this.certificate = certificate.trim();
      this.runtime = Integer.parseInt(runTime.substring(0, 3).trim());
      String[] listCells = genre.split(", ");
      Arrays.sort(listCells, (str1, str2) -> str1.compareTo(str2));
      this.genre = new ArrayList<String>(Arrays.asList(listCells));
      this.imdbRating = Float.parseFloat(imdbRating.trim());
      this.overviewDescribe = overviewDescribe;
      if (metaScore.length() == 0) {
        this.metaScore = 0;
      } else {
        this.metaScore = Integer.parseInt(metaScore.trim());
      }
      //      this.Meta_score = Integer.parseInt(Meta_score.trim());
      this.director = director.trim();
      this.stars.add(star1);
      this.stars.add(star2);
      this.stars.add(star3);
      this.stars.add(star4);
      this.noofVotes = Long.parseLong(votes.trim());
      grs = grs.replaceAll(",", "");
      if (grs.length() == 0) {
        this.grs = 0;
      } else {
        grs = grs.replaceAll(",", "");
        grs = grs.replace(" ", "");
        this.grs = Long.parseLong(grs);
      }
    }
  }

  ArrayList<MovieData> allMovies = new ArrayList<MovieData>();

  public MovieAnalyzer(String dataset_path) {
    try {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(dataset_path), "UTF-8")
      );
      reader.readLine();
      String line = null;
      Pattern regex = Pattern.compile(
          "(?:,|\\n|^)(\"(?:(?:\"\")*[^\"]*)*\"|[^\",\\n]*|(?:\\n|$))"
      );
      Pattern preProcess = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");

      List<String> listField;
      // int lineNum = 1;
      while ((line = reader.readLine()) != null) {
        listField = new LinkedList<>();
        String str;
        line += ", ";
        Matcher eachCell = preProcess.matcher(line);
        while (eachCell.find()) {
          str = eachCell.group();
          str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
          str = str.replaceAll("(?sm)(\"(\"))", "$2");
          // str = str.replaceAll("\"\"", "\"");
          listField.add(str);
          // System.out.print(lineNum);
          // System.out.print(": ");
          // System.out.println(str);
          // lineNum++;
        }

        MovieData thisMovie = new MovieData(
            listField.get(0),
            listField.get(1),
            listField.get(2),
            listField.get(3),
            listField.get(4),
            listField.get(5),
            listField.get(6),
            listField.get(7),
            listField.get(8),
            listField.get(9),
            listField.get(10),
            listField.get(11),
            listField.get(12),
            listField.get(13),
            listField.get(14),
            listField.get(15)
        );
        allMovies.add(thisMovie);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<Integer, Integer> getMovieCountByYear() {
    // Collections.sort(
    //   allMovies,
    //   (MovieData movie1, MovieData movie2) ->
    //     movie2.Released_Year - movie1.Released_Year
    //   // movie2.Released_Year - movie1.Released_Year
    // );
    Map<Integer, Integer> ret = new TreeMap<Integer, Integer>(
      new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
          return o2 - o1;
        }
      }
    );
    for (int i = 0; i < allMovies.size(); i++) {
      // MovieData thisMovie = allMovies.get(i);
      // if (ret.containsKey(thisMovie.Released_Year)) {
      //   ret.put(thisMovie.Released_Year, ret.get(thisMovie.Released_Year) + 1);
      // } else {
      //   ret.put(thisMovie.Released_Year, 1);
      // }
      int thisYear = allMovies.get(i).releasedYear;
      if (ret.containsKey(thisYear)) {
        ret.put(thisYear, ret.get(thisYear) + 1);
      } else {
        ret.put(thisYear, 1);
      }
    }
    return ret;
  }

  public Map<String, Integer> getMovieCountByGenre() {
    Map<String, Integer> res = new HashMap<String, Integer>();

    allMovies.forEach(
      movie ->
        movie.genre.forEach(
          label -> {
            if (res.containsKey(label)) {
              res.put(label, res.get(label) + 1);
            } else {
              res.put(label, 1);
            }
          }
        )
    );

    Map<String, Integer> ret = new LinkedHashMap<String, Integer>();
    res
      .entrySet()
      .stream()
      .sorted(Map.Entry.comparingByKey())
      .sorted(Collections.reverseOrder(comparingByValue()))
      .forEachOrdered(x -> ret.put(x.getKey(), x.getValue()));

    return ret;
  }

  public Map<List<String>, Integer> getCoStarCount() {
    Map<List<String>, Integer> res = new LinkedHashMap<List<String>, Integer>();
    allMovies.forEach(
      movie -> {
        String[] starList = new String[movie.stars.size()];
        starList = movie.stars.toArray(starList);
        for (int i = 0; i < starList.length; i++) {
          starList[i] = starList[i].trim();
        }
        Arrays.sort(starList);
        for (int i = 0; i < 3; i++) {
          for (int j = i + 1; j < 4; j++) {
            boolean flag = true;
            for (Map.Entry<List<String>, Integer> entry : res.entrySet()) {
              if (
                entry.getKey().get(0).equals(starList[i]) &&
                entry.getKey().get(1).equals(starList[j])
              ) {
                entry.setValue(entry.getValue() + 1);
                flag = false;
              } else {
                continue;
              }
            }
            if (flag) {
              List<String> pair = new ArrayList<>();
              pair.add(starList[i]);
              pair.add(starList[j]);
              res.put(pair, 1);
            }
          }
        }
      }
    );

    return res;
  }

  public int compareOverview(MovieData movie1, MovieData movie2) {
    String overview1 = movie1.overviewDescribe.trim();
    String overview2 = movie2.overviewDescribe.trim();
    return overview1.compareTo(overview2);
  }

  public List<String> getTopMovies(int top_k, String by) {
    Collections.sort(
      allMovies,
      (MovieData movie1, MovieData movie2) ->
        movie1.seriesTitle.compareTo(movie2.seriesTitle)
    );
    if (by.equals("runtime")) {
      Collections.sort(
        allMovies,
        (MovieData movie1, MovieData movie2) -> movie2.runtime - movie1.runtime
      );
    } else {
      Collections.sort(
        allMovies,
        (MovieData movie1, MovieData movie2) -> compareOverview(movie2, movie1)
      );
    }

    List<String> res = new ArrayList<String>();
    res =
      allMovies
        .stream()
        .map(movie -> movie.seriesTitle)
        .limit(top_k)
        .collect(Collectors.toList());

    return res;
  }

  public List<String> getTopStars(int top_k, String by) {
    Collections.sort(
      allMovies,
      (MovieData movie1, MovieData movie2) ->
        movie1.seriesTitle.compareTo(movie2.seriesTitle)
    );

    if (by.equals("rating")) {
      Map<String, Double[]> starRating = new HashMap<>();
      allMovies.forEach(
        movie -> {
          if (movie.imdbRating > 0) {
            double rate = movie.imdbRating;
            movie.stars.forEach(
              star -> {
                if (starRating.containsKey(star)) {
                  Double[] tmp = starRating.get(star);
                  tmp[1] = tmp[1] + 1;
                  tmp[0] = tmp[0] + rate;
                  starRating.put(star, tmp);
                } else {
                  Double[] data = new Double[2];
                  data[1] = Double.valueOf(1);
                  data[0] = rate;
                  starRating.put(star, data);
                }
              }
            );
          }
        }
      );
      Map<String, Double> avg = new TreeMap<>();
      for (Map.Entry<String, Double[]> entry : starRating.entrySet()) {
        avg.put(entry.getKey(), entry.getValue()[0] / entry.getValue()[1]);
      }
      List<String> res = new ArrayList<>();
      res =
        avg
          .entrySet()
          .stream()
          .sorted(Collections.reverseOrder(comparingByValue()))
          .limit(top_k)
          .map(element -> element.getKey())
          .collect(Collectors.toList());
      return res;
    } else {
      Map<String, Long[]> starIncome = new HashMap<>();
      allMovies.forEach(
        movie -> {
          if (movie.grs > 0) {
            long income = movie.grs;
            movie.stars.forEach(
              star -> {
                if (starIncome.containsKey(star)) {
                  Long[] tmp = starIncome.get(star);
                  tmp[1] += 1;
                  tmp[0] += income;
                  starIncome.put(star, tmp);
                } else {
                  Long[] data = new Long[2];
                  data[1] = Long.valueOf(1);
                  data[0] = income;
                  starIncome.put(star, data);
                }
              }
            );
          }
        }
      );
      Map<String, Long> avg = new TreeMap<>();
      for (Map.Entry<String, Long[]> entry : starIncome.entrySet()) {
        avg.put(entry.getKey(), entry.getValue()[0] / entry.getValue()[1]);
      }
      List<String> res = new ArrayList<>();
      res =
        avg
          .entrySet()
          .stream()
          .sorted(Collections.reverseOrder(comparingByValue()))
          .limit(top_k)
          .map(element -> element.getKey())
          .collect(Collectors.toList());
      return res;
    }
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
            movie.imdbRating >= min_rating &&
            movie.runtime <= max_runtime &&
            movie.genre.contains(genre)
        )
        .map(movie -> movie.seriesTitle)
        // .sorted(Collections.reverseOrder())
        .sorted()
        .collect(Collectors.toList());
    return res;
  }
}
