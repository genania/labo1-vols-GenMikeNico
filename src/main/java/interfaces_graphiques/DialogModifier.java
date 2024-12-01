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
}
