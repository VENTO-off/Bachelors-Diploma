package nuzp.yuliy_gorichenko.openhab_helper.manager;

import javafx.application.Platform;
import nuzp.yuliy_gorichenko.openhab_helper.exceptions.*;
import nuzp.yuliy_gorichenko.openhab_helper.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.DeviceManager.Device;

public class AnalyzeManager {
    private final SettingsManager settingsManager;
    private final LocalizationManager localizationManager;
    private final OpenHabManager openHabManager;
    private final DeviceManager deviceManager;
    private final ScheduledExecutorService scheduledExecutorService;
    private StatusType lastStatus;
    private final List<StatusListener> statusListeners;
    private final List<ElectricListener> electricListeners;
    private final List<TemperatureListener> temperatureListeners;
    private final List<RecognizedDevicesListener> recognizedDevicesListeners;
    private double currentAmperage;
    private double currentVoltage;
    private double currentTemperature;
    private HashMap<Device, Boolean> enabledDevices;
    private LinkedHashMap<Device, Boolean> recognizedDevices;

    /**
     * Status Type
     */
    public enum StatusType {
        GOOD,
        WARNING,
        DANGER,
        CONNECTING,
        ERROR
    }

    /**
     * Constructor
     */
    public AnalyzeManager(SettingsManager settingsManager, LocalizationManager localizationManager, OpenHabManager openHabManager, DeviceManager deviceManager) {
        this.settingsManager = settingsManager;
        this.localizationManager = localizationManager;
        this.openHabManager = openHabManager;
        this.deviceManager = deviceManager;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.lastStatus = StatusType.CONNECTING;
        this.statusListeners = new ArrayList<>();
        this.electricListeners = new ArrayList<>();
        this.temperatureListeners = new ArrayList<>();
        this.recognizedDevicesListeners = new ArrayList<>();
        this.currentAmperage = 0;
        this.currentTemperature = 0;
        this.enabledDevices = new HashMap<>();
        this.recognizedDevices = new LinkedHashMap<>();
        this.initAnalyzer();
    }

    /**
     * Init OpenHAB data analyzer
     */
    private void initAnalyzer() {
        if (!settingsManager.isSettingsDone()) {
            return;
        }

        // run in new thread
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            final double warning = settingsManager.getMaxAmperage() * settingsManager.getWarningPercentage() / 100.0;
            StatusType currentStatus = null;

            try {
                // retrieve data from OpenHAB
                currentAmperage = getAmperage();
                currentVoltage = getVoltage();
                currentTemperature = getTemperature();
                enabledDevices = getEnabledDevices();
                recognizedDevices = getRecognizedDevices();

                // get current status
                if (currentAmperage >= 0 && currentAmperage < warning) {
                    currentStatus = StatusType.GOOD;
                } else if (currentAmperage >= warning && currentAmperage < settingsManager.getMaxAmperage()) {
                    currentStatus = StatusType.WARNING;
                } else if (currentAmperage >= settingsManager.getMaxAmperage()) {
                    currentStatus = StatusType.DANGER;
                }

                // notify all listeners
                electricListeners.forEach(listener -> listener.onUpdate(currentAmperage, currentVoltage));
                temperatureListeners.forEach(listener -> listener.onUpdate(currentTemperature));
                recognizedDevicesListeners.forEach(listener -> listener.onUpdate(recognizedDevices));
            } catch (OpenHabItemException | OpenHabConnectionException e) {
                currentStatus = StatusType.ERROR;
                Platform.runLater(() -> DialogUtils.showErrorMessage(e, localizationManager));
            }

            // notify status listener on change
            if (lastStatus != currentStatus) {
                lastStatus = currentStatus;
                statusListeners.forEach(listener -> listener.onUpdate(lastStatus));
            }
        }, settingsManager.getUpdateInterval(), settingsManager.getUpdateInterval(), TimeUnit.MILLISECONDS);
    }

    /**
     * Get analyzer thread
     */
    public ScheduledExecutorService getAnalyzerThread() {
        return scheduledExecutorService;
    }

    /**
     * Get current amperage
     */
    private double getAmperage() throws OpenHabItemException, OpenHabConnectionException {
        return Double.parseDouble(openHabManager.getData(settingsManager.getItemAmperage()));
    }

    /**
     * Get current voltage
     */
    private double getVoltage() throws OpenHabItemException, OpenHabConnectionException {
        return Double.parseDouble(openHabManager.getData(settingsManager.getItemVoltage()));
    }

    /**
     * Get current temperature
     */
    private double getTemperature() throws OpenHabItemException, OpenHabConnectionException {
        return Double.parseDouble(openHabManager.getData(settingsManager.getItemTemperature()));
    }

    /**
     * Get enabled devices
     */
    private HashMap<Device, Boolean> getEnabledDevices() throws OpenHabItemException, OpenHabConnectionException {
        HashMap<Device, Boolean> enabledDevices = new HashMap<>();

        for (Device device : deviceManager.getAllDevices()) {
            if (!device.getItem().isEmpty()) {
                String state = openHabManager.getData(device.getItem());
                enabledDevices.put(device, state.equalsIgnoreCase("ON"));
            }
        }

        return enabledDevices;
    }

    /**
     * Get recognized devices
     */
    private LinkedHashMap<Device, Boolean> getRecognizedDevices() {
        LinkedHashMap<Device, Boolean> recognizedDeices = new LinkedHashMap<>();

        DeviceRecognizer recognizer = new DeviceRecognizer(deviceManager.getAllDevices(), currentAmperage, enabledDevices, settingsManager.getMinAmperage());
        recognizer.recognize();
        for (Device device : recognizer.getFinalDevices()) {
            recognizedDeices.put(device, true);
        }
        for (Device device : deviceManager.getAllDevices()) {
            if (!recognizer.getFinalDevices().contains(device)) {
                recognizedDeices.put(device, false);
            }
        }

        return recognizedDeices;
    }

    /**
     * Get current amperage value
     */
    public double getCurrentAmperage() {
        return currentAmperage;
    }

    /**
     * Get current voltage value
     */
    public double getCurrentVoltage() {
        return currentVoltage;
    }

    /**
     * Get current temperature value
     */
    public double getCurrentTemperature() {
        return currentTemperature;
    }

    /**
     * Get current recognized devices
     */
    public LinkedHashMap<Device, Boolean> getCurrentRecognizedDevices() {
        return recognizedDevices;
    }

    /**
     * Current status listener
     */
    public interface StatusListener {
        void onUpdate(StatusType statusType);
    }

    /**
     * Add current status listener
     */
    public void addStatusListener(StatusListener listener) {
        this.statusListeners.add(listener);
    }

    /**
     * Current electric listener
     */
    public interface ElectricListener {
        void onUpdate(double amperage, double voltage);
    }

    /**
     * Add current electric listener
     */
    public void addElectricListener(ElectricListener listener) {
        this.electricListeners.add(listener);
    }

    /**
     * Current temperature listener
     */
    public interface TemperatureListener {
        void onUpdate(double temperature);
    }

    /**
     * Add current temperature listener
     */
    public void addTemperatureListener(TemperatureListener listener) {
        this.temperatureListeners.add(listener);
    }

    /**
     * Current recognized devices
     */
    public interface RecognizedDevicesListener {
        void onUpdate(LinkedHashMap<Device, Boolean> devices);
    }

    /**
     * Add current enabled devices listener
     */
    public void addRecognizedDevicesListener(RecognizedDevicesListener listener) {
        this.recognizedDevicesListeners.add(listener);
    }
}
