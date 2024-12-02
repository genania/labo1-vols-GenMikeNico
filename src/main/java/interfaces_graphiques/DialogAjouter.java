package interfaces_graphiques;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import modele.*;
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
        dialog.getContentPane().setBackground(new Color(159, 232, 159)); // Vert pâle

        // Ajouter une bordure noire autour de la fenêtre
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        // Panneau pour les champs de formulaire
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(new Color(159, 232, 159)); // Vert pâle

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
        btnConfirmer.setPreferredSize(new Dimension(100, 50));
        btnConfirmer.setBackground(new Color(159, 232, 159)); // Vert pâle
        btnConfirmer.setForeground(Color.BLACK);
        btnConfirmer.setFocusPainted(false);
        btnConfirmer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        btnAnnuler.setPreferredSize(new Dimension(100, 50));
        btnAnnuler.setBackground(Color.RED);
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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(159, 232, 159));
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
                    afficherFenetreErreur(messageErreur);
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
                afficherFenetreErreur("Veuillez entrer des données valides.");
            }
        });

        // Action du bouton Annuler
        btnAnnuler.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void afficherFenetreErreur(String message) {
        JDialog erreurDialog = new JDialog(this, "Erreur", true);
        erreurDialog.setSize(450, 200); // Taille ajustée
        erreurDialog.setLayout(new BorderLayout());
        erreurDialog.getContentPane().setBackground(new Color(255, 182, 193)); // Rose clair

        // Ajouter une bordure noire à la boîte de dialogue
        erreurDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        // Définir une icône personnalisée
        ImageIcon iconErreur = new ImageIcon("src/icone/erreur.png");
        erreurDialog.setIconImage(iconErreur.getImage());

        // Message
        JLabel lblMessage = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        lblMessage.setFont(new Font("Arial", Font.BOLD, 16));
        lblMessage.setForeground(Color.BLACK);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte
        lblMessage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ajouter du padding autour du texte
        erreurDialog.add(lblMessage, BorderLayout.CENTER);

        // Bouton OK
        JButton btnOk = new JButton("OK");
        btnOk.setFont(new Font("Arial", Font.BOLD, 14));
        btnOk.setBackground(Color.RED);
        btnOk.setForeground(Color.WHITE);
        btnOk.setFocusPainted(false);
        btnOk.setPreferredSize(new Dimension(100, 40));
        btnOk.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        btnOk.addActionListener(e -> erreurDialog.dispose());

        // Panneau pour le bouton
        JPanel panelBouton = new JPanel();
        panelBouton.setBackground(new Color(255, 182, 193)); // Rose clair
        panelBouton.add(btnOk);
        erreurDialog.add(panelBouton, BorderLayout.SOUTH);

        erreurDialog.setLocationRelativeTo(this);
        erreurDialog.setVisible(true);
    }

    private void afficherConfirmationVol(Vol vol) {
        JDialog confirmationDialog = new JDialog(this, "Confirmation", true);
        confirmationDialog.setSize(500, 300);
        confirmationDialog.setLayout(new BorderLayout());

        // Définir une icône pour la fenêtre
        ImageIcon icon = new ImageIcon("src/icone/modifierAvion.png");
        confirmationDialog.setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 250, 205)); // Jaune pâle
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel message = new JLabel(
                "<html><div style='text-align: center;'>Le vol a été enregistré avec succès !</div></html>");
        message.setFont(new Font("Arial", Font.BOLD, 16));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(message, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 250, 205));

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(new Color(159, 232, 159));
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        okButton.setPreferredSize(new Dimension(100, 40));

        okButton.addActionListener(e -> confirmationDialog.dispose());
        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmationDialog.add(mainPanel);
        confirmationDialog.setLocationRelativeTo(this);
        confirmationDialog.setVisible(true);
    }
}
