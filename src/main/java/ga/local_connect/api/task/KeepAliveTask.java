package ga.local_connect.api.task;

import ga.local_connect.api.socket.SessionManager;

public class KeepAliveTask implements Runnable {
    @Override
    public void run() {
        SessionManager.getInstance().broadcast("{\"type\": \"KeepAlive\"}");
    }
}
