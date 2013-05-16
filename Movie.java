import java.io.File;

/**
 * Movie.java
 *
 * File:
 *  $Id$
 *
 * Revisions:
 *  $Log$
 * 
 */

/**
 * A Movie object for use with MovieList
 * 
 * @author Corban Mailloux <corb@corb.co>
 */
public class Movie implements Comparable<Movie> {

  /**
   * File object representing this movie
   */
  private File fileObject;

  /**
   * File size in GB
   */
  private double fileSize;

  /**
   * The file's name
   */
  private String fileName;

  /**
   * The constructor for a Movie
   * 
   * @param inFile
   *          - a File representing the Movie
   */
  public Movie(File inFile) {
    fileObject = inFile;
    fileSize =
        (double) Math.round((fileObject.length() / 1073741824.0) * 100) / 100;
    fileName = inFile.getName();
  }

  /**
   * The basic toString() method.
   * 
   * @return - a String with the name, path, and size
   */
  @Override
  public String toString() {
    String returnStr = "---------- Details: ----------\n";
    returnStr += "Name: " + fileName + "\n";
    returnStr += "  Path: " + fileObject.getAbsolutePath() + "\n";
    returnStr += "  Size: " + fileSize + " GB";
    return returnStr;
  }

  /**
   * Return the Movie's name
   * 
   * @return - the filename
   */
  public String getName() {
    return fileName;
  }

  /**
   * Return the full path to the Movie
   * 
   * @return - the path to the Movie
   */
  public String getPath() {
    return fileObject.getAbsolutePath();
  }

  /**
   * Compare this Movie to another Movie, based only on name, ignoring case
   * 
   * @return - -1 if this < m1; 0 if this == m1; 1 if this > m1
   */
  @Override
  public int compareTo(Movie m1) {
    return fileName.compareToIgnoreCase(m1.getName());
  }

  /**
   * Compare this Movie to another Movie, based only on name, ignoring case
   * 
   * @return - true iff the name's are the same, ignoring case
   */
  @Override
  public boolean equals(Object m1) {
    if (m1 instanceof Movie) {
      return fileName.equalsIgnoreCase((((Movie) m1).getName()));
    }
    return false;
  }

  /**
   * Computes a hashCode for a Movie. Uses File's hashCode()
   * 
   * @return - the hashCode
   */
  @Override
  public int hashCode() {
    return fileObject.hashCode();
  }
}
