package interfaces_graphiques;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import modele.Vol;
import service.AccesDonnees;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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

        setExtendedState(JFrame.NORMAL);

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
        tableModel = new EditableTableModel(
                new Object[] { "Numero", "Destination", "Départ", "Réservations", "Catégorie" },
                0);
        table = new JTable(tableModel);
        // Personnalisation des en-têtes du tableau via JTableHeader
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 14)); // Police en gras et taille 14
        tableHeader.setForeground(Color.BLACK); // Couleur du texte
        tableHeader.setBackground(new Color(200, 200, 200)); // Fond gris clair pour les en-têtes

        // Centrer les strings et les ints et le tableau
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        table.setDefaultRenderer(Object.class, renderer);
        table.setDefaultRenderer(Integer.class, renderer);

        // Rendre le tableau triable
        table.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Chargement des icône
        ImageIcon iconAvion = new ImageIcon("src/icone/avion.jpg");
        ImageIcon iconAjouterAvion = new ImageIcon("src/icone/ajouterAvion.jpg");
        ImageIcon iconModifier = new ImageIcon("src/icone/modifierAvion.png");
        ImageIcon iconReserver = new ImageIcon("src/icone/reserverAvion.png");
        ImageIcon iconSupprimer = new ImageIcon("src/icone/supprimerAvion.png");

        // Redimensionnez l'icône si nécessaire
        Image scaledImage = iconAvion.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIconAvion = new ImageIcon(scaledImage);
        Image scaledImageUn = iconAjouterAvion.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scalediconAjouterAvion = new ImageIcon(scaledImageUn);
        Image scaledImageModifier = iconModifier.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledImageReserver = iconReserver.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledImageSupprimer = iconSupprimer.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIconModifier = new ImageIcon(scaledImageModifier);
        ImageIcon scaledIconReserver = new ImageIcon(scaledImageReserver);
        ImageIcon scaledIconSupprimer = new ImageIcon(scaledImageSupprimer);

        // Création d'un JLabel pour afficher l'icône
        JLabel lblIconAvion = new JLabel(scaledIconAvion);
        JLabel lblIconAjouterAvion = new JLabel(scalediconAjouterAvion);
        JLabel lblIconModifier = new JLabel(scaledIconModifier);
        JLabel lblIconReserver = new JLabel(scaledIconReserver);
        JLabel lblIconSupprimer = new JLabel(scaledIconSupprimer);

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
                updateTableData(listevols);
            }
        });

        // Bouton pour ajouter un vol
        JButton btnAjouter = new JButton("Ajouter un vol");
        btnAjouter.setBackground(Color.GREEN);
        btnAjouter.setForeground(Color.black);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 12));
        btnAjouter.setMargin(new Insets(5, 5, 5, 5));
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogAjouter dialogAjouter = new DialogAjouter(listevols);
                updateTableData(listevols); // Mettre à jour la table pour afficher le nouveau vol
            }
        });

        // Bouton pour modifier un vol
        JButton btnModifier = new JButton("Modifier un vol");
        btnModifier.setBackground(Color.YELLOW);
        btnModifier.setForeground(Color.BLACK);
        btnModifier.setFocusPainted(false);
        btnModifier.setFont(new Font("Arial", Font.BOLD, 12));
        btnModifier.setMargin(new Insets(5, 5, 5, 5));
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogModifier dialogModifier = new DialogModifier(listevols, table);
                updateTableData(listevols); // Mettre à jour la table
            }
        });

        // Bouton pour supprimer un vol
        JButton btnSupprimer = new JButton("Supprimer un vol");
        btnSupprimer.setBackground(Color.RED);
        btnSupprimer.setForeground(Color.BLACK);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setFont(new Font("Arial", Font.BOLD, 12));
        btnSupprimer.setMargin(new Insets(5, 5, 5, 5));
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogSupprimer dialogSupprimer = new DialogSupprimer(listevols, table);
                updateTableData(listevols); // Mettre à jour la table
            }
        });

        // Bouton pour réserver un vol
        JButton btnReservation = new JButton("Réservation d'un vol");
        btnReservation.setBackground(new Color(173, 216, 230)); // Bleu pâle (RGB)
                                                                // btnReservation.setForeground(Color.black);
        btnReservation.setFocusPainted(false);
        btnReservation.setFont(new Font("Arial", Font.BOLD, 12));
        btnReservation.setMargin(new Insets(5, 5, 5, 5));
        btnReservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogReserver dialogReserver = new DialogReserver(listevols, table);
                updateTableData(listevols); // Mettre à jour la table
            }
        });

        // Mise en page de l'interface avec le bouton en haut
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();

        // Ajout de l'icône et des boutons au panel
        buttonPanel.add(lblIconAvion); // Ajout de l'icône
        buttonPanel.add(btnLister);
        buttonPanel.add(lblIconAjouterAvion);
        buttonPanel.add(btnAjouter);
        buttonPanel.add(lblIconModifier);
        buttonPanel.add(btnModifier);
        buttonPanel.add(lblIconSupprimer);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(lblIconReserver);
        buttonPanel.add(btnReservation);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel);
    }

    // Méthode pour ajouter les données des vols dans le modèle de la table
    public void updateTableData(List<Vol> vols) {
        tableModel.setRowCount(0); // Vider les données actuelles du tableau
        table.setRowHeight(80); // Augmentez la valeur pour une plus grande hauteur d'image si nécessaire

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
