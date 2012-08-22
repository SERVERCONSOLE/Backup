package com.bukkitbackup.full.threading;

import com.bukkitbackup.full.config.Settings;
import com.bukkitbackup.full.config.Strings;
import com.bukkitbackup.full.utils.LogUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Domenic Horner
 */
public class BackupScheduler implements Runnable {

    private final Plugin plugin;
    private final PrepareBackup prepareBackup;
    private final Settings settings;
    private final Strings strings;
    private final Server pluginServer;
    private final String[] timesArray;

    public BackupScheduler(Plugin plugin, PrepareBackup prepareBackup, Settings settings, Strings strings, String[] timesArray) {
        this.plugin = plugin;
        this.prepareBackup = prepareBackup;
        this.pluginServer = plugin.getServer();
        this.settings = settings;
        this.strings = strings;
        this.timesArray = timesArray;
    }

    public void run() {
        
        // Checking Loop.
        for (int i = 0; i > 0; i++) {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        
            // this is like HH:mm.
            String dateNow = formatter.format(currentDate.getTime());
            
            for (int j = 0; j < timesArray.length; j++) {
                if(timesArray[j].equals(dateNow)) {
                    pluginServer.getScheduler().scheduleAsyncDelayedTask(plugin, prepareBackup);
                }
            }
            try {
                //Pause for 30 seconds
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                LogUtils.exceptionLog(ex);
            }
        }
        
    }
}
