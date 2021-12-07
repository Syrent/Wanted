package ir.syrent.wanted.DataManager;

import ir.syrent.wanted.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Log {

    public Log() {
        setupLogFolder();
    }

    public File logsFolder;

    public void setupLogFolder() {
        if (!Main.getInstance().getDataFolder().exists()) { // Check if plugin folder exists
            Main.getInstance().getDataFolder().mkdir(); // if not then create it
        }

        logsFolder = new File(Main.getInstance().getDataFolder(), "logs"); // Set the path of the new logs folder

        if (!logsFolder.exists()) { // Check if logs folder exists
            logsFolder.mkdirs(); // if not then create it
            Main.getInstance().getLogger().info("Created the logs folder");
        }
    }

    public void logToFile(String file, String message) {
        try {
            File dataFolder = Main.getInstance().getDataFolder(); // Sets file to the plugins/<pluginname> folder
            if (!dataFolder.exists()) { // Check if logs folder exists
                dataFolder.mkdir(); // if not then create it
            }
            File saveTo = new File(Main.getInstance().getDataFolder() + File.separator + "logs" + File.separator, file + ".log"); // Sets the path of the new log file
            if (!saveTo.exists()) { // Check if logs folder exists
                saveTo.createNewFile(); // if not then create it
            }
            FileWriter fw = new FileWriter(saveTo, true); // Create a FileWriter to edit the file
            PrintWriter pw = new PrintWriter(fw); // Create a PrintWriter
            pw.println(message); // This is the text/message you will be writing to the file
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace(); // If theres any errors in this process it will print the error in console
        }
    }

    public String logTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public String formatMessage() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}