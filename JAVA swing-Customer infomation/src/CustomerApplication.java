import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class CustomerApplication {
    private JFrame frame;
    private JLabel idLabel, lastNameLabel, firstNameLabel, phoneLabel;
    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton prevButton, nextButton;
    private ArrayList<Customer> customers;
    private int currentIndex = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerApplication().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Customer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        idLabel = new JLabel("ID:");
        lastNameLabel = new JLabel("Last Name:");
        firstNameLabel = new JLabel("First Name:");
        phoneLabel = new JLabel("Phone:");

        idField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        phoneField = new JTextField();

        idField.setEditable(false);
        lastNameField.setEditable(false);
        firstNameField.setEditable(false);
        phoneField.setEditable(false);

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(prevButton);
        panel.add(nextButton);

        frame.add(panel);

        prevButton.addActionListener(e -> showPreviousCustomer());
        nextButton.addActionListener(e -> showNextCustomer());

        loadCustomers();
        if (!customers.isEmpty()) {
            showCustomer(currentIndex);
        }

        frame.setVisible(true);
    }

    private void loadCustomers() {
        customers = new ArrayList<>();
        String url = "jdbc:mysql://127.0.0.1:3306/custome_info"; // Database URL
        String user = "root"; // Database user
        String password = ""; // Database password
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) { // Corrected table name

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("idcustomers"), // Corrected column name
                        rs.getString("last_name"), // Corrected column name
                        rs.getString("first_name"), // Corrected column name
                        rs.getString("phone") // Corrected column name
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void showCustomer(int index) {
        if (index >= 0 && index < customers.size()) {
            Customer customer = customers.get(index);
            idField.setText(String.valueOf(customer.getId()));
            lastNameField.setText(customer.getLastName());
            firstNameField.setText(customer.getFirstName());
            phoneField.setText(customer.getPhone());
        }
    }

    private void showPreviousCustomer() {
        if (currentIndex > 0) {
            currentIndex--;
            showCustomer(currentIndex);
        }
    }

    private void showNextCustomer() {
        if (currentIndex < customers.size() - 1) {
            currentIndex++;
            showCustomer(currentIndex);
        }
    }

    class Customer {
        private int id;
        private String lastName, firstName, phone;

        public Customer(int id, String lastName, String firstName, String phone) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.phone = phone;
        }

        public int getId() {
            return id;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getPhone() {
            return phone;
        }
    }
}
