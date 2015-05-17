package me.daansander.reporter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Daan on 12-5-2015.
 */
public class ReportSQL {

    private MySQL mySql;
    private Data data = new Data();

    public void connect() {
        mySql = new MySQL(data.getHost(), data.getPort(), data.getDB(), data.getUsername(), data.getPassword());
        mySql.getConnection();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Reporter connected succsesfully!");

        try {
            PreparedStatement sql = mySql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `reports` (reporter text, reportedplayer text, reason text);");
            sql.executeUpdate();
            sql.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createReport(String reporter, String reportedplayer, String reason) {
        try{
            PreparedStatement sql = mySql.getConnection().prepareStatement("INSERT INTO `reports` (`reporter`, `reportedplayer`, `reason`) VALUES ('" + reporter + "', '" + reportedplayer + "', '" + reason + "');");
            sql.executeUpdate();
            sql.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public String getReport() {

    }
}
