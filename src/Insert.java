/* 
    cd "c:\Users\User\Desktop\Code\.java_projects\Chess_Utility_Tool\JCUT\src\" ; if ($?) { javac -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" Insert.java } ; if ($?) { java -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" Insert; if ($?) {rm *.class}}
*/

import java.sql.*;

public class Insert {
    public static void main(String[] args) {
        try {
            // Database connection details
            String url = "jdbc:mysql://localhost:3306/JCUT"; // Assuming JCUT is your database name
            String db_username = "root";
            String db_password = "";

            // Name and password to insert
            String nameToInsert = "Avijeet";
            String passwordToInsert = "123";

            // Establishing database connection
            Connection connection = DriverManager.getConnection(url, db_username, db_password);

            // SQL query to insert name and password into the 'user' table
            String insertSQL = "INSERT INTO user (name, password) VALUES (?, ?)";

            // Creating a prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, nameToInsert);
            preparedStatement.setString(2, passwordToInsert);

            // Executing the query
            int rowsAffected = preparedStatement.executeUpdate();

            // Closing resources
            preparedStatement.close();
            connection.close();

            // Checking if insertion was successful
            if (rowsAffected > 0) {
                System.out.println("Name and password inserted successfully.");
            } else {
                System.out.println("Failed to insert name and password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
