package com.mycompany.simpleinventorycrudapp;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import com.mycompany.simpleinventorycrudapp.ui.LoginPanel;
import com.mycompany.simpleinventorycrudapp.ui.DashboardPanel;
import com.mycompany.simpleinventorycrudapp.ui.RegistrationPanel;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String DASHBOARD_PANEL = "DashboardPanel";
    public static final String REGISTRATION_PANEL = "RegistrationPanel";

    public MainFrame() {
        setTitle("Simple Inventory CRUD App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create and add panels
        mainPanel.add(new LoginPanel(this), LOGIN_PANEL);
        mainPanel.add(new DashboardPanel(this), DASHBOARD_PANEL);
        mainPanel.add(new RegistrationPanel(this), REGISTRATION_PANEL);

        add(mainPanel);
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, LOGIN_PANEL);
    }

    public void showDashboardPanel() {
        cardLayout.show(mainPanel, DASHBOARD_PANEL);
    }

    public void showRegistrationPanel() {
        cardLayout.show(mainPanel, REGISTRATION_PANEL);
    }
}
