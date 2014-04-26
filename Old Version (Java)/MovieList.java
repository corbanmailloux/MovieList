import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

/**
 * MovieList.java
 *
 * File:
 *  $Id$
 *
 * Revisions:
 *  $Log$
 * 
 */

/**
 * A new version of MovieList, using Java 1.7.
 * 
 * @author Corban Mailloux <corb@corb.co>
 */
public class MovieList extends Observable {

  /**
   * The list of folders to search through
   */
  private static List<File> folderList = new ArrayList<File>();

  /**
   * The complete MovieList
   */
  private static List<Movie> HdMovieList = new ArrayList<Movie>();

  /**
   * The MovieList that represents the current state
   */
  private static List<Movie> currMovieList = new ArrayList<Movie>();

  /**
   * The list of duplicates.
   */
  private static List<Movie> dupeList = new ArrayList<Movie>();

  public MovieList(List<File> inFolderList) {

    folderList = inFolderList;

    // For each folder
    for (File folder : folderList) {
      // If the folder doesn't exist, skip it
      if (!folder.exists() || !folder.isDirectory()) {
        continue;
      }

      // For each movie in this folder
      for (File movie : folder.listFiles()) {
        if (movie.isFile()) {
          if (movie.getName().equalsIgnoreCase("Thumbs.db")) {
            continue;
          }
          // Add it to the HdMovieList
          HdMovieList.add(new Movie(movie));
        }
      }
    }

    if (HdMovieList.isEmpty()) {
      JOptionPane.showMessageDialog(null,
          "No movies found. Please verify your \"MovieList.properties\" file.");
    }

    // Sort the movie list
    Collections.sort(HdMovieList);

    // Find duplicates
    for (Movie m1 : HdMovieList) {
      if (Collections.frequency(HdMovieList, m1) > 1) {
        dupeList.add(m1);
      }
    }

    // Set up the current MovieList
    currMovieList.addAll(HdMovieList);
  }

  /**
   * A forced call to update.
   */
  public void update() {
    setChanged();
    notifyObservers();
  }

  /**
   * Set the current list to the dupeList
   */
  public void dupes() {
    currMovieList.clear();
    currMovieList.addAll(dupeList);
    setChanged();
    notifyObservers();
  }

  /**
   * Open a given Movie in Windows Explorer.
   * 
   * @param m1
   *          - the Movie to open and select
   */
  public void openExplorer(Movie m1) {
    try {
      Runtime.getRuntime().exec("explorer /select, \"" + m1.getPath() + "\"");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Return the current MovieList. Called by Observer's update.
   * 
   * @return - current MovieList
   */
  public List<Movie> getMovieList() {
    return currMovieList;
  }

  /**
   * Non-case-sensitive search of the current movie list for a string
   * 
   * @param searchStr
   *          - the string to search for in movie titles
   */
  public void search(String searchStr) {
    currMovieList.clear();
    for (Movie m1 : HdMovieList) {
      if (m1.getName().toLowerCase().contains(searchStr.toLowerCase())) {
        currMovieList.add(m1);
      }
    }
    setChanged();
    notifyObservers();
  }
}
