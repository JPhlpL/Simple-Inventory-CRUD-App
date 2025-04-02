package com.mycompany.simpleinventorycrudapp.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mycompany.simpleinventorycrudapp.MainFrame;
import com.mycompany.simpleinventorycrudapp.db.DatabaseManager;
import com.mycompany.simpleinventorycrudapp.util.ValidationUtil;
import com.toedter.calendar.JDateChooser;

public class DashboardPanel extends JPanel {

    private final MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JButton editButton, insertButton, updateButton, deleteButton, deleteAllButton, importButton, searchButton, logoutButton;
    private JTextField idField, partNameField, partNumberField, supplierNameField, stocksField;
    private JDateChooser transactDatePicker;
    private BufferedImage iconImage;

    // Define a date format for display and parsing.
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DashboardPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initialize();

    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Sidebar Panel
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setBackground(Color.BLACK);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        // inputPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Set icon for frame
        try {
            iconImage = ImageIO.read(new File("src/resources/img/icon.jpg"));
            mainFrame.setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup fields
        idField = new JTextField(10);
        idField.setEditable(false);
        partNameField = new JTextField(10);
        partNumberField = new JTextField(10);
        supplierNameField = new JTextField(10);
        stocksField = new JTextField(10);
        // Apply numeric filter
        ValidationUtil.setupNumericField(stocksField);

        transactDatePicker = new JDateChooser();
        transactDatePicker.setEnabled(true);

        // Add a logo to the sidebar
        try {
            BufferedImage logo = ImageIO.read(new File("src/resources/img/logo.png"));
            JLabel picLabel = new JLabel(new ImageIcon(logo.getScaledInstance(240, 130, Image.SCALE_SMOOTH)));
            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 0;
            inputPanel.add(picLabel, gbc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Part Name:"), gbc);
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Part Number:"), gbc);
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Supplier Name:"), gbc);
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Stocks:"), gbc);
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Transaction Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(idField, gbc);
        gbc.gridy = 2;
        inputPanel.add(partNameField, gbc);
        gbc.gridy = 3;
        inputPanel.add(partNumberField, gbc);
        gbc.gridy = 4;
        inputPanel.add(supplierNameField, gbc);
        gbc.gridy = 5;
        inputPanel.add(stocksField, gbc);
        gbc.gridy = 6;
        inputPanel.add(transactDatePicker, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        insertButton = new JButton("Insert");
        updateButton = new JButton("Update");
        importButton = new JButton("Import");
        logoutButton = new JButton("Logout");
        editButton = new JButton("Select");
        deleteButton = new JButton("Delete");
        deleteAllButton = new JButton("Delete All");

        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(deleteAllButton);
        buttonPanel.add(importButton);
        buttonPanel.add(logoutButton);

        sidebarPanel.add(inputPanel, BorderLayout.NORTH);
        sidebarPanel.add(buttonPanel, BorderLayout.CENTER);

        // Main table panel
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Part Name");
        tableModel.addColumn("Part Number");
        tableModel.addColumn("Supplier Name");
        tableModel.addColumn("Stocks");
        tableModel.addColumn("Transaction Date");

        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        JScrollPane tableScroll = new JScrollPane(table);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search All");
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(editButton);

        // Apply color and font to buttons and text fields
        java.awt.Color textColor = java.awt.Color.WHITE;
        java.awt.Color buttonColor = java.awt.Color.DARK_GRAY;

        insertButton.setForeground(textColor);
        editButton.setForeground(textColor);
        updateButton.setForeground(textColor);
        deleteButton.setForeground(textColor);
        deleteAllButton.setForeground(textColor);
        importButton.setForeground(textColor);
        searchButton.setForeground(textColor);
        logoutButton.setForeground(textColor);

        insertButton.setBackground(buttonColor);
        editButton.setBackground(buttonColor);
        updateButton.setBackground(buttonColor);
        deleteButton.setBackground(buttonColor);
        deleteAllButton.setBackground(buttonColor);
        importButton.setBackground(buttonColor);
        searchButton.setBackground(buttonColor);
        logoutButton.setBackground(buttonColor);

        // Outer panel
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(sidebarPanel, BorderLayout.WEST);
        outerPanel.add(tableScroll, BorderLayout.CENTER);
        outerPanel.add(searchPanel, BorderLayout.NORTH);

        add(outerPanel, BorderLayout.CENTER);

        // Load initial data using the Transaction model
        loadData();

        // Action listeners
        insertButton.addActionListener(e -> insertRow());
        editButton.addActionListener(e -> editRow());
        updateButton.addActionListener(e -> updateRow());
        deleteButton.addActionListener(e -> deleteRow());
        deleteAllButton.addActionListener(e -> deleteAllRows());
        importButton.addActionListener(e -> importExcel());
        logoutButton.addActionListener(e -> mainFrame.showLoginPanel());
        searchButton.addActionListener(e -> searchAllFields(searchField.getText()));

        // Allow column sort on header click
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                sorter.toggleSortOrder(col);
            }
        });
    }

    private void loadData() {
        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM transactions")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String partName = rs.getString("part_name");
                String partNumber = rs.getString("part_number");
                String supplierName = rs.getString("supplier_name");
                int stocks = rs.getInt("stocks");

                // Retrieve the timestamp as a String
                String tsString = rs.getString("timestamp");
                Timestamp ts = null;
                if (tsString != null) {
                    // If the string is in a date-only format ("yyyy-MM-dd"), append a default time.
                    if (tsString.trim().length() == 10) {  // e.g., "2025-04-18"
                        tsString += " 00:00:00";
                    }
                    // Convert the string to a Timestamp.
                    ts = Timestamp.valueOf(tsString);
                }

                // Format the timestamp for display using your full timestamp format.
                String formattedTimestamp = (ts != null) ? dateFormat.format(ts) : "";

                Object[] rowData = {id, partName, partNumber, supplierName, stocks, formattedTimestamp};
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchAllFields(String query) {
        // Cast the row sorter to a parameterized type.
        @SuppressWarnings("unchecked")
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        sorter.setRowFilter(javax.swing.RowFilter.regexFilter(query));
    }

    private void editRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(table.getValueAt(selectedRow, 0).toString());
            partNameField.setText(table.getValueAt(selectedRow, 1).toString());
            partNumberField.setText(table.getValueAt(selectedRow, 2).toString());
            supplierNameField.setText(table.getValueAt(selectedRow, 3).toString());
            stocksField.setText(table.getValueAt(selectedRow, 4).toString());
            String dateString = table.getValueAt(selectedRow, 5).toString();
            try {
                Date date = dateFormat.parse(dateString);
                transactDatePicker.setDate(date);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void insertRow() {
        // Gather and validate input values
        String partName = partNameField.getText().trim();
        String partNumber = partNumberField.getText().trim();
        String supplierName = supplierNameField.getText().trim();
        String stocksText = stocksField.getText().trim();
        Date selectedDate = transactDatePicker.getDate();

        if (partName.isEmpty() || partNumber.isEmpty() || stocksText.isEmpty() || selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stocks;
        try {
            stocks = Integer.parseInt(stocksText);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Stocks must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format the date with time (e.g., "yyyy-MM-dd HH:mm:ss")
        String formattedTimestamp = dateFormat.format(selectedDate);

        // Insert the new record into the database
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transactions (part_name, part_number, supplier_name, stocks, timestamp) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, partName);
            ps.setString(2, partNumber);
            ps.setString(3, supplierName);
            ps.setInt(4, stocks);
            ps.setString(5, formattedTimestamp); // Alternatively, you could convert to a Timestamp if needed

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting transaction failed, no rows affected.");
            }

            // Retrieve the auto-generated ID for the new row
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    // Add the new row to the table model
                    Object[] rowData = {newId, partName, partNumber, supplierName, stocks, formattedTimestamp};
                    tableModel.addRow(rowData);
                    JOptionPane.showMessageDialog(this, "Record inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new SQLException("Inserting transaction failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting record.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Clear the input fields after insertion
        clearFields();
    }

    private void updateRow() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(idField.getText());
                String partName = partNameField.getText();
                String partNumber = partNumberField.getText();
                String supplierName = supplierNameField.getText();
                String stocks = stocksField.getText();
                Date selectedDate = transactDatePicker.getDate();
                if (stocks.isBlank() || selectedDate == null) {
                    JOptionPane.showMessageDialog(this, "Please check your inputs!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Format the date with time
                String formattedTimestamp = dateFormat.format(selectedDate);

                tableModel.setValueAt(id, selectedRow, 0);
                tableModel.setValueAt(partName, selectedRow, 1);
                tableModel.setValueAt(partNumber, selectedRow, 2);
                tableModel.setValueAt(supplierName, selectedRow, 3);
                tableModel.setValueAt(stocks, selectedRow, 4);
                tableModel.setValueAt(formattedTimestamp, selectedRow, 5);

                try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement()) {
                    String sql = "UPDATE transactions SET part_name = '" + partName
                            + "', part_number = '" + partNumber
                            + "', supplier_name = '" + supplierName
                            + "', stocks = '" + stocks
                            + "', timestamp = '" + formattedTimestamp
                            + "' WHERE id = " + id;
                    stmt.executeUpdate(sql);
                    JOptionPane.showMessageDialog(this, "Record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating record.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                clearFields();
            }
        }
    }

    private void deleteRow() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                tableModel.removeRow(selectedRow);
                try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement()) {
                    String sql = "DELETE FROM transactions WHERE id = " + id;
                    stmt.executeUpdate(sql);
                    JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                clearFields();
            }
        }
    }

    private void deleteAllRows() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all records?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM transactions");
                tableModel.setRowCount(0);
                JOptionPane.showMessageDialog(this, "All records deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting records.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importExcel() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
            JFileChooser fileChooser = new JFileChooser(desktopPath);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (FileInputStream fis = new FileInputStream(selectedFile); XSSFWorkbook workbook = new XSSFWorkbook(fis); Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement()) {

                    for (int i = 2; i <= workbook.getSheetAt(0).getLastRowNum(); i++) {
                        XSSFRow row = workbook.getSheetAt(0).getRow(i);
                        String partName = row.getCell(1).getStringCellValue();
                        String partNumber = row.getCell(2).getStringCellValue();
                        String supplierName = row.getCell(3).getStringCellValue();
                        int stocks = 0;
                        Cell stocksCell = row.getCell(4);
                        if (stocksCell.getCellType() == CellType.NUMERIC) {
                            stocks = (int) Math.round(stocksCell.getNumericCellValue());
                        }
                        PreparedStatement ps = conn.prepareStatement(
                                "INSERT INTO transactions (part_name, part_number, supplier_name, stocks) VALUES (?, ?, ?, ?)",
                                Statement.RETURN_GENERATED_KEYS);
                        ps.setString(1, partName);
                        ps.setString(2, partNumber);
                        ps.setString(3, supplierName);
                        ps.setInt(4, stocks);
                        ps.executeUpdate();
                        ResultSet generatedKeys = ps.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            // For imported rows, the timestamp can be left empty (or set via the DB's default)
                            Object[] rowData = {id, partName, partNumber, supplierName, stocks, ""};
                            tableModel.addRow(rowData);
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Excel file imported successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error importing Excel file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void clearFields() {
        idField.setText("");
        partNameField.setText("");
        partNumberField.setText("");
        supplierNameField.setText("");
        stocksField.setText("");
        transactDatePicker.setDate(null);
    }
}
