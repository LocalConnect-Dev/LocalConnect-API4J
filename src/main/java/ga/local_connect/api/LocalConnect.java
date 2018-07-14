package ga.local_connect.api;

import ga.local_connect.api.http.HttpHandler;
import ga.local_connect.api.util.JettyLogger;
import ga.local_connect.api.util.Logger;
import ga.local_connect.api.util.SQLManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class LocalConnect {
    private static final String CONFIG_FILE = "local_connect.properties";

    private static SQLManager sql;

    public static void main(String[] args) {
        Log.setLog(new JettyLogger());

        var conf = new Properties();
        var file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (var is = new FileInputStream(CONFIG_FILE);
                 var br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                conf.load(br);
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
            var type = conf.getProperty("SQL.Type", "mysql");
            var host = conf.getProperty("SQL.Host", "localhost");
            var user = conf.getProperty("SQL.User", "local_connect");
            var password = conf.getProperty("SQL.Password", "");
            var database = conf.getProperty("SQL.Database", "local_connect");
            var port = Integer.valueOf(conf.getProperty("SQL.Port", "3306"));
            var timeout = Integer.valueOf(conf.getProperty("SQL.Timeout", "1"));

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
            var server = new Server(Integer.valueOf(conf.getProperty("Http.Port", "8080")));
            var handler = new HttpHandler();

            server.setHandler(handler);
            server.start();

            Logger.success("Started HTTP server.");
        } catch (Exception e) {
            Logger.error("Failed to start HTTP server.");
            e.printStackTrace();
            return;
        }

        Logger.success("Ready.");
    }

    static SQLManager getSQL() {
        return sql;
    }
}
