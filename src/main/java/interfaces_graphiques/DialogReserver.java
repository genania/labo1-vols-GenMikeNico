package interfaces_graphiques;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

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
        // dialog.setLayout(new BoxLayout(BoxLayout.Y_AXIS)); // Disposition verticale
        dialog.setLayout(new GridLayout(9, 1, 10, 10));

        // Champs pré-remplis
        JLabel labelNumero = new JLabel("Numéro de vol : " + volAReserver.getNumero());
        JLabel labelDestination = new JLabel("Destination : " + volAReserver.getDestination());
        JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : " + volAReserver.getDepart().getJour() + "-" +
                volAReserver.getDepart().getMois() + "-" + volAReserver.getDepart().getAn());
        JLabel labelMaxReservations = new JLabel("Maximum de réservation : " + volAReserver.getMaxReservations());
        JLabel labelReservationsActuel = new JLabel("Réservations actuelles : " + volAReserver.getReservations());
        JLabel labelCategorie = new JLabel("Categorie : " + volAReserver.getCategorie().toString());

        // Panel pour la ligne "Nombre de réservations à ajouter"
        JPanel reservationPanel = new JPanel();
        reservationPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Aligner les composants sur la même ligne
        JLabel labelReservations = new JLabel("Nombre de réservations à ajouter : ");
        JTextField textReservations = new JTextField(10); // Taille du champ texte
        reservationPanel.add(labelReservations);
        reservationPanel.add(textReservations);

        // Boutons OK et Annuler
        JButton btnOk = new JButton("OK");
        JButton btnAnnuler = new JButton("Annuler");

        // Personnalisation du bouton OK
        btnOk.setBackground(Color.GREEN); // Fond vert
        btnOk.setForeground(Color.WHITE); // Texte blanc
        btnOk.setFocusPainted(false); // Désactiver l'effet de focus
        btnOk.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Bordure noire

        // Personnalisation du bouton Annuler
        btnAnnuler.setBackground(Color.RED); // Fond rouge
        btnAnnuler.setForeground(Color.BLACK); // Texte noir
        btnAnnuler.setFocusPainted(false); // Désactiver l'effet de focus
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 12)); // Texte noir en gras
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Bordure noire

        // Ajout des composants au JDialog
        dialog.add(labelNumero);
        dialog.add(labelDestination);
        dialog.add(labelDate);
        dialog.add(labelMaxReservations);
        dialog.add(labelReservationsActuel);
        dialog.add(reservationPanel); // Ajouter le panel de réservation sur une seule ligne
        dialog.add(labelCategorie);

        // Action du bouton OK
        btnOk.addActionListener(e -> {
            try {
                // Récupérer les nouvelles valeurs
                int reservations = Integer.parseInt(textReservations.getText());
                if (reservations <= 0) {
                    throw new IllegalArgumentException("Le nombre de réservations doit être supérieur à 0.");
                }

                if (volAReserver.getMaxReservations() < reservations + volAReserver.getReservations()) {
                    throw new IllegalArgumentException("Le nombre de place disponible est insuffisante.");
                }

                // Mettre à jour le vol sélectionné
                volAReserver.setReservations(reservations + volAReserver.getReservations());

                // Mettre à jour la liste et le fichier JSON
                AccesDonnees.enregistrerListeVols(listeVols);

                // Fermer le JDialog
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

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
