import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * MovieListGui.java
 *
 * File:
 *  $Id$
 *
 * Revisions:
 *  $Log$
 * 
 */

/**
 * A GUI for the MovieList
 * 
 * @author Corban Mailloux <corb@corb.co>
 */
public class MovieListGui extends JFrame implements Observer, ActionListener,
    ListSelectionListener {

  /**
   * The "Please wait" window
   */
  private JDialog waitDialog;

  /**
   * Default, to avoid a warning
   */
  private static final long serialVersionUID = 1L;

  /**
   * The model that handles the movies
   */
  private MovieList movieModel;

  /**
   * The current list of Movies
   */
  private List<Movie> movieList;

  /**
   * The top search box
   */
  private JTextField searchBox;

  /**
   * The JList for the main window
   */
  private JList<String> list;

  /**
   * The ListModel for the JList of movie titles
   */
  private DefaultListModel<String> listModel;

  /**
   * The button that opens the current selection in explorer
   */
  private JButton openExplorer;

  /**
   * The label that says the number of movies
   */
  private JLabel statusLabel;

  /**
   * The Movie Details box
   */
  private JTextArea detailsBox;

  /**
   * The constructor for a GUI
   */
  public MovieListGui() {
    // Build the GUI
    setTitle("MovieList");
    setLayout(new BorderLayout());

    // Top search box
    searchBox = new JTextField("Enter search term...");
    searchBox.addFocusListener(new FocusListener() {

      @Override
      public void focusGained(FocusEvent arg0) {
        if (searchBox.getText().equals("Enter search term...")) {
          searchBox.setText("");
        }
      }

      @Override
      public void focusLost(FocusEvent arg0) {
        if (searchBox.getText().equals("")) {
          searchBox.setText("Enter search term...");
        }
      }

    });

    // Listen for ENTER
    searchBox.addActionListener(this);

    // Real-time searching
    searchBox.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        if (!searchBox.getText().equals("Enter search term...")) {
          movieModel.search(searchBox.getText());
        }
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        if (!searchBox.getText().equals("Enter search term...")) {
          movieModel.search(searchBox.getText());
        }
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        if (!searchBox.getText().equals("Enter search term...")) {
          movieModel.search(searchBox.getText());
        }
      }
    });

    // Middle selection box
    listModel = new DefaultListModel<String>();
    // Add a blank element (required by JList)
    listModel.addElement(" ");

    // Create the list and put it in a scroll pane.
    list = new JList<String>(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setSelectedIndex(0);
    list.setVisibleRowCount(20);
    list.addListSelectionListener(this);
    list.addKeyListener(new KeyAdapter() {
      // Allows the user to press ENTER to open a movie in Explorer
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
          int tempSelectionIndex = list.getSelectedIndex();
          if (tempSelectionIndex >= 0) {
            movieModel.openExplorer(movieList.get(tempSelectionIndex));
          }
        }
      }
    });

    list.addMouseListener(new MouseAdapter() {
      // Allows the user to double-click to open a movie in Explorer
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          int tempSelectionIndex = list.getSelectedIndex();
          if (tempSelectionIndex >= 0) {
            movieModel.openExplorer(movieList.get(tempSelectionIndex));
          }
        }
      }
    });

    // Jason's color choices
    // list.setBackground(Color.BLACK);
    // list.setForeground(Color.GREEN);

    // Bottom section (Details and buttons)
    JPanel bottomPanel = new JPanel(new BorderLayout());

    detailsBox = new JTextArea("Select a movie.", 3, 0);
    detailsBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

    JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    openExplorer = new JButton("Open in Explorer");
    openExplorer.addActionListener(this);
    statusLabel = new JLabel();
    bottomButtonPanel.add(openExplorer);
    bottomButtonPanel.add(statusLabel);

    // Add to the bottomPanel
    bottomPanel.add(detailsBox, BorderLayout.CENTER);
    bottomPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

    // Add everything the the main frame
    add(searchBox, BorderLayout.NORTH);
    add(new JScrollPane(list), BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
    setSize(700, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // pack();

    // Build the "Please wait" dialog
    waitDialog = new JDialog();
    JLabel label = new JLabel("Please wait while the files are indexed.");
    label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
    waitDialog.setTitle("Please Wait...");
    waitDialog.add(label);
    waitDialog.pack();

    // Show the "Please wait" dialog
    waitDialog.setVisible(true);
    movieModel = new MovieList();
    // Register the view with the model.
    movieModel.addObserver(this);
    // waitDialog.dispose();
    // waitDialog.setVisible(false);

    // Show the main window
    // setVisible(true);
    movieModel.update();

  }

  public static void main(String[] args) {
    new MovieListGui();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
   */
  @Override
  public void update(Observable obs1, Object obj1) {
    movieList = movieModel.getMovieList();
    int numMovies = movieList.size();
    statusLabel.setText("Number of Movies: " + numMovies);

    listModel.clear();

    if (numMovies > 0) {
      for (Movie m1 : movieList) {
        listModel.addElement(m1.getName());
      }
    }
    validate();

    waitDialog.setVisible(false);
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // If it's a search:
    if (e.getSource() == searchBox) {
      movieModel.search(searchBox.getText());
    } else if (e.getSource() == openExplorer) {
      // Open Explorer
      int tempSelectionIndex = list.getSelectedIndex();
      if (tempSelectionIndex >= 0) {
        movieModel.openExplorer(movieList.get(tempSelectionIndex));
      }
    }

  }

  /**
   * Updates the details box when a movie is selected.
   */
  @Override
  public void valueChanged(ListSelectionEvent e) {
    int tempSelectionIndex = list.getSelectedIndex();
    if (tempSelectionIndex >= 0) {
      detailsBox.setText(movieList.get(tempSelectionIndex).toString());
    }
  }
}
