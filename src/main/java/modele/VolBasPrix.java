package modele;

public class VolBasPrix extends Vol {
  private boolean repas = false;
  private boolean reservable = false;
  private boolean alcool = false;
  private boolean wifi = false;
  private boolean divertissement = false;
  private boolean services = false;
  private boolean prises = false;

  public VolBasPrix(int numero, String destination, DateVol depart, int reservations, int maxReservations) {
    super(numero, destination, depart, reservations, maxReservations, Categories.BAS_PRIX);
  }
}
