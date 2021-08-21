package nuzp.yuliy_gorichenko.openhab_helper.manager;

import javafx.application.Platform;
import nuzp.yuliy_gorichenko.openhab_helper.Settings;
import nuzp.yuliy_gorichenko.openhab_helper.utils.DialogUtils;

import java.util.StringJoiner;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.AnalyzeManager.StatusType;

public class NotifyManager {
    private final SettingsManager settingsManager;
    private final LocalizationManager localizationManager;
    private final OpenHabManager openHabManager;
    private final AnalyzeManager analyzeManager;

    /**
     * Constructor
     */
    public NotifyManager(SettingsManager settingsManager, LocalizationManager localizationManager, OpenHabManager openHabManager, AnalyzeManager analyzeManager) {
        this.settingsManager = settingsManager;
        this.localizationManager = localizationManager;
        this.openHabManager = openHabManager;
        this.analyzeManager = analyzeManager;
        this.initListener();
    }

    /**
     * Listen to AnalyzeManager and send message to OpenHAB
     */
    private void initListener() {
        analyzeManager.addStatusListener(statusType -> {
            try {
                openHabManager.sendData(settingsManager.getItemMessage(), createMessage(statusType));
            } catch (Exception e) {
                Platform.runLater(() -> DialogUtils.showErrorMessage(e, localizationManager));
            }
        });
    }

    /**
     * Create message
     */
    private String createMessage(StatusType statusType) {
        StringBuilder message = new StringBuilder();

        switch (statusType) {
            case GOOD:
                message.append(localizationManager.translate("message.good"));
                break;
            case WARNING:
                message.append(localizationManager.translate("message.warning"));
                if (getDisallowedDevices().length() != 0) {
                    message.append(" ");
                    message.append(getDisallowedDevices());
                    message.append(".");
                }
                break;
            case DANGER:
                message.append(localizationManager.translate("message.danger"));
                break;
        }

        if (getTemperatureMessage() != null) {
            message.append(getTemperatureMessage());
        }

        return message.toString();
    }

    /**
     * Get list of disallowed devices
     */
    private StringJoiner getDisallowedDevices() {
        double leftAmperage = settingsManager.getMaxAmperage() - analyzeManager.getCurrentAmperage();

        StringJoiner devices = new StringJoiner(", ");
        analyzeManager.getCurrentRecognizedDevices().forEach((device, isEnabled) -> {
            if (!isEnabled && device.getAmperage() > leftAmperage) {
                devices.add(device.getName());
            }
        });

        return devices;
    }

    /**
     * Check temperature
     */
    private String getTemperatureMessage() {
        if (analyzeManager.getCurrentTemperature() >= Settings.MIN_TEMPERATURE && analyzeManager.getCurrentTemperature() <= Settings.MAX_TEMPERATURE) {
            return null;
        }

        int deviation = (int) Math.abs((analyzeManager.getCurrentTemperature() - Settings.NORMAL_TEMPERATURE) / Settings.NORMAL_TEMPERATURE * 100);

        if (analyzeManager.getCurrentTemperature() < Settings.MIN_TEMPERATURE) {
            return String.format(localizationManager.translate("message.low"), deviation);
        } else if (analyzeManager.getCurrentTemperature() > Settings.MAX_TEMPERATURE) {
            return String.format(localizationManager.translate("message.high"), deviation);
        }

        return null;
    }
}
