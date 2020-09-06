package db.dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class DBConn {

    private static DBConn instance;
    private Connection conn;

    private DBConn () {
        ResourceBundle rb = ResourceBundle.getBundle("db");
        String host = rb.getString("host");
        String port = rb.getString("port");
        String db = rb.getString("db");
        String user = rb.getString("user");
        String password = rb.getString("password");

        String jdbcURL = MessageFormat.format(
                "jdbc:mysql://{0}:{1}/{2}",
                host, port, db);
        try {
            conn = DriverManager.getConnection(jdbcURL, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static DBConn getInstance() {
        if (instance == null) {
            instance = new DBConn();
        }
        return instance;
    }

    public Connection connection () {
        return conn;
    }
}
