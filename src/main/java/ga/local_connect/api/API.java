package ga.local_connect.api;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.http.HttpStatuses;
import ga.local_connect.api.object.*;
import ga.local_connect.api.util.SQLManager;
import ga.local_connect.api.util.UUIDHelper;

import java.io.*;
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

    public static Region getRegion(String id) throws SQLException, LocalConnectException {
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

    public static List<Region> getRegions() throws SQLException {
        var regions = new ArrayList<Region>();
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `regions`")) {
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    regions.add(
                        new Region(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return regions;
    }

    public static Group getGroup(String id) throws SQLException, LocalConnectException {
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

    public static List<Group> getGroups(Region region) throws SQLException {
        var groups = new ArrayList<Group>();
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `groups` WHERE `region` = ?")) {
            stmt.setString(1, region.getId());

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(
                        new Group(
                            rs.getString("id"),
                            region,
                            rs.getString("name"),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return groups;
    }

    public static User getUser(String id) throws SQLException, LocalConnectException {
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
                    getImage(rs.getString("avatar")),
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
                    getImage(rs.getString("avatar")),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    public static List<User> getUsers(Group group) throws SQLException, LocalConnectException {
        var users = new ArrayList<User>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `users` WHERE `group` = ?"
                + " ORDER BY `created_at` DESC LIMIT ?"
        )) {
            stmt.setString(1, group.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(
                        new User(
                            rs.getString("id"),
                            group,
                            rs.getString("name"),
                            getImage(rs.getString("avatar")),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return users;
    }

    public static Document getDocument(String id) throws SQLException, LocalConnectException {
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

    public static Board getBoard(String id) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `boards` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.BOARD_NOT_FOUND
                    );
                }

                return new Board(
                    rs.getString("id"),
                    getGroup(rs.getString("group")),
                    getDocument(rs.getString("document")),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    public static List<Board> getBoards(Group group) throws SQLException, LocalConnectException {
        var boards = new ArrayList<Board>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `boards` WHERE `group` = ?"
                + " ORDER BY `created_at` DESC LIMIT ?"
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

    public static Event getEvent(String id) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `events` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.EVENT_NOT_FOUND
                    );
                }

                id = rs.getString("id");
                return new Event(
                    id,
                    getUser(rs.getString("author")),
                    getDocument(rs.getString("document")),
                    rs.getTimestamp("date"),
                    getEventAttendances(id),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    private static List<EventAttendance> getEventAttendances(String eventId) throws SQLException, LocalConnectException {
        var attendances = new ArrayList<EventAttendance>();
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `event_attendances` WHERE `event` = ?")) {
            stmt.setString(1, eventId);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attendances.add(
                        new EventAttendance(
                            rs.getString("id"),
                            getUser(rs.getString("user")),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return attendances;
    }

    public static List<Event> getUserEvents(User user) throws SQLException, LocalConnectException {
        var events = new ArrayList<Event>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `events` WHERE `author` = ?"
                + " ORDER BY `date` ASC LIMIT ?"
        )) {
            stmt.setString(1, user.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getString("id");

                    events.add(
                        new Event(
                            id,
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            rs.getTimestamp("date"),
                            getEventAttendances(id),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return events;
    }

    public static List<Event> getJoinedEvents(User user) throws SQLException, LocalConnectException {
        var events = new ArrayList<Event>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `events` WHERE `id` in (SELECT `event` FROM `event_attendances` WHERE `user` = ?)"
                + " ORDER BY `date` ASC LIMIT ?"
        )) {
            stmt.setString(1, user.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getString("id");

                    events.add(
                        new Event(
                            id,
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            rs.getTimestamp("date"),
                            getEventAttendances(id),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return events;
    }

    public static List<Event> getGroupEvents(Group group) throws SQLException, LocalConnectException {
        var events = new ArrayList<Event>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `events` WHERE `author` in (SELECT `id` FROM `users` WHERE `group` = ?)"
                + " ORDER BY `date` ASC LIMIT ?"
        )) {
            stmt.setString(1, group.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getString("id");

                    events.add(
                        new Event(
                            id,
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            rs.getTimestamp("date"),
                            getEventAttendances(id),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return events;
    }

    public static Post getPost(String id) throws SQLException, LocalConnectException {
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `posts` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.POST_NOT_FOUND
                    );
                }

                id = rs.getString("id");
                return new Post(
                    id,
                    getUser(rs.getString("author")),
                    getDocument(rs.getString("document")),
                    getPostLikes(id),
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    private static List<PostLike> getPostLikes(String postId) throws SQLException, LocalConnectException {
        var likes = new ArrayList<PostLike>();
        try (var stmt = sql.getPreparedStatement("SELECT * FROM `post_likes` WHERE `post` = ?")) {
            stmt.setString(1, postId);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    likes.add(
                        new PostLike(
                            rs.getString("id"),
                            getUser(rs.getString("user")),
                            rs.getTimestamp("created_at")
                        )
                    );
                }
            }
        }

        return likes;
    }

    public static List<Post> getUserPosts(User user) throws SQLException, LocalConnectException {
        var posts = new ArrayList<Post>();
        try (var stmt = sql.getPreparedStatement(
            "SELECT * FROM `posts` WHERE `author` = ?"
                + " ORDER BY `created_at` DESC LIMIT ?"
        )) {
            stmt.setString(1, user.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getString("id");

                    posts.add(
                        new Post(
                            id,
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            getPostLikes(id),
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
            "SELECT * FROM `posts` WHERE `author` in (SELECT `id` FROM `users` WHERE `group` = ?)"
                + " ORDER BY `created_at` DESC LIMIT ?"
        )) {
            stmt.setString(1, group.getId());
            stmt.setInt(2, MAX_OBJECTS);

            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getString("id");

                    posts.add(
                        new Post(
                            id,
                            getUser(rs.getString("author")),
                            getDocument(rs.getString("document")),
                            getPostLikes(id),
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

    private static Image getImage(String id) throws SQLException, LocalConnectException {
        if (id == null) {
            return null;
        }

        try (var stmt = sql.getPreparedStatement("SELECT * FROM `images` WHERE `id` = ?")) {
            stmt.setString(1, id);

            try (var rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new LocalConnectException(
                        HttpStatuses.NOT_FOUND,
                        APIErrorType.IMAGE_NOT_FOUND
                    );
                }

                id = rs.getString("id");
                return new Image(
                    id,
                    rs.getTimestamp("created_at")
                );
            }
        }
    }

    public static byte[] getImageData(String id) throws IOException, LocalConnectException {
        var file = new File(LocalConnect.IMAGE_FILE_PREFIX + id + LocalConnect.IMAGE_FILE_SUFFIX);
        var length = (int) file.length();
        var data = new byte[length];

        try (var stream = new FileInputStream(file)) {
            if (stream.read(data) == length) {
                return data;
            } else {
                throw new IOException();
            }
        } catch (FileNotFoundException e) {
            throw new LocalConnectException(
                HttpStatuses.NOT_FOUND,
                APIErrorType.IMAGE_NOT_FOUND
            );
        }
    }

    public static Group createGroup(Region region, String name) throws SQLException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `groups` (`id`, `region`, `name`, `created_at`) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, region.getId());
            stmt.setString(3, name);
            stmt.setTimestamp(4, createdAt);

            stmt.executeUpdate();
        }

        return new Group(id, region, name, createdAt);
    }

    public static CreatedUser createUser(Group group, String name) throws SQLException {
        var id = UUIDHelper.generate();
        var token = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `users` (`id`, `group`, `name`, `token`, `created_at`) VALUES (?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, group.getId());
            stmt.setString(3, name);
            stmt.setString(4, token);
            stmt.setTimestamp(5, createdAt);

            stmt.executeUpdate();
        }

        return new CreatedUser(id, group, name, token, createdAt);
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

    public static Document createDocument(User user, String title, String content) throws SQLException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `documents` (`id`, `author`, `title`, `content`, `created_at`) VALUES (?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setString(3, title);
            stmt.setString(4, content);
            stmt.setTimestamp(5, createdAt);

            stmt.executeUpdate();
        }

        return new Document(id, user, title, content, createdAt);
    }

    public static Board createBoard(Group group, Document document) throws SQLException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `boards` (`id`, `group`, `document`, `created_at`) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, group.getId());
            stmt.setString(3, document.getId());
            stmt.setTimestamp(4, createdAt);

            stmt.executeUpdate();
        }

        return new Board(id, group, document, createdAt);
    }

    public static Event createEvent(User user, Document document, Timestamp date) throws SQLException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `events` (`id`, `author`, `document`, `date`, `created_at`) VALUES (?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setString(3, document.getId());
            stmt.setTimestamp(4, date);
            stmt.setTimestamp(5, createdAt);

            stmt.executeUpdate();
        }

        return new Event(id, user, document, date, new ArrayList<>(), createdAt);
    }

    public static Post createPost(User user, Document document) throws SQLException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `posts` (`id`, `author`, `document`, `created_at`) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setString(3, document.getId());
            stmt.setTimestamp(4, createdAt);

            stmt.executeUpdate();
        }

        return new Post(id, user, document, new ArrayList<>(), createdAt);
    }

    public static Profile createProfile(User user, String hobbies, String favorites, String mottoes) throws SQLException {
        String id;
        var createdAt = new Timestamp(System.currentTimeMillis());

        try {
            id = getProfile(user).getId();

            try (var stmt = sql.getPreparedStatement(
                "UPDATE `profiles` SET `hobbies` = ?, `favorites` = ?, `mottoes` = ?, `updated_at` = ?"
                    + " WHERE `id` = ?"
            )) {
                stmt.setString(1, hobbies);
                stmt.setString(2, favorites);
                stmt.setString(3, mottoes);
                stmt.setTimestamp(4, createdAt);
                stmt.setString(5, id);

                stmt.executeUpdate();
            }
        } catch (LocalConnectException e) {
            id = UUIDHelper.generate();

            try (var stmt = sql.getPreparedStatement(
                "INSERT INTO `profiles` (`id`, `user`, `hobbies`, `favorites`, `mottoes`, `updated_at`)"
                    + " VALUES (?, ?, ?, ?, ?, ?)"
            )) {
                stmt.setString(1, id);
                stmt.setString(2, user.getId());
                stmt.setString(3, hobbies);
                stmt.setString(4, favorites);
                stmt.setString(5, mottoes);
                stmt.setTimestamp(6, createdAt);

                stmt.executeUpdate();
            }
        }

        return new Profile(id, user, hobbies, favorites, mottoes, createdAt);
    }

    public static Image createImage(User user, byte[] bytes) throws SQLException, IOException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());
        createImageData(id, bytes);

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `images` (`id`, `owner`, `created_at`) VALUES (?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setTimestamp(3, createdAt);

            stmt.executeUpdate();
        }

        return new Image(id, createdAt);
    }

    private static void createImageData(String id, byte[] bytes) throws IOException {
        var file = new File(LocalConnect.IMAGE_FILE_PREFIX + id + LocalConnect.IMAGE_FILE_SUFFIX);
        if (!file.createNewFile()) {
            throw new IOException();
        }

        try (var stream = new FileOutputStream(file)) {
            stream.write(bytes);
            stream.flush();
        }
    }

    public static Event joinEvent(User user, Event event) throws SQLException, LocalConnectException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `event_attendances` (`id`, `user`, `event`, `created_at`) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setString(3, event.getId());
            stmt.setTimestamp(4, createdAt);

            stmt.executeUpdate();
        }

        return getEvent(event.getId());
    }

    public static Post likePost(User user, Post post) throws SQLException, LocalConnectException {
        var id = UUIDHelper.generate();
        var createdAt = new Timestamp(System.currentTimeMillis());

        try (var stmt = sql.getPreparedStatement(
            "INSERT INTO `post_likes` (`id`, `user`, `post`, `created_at`) VALUES (?, ?, ?, ?)"
        )) {
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setString(3, post.getId());
            stmt.setTimestamp(4, createdAt);

            stmt.executeUpdate();
        }

        return getPost(post.getId());
    }
}
