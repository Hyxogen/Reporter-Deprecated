package me.daansander.reporter;

/**
 * Created by Daan on 11-5-2015.
 */
public class Data {

    public String getHost() {
        return Reporter.getPlugin().settings.getConfig().getString("mysql-host");
    }
    public String getPort() {
        return Reporter.getPlugin().settings.getConfig().getString("mysql-port");
    }
    public String getDB() {
        return Reporter.getPlugin().settings.getConfig().getString("mysql-db");
    }
    public String getUsername() {
        return Reporter.getPlugin().settings.getConfig().getString("mysql-username");
    }
    public String getPassword() {
        return Reporter.getPlugin().settings.getConfig().getString("mysql-password");
    }

}
