import java.sql.*;
import javax.swing.*;
// import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.awt.image.BufferedImage;

public class Application extends JFrame {
  private static Point size = new Point(1200, 800);
  private JTable table;
  private DefaultTableModel tableModel;

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Error : No look, no feel");
    }

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

  public static void kevin() {
    String sql = "SELECT * FROM films";
    JFrame frame = new JFrame("App");
    JButton button = new JButton("Lister");
    frame.add(button);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    button.addActionListener(e -> {
      // try (Connection connection = App.getConnection();
      // PreparedStatement statement = connection.prepareStatement(sql);
      // ResultSet resultSet = statement.executeQuery()) {
      // JFrame frame2 = new JFrame("Liste des affiches");
      // JPanel titresSurTable = new JPanel();
      // while (resultSet.next()) {
      // String imageUrl = resultSet.getString("affiche");
      // if (imageUrl.startsWith("http")) {
      // try {
      // ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
      // JLabel imageLabel = new JLabel(imageIcon);
      // titresSurTable.add(imageLabel);
      // } catch (Exception ex) {
      // ex.printStackTrace();
      // }
      // }
      //
      // }
      // frame2.add(new JScrollPane(titresSurTable));
      // frame2.setSize(300, 200);
      // frame2.setVisible(true);
      //
      // } catch (Exception e1) {
      // e1.printStackTrace();
      // }
    });

    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}
