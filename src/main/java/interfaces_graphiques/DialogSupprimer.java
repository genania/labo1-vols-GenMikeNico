package interfaces_graphiques;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import modele.Vol;
import service.AccesDonnees;

public class DialogSupprimer extends JDialog {
    public DialogSupprimer(List<Vol> listeVols, JTable tableFenetrePrincipale) {
        // Vérifier si une ligne est sélectionnée
        int selectedRow = tableFenetrePrincipale.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vol à supprimer.", "Erreur",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupérer l'index du vol dans la liste
        int modelRow = tableFenetrePrincipale.convertRowIndexToModel(selectedRow);
        Vol volASupprimer = listeVols.get(modelRow);

        // Créer un JDialog pour la confirmation
        JDialog dialog = new JDialog(this, "Confirmer la suppression", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(255, 182, 193)); // Fond rose pâle (Delete Flight)

        // Ajouter une bordure noire autour de la fenêtre complète
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // Charger l'icône pour le titre
        ImageIcon iconSupprimer = new ImageIcon("src/icone/supprimerAvion.png");
        Image scaledImage = iconSupprimer.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        dialog.setIconImage(scaledImage);

        // Panel principal pour les champs
        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        mainPanel.setBackground(new Color(255, 182, 193)); // Fond rose pâle (Delete Flight)

        // Afficher les informations du vol
        JLabel labelNumero = new JLabel("Numéro de vol : " + volASupprimer.getNumero());
        JLabel labelDestination = new JLabel("Destination : " + volASupprimer.getDestination());
        JLabel labelDate = new JLabel("Date de départ : " + volASupprimer.getDepart().getJour() + "-" +
                volASupprimer.getDepart().getMois() + "-" + volASupprimer.getDepart().getAn());
        JLabel labelReservations = new JLabel("Nombre de réservations actuelles : " + volASupprimer.getReservations());
        JLabel labelCategorie = new JLabel("Catégorie : " + volASupprimer.getCategorie().toString());

        mainPanel.add(labelNumero);
        mainPanel.add(labelDestination);
        mainPanel.add(labelDate);
        mainPanel.add(labelReservations);
        mainPanel.add(labelCategorie);

        // Boutons Supprimer et Annuler
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(255, 182, 193)); // Fond rose pâle (Delete Flight)

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(Color.RED); // Rouge pour indiquer la suppression
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setFont(new Font("Arial", Font.BOLD, 12));
        btnSupprimer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnSupprimer.setPreferredSize(new Dimension(100, 40)); // Ajuster la hauteur

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setBackground(new Color(159, 232, 159)); // Vert pâle pour annuler
        btnAnnuler.setForeground(Color.BLACK);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 12));
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnAnnuler.setPreferredSize(new Dimension(100, 40)); // Ajuster la hauteur

        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnAnnuler);

        // Ajouter les composants au JDialog
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Supprimer
        btnSupprimer.addActionListener(e -> {
            // Supprimer le vol
            listeVols.remove(modelRow);

            // Mettre à jour le fichier JSON
            AccesDonnees.enregistrerListeVols(listeVols);

            // Afficher un message de confirmation
            JOptionPane.showMessageDialog(dialog, "Le vol a été supprimé avec succès.", "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            // Fermer le JDialog
            dialog.dispose();
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dialog.dispose());

        // Centrer et afficher la fenêtre
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
