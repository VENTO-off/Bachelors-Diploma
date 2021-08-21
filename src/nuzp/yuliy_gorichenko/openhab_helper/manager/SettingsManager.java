package nuzp.yuliy_gorichenko.openhab_helper.manager;

import java.util.prefs.Preferences;

public class SettingsManager {
    private final Preferences data;
    private int language;
    private String openHabIp;
    private int openHabPort;
    private long updateInterval;
    private String itemAmperage;
    private String itemVoltage;
    private String itemTemperature;
    private double minAmperage;
    private int maxAmperage;
    private int warningPercentage;
    private String itemMessage;
    private String mysqlIp;
    private int mysqlPort;
    private String mysqlUser;
    private String mysqlPassword;
    private String mysqlDatabase;

    /**
     * Constructor
     */
    public SettingsManager() {
        this.data = Preferences.userRoot().node("openhab_helper/settings");
        this.load();
    }

    /**
     * Load settings
     */
    private void load() {
        language = data.getInt("language", -1);
        openHabIp = data.get("openHabIp", null);
        openHabPort = data.getInt("openHabPort", 0);
        updateInterval = data.getLong("updateInterval", 0);
        itemAmperage = data.get("itemAmperage", null);
        itemVoltage = data.get("itemVoltage", null);
        itemTemperature = data.get("itemTemperature", null);
        minAmperage = data.getDouble("minAmperage", 0);
        maxAmperage = data.getInt("maxAmperage", 0);
        warningPercentage = data.getInt("warningPercentage", 0);
        itemMessage = data.get("itemMessage", null);
        mysqlIp = data.get("mysqlIp", null);
        mysqlPort = data.getInt("mysqlPort", 0);
        mysqlUser = data.get("mysqlUser", null);
        mysqlPassword = data.get("mysqlPassword", null);
        mysqlDatabase = data.get("mysqlDatabase", null);
    }

    /**
     * Save settings
     */
    public void save() {
        if (language != -1)
            data.putInt("language", language);

        if (openHabIp != null)
            data.put("openHabIp", openHabIp);

        if (openHabPort != 0)
            data.putInt("openHabPort", openHabPort);

        if (updateInterval != 0)
            data.putLong("updateInterval", updateInterval);

        if (itemAmperage != null)
            data.put("itemAmperage", itemAmperage);

        if (itemVoltage != null)
            data.put("itemVoltage", itemVoltage);

        if (itemTemperature != null)
            data.put("itemTemperature", itemTemperature);

        if (minAmperage != 0)
            data.putDouble("minAmperage", minAmperage);

        if (maxAmperage != 0)
            data.putInt("maxAmperage", maxAmperage);

        if (warningPercentage != 0)
            data.putInt("warningPercentage", warningPercentage);

        if (itemMessage != null)
            data.put("itemMessage", itemMessage);

        if (mysqlIp != null)
            data.put("mysqlIp", mysqlIp);

        if (mysqlPort != 0)
            data.putInt("mysqlPort", mysqlPort);

        if (mysqlUser != null)
            data.put("mysqlUser", mysqlUser);

        if (mysqlPassword != null)
            data.put("mysqlPassword", mysqlPassword);

        if (mysqlDatabase != null)
            data.put("mysqlDatabase", mysqlDatabase);
    }

    /**
     * Check if settings done
     */
    public boolean isSettingsDone() {
        return openHabIp != null && openHabPort != 0 && updateInterval != 0 && itemAmperage != null &&
                itemVoltage != null && itemTemperature != null && minAmperage != 0 && warningPercentage != 0 &&
                itemMessage != null && mysqlIp != null && mysqlPort != 0 && mysqlUser != null &&
                mysqlPassword != null && mysqlDatabase != null;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getOpenHabIp() {
        return openHabIp;
    }

    public void setOpenHabIp(String openHabIp) {
        this.openHabIp = openHabIp;
    }

    public int getOpenHabPort() {
        return openHabPort;
    }

    public void setOpenHabPort(int openHabPort) {
        this.openHabPort = openHabPort;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getItemAmperage() {
        return itemAmperage;
    }

    public void setItemAmperage(String itemAmperage) {
        this.itemAmperage = itemAmperage;
    }

    public String getItemVoltage() {
        return itemVoltage;
    }

    public void setItemVoltage(String itemVoltage) {
        this.itemVoltage = itemVoltage;
    }

    public String getItemTemperature() {
        return itemTemperature;
    }

    public void setItemTemperature(String itemTemperature) {
        this.itemTemperature = itemTemperature;
    }

    public double getMinAmperage() {
        return minAmperage;
    }

    public void setMinAmperage(double minAmperage) {
        this.minAmperage = minAmperage;
    }

    public int getMaxAmperage() {
        return maxAmperage;
    }

    public void setMaxAmperage(int maxAmperage) {
        this.maxAmperage = maxAmperage;
    }

    public int getWarningPercentage() {
        return warningPercentage;
    }

    public void setWarningPercentage(int warningPercentage) {
        this.warningPercentage = warningPercentage;
    }

    public String getItemMessage() {
        return itemMessage;
    }

    public void setItemMessage(String itemMessage) {
        this.itemMessage = itemMessage;
    }

    public String getMysqlIp() {
        return mysqlIp;
    }

    public void setMysqlIp(String mysqlIp) {
        this.mysqlIp = mysqlIp;
    }

    public int getMysqlPort() {
        return mysqlPort;
    }

    public void setMysqlPort(int mysqlPort) {
        this.mysqlPort = mysqlPort;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public void setMysqlUser(String mysqlUser) {
        this.mysqlUser = mysqlUser;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }

    public String getMysqlDatabase() {
        return mysqlDatabase;
    }

    public void setMysqlDatabase(String mysqlDatabase) {
        this.mysqlDatabase = mysqlDatabase;
    }
}
