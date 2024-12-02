package interfaces_graphiques;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import modele.DateVol;
import modele.Vol;
import service.AccesDonnees;

public class DialogModifier extends JDialog {
    public DialogModifier(List<Vol> listeVols, JTable tableFenetrePrincipale) {
        // Vérifier si une ligne est sélectionnée
        int selectedRow = tableFenetrePrincipale.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vol à modifier.", "Erreur",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupérer l'index du vol sélectionné dans la liste
        int modelRow = tableFenetrePrincipale.convertRowIndexToModel(selectedRow);
        Vol volAModifier = listeVols.get(modelRow);

        // Configuration de la fenêtre de dialogue
        JDialog dialog = new JDialog(this, "Modifier un vol", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(255, 250, 205)); // Jaune pâle (Edit Flight)

        // Définir l'icône dans la barre de titre
        ImageIcon iconModifier = new ImageIcon("src/icone/modifierAvion.png");
        dialog.setIconImage(iconModifier.getImage());

        // Ajouter une bordure noire autour de la fenêtre
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        // Panneau principal pour le formulaire
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle (Edit Flight)

        // Champs pré-remplis
        JLabel labelNumero = new JLabel("Numéro de vol : ");
        JTextField textNumero = new JTextField(String.valueOf(volAModifier.getNumero()));

        JLabel labelDestination = new JLabel("Destination : ");
        JTextField textDestination = new JTextField(volAModifier.getDestination());

        JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : ");
        JTextField textDate = new JTextField(
                volAModifier.getDepart().getJour() + "-" +
                        volAModifier.getDepart().getMois() + "-" +
                        volAModifier.getDepart().getAn());

        JLabel labelMaxReservations = new JLabel("Maximum de réservations : ");
        JTextField textMaxReservations = new JTextField(String.valueOf(volAModifier.getMaxReservations()));

        JLabel labelReservations = new JLabel("Réservations : ");
        JTextField textReservations = new JTextField(String.valueOf(volAModifier.getReservations()));

        // Afficher la catégorie de vol sans pouvoir la modifier
        JLabel labelCategorie = new JLabel("Catégorie de vol : ");
        JLabel labelCategorieValue = new JLabel(volAModifier.getCategorie().toString());

        // Ajouter les champs au formulaire
        formPanel.add(labelNumero);
        formPanel.add(textNumero);
        formPanel.add(labelDestination);
        formPanel.add(textDestination);
        formPanel.add(labelDate);
        formPanel.add(textDate);
        formPanel.add(labelMaxReservations);
        formPanel.add(textMaxReservations);
        formPanel.add(labelReservations);
        formPanel.add(textReservations);
        formPanel.add(labelCategorie);
        formPanel.add(labelCategorieValue);

        // Boutons Confirmer et Annuler
        JButton btnConfirmer = new JButton("Confirmer");
        JButton btnAnnuler = new JButton("Annuler");

        // Personnalisation des boutons
        btnConfirmer.setBackground(new Color(159, 232, 159)); // Vert pâle pour confirmer
        btnConfirmer.setForeground(Color.BLACK);
        btnConfirmer.setFocusPainted(false);
        btnConfirmer.setFont(new Font("Arial", Font.BOLD, 12));
        btnConfirmer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnConfirmer.setPreferredSize(new Dimension(150, 40)); // Hauteur des boutons augmentée

        btnAnnuler.setBackground(Color.RED); // Rouge pour annuler
        btnAnnuler.setForeground(Color.BLACK);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 12));
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnAnnuler.setPreferredSize(new Dimension(150, 40)); // Hauteur des boutons augmentée

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle (Edit Flight)
        buttonPanel.add(btnConfirmer);
        buttonPanel.add(btnAnnuler);

        // Ajout des panneaux à la fenêtre
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Confirmer
        btnConfirmer.addActionListener(e -> {
            try {
                // Récupérer les nouvelles valeurs
                int numero = Integer.parseInt(textNumero.getText());
                String destination = textDestination.getText();
                String dateStr = textDate.getText();
                int maxReservations = Integer.parseInt(textMaxReservations.getText());
                int reservations = Integer.parseInt(textReservations.getText());

                // Valider que le numéro est unique
                boolean numeroExistant = listeVols.stream()
                        .anyMatch(vol -> vol.getNumero() == numero && vol != volAModifier);
                if (numeroExistant) {
                    throw new IllegalArgumentException("Un vol avec ce numéro existe déjà.");
                }

                DateVol date = new DateVol(dateStr);

                // Mettre à jour le vol sélectionné
                volAModifier.setNumero(numero);
                volAModifier.setDestination(destination);
                volAModifier.setDepart(date);
                volAModifier.setMaxReservations(maxReservations);
                volAModifier.setReservations(reservations);

                // Mettre à jour la liste et le fichier JSON
                AccesDonnees.enregistrerListeVols(listeVols);

                // Afficher la fenêtre de confirmation
                afficherConfirmationVol(volAModifier);

                // Fermer le JDialog
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer un numéro valide.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void afficherConfirmationVol(Vol vol) {
        JDialog confirmationDialog = new JDialog(this, "Confirmation", true);
        confirmationDialog.setSize(500, 300); // Taille augmentée pour afficher tout le contenu
        confirmationDialog.setLayout(new BorderLayout());

        // Définir une icône pour la fenêtre (en haut à gauche de la barre de titre)
        ImageIcon icon = new ImageIcon("src/icone/modifierAvion.png");
        confirmationDialog.setIconImage(icon.getImage());

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); // Bordure noire

        // Message principal
        JLabel message = new JLabel("<html><div style='text-align: center;'>"
                + "Les modifications ont été enregistrées avec succès !</div></html>");
        message.setFont(new Font("Arial", Font.BOLD, 16));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Ajouter du padding
        mainPanel.add(message, BorderLayout.NORTH);

        // Détails du vol
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        detailsPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Espacement interne

        JLabel numeroLabel = new JLabel("Numéro : " + vol.getNumero());
        JLabel destinationLabel = new JLabel("Destination : " + vol.getDestination());
        JLabel dateLabel = new JLabel("Date : " + vol.getDepart().getJour() + "-" + vol.getDepart().getMois() + "-"
                + vol.getDepart().getAn());
        JLabel categorieLabel = new JLabel("Catégorie : " + vol.getCategorie());

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

        // Bouton OK
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(new Color(159, 232, 159)); // Vert pâle
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        okButton.setPreferredSize(new Dimension(100, 40));

        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton OK
        okButton.addActionListener(e -> confirmationDialog.dispose());

        confirmationDialog.add(mainPanel);
        confirmationDialog.setLocationRelativeTo(this);
        confirmationDialog.setVisible(true);
    }

}
