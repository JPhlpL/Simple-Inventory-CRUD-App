package com.mycompany.simpleinventorycrudapp;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.showLoginPanel(); // start with the login panel
            mainFrame.setVisible(true);
        });
    }
}
