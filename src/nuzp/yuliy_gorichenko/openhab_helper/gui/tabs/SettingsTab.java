package nuzp.yuliy_gorichenko.openhab_helper.gui.tabs;

import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import nuzp.yuliy_gorichenko.openhab_helper.manager.LocalizationManager;
import nuzp.yuliy_gorichenko.openhab_helper.manager.SettingsManager;

public class SettingsTab {
    private final SettingsManager settingsManager;
    private final LocalizationManager localizationManager;
    private final ComboBox language;
    private final TextField openHabIp;
    private final TextField openHabPort;
    private final TextField updateInterval;
    private final TextField itemAmperage;
    private final TextField itemVoltage;
    private final TextField itemTemperature;
    private final TextField minAmperage;
    private final TextField maxAmperage;
    private final TextField warningPercentage;
    private final TextField itemMessage;
    private final TextField mysqlIp;
    private final TextField mysqlPort;
    private final TextField mysqlUser;
    private final TextField mysqlPassword;
    private final TextField mysqlDatabase;
    private final Button saveSettings;

    /**
     * Constructor
     */
    public SettingsTab(SettingsManager settingsManager, LocalizationManager localizationManager, ComboBox language, TextField openHabIp, TextField openHabPort, TextField updateInterval, TextField itemAmperage, TextField itemVoltage, TextField itemTemperature, TextField minAmperage, TextField maxAmperage, TextField warningPercentage, TextField itemMessage, TextField mysqlIp, TextField mysqlPort, TextField mysqlUser, TextField mysqlPassword, TextField mysqlDatabase, Button saveSettings) {
        this.settingsManager = settingsManager;
        this.localizationManager = localizationManager;
        this.language = language;
        this.openHabIp = openHabIp;
        this.openHabPort = openHabPort;
        this.updateInterval = updateInterval;
        this.itemAmperage = itemAmperage;
        this.itemVoltage = itemVoltage;
        this.itemTemperature = itemTemperature;
        this.minAmperage = minAmperage;
        this.maxAmperage = maxAmperage;
        this.warningPercentage = warningPercentage;
        this.itemMessage = itemMessage;
        this.mysqlIp = mysqlIp;
        this.mysqlPort = mysqlPort;
        this.mysqlUser = mysqlUser;
        this.mysqlPassword = mysqlPassword;
        this.mysqlDatabase = mysqlDatabase;
        this.saveSettings = saveSettings;
        this.saveSettings.setOnMouseClicked(this::save);
        this.language.getSelectionModel().selectedIndexProperty().addListener(this::onLanguageChange);
        this.initTextFieldRegex();
        this.render();
    }

    /**
     * Render settings values
     */
    private void render() {
        localizationManager.getSupportedLocales().forEach(locale -> language.getItems().add(locale.getDisplayLanguage()));
        language.getSelectionModel().select(localizationManager.getLocale());

        if (settingsManager.getOpenHabIp() != null)
            openHabIp.setText(settingsManager.getOpenHabIp());

        if (settingsManager.getOpenHabPort() != 0)
            openHabPort.setText(String.valueOf(settingsManager.getOpenHabPort()));

        if (settingsManager.getUpdateInterval() != 0)
            updateInterval.setText(String.valueOf(settingsManager.getUpdateInterval()));

        if (settingsManager.getItemAmperage() != null)
            itemAmperage.setText(settingsManager.getItemAmperage());

        if (settingsManager.getItemVoltage() != null)
            itemVoltage.setText(settingsManager.getItemVoltage());

        if (settingsManager.getItemTemperature() != null)
            itemTemperature.setText(settingsManager.getItemTemperature());

        if (settingsManager.getMinAmperage() != 0)
            minAmperage.setText(String.valueOf(settingsManager.getMinAmperage()));

        if (settingsManager.getMaxAmperage() != 0)
            maxAmperage.setText(String.valueOf(settingsManager.getMaxAmperage()));

        if (settingsManager.getWarningPercentage() != 0)
            warningPercentage.setText(String.valueOf(settingsManager.getWarningPercentage()));

        if (settingsManager.getItemMessage() != null)
            itemMessage.setText(settingsManager.getItemMessage());

        if (settingsManager.getMysqlIp() != null)
            mysqlIp.setText(settingsManager.getMysqlIp());

        if (settingsManager.getMysqlPort() != 0)
            mysqlPort.setText(String.valueOf(settingsManager.getMysqlPort()));

        if (settingsManager.getMysqlUser() != null)
            mysqlUser.setText(settingsManager.getMysqlUser());

        if (settingsManager.getMysqlPassword() != null)
            mysqlPassword.setText(settingsManager.getMysqlPassword());

        if (settingsManager.getMysqlDatabase() != null)
            mysqlDatabase.setText(settingsManager.getMysqlDatabase());
    }

    /**
     * Save settings values
     */
    private void save(MouseEvent e) {
        if (!openHabIp.getText().isEmpty())
            settingsManager.setOpenHabIp(openHabIp.getText());

        if (!openHabPort.getText().isEmpty())
            settingsManager.setOpenHabPort(Integer.parseInt(openHabPort.getText()));

        if (!updateInterval.getText().isEmpty())
            settingsManager.setUpdateInterval(Long.parseLong(updateInterval.getText()));

        if (!itemAmperage.getText().isEmpty())
            settingsManager.setItemAmperage(itemAmperage.getText());

        if (!itemVoltage.getText().isEmpty())
            settingsManager.setItemVoltage(itemVoltage.getText());

        if (!itemTemperature.getText().isEmpty())
            settingsManager.setItemTemperature(itemTemperature.getText());

        if (!minAmperage.getText().isEmpty())
            settingsManager.setMinAmperage(Double.parseDouble(minAmperage.getText()));

        if (!maxAmperage.getText().isEmpty())
            settingsManager.setMaxAmperage(Integer.parseInt(maxAmperage.getText()));

        if (!warningPercentage.getText().isEmpty())
            settingsManager.setWarningPercentage(Integer.parseInt(warningPercentage.getText()));

        if (!itemMessage.getText().isEmpty())
            settingsManager.setItemMessage(itemMessage.getText());

        if (!mysqlIp.getText().isEmpty())
            settingsManager.setMysqlIp(mysqlIp.getText());

        if (!mysqlPort.getText().isEmpty())
            settingsManager.setMysqlPort(Integer.parseInt(mysqlPort.getText()));

        if (!mysqlUser.getText().isEmpty())
            settingsManager.setMysqlUser(mysqlUser.getText());

        if (!mysqlPassword.getText().isEmpty())
            settingsManager.setMysqlPassword(mysqlPassword.getText());

        if (!mysqlDatabase.getText().isEmpty())
            settingsManager.setMysqlDatabase(mysqlDatabase.getText());

        settingsManager.save();
    }

    /**
     * Language change event
     */
    private void onLanguageChange(Observable observable) {
        localizationManager.setLocale(language.getSelectionModel().getSelectedIndex());
    }

    /**
     * Apply Regex for text fields
     */
    private void initTextFieldRegex() {
        openHabIp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[0-9.]*$")) {
                openHabIp.setText(oldValue);
            }
        });

        openHabPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^\\d{0,5}+$")) {
                openHabPort.setText(oldValue);
            }
        });

        updateInterval.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^\\d{0,5}+$")) {
                updateInterval.setText(oldValue);
            }
        });

        itemAmperage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d]*$")) {
                itemAmperage.setText(oldValue);
            }
        });

        itemVoltage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d]*$")) {
                itemVoltage.setText(oldValue);
            }
        });

        itemTemperature.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d]*$")) {
                itemTemperature.setText(oldValue);
            }
        });

        minAmperage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\d.]*$")) {
                minAmperage.setText(oldValue);
            }
        });

        maxAmperage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^\\d{0,3}+$")) {
                maxAmperage.setText(oldValue);
            }
        });

        warningPercentage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^\\d{0,2}+$")) {
                warningPercentage.setText(oldValue);
            }
        });

        itemMessage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d]*$")) {
                itemMessage.setText(oldValue);
            }
        });

        mysqlIp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[0-9.]*$")) {
                mysqlIp.setText(oldValue);
            }
        });

        mysqlPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^\\d{0,5}+$")) {
                mysqlPort.setText(oldValue);
            }
        });

        mysqlUser.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d]*$")) {
                mysqlUser.setText(oldValue);
            }
        });

        mysqlDatabase.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (!newValue.matches("^[\\w\\d]*$")) {
                mysqlDatabase.setText(oldValue);
            }
        });
    }
}
