package de.tom.equationSolver.logger;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static Long currentTime;

    public static void log(LogLevel level, String message, String equation) {
        final File file = new File("." + File.separator + "logs" + File.separator + currentTime, equation.replace("*", " mal ").replace("/", " div ") + ".txt");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write("\n" + level.getPrefix() + message);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTimestamp() {
        currentTime = System.currentTimeMillis();
    }

}
