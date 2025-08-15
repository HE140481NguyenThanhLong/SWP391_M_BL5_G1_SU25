package spring.backend.m_bl5_g1_su25;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Simple database connection test without Spring framework
 * Tests direct JDBC connection to Azure SQL Database
 */
public class DatabaseConnectionTest {

    private static final String JDBC_URL = "jdbc:sqlserver://g1-swp391-bl5-su25.database.windows.net:1433;database=G1_SWP391;encrypt=true;trustServerCertificate=true;loginTimeout=30;";
    private static final String USERNAME = "group1";
    private static final String PASSWORD = "AHT8zf7LooEBNuV";

    @Test
    public void testDatabaseConnection() {
        Connection connection = null;
        try {
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            System.out.println("Attempting to connect to Azure SQL Database...");
            System.out.println("URL: " + JDBC_URL);
            System.out.println("Username: " + USERNAME);

            // Establish connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            if (connection != null && !connection.isClosed()) {
                System.out.println("✓ Database connection successful!");

                // Test a simple query
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT 1 as test_value");

                if (resultSet.next()) {
                    int testValue = resultSet.getInt("test_value");
                    System.out.println("✓ Query test successful. Test value: " + testValue);
                }

                resultSet.close();
                statement.close();
            }

        } catch (ClassNotFoundException e) {
            System.err.println("✗ SQL Server JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close connection
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("✓ Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
