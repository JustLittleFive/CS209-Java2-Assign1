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

  class movieData {

    String Poster_Link;
    String Series_Title;
    int Released_Year;
    String Certificate;
    int Runtime; // todo: delete following min
    //    List<String> Genre;
    List<String> Genre;
    double IMDB_Rating;
    String Overview;
    int Meta_score;
    String Director;
    List<String> Stars = new ArrayList<>();
    long No_of_Votes;
    long GRS;

    movieData(
      String Poster_Link,
      String Series_Title,
      String Release_Year,
      String Certificate,
      String Runtime,
      String Genre,
      String IMDB_Rating,
      String Overview,
      String Meta_score,
      String Director,
      String Star1,
      String Star2,
      String Star3,
      String Star4,
      String votes,
      String GRS
    ) {
      this.Poster_Link = Poster_Link.trim();
      this.Series_Title = Series_Title.trim();
      this.Released_Year = Integer.parseInt(Release_Year.trim());
      this.Certificate = Certificate.trim();
      this.Runtime = Integer.parseInt(Runtime.substring(0, 3).trim());
      String[] listCells = Genre.split(", ");
      Arrays.sort(listCells, (str1, str2) -> str1.compareTo(str2));
      this.Genre = new ArrayList<String>(Arrays.asList(listCells));
      this.IMDB_Rating = Float.parseFloat(IMDB_Rating.trim());
      this.Overview = Overview;
      if (Meta_score.length() == 0) {
        this.Meta_score = 0;
      } else {
        this.Meta_score = Integer.parseInt(Meta_score.trim());
      }
      //      this.Meta_score = Integer.parseInt(Meta_score.trim());
      this.Director = Director.trim();
      this.Stars.add(Star1);
      this.Stars.add(Star2);
      this.Stars.add(Star3);
      this.Stars.add(Star4);
      this.No_of_Votes = Long.parseLong(votes.trim());
      GRS = GRS.replaceAll(",", "");
      if (GRS.length() == 0) {
        this.GRS = 0;
      } else {
        GRS = GRS.replaceAll(",", "");
        GRS = GRS.replace(" ", "");
        this.GRS = Long.parseLong(GRS);
      }
    }
  }

  ArrayList<movieData> allMovies = new ArrayList<movieData>();

  public MovieAnalyzer(String dataset_path) {
    try {
      BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(dataset_path), "UTF-8")
      );
      reader.readLine();
      String line = null;
      Pattern REGEX = Pattern.compile(
        "(?:,|\\n|^)(\"(?:(?:\"\")*[^\"]*)*\"|[^\",\\n]*|(?:\\n|$))"
      );
      Pattern preProcess = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");

      List<String> listField;
      int lineNum = 0;
      while ((line = reader.readLine()) != null) {
        // if (lineNum == 0) {
        //   lineNum++;
        //   continue;
        // }
        listField = new LinkedList<>();
        String str;
        line += ", ";
        Matcher eachCell = preProcess.matcher(line);
        while (eachCell.find()) {
          str = eachCell.group();
          // if (str.startsWith("\"")) {
          //   str = str.substring(1, str.length() - 1);
          //   // if (str.length() == 1) {
          //   //   str = "";
          //   // } else {
          //   str = str.substring(0, str.length() - 2);
          //   // }
          // }
          str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
          str = str.replaceAll("(?sm)(\"(\"))", "$2");
          // str = str.replaceAll("\"\"", "\"");
          listField.add(str);
          // System.out.print(lineNum);
          // System.out.print(": ");
          // System.out.println(str);
          lineNum++;
        }
        //        try{
        movieData thisMovie = new movieData(
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
        lineNum = 0;
        //        }catch(Exception e){
        //          continue;
        //        }
        // try {
        //   thisMovie.Poster_Link = listField.get(0);
        //   thisMovie.Series_Title = listField.get(1);
        //   thisMovie.Released_Year = Integer.parseInt(listField.get(2));
        //   thisMovie.Certificate = listField.get(3);
        //   thisMovie.Runtime =
        //     Integer.parseInt(listField.get(4).substring(0, 3).trim());
        //   String[] listCells = listField.get(5).split(", ");
        //   Arrays.sort(listCells, (str1, str2) -> str1.compareTo(str2));
        //   thisMovie.Genre = new ArrayList<String>(Arrays.asList(listCells));
        //   thisMovie.IMDB_Rating = Float.parseFloat(listField.get(6));
        //   thisMovie.Overview = Long.parseLong(listField.get(7));
        //   thisMovie.Meta_score = Integer.parseInt(listField.get(8));
        //   thisMovie.Director = listField.get(9);
        //   thisMovie.Stars.add(listField.get(10));
        //   thisMovie.Stars.add(listField.get(11));
        //   thisMovie.Stars.add(listField.get(12));
        //   thisMovie.Stars.add(listField.get(13));
        //   thisMovie.No_of_Votes = Long.parseLong(listField.get(14));
        //   thisMovie.GRS = Long.parseLong(listField.get(15));
        //   allMovies.add(thisMovie);
        // } catch (Exception e) {
        //   continue;
        // }
        // lineNum++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Map<Integer, Integer> getMovieCountByYear() {
    // Collections.sort(
    //   allMovies,
    //   (movieData movie1, movieData movie2) ->
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
      // movieData thisMovie = allMovies.get(i);
      // if (ret.containsKey(thisMovie.Released_Year)) {
      //   ret.put(thisMovie.Released_Year, ret.get(thisMovie.Released_Year) + 1);
      // } else {
      //   ret.put(thisMovie.Released_Year, 1);
      // }
      int thisYear = allMovies.get(i).Released_Year;
      if (ret.containsKey(thisYear)) {
        ret.put(thisYear, ret.get(thisYear) + 1);
      } else {
        ret.put(thisYear, 1);
      }
    }
    // todo: convert to stream.groupingby
    return ret;
  }

  // public Map<Integer, Integer> getMovieCountByYear() {
  //   Map<Integer, Integer> map = new TreeMap<Integer, Integer>(
  //     new Comparator<Integer>() {
  //       @Override
  //       public int compare(Integer o1, Integer o2) {
  //         return (int) (o2 - o1);
  //       }
  //     }
  //   );

  //   for (int i = 0; i < allMovies.size(); i++) {
  //     int temp_year = allMovies.get(i).Released_Year;
  //     if (map.get(temp_year) == null) {
  //       map.put(temp_year, 1);
  //     } else {
  //       map.put(temp_year, map.get(temp_year) + 1);
  //     }
  //   }
  //   return map;
  // }

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
        String[] starList = new String[movie.Stars.size()];
        starList = movie.Stars.toArray(starList);
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

    // Map<List<String>, Integer> ret = new LinkedHashMap<List<String>, Integer>();
    // res
    //   .entrySet()
    //   .stream()
    //   // .sorted(Map.Entry.comparingByKey())
    //   .forEachOrdered(x -> ret.put(x.getKey(), x.getValue()));

    return res;
  }

  public int compareOverview(movieData movie1, movieData movie2) {
    String overview1 = movie1.Overview.trim();
    String overview2 = movie2.Overview.trim();
    return overview1.length() - overview2.length();
    // if (overview1.length() > overview2.length()) {
    //   return 1;
    // }
    // if (overview1.length() < overview2.length()) {
    //   return 0;
    // }
    // if (movie1.Overview.compareTo(movie2.Overview) > 0) {
    //   return 1;
    // } else {
    //   return 0;
    // }
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
          // movie2.Overview.compareTo(movie1.Overview)
          compareOverview(movie2, movie1)
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
      Map<String, Double[]> starRating = new HashMap<>();
      allMovies.forEach(
        movie -> {
          if (movie.IMDB_Rating > 0) {
            double rate = movie.IMDB_Rating;
            movie.Stars.forEach(
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
          if (movie.GRS > 0) {
            long income = movie.GRS;
            movie.Stars.forEach(
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
    //    List<String> res = new ArrayList<String>();
    //    allMovies
    //      .stream()
    //      .limit(top_k)
    //      .forEachOrdered(
    //        movie ->
    //          movie.Stars.forEach(
    //            star -> {
    //              if (!res.contains(star)) {
    //                res.add(star);
    //              }
    //            }
    //          )
    //      );

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
            movie.IMDB_Rating >= min_rating &&
            movie.Runtime <= max_runtime &&
            movie.Genre.contains(genre)
        )
        .map(movie -> movie.Series_Title)
        // .sorted(Collections.reverseOrder())
        .sorted()
        .collect(Collectors.toList());
    return res;
  }
}
