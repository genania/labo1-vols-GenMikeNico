package interfaces_graphiques;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Color;
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

        // Confirmer la suppression
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir supprimer ce vol ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Récupérer l'index du vol dans la liste
            int modelRow = tableFenetrePrincipale.convertRowIndexToModel(selectedRow);

            // Supprimer le vol de la liste
            listeVols.remove(modelRow);

            // Mettre à jour le fichier JSON
            AccesDonnees.enregistrerListeVols(listeVols);

            // Afficher une confirmation
            JOptionPane.showMessageDialog(this, "Le vol a été supprimé avec succès.", "Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
