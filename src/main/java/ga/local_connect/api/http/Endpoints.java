package ga.local_connect.api.http;

import ga.local_connect.api.API;
import ga.local_connect.api.annotation.Endpoint;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.enumeration.EndpointCategory;
import ga.local_connect.api.enumeration.HttpMethodType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.object.*;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

class Endpoints {
    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.SESSIONS, name = "current")
    public static Session getCurrentSession(Request req) throws SQLException, LocalConnectException {
        return API.getSession(req.getHeader("X-LocalConnect-Session"));
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.USERS, name = "me")
    public static User getCurrentUser(Request req) throws SQLException, LocalConnectException {
        return getCurrentSession(req).getUser();
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.PROFILES, name = "mine")
    public static Profile getProfileOfCurrentUser(Request req) throws SQLException, LocalConnectException {
        return API.getProfile(getCurrentUser(req));
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.REGIONS, name = "show")
    public static Region getRegion(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getRegion(id);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.REGIONS, name = "list")
    public static List<Region> getRegions(Request req) throws SQLException {
        return API.getRegions();
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.GROUPS, name = "show")
    public static Group getGroup(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getGroup(id);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.GROUPS, name = "list")
    public static List<Group> getGroups(Request req) throws SQLException, LocalConnectException {
        Region region;
        var regionId = req.getParameter("region");
        if (regionId == null || regionId.isEmpty()) {
            region = getCurrentUser(req).getGroup().getRegion();
        } else {
            region = API.getRegion(regionId);
        }

        return API.getGroups(region);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.USERS, name = "show")
    public static User getUser(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getUser(id);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.USERS, name = "list")
    public static List<User> getUsers(Request req) throws SQLException, LocalConnectException {
        Group group;
        var groupId = req.getParameter("group");
        if (groupId == null || groupId.isEmpty()) {
            group = getCurrentUser(req).getGroup();
        } else {
            group = API.getGroup(groupId);
        }

        return API.getUsers(group);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.BOARDS, name = "show")
    public static Board getBoard(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getBoard(id);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.BOARDS, name = "list")
    public static List<Board> getBoards(Request req) throws SQLException, LocalConnectException {
        return API.getBoards(getCurrentUser(req).getGroup());
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.EVENTS, name = "show")
    public static Event getEvent(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getEvent(id);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.EVENTS, name = "list")
    public static List<Event> getEvents(Request req) throws SQLException, LocalConnectException {
        return API.getGroupEvents(getCurrentUser(req).getGroup());
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.EVENTS, name = "list_user")
    public static List<Event> getUserEvents(Request req) throws SQLException, LocalConnectException {
        return API.getUserEvents(getCurrentUser(req));
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.EVENTS, name = "list_joined")
    public static List<Event> getJoinedEvents(Request req) throws SQLException, LocalConnectException {
        return API.getJoinedEvents(getCurrentUser(req));
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.POSTS, name = "list_user")
    public static List<Post> getUserPosts(Request req) throws SQLException, LocalConnectException {
        return API.getUserPosts(getCurrentUser(req));
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.POSTS, name = "list_group")
    public static List<Post> getGroupPosts(Request req) throws SQLException, LocalConnectException {
        return API.getGroupPosts(getCurrentUser(req).getGroup());
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.PROFILES, name = "show")
    public static Profile getProfile(Request req) throws SQLException, LocalConnectException {
        var userId = req.getParameter("user");
        if (userId == null || userId.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getProfile(API.getUser(userId));
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.IMAGES, name = "show")
    public static byte[] getImage(Request req) throws IOException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getImageData(id);
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.REGIONS, name = "create")
    public static Region createRegion(Request req) throws SQLException, LocalConnectException {
        var name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createRegion(name);
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.GROUPS, name = "create")
    public static Group createGroup(Request req) throws SQLException, LocalConnectException {
        var name = req.getParameter("name");
        var regionId = req.getParameter("region");
        if (name == null || name.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        Region region;
        if (regionId == null || regionId.isEmpty()) {
            region = getCurrentUser(req).getGroup().getRegion();
        } else {
            region = API.getRegion(regionId);
        }

        return API.createGroup(
            region,
            name
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.USERS, name = "create")
    public static User createUser(Request req) throws SQLException, LocalConnectException {
        var name = req.getParameter("name");
        var groupId = req.getParameter("group");
        if (name == null || name.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        Group group;
        if (groupId == null || groupId.isEmpty()) {
            group = getCurrentUser(req).getGroup();
        } else {
            group = API.getGroup(groupId);
        }

        return API.createUser(
            group,
            name
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.SESSIONS, name = "create")
    public static CreatedSession createSession(Request req) throws SQLException, LocalConnectException {
        var token = req.getParameter("token");
        if (token == null || token.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createSession(API.getUserByToken(token));
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.DOCUMENTS, name = "create")
    public static Document createDocument(Request req) throws SQLException, LocalConnectException {
        var title = req.getParameter("title");
        var content = req.getParameter("content");
        if (title == null || content == null || title.isEmpty() || content.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createDocument(
            getCurrentUser(req),
            title,
            content
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.BOARDS, name = "create")
    public static Board createBoard(Request req) throws SQLException, LocalConnectException {
        var document = req.getParameter("document");
        if (document == null || document.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createBoard(
            getCurrentUser(req).getGroup(),
            API.getDocument(document)
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.EVENTS, name = "create")
    public static Event createEvent(Request req) throws SQLException, LocalConnectException {
        var document = req.getParameter("document");
        var dateStr = req.getParameter("date");
        if (document == null || dateStr == null || document.isEmpty() || dateStr.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createEvent(
            getCurrentUser(req),
            API.getDocument(document),
            new Timestamp(Long.parseUnsignedLong(dateStr) * 1000)
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.POSTS, name = "create")
    public static Post createPost(Request req) throws SQLException, LocalConnectException {
        var document = req.getParameter("document");
        if (document == null || document.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createPost(
            getCurrentUser(req),
            API.getDocument(document)
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.PROFILES, name = "create")
    public static Profile createProfile(Request req) throws SQLException, LocalConnectException {
        var hobbies = req.getParameter("hobbies");
        var favorites = req.getParameter("favorites");
        var mottoes = req.getParameter("mottoes");
        if (hobbies == null || favorites == null || mottoes == null ||
            hobbies.isEmpty() || favorites.isEmpty() || mottoes.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createProfile(
            getCurrentUser(req),
            hobbies,
            favorites,
            mottoes
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.IMAGES, name = "create")
    public static Image createImage(Request req) throws SQLException, IOException, LocalConnectException {
        try (var stream = req.getInputStream()) {
            var bytes = stream.readAllBytes();
            return API.createImage(
                getCurrentUser(req),
                bytes
            );
        }
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.EVENTS, name = "join")
    public static Event joinEvent(Request req) throws SQLException, LocalConnectException {
        var eventId = req.getParameter("event");
        if (eventId == null || eventId.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.joinEvent(
            getCurrentUser(req),
            API.getEvent(eventId)
        );
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.POSTS, name = "like")
    public static Post likePost(Request req) throws SQLException, LocalConnectException {
        var postId = req.getParameter("post");
        if (postId == null || postId.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.likePost(
            getCurrentUser(req),
            API.getPost(postId)
        );
    }
}
