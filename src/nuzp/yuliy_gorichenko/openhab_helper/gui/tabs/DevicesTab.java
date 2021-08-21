package nuzp.yuliy_gorichenko.openhab_helper.gui.tabs;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nuzp.yuliy_gorichenko.openhab_helper.gui.elements.*;
import nuzp.yuliy_gorichenko.openhab_helper.manager.*;

import java.util.LinkedHashMap;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.DeviceManager.Device;

public class DevicesTab {
    private final SettingsManager settingsManager;
    private final LocalizationManager localizationManager;
    private final DeviceManager deviceManager;
    private final AnalyzeManager analyzeManager;
    private final AnchorPane showDevices;
    private final VBox currentDevices;
    private final Button editDevices;
    private final AnchorPane editorDevices;
    private final VBox allDevices;
    private final TextField deviceName;
    private final TextField deviceAmperage;
    private final TextField deviceItem;
    private final Button addDevice;
    private final Button saveDevices;
    private final StackPane devicesBlock;

    /**
     * Constructor
     */
    public DevicesTab(SettingsManager settingsManager, LocalizationManager localizationManager, DeviceManager deviceManager, AnalyzeManager analyzeManager, AnchorPane showDevices, VBox currentDevices, Button editDevices, AnchorPane editorDevices, VBox allDevices, TextField deviceName, TextField deviceAmperage, TextField deviceItem, Button addDevice, Button saveDevices, StackPane devicesBlock) {
        this.settingsManager = settingsManager;
        this.localizationManager = localizationManager;
        this.deviceManager = deviceManager;
        this.analyzeManager = analyzeManager;
        this.showDevices = showDevices;
        this.currentDevices = currentDevices;
        this.editDevices = editDevices;
        this.editorDevices = editorDevices;
        this.allDevices = allDevices;
        this.deviceName = deviceName;
        this.deviceAmperage = deviceAmperage;
        this.deviceItem = deviceItem;
        this.addDevice = addDevice;
        this.saveDevices = saveDevices;
        this.devicesBlock = devicesBlock;
        this.editDevices.setVisible(true);
        this.showDevices.setVisible(true);
        this.editorDevices.setVisible(false);
        this.devicesBlock.setVisible(!settingsManager.isSettingsDone());
        this.initTextFieldRegex();
        this.initButtonEvents();
        this.renderAllDevicesList();
        this.initListener();
    }

    /**
     * Init button events
     */
    private void initButtonEvents() {
        editDevices.setOnMouseClicked(e -> {
            editDevices.setVisible(false);
            showDevices.setVisible(false);
            editorDevices.setVisible(true);
        });

        addDevice.setOnMouseClicked(e -> {
            if (deviceName.getText() == null && deviceAmperage.getText() == null) {
                return;
            }

            addDevice(new Device(deviceName.getText(), Double.parseDouble(deviceAmperage.getText()), deviceItem.getText()));
            deviceName.setText(null);
            deviceAmperage.setText(null);
            deviceItem.setText(null);
        });

        saveDevices.setOnMouseClicked(e -> {
            deviceManager.saveAllDevices();
            editDevices.setVisible(true);
            showDevices.setVisible(true);
            editorDevices.setVisible(false);
        });
    }

    /**
     * Listen to AnalyzeManager
     */
    private void initListener() {
        analyzeManager.addRecognizedDevicesListener(this::renderRecognizedDevices);
    }

    /**
     * Render enabled devices
     */
    private void renderRecognizedDevices(LinkedHashMap<Device, Boolean> recognizedDevices) {
        Platform.runLater(() -> {
            currentDevices.getChildren().clear();
            recognizedDevices.forEach((device, isEnabled) -> currentDevices.getChildren().add(new OnlineDevice(device, isEnabled, analyzeManager.getCurrentVoltage(), localizationManager)));
        });
    }

    /**
     * Render all devices list
     */
    private void renderAllDevicesList() {
        allDevices.getChildren().clear();
        deviceManager.getAllDevices().forEach(device -> allDevices.getChildren().add(new EditableDevice(this, device, localizationManager)));
    }

    /**
     * Add device to all devices list
     */
    private void addDevice(Device device) {
        deviceManager.addDevice(device);
        renderAllDevicesList();
    }

    /**
     * Delete device from all devices list
     */
    public void deleteDevice(Device device) {
        deviceManager.removeDevice(device);
        renderAllDevicesList();
    }

    /**
     * Apply Regex for text fields
     */
    private void initTextFieldRegex() {
        deviceName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d\\p{L}\\s]*$")) {
                deviceName.setText(oldValue);
            }
        });

        deviceAmperage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\d.]*$")) {
                deviceAmperage.setText(oldValue);
            }
        });

        deviceItem.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d]*$")) {
                deviceItem.setText(oldValue);
            }
        });
    }
}
