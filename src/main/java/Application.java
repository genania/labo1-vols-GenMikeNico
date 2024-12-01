import javax.swing.*;
import interfaces_graphiques.FenetrePrincipale;
import java.awt.*;

public class Application {
  private static Point size = new Point(1200, 800);

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Error : No look, no feel");
    }

    SwingUtilities.invokeLater(() -> {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      FenetrePrincipale app = new FenetrePrincipale();

      app.setBounds(
          (screenSize.width - size.x) / 2,
          (screenSize.height - size.y) / 2,
          size.x, size.y);

      app.setExtendedState(JFrame.NORMAL);

      app.setVisible(true);
      app.updateTableData(app.listevols);
    });
  }
}
