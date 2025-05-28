import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.*;

public class SupremeLogin extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel titleLabel;
    private boolean passwordVisible = false;
    private JButton toggleVisibility;
    
    public SupremeLogin() {
        // Set up the frame with modern look
        setTitle("World Domination Control Panel");
        setSize(500, 350); // Increased size for better header
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        
        // Create main panel with layered pane for depth
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(500, 350));
        
        // Background panel with gradient
        JPanel bgPanel = new JPanel() {
            
        	@Override
        	protected void paintComponent(Graphics g) {
        	    super.paintComponent(g);
        	    Graphics2D g2d = (Graphics2D) g;
        	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        	    // Red to Orange Gradient
        	    Color color1 = new Color(255, 0, 0);     // Red
        	    Color color2 = new Color(255, 140, 0);   // Dark Orange

        	    GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        	    g2d.setPaint(gp);
        	    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        	}


        };
        bgPanel.setBounds(0, 0, 500, 350);
        layeredPane.add(bgPanel, JLayeredPane.DEFAULT_LAYER);
        
        // Create content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        mainPanel.setBounds(0, 0, 500, 350);
        layeredPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);
        
        // Create world domination header - MADE MORE VISIBLE
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        titleLabel = new JLabel("WORLD DOMINATION CONTROL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 32)); // Increased font size
        titleLabel.setForeground(new Color(255, 255, 255)); // Changed to white for better visibility
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0)); // Added padding
        
        JLabel subtitleLabel = new JLabel("Secure Authentication Portal", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14)); // Increased size
        subtitleLabel.setForeground(new Color(220, 220, 220)); // Brighter text
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Add glow effect animation
        Timer glowTimer = new Timer(100, e -> {
            float glow = 0.5f + 0.5f * (float)Math.sin(System.currentTimeMillis() * 0.005);
            titleLabel.setForeground(new Color(255, (int)(255 * glow), (int)(255 * glow)));
        });
        glowTimer.start();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create form panel with glass effect
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        formPanel.setOpaque(false);
        
        // Username field with modern look
        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.setOpaque(false);
        JLabel usernameLabel = new JLabel("AGENT ID:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(200, 200, 200));
        usernameField = new JTextField();
        styleTextField(usernameField);
        
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        
        // Password field with toggle visibility
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("ACCESS CODE:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(200, 200, 200));
        
        JPanel passwordFieldPanel = new JPanel(new BorderLayout());
        passwordFieldPanel.setOpaque(false);
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        
        // Toggle password visibility button
        toggleVisibility = new JButton("SHOW");
        toggleVisibility.setContentAreaFilled(false);
        toggleVisibility.setBorder(BorderFactory.createEmptyBorder());
        toggleVisibility.setForeground(new Color(180, 180, 180));
        toggleVisibility.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toggleVisibility.setPreferredSize(new Dimension(60, 30));
        toggleVisibility.addActionListener(e -> togglePasswordVisibility());
        
        passwordFieldPanel.add(passwordField, BorderLayout.CENTER);
        passwordFieldPanel.add(toggleVisibility, BorderLayout.EAST);
        
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordFieldPanel, BorderLayout.CENTER);
        
     // Button panel with enhanced buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // === AUTHENTICATE Button ===
        loginButton = new JButton("AUTHENTICATE");
        styleMainButton(loginButton, new Color(220, 20, 60), Color.WHITE); // Strong red tone
        loginButton.addActionListener(e -> login());

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(255, 40, 80)); // Lighter hover red
                loginButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(220, 20, 60));
                loginButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100, 100), 2),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }
        });

        // === ABORT Button ===
        JButton exitButton = new JButton("ABORT");
        styleMainButton(exitButton, new Color(120, 20, 20), Color.WHITE); // Darker danger tone
        exitButton.addActionListener(e -> System.exit(0));

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(160, 30, 30)); // Slightly more vibrant hover
                exitButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(120, 20, 20));
                exitButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100, 100), 2),
                    BorderFactory.createEmptyBorder(8, 25, 8, 25)
                ));
            }
        });

        buttonPanel.add(exitButton);
        buttonPanel.add(loginButton);

        // Add components to form panel
        formPanel.add(usernamePanel);
        formPanel.add(passwordPanel);
        formPanel.add(buttonPanel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add close button at top-right
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.setContentAreaFilled(false);
        closeButton.setForeground(new Color(180, 180, 180));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(new Color(255, 80, 80));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(new Color(180, 180, 180));
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(closeButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Add drag functionality for the undecorated window
        MouseAdapter ma = new MouseAdapter() {
            private Point offset;
            
            @Override
            public void mousePressed(MouseEvent e) {
                offset = e.getPoint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                Point newLocation = e.getLocationOnScreen();
                newLocation.translate(-offset.x, -offset.y);
                setLocation(newLocation);
            }
        };
        
        headerPanel.addMouseListener(ma);
        headerPanel.addMouseMotionListener(ma);
        
        add(layeredPane);
        setVisible(true);
        
        // Add subtle background animation
        new Timer(50, e -> bgPanel.repaint()).start();
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(60, 60, 70, 150));
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100, 100)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setSelectionColor(new Color(180, 10, 30, 150));
    }
    
    private void styleMainButton(JButton button, Color bgColor, Color textColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100, 100), 2),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            passwordField.setEchoChar((char)0);
            toggleVisibility.setText("HIDE");
            toggleVisibility.setForeground(new Color(220, 220, 220));
        } else {
            passwordField.setEchoChar('•');
            toggleVisibility.setText("SHOW");
            toggleVisibility.setForeground(new Color(180, 180, 180));
        }
    }
    
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            animateError();
            JOptionPane.showMessageDialog(this, 
                "All fields must be completed", 
                "ACCESS DENIED", 
                JOptionPane.ERROR_MESSAGE);
        } else {
            loginButton.setEnabled(false);
            loginButton.setText("VERIFYING...");
            
            Timer timer = new Timer(2000, e -> {
                loginButton.setEnabled(true);
                loginButton.setText("AUTHENTICATE");
                
                if ("admin".equals(username) && "supreme".equals(password)) {
                    JOptionPane.showMessageDialog(this, 
                        "Welcome, Supreme Leader", 
                        "ACCESS GRANTED", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    animateError();
                    JOptionPane.showMessageDialog(this, 
                        "Invalid credentials detected", 
                        "INTRUDER ALERT", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    private void animateError() {
        Timer shakeTimer = new Timer(20, null);
        final int[] offsets = {5, -5, 4, -4, 3, -3, 2, -2, 1, -1, 0};
        final int[] counter = {0};
        
        Point originalLocation = getLocation();
        
        shakeTimer.addActionListener(e -> {
            if (counter[0] < offsets.length) {
                setLocation(originalLocation.x + offsets[counter[0]], originalLocation.y);
                counter[0]++;
            } else {
                setLocation(originalLocation);
                shakeTimer.stop();
            }
        });
        shakeTimer.start();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new SupremeLogin();
        });
    }
}