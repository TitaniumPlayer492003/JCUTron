/*
    cd "c:\Users\User\Desktop\Code\.java_projects\Chess_Utility_Tool\JCUT\src\" ; if ($?) { javac -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" Create.java } ; if ($?) { java -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" Create; if ($?) {rm *.class}}
    */

import java.sql.*;

public class Create {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String db_name = "JCUT";
            String db_username = "root";
            String db_password = "";
            String table_name = "user";

            Connection connection = DriverManager.getConnection(url, db_username, db_password);

            // Create the database
            String createDB = "CREATE DATABASE IF NOT EXISTS " + db_name;
            Statement createDBStatement = connection.createStatement();
            createDBStatement.executeUpdate(createDB);
            createDBStatement.close();

            // Use the database
            String useDB = "USE " + db_name;
            Statement useDBStatement = connection.createStatement();
            useDBStatement.executeUpdate(useDB);
            useDBStatement.close();

            // Create the table
            String createTable = "CREATE TABLE IF NOT EXISTS " + table_name
                    + " (name varchar(50), password varchar(50))";
            Statement createTableStatement = connection.createStatement();
            createTableStatement.executeUpdate(createTable);
            createTableStatement.close();

            System.out.println("Operation completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
