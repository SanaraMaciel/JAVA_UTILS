import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/conversion_history";
        String user = "root";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }
}
