import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class SupremeLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private DownloadManager downloadManager;

    public SupremeLogin() {
        setTitle("Secure Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 35));
        
        // Header
        JLabel titleLabel = new JLabel("SECURE LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 15));
        formPanel.setOpaque(false);
        
        // Username field
        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.setOpaque(false);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameField = new JTextField();
        styleTextField(usernameField);
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        
        // Password field
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        // Login button
        loginButton = new JButton("LOGIN");
        styleButton(loginButton);
        loginButton.addActionListener(e -> showTermsAndConditions());
        
        // Add components
        formPanel.add(usernamePanel);
        formPanel.add(passwordPanel);
        formPanel.add(loginButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Close button
        JButton closeButton = new JButton("X");
        closeButton.setContentAreaFilled(false);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(closeButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Dragging functionality
        MouseAdapter ma = new MouseAdapter() {
            private Point offset;
            @Override public void mousePressed(MouseEvent e) { offset = e.getPoint(); }
            @Override public void mouseDragged(MouseEvent e) {
                Point newLocation = e.getLocationOnScreen();
                newLocation.translate(-offset.x, -offset.y);
                setLocation(newLocation);
            }
        };
        titleLabel.addMouseListener(ma);
        titleLabel.addMouseMotionListener(ma);
    }
    
    private void showTermsAndConditions() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (!username.equals("admin") || !password.equals("supreme")) {
            JOptionPane.showMessageDialog(this, 
                "Invalid credentials", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFrame termsFrame = new JFrame("Terms and Conditions");
        termsFrame.setSize(600, 400);
        termsFrame.setLocationRelativeTo(this);
        termsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel termsPanel = new JPanel(new BorderLayout());
        termsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Terms text
        JTextArea termsText = new JTextArea();
        termsText.setEditable(false);
        termsText.setLineWrap(true);
        termsText.setWrapStyleWord(true);
        termsText.setText(
            "END USER LICENSE AGREEMENT\n\n" +
            "1. LICENSE GRANT\n" +
            "The software is licensed, not sold. This agreement only gives you some rights to use the software.\n\n" +
            "2. SCOPE OF LICENSE\n" +
            "You may not:\n" +
            "- Work around any technical limitations in the software\n" +
            "- Reverse engineer, decompile or disassemble the software\n" +
            "- Use the software for any illegal purpose\n\n" +
            "3. PRIVACY\n" +
            "Your use of the software operates as a consent to the collection and use of your data as described in our Privacy Policy.\n\n" +
            "4. DISCLAIMER OF WARRANTY\n" +
            "The software is provided 'as is' without warranty of any kind.\n\n" +
            "5. LIMITATION OF LIABILITY\n" +
            "In no event shall the authors be liable for any damages arising from the use of this software."
        );
        
        JScrollPane scrollPane = new JScrollPane(termsText);
        
        // Accept/Decline buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton acceptButton = new JButton("Accept");
        acceptButton.setBackground(new Color(76, 175, 80));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.addActionListener(e -> {
            termsFrame.dispose();
            showPackagingProgress();
        });
        
        JButton declineButton = new JButton("Decline");
        declineButton.setBackground(new Color(244, 67, 54));
        declineButton.setForeground(Color.WHITE);
        declineButton.addActionListener(e -> termsFrame.dispose());
        
        buttonPanel.add(acceptButton);
        buttonPanel.add(declineButton);
        
        termsPanel.add(new JLabel("Please read and accept the terms and conditions:"), BorderLayout.NORTH);
        termsPanel.add(scrollPane, BorderLayout.CENTER);
        termsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        termsFrame.add(termsPanel);
        termsFrame.setVisible(true);
    }
    
    private void showPackagingProgress() {
        JFrame progressFrame = new JFrame("Preparing Application");
        progressFrame.setSize(400, 150);
        progressFrame.setLocationRelativeTo(this);
        progressFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel statusLabel = new JLabel("Packaging application components...", JLabel.CENTER);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        
        progressPanel.add(statusLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // Simulate packaging progress
        new Timer(50, new ActionListener() {
            int progress = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                progress++;
                progressBar.setValue(progress);
                
                if (progress == 20) {
                    statusLabel.setText("Loading core modules...");
                } else if (progress == 40) {
                    statusLabel.setText("Configuring settings...");
                } else if (progress == 60) {
                    statusLabel.setText("Optimizing performance...");
                } else if (progress == 80) {
                    statusLabel.setText("Finalizing installation...");
                } else if (progress >= 100) {
                    ((Timer)e.getSource()).stop();
                    progressFrame.dispose();
                    dispose(); // Close login window
                    SwingUtilities.invokeLater(() -> {
                        DownloadManager manager = new DownloadManager();
                        manager.setVisible(true);
                    });
                }
            }
        }).start();
        
        progressFrame.add(progressPanel);
        progressFrame.setVisible(true);
    }
    
    private void styleTextField(JTextField field) {
        field.setBackground(new Color(50, 50, 55));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 75)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SupremeLogin login = new SupremeLogin();
            login.setVisible(true);
        });
    }
}