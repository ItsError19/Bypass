import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class SupremeLogin extends JFrame {
    // Modern Color Scheme
    private final Color PRIMARY_COLOR = new Color(48, 63, 159);    // Dark Blue
    private final Color SECONDARY_COLOR = new Color(83, 109, 254); // Bright Blue
    private final Color ACCENT_COLOR = new Color(255, 213, 79);    // Amber
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250); // Light Gray
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);      // Dark Gray
    private final Color TEXT_SECONDARY = new Color(117, 117, 117); // Medium Gray
    private final Color SUCCESS_COLOR = new Color(67, 160, 71);    // Green
    private final Color ERROR_COLOR = new Color(229, 57, 53);      // Red
    private final Color DISABLED_COLOR = new Color(189, 189, 189); // Light Gray

    public SupremeLogin() {
        setTitle("{ESL/} IDM Secure Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        
        // Main panel with shadow effect
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(0, 0, 0, 30), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header with gradient
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR, 
                    getWidth(), 0, SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        JLabel titleLabel = new JLabel("{ESL/} IDM LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Close button
        JButton closeButton = new JButton("close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setForeground(new Color(255, 255, 255, 180));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(new Color(255, 255, 255, 180));
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(closeButton, BorderLayout.EAST);
        headerPanel.add(topPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        // Username field
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridy = 1;
        JTextField usernameField = new JTextField();
        styleTextField(usernameField);
        formPanel.add(usernameField, gbc);
        
        // Password field
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 3;
        JPasswordField passwordField = new JPasswordField();
        styleTextField(passwordField);
        formPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton loginButton = new JButton("LOGIN");
        styleButton(loginButton, SECONDARY_COLOR, Color.BLACK);
        loginButton.addActionListener(e -> showTermsAndConditions(usernameField.getText(), new String(passwordField.getPassword())));
        formPanel.add(loginButton, gbc);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
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
        headerPanel.addMouseListener(ma);
        headerPanel.addMouseMotionListener(ma);
        
        add(mainPanel);
    }
    
    private void showTermsAndConditions(String username, String password) {
        if (!username.equals("Error 19") || !password.equals("supreme")) {
            JOptionPane.showMessageDialog(this, 
                "Invalid credentials", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog termsDialog = new JDialog(this, "Terms and Conditions", true);
        termsDialog.setSize(650, 450);
        termsDialog.setLocationRelativeTo(this);
        termsDialog.setUndecorated(true);
        termsDialog.setShape(new RoundRectangle2D.Double(0, 0, 650, 450, 20, 20));
        
        JPanel termsPanel = new JPanel(new BorderLayout());
        termsPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(0, 0, 0, 30), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        termsPanel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel termsHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR, 
                    getWidth(), 0, SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        termsHeader.setPreferredSize(new Dimension(getWidth(), 50));
        
        JLabel termsTitle = new JLabel("Terms and Conditions", SwingConstants.CENTER);
        termsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        termsTitle.setForeground(Color.WHITE);
        termsHeader.add(termsTitle, BorderLayout.CENTER);
        
        // Terms text
        JTextArea termsText = new JTextArea();
        termsText.setEditable(false);
        termsText.setLineWrap(true);
        termsText.setWrapStyleWord(true);
        termsText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        termsText.setText(getTermsText());
        
        JScrollPane scrollPane = new JScrollPane(termsText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Accept/Decline buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton acceptButton = new JButton("ACCEPT");
        styleButton(acceptButton, SUCCESS_COLOR, Color.BLACK);
        acceptButton.addActionListener(e -> {
            termsDialog.dispose();
            showPackagingProgress();
        });
        
        JButton declineButton = new JButton("DECLINE");
        styleButton(declineButton, ERROR_COLOR, Color.BLACK);
        declineButton.addActionListener(e -> termsDialog.dispose());
        
        buttonPanel.add(declineButton);
        buttonPanel.add(acceptButton);
        
        termsPanel.add(termsHeader, BorderLayout.NORTH);
        termsPanel.add(scrollPane, BorderLayout.CENTER);
        termsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        termsDialog.add(termsPanel);
        termsDialog.setVisible(true);
    }
    
    private void showPackagingProgress() {
        JDialog progressDialog = new JDialog(this, "Preparing Application", true);
        progressDialog.setSize(450, 180);
        progressDialog.setLocationRelativeTo(this);
        progressDialog.setUndecorated(true);
        progressDialog.setShape(new RoundRectangle2D.Double(0, 0, 450, 180, 20, 20));
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(0, 0, 0, 30), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        progressPanel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel progressHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR, 
                    getWidth(), 0, SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        progressHeader.setPreferredSize(new Dimension(getWidth(), 40));
        
        JLabel progressTitle = new JLabel("PACKAGING APPLICATION", SwingConstants.CENTER);
        progressTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressTitle.setForeground(Color.WHITE);
        progressHeader.add(progressTitle, BorderLayout.CENTER);
        
        // Progress content
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        
        JLabel statusLabel = new JLabel("Initializing...", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_PRIMARY);
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        progressBar.setForeground(SUCCESS_COLOR);
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        
        contentPanel.add(statusLabel, BorderLayout.NORTH);
        contentPanel.add(progressBar, BorderLayout.CENTER);
        
        progressPanel.add(progressHeader, BorderLayout.NORTH);
        progressPanel.add(contentPanel, BorderLayout.CENTER);
        
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
                    progressDialog.dispose();
                    dispose(); // Close login window
                    SwingUtilities.invokeLater(() -> {
                        DownloadManager manager = new DownloadManager();
                        manager.setVisible(true);
                    });
                }
            }
        }).start();
        
        progressDialog.add(progressPanel);
        progressDialog.setVisible(true);
    }
    
    private String getTermsText() {
        return "END USER LICENSE AGREEMENT\n\n" +
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
            "In no event shall the authors be liable for any damages arising from the use of this software.\n\n" +
            "By accepting these terms, you agree to be bound by all conditions of this agreement.";
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBackground(CARD_COLOR);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(SECONDARY_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 189, 189)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
    
    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighter(bgColor, 0.1f));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
            public void mousePressed(MouseEvent e) {
                button.setBackground(darker(bgColor, 0.1f));
            }
            public void mouseReleased(MouseEvent e) {
                button.setBackground(brighter(bgColor, 0.1f));
            }
        });
    }
    
    private Color brighter(Color color, float factor) {
        return new Color(
            Math.min((int)(color.getRed() * (1 + factor)), 255),
            Math.min((int)(color.getGreen() * (1 + factor)), 255),
            Math.min((int)(color.getBlue() * (1 + factor)), 255)
        );
    }
    
    private Color darker(Color color, float factor) {
        return new Color(
            Math.max((int)(color.getRed() * (1 - factor)), 0),
            Math.max((int)(color.getGreen() * (1 - factor)), 0),
            Math.max((int)(color.getBlue() * (1 - factor)), 0)
        );
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            SupremeLogin login = new SupremeLogin();
            login.setVisible(true);
        });
    }
}