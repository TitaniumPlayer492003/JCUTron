/*
    cd "c:\Users\User\Desktop\Code\.java_projects\Chess_Utility_Tool\JCUT\src\" ; if ($?) { javac -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" CreateDatabase.java } ; if ($?) { java -cp ".;C:\Program Files (x86)\MySQL\Connector Java 8.0\mysql-connector-j-8.0.32.jar" CreateDatabase }

    */
import java.sql.*;

public class CreateDatabase {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String db_name = "JCUT";
            String db_username = "root";
            String db_password = "root";

            Connection connection = DriverManager.getConnection(url, db_username, db_password);

            String sql = "CREATE DATABASE" + " " + db_name;

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            System.out.println("Database created successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
