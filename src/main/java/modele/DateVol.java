package modele;

import java.time.LocalDate;

public class DateVol implements Comparable<DateVol> {
  private int jour;
  private int mois;
  private int an;

  static public String tabMois[] = {
      null, "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aoét", "Septembre", "Octobre", "Novembre",
      "Décembre"
  };
  static public LocalDate dateActuelle = LocalDate.now();

  public DateVol() {
    jour = mois = 1;
    an = 2000;
  }

  public DateVol(int jour, int mois, int an) {
    this.jour = jour;
    this.mois = mois;
    this.an = an;
  }

  public DateVol(String dateStr) {
    // La date doit être au format JJ-MM-YYYY
    String[] dateParts = dateStr.split("-");
    this.jour = Integer.parseInt(dateParts[0]);
    this.mois = Integer.parseInt(dateParts[1]);
    this.an = Integer.parseInt(dateParts[2]);
  }

  // Les méthodes set et get habituelles
  public int getJour() {
    return this.jour;
  }

  public int getMois() {
    return this.mois;
  }

  public int getAn() {
    return this.an;
  }

  public static boolean estDateValide(int jour, int mois, int annee) {
    if (mois < 1 || mois > 12) {
      return false;
    }
    if (jour < 1 || jour > 31) {
      return false;
    }
    if (mois == 4 || mois == 6 || mois == 9 || mois == 11) {
      if (jour > 30) {
        return false;
      }
    }
    if (mois == 2) {
      boolean estBissextile = estBissextile(annee);
      if (estBissextile && jour > 29) {
        return false;
      }
      if (!estBissextile && jour > 28) {
        return false;
      }
    }
    return true;
  }

  public static boolean validerDateVol(DateVol dateVol) {

    if (!estDateValide(dateVol.getJour(), dateVol.getMois(), dateVol.getAn())) {
      return false;
    }

    LocalDate dateActuelleLocalDate = LocalDate.now();

    DateVol dateActuelle = new DateVol(
        dateActuelleLocalDate.getDayOfMonth(), dateActuelleLocalDate.getMonthValue(), dateActuelleLocalDate.getYear());

    return dateActuelle.compareTo(dateVol) == -1;
  }

  public void setJour(int jour) {
    int nbJours = determinerNbJoursMois(this.mois, this.an);
    if (jour > nbJours || jour < 1) {
      System.out.println("Jour invalide pour le mois de " + tabMois[this.mois].toLowerCase());
    } else {
      this.jour = jour;
    }
  }

  public void setMois(int mois) {
    int nbJours;
    if (mois < 1 || mois > 12) {
      System.out.println("Mois " + mois + " n'est un mois valide [1-12]");
    } else {
      nbJours = determinerNbJoursMois(mois, this.an);
      if (this.jour > nbJours) {
        System.out.println("Mois " + tabMois[mois].toLowerCase()
            + " n'est un mois valide pour le jour actuel du vol qu'est " + this.jour);
      } else {
        this.mois = mois;
      }
    }
  }

  public void setAn(int an) {
    int anneActuelle = dateActuelle.getYear();
    if (an < anneActuelle) {
      System.out.println("Année " + an + " ne peut pas étre inférieure é l'année actuelle, soit " + anneActuelle);
    } else {
      this.an = an;
    }
  }

  public static boolean estBissextile(int an) {
    return (an % 4 == 0 && an % 100 != 0) || (an % 400 == 0);
  }

  public static int determinerNbJoursMois(int mois, int an) {
    int nbJours;
    int tabJrMois[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    if (mois == 2 && estBissextile(an))
      nbJours = 29;
    else
      nbJours = tabJrMois[mois];
    return nbJours;
  }

  public static String fillLeft(char character, int length, String string) {
    String filled = "";
    int toFill = length - string.length();
    for (int i = 1; i <= toFill; i++) {
      filled += character;
    }
    return filled + string;
  }

  public static boolean estBonFormat(String dateStr) {
    // Regex pour vérifier le format JJ-MM-AAAA
    String regex = "^(\\d{2}|\\d{1})-(\\d{2}|\\d{1})-(\\d{4})$";

    // Vérifier si dateStr correspond au format
    return dateStr.matches(regex);
  }

  @Override
  public String toString() {
    String leJour, leMois;
    leJour = fillLeft('0', 2, this.jour + "");
    leMois = fillLeft('0', 2, this.mois + "");
    return leJour + "/" + leMois + "/" + this.an;
  }

  @Override
  public int compareTo(DateVol autreDate) {
    // 0 Pour égal
    // 1 si autreDate < this
    // -1 si autreDate > this

    // Comparer l'année
    if (this.getAn() > autreDate.getAn()) {
      return 1;
    } else if (this.getAn() < autreDate.getAn()) {
      return -1;
    }

    // Comparer le mois (si les années sont égales)
    if (this.getMois() > autreDate.getMois()) {
      return 1;
    } else if (this.getMois() < autreDate.getMois()) {
      return -1;
    }

    // Comparer le jour (si les années et les mois sont égaux)
    if (this.getJour() > autreDate.getJour()) {
      return 1;
    } else if (this.getJour() < autreDate.getJour()) {
      return -1;
    }

    // Si tout est égal
    return 0;

  }
} // fin de la classe Date
