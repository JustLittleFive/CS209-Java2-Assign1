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

import java.io.IOException;
import java.nio.file.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

class MovieAnalyzer {

  public MovieAnalyzer(String dataset_path) {}

  public Map<Integer, Integer> getMovieCountByYear() {

  }

  public Map<String, Integer> getMovieCountByGenre(){}

  public Map<List<String>, Integer> getCoStarCount(){}

  public List<String> getTopMovies(int top_k, String by){}

  public List<String> getTopStars(int top_k, String by){}

  public List<String> searchMovies(String genre, float min_rating, int max_runtime){}






}
