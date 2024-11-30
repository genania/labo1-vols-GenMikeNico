package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequetesServeur {
    public static String obtenirFichierJson(String urlStr) {
        try {
            // Définir l'URL de l'API que l'on souhaite interroger
            URL url = new URL(urlStr);

            // Ouvrir une connexion HTTP avec l'URL spécifiée
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();

            // Définir la méthode de requête HTTP comme étant GET
            connexion.setRequestMethod("GET");

            // Définir le type de réponse attendue (ici, JSON)
            connexion.setRequestProperty("Accept", "application/json");

            // Obtenir le code de réponse HTTP du serveur (par exemple, 200 pour une requête
            // réussie)
            int codeRetour = connexion.getResponseCode();

            // Vérifier si le code de réponse est 200 (HTTP_OK), ce qui indique que tout
            // s'est bien passé
            if (codeRetour == HttpURLConnection.HTTP_OK) { // HTTP_OK = 200

                // Créer un BufferedReader pour lire la réponse du serveur
                // Spécifier l'encodage UTF-8 pour lire correctement les caractères spéciaux
                // (comme les accents)
                BufferedReader br = new BufferedReader(new InputStreamReader(connexion.getInputStream(), "UTF-8"));

                // Initialiser une variable pour chaque ligne lue et un StringBuilder pour
                // construire la réponse complète
                String ligne;
                StringBuilder contenu = new StringBuilder();

                // Lire chaque ligne de la réponse et l'ajouter au StringBuilder
                while ((ligne = br.readLine()) != null) {
                    contenu.append(ligne);
                }

                // Fermer le BufferedReader après avoir fini de lire la réponse
                br.close();

                // Déconnecter la connexion HTTP une fois la lecture terminée
                connexion.disconnect();

                // Retourner la réponse sous forme de chaîne de caractères
                // System.out.println(contenu.toString());
                return contenu.toString();
            } else {
                System.out.println("La requête a échoué. Code de réponse HTTP : " + codeRetour);
                return null;
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }
    }

    public static void envoyerFichierJson(String urlStr, String contenu) {
        try {
            // Définir l'URL de l'API où envoyer le contenu
            URL url = new URL(urlStr);

            // Ouvrir une connexion HTTP avec l'URL spécifiée
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();

            // Définir la méthode de requête HTTP comme étant POST
            connexion.setRequestMethod("POST");

            // Définir les en-têtes pour indiquer que nous envoyons du JSON
            connexion.setRequestProperty("Content-Type", "application/json");
            connexion.setRequestProperty("Accept", "application/json");

            // Activer la sortie pour pouvoir écrire dans la connexion
            connexion.setDoOutput(true);

            // Écrire le contenu dans la connexion
            try (OutputStream os = connexion.getOutputStream()) {
                byte[] input = contenu.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // Obtenir le code de réponse HTTP du serveur
            int codeRetour = connexion.getResponseCode();

            // Vérifier si le code de réponse est 200 ou 201 (succès)
            if (codeRetour == HttpURLConnection.HTTP_OK || codeRetour == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Données envoyées avec succès. Code de réponse HTTP : " + codeRetour);
            } else {
                System.out.println("La requête a échoué. Code de réponse HTTP : " + codeRetour);
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

}
