package modele;

import java.io.Serializable;

public abstract class Vol implements Serializable {
  private static int nombreVols = 0;
  private int numero;
  private String destination;
  private DateVol depart;
  private int reservations;
  private Categories categorie;
  private int maxReservations;

  public Vol(int numero, String destination, DateVol depart, int reservations, int maxReservations,
      Categories categorie) {
    this.numero = numero;
    this.destination = destination;
    this.depart = depart;
    this.reservations = reservations;
    this.maxReservations = maxReservations;
    this.categorie = categorie;
  }

  public static int getNombreVols() {
    return nombreVols;
  }

  public static int setNombreVols(int nombre) {
    nombreVols = nombre;
    return nombreVols;
  }

  public void setDepart(DateVol depart) {
    this.depart = depart;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public int setReservations(int reservations) {
    return this.reservations = reservations;
  }

  public int setNumero(int numero) {
    return this.numero = numero;
  }

  public int setMaxReservations(int maxReservations) {
    return this.maxReservations = maxReservations;
  }

  public int getNumero() {
    return this.numero;
  }

  public String getDestination() {
    return this.destination;
  }

  public DateVol getDepart() {
    return this.depart;
  }

  public int getReservations() {
    return this.reservations;
  }

  public int getMaxReservations() {
    return this.maxReservations;
  }

  public Categories getCategorie() {
    return categorie;
  }

  @Override
  public String toString() {
    StringBuilder table = new StringBuilder();

    table.append(this.numero + ", " + this.destination + ", " + this.depart + ", " + this.reservations);
    // table.append(
    // "<tr style='border-collapse: collapse; background-color: #e6e4e1;
    // color:#241e16;'><td style='padding: 3px; text-align: center;'>")
    // .append(this.numero)
    // .append("</td><td style='padding: 3px;'>")
    // .append(this.destination)
    // .append("</td><td style='padding: 3px; text-align: center;'>")
    // .append(this.depart.toString())
    // .append("</td><td style='padding: 3px; text-align: center;'>")
    // .append(this.reservations)
    // .append("</td></tr>");

    return table.toString();
  }
}
