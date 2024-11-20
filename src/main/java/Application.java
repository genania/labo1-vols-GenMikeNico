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
  static List<Vol> listevols;

  public static void main(String[] args) {

    readJsonFile(path);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Error : No look, no feel");
    }

    SwingUtilities.invokeLater(() -> {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      Application app = new Application();

      app.setBounds(
          (screenSize.width - size.x) / 2,
          (screenSize.height - size.y) / 2,
          size.x, size.y);

      app.setExtendedState(JFrame.NORMAL);

      app.setVisible(true);
    });
  }

  public Application() {
    setTitle("Liste des vols");
    setSize(size.x, size.y);

    setExtendedState(JFrame.NORMAL);
    // setMinimumSize(new Dimension(640, 480));

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
    tableModel = new DefaultTableModel(new Object[] { "numero", "destination", "depart", "reservations" }, 0);
    table = new JTable(tableModel);

    // Rendre le tableau triable
    table.setAutoCreateRowSorter(true);
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    table.setRowSorter(sorter);

    // Bouton pour lister les vols
    JButton btnLister = new JButton("Lister");

    btnLister.setBackground(Color.BLUE);
    btnLister.setForeground(Color.WHITE);
    btnLister.setFocusPainted(false);
    btnLister.setFont(new Font("Arial", Font.BOLD, 12));
    btnLister.setMargin(new Insets(5, 5, 5, 5));
    btnLister.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // listevols = RequetesServeurNode.obtenirListevolsDeJSON("/vols");
        listevols = readJsonFile(path);
        afficherListeVolsParJTable();
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
    //
    // for (Vol vol : vols) {
    // System.out.println(vol);
    // }
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

  // Méthode pour afficher la liste des films dans un JTable
  public static void afficherListeVolsParJTable() {
    try {

      // Rafraîchir la table avec les nouveaux films
      Application filmApp = new Application();
      filmApp.updateTableData(listevols);
      filmApp.setVisible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Méthode pour ajouter les données des vols dans le modèle de la table
  public void updateTableData(List<Vol> vols) {
    tableModel.setRowCount(0); // Vider les données actuelles du tableau
    table.setRowHeight(80); // Augmentez la valeur pour une plus grande hauteur d'image si nécessaire
    for (Vol vol : vols) {
      tableModel.addRow(new Object[] { vol.getNumero(), vol.getDestination(),
          vol.getDepart(),
          vol.getReservations() });

      // Charger l'image à partir de l'URL pour la colonne "Affiche"
      // ImageIcon imageIcon = null;
      // try {
      // URL url = new URL(vol.getAffiche());
      // BufferedImage image = javax.imageio.ImageIO.read(url);
      // imageIcon = new ImageIcon(image.getScaledInstance(75, 80,
      // Image.SCALE_SMOOTH));
      // } catch (Exception e) {
      // e.printStackTrace();
      // // Image par défaut si l'URL ne peut être chargée
      // imageIcon = new ImageIcon(new BufferedImage(75, 80,
      // BufferedImage.TYPE_INT_ARGB));
      // }

      // Ajouter la ligne avec les colonnes demandées
      // tableModel.addRow(new Object[] { imageIcon, vol.getId(), vol.getTitre(),
      // vol.getRealisateur(),
      // vol.getAnnee(), vol.getDuree() });
    }
  }

}
