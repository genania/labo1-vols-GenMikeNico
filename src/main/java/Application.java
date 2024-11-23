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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import utilitaires.EditableTableModel;

public class Application extends JFrame {
  private JTable table;
  private EditableTableModel tableModel;
  private static Point size = new Point(1200, 800);
  static String path = "src/main/resources/donnees/vols.json";
  List<Vol> listevols;

  public static void main(String[] args) {
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
      app.afficherListeVolsParJTable();
    });
  }

  public Application() {
    listevols = readJsonFile(path);

    setTitle("Gestion des vols");
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
    tableModel = new EditableTableModel(new Object[] { "numero", "destination", "depart", "reservations" }, 0);
    table = new JTable(tableModel);

    // Rendre le tableau triable
    table.setAutoCreateRowSorter(true);
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    table.setRowSorter(sorter);

    // Bouton pour lister les vols
    JButton btnLister = new JButton("Lister");

    btnLister.setBackground(Color.BLUE);
    btnLister.setForeground(Color.black);
    btnLister.setFocusPainted(false);
    btnLister.setFont(new Font("Arial", Font.BOLD, 12));
    btnLister.setMargin(new Insets(5, 5, 5, 5));
    btnLister.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // listevols = RequetesServeurNode.obtenirListevolsDeJSON("/vols");
        afficherListeVolsParJTable();
      }
    });

    // Bouton pour ajouter un vols
    JButton btnAjouter = new JButton("Ajouter un vol");

    btnAjouter.setBackground(Color.BLUE);
    btnAjouter.setForeground(Color.black);
    btnAjouter.setFocusPainted(false);
    btnAjouter.setFont(new Font("Arial", Font.BOLD, 12));
    btnAjouter.setMargin(new Insets(5, 5, 5, 5));
    btnAjouter.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // listevols = RequetesServeurNode.obtenirListevolsDeJSON("/vols");
        ajouterVol();
      }
    });

    // Bouton pour modifier un vols
    JButton btnModifier = new JButton("Modifier un vol");

    btnModifier.setBackground(Color.RED);
    btnModifier.setForeground(Color.BLACK);
    btnModifier.setFocusPainted(false);
    btnModifier.setFont(new Font("Arial", Font.BOLD, 12));
    btnModifier.setMargin(new Insets(5, 5, 5, 5));

    // Action pour modifier un vol
    btnModifier.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modifierVol();
      }
    });

    // Bouton pour supprimer un vols
    JButton btnSupprimer = new JButton("Supprimer un vol");

    btnSupprimer.setBackground(Color.RED);
    btnSupprimer.setForeground(Color.BLACK);
    btnSupprimer.setFocusPainted(false);
    btnSupprimer.setFont(new Font("Arial", Font.BOLD, 12));
    btnSupprimer.setMargin(new Insets(5, 5, 5, 5));

    // Action pour supprimer un vol
    btnSupprimer.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        supprimerVol();
      }
    });

    // Bouton pour reserver un vols
    JButton btnReservation = new JButton("Réservation d'un vol");
    btnReservation.setBackground(Color.BLUE);
    btnReservation.setForeground(Color.black);
    btnReservation.setFocusPainted(false);
    btnReservation.setFont(new Font("Arial", Font.BOLD, 12));
    btnReservation.setMargin(new Insets(5, 5, 5, 5));
    btnReservation.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ;
        reserverVol();
      }
    });

    // Mise en page de l'interface avec le bouton en haut
    JPanel panel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(btnLister);
    buttonPanel.add(btnAjouter);
    buttonPanel.add(btnModifier);
    buttonPanel.add(btnSupprimer);
    buttonPanel.add(btnReservation);
    panel.add(buttonPanel, BorderLayout.NORTH);
    panel.add(new JScrollPane(table), BorderLayout.CENTER);
    add(panel);
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

  // Méthode pour afficher la liste des vols dans un JTable
  public void afficherListeVolsParJTable() {
    try {

      // Rafraîchir la table avec les nouveaux vols
      this.updateTableData(listevols);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ajouterVol() {
    JDialog dialog = new JDialog(this, "Ajouter un vol", true);
    dialog.setSize(400, 300);
    dialog.setLayout(new GridLayout(5, 2, 10, 10));

    // Champs pour les données
    JLabel labelNumero = new JLabel("Numéro de vol : ");
    JTextField textNumero = new JTextField();

    JLabel labelDestination = new JLabel("Destination : ");
    JTextField textDestination = new JTextField();

    JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : ");
    JTextField textDate = new JTextField();

    JLabel labelReservations = new JLabel("Réservations : ");
    JTextField textReservations = new JTextField();
    textReservations.setText("0"); // Par défaut, 0 réservations
    textReservations.setEditable(false); // Non modifiable

    // Boutons OK et Annuler
    JButton btnOk = new JButton("OK");
    JButton btnAnnuler = new JButton("Annuler");

    // Ajout des composants au JDialog
    dialog.add(labelNumero);
    dialog.add(textNumero);

    dialog.add(labelDestination);
    dialog.add(textDestination);

    dialog.add(labelDate);
    dialog.add(textDate);

    dialog.add(labelReservations);
    dialog.add(textReservations);

    dialog.add(btnOk);
    dialog.add(btnAnnuler);

    // Action du bouton OK
    btnOk.addActionListener(e -> {
      try {
        // Récupérer les valeurs saisies
        int numero = Integer.parseInt(textNumero.getText());
        String destination = textDestination.getText();
        String[] dateParts = textDate.getText().split("-");
        DateVol date = new DateVol(
            Integer.parseInt(dateParts[0]), // Jour
            Integer.parseInt(dateParts[1]), // Mois
            Integer.parseInt(dateParts[2]) // Année
        );
        int reservations = Integer.parseInt(textReservations.getText());

        // Ajouter le nouveau vol à la liste
        Vol nouveauVol = new Vol(numero, destination, date, reservations);
        listevols.add(nouveauVol);

        // Enregistrer la liste mise à jour dans le fichier JSON
        enregistrerSurFichierJson(listevols);

        // Mettre à jour la table pour afficher le nouveau vol
        updateTableData(listevols);

        // Fermer le JDialog actuel
        dialog.dispose();

        // Afficher la confirmation
        afficherConfirmationVol(nouveauVol);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dialog, "Veuillez entrer des données valides.", "Erreur",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    // Action du bouton Annuler
    btnAnnuler.addActionListener(e -> dialog.dispose());

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  public void modifierVol() {
    // Vérifier si une ligne est sélectionnée
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vol à modifier.", "Erreur",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // Récupérer l'index du vol sélectionné dans la liste
    int modelRow = table.convertRowIndexToModel(selectedRow);
    Vol volAModifier = listevols.get(modelRow);

    // Ouvrir un JDialog pré-rempli avec les informations du vol
    JDialog dialog = new JDialog(this, "Modifier un vol", true);
    dialog.setSize(400, 300);
    dialog.setLayout(new GridLayout(5, 2, 10, 10));

    // Champs pré-remplis
    JLabel labelNumero = new JLabel("Numéro de vol : ");
    JTextField textNumero = new JTextField(String.valueOf(volAModifier.getNumero()));
    textNumero.setEditable(false); // Numéro non modifiable

    JLabel labelDestination = new JLabel("Destination : ");
    JTextField textDestination = new JTextField(volAModifier.getDestination());

    JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : ");
    JTextField textDate = new JTextField(
        volAModifier.getDepart().getJour() + "-" +
            volAModifier.getDepart().getMois() + "-" +
            volAModifier.getDepart().getAn());

    JLabel labelReservations = new JLabel("Réservations : ");
    JTextField textReservations = new JTextField(String.valueOf(volAModifier.getReservations()));

    // Boutons OK et Annuler
    JButton btnOk = new JButton("OK");
    JButton btnAnnuler = new JButton("Annuler");

    // Ajout des composants au JDialog
    dialog.add(labelNumero);
    dialog.add(textNumero);

    dialog.add(labelDestination);
    dialog.add(textDestination);

    dialog.add(labelDate);
    dialog.add(textDate);

    dialog.add(labelReservations);
    dialog.add(textReservations);

    dialog.add(btnOk);
    dialog.add(btnAnnuler);

    // Action du bouton OK
    btnOk.addActionListener(e -> {
      try {
        // Récupérer les nouvelles valeurs
        String destination = textDestination.getText();
        String[] dateParts = textDate.getText().split("-");
        DateVol date = new DateVol(
            Integer.parseInt(dateParts[0]), // Jour
            Integer.parseInt(dateParts[1]), // Mois
            Integer.parseInt(dateParts[2]) // Année
        );
        int reservations = Integer.parseInt(textReservations.getText());

        // Mettre à jour le vol sélectionné
        volAModifier.setDestination(destination);
        volAModifier.setDepart(date);
        volAModifier.setReservations(reservations);

        // Mettre à jour la liste et le fichier JSON
        enregistrerSurFichierJson(listevols);

        // Mettre à jour la table
        updateTableData(listevols);

        // Fermer le JDialog
        dialog.dispose();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(dialog, "Veuillez entrer des données valides.", "Erreur",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    // Action du bouton Annuler
    btnAnnuler.addActionListener(e -> dialog.dispose());

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  public void supprimerVol() {
    // Vérifier si une ligne est sélectionnée
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vol à supprimer.", "Erreur",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // Confirmer la suppression
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Êtes-vous sûr de vouloir supprimer ce vol ?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      // Récupérer l'index du vol dans la liste
      int modelRow = table.convertRowIndexToModel(selectedRow);

      // Supprimer le vol de la liste
      listevols.remove(modelRow);

      // Mettre à jour le fichier JSON
      enregistrerSurFichierJson(listevols);

      // Mettre à jour la table
      updateTableData(listevols);

      // Afficher une confirmation
      JOptionPane.showMessageDialog(this, "Le vol a été supprimé avec succès.", "Confirmation",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public void reserverVol() {
    // Vérifier si une ligne est sélectionnée
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vol à réserver.", "Erreur",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // Récupérer l'index du vol sélectionné dans la liste
    int modelRow = table.convertRowIndexToModel(selectedRow);
    Vol volAReserver = listevols.get(modelRow);

    // Ouvrir un JDialog pré-rempli avec les informations du vol
    JDialog dialog = new JDialog(this, "Réserver un vol", true);
    dialog.setSize(400, 300);
    // dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS)); // Disposition verticale
    dialog.setLayout(new GridLayout(7, 1, 10, 10));

    // Champs pré-remplis
    JLabel labelNumero = new JLabel("Numéro de vol : " + volAReserver.getNumero());
    JLabel labelDestination = new JLabel("Destination : " + volAReserver.getDestination());
    JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : " + volAReserver.getDepart().getJour() + "-" +
        volAReserver.getDepart().getMois() + "-" + volAReserver.getDepart().getAn());
    JLabel labelReservationsActuel = new JLabel("Réservations actuelles : " + volAReserver.getReservations());

    // Panel pour la ligne "Nombre de réservations à ajouter"
    JPanel reservationPanel = new JPanel();
    reservationPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Aligner les composants sur la même ligne
    JLabel labelReservations = new JLabel("Nombre de réservations à ajouter : ");
    JTextField textReservations = new JTextField(10); // Taille du champ texte
    reservationPanel.add(labelReservations);
    reservationPanel.add(textReservations);

    // Boutons OK et Annuler dans un panneau séparé
    JPanel buttonPanel = new JPanel();
    JButton btnOk = new JButton("OK");
    JButton btnAnnuler = new JButton("Annuler");
    buttonPanel.add(btnOk);
    buttonPanel.add(btnAnnuler);

    // Ajout des composants au JDialog
    dialog.add(labelNumero);
    dialog.add(labelDestination);
    dialog.add(labelDate);
    dialog.add(labelReservationsActuel);
    dialog.add(reservationPanel); // Ajouter le panel de réservation sur une seule ligne
    dialog.add(buttonPanel); // Ajouter le panneau des boutons

    // Action du bouton OK
    btnOk.addActionListener(e -> {
      try {
        // Récupérer les nouvelles valeurs
        int reservations = Integer.parseInt(textReservations.getText());
        if (reservations <= 0) {
          throw new IllegalArgumentException("Le nombre de réservations doit être supérieur à 0.");
        }

        // Mettre à jour le vol sélectionné
        volAReserver.setReservations(reservations + volAReserver.getReservations());

        // Mettre à jour la liste et le fichier JSON
        enregistrerSurFichierJson(listevols);

        // Mettre à jour la table
        updateTableData(listevols);

        // Fermer le JDialog
        dialog.dispose();
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(dialog, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
      } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
      }
    });

    // Action du bouton Annuler
    btnAnnuler.addActionListener(e -> dialog.dispose());

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
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

  // **********************************************************************************************
  // ****************************************** UTILITAIRE
  // ****************************************
  // **********************************************************************************************

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

  public static void enregistrerSurFichierJson(List<Vol> listevols) {
    // Construire le JSON manuellement
    StringBuilder jsonBuilder = new StringBuilder();
    jsonBuilder.append("[\n");

    for (int i = 0; i < listevols.size(); i++) {
      Vol vol = listevols.get(i);
      jsonBuilder.append("  {\n");
      jsonBuilder.append("    \"id\": ").append(vol.getNumero()).append(",\n");
      jsonBuilder.append("    \"destination\": \"").append(vol.getDestination()).append("\",\n");
      jsonBuilder.append("      \"jour\": ").append(vol.getDepart().getJour()).append(",\n");
      jsonBuilder.append("      \"mois\": ").append(vol.getDepart().getMois()).append(",\n");
      jsonBuilder.append("      \"annee\": ").append(vol.getDepart().getAn()).append(",\n");
      jsonBuilder.append("    \"reservations\": ").append(vol.getReservations()).append("\n");
      jsonBuilder.append("  }");

      // Ajouter une virgule après chaque objet sauf le dernier
      if (i < listevols.size() - 1) {
        jsonBuilder.append(",");
      }
      jsonBuilder.append("\n");
    }

    jsonBuilder.append("]");

    // Écrire dans le fichier
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      writer.write(jsonBuilder.toString());
      System.out.println("Données enregistrées avec succès dans le fichier JSON.");
    } catch (IOException e) {
      System.err.println("Erreur lors de l'enregistrement des données : " + e.getMessage());
    }
  }

  private void afficherConfirmationVol(Vol vol) {
    // Créer un JDialog pour la confirmation
    JDialog confirmationDialog = new JDialog(this, "Confirmation", true);
    confirmationDialog.setSize(300, 150);
    confirmationDialog.setLayout(new BorderLayout());

    // Texte de confirmation
    JLabel message = new JLabel(
        "<html>Le vol a été enregistré avec succès !<br>" +
            "Numéro : " + vol.getNumero() + "<br>" +
            "Destination : " + vol.getDestination() + "<br>" +
            "Date : " + vol.getDepart().getJour() + "-" +
            vol.getDepart().getMois() + "-" + vol.getDepart().getAn() + "</html>",
        JLabel.CENTER);
    confirmationDialog.add(message, BorderLayout.CENTER);

    // Afficher la fenêtre au centre de l'application
    confirmationDialog.setLocationRelativeTo(this);

    // Lancer un thread pour fermer automatiquement la fenêtre après 3 secondes
    new Thread(() -> {
      try {
        Thread.sleep(3000); // Pause de 3 secondes
        confirmationDialog.dispose(); // Fermer la fenêtre
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }).start();

    // Rendre la fenêtre visible
    confirmationDialog.setVisible(true);
  }

}
