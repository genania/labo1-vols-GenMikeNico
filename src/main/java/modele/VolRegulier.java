package modele;

public class VolRegulier extends Vol {
  private boolean repas = false;
  private boolean reservable = false;
  private boolean alcool = false;
  private boolean wifi = false;
  private boolean divertissement = false;
  private boolean services = true;
  private boolean prises = true;

  public VolRegulier(int numero, String destination, DateVol depart, int reservations) {
    super(numero, destination, depart, reservations, 3);
  }
}
