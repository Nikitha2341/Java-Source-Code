package schools;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Schools extends Frame {

    // Labels
    Label l1, l2, l3, l4, l5, headerLabel, footerLabel;

    // TextFields
    TextField tfUniID, tfSchCode, tfSchName, tfSchLocation, tfSchEmail;

    // Buttons
    Button btnAdd, btnUpdate, btnDelete, btnRetrieve;

    // Database Connection
    Connection con;
    PreparedStatement pst;

    public Schools() {
        setTitle("University School Management");

        // Use BorderLayout for screen adaptation
        setLayout(new BorderLayout());

        // ===== Header =====
        Panel header = new Panel();
        header.setBackground(new Color(0, 100, 0));
        headerLabel = new Label("University School Management", Label.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel);
        add(header, BorderLayout.NORTH);

        // ===== Footer =====
        Panel footer = new Panel();
        footer.setBackground(new Color(0, 100, 0));
        footerLabel = new Label("© 2025 University Management System");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        // ===== Center Form Panel =====
        Panel formPanel = new Panel(null); // Absolute layout
        formPanel.setPreferredSize(new Dimension(1000, 400));

        // Initialize Labels
        l1 = new Label("University ID:");
        l2 = new Label("School Code:");
        l3 = new Label("School Name:");
        l4 = new Label("School Location:");
        l5 = new Label("School Email:");

        // Initialize TextFields
        tfUniID = new TextField();
        tfSchCode = new TextField();
        tfSchName = new TextField();
        tfSchLocation = new TextField();
        tfSchEmail = new TextField();

        // Initialize Buttons
        btnAdd = new Button("Add School");
        btnUpdate = new Button("Update School");
        btnDelete = new Button("Delete School");
        btnRetrieve = new Button("Retrieve School");

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int xLabel = (screenWidth / 2) - 180;
        int xField = (screenWidth / 2);
        int y = 250;
        int hGap = 40;

        l1.setBounds(xLabel, y, 150, 25); tfUniID.setBounds(xField, y, 200, 25); y += hGap;
        l2.setBounds(xLabel, y, 150, 25); tfSchCode.setBounds(xField, y, 200, 25); y += hGap;
        l3.setBounds(xLabel, y, 150, 25); tfSchName.setBounds(xField, y, 200, 25); y += hGap;
        l4.setBounds(xLabel, y, 150, 25); tfSchLocation.setBounds(xField, y, 200, 25); y += hGap;
        l5.setBounds(xLabel, y, 150, 25); tfSchEmail.setBounds(xField, y, 200, 25); y += hGap;

        btnAdd.setBounds(xLabel, y, 100, 30);
        btnUpdate.setBounds(xLabel + 110, y, 100, 30);
        btnDelete.setBounds(xLabel + 220, y, 100, 30);
        btnRetrieve.setBounds(xLabel + 330, y, 100, 30);


        // Add components to formPanel
        formPanel.add(l1); formPanel.add(tfUniID);
        formPanel.add(l2); formPanel.add(tfSchCode);
        formPanel.add(l3); formPanel.add(tfSchName);
        formPanel.add(l4); formPanel.add(tfSchLocation);
        formPanel.add(l5); formPanel.add(tfSchEmail);
        formPanel.add(btnAdd); formPanel.add(btnUpdate);
        formPanel.add(btnDelete); formPanel.add(btnRetrieve);

        add(formPanel, BorderLayout.CENTER);

        // Connect to DB
        connectToDatabase();

        // Event Listeners
        btnAdd.addActionListener(e -> addSchool());
        btnUpdate.addActionListener(e -> updateSchool());
        btnDelete.addActionListener(e -> deleteSchool());
        btnRetrieve.addActionListener(e -> retrieveSchool());

        // Frame Settings
        setSize(1000, 500);
        setVisible(true);
        setLocationRelativeTo(null); // Center the window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/university_db?useSSL=false";
        String user = "root";
        String password = "Nikitha@2030";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL database successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to MySQL!");
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfUniID.setText("");
        tfSchCode.setText("");
        tfSchName.setText("");
        tfSchLocation.setText("");
        tfSchEmail.setText("");
    }

    private void addSchool() {
        try {
            String query = "INSERT INTO schools (uni_id, sch_code, sch_name, sch_location, sch_email) VALUES (?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, tfUniID.getText());
            pst.setString(2, tfSchCode.getText());
            pst.setString(3, tfSchName.getText());
            pst.setString(4, tfSchLocation.getText());
            pst.setString(5, tfSchEmail.getText());
            pst.executeUpdate();
            System.out.println("School Added Successfully!");
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void updateSchool() {
//        try {
//            String query = "UPDATE schools SET uni_id = ?, sch_name = ?, sch_location = ?, sch_email = ? WHERE sch_code = ?";
//            pst = con.prepareStatement(query);
//            pst.setString(1, tfUniID.getText());
//            pst.setString(2, tfSchName.getText());
//            pst.setString(3, tfSchLocation.getText());
//            pst.setString(4, tfSchEmail.getText());
//            pst.setString(5, tfSchCode.getText());
//            int rowsAffected = pst.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("School Updated Successfully!");
//                clearForm();
//            } else {
//                System.out.println("No matching school found for update.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
private void updateSchool() {
    try {
        // Step 1: Retrieve existing values based on sch_code
        String selectQuery = "SELECT * FROM schools WHERE sch_code = ?";
        pst = con.prepareStatement(selectQuery);
        pst.setString(1, tfSchCode.getText());
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            // Step 2: Decide what to update
            String uniId = tfUniID.getText().isEmpty() ? rs.getString("uni_id") : tfUniID.getText();
            String schName = tfSchName.getText().isEmpty() ? rs.getString("sch_name") : tfSchName.getText();
            String schLocation = tfSchLocation.getText().isEmpty() ? rs.getString("sch_location") : tfSchLocation.getText();
            String schEmail = tfSchEmail.getText().isEmpty() ? rs.getString("sch_email") : tfSchEmail.getText();

            // Step 3: Update with final values
            String updateQuery = "UPDATE schools SET uni_id = ?, sch_name = ?, sch_location = ?, sch_email = ? WHERE sch_code = ?";
            pst = con.prepareStatement(updateQuery);
            pst.setString(1, uniId);
            pst.setString(2, schName);
            pst.setString(3, schLocation);
            pst.setString(4, schEmail);
            pst.setString(5, tfSchCode.getText());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("School Updated Successfully!");
                clearForm();
            } else {
                System.out.println("No matching school found for update.");
            }
        } else {
            System.out.println("School not found with sch_code: " + tfSchCode.getText());
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    private void deleteSchool() {
        try {
            String query = "DELETE FROM schools WHERE sch_code = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, tfSchCode.getText());
            pst.executeUpdate();
            System.out.println("School Deleted Successfully!");
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retrieveSchool() {
        try {
            String query = "SELECT * FROM schools WHERE sch_code = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, tfSchCode.getText());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tfUniID.setText(rs.getString("uni_id"));
                tfSchName.setText(rs.getString("sch_name"));
                tfSchLocation.setText(rs.getString("sch_location"));
                tfSchEmail.setText(rs.getString("sch_email"));
                System.out.println("School Retrieved Successfully!");
            } else {
                System.out.println("No School Found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Main method
    public static void main(String[] args) {
        new Schools();
    }
}