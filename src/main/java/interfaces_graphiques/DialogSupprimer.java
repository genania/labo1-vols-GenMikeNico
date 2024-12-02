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

        // Configuration de la fenêtre de dialogue
        JDialog dialog = new JDialog(this, "Confirmer la suppression", true);
        dialog.setSize(400, 250); // Taille ajustée pour un affichage clair
        dialog.setLayout(new BorderLayout());

        // Définir une icône pour la fenêtre (en haut à gauche de la barre de titre)
        ImageIcon iconSupprimer = new ImageIcon("src/icone/supprimerAvion.png");
        dialog.setIconImage(iconSupprimer.getImage());

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 182, 193)); // Couleur rose pâle
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // Bordure noire

        // Message de confirmation
        JLabel message = new JLabel("Êtes-vous sûr de vouloir supprimer ce vol ?");
        message.setFont(new Font("Arial", Font.BOLD, 16));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Espacement interne

        mainPanel.add(message, BorderLayout.NORTH);

        // Détails du vol
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 5, 5)); // Grille avec 4 lignes
        detailsPanel.setBackground(new Color(255, 182, 193));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding pour les détails

        JLabel numeroLabel = new JLabel("Numéro : " + volASupprimer.getNumero());
        JLabel destinationLabel = new JLabel("Destination : " + volASupprimer.getDestination());
        JLabel dateLabel = new JLabel("Date : " + volASupprimer.getDepart().getJour() + "-" +
                volASupprimer.getDepart().getMois() + "-" + volASupprimer.getDepart().getAn());
        JLabel categorieLabel = new JLabel("Catégorie : " + volASupprimer.getCategorie());

        // Appliquer une police et un alignement uniforme
        Font detailsFont = new Font("Arial", Font.PLAIN, 14);
        numeroLabel.setFont(detailsFont);
        destinationLabel.setFont(detailsFont);
        dateLabel.setFont(detailsFont);
        categorieLabel.setFont(detailsFont);

        detailsPanel.add(numeroLabel);
        detailsPanel.add(destinationLabel);
        detailsPanel.add(dateLabel);
        detailsPanel.add(categorieLabel);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Boutons Supprimer et Annuler
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 182, 193)); // Couleur rose pâle

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setFont(new Font("Arial", Font.BOLD, 14));
        btnSupprimer.setBackground(Color.RED);
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btnSupprimer.setPreferredSize(new Dimension(120, 30));

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 14));
        btnAnnuler.setBackground(new Color(159, 232, 159)); // Vert pâle
        btnAnnuler.setForeground(Color.BLACK);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        btnAnnuler.setPreferredSize(new Dimension(120, 30));

        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnAnnuler);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Actions des boutons
        btnSupprimer.addActionListener(e -> {
            // Supprimer le vol
            listeVols.remove(modelRow);

            // Mettre à jour le fichier JSON
            AccesDonnees.enregistrerListeVols(listeVols);

            // Afficher une belle confirmation après suppression
            afficherConfirmationSuppression(volASupprimer);

            // Fermer le JDialog
            dialog.dispose();
        });

        btnAnnuler.addActionListener(e -> dialog.dispose());

        // Ajouter le panneau principal au dialogue
        dialog.add(mainPanel);

        // Centrer et afficher la fenêtre
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void afficherConfirmationSuppression(Vol vol) {
        JDialog confirmationDialog = new JDialog(this, "Suppression confirmée", true);
        confirmationDialog.setSize(400, 200); // Taille ajustée
        confirmationDialog.setLayout(new BorderLayout());

        // Définir une icône pour la fenêtre
        ImageIcon icon = new ImageIcon("src/icone/supprimerAvion.png");
        confirmationDialog.setIconImage(icon.getImage());

        // Contenu de la fenêtre de confirmation
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel successMessage = new JLabel("<html>Le vol suivant a été supprimé avec succès :<br><br>" +
                "<b>Numéro : </b>" + vol.getNumero() + "<br>" +
                "<b>Destination : </b>" + vol.getDestination() + "<br>" +
                "<b>Date : </b>" + vol.getDepart().getJour() + "-" +
                vol.getDepart().getMois() + "-" + vol.getDepart().getAn() + "<br>" +
                "<b>Catégorie : </b>" + vol.getCategorie() + "</html>");
        successMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        successMessage.setHorizontalAlignment(SwingConstants.CENTER);
        successMessage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPanel.add(successMessage, BorderLayout.CENTER);

        // Bouton OK
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(new Color(159, 232, 159)); // Vert pâle
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        okButton.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(okButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton OK
        okButton.addActionListener(e -> confirmationDialog.dispose());

        confirmationDialog.add(contentPanel);

        // Centrer et afficher la fenêtre
        confirmationDialog.setLocationRelativeTo(this);
        confirmationDialog.setVisible(true);
    }
}
