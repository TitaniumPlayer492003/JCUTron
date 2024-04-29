/*    
    cd "c:\Users\User\Desktop\Code\.java_projects\Chess_Utility_Tool\JCUT\src\" ; if ($?) { javac -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" App.java } ; if ($?) { java -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" App; if ($?) {rm *.class}}
 */

import javax.swing.*;

public class App {
    public static void main(String... avi) {
        System.out.println("Hi Mom!");

        // Create the main application window
        JFrame appWindow = Authenticator.createAppWindow();

        // Create the login page panel and add it to the app window
        JPanel loginPagePanel = Authenticator.createLoginPage(appWindow);
        appWindow.add(loginPagePanel);

        // Make the app window visible
        appWindow.setVisible(true);
    }
}
