package me.daansander.reporter;

import io.netty.handler.codec.http.HttpContentEncoder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public ArrayList<String> getReport(String name) {
        try {
            Statement sql =  mySql.getConnection().createStatement();
            ResultSet resultSet = sql.executeQuery("SELECT * FROM `reports` WHERE `reportedplayer`='" + name + "';");
            ArrayList<String> rowValues = new ArrayList<String>();
            while (resultSet.next()) {
                rowValues.add(resultSet.getString("reason"));
            }
            return rowValues;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean isReported(String name) {
        try {
            Statement sql = mySql.getConnection().createStatement();
            ResultSet resultSet = sql.executeQuery("SELECT * FROM `reports` WHERE `reportedplayer`='" + name + "';");
            if(resultSet.next()) {
                sql.close();
                resultSet.close();
                return true;
            }
            sql.close();
            resultSet.close();
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
