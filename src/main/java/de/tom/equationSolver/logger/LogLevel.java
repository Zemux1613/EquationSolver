package de.tom.equationSolver.logger;

public enum LogLevel {

    WARNING("[WARNING] "),
    INFO("[INFO] "),
    ERROR("[ERROR] ");

    private String prefix;

    LogLevel(final String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
