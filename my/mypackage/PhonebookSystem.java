import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Vector;
package myPackage;

public class PhonebookSystem {
    private DefaultTableModel tableModel;
    private JTable contactTable;
    private JTextField firstNameField, lastNameField, locationField, phoneField;
    private JComboBox<String> groupComboBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PhonebookSystem().createAndShowGUI());
    }

    public void createAndShowGUI() {
        // Main frame
        JFrame frame = new JFrame("PHONE BOOK");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(204, 248, 255));

        // Title label
        JLabel titleLabel = new JLabel("PHONE BOOK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(300, 10, 400, 40);
        frame.add(titleLabel);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        searchLabel.setBounds(20, 60, 60, 25);
        frame.add(searchLabel);

        JTextField searchField = new JTextField();
        searchField.setBounds(80, 60, 200, 25);
        frame.add(searchField);

        String[] columnNames = {"FIRSTNAME", "LASTNAME", "LOCATION", "PHONE", "GROUP"};
        tableModel = new DefaultTableModel(columnNames, 0);
        contactTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(contactTable);
        tableScrollPane.setBounds(20, 100, 600, 400);
        frame.add(tableScrollPane);

        loadContactsFromFile();

        JLabel detailsLabel = new JLabel("Enter Contact Details");
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        detailsLabel.setBounds(640, 100, 200, 25);
        frame.add(detailsLabel);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        firstNameLabel.setBounds(640, 140, 100, 25);
        frame.add(firstNameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(740, 140, 200, 25);
        frame.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lastNameLabel.setBounds(640, 180, 100, 25);
        frame.add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(740, 180, 200, 25);
        frame.add(lastNameField);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        locationLabel.setBounds(640, 220, 100, 25);
        frame.add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(740, 220, 200, 25);
        frame.add(locationField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setBounds(640, 260, 100, 25);
        frame.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(740, 260, 200, 25);
        frame.add(phoneField);

        JLabel groupLabel = new JLabel("Group:");
        groupLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        groupLabel.setBounds(640, 300, 100, 25);
        frame.add(groupLabel);

        groupComboBox = new JComboBox<>();
        groupComboBox.addItem("Friend");
        groupComboBox.addItem("Family");
        groupComboBox.addItem("Work");
        groupComboBox.addItem("Other");
        groupComboBox.setBounds(740, 300, 200, 25);
        frame.add(groupComboBox);

        JButton addButton = new JButton("Add Contact");
        addButton.setBounds(660, 380, 120, 30);
        frame.add(addButton);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(820, 380, 120, 30);
        frame.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(660, 420, 120, 30);
        frame.add(deleteButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(820, 420, 120, 30);
        frame.add(clearButton);

        JButton filterButton = new JButton("Filter by Group");
        filterButton.setBounds(730, 460, 140, 40);
        frame.add(filterButton);

        addButton.addActionListener(e -> addContact());
        updateButton.addActionListener(e -> editContact());
        deleteButton.addActionListener(e -> deleteContact());
        clearButton.addActionListener(e -> clearFields());
        searchField.addActionListener(e -> searchContact(searchField.getText()));
        filterButton.addActionListener(e-> filterByGroup());

        frame.setVisible(true);
    }

    private void addContact() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String location = locationField.getText();
        String phone = phoneField.getText();
        String group = (String) groupComboBox.getSelectedItem();

        if (firstName.isEmpty() || lastName.isEmpty() || location.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        tableModel.addRow(new Object[]{firstName, lastName, location, phone, group});
        saveContactsToFile();
        clearFields();
    }

    private void filterByGroup() {
        String selectedGroup = (String) groupComboBox.getSelectedItem();
        if (selectedGroup == null) {
            JOptionPane.showMessageDialog(null, "No group selected.");
            return;
        }

        DefaultTableModel filteredModel = new DefaultTableModel(new String[]{"FIRSTNAME", "LASTNAME", "LOCATION", "PHONE", "GROUP"}, 0);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (selectedGroup.equals(tableModel.getValueAt(i, 4))) {
                filteredModel.addRow(new Object[]{
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4)
                });
            }
        }
        contactTable.setModel(filteredModel);
    }

    private void editContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit.");
            return;
        }

        tableModel.setValueAt(firstNameField.getText(), selectedRow, 0);
        tableModel.setValueAt(lastNameField.getText(), selectedRow, 1);
        tableModel.setValueAt(locationField.getText(), selectedRow, 2);
        tableModel.setValueAt(phoneField.getText(), selectedRow, 3);
        tableModel.setValueAt(groupComboBox.getSelectedItem(), selectedRow, 4);

        saveContactsToFile();
        clearFields();
    }

    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a contact to delete.");
            return;
        }

        tableModel.removeRow(selectedRow);
        saveContactsToFile();
        clearFields();
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        locationField.setText("");
        phoneField.setText("");
        groupComboBox.setSelectedIndex(0);
    }

    private void searchContact(String query) {
        String lowerCaseQuery = query.toLowerCase();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String firstName = ((String) tableModel.getValueAt(i, 0)).toLowerCase();
            String lastName = ((String) tableModel.getValueAt(i, 1)).toLowerCase();
            String location = ((String) tableModel.getValueAt(i, 2)).toLowerCase();
            String phone = ((String) tableModel.getValueAt(i, 3)).toLowerCase();

            if (firstName.contains(lowerCaseQuery) || lastName.contains(lowerCaseQuery) || location.contains(lowerCaseQuery) || phone.contains(lowerCaseQuery)) {
                contactTable.setRowSelectionInterval(i, i);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Contact not found.");
    }

    private void saveContactsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Vector<?> row = tableModel.getDataVector().elementAt(i);
                writer.write(String.join(",", row.stream().map(Object::toString).toArray(String[]::new)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContactsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tableModel.addRow(line.split(","));
            }
        } catch (IOException e) {

        }
    }
}
