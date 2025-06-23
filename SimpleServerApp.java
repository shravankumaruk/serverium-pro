import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class SimpleServerApp extends JFrame {
    private JTextField portField;
    private JTextArea logArea;
    private JButton startButton, stopButton, selectDirButton, saveLogsButton, openPageButton, viewLogsButton, clearLogsButton, aboutButton;
    private JButton zoomInButton, zoomOutButton;
    private JLabel portLabel;
    private File serveDirectory = null;
    private ServerSocket serverSocket = null;
    private volatile boolean isRunning = false;
    private float fontSize = 12f;

    public SimpleServerApp() {
         ImageIcon icon = new ImageIcon("logo.png");
        setIconImage(icon.getImage());
        setTitle("Serverium Pro");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        portLabel = new JLabel("Port:");
        portField = new JTextField(10);
        inputPanel.add(portLabel);
        inputPanel.add(portField);

        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);
        inputPanel.add(startButton);
        inputPanel.add(stopButton);

        selectDirButton = new JButton("Select Directory");
        inputPanel.add(selectDirButton);

        openPageButton = new JButton("Open Page");
        openPageButton.setEnabled(false);
        inputPanel.add(openPageButton);

        aboutButton = new JButton("About");
        inputPanel.add(aboutButton);

        add(inputPanel, BorderLayout.NORTH);

        logArea = new JTextArea(15, 50);
        logArea.setEditable(false);
        logArea.setFont(logArea.getFont().deriveFont(fontSize));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(logScrollPane, BorderLayout.CENTER);

        JPanel logPanel = new JPanel();
        saveLogsButton = new JButton("Save Logs");
        viewLogsButton = new JButton("View Logs");
        clearLogsButton = new JButton("Clear Logs");
        logPanel.add(saveLogsButton);
        logPanel.add(viewLogsButton);
        logPanel.add(clearLogsButton);

        zoomInButton = new JButton("Zoom In");
        zoomOutButton = new JButton("Zoom Out");
        logPanel.add(zoomInButton);
        logPanel.add(zoomOutButton);
        add(logPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());
        selectDirButton.addActionListener(e -> selectDirectory());
        saveLogsButton.addActionListener(e -> saveLogs());
        openPageButton.addActionListener(e -> openPage());
        viewLogsButton.addActionListener(e -> viewLogs());
        clearLogsButton.addActionListener(e -> clearLogs());
        aboutButton.addActionListener(e -> showAbout());
        zoomInButton.addActionListener(e -> zoomIn());
        zoomOutButton.addActionListener(e -> zoomOut());

        InputMap im = logArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = logArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("control PLUS"), "zoomIn");
        am.put("zoomIn", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { zoomIn(); }
        });
        im.put(KeyStroke.getKeyStroke("control MINUS"), "zoomOut");
        am.put("zoomOut", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { zoomOut(); }
        });
    }

    private void zoomIn() {
        fontSize += 2f;
        logArea.setFont(logArea.getFont().deriveFont(fontSize));
    }

    private void zoomOut() {
        fontSize = Math.max(10f, fontSize - 2f);
        logArea.setFont(logArea.getFont().deriveFont(fontSize));
    }

    private void startServer() {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
            if (port < 1024) {
                log("Error: Reserved ports are not allowed. Please use a port above 1024.");
                return;
            }
        } catch (NumberFormatException ex) {
            log("Error: Invalid port number.");
            return;
        }
        if (serveDirectory == null) {
            log("Error: You must select a directory to serve.");
            return;
        }
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                log("Server started on port: " + port);
                isRunning = true;
                SwingUtilities.invokeLater(() -> {
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    openPageButton.setEnabled(true);
                    viewLogsButton.setEnabled(true);
                });
                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException ex) {
                log("Error: Unable to start server.");
            }
        }).start();
    }

    private void stopServer() {
        try {
            if (serverSocket != null) {
                isRunning = false;
                serverSocket.close();
                log("Server stopped.");
            }
        } catch (IOException ex) {
            log("Error: Unable to stop server.");
        } finally {
            SwingUtilities.invokeLater(() -> {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                openPageButton.setEnabled(false);
            });
        }
    }

    private void selectDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            serveDirectory = chooser.getSelectedFile();
            log("Directory selected: " + serveDirectory.getAbsolutePath());
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void saveLogs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("logs.srep"));
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".srep")) {
                file = new File(file.getAbsolutePath() + ".srep");
            }
            try (PrintWriter out = new PrintWriter(file)) {
                String logs = logArea.getText();
                String encodedLogs = encodeLogs(logs, "shravan"); //change it to your desired password.
                out.write(encodedLogs);
                log("Logs saved as: " + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                log("Error: Unable to save logs.");
            }
        }
    }

    private String encodeLogs(String logs, String password) {
        String combined = password + logs;
        return Base64.getEncoder().encodeToString(combined.getBytes());
    }

    private void viewLogs() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".srep")) {
                log("Error: Please select a .srep file.");
                return;
            }
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                StringBuilder decodedLogs = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    decodedLogs.append(line);
                }
                String logs = decodeLogs(decodedLogs.toString(), "shravan");
                logArea.setText(logs);
                log("Logs viewed from: " + file.getAbsolutePath());
            } catch (IOException e) {
                log("Error: Unable to open logs.");
            }
        }
    }

    private String decodeLogs(String encodedLogs, String password) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedLogs);
        String decodedString = new String(decodedBytes);
        return decodedString.replaceFirst(password, "");
    }

    private void clearLogs() {
        logArea.setText("");
    }

    private void openPage() {
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:" + portField.getText()));
        } catch (Exception e) {
            log("Error: Unable to open the page.");
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                handleClientRequest(clientSocket);
            } catch (IOException e) {
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                }
            }
        }

        private void handleClientRequest(Socket clientSocket) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String requestLine = in.readLine();
            if (requestLine == null) return;
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) return;
            String requestedPath = requestParts[1];
            File fileToServe;
            if ("/".equals(requestedPath)) {
                fileToServe = new File(serveDirectory, "index.html");
            } else {
                fileToServe = new File(serveDirectory, requestedPath.substring(1));
            }
            if (fileToServe.isDirectory()) {
                serveErrorPage(clientSocket, 403);
            } else if (!fileToServe.exists()) {
                serveNotFoundMessage(clientSocket);
            } else {
                serveFile(clientSocket, fileToServe);
            }
        }

        private void serveFile(Socket clientSocket, File fileToServe) throws IOException {
            String contentType = getContentType(fileToServe);
            String responseHeader = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + fileToServe.length() + "\r\n\r\n";
            clientSocket.getOutputStream().write(responseHeader.getBytes());
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToServe))) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    clientSocket.getOutputStream().write(buffer, 0, bytesRead);
                }
            }
        }

        private void serveNotFoundMessage(Socket clientSocket) throws IOException {
            String responseMessage = "<html><body><h1>404 Not Found</h1><p>Error: File not found.</p>" +
                    "<p>Please add an <strong>index.html</strong> file to run the server.</p></body></html>";
            String responseHeader = "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + responseMessage.length() + "\r\n\r\n";
            clientSocket.getOutputStream().write(responseHeader.getBytes());
            clientSocket.getOutputStream().write(responseMessage.getBytes());
        }

        private void serveErrorPage(Socket clientSocket, int errorCode) throws IOException {
            String errorPagePath;
            String errorMessage;
            if (errorCode == 404) {
                errorPagePath = new File(serveDirectory, "404.html").getAbsolutePath();
                errorMessage = "Error: File not found.";
            } else {
                errorPagePath = new File(serveDirectory, "403.html").getAbsolutePath();
                errorMessage = "Error: Access forbidden.";
            }
            File errorFile = new File(errorPagePath);
            if (errorFile.exists()) {
                String contentType = getContentType(errorFile);
                String responseHeader = "HTTP/1.1 " + errorCode + " " + errorMessage + "\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + errorFile.length() + "\r\n\r\n";
                clientSocket.getOutputStream().write(responseHeader.getBytes());
                byte[] content = Files.readAllBytes(Paths.get(errorFile.getAbsolutePath()));
                clientSocket.getOutputStream().write(content);
            } else {
                String responseHeader = "HTTP/1.1 " + errorCode + " " + errorMessage + "\r\n\r\n" + errorMessage;
                clientSocket.getOutputStream().write(responseHeader.getBytes());
            }
        }

        private String getContentType(File file) {
            String name = file.getName().toLowerCase();
            if (name.endsWith(".html")) return "text/html";
            if (name.endsWith(".css")) return "text/css";
            if (name.endsWith(".js")) return "application/javascript";
            if (name.endsWith(".png")) return "image/png";
            if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
            if (name.endsWith(".gif")) return "image/gif";
            return "application/octet-stream";
        }
    }

    private void showAbout() {
        String aboutMessage = ".srep Files are viewed only by this app.\nThis app was built by Shravan Kumar UK.";
        JOptionPane.showMessageDialog(this, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleServerApp app = new SimpleServerApp();
            app.setVisible(true);
        });
    }
}
