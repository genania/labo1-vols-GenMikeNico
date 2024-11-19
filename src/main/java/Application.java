import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import modele.DateVol;
import modele.Vol;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Application extends JFrame {
  private JTable table;
  private DefaultTableModel tableModel;

  private static Point size = new Point(1200, 800);
  static String path = "src/main/resources/donnees/vols.json";
  static List<Vol> listeFilms;

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Error : No look, no feel");
    }

    readJsonFile(path);
    SwingUtilities.invokeLater(() -> {
      Application app = new Application();

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      app.setBounds(
          (screenSize.width - size.x) / 2,
          (screenSize.height - size.y) / 2,
          size.x, size.y);

      app.setExtendedState(JFrame.NORMAL);

      app.setVisible(true);
    });
  }

  public Application() {
    setTitle("Liste des Films");
    setSize(size.x, size.y);

    setExtendedState(JFrame.NORMAL);
    setMinimumSize(new Dimension(640, 480));

    // Force normal state and specific bounds
    addComponentListener(new java.awt.event.ComponentAdapter() {
      @Override
      public void componentResized(java.awt.event.ComponentEvent e) {
        if (getExtendedState() != JFrame.NORMAL) {
          setExtendedState(JFrame.NORMAL);
          setBounds(
              (Toolkit.getDefaultToolkit().getScreenSize().width - size.x) / 2,
              (Toolkit.getDefaultToolkit().getScreenSize().height - size.y) / 2,
              size.x, size.y);
        }
      }
    });

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null); // Centrer la fenêtre

    // Initialisation du modèle de table avec les colonnes demandées
    tableModel = new DefaultTableModel(new Object[] { "Affiche", "ID", "Titre", "Réalisateur", "Année", "Durée" },
        0) {
      @Override
      public Class<?> getColumnClass(int column) {
        return column == 0 ? ImageIcon.class : Object.class; // Affiche la première colonne comme une image
      }
    };
    table = new JTable(tableModel);

    // Rendre le tableau triable
    table.setAutoCreateRowSorter(true);
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    table.setRowSorter(sorter);

    // Bouton pour lister les films
    JButton btnLister = new JButton("Lister");
    btnLister.setBackground(Color.BLUE);
    btnLister.setForeground(Color.WHITE);
    btnLister.setFocusPainted(false);
    btnLister.setFont(new Font("Arial", Font.BOLD, 12));
    btnLister.setMargin(new Insets(5, 5, 5, 5));
    btnLister.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // listeFilms = RequetesServeurNode.obtenirListeFilmsDeJSON("/films");
        // afficherListeFilmsParJTable();
      }
    });

    // Mise en page de l'interface avec le bouton en haut
    JPanel panel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(btnLister);
    panel.add(buttonPanel, BorderLayout.NORTH);
    panel.add(new JScrollPane(table), BorderLayout.CENTER);
    add(panel);
  }

  public static List<Vol> readJsonFile(String filePath) {
    List<Vol> vols = new ArrayList<>();

    try {
      // Read the entire file into a string
      String content = new String(Files.readAllBytes(Paths.get(filePath)));

      // Remove whitespace and newlines for easier parsing
      content = content.replaceAll("\\s+", "");

      // Extract individual objects between curly braces
      Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
      Matcher matcher = pattern.matcher(content);

      while (matcher.find()) {
        String objectStr = matcher.group(1);
        int numero = Integer.parseInt(extractValue(objectStr, "id"));
        String destination = extractValue(objectStr, "destination");
        DateVol date = new DateVol(
            Integer.parseInt(extractValue(objectStr, "jour")),
            Integer.parseInt(extractValue(objectStr, "mois")),
            Integer.parseInt(extractValue(objectStr, "annee")));
        int reservations = Integer.parseInt(extractValue(objectStr, "reservations"));

        vols.add(new Vol(numero, destination, date, reservations));
      }

    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Error parsing JSON: " + e.getMessage());
    }

    for (Vol vol : vols) {
      System.out.println(vol);
    }
    return vols;
  }

  private static String extractValue(String json, String key) {
    // Find the value after the key
    String pattern = "\"" + key + "\":\"?([^,\"}]+)\"?";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(json);

    if (m.find()) {
      return m.group(1);
    }
    return "";
  }
}
