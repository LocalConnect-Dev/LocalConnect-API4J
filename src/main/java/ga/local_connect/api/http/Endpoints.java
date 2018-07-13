package ga.local_connect.api.http;

import ga.local_connect.api.API;
import ga.local_connect.api.annotation.Endpoint;
import ga.local_connect.api.enumeration.EndpointCategory;
import ga.local_connect.api.enumeration.HttpMethodType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.object.Session;
import org.eclipse.jetty.server.Request;

import java.sql.SQLException;

class Endpoints {
    @Endpoint(method = HttpMethodType.GET, category = EndpointCategory.SESSIONS, name = "now")
    public static Session showSession(Request req) throws SQLException, LocalConnectException {
        return API.getSession(req.getHeader("X-LocalConnect-Session"));
    }
}
