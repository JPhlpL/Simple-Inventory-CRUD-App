package com.mycompany.simpleinventorycrudapp.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.mindrot.jbcrypt.BCrypt;

import com.mycompany.simpleinventorycrudapp.MainFrame;
import com.mycompany.simpleinventorycrudapp.db.DatabaseManager;

public class RegistrationPanel extends JPanel {

    private MainFrame mainFrame;
    private JTextField registerUsernameField, registerNameField;
    private JPasswordField registerPasswordField, registerConfirmPasswordField;

    public RegistrationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel labelUsername = new JLabel("Username:");
        JLabel labelName = new JLabel("Name:");
        JLabel labelPassword = new JLabel("Password:");
        JLabel labelConfirm = new JLabel("Confirm Password:");

        registerUsernameField = new JTextField(10);
        registerNameField = new JTextField(10);
        registerPasswordField = new JPasswordField(10);
        registerConfirmPasswordField = new JPasswordField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(labelUsername, gbc);
        gbc.gridy = 1;
        add(labelName, gbc);
        gbc.gridy = 2;
        add(labelPassword, gbc);
        gbc.gridy = 3;
        add(labelConfirm, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(registerUsernameField, gbc);
        gbc.gridy = 1;
        add(registerNameField, gbc);
        gbc.gridy = 2;
        add(registerPasswordField, gbc);
        gbc.gridy = 3;
        add(registerConfirmPasswordField, gbc);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        add(backButton, gbc);

        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> mainFrame.showLoginPanel());
    }

    private void registerUser() {
        String username = registerUsernameField.getText();
        String name = registerNameField.getText();
        char[] password = registerPasswordField.getPassword();
        char[] confirmPassword = registerConfirmPasswordField.getPassword();

        if (username.isEmpty() || name.isEmpty() || password.length == 0 || confirmPassword.length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!java.util.Arrays.equals(password, confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hashedPassword = BCrypt.hashpw(new String(password), BCrypt.gensalt());

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, name) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, name);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration successful. You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.showLoginPanel();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering user.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
