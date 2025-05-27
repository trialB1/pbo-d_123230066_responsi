package main;

import view.ProduksiView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                System.err.println("Tidak dapat mengatur Look and Feel: " + e.getMessage());
            }

            new ProduksiView().setVisible(true);
        });
    }
}