package me.daansander.reporter;

import java.util.ArrayList;

/**
 * Created by Daan on 2-4-2015.
 */
public class Report {

    private String name;
    private String details;
    private String reporter;
    public static ArrayList<Report> reports = new ArrayList<Report>();
    public static ArrayList<String> reported = new ArrayList<String>();

    public Report(String name, String reporter, String details) {
        this.name = name;
        this.details = details;
        this.reporter = reporter;

        reports.add(this);
        reported.add(name);

        if(!Reporter.getPlugin().settings.getConfig().getBoolean("mysql")) {
            int next = 1;
            if (Reporter.getPlugin().config.getConfigurationSection(name) != null)
                for (String report : Reporter.getPlugin().config.getConfigurationSection(name).getKeys(false))
                    next++;
            Reporter.getPlugin().config.set(name + "." + next + ".reason", "" + details);
            Reporter.getPlugin().config.set(name + "." + next + ".reporter", "" + reporter);
            Reporter.plugin.saveConfig();
        } else {
            Reporter.getPlugin().getReportSQL().createReport(reporter, name, details);
        }
    }

    public boolean isReported(String player) {
        if (reported.contains(player)) return true;
        return false;
    }

    public String getReporter(String reportedplayer) {
        return reporter;
    }

    public String getDetails(String name) {
        return details;
    }
}
