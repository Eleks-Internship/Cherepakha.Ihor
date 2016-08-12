import java.sql.*;

/**
 * Created by Ihor on 18.07.2016.
 */
public class ConnectDB {

    private final String URL = "jdbc:postgresql://localhost:5432/ConDB";
    private final String USERNAME = "postgres";
    private final String PASSWORD = "postgres";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public ConnectDB(){
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
        }


    }
}
