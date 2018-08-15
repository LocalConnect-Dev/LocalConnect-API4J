package ga.local_connect.api.http;

import ga.local_connect.api.API;
import ga.local_connect.api.annotation.Endpoint;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.enumeration.EndpointCategory;
import ga.local_connect.api.enumeration.HttpMethodType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.object.*;
import org.eclipse.jetty.server.Request;

import java.sql.SQLException;
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

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.EVENTS, name = "list")
    public static List<Event> getEvents(Request req) throws SQLException, LocalConnectException {
        return API.getGroupEvents(getCurrentUser(req).getGroup());
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.POSTS, name = "list_user")
    public static List<Post> getUserPosts(Request req) throws SQLException, LocalConnectException {
        return API.getUserPosts(getCurrentUser(req));
    }

    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.POSTS, name = "list_group")
    public static List<Post> getGroupPosts(Request req) throws SQLException, LocalConnectException {
        return API.getGroupPosts(getCurrentUser(req).getGroup());
    }
}
