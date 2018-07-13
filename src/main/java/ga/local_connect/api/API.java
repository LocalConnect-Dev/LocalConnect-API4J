package ga.local_connect.api;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.http.HttpStatuses;
import ga.local_connect.api.object.Session;
import ga.local_connect.api.object.User;
import ga.local_connect.api.util.SQLManager;

import java.sql.SQLException;

public class API {
    private static final SQLManager sql = LocalConnect.getSQL();

    public static Session getSession(String secret) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `sessions` WHERE `secret` = ?")) {
            stmt.setString(1, secret);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.UNAUTHORIZED,
                        APIErrorType.INVALID_SESSION_SECRET
                    );
                }

                return new Session(
                    rs.getString("id"),
                    getUser(rs.getString("user")),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    private static User getUser(String id) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `users` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.USER_NOT_FOUND
                    );
                }

                return new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }
}
