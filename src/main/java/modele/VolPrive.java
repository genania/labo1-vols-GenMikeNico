package modele;

public class VolPrive extends Vol {
  private boolean repas = true;
  private boolean reservable = true;
  private boolean alcool = true;
  private boolean wifi = true;
  private boolean divertissement = true;
  private boolean services = true;
  private boolean prises = true;

  public VolPrive(int numero, String destination, DateVol depart, int reservations, int maxReservations) {
    super(numero, destination, depart, reservations, maxReservations, Categories.PRIVE);
  }
}
