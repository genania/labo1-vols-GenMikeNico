package modele;

public class VolCharter extends Vol {
  private boolean repas = true;
  private boolean reservable = true;
  private boolean alcool = true;
  private boolean wifi = true;
  private boolean divertissement = false;
  private boolean services = false;
  private boolean prises = false;

  public VolCharter(int numero, String destination, DateVol depart, int reservations, int maxReservations) {
    super(numero, destination, depart, reservations, maxReservations, Categories.CHARTER);
  }
}
