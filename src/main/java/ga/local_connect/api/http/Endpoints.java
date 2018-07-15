package ga.local_connect.api.http;

import ga.local_connect.api.API;
import ga.local_connect.api.annotation.Endpoint;
import ga.local_connect.api.enumeration.EndpointCategory;
import ga.local_connect.api.enumeration.HttpMethodType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.object.Profile;
import ga.local_connect.api.object.Session;
import ga.local_connect.api.object.User;
import org.eclipse.jetty.server.Request;

import java.sql.SQLException;

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
}
