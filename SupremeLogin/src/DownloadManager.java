import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;

public class DownloadManager extends JFrame {
    // Color scheme
	private final Color PRIMARY_COLOR = new Color(40, 53, 147);         // Indigo (Dark Blue)
	private final Color SECONDARY_COLOR = new Color(25, 118, 210);      // Medium Blue / Material Blue 700
	private final Color ACCENT_COLOR = new Color(255, 171, 64);         // Amber / Orange Accent
	private final Color BACKGROUND_COLOR = new Color(245, 245, 245);    // Light Gray / Almost White
	private final Color CARD_COLOR = Color.WHITE;                       // White
	private final Color TEXT_PRIMARY = new Color(0, 0, 0);              // Pure Black
	private final Color TEXT_SECONDARY = new Color(51, 51, 51);         // Dark Gray

	private final Color SUCCESS_COLOR = new Color(46, 125, 50);         // Green / Material Green 700
	private final Color ERROR_COLOR = new Color(198, 40, 40);           // Red / Material Red 800
	// Define darker background colors
	private final Color DARK_SECONDARY_COLOR = new Color(13, 71, 161); // Dark Blue (Material Blue 900)
	private final Color DARK_ERROR_COLOR = new Color(183, 28, 28);     // Dark Red (Material Red 900)

    // UI Components
    private JTextField urlField;
    private JButton downloadButton;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JLabel statusLabel;
    private JComboBox<String> qualityCombo;
    private JButton pauseButton;
    private JButton cancelButton;
    private ExecutorService executor;
    private File desktopDir;
    private DownloadTask currentDownload;

    public DownloadManager() {
        setTitle("{ESL/} Download Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        // Get user's desktop directory
        desktopDir = new File(System.getProperty("user.home"), "Desktop");
        executor = Executors.newCachedThreadPool();
        
        // Main panel with modern look
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Add components to main panel
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createUrlPanel(), BorderLayout.CENTER);
        mainPanel.add(createControlPanel(), BorderLayout.WEST);
        mainPanel.add(createProgressPanel(), BorderLayout.SOUTH);
        
        // Create tabbed pane for different views
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Downloads", createDownloadsPanel());
        tabbedPane.addTab("History", createHistoryPanel());
        tabbedPane.addTab("Settings", createSettingsPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.EAST);
        
        add(mainPanel);
    }
    

    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // App title
        JLabel titleLabel = new JLabel("{ESL/} INTERNET DOWNLOAD MANAGER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
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
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(closeButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createUrlPanel() {
        JPanel urlPanel = new JPanel(new BorderLayout(10, 10));
        urlPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(20, 20, 20, 20),
            new TitledBorder("Download URL")
        ));
        urlPanel.setBackground(CARD_COLOR);
        
        JLabel urlLabel = new JLabel("Enter URL:");
        urlLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        urlLabel.setForeground(TEXT_PRIMARY);
        
        urlField = new JTextField();
        urlField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        urlField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        downloadButton = new JButton("START DOWNLOAD");
        styleButton(downloadButton, ACCENT_COLOR, Color.BLACK);
        downloadButton.addActionListener(e -> startDownload());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(downloadButton);
        
        urlPanel.add(urlLabel, BorderLayout.NORTH);
        urlPanel.add(urlField, BorderLayout.CENTER);
        urlPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return urlPanel;
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(20, 20, 20, 20),
            new TitledBorder("Download Options")
        ));
        controlPanel.setBackground(CARD_COLOR);
        
        // Quality selection
        JPanel qualityPanel = new JPanel(new BorderLayout());
        qualityPanel.setOpaque(false);
        JLabel qualityLabel = new JLabel("Quality:");
        qualityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        qualityLabel.setForeground(TEXT_PRIMARY);
        
        qualityCombo = new JComboBox<>(new String[]{"Best", "1080p", "720p", "480p", "Audio Only"});
        styleComboBox(qualityCombo);
        
        qualityPanel.add(qualityLabel, BorderLayout.NORTH);
        qualityPanel.add(qualityCombo, BorderLayout.CENTER);
        
        // Download location
        JPanel locationPanel = new JPanel(new BorderLayout());
        locationPanel.setOpaque(false);
        JLabel locationLabel = new JLabel("Download Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        locationLabel.setForeground(TEXT_PRIMARY);
        
        JLabel locationValue = new JLabel("Desktop");
        locationValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        locationValue.setForeground(TEXT_SECONDARY);
        
        JButton changeLocationBtn = new JButton("Change");
        styleButton(changeLocationBtn, SECONDARY_COLOR, Color.BLACK);
        changeLocationBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        changeLocationBtn.setPreferredSize(new Dimension(80, 25));
        changeLocationBtn.addActionListener(e -> changeDownloadLocation());
        
        JPanel locationValuePanel = new JPanel(new BorderLayout());
        locationValuePanel.setOpaque(false);
        locationValuePanel.add(locationValue, BorderLayout.WEST);
        locationValuePanel.add(changeLocationBtn, BorderLayout.EAST);
        
        locationPanel.add(locationLabel, BorderLayout.NORTH);
        locationPanel.add(locationValuePanel, BorderLayout.CENTER);
        
        // Download controls
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        actionPanel.setOpaque(false);
        
     // Buttons using darker backgrounds
        pauseButton = new JButton("PAUSE");
        styleButton(pauseButton, DARK_SECONDARY_COLOR, Color.WHITE); // White text for contrast
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(e -> pauseDownload());

        cancelButton = new JButton("CANCEL");
        styleButton(cancelButton, DARK_ERROR_COLOR, Color.WHITE); // White text for contrast
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(e -> cancelDownload());
        
        actionPanel.add(pauseButton);
        actionPanel.add(cancelButton);
        
        controlPanel.add(qualityPanel);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(locationPanel);
        controlPanel.add(Box.createVerticalStrut(15));
        controlPanel.add(actionPanel);
        
        return controlPanel;
    }
    
    private JPanel createProgressPanel() {
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        progressPanel.setBackground(BACKGROUND_COLOR);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        progressBar.setForeground(SUCCESS_COLOR);
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        statusLabel = new JLabel("Ready to download");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(TEXT_SECONDARY);
        
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return progressPanel;
    }
    
    private JPanel createDownloadsPanel() {
        JPanel downloadsPanel = new JPanel(new BorderLayout());
        downloadsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        downloadsPanel.setBackground(CARD_COLOR);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(250, 250, 250));
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Download Log"));
        
        downloadsPanel.add(scrollPane);
        return downloadsPanel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        historyPanel.setBackground(CARD_COLOR);
        
        JTable historyTable = new JTable(new Object[][]{
            {"File1.mp4", "Completed", "1.2 GB", "Yesterday"},
            {"File2.pdf", "Failed", "350 MB", "2 days ago"},
            {"File3.mp3", "Completed", "8.5 MB", "1 week ago"}
        }, new String[]{"File Name", "Status", "Size", "Date"});
        
        historyTable.setFillsViewportHeight(true);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Download History"));
        
        historyPanel.add(scrollPane);
        return historyPanel;
    }
    
    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        settingsPanel.setBackground(CARD_COLOR);
        
        // Connection settings
        JPanel connectionPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        connectionPanel.setBorder(BorderFactory.createTitledBorder("Connection Settings"));
        connectionPanel.setBackground(CARD_COLOR);
        
        JCheckBox speedLimitCheck = new JCheckBox("Limit download speed");
        JTextField speedLimitField = new JTextField("1000");
        JLabel speedLimitLabel = new JLabel("KB/s");
        
        JPanel speedLimitPanel = new JPanel(new BorderLayout());
        speedLimitPanel.add(speedLimitField, BorderLayout.CENTER);
        speedLimitPanel.add(speedLimitLabel, BorderLayout.EAST);
        
        connectionPanel.add(speedLimitCheck);
        connectionPanel.add(speedLimitPanel);
        
        // Appearance settings
        JPanel appearancePanel = new JPanel(new GridLayout(0, 1, 5, 5));
        appearancePanel.setBorder(BorderFactory.createTitledBorder("Appearance"));
        appearancePanel.setBackground(CARD_COLOR);
        
        JCheckBox darkModeCheck = new JCheckBox("Dark mode");
        JComboBox<String> themeCombo = new JComboBox<>(new String[]{"Blue", "Green", "Red", "Purple"});
        
        appearancePanel.add(darkModeCheck);
        appearancePanel.add(themeCombo);
        
        settingsPanel.add(connectionPanel);
        settingsPanel.add(Box.createVerticalStrut(15));
        settingsPanel.add(appearancePanel);
        
        return settingsPanel;
    }
    
    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100, 30), 1),
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
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
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
    
    private void changeDownloadLocation() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select Download Location");
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            desktopDir = chooser.getSelectedFile();
            log("Download location changed to: " + desktopDir.getAbsolutePath());
        }
    }
    
    private void startDownload() {
        String url = urlField.getText().trim();
        if (url.isEmpty()) {
            updateStatus("Please enter a URL", true);
            return;
        }
        
        String selectedQuality = (String) qualityCombo.getSelectedItem();
        
        if (isYouTubeUrl(url)) {
            downloadYouTubeVideo(url, selectedQuality);
        } else {
            downloadRegularFile(url);
        }
        
        downloadButton.setEnabled(false);
        pauseButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }
    
    private void pauseDownload() {
        if (currentDownload != null) {
            if (pauseButton.getText().equals("PAUSE")) {
                currentDownload.pause();
                pauseButton.setText("RESUME");
                updateStatus("Download paused", false);
            } else {
                currentDownload.resume();
                pauseButton.setText("PAUSE");
                updateStatus("Download resumed", false);
            }
        }
    }
    
    private void cancelDownload() {
        if (currentDownload != null) {
            currentDownload.cancel();
            updateStatus("Download cancelled", true);
            resetControls();
        }
    }
    
    private void resetControls() {
        downloadButton.setEnabled(true);
        pauseButton.setEnabled(false);
        cancelButton.setEnabled(false);
        pauseButton.setText("PAUSE");
    }
    
    private boolean isYouTubeUrl(String url) {
        return url.matches("^(https?://)?(www\\.)?(youtube\\.com|youtu\\.?be)/.+$");
    }
    
    private void downloadYouTubeVideo(String url, String quality) {
        updateStatus("Preparing YouTube download...", false);
        log("Starting YouTube download: " + url);
        
        currentDownload = new DownloadTask(url, true, quality);
        executor.execute(currentDownload);
    }
    
    private void downloadRegularFile(String url) {
        updateStatus("Starting download...", false);
        log("Starting download: " + url);
        
        currentDownload = new DownloadTask(url, false, null);
        executor.execute(currentDownload);
    }
    
    private void updateProgressFromYtDlpOutput(String line) {
        if (line.contains("% of")) {
            try {
                String percentStr = line.split("%")[0].trim().split(" ")[1];
                int percent = (int) Double.parseDouble(percentStr);
                SwingUtilities.invokeLater(() -> progressBar.setValue(percent));
            } catch (Exception e) {
                log("Couldn't parse progress: " + line);
            }
        }
    }
    
    private String getFileName(URL url) {
        String fileName = url.getPath();
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        
        if (fileName.isEmpty() || !fileName.contains(".")) {
            fileName = "download_" + System.currentTimeMillis() + ".tmp";
        }
        
        return fileName;
    }
    
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + new Date() + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    private void updateStatus(String message, boolean isError) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            statusLabel.setForeground(isError ? ERROR_COLOR : TEXT_PRIMARY);
        });
    }
    
    private class DownloadTask implements Runnable {
        private final String fileURL;
        private final boolean isYouTube;
        private final String quality;
        private volatile boolean paused = false;
        private volatile boolean cancelled = false;
        private long downloadedBytes = 0;
        private long fileSize = 0;
        
        public DownloadTask(String fileURL, boolean isYouTube, String quality) {
            this.fileURL = fileURL;
            this.isYouTube = isYouTube;
            this.quality = quality;
        }
        
        @Override
        public void run() {
            if (isYouTube) {
                downloadYouTube();
            } else {
                downloadRegular();
            }
        }
        
        private void downloadYouTube() {
            try {
                String ytDlpPath = "yt-dlp"; // Make sure yt-dlp is installed
                
                String format = "";
                switch (quality) {
                    case "1080p": format = "bestvideo[height<=1080]+bestaudio/best[height<=1080]"; break;
                    case "720p": format = "bestvideo[height<=720]+bestaudio/best[height<=720]"; break;
                    case "480p": format = "bestvideo[height<=480]+bestaudio/best[height<=480]"; break;
                    case "Audio Only": format = "bestaudio"; break;
                    default: format = "bestvideo+bestaudio/best";
                }
                
                ProcessBuilder pb = new ProcessBuilder(
                    ytDlpPath,
                    "-f", format,
                    "-o", desktopDir.getAbsolutePath() + "/%(title)s.%(ext)s",
                    fileURL
                );
                
                Process process = pb.start();
                
                // Read output stream
                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            log(line);
                            if (line.contains("[download]")) {
                                updateProgressFromYtDlpOutput(line);
                            }
                        }
                    } catch (IOException e) {
                        log("Error reading output: " + e.getMessage());
                    }
                }).start();
                
                // Read error stream
                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            log("ERROR: " + line);
                        }
                    } catch (IOException e) {
                        log("Error reading errors: " + e.getMessage());
                    }
                }).start();
                
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    updateStatus("YouTube download completed!", false);
                } else {
                    updateStatus("YouTube download failed", true);
                }
                
            } catch (Exception e) {
                log("YouTube download error: " + e.getMessage());
                updateStatus("Error downloading YouTube video", true);
            } finally {
                SwingUtilities.invokeLater(() -> resetControls());
            }
        }
        
        private void downloadRegular() {
            try {
                URL fileUrl = new URL(fileURL);
                HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
                connection.connect();
                
                fileSize = connection.getContentLength();
                if (fileSize <= 0) {
                    throw new IOException("Could not determine file size");
                }
                
                String fileName = getFileName(fileUrl);
                File outputFile = new File(desktopDir, fileName);
                
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        if (cancelled) {
                            throw new InterruptedException("Download cancelled");
                        }
                        
                        while (paused && !cancelled) {
                            Thread.sleep(500);
                        }
                        
                        outputStream.write(buffer, 0, bytesRead);
                        downloadedBytes += bytesRead;
                        
                        // Update progress
                        int progress = (int) (downloadedBytes * 100 / fileSize);
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setValue(progress);
                            statusLabel.setText(String.format("Downloading: %.1fMB/%.1fMB", 
                                downloadedBytes/(1024.0*1024.0), fileSize/(1024.0*1024.0)));
                        });
                    }
                    
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(100);
                        updateStatus("Download completed: " + fileName, false);
                    });
                    log("Download completed: " + outputFile.getAbsolutePath());
                }
            } catch (Exception e) {
                if (!(e instanceof InterruptedException)) {
                    log("Download error: " + e.getMessage());
                    updateStatus("Download failed", true);
                }
            } finally {
                SwingUtilities.invokeLater(() -> resetControls());
            }
        }
        
        public void pause() {
            paused = true;
        }
        
        public void resume() {
            paused = false;
        }
        
        public void cancel() {
            cancelled = true;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            DownloadManager manager = new DownloadManager();
            manager.setVisible(true);
        });
    }
}