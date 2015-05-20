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

    public String getReporter() {
        return reporter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public static ArrayList<Report> getReports() {
        return reports;
    }

    public static void setReports(ArrayList<Report> reports) {
        Report.reports = reports;
    }

    public static ArrayList<String> getReported() {
        return reported;
    }

    public static void setReported(ArrayList<String> reported) {
        Report.reported = reported;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails(String name) {
        return details;
    }
}
