package nuzp.yuliy_gorichenko.openhab_helper.manager;

import nuzp.yuliy_gorichenko.openhab_helper.exceptions.*;

import java.util.StringJoiner;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.ConnectionManager.RequestType;

public class OpenHabManager {
    private final SettingsManager settingsManager;
    private final ConnectionManager connectionManager;

    /**
     * Constructor
     */
    public OpenHabManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        this.connectionManager = new ConnectionManager();
    }

    /**
     * Create REST API URL for OpenHAB
     */
    private String createOpenHabURL(String itemName) {
        StringJoiner url = new StringJoiner("/");
        url.add("http://" + settingsManager.getOpenHabIp() + ":" + settingsManager.getOpenHabPort());
        url.add("rest");
        url.add("items");
        url.add(itemName);
        url.add("state");

        return url.toString();
    }

    /**
     * Send data to OpenHAB
     */
    public void sendData(String itemName, String message) throws OpenHabException, OpenHabConnectionException {
        String URL = createOpenHabURL(itemName);
        connectionManager.sendRequest(URL, RequestType.PUT, message);
    }

    /**
     * Get data from OpenHAB
     */
    public String getData(String itemName) throws OpenHabItemException, OpenHabConnectionException {
        try {
            String URL = createOpenHabURL(itemName);
            return connectionManager.sendRequest(URL, RequestType.GET, null);
        } catch (OpenHabException e) {
            throw new OpenHabItemException(itemName);
        }
    }
}
