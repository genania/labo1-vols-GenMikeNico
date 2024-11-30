package interfaces_graphiques;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

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

        // Ouvrir un JDialog pré-rempli avec les informations du vol
        JDialog dialog = new JDialog(this, "Modifier un vol", true);
        dialog.setSize(400, 400); // Augmenté pour inclure la catégorie
        dialog.setLayout(new GridLayout(7, 2, 10, 10)); // Ajout d'une ligne supplémentaire pour la catégorie

        // Champs pré-remplis
        JLabel labelNumero = new JLabel("Numéro de vol : ");
        JTextField textNumero = new JTextField(String.valueOf(volAModifier.getNumero()));
        textNumero.setEditable(false); // Numéro non modifiable

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

        // Boutons OK et Annuler
        JButton btnOk = new JButton("OK");
        JButton btnAnnuler = new JButton("Annuler");

        // Ajout des composants au JDialog
        dialog.add(labelNumero);
        dialog.add(textNumero);

        dialog.add(labelDestination);
        dialog.add(textDestination);

        dialog.add(labelDate);
        dialog.add(textDate);

        dialog.add(labelMaxReservations);
        dialog.add(textMaxReservations);

        dialog.add(labelReservations);
        dialog.add(textReservations);

        dialog.add(labelCategorie);
        dialog.add(labelCategorieValue); // Afficher la catégorie

        dialog.add(btnOk);
        dialog.add(btnAnnuler);

        // Action du bouton OK
        btnOk.addActionListener(e -> {
            try {
                // Récupérer les nouvelles valeurs
                String destination = textDestination.getText();
                String dateStr = textDate.getText();

                int maxReservations = Integer.parseInt(textMaxReservations.getText());
                int reservations = Integer.parseInt(textReservations.getText());

                String messageErreur = Utilitaires.obtenirMessageErreurSaisie(dateStr, reservations, maxReservations);

                if (messageErreur != "") {
                    JOptionPane.showMessageDialog(dialog, messageErreur, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DateVol date = new DateVol(dateStr);

                // Mettre à jour le vol sélectionné
                volAModifier.setDestination(destination);
                volAModifier.setDepart(date);
                volAModifier.setMaxReservations(maxReservations);
                volAModifier.setReservations(reservations);

                // Mettre à jour la liste et le fichier JSON
                AccesDonnees.enregistrerListeVols(listeVols);

                // Fermer le JDialog
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer des données valides.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
