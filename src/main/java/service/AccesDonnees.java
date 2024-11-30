package service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modele.Categories;
import modele.DateVol;
import modele.Vol;
import modele.VolBasPrix;
import modele.VolCharter;
import modele.VolPrive;
import modele.VolRegulier;

public class AccesDonnees {
    private static String urlVols = "http://localhost:3000/vols";

    public static List<Vol> obtenirListeVols() {
        String contenuJson = RequetesServeur.obtenirFichierJson(urlVols);
        List<Vol> vols = new ArrayList<>();

        try {
            // Remove whitespace and newlines for easier parsing
            contenuJson = contenuJson.replaceAll("(?=([^\"]*\"[^\"]*\")*[^\"]*$)\\s+", "");

            // Extract individual objects between curly braces
            Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher matcher = pattern.matcher(contenuJson);

            while (matcher.find()) {
                String objectStr = matcher.group(1);
                int numero = Integer.parseInt(extraireValeur(objectStr, "id"));
                String destination = extraireValeur(objectStr, "destination");
                DateVol depart = new DateVol(
                        Integer.parseInt(extraireValeur(objectStr, "jour")),
                        Integer.parseInt(extraireValeur(objectStr, "mois")),
                        Integer.parseInt(extraireValeur(objectStr, "annee")));
                int reservations = Integer.parseInt(extraireValeur(objectStr, "reservations"));
                int maxReservations = Integer.parseInt(extraireValeur(objectStr, "maxReservations"));
                int categorieInt = Integer.parseInt(extraireValeur(objectStr, "categorie"));
                Categories categorie = Categories.convertir(categorieInt);

                switch (categorie) {
                    case BAS_PRIX:
                        vols.add(new VolBasPrix(numero, destination, depart, reservations, maxReservations));
                        break;
                    case CHARTER:
                        vols.add(new VolCharter(numero, destination, depart, reservations, maxReservations));
                        break;
                    case PRIVE:
                        vols.add(new VolPrive(numero, destination, depart, reservations, maxReservations));
                        break;
                    case REGULIER:
                        vols.add(new VolRegulier(numero, destination, depart, reservations, maxReservations));
                        break;
                    default:
                        break;
                }
                // vols.add(new Vol(numero, destination, date, reservations, categorie));
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return vols;
    }

    public static void enregistrerListeVols(List<Vol> listeVols) {
        // Construire le JSON manuellement
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < listeVols.size(); i++) {
            Vol vol = listeVols.get(i);
            jsonBuilder.append("  {\n");
            jsonBuilder.append("    \"id\": ").append(vol.getNumero()).append(",\n");
            jsonBuilder.append("    \"destination\": \"").append(vol.getDestination()).append("\",\n");
            jsonBuilder.append("      \"jour\": ").append(vol.getDepart().getJour()).append(",\n");
            jsonBuilder.append("      \"mois\": ").append(vol.getDepart().getMois()).append(",\n");
            jsonBuilder.append("      \"annee\": ").append(vol.getDepart().getAn()).append(",\n");
            jsonBuilder.append("    \"maxReservations\": ").append(vol.getMaxReservations()).append(",\n");
            jsonBuilder.append("    \"reservations\": ").append(vol.getReservations()).append(",\n");
            jsonBuilder.append("    \"categorie\": ").append(vol.getCategorie().toInt()).append("\n");
            jsonBuilder.append("  }");

            // Ajouter une virgule après chaque objet sauf le dernier
            if (i < listeVols.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append("]");

        RequetesServeur.envoyerFichierJson(urlVols, jsonBuilder.toString());
    }

    private static String extraireValeur(String json, String key) {
        // Find the value after the key
        String pattern = "\"" + key + "\":\"?([^,\"}]+)\"?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(json);

        if (m.find()) {
            return m.group(1);
        }
        return "";
    }
}
