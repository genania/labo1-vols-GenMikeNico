package interfaces_graphiques;

import modele.DateVol;

public class Utilitaires {
    public static String obtenirMessageErreurSaisie(String dateStr, int reservation, int maxReservation) {
        String messageErreur = "";

        if (!DateVol.estBonFormat(dateStr)) {
            messageErreur += "* Veuillez entrer une date du format indiqué.\n";
        }

        DateVol dateVol = new DateVol(dateStr);

        if (!DateVol.validerDateVol(dateVol)) {
            messageErreur += "* Veuillez entrer une date valide et ultérieure à aujourd'hui.\n";
        }

        if (reservation > maxReservation) {
            messageErreur += "* Nombre de places insuffissantes.\n";
        }

        if (maxReservation <= 0) {
            messageErreur += "* Le nommrbe de réservations maximum doit être minimum 1.";
        }

        return messageErreur;
    }
}
