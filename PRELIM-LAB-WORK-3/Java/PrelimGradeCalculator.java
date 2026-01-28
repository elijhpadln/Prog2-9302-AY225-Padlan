import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PrelimGradeCalculator extends JFrame {
    // Input fields
    private JTextField attendanceField;
    private JTextField lab1Field;
    private JTextField lab2Field;
    private JTextField lab3Field;
    private JTextField absentWithExcuseField;
    
    // Results display area
    private JTextArea resultsArea;
    
    public PrelimGradeCalculator() {
        setTitle("Prelim Grade Calculator");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("Prelim Grade Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 15));
        inputPanel.setBackground(Color.WHITE);
        
        // Attendance input
        JLabel attendanceLabel = new JLabel("Number of Attendances (0-5):");
        attendanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        attendanceField = createNumberField();
        inputPanel.add(attendanceLabel);
        inputPanel.add(attendanceField);
        
        // Absences with excuse letter (includes late enrollees)
        JLabel absentWithExcuseLabel = new JLabel("Excused Absences (late enrollment, sick, etc.):");
        absentWithExcuseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        absentWithExcuseField = createNumberField();
        inputPanel.add(absentWithExcuseLabel);
        inputPanel.add(absentWithExcuseField);
        
        // Lab 1 input
        JLabel lab1Label = new JLabel("Lab Work 1 Grade (0-100):");
        lab1Label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lab1Field = createNumberField();
        inputPanel.add(lab1Label);
        inputPanel.add(lab1Field);
        
        // Lab 2 input
        JLabel lab2Label = new JLabel("Lab Work 2 Grade (0-100):");
        lab2Label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lab2Field = createNumberField();
        inputPanel.add(lab2Label);
        inputPanel.add(lab2Field);
        
        // Lab 3 input
        JLabel lab3Label = new JLabel("Lab Work 3 Grade (0-100):");
        lab3Label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lab3Field = createNumberField();
        inputPanel.add(lab3Label);
        inputPanel.add(lab3Field);
        
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Results area
        JLabel resultsLabel = new JLabel("Computation Results");
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resultsLabel.setForeground(new Color(44, 62, 80));
        resultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(resultsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        resultsArea = new JTextArea(20, 40);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setBackground(new Color(241, 248, 255));
        resultsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        mainPanel.add(scrollPane);
        
        // Add document listeners for real-time calculation
        addRealTimeListener(attendanceField);
        addRealTimeListener(absentWithExcuseField);
        addRealTimeListener(lab1Field);
        addRealTimeListener(lab2Field);
        addRealTimeListener(lab3Field);
        
        add(mainPanel);
        
        // Initial calculation
        calculateGrade();
    }
    
    private JTextField createNumberField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 221, 221), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private void addRealTimeListener(JTextField field) {
        // Add key listener for immediate validation
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                enforceValidation(field);
                calculateGrade();
            }
        });
        
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                calculateGrade();
            }
            public void removeUpdate(DocumentEvent e) {
                calculateGrade();
            }
            public void insertUpdate(DocumentEvent e) {
                calculateGrade();
            }
        });
    }
    
    private void enforceValidation(JTextField field) {
        SwingUtilities.invokeLater(() -> {
            try {
                String text = field.getText().trim();
                if (text.isEmpty()) return;
                
                double value = Double.parseDouble(text);
                
                // Enforce attendance limit (0-5)
                if (field == attendanceField) {
                    if (value > 5) {
                        field.setText("5");
                    } else if (value < 0) {
                        field.setText("0");
                    }
                }
                
                // Enforce absences with excuse limit (0-5)
                if (field == absentWithExcuseField) {
                    if (value > 5) {
                        field.setText("5");
                    } else if (value < 0) {
                        field.setText("0");
                    }
                }
                
                // Enforce lab work grade limits (0-100)
                if (field == lab1Field || field == lab2Field || field == lab3Field) {
                    if (value > 100) {
                        field.setText("100");
                    } else if (value < 0) {
                        field.setText("0");
                    }
                }
            } catch (NumberFormatException ex) {
                // Invalid input, will be handled in calculateGrade
            }
        });
    }
    
    private void calculateGrade() {
        try {
            // Get input values
            String attendanceText = attendanceField.getText().trim();
            String absentWithExcuseText = absentWithExcuseField.getText().trim();
            String lab1Text = lab1Field.getText().trim();
            String lab2Text = lab2Field.getText().trim();
            String lab3Text = lab3Field.getText().trim();
            
            // Check if all required fields have values
            if (attendanceText.isEmpty() || lab1Text.isEmpty() || 
                lab2Text.isEmpty() || lab3Text.isEmpty()) {
                resultsArea.setText("Please enter all values to see results.");
                return;
            }
            
            double attendanceCount = Double.parseDouble(attendanceText);
            double absentWithExcuse = absentWithExcuseText.isEmpty() ? 0 : Double.parseDouble(absentWithExcuseText);
            double lab1 = Double.parseDouble(lab1Text);
            double lab2 = Double.parseDouble(lab2Text);
            double lab3 = Double.parseDouble(lab3Text);
            
            // Validate ranges
            if (attendanceCount < 0 || attendanceCount > 5) {
                resultsArea.setText("Attendance must be between 0 and 5.");
                return;
            }
            
            if (absentWithExcuse < 0 || absentWithExcuse > 5) {
                resultsArea.setText("Excused absences must be between 0 and 5.");
                return;
            }
            
            if (lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
                resultsArea.setText("Lab work grades must be between 0 and 100.");
                return;
            }
            
            // Calculate total absences (absences without excuse letter)
            double maxAttendances = 5;
            double effectiveAttendance = attendanceCount + absentWithExcuse;
            
            // Cap effective attendance at max
            if (effectiveAttendance > maxAttendances) {
                effectiveAttendance = maxAttendances;
            }
            
            double totalAbsences = maxAttendances - effectiveAttendance;
            
            // Check if student has 4 or more absences (FAILED)
            if (totalAbsences >= 4) {
                StringBuilder results = new StringBuilder();
                results.append("=== FAILED DUE TO ABSENCES ===\n\n");
                results.append(String.format("Present: %d\n", (int)attendanceCount));
                results.append(String.format("Excused Absences: %d\n", (int)absentWithExcuse));
                results.append(String.format("Unexcused Absences: %d\n\n", (int)totalAbsences));
                results.append("RESULT: FAILED\n\n");
                results.append("You have 4 or more unexcused absences.\n");
                results.append("According to policy, you automatically fail the course.");
                
                resultsArea.setText(results.toString());
                return;
            }
            
            // Calculate attendance score
            double attendanceScore = (effectiveAttendance / maxAttendances) * 100;
            
            // Calculations
            double labWorkAverage = (lab1 + lab2 + lab3) / 3;
            double classStanding = (attendanceScore * 0.40) + (labWorkAverage * 0.60);
            
            // Calculate required Prelim Exam scores
            double requiredForPass = (75 - (classStanding * 0.30)) / 0.70;
            double requiredForExcellent = (100 - (classStanding * 0.30)) / 0.70;
            
            // Determine student's standing
            String standing;
            if (classStanding >= 90) {
                standing = "Excellent! You have a very strong class standing.";
            } else if (classStanding >= 75) {
                standing = "Good! You have a passing class standing.";
            } else {
                standing = "You need to improve your class standing.";
            }
            
            // Build results text
            StringBuilder results = new StringBuilder();
            results.append("=== COMPUTATION RESULTS ===\n\n");
            
            results.append(String.format("Attendance Score: %.2f\n", attendanceScore));
            results.append(String.format("Present: %d, Excused Absences: %d, Unexcused Absences: %d\n", 
                (int)attendanceCount, (int)absentWithExcuse, (int)totalAbsences));
            results.append(String.format("Effective Attendance: %.0f out of 5\n\n", effectiveAttendance));
            
            results.append(String.format("Lab Work 1 Grade: %.2f\n", lab1));
            results.append(String.format("Lab Work 2 Grade: %.2f\n", lab2));
            results.append(String.format("Lab Work 3 Grade: %.2f\n", lab3));
            results.append(String.format("Lab Work Average: %.2f\n", labWorkAverage));
            results.append(String.format("Class Standing: %.2f\n\n", classStanding));
            
            results.append("=== REQUIRED PRELIM EXAM SCORES ===\n\n");
            
            // Passing requirement
            results.append("To Pass (75%):\n");
            results.append(String.format("Required Score: %.2f\n", requiredForPass));
            if (requiredForPass <= 0) {
                results.append("You've already secured a passing grade!\n\n");
            } else if (requiredForPass > 100) {
                results.append("Unfortunately, passing is mathematically impossible with your current class standing.\n\n");
            } else {
                results.append("You need this score on the Prelim Exam to pass.\n\n");
            }
            
            // Excellent requirement
            results.append("To Achieve Excellent (100%):\n");
            results.append(String.format("Required Score: %.2f\n", requiredForExcellent));
            if (requiredForExcellent <= 0) {
                results.append("You've already secured an excellent grade!\n\n");
            } else if (requiredForExcellent > 100) {
                results.append("This would require more than 100% on the exam.\n\n");
            } else {
                results.append("You need this score on the Prelim Exam for an excellent grade.\n\n");
            }
            
            // Student standing
            results.append("=== STUDENT STANDING ===\n\n");
            results.append(standing);
            
            resultsArea.setText(results.toString());
            
        } catch (NumberFormatException ex) {
            resultsArea.setText("Invalid input! Please enter valid numbers.");
        }
    }
    
    public static void main(String[] args) {
        // Use system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the GUI
        SwingUtilities.invokeLater(() -> {
            PrelimGradeCalculator calculator = new PrelimGradeCalculator();
            calculator.setVisible(true);
        });
    }
}