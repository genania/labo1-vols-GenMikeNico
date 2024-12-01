package interfaces_graphiques;

import modele.Vol;
import utilitaires.EditableTableModel;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.KeyAdapter;

public class DialogRechercher extends JDialog {
    public DialogRechercher(List<Vol> listeVols, JTable table) {
        setTitle("Rechercher un vol");
        setSize(400, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Ajouter une bordure noire autour de toute la fenêtre
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        // Ajouter l'icône de la fenêtre
        ImageIcon iconRechercher = new ImageIcon("src/icone/rechercherAvion.png");
        Image scaledImage = iconRechercher.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        setIconImage(scaledImage);

        // Panel principal pour les critères
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 lignes pour les critères
        mainPanel.setBackground(new Color(135, 206, 235)); // Fond bleu clair (Search Flight)
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes pour espacer le contenu

        JLabel instructionLabel = new JLabel("Cochez les critères à rechercher :", JLabel.LEFT);
        instructionLabel.setBackground(new Color(135, 206, 235)); // Bleu clair (Search Flight)
        instructionLabel.setOpaque(true); // Nécessaire pour voir la couleur de fond
        mainPanel.add(instructionLabel);

        // Panel pour les cases à cocher
        JPanel criteriaPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        criteriaPanel.setBackground(new Color(135, 206, 235)); // Fond bleu clair (Search Flight)
        JCheckBox checkNumero = new JCheckBox("Numéro");
        checkNumero.setBackground(new Color(135, 206, 235)); // Bleu clair
        checkNumero.setOpaque(true);
        JCheckBox checkDestination = new JCheckBox("Destination");
        checkDestination.setBackground(new Color(135, 206, 235)); // Bleu clair
        checkDestination.setOpaque(true);
        JCheckBox checkCategorie = new JCheckBox("Catégorie");
        checkCategorie.setBackground(new Color(135, 206, 235)); // Bleu clair
        checkCategorie.setOpaque(true);

        criteriaPanel.add(checkNumero);
        criteriaPanel.add(checkDestination);
        criteriaPanel.add(checkCategorie);
        mainPanel.add(criteriaPanel);

        // Champ de saisie
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(135, 206, 235)); // Fond bleu clair (Search Flight)
        searchPanel.add(new JLabel("Valeur à rechercher :"), BorderLayout.NORTH);
        JTextField textRecherche = new JTextField();
        searchPanel.add(textRecherche, BorderLayout.CENTER);

        mainPanel.add(searchPanel);

        // Boutons "Rechercher" et "Annuler"
        JButton btnOk = new JButton("Rechercher");
        btnOk.setBackground(new Color(159, 232, 159)); // Vert pâle pour confirmer
        btnOk.setForeground(Color.BLACK);
        btnOk.setFocusPainted(false);
        btnOk.setFont(new Font("Arial", Font.BOLD, 14));
        btnOk.setPreferredSize(new Dimension(120, 50));
        btnOk.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setBackground(Color.RED); // Rouge pour annuler
        btnAnnuler.setForeground(Color.WHITE);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 14));
        btnAnnuler.setPreferredSize(new Dimension(120, 50));
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(135, 206, 235)); // Fond bleu clair (Search Flight)
        buttonPanel.add(btnOk);
        buttonPanel.add(btnAnnuler);

        // Ajouter les panels à la fenêtre
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Rechercher
        btnOk.addActionListener(e -> {
            String valeurRecherche = textRecherche.getText().trim();

            if (valeurRecherche.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur à rechercher.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Appliquer les filtres sélectionnés
            List<Vol> resultats = listeVols.stream()
                    .filter(vol -> {
                        boolean match = false;
                        if (checkNumero.isSelected()) {
                            match |= String.valueOf(vol.getNumero()).contains(valeurRecherche);
                        }
                        if (checkDestination.isSelected()) {
                            match |= vol.getDestination().toLowerCase().contains(valeurRecherche.toLowerCase());
                        }
                        if (checkCategorie.isSelected()) {
                            match |= vol.getCategorie().toString().toLowerCase()
                                    .contains(valeurRecherche.toLowerCase());
                        }
                        return match;
                    })
                    .collect(Collectors.toList());

            // Afficher les résultats dans le tableau
            if (resultats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun vol trouvé.", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                EditableTableModel tableModel = (EditableTableModel) table.getModel();
                tableModel.setRowCount(0); // Vider les données actuelles

                for (Vol vol : resultats) {
                    tableModel.addRow(new Object[] {
                            vol.getNumero(),
                            vol.getDestination(),
                            vol.getDepart(),
                            vol.getReservations(),
                            vol.getCategorie()
                    });
                }
                dispose(); // Fermer la fenêtre
            }
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dispose());

        // Ajouter un KeyListener pour détecter l'appui sur la touche Entrée
        textRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Appuyer sur Entrée exécute l'action du bouton Rechercher
                    btnOk.doClick(); // Cela simule un clic sur le bouton Rechercher
                }
            }
        });

        setVisible(true);
    }
}
