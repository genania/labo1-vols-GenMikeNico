package interfaces_graphiques;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import modele.Categories;
import modele.DateVol;
import modele.Vol;
import modele.VolBasPrix;
import modele.VolCharter;
import modele.VolPrive;
import modele.VolRegulier;
import service.AccesDonnees;

public class DialogAjouter extends JDialog {
    public DialogAjouter(List<Vol> listeVols) {
        JDialog dialog = new JDialog(this, "Ajouter un vol", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2, 10, 10)); // 6 lignes pour inclure la liste déroulante

        // Champs pour les données
        JLabel labelNumero = new JLabel("Numéro de vol : ");
        JTextField textNumero = new JTextField();

        JLabel labelDestination = new JLabel("Destination : ");
        JTextField textDestination = new JTextField();

        JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : ");
        JTextField textDate = new JTextField();

        JLabel labelMaxReservations = new JLabel("Maximun de réservations : ");
        JTextField textMaxReservations = new JTextField();
        textMaxReservations.setText("340"); // Par défaut, 0 réservations

        JLabel labelReservations = new JLabel("Réservations : ");
        JTextField textReservations = new JTextField();
        textReservations.setText("0"); // Par défaut, 0 réservations

        // Liste déroulante pour la catégorie
        JLabel labelCategorie = new JLabel("Catégorie de vol : ");
        JComboBox<Categories> comboCategorie = new JComboBox<>(Categories.values());

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
        dialog.add(comboCategorie); // Ajout de la liste déroulante

        dialog.add(btnOk);
        dialog.add(btnAnnuler);

        // Action du bouton OK
        btnOk.addActionListener(e -> {
            try {
                // Récupérer les valeurs saisies
                int numero = Integer.parseInt(textNumero.getText());
                String destination = textDestination.getText();
                int reservations = Integer.parseInt(textReservations.getText());
                int maxReservations = Integer.parseInt(textMaxReservations.getText());
                String dateStr = textDate.getText();

                String messageErreur = Utilitaires.obtenirMessageErreurSaisie(dateStr, reservations, maxReservations);

                if (messageErreur != "") {
                    JOptionPane.showMessageDialog(dialog, messageErreur, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DateVol depart = new DateVol(dateStr);

                // Récupérer la catégorie sélectionnée
                Categories categorie = (Categories) comboCategorie.getSelectedItem();

                // Créer le vol selon la catégorie choisie
                Vol nouveauVol;

                switch (categorie) {
                    case BAS_PRIX:
                        nouveauVol = new VolBasPrix(numero, destination, depart, reservations, maxReservations);
                        break;
                    case CHARTER:
                        nouveauVol = new VolCharter(numero, destination, depart, reservations, maxReservations);
                        break;
                    case PRIVE:
                        nouveauVol = new VolPrive(numero, destination, depart, reservations, maxReservations);
                        break;
                    case REGULIER:
                        nouveauVol = new VolRegulier(numero, destination, depart, reservations, maxReservations);
                        break;
                    default:
                        throw new IllegalArgumentException("Catégorie invalide.");
                }

                // Ajouter le nouveau vol à la liste
                listeVols.add(nouveauVol);

                // Enregistrer la liste mise à jour dans le fichier JSON
                AccesDonnees.enregistrerListeVols(listeVols);

                // Fermer le JDialog actuel
                dialog.dispose();

                // Afficher la confirmation
                afficherConfirmationVol(nouveauVol);
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

    private void afficherConfirmationVol(Vol vol) {
        // Créer un JDialog pour la confirmation
        JDialog confirmationDialog = new JDialog(this, "Confirmation", true);
        confirmationDialog.setSize(300, 150);
        confirmationDialog.setLayout(new BorderLayout());

        // Texte de confirmation
        JLabel message = new JLabel(
                "<html>Le vol a été enregistré avec succès !<br>" +
                        "Numéro : " + vol.getNumero() + "<br>" +
                        "Destination : " + vol.getDestination() + "<br>" +
                        "Date : " + vol.getDepart().getJour() + "-" +
                        vol.getDepart().getMois() + "-" + vol.getDepart().getAn() + "<br>" +
                        "Catégorie : " + vol.getCategorie() + "</html>",
                JLabel.CENTER);
        confirmationDialog.add(message, BorderLayout.CENTER);

        // Afficher la fenêtre au centre de l'application
        confirmationDialog.setLocationRelativeTo(this);

        // Lancer un thread pour fermer automatiquement la fenêtre après 3 secondes
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Pause de 3 secondes
                confirmationDialog.dispose(); // Fermer la fenêtre
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

        // Rendre la fenêtre visible
        confirmationDialog.setVisible(true);
    }
}
