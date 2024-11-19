package service;

import java.util.ArrayList;
import javax.swing.JTextPane;
import modele.Vol;

public class GestionsVols {

  static final String FICHIER = "src/main/resources/donnees/vols.json";
  static final int MAX_PLACES = 340;
  static ArrayList<Vol> listeVols = new ArrayList<>();
  // MENU
  static final String COLONNES = "Numéro;Destination;Départ;Réservations";
  static JTextPane panel = new JTextPane();
  static boolean enregistre = false;
  static final String[] OPTIONS = {
      "Liste des vols",
      "Ajout d'un vol",
      "Retrait d'un vol",
      "Modification de la date de départ",
      "Réservation d'un vol",
      "Terminer" };

  public static void chargerVols() {

  }
}
