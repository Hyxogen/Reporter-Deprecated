package me.daansander.reporter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class Setting
{
    static Setting instance = new Setting();
    Plugin p;
    FileConfiguration config;
    File cfile;

    public static Setting getInstance()
    {
        return instance;
    }

    public void setup(Plugin p)
    {
        this.cfile = new File(p.getDataFolder(), "cooldowns.yml");
        if (!this.cfile.exists()) {
            try
            {
                this.cfile.createNewFile();
                p.saveResource("cooldowns.yml", true);
            }
            catch (IOException e)
            {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create cooldowns.yml!");
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.cfile);
    }

    public FileConfiguration getConfig()
    {
        return this.config;
    }

    public void saveConfig()
    {
        try
        {
            this.config.save(this.cfile);
        }
        catch (IOException e)
        {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save cooldowns.yml!");
        }
    }

    public void reloadConfig()
    {
        this.config = YamlConfiguration.loadConfiguration(this.cfile);
    }

    public PluginDescriptionFile getDesc()
    {
        return this.p.getDescription();
    }

    public void saveDefaultConfig()
    {
        if (this.cfile == null) {
            this.cfile = new File(Reporter.plugin.getDataFolder(), "cooldowns.yml");
        }
        if (!this.cfile.exists()) {
            Reporter.plugin.saveResource("cooldowns.yml", false);
        }
    }
}
