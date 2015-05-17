package me.daansander.reporter;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Reporter extends JavaPlugin implements PluginMessageListener {

    public Setting settings = Setting.getInstance();
    private HashMap<String, Integer> cooldown = new HashMap();
    public static Reporter plugin = null;
    public Permission report = new Permission("reporter.report");
    public Permission creport = new Permission("reporter.creport");
    public Permission adminmessage = new Permission("reporter.am");
    public Permission reload = new Permission("reporter.reload");
    public FileConfiguration config;
    public ReportSQL sql;

    public void onEnable() {
        config = getConfig();
        plugin = this;
        this.settings.setup(this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        if(settings.getConfig().getBoolean("mysql")) {
            sql = new ReportSQL();
            sql.connect();
        }
        saveDefaultConfig();
        getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                for (String s : Reporter.this.cooldown.keySet()) {
                    Reporter.this.cooldown.put(s, Integer.valueOf(((Integer) Reporter.this.cooldown.get(s)).intValue() - 1));
                    if (((Integer) Reporter.this.cooldown.get(s)).intValue() <= 0) {
                        Reporter.this.cooldown.remove(s);
                    }
                }
            }
        }, 0L, 20L);
    }
    public ReportSQL getReportSQL() {
        return sql;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    public static Reporter getPlugin() {
        return plugin;
    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("SomeSubChannel")) {
            // Use the code sample in the 'Response' sections below to read
            // the data.
        }
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((cmd.getName().equalsIgnoreCase("report")) && ((sender instanceof Player))) {
            Player p = (Player) sender;
            if (p.hasPermission(this.report)) {
                if (this.cooldown.containsKey(p.getName())) {
                    String ParamCooldown = settings.getConfig().get("cooldown-message").toString().replace("%time%", this.cooldown.get(p.getName()).toString()).replaceAll("&", "§");
                    p.sendMessage(ParamCooldown);
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Incorrect arguments usage: /report <Player> <Reason>");
                    return true;
                }
                Player t = Bukkit.getPlayerExact(args[0]);
                if (t == null) {
                    p.sendMessage(ChatColor.RED + "Could not find the player: " + args[0]);
                }
                List<String> detailsList = new ArrayList(Arrays.asList(args));
                detailsList.remove(0);
                String details = "";
                for (String string : detailsList) {
                    details = details + " " + string;
                    this.cooldown.put(p.getName(), Integer.valueOf(this.settings.getConfig().getInt("cooldown")));
                }
                details = details.trim();
                String admin = settings.getConfig().get("message-adminnote").toString().replace("%target%", t.getName()).replace("%reporter%", p.getName()).replace("%reason%", details).replaceAll("&", "§");
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.hasPermission("reporter.am")) {
                        p. sendMessage(admin);
                    }
                }
                String player = settings.getConfig().get("message-report").toString().replace("%target%", t.getName()).replace("%reason%", details).replaceAll("&", "§");
                p.sendMessage(player);
                //p.sendMessage(ChatColor.RED + "Reported the player: " + ChatColor.GOLD + t.getName() + ChatColor.RED + " with the reason: " + ChatColor.GOLD + details);
                if(!settings.getConfig().getBoolean("mysql-use-uuid")) {
                    new Report(t.getName(), p.getName(), details);
                } else {
                    new Report(t.getUniqueId().toString(), p.getUniqueId().toString(), details);
                }
            } else {
                p.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return true;
            }
        }
        Player p;
        Player t;
        if ((cmd.getName().equalsIgnoreCase("isreported")) && ((sender instanceof Player))) {
            p = (Player) sender;
            if (!p.hasPermission("reporter.creport")) {
                p.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return true;
            }
            if ((args.length < 1) || (args.length > 1)) {
                p.sendMessage(ChatColor.RED + "Incorrect arguments usage: /isreported <Player>");
                return true;
            }
            t = Bukkit.getPlayerExact(args[0]);
            if (t == null) {
                p.sendMessage(ChatColor.RED + "Could not find the player: " + args[0]);
                return true;
            }
            if (!config.contains(t.getName())) {
                p.sendMessage(ChatColor.RED + "The player: " + args[0] + " is not reported");
                return true;
            }
            for (String index : config.getConfigurationSection(t.getName()).getKeys(false)) {
                String ParamInfo = settings.getConfig().get("message-checkreport").toString().replace("%reporter%", config.get(t.getName() + "." + index + ".reporter").toString()).replace("%reason%",
                        config.get(t.getName() + "." + index + ".reason").toString()).replaceAll("&", "§");
                String info = config.get(t.getName() + "." + index + ".reason").toString();
                String reporter = config.get(t.getName() + "." + index + ".reporter").toString();
                p.sendMessage(ParamInfo);
                //p.sendMessage("Reported by " + reporter + " with reason: " + info);
            }
        } if(cmd.getName().equalsIgnoreCase("rreload")) {
            Player ParamPlayer = (Player) sender;
            if(ParamPlayer.hasPermission("reporter.reload")) {
                try {
                    settings.reloadConfig();
                } catch (Exception e) {
                    e.printStackTrace();
                    ParamPlayer.sendMessage("§cAn error has ocurred while reloading the config see console for full log");
                    return true;
                }
                ParamPlayer.sendMessage("§cReloaded Reporter version: 4.0");
                return true;

            }
        }
        return true;
    }
}
