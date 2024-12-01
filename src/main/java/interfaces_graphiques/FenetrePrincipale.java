package interfaces_graphiques;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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
        tableModel = new EditableTableModel(
                new Object[] { "numero", "destination", "depart", "reservations", "categorie" },
                0);
        table = new JTable(tableModel);

        // Centrer les strings et les ints et le tableau
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        table.setDefaultRenderer(Object.class, renderer);
        table.setDefaultRenderer(Integer.class, renderer);

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
                updateTableData(listevols);
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
                DialogAjouter dialogAjouter = new DialogAjouter(listevols);

                // Mettre à jour la table pour afficher le nouveau vol
                updateTableData(listevols);
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
                DialogModifier dialogModifier = new DialogModifier(listevols, table);

                // Mettre à jour la table
                updateTableData(listevols);
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
                DialogSupprimer dialogSupprimer = new DialogSupprimer(listevols, table);

                // Mettre à jour la table
                updateTableData(listevols);
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
                DialogReserver dialogReserver = new DialogReserver(listevols, table);

                // Mettre à jour la table
                updateTableData(listevols);
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
