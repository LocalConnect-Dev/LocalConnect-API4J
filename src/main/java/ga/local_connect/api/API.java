package ga.local_connect.api;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.http.HttpStatuses;
import ga.local_connect.api.object.CreatedSession;
import ga.local_connect.api.object.Profile;
import ga.local_connect.api.object.Session;
import ga.local_connect.api.object.User;
import ga.local_connect.api.util.SQLManager;
import ga.local_connect.api.util.UUIDHelper;

import java.sql.SQLException;
import java.sql.Timestamp;

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

    public static User getUserByToken(String token) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `users` WHERE `token` = ?")) {
            stmt.setString(1, token);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.INVALID_USER_SECRET
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

    public static Profile getProfile(User user) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `profiles` WHERE `user` = ?")) {
            stmt.setString(1, user.getId());

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.PROFILE_NOT_FOUND
                    );
                }

                return new Profile(
                    rs.getString("id"),
                    user,
                    rs.getString("hobbies"),
                    rs.getString("favorites"),
                    rs.getString("mottoes"),
                    rs.getTimestamp("updated_at")
                );
            }
        }
    }

    public static CreatedSession createSession(User user) throws SQLException {
        var id = UUIDHelper.generate();
        var secret = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `sessions` (`id`, `user`, `secret`, `created_at`) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setString(3, secret);
            stmt.setTimestamp(4, createdAt);

            stmt.executeUpdate();
        }

        return new CreatedSession(id, user, secret, createdAt);
    }
}
