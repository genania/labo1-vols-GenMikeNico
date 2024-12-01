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
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vol à réserver.", "Erreur",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupérer l'index du vol sélectionné dans la liste
        int modelRow = tableFenetrePrincipale.convertRowIndexToModel(selectedRow);
        Vol volAReserver = listeVols.get(modelRow);

        // Ouvrir un JDialog pré-rempli avec les informations du vol
        JDialog dialog = new JDialog(this, "Réserver un vol", true);
        dialog.setSize(400, 430);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(178, 255, 194)); // Vert pâle (Add Passenger)

        // Ajouter une bordure noire autour de la fenêtre complète
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // Charger l'icône pour le titre
        ImageIcon iconReserver = new ImageIcon("src/icone/reserverAvion.png");
        Image scaledImage = iconReserver.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        dialog.setIconImage(scaledImage);

        // Panel principal pour les champs
        JPanel mainPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        mainPanel.setBackground(new Color(178, 255, 194)); // Vert pâle (Add Passenger)

        // Champs pré-remplis
        JLabel labelNumero = new JLabel("Numéro de vol : " + volAReserver.getNumero());
        JLabel labelDestination = new JLabel("Destination : " + volAReserver.getDestination());
        JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : " + volAReserver.getDepart().getJour() + "-" +
                volAReserver.getDepart().getMois() + "-" + volAReserver.getDepart().getAn());
        JLabel labelMaxReservations = new JLabel("Maximum de réservation : " + volAReserver.getMaxReservations());
        JLabel labelReservationsActuel = new JLabel("Réservations actuelles : " + volAReserver.getReservations());
        JLabel labelCategorie = new JLabel("Catégorie : " + volAReserver.getCategorie().toString());

        // Panel pour la ligne "Nombre de réservations à ajouter"
        JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reservationPanel.setBackground(new Color(178, 255, 194)); // Vert pâle (Add Passenger)
        JLabel labelReservations = new JLabel("Nombre de réservations à ajouter : ");
        JTextField textReservations = new JTextField(10);
        reservationPanel.add(labelReservations);
        reservationPanel.add(textReservations);

        mainPanel.add(labelNumero);
        mainPanel.add(labelDestination);
        mainPanel.add(labelDate);
        mainPanel.add(labelMaxReservations);
        mainPanel.add(labelReservationsActuel);
        mainPanel.add(labelCategorie);
        mainPanel.add(reservationPanel);

        // Boutons Confirmer et Annuler
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(178, 255, 194)); // Vert pâle (Add Passenger)

        JButton btnConfirmer = new JButton("Confirmer");
        btnConfirmer.setBackground(new Color(159, 232, 159)); // Vert pâle pour confirmer
        btnConfirmer.setForeground(Color.BLACK);
        btnConfirmer.setFocusPainted(false);
        btnConfirmer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnConfirmer.setPreferredSize(new Dimension(100, 40)); // Hauteur ajustée

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setBackground(Color.RED); // Rouge pour annuler
        btnAnnuler.setForeground(Color.BLACK);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnAnnuler.setPreferredSize(new Dimension(100, 40)); // Hauteur ajustée

        buttonPanel.add(btnConfirmer);
        buttonPanel.add(btnAnnuler);

        // Ajout des composants au JDialog
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Confirmer
        btnConfirmer.addActionListener(e -> {
            try {
                int reservations = Integer.parseInt(textReservations.getText());
                if (reservations <= 0) {
                    throw new IllegalArgumentException("Le nombre de réservations doit être supérieur à 0.");
                }
                if (volAReserver.getMaxReservations() < reservations + volAReserver.getReservations()) {
                    throw new IllegalArgumentException("Le nombre de places disponibles est insuffisant.");
                }
                volAReserver.setReservations(reservations + volAReserver.getReservations());
                AccesDonnees.enregistrerListeVols(listeVols);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer un nombre valide.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dialog.dispose());

        // Ajout d'un KeyListener pour activer le bouton Confirmer avec la touche Entrée
        textReservations.addActionListener(e -> btnConfirmer.doClick());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
