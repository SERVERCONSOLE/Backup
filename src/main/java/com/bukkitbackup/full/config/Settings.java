package com.bukkitbackup.full.config;

import com.bukkitbackup.full.utils.LogUtils;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Class for loading the configuration file.
 *
 * @author Domenic Horner
 */
public final class Settings {

    private Strings strings;
    private File configFile;
    private FileConfiguration settings;
    public boolean useMaxSizeBackup = false;

    public Settings(File configFile, Strings strings) {

        this.strings = strings;
        this.configFile = configFile;

        try {

            // Checks if configuration file exists, creates it if it does not.
            if (!configFile.exists()) {
                LogUtils.sendLog(strings.getString("newconfigfile"));
                createDefaultSettings();
            }

            // Initialize the configuration, and populate with settings.
            settings = new YamlConfiguration();
            settings.load(configFile);

        } catch (Exception e) {
            LogUtils.exceptionLog(e, "Failed to load configuration.");
        }
    }

    /**
     * Load the properties file from the JAR and place it in the backup DIR.
     */
    private void createDefaultSettings() {

        BufferedReader bReader = null;
        BufferedWriter bWriter = null;
        String line;

        try {

            // Open a stream to the configuration file in the jar, because we can only access over the class loader.
            bReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/config.yml")));
            bWriter = new BufferedWriter(new FileWriter(configFile));

            // Writeout the new configuration file.
            while ((line = bReader.readLine()) != null) {
                bWriter.write(line);
                bWriter.newLine();
            }

        } catch (Exception e) {
            LogUtils.exceptionLog(e, "Error opening stream.");
        } finally {
            try {

                // Confirm the streams are closed.
                if (bReader != null) {
                    bReader.close();
                }
                if (bWriter != null) {
                    bWriter.close();
                }
            } catch (Exception e) {
                LogUtils.exceptionLog(e, "Error closing configuration stream.");
            }
        }
    }

    public void checkSettingsVersion(String requiredVersion) {

        // Get the version information from the file.
        String configVersion = settings.getString("version", null);

        // Check we got a version from the config file.
        if (configVersion == null) {
            LogUtils.sendLog(strings.getString("failedtogetpropsver"));
        } else if (!configVersion.equals(requiredVersion)) {
            LogUtils.sendLog(strings.getString("configupdate"));
        }
    }

    /**
     * Used to upgrade the configuration file.
     */
    public void doConfigurationUpgrade() {
        LogUtils.sendLog(strings.getString("updatingconf"));
        if (configFile.exists()) {
            configFile.delete();
        }
        createDefaultSettings();
        LogUtils.sendLog(strings.getString("updatingconf"));
    }

    /**
     * Gets the value of a integer property.
     *
     * @param property The name of the property.
     * @return The value of the property, defaults to -1.
     */
    public int getIntProperty(String property, int defaultInt) {
        return settings.getInt(property, defaultInt);
    }

    /**
     * Gets the value of a boolean property.
     *
     * @param property The name of the property.
     * @return The value of the property, defaults to true.
     */
    public boolean getBooleanProperty(String property, boolean defaultBool) {
        return settings.getBoolean(property, defaultBool);
    }

    /**
     * Gets a value of the string property and make sure it is not null.
     *
     * @param property The name of the property.
     * @return The value of the property.
     */
    public String getStringProperty(String property, String defaultString) {
        return settings.getString(property, defaultString);
    }

    /**
     * Method that gets the amount of time between backups. - Checks string for
     * no automatic backup. - Checks for if only number (as minutes). - Checks
     * for properly formatted string. - If unknown amount of time, sets as
     * minutes.
     *
     * @return Amount of time between backups. (In minutes)
     */
    public int getIntervalInMinutes(String forSetting) {
        String settingInterval = getStringProperty(forSetting, "15M").trim().toLowerCase();
        // If it is null or set to disable.
        if (settingInterval.equals("-1") || settingInterval == null) {
            return 0;
        }
        // If it is just a number, return minutes.
        if (settingInterval.matches("^[0-9]+$")) {
            return Integer.parseInt(settingInterval);
        } else if (settingInterval.matches("[0-9]+[a-z]")) {
            Pattern timePattern = Pattern.compile("^([0-9]+)[a-z]$");
            Matcher amountTime = timePattern.matcher(settingInterval);
            Pattern letterPattern = Pattern.compile("^[0-9]+([a-z])$");
            Matcher letterTime = letterPattern.matcher(settingInterval);
            if (letterTime.matches() && amountTime.matches()) {
                String letter = letterTime.group(1);
                int time = Integer.parseInt(amountTime.group(1));
                if (letter.equals("m")) {
                    return time;
                } else if (letter.equals("h")) {
                    return time * 60;
                } else if (letter.equals("d")) {
                    return time * 1440;
                } else if (letter.equals("w")) {
                    return time * 10080;
                } else {
                    LogUtils.sendLog(strings.getString("unknowntimeident"));
                    return time;
                }
            } else {
                LogUtils.sendLog(strings.getString("checkbackupinterval"));
                return 0;
            }
        } else {
            LogUtils.sendLog(strings.getString("checkbackupinterval"));
            return 0;
        }
    }

    /**
     * Method that gets the amount of time between backups. - Checks string for
     * no automatic backup. - Checks for if only number (as minutes). - Checks
     * for properly formatted string. - If unknown amount of time, sets as
     * minutes.
     *
     * @return Amount of time between backups. (In minutes)
     */
    public int getBackupLimits() {
        String limitSetting = getStringProperty("maxbackups", "25").trim().toLowerCase();
        
        // If it is null or set to disable.
        if (limitSetting.equals("-1") || limitSetting == null) {
            return 0;
        }
        // If it is just a number, return minutes.
        if (limitSetting.matches("^[0-9]+$")) {
            return Integer.parseInt(limitSetting);
        } else if (limitSetting.matches("[0-9]+[a-z]")) {
            Pattern timePattern = Pattern.compile("^([0-9]+)[a-z]$");
            Matcher amountTime = timePattern.matcher(limitSetting);
            Pattern letterPattern = Pattern.compile("^[0-9]+([a-z])$");
            Matcher letterTime = letterPattern.matcher(limitSetting);
            if (letterTime.matches() && amountTime.matches()) {
                String letter = letterTime.group(1);
                int bytes = Integer.parseInt(amountTime.group(1));
                useMaxSizeBackup = true;
                if (letter.equals("k")) {
                    return bytes;
                } else if (letter.equals("k")) {
                    return bytes * 1024;
                } else if (letter.equals("m")) {
                    return bytes * 1048576;
                } else if (letter.equals("g")) {
                    return bytes * 1073741824;
                } else {
                    LogUtils.sendLog(strings.getString("unknownsizeident"));
                    return bytes;
                }
            } else {
                LogUtils.sendLog(strings.getString("checksizelimit"));
                return 0;
            }
        } else {
            LogUtils.sendLog(strings.getString("checksizelimit"));
            return 0;
        }
    }
}
