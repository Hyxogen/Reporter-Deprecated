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

        int next = 1;
        if (Reporter.config.getConfigurationSection(name) != null)
            for (String report : Reporter.config.getConfigurationSection(name).getKeys(false))
                next++;
        Reporter.config.set(name + "." + next + ".reason", "" + details);
        Reporter.config.set(name + "." + next + ".reporter", "" + reporter);
        Reporter.plugin.saveConfig();
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
