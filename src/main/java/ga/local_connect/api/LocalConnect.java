package ga.local_connect.api;

import ga.local_connect.api.http.HttpHandler;
import ga.local_connect.api.socket.SocketServlet;
import ga.local_connect.api.util.JettyLogger;
import ga.local_connect.api.util.Logger;
import ga.local_connect.api.util.SQLManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Properties;

public class LocalConnect {
    private static final String CONFIG_FILE = "local_connect.properties";
    static final String IMAGE_FILE_PREFIX = "Images/";
    static final String IMAGE_JPEG_SUFFIX = ".jpg";
    static final String IMAGE_PNG_SUFFIX = ".png";

    private static Properties config;
    private static SQLManager sql;

    public static void main(String[] args) {
        Log.setLog(new JettyLogger());

        config = new Properties();
        var file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (var is = new FileInputStream(CONFIG_FILE);
                 var br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                config.load(br);
                Logger.success("Loaded config.");
            } catch (IOException e) {
                Logger.error("Failed to load config.");
                e.printStackTrace();
                return;
            }
        } else {
            Logger.success("Loaded default config.");
        }

        try {
            var type = config.getProperty("SQL.Type", "mysql");
            var host = config.getProperty("SQL.Host", "localhost");
            var user = config.getProperty("SQL.User", "local_connect");
            var password = config.getProperty("SQL.Password", "");
            var database = config.getProperty("SQL.Database", "local_connect");
            var port = Integer.parseInt(config.getProperty("SQL.Port", "3306"));
            var timeout = Integer.parseInt(config.getProperty("SQL.Timeout", "1"));

            sql = new SQLManager(
                type, host, port, database, user, password, timeout
            );

            Logger.success("Connected to SQL server.");
        } catch (SQLException e) {
            Logger.error("Failed to connect to database.");
            e.printStackTrace();
            return;
        }

        try {
            var server = new Server(Integer.valueOf(config.getProperty("Http.Port", "8080")));
            var handler = new HttpHandler();

            server.setHandler(handler);
            server.start();

            Logger.success("Started HTTP server.");
        } catch (Exception e) {
            Logger.error("Failed to start HTTP server.");
            e.printStackTrace();
            return;
        }

        try {
            var server = new Server(Integer.valueOf(config.getProperty("WebSocket.Port", "8888")));
            var handlers = new HandlerCollection();
            var servlet = new SocketServlet();
            var handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

            handler.addServlet(new ServletHolder(servlet), "/");
            handlers.addHandler(handler);
            server.setHandler(handlers);
            server.start();

            Logger.success("Started WebSocket server.");
        } catch (Exception e) {
            Logger.error("Failed to start WebSocket server.");
            e.printStackTrace();
            return;
        }

        Logger.success("Ready.");
    }

    public static Properties getConfig() {
        return config;
    }

    static SQLManager getSQL() {
        return sql;
    }
}
