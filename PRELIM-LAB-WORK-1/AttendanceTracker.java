import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * AttendanceTracker - A Java Swing application for tracking attendance.
 * This version only accepts "BSIT 1st Year" as a valid course.
 */
public class AttendanceTracker {
    
    private JFrame frame;
    private JTextField nameField;
    private JTextField courseField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    
    public AttendanceTracker() {
        createAndShowGUI();
    }
    
    private void createAndShowGUI() {
        // Create the main frame
        frame = new JFrame("Attendance Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Increased size slightly for better readability
        frame.setSize(450, 350); 
        frame.setLocationRelativeTo(null); 
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Attendance Tracking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); 
        
        Font labelFont = new Font("Arial", Font.PLAIN, 12);
        Font inputFont = new Font("Arial", Font.PLAIN, 12);

        // Row 0: Attendance Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        nameField = new JTextField();
        nameField.setFont(inputFont);
        formPanel.add(nameField, gbc);
        
        // Row 1: Course/Year
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel courseLabel = new JLabel("Course/Year:");
        courseLabel.setFont(labelFont);
        formPanel.add(courseLabel, gbc);
        
        gbc.gridx = 1;
        courseField = new JTextField();
        courseField.setFont(inputFont);
        // Tip: You can pre-fill it for the user if you want: courseField.setText("BSIT 1st Year");
        formPanel.add(courseField, gbc);
        
        // Row 2: Time In
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel timeLabel = new JLabel("Time In:");
        timeLabel.setFont(labelFont);
        formPanel.add(timeLabel, gbc);
        
        gbc.gridx = 1;
        timeInField = new JTextField();
        timeInField.setFont(inputFont);
        timeInField.setEditable(false); 
        timeInField.setBackground(Color.WHITE);
        
        // Set System Time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        timeInField.setText(LocalDateTime.now().format(formatter));
        formPanel.add(timeInField, gbc);
        
        // Row 3: E-Signature
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel signatureLabel = new JLabel("E-Signature:");
        signatureLabel.setFont(labelFont);
        formPanel.add(signatureLabel, gbc);
        
        gbc.gridx = 1;
        eSignatureField = new JTextField();
        eSignatureField.setFont(new Font("Monospaced", Font.PLAIN, 10));
        eSignatureField.setEditable(false);
        eSignatureField.setBackground(Color.WHITE);
        eSignatureField.setText(UUID.randomUUID().toString());
        formPanel.add(eSignatureField, gbc);
        
        // Submit Button
        JButton submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.setPreferredSize(new Dimension(200, 40));
        
        // Logic for Submit Button
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String course = courseField.getText().trim();
            
            // Validation Logic
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name!", "Error", JOptionPane.WARNING_MESSAGE);
            } 
            // Check if it is EXACTLY "BSIT 1st Year" (ignoring case)
            else if (!course.equalsIgnoreCase("BSIT 1st Year")) {
                JOptionPane.showMessageDialog(frame, 
                    "Invalid Course!\nOnly 'BSIT 1st Year' is allowed to register here.", 
                    "Access Denied", 
                    JOptionPane.ERROR_MESSAGE);
            } 
            else {
                // Success
                JOptionPane.showMessageDialog(frame, 
                    "Attendance recorded for: " + name + "\nCourse: " + course,
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear and Refresh for next entry
                nameField.setText("");
                courseField.setText("");
                timeInField.setText(LocalDateTime.now().format(formatter));
                eSignatureField.setText(UUID.randomUUID().toString());
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceTracker());
    }
}
