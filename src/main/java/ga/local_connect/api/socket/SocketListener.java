package ga.local_connect.api.socket;

import ga.local_connect.api.util.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class SocketListener {
    private Session session;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        SessionManager.getInstance().add(this);
        Logger.info(getRemoteAddress() + " joined.");
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        SessionManager.getInstance().remove(this);
        Logger.info(getRemoteAddress() + " left.");
    }

    void send(String message) {
        session.getRemote().sendStringByFuture(message);
    }

    private String getRemoteAddress() {
        var address = session.getRemoteAddress();
        return address.getHostString() + ":" + address.getPort();
    }
}
