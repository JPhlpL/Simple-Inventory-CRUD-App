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

    private final MainFrame mainFrame;
    private JTextField registerUsernameField, registerNameField;
    private JPasswordField registerPasswordField, registerConfirmPasswordField;

    public RegistrationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize() {
        // Use GridBagLayout for a two-column layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create labels
        JLabel labelUsername = new JLabel("Username:");
        JLabel labelName = new JLabel("Name:");
        JLabel labelPassword = new JLabel("Password:");
        JLabel labelConfirm = new JLabel("Confirm Password:");

        // Create input fields with a bit wider columns
        registerUsernameField = new JTextField(15);
        registerNameField = new JTextField(15);
        registerPasswordField = new JPasswordField(15);
        registerConfirmPasswordField = new JPasswordField(15);

        // Row 0: Username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(labelUsername, gbc);
        gbc.gridx = 1;
        add(registerUsernameField, gbc);

        // Row 1: Name label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(labelName, gbc);
        gbc.gridx = 1;
        add(registerNameField, gbc);

        // Row 2: Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelPassword, gbc);
        gbc.gridx = 1;
        add(registerPasswordField, gbc);

        // Row 3: Confirm Password label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(labelConfirm, gbc);
        gbc.gridx = 1;
        add(registerConfirmPasswordField, gbc);

        // Create a separate panel for buttons
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // Row 4: Button panel spanning both columns
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Button actions
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
