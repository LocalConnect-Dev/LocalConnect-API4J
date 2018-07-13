package ga.local_connect.api.util;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager implements Closeable {
    private final int timeout;
    private final String user;
    private final String password;
    private final String url;

    private Connection connection;

    public SQLManager(String type, String host, int port,
                      String database, String user, String password, int timeout) throws SQLException {
        this.timeout = timeout;
        this.user = user;
        this.password = password;
        this.url =
            "jdbc:" + type + "://" + host + ":" + port + "/"
                + database + "?useSSL=false";

        connect();
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(
            url, user, password
        );
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkTimeout() throws SQLException {
        if (!connection.isValid(timeout)) {
            connect();
        }
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        checkTimeout();
        return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    }
}
