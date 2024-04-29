import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

public class Authenticator {
    public static JFrame createAppWindow() {
        JFrame appWindow = new JFrame("Login");
        appWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appWindow.setSize(1080, 810);
        appWindow.setResizable(false);
        appWindow.setLocationRelativeTo(null); // Center the window on the screen

        // Create a custom JPanel with background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Load the image from file
                    Image backgroundImage = ImageIO.read(new File("visual-assets\\27476-2485227023.jpg"));

                    // Draw the image
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Set layout for the background panel
        backgroundPanel.setLayout(new BorderLayout());

        // Add the background panel to the JFrame's content pane
        appWindow.setContentPane(backgroundPanel);

        return appWindow;
    }

    public static void main(String[] args) {
        // Create the main application window
        JFrame appWindow = createAppWindow();

        // Create the login page panel and add it to the app window
        JPanel loginPagePanel = createLoginPage(appWindow);
        appWindow.add(loginPagePanel);

        // Make the app window visible
        appWindow.setVisible(true);
    }

    public static JPanel createLoginPage(JFrame parentFrame) {
        JPanel loginPagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Load the image from file
                    Image backgroundImage = ImageIO.read(new File("visual-assets\\27476-2485227023.jpg"));

                    // Draw the image
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        // Set layout for the login page panel
        loginPagePanel.setLayout(null); // Using absolute layout for manual component placement

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Set bounds for each component
        usernameLabel.setBounds(430, 305, 65, 25);
        passwordLabel.setBounds(430, 345, 65, 25);
        usernameField.setBounds(505, 305, 140, 25);
        passwordField.setBounds(505, 345, 140, 25);
        loginButton.setBounds(535, 385, 80, 30);

        // Add components directly to the login page panel
        loginPagePanel.add(usernameLabel);
        loginPagePanel.add(usernameField);
        loginPagePanel.add(passwordLabel);
        loginPagePanel.add(passwordField);
        loginPagePanel.add(loginButton);

        // Add action listener to the login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword()); // Retrieve password as a String

            if (isValidCredentials(username, password)) {
                // If credentials are valid, create a GamePanel instance and launch the game
                GAME.NEW(); // This line is commented because it's not clear where you want to launch the game
                parentFrame.dispose(); // Close the login window
            } else {
                // If credentials are invalid, show an error message
                JOptionPane.showMessageDialog(parentFrame, "Incorrect username or password", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        return loginPagePanel;
    }

    private static boolean isValidCredentials(String username, String password) {
        // JDBC URL, username, and password
        String url = "jdbc:mysql://localhost:3306/JCUT";
        String dbUsername = "root";
        String dbPassword = "";

        // SQL query to check if the provided username and password match any entry in the database
        String sql = "SELECT * FROM user WHERE name = ? AND password = ?";

        // Initialize database resources
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Prepare the SQL statement
            statement = connection.prepareStatement(sql);

            // Set the username and password parameters
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute the query
            resultSet = statement.executeQuery();

            // If any rows are returned, the username and password are valid
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Return false in case of any error
        } finally {
            // Close database resources in a finally block to ensure proper resource management
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
