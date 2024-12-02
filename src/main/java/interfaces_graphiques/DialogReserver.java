package interfaces_graphiques;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import modele.Vol;
import service.AccesDonnees;

public class DialogReserver extends JDialog {
    public DialogReserver(List<Vol> listeVols, JTable tableFenetrePrincipale) {
        // Vérifier si une ligne est sélectionnée
        int selectedRow = tableFenetrePrincipale.getSelectedRow();
        if (selectedRow == -1) {
            afficherFenetreErreur("Veuillez sélectionner un vol à réserver.");
            return;
        }

        // Récupérer l'index du vol sélectionné dans la liste
        int modelRow = tableFenetrePrincipale.convertRowIndexToModel(selectedRow);
        Vol volAReserver = listeVols.get(modelRow);

        // Configuration de la fenêtre de dialogue
        JDialog dialog = new JDialog(this, "Réserver un vol", true);
        dialog.setSize(450, 400); // Hauteur ajustée
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(178, 255, 194)); // Vert pâle
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // Définir une icône pour la fenêtre
        ImageIcon iconReserver = new ImageIcon("src/icone/reserverAvion.png");
        dialog.setIconImage(iconReserver.getImage());

        // Panneau principal pour les champs
        JPanel mainPanel = new JPanel(new GridLayout(8, 1, 10, 10)); // Espacement vertical réduit
        mainPanel.setBackground(new Color(178, 255, 194));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Champs pré-remplis
        JLabel labelNumero = new JLabel("Numéro de vol : " + volAReserver.getNumero());
        JLabel labelDestination = new JLabel("Destination : " + volAReserver.getDestination());
        JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : " + volAReserver.getDepart().getJour() + "-" +
                volAReserver.getDepart().getMois() + "-" + volAReserver.getDepart().getAn());
        JLabel labelCategorie = new JLabel("Catégorie : " + volAReserver.getCategorie().toString());
        JLabel labelMaxReservations = new JLabel("Maximum de réservations : " + volAReserver.getMaxReservations());
        JLabel labelReservationsActuelles = new JLabel("Réservations actuelles : " + volAReserver.getReservations());
        JLabel labelPlacesDisponibles = new JLabel("Places disponibles : " +
                (volAReserver.getMaxReservations() - volAReserver.getReservations()));

        Font detailFont = new Font("Arial", Font.PLAIN, 14);
        labelNumero.setFont(detailFont);
        labelDestination.setFont(detailFont);
        labelDate.setFont(detailFont);
        labelCategorie.setFont(detailFont);
        labelMaxReservations.setFont(detailFont);
        labelReservationsActuelles.setFont(detailFont);
        labelPlacesDisponibles.setFont(detailFont);

        // Champ pour les nouvelles réservations
        JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reservationPanel.setBackground(new Color(178, 255, 194));
        JLabel labelReservations = new JLabel("Nombre de réservations à ajouter : ");
        JTextField textReservations = new JTextField(10);
        reservationPanel.add(labelReservations);
        reservationPanel.add(textReservations);

        // Ajouter les champs au panneau principal
        mainPanel.add(labelNumero);
        mainPanel.add(labelDestination);
        mainPanel.add(labelDate);
        mainPanel.add(labelCategorie);
        mainPanel.add(labelMaxReservations);
        mainPanel.add(labelReservationsActuelles);
        mainPanel.add(labelPlacesDisponibles);
        mainPanel.add(reservationPanel);

        // Boutons Confirmer et Annuler
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // Boutons côte à côte
        buttonPanel.setBackground(new Color(178, 255, 194));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnConfirmer = new JButton("Confirmer");
        btnConfirmer.setFont(new Font("Arial", Font.BOLD, 16));
        btnConfirmer.setBackground(new Color(159, 232, 159));
        btnConfirmer.setForeground(Color.BLACK);
        btnConfirmer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnConfirmer.setPreferredSize(new Dimension(200, 60)); // Largeur et hauteur ajustées

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 16));
        btnAnnuler.setBackground(Color.RED);
        btnAnnuler.setForeground(Color.WHITE);
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnAnnuler.setPreferredSize(new Dimension(200, 60)); // Largeur et hauteur ajustées

        buttonPanel.add(btnConfirmer);
        buttonPanel.add(btnAnnuler);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Confirmer
        btnConfirmer.addActionListener(e -> {
            try {
                int reservations = Integer.parseInt(textReservations.getText());
                if (reservations <= 0) {
                    afficherFenetreErreur("Le nombre de réservations doit être supérieur à 0.");
                    return;
                }
                if (reservations + volAReserver.getReservations() > volAReserver.getMaxReservations()) {
                    afficherFenetreErreur("Le nombre de places disponibles est insuffisant.");
                    return;
                }

                // Ajouter les réservations
                volAReserver.setReservations(volAReserver.getReservations() + reservations);

                // Sauvegarder les données
                AccesDonnees.enregistrerListeVols(listeVols);

                // Afficher une belle fenêtre de confirmation
                afficherConfirmationReservation(volAReserver, reservations);

                // Fermer le dialogue
                dialog.dispose();
            } catch (NumberFormatException ex) {
                afficherFenetreErreur("Veuillez entrer un nombre valide.");
            }
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void afficherFenetreErreur(String messageErreur) {
        JDialog erreurDialog = new JDialog(this, "Erreur", true);
        erreurDialog.setSize(400, 200);
        erreurDialog.setLayout(new BorderLayout());
        erreurDialog.getContentPane().setBackground(new Color(255, 182, 193));
        erreurDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        ImageIcon icon = new ImageIcon("src/icone/erreur.png");
        erreurDialog.setIconImage(icon.getImage());

        JLabel message = new JLabel("<html><div style='text-align: center;'>" + messageErreur + "</div></html>");
        message.setFont(new Font("Arial", Font.BOLD, 14));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(Color.RED);
        okButton.setForeground(Color.WHITE);
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.addActionListener(e -> erreurDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 182, 193));
        buttonPanel.add(okButton);

        erreurDialog.add(message, BorderLayout.CENTER);
        erreurDialog.add(buttonPanel, BorderLayout.SOUTH);
        erreurDialog.setLocationRelativeTo(this);
        erreurDialog.setVisible(true);
    }

    private void afficherConfirmationReservation(Vol vol, int reservationsAjoutees) {
        JDialog confirmationDialog = new JDialog(this, "Confirmation de la réservation", true);
        confirmationDialog.setSize(500, 300);
        confirmationDialog.setLayout(new BorderLayout());
        confirmationDialog.getContentPane().setBackground(new Color(255, 250, 205));
        confirmationDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        ImageIcon icon = new ImageIcon("src/icone/reserverAvion.png");
        confirmationDialog.setIconImage(icon.getImage());

        JLabel successMessage = new JLabel("<html><div style='text-align: center;'>"
                + "La réservation a été effectuée avec succès !<br><br>"
                + "<b>Numéro :</b> " + vol.getNumero() + "<br>"
                + "<b>Destination :</b> " + vol.getDestination() + "<br>"
                + "<b>Date :</b> " + vol.getDepart().getJour() + "-" + vol.getDepart().getMois() + "-"
                + vol.getDepart().getAn() + "<br>"
                + "<b>Nombre de réservations ajoutées :</b> " + reservationsAjoutees + "<br>"
                + "<b>Réservations totales :</b> " + vol.getReservations() + "</div></html>");
        successMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        successMessage.setHorizontalAlignment(SwingConstants.CENTER);
        successMessage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        confirmationDialog.add(successMessage, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(new Color(159, 232, 159));
        okButton.setForeground(Color.BLACK);
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.addActionListener(e -> confirmationDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 250, 205));
        buttonPanel.add(okButton);

        confirmationDialog.add(buttonPanel, BorderLayout.SOUTH);
        confirmationDialog.setLocationRelativeTo(this);
        confirmationDialog.setVisible(true);
    }
}
