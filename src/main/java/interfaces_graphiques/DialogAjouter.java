package interfaces_graphiques;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

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
        // Configuration de la fenêtre de dialogue
        JDialog dialog = new JDialog(this, "Ajouter un vol", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());

        // Définir une icône pour la fenêtre (en haut à gauche de la barre de titre)
        dialog.setIconImage(new ImageIcon("src/icone/ajouterAvion.png").getImage());

        // Ajouter une bannière en haut
        JLabel lblBanner = new JLabel(new ImageIcon("src/icone/banner.png"));
        dialog.add(lblBanner, BorderLayout.NORTH);

        // Définir une couleur de fond pour la fenêtre principale
        dialog.getContentPane().setBackground(new Color(159, 232, 159)); // Vert pâle (Add Flight)

        // Ajouter une bordure noire autour de la fenêtre
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        // Panneau pour les champs de formulaire
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(new Color(159, 232, 159)); // Vert pâle (Add Flight)

        // Champs pour les données
        JLabel labelNumero = new JLabel("Numéro de vol : ");
        JTextField textNumero = new JTextField();

        JLabel labelDestination = new JLabel("Destination : ");
        JTextField textDestination = new JTextField();

        JLabel labelDate = new JLabel("Date (JJ-MM-AAAA) : ");
        JTextField textDate = new JTextField();

        JLabel labelMaxReservations = new JLabel("Maximum de réservations : ");
        JTextField textMaxReservations = new JTextField();
        textMaxReservations.setText("340");

        JLabel labelReservations = new JLabel("Réservations : ");
        JTextField textReservations = new JTextField();
        textReservations.setText("0");

        JLabel labelCategorie = new JLabel("Catégorie de vol : ");
        JComboBox<Categories> comboCategorie = new JComboBox<>(Categories.values());

        // Boutons Confirmer et Annuler
        JButton btnConfirmer = new JButton("Confirmer");
        JButton btnAnnuler = new JButton("Annuler");

        // Personnalisation des boutons
        btnConfirmer.setPreferredSize(new java.awt.Dimension(100, 50)); // Largeur 100, Hauteur 50
        btnConfirmer.setBackground(new Color(159, 232, 159)); // Vert pâle (Add Flight)
        btnConfirmer.setForeground(Color.BLACK);
        btnConfirmer.setFocusPainted(false);
        btnConfirmer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        btnAnnuler.setPreferredSize(new java.awt.Dimension(100, 50)); // Largeur 100, Hauteur 50
        btnAnnuler.setBackground(Color.RED); // Rouge pour annuler
        btnAnnuler.setForeground(Color.BLACK);
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 12));
        btnAnnuler.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Ajout des champs au panneau de formulaire
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
        formPanel.add(comboCategorie);

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 ligne, 2 colonnes, espacement horizontal de
                                                                      // 10
        buttonPanel.setBackground(new Color(159, 232, 159)); // Vert pâle (Add Flight)
        buttonPanel.add(btnConfirmer);
        buttonPanel.add(btnAnnuler);

        // Ajout des panneaux au dialogue
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Confirmer
        btnConfirmer.addActionListener(e -> {
            try {
                int numero = Integer.parseInt(textNumero.getText());
                String destination = textDestination.getText();
                int reservations = Integer.parseInt(textReservations.getText());
                int maxReservations = Integer.parseInt(textMaxReservations.getText());
                String dateStr = textDate.getText();

                String messageErreur = Utilitaires.obtenirMessageErreurSaisie(dateStr, reservations, maxReservations);

                if (!messageErreur.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, messageErreur, "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DateVol depart = new DateVol(dateStr);
                Categories categorie = (Categories) comboCategorie.getSelectedItem();

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

                listeVols.add(nouveauVol);
                AccesDonnees.enregistrerListeVols(listeVols);
                dialog.dispose();
                afficherConfirmationVol(nouveauVol);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer des données valides.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dialog.dispose());

        // Ajouter un KeyListener global pour détecter l'appui sur la touche Entrée
        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Appuyer sur Entrée exécute l'action du bouton Confirmer
                    btnConfirmer.doClick(); // Cela simule un clic sur le bouton Confirmer
                }
            }
        });

        // Activation de l'écouteur sur les champs et la fenêtre
        textNumero.requestFocusInWindow(); // Focus sur le premier champ

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void afficherConfirmationVol(Vol vol) {
        JDialog confirmationDialog = new JDialog(this, "Confirmation", true);
        confirmationDialog.setSize(300, 150);
        confirmationDialog.setLayout(new BorderLayout());

        JLabel message = new JLabel(
                "<html>Le vol a été enregistré avec succès !<br>" +
                        "Numéro : " + vol.getNumero() + "<br>" +
                        "Destination : " + vol.getDestination() + "<br>" +
                        "Date : " + vol.getDepart().getJour() + "-" +
                        vol.getDepart().getMois() + "-" + vol.getDepart().getAn() + "<br>" +
                        "Catégorie : " + vol.getCategorie() + "</html>",
                JLabel.CENTER);
        confirmationDialog.add(message, BorderLayout.CENTER);

        confirmationDialog.setLocationRelativeTo(this);

        new Thread(() -> {
            try {
                Thread.sleep(1500);
                confirmationDialog.dispose();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

        confirmationDialog.setVisible(true);
    }
}
