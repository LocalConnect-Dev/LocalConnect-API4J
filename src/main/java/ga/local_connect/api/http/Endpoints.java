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
import java.util.ArrayList;
import java.util.Arrays;
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

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.PERMISSIONS, name = "show")
    public static Permission getPermission(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getPermission(id);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.PERMISSIONS, name = "list")
    public static List<Permission> getPermissions(Request req) throws SQLException {
        return API.getPermissions();
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.TYPES, name = "show")
    public static UserType getUserType(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getUserType(id);
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.TYPES, name = "list")
    public static List<UserType> getUserTypes(Request req) throws SQLException, LocalConnectException {
        return API.getUserTypes();
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

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.BOARDS, name = "reads")
    public static List<BoardRead> getBoardReads(Request req) throws SQLException, LocalConnectException {
        var id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.getBoardReads(API.getBoard(id));
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

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.SERVICE, name = "show")
    public static Service getService(Request req) throws SQLException, LocalConnectException {
        return API.getService();
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.PERMISSIONS, name = "create")
    public static Permission createPermission(Request req) throws SQLException, LocalConnectException {
        var name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.createPermission(name);
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.TYPES, name = "create")
    public static UserType createUserType(Request req) throws SQLException, LocalConnectException {
        var name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        var permissions = new ArrayList<Permission>();
        var permissionIds = req.getParameter("permissions");
        if (permissionIds != null) {
            for (var permissionId : permissionIds.split(",")) {
                permissions.add(
                    API.getPermission(permissionId)
                );
            }
        }

        return API.createUserType(
            name,
            permissions
        );
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
        var typeId = req.getParameter("type");
        if (name == null || typeId == null || name.isEmpty() || typeId.isEmpty()) {
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
            API.getUserType(typeId),
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

        var attachments = new ArrayList<Attachment>();
        var attachmentIds = req.getParameter("attachments");
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            for (var attachmentId : attachmentIds.split(",")) {
                attachments.add(
                    API.getAttachment(attachmentId)
                );
            }
        }

        return API.createDocument(
            getCurrentUser(req),
            title,
            content,
            attachments
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

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.ATTACHMENTS, name = "create")
    public static Attachment createAttachment(Request req) throws SQLException, LocalConnectException {
        var type = req.getParameter("type");
        var objectId = req.getParameter("object_id");
        if (type == null || objectId == null || type.isEmpty() || objectId.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        Attachable object;
        try {
            var classType = Class.forName(Attachable.class.getPackageName().concat(".").concat(type));
            if (!Arrays.asList(classType.getInterfaces()).contains(Attachable.class)) {
                throw new ClassNotFoundException();
            }

            if (classType.equals(Image.class)) {
                object = API.getImage(objectId);
            } else if (classType.equals(Event.class)) {
                object = API.getEvent(objectId);
            } else {
                throw new ClassNotFoundException();
            }
        } catch (ClassNotFoundException e) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_OBJECT_TYPE
            );
        }

        return API.createAttachment(object);
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.SERVICE, name = "edit")
    public static Service editService(Request req) throws SQLException, LocalConnectException {
        var description = req.getParameter("description");
        if (description == null || description.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.editService(description);
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.USERS, name = "set_avatar")
    public static User setAvatar(Request req) throws SQLException, LocalConnectException {
        var userId = req.getParameter("user");
        if (userId == null || userId.isEmpty()) {
            userId = getCurrentUser(req).getId();
        }

        var avatarId = req.getParameter("avatar");
        if (avatarId == null || userId.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        API.setAvatar(
            API.getUser(userId),
            API.getImage(avatarId)
        );

        return API.getUser(userId);
    }

    @Endpoint(method = HttpMethodType.POST, category = EndpointCategory.BOARDS, name = "read")
    public static BoardRead readBoard(Request req) throws SQLException, LocalConnectException {
        var boardId = req.getParameter("board");
        if (boardId == null || boardId.isEmpty()) {
            throw new LocalConnectException(
                HttpStatuses.BAD_REQUEST,
                APIErrorType.INVALID_PARAMETER
            );
        }

        return API.readBoard(
            getCurrentUser(req),
            API.getBoard(boardId)
        );
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

    @Endpoint(method = HttpMethodType.DELETE, category = EndpointCategory.SESSIONS, name = "destroy")
    public static void destroySession(Request req) throws SQLException, LocalConnectException {
        API.destroySession(getCurrentSession(req));
    }
}
