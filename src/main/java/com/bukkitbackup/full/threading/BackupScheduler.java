package com.bukkitbackup.full.threading;

import com.bukkitbackup.full.config.Settings;
import com.bukkitbackup.full.config.Strings;
import com.bukkitbackup.full.utils.LogUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            Calendar currentDate = Calendar.getInstance();
    SimpleDateFormat formatter= new SimpleDateFormat("HH:mm");
    String dateNow = formatter.format(currentDate.getTime());
    
        
//                // Configure main backup task schedule.
//        int backupInterval = settings.getBackupInterval();
//        if (backupInterval != -1 && backupInterval != 0) {
//
//            // Convert to server ticks.
//            int backupIntervalInTicks = (backupInterval * 1200);
//
//            // Should the schedule repeat?
//            if (settings.getBooleanProperty("norepeat", false)) {
//                pluginServer.getScheduler().scheduleAsyncDelayedTask(this, prepareBackup, backupIntervalInTicks);
//                LogUtils.sendLog(strings.getString("norepeatenabled", Integer.toString(backupInterval)));
//            } else {
//                pluginServer.getScheduler().scheduleAsyncRepeatingTask(this, prepareBackup, backupIntervalInTicks, backupIntervalInTicks);
//            }
//        } else {
//            LogUtils.sendLog(strings.getString("disbaledauto"));
//        }
//        
    }
    
}
