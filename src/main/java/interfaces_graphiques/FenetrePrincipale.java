package interfaces_graphiques;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import modele.Vol;
import service.AccesDonnees;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import utilitaires.EditableTableModel;

public class FenetrePrincipale extends JFrame {
    private JTable table;
    private EditableTableModel tableModel;
    private static Point size = new Point(1200, 800);
    public List<Vol> listevols;

    public FenetrePrincipale() {
        listevols = AccesDonnees.obtenirListeVols();

        setTitle("Gestion des vols");
        setSize(size.x, size.y);

        // Remplacer l'icône Java par l'icône d'avion
        ImageIcon iconAvion = new ImageIcon("src/icone/avion.png");
        Image scaledImage = iconAvion.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        setIconImage(scaledImage);
        ImageIcon scaledIconAvion = new ImageIcon(scaledImage);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialisation du modèle de table avec les colonnes demandées
        tableModel = new EditableTableModel(
                new Object[] { "Numero", "Destination", "Départ", "Réservations", "Catégorie" },
                0);
        table = new JTable(tableModel);

        // Personnalisation des en-têtes du tableau
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 14));
        tableHeader.setForeground(Color.BLACK);
        tableHeader.setBackground(new Color(200, 200, 200));

        // Centrer les strings et les ints et le tableau
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        table.setDefaultRenderer(Object.class, renderer);
        table.setDefaultRenderer(Integer.class, renderer);

        // Rendre le tableau triable
        table.setAutoCreateRowSorter(true);
        TableRowSorter<EditableTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Chargement des icônes
        ImageIcon iconAjouterAvion = new ImageIcon("src/icone/ajouterAvion.png");
        ImageIcon iconModifier = new ImageIcon("src/icone/modifierAvion.png");
        ImageIcon iconReserver = new ImageIcon("src/icone/reserverAvion.png");
        ImageIcon iconSupprimer = new ImageIcon("src/icone/supprimerAvion.png");
        ImageIcon iconRechercher = new ImageIcon("src/icone/rechercherAvion.png"); // Icône pour le bouton Rechercher

        // Redimensionner les icônes si nécessaire
        Image scaledImageAjouter = iconAjouterAvion.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledImageModifier = iconModifier.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledImageReserver = iconReserver.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledImageSupprimer = iconSupprimer.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledImageRechercher = iconRechercher.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        ImageIcon scaledIconAjouter = new ImageIcon(scaledImageAjouter);
        ImageIcon scaledIconModifier = new ImageIcon(scaledImageModifier);
        ImageIcon scaledIconReserver = new ImageIcon(scaledImageReserver);
        ImageIcon scaledIconSupprimer = new ImageIcon(scaledImageSupprimer);
        ImageIcon scaledIconRechercher = new ImageIcon(scaledImageRechercher);

        // Boutons avec actions
        JButton btnLister = createButton("Lister", scaledIconAvion, new Color(173, 216, 230),
                e -> updateTableData(listevols));
        JButton btnAjouter = createButton("Ajouter un vol", scaledIconAjouter, new Color(159, 232, 159), // Vert pâle
                                                                                                         // (Add Flight)
                e -> {
                    DialogAjouter dialogAjouter = new DialogAjouter(listevols);
                    updateTableData(listevols);
                });
        JButton btnModifier = createButton("Modifier un vol", scaledIconModifier, new Color(255, 250, 205), // Jaune
                                                                                                            // pâle
                                                                                                            // (Edit
                                                                                                            // Flight)
                e -> {
                    DialogModifier dialogModifier = new DialogModifier(listevols, table);
                    updateTableData(listevols);
                });
        JButton btnSupprimer = createButton("Supprimer un vol", scaledIconSupprimer, new Color(255, 182, 193), // Rose
                                                                                                               // pâle
                                                                                                               // (Delete
                                                                                                               // Flight)
                e -> {
                    DialogSupprimer dialogSupprimer = new DialogSupprimer(listevols, table);
                    updateTableData(listevols);
                });
        JButton btnReservation = createButton("Réserver un vol", scaledIconReserver, new Color(178, 255, 194), // Vert
                                                                                                               // pâle
                                                                                                               // (Add
                                                                                                               // Passenger)
                e -> {
                    DialogReserver dialogReserver = new DialogReserver(listevols, table);
                    updateTableData(listevols);
                });

        // Bouton pour rechercher un vol
        JButton btnRechercher = createButton("Rechercher", scaledIconRechercher, new Color(135, 206, 235), // Bleu clair
                                                                                                           // (Search
                                                                                                           // Flight)
                e -> {
                    DialogRechercher dialogRechercher = new DialogRechercher(listevols, table);
                    dialogRechercher.setVisible(true); // Afficher la boîte de dialogue
                });

        // Mise en page de l'interface
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        buttonPanel.add(btnLister);
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnReservation);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnRechercher);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel);

        updateTableData(listevols); // Charger les données initiales
    }

    private JButton createButton(String text, Icon icon, Color bgColor, ActionListener actionListener) {
        JButton button = new JButton(text, icon);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setPreferredSize(new Dimension(150, 80));
        button.addActionListener(actionListener);
        return button;
    }

    public void updateTableData(List<Vol> vols) {
        tableModel.setRowCount(0);
        table.setRowHeight(80);

        for (Vol vol : vols) {
            tableModel.addRow(new Object[] {
                    vol.getNumero(),
                    vol.getDestination(),
                    vol.getDepart(),
                    vol.getReservations(),
                    vol.getCategorie(),
            });
        }
    }
}
