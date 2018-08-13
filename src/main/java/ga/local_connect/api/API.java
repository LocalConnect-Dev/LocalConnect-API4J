package ga.local_connect.api;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.http.HttpStatuses;
import ga.local_connect.api.object.*;
import ga.local_connect.api.util.SQLManager;
import ga.local_connect.api.util.UUIDHelper;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class API {
    private static final int MAX_OBJECTS = 100;
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

    private static Region getRegion(String id) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `regions` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.REGION_NOT_FOUND
                    );
                }

                return new Region(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    private static Group getGroup(String id) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `groups` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.GROUP_NOT_FOUND
                    );
                }

                return new Group(
                    rs.getString("id"),
                    getRegion(rs.getString("region")),
                    rs.getString("name"),
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
                    getGroup(rs.getString("group")),
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
                    getGroup(rs.getString("group")),
                    rs.getString("name"),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    private static Document getDocument(String id) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `documents` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.USER_NOT_FOUND
                    );
                }

                return new Document(
                    rs.getString("id"),
                    getUser(rs.getString("author")),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    public static List<Board> getBoards(Group group) throws SQLException, LocalConnectException {
        var boards = new ArrayList<Board>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `boards` WHERE `group` = ?"
                + " ORDER BY `id` DESC LIMIT ?"
        )) {
            stmt.setString(1, group.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    boards.add(
                        new Board(
                            rs.getString("id"),
                            group,
                            getDocument(rs.getString("document")),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return boards;
    }

    public static List<Event> getEvents(User user) throws SQLException, LocalConnectException {
        var events = new ArrayList<Event>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `events` WHERE `author` = (SELECT `id` FROM `users` WHERE `group` = ?)"
                + " ORDER BY `id` DESC LIMIT ?"
        )) {
            stmt.setString(1, user.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(
                        new Event(
                            rs.getString("id"),
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            rs.getTimestamp("date"),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return events;
    }

    public static List<Post> getUserPosts(User user) throws SQLException, LocalConnectException {
        var posts = new ArrayList<Post>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `posts` WHERE `author` = ?"
                + " ORDER BY `id` DESC LIMIT ?"
        )) {
            stmt.setString(1, user.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(
                        new Post(
                            rs.getString("id"),
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return posts;
    }

    public static List<Post> getGroupPosts(Group group) throws SQLException, LocalConnectException {
        var posts = new ArrayList<Post>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `posts` WHERE `author` = (SELECT `id` FROM `users` WHERE `group` = ?)"
                + " ORDER BY `id` DESC LIMIT ?"
        )) {
            stmt.setString(1, group.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(
                        new Post(
                            rs.getString("id"),
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return posts;
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
