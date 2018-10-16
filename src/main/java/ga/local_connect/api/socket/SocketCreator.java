package ga.local_connect.api.socket;

import ga.local_connect.api.API;
import ga.local_connect.api.enumeration.APIErrorType;
import ga.local_connect.api.exception.LocalConnectException;
import ga.local_connect.api.http.HttpStatuses;
import ga.local_connect.api.object.Session;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class SocketCreator implements WebSocketCreator {
    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse res) {
        try {
            var secret = req.getHeader("Sec-WebSocket-Protocol");
            Session session = API.getSession(secret);
            res.setAcceptedSubProtocol(secret);

            return new SocketListener(session);
        } catch (LocalConnectException e) {
            res.setStatusCode(e.getStatus());
            res.setStatusReason(e.getCode().name());
            return null;
        } catch (Throwable t) {
            res.setStatusCode(HttpStatuses.INTERNAL_SERVER_ERROR);
            res.setStatusReason(APIErrorType.UNKNOWN_ERROR.name());
            t.printStackTrace();

            return null;
        }
    }
}
