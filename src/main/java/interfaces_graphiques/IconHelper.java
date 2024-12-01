package interfaces_graphiques;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class IconHelper {

    /**
     * Transforme une ImageIcon en une icône ronde avec bordure.
     *
     * @param icon     L'icône d'origine.
     * @param diameter Le diamètre du cercle (taille de l'icône finale).
     * @return Une nouvelle ImageIcon ronde.
     */
    public static ImageIcon makeIconRound(ImageIcon icon, int diameter) {
        BufferedImage roundedImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();

        // Activer l'anti-aliasing pour un rendu propre
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner un cercle
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
        g2.setClip(circle);

        // Redimensionner et dessiner l'image dans le cercle
        g2.drawImage(icon.getImage(), 0, 0, diameter, diameter, null);

        // Ajouter une bordure noire autour du cercle
        g2.setClip(null);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2)); // Épaisseur de la bordure
        g2.draw(circle);

        g2.dispose();

        return new ImageIcon(roundedImage);
    }
}
