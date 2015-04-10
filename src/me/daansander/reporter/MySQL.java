package me.daansander.reporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.ChatColor;

public class MySQL {

    private Connection connection;

    private String host;
    private String port;
    private String db;
    private String user;
    private String pass;

    public MySQL(String host, String port, String db, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || (!connection.isValid(500))) {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
                System.out.println("Opened MySQL connection!");
            }
            return connection;
        } catch (SQLException e) {
            System.out.println(ChatColor.RED + "Failed to establish connection to database.");
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null || !connection.isClosed()) {
                connection.close();
                System.out.println("Closed MySQL connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
