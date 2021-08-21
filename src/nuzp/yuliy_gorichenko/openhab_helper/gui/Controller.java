package nuzp.yuliy_gorichenko.openhab_helper.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nuzp.yuliy_gorichenko.openhab_helper.gui.tabs.DevicesTab;
import nuzp.yuliy_gorichenko.openhab_helper.gui.tabs.MainTab;
import nuzp.yuliy_gorichenko.openhab_helper.gui.tabs.SettingsTab;
import nuzp.yuliy_gorichenko.openhab_helper.manager.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Stage window;
    private double xOffset = 0;
    private double yOffset = 0;
    private SettingsManager settingsManager;
    private LocalizationManager localizationManager;
    private OpenHabManager openHabManager;
    private DataBaseManager dataBaseManager;
    private DeviceManager deviceManager;
    private AnalyzeManager analyzeManager;
    private NotifyManager notifyManager;
    private MainTab mainTab;
    private DevicesTab devicesTab;
    private SettingsTab settingsTab;

    @FXML
    private AnchorPane layout;

    @FXML
    private AnchorPane title;
    @FXML
    private StackPane minimizeButton;
    @FXML
    private StackPane closeButton;

    @FXML
    private Label currentVoltage;
    @FXML
    private Label currentAmperage;
    @FXML
    private Label currentPercent;
    @FXML
    private Label nominalAmperage;
    @FXML
    private Label currentTemperature;
    @FXML
    private Rectangle statusBorder;
    @FXML
    private FontAwesomeIconView statusIcon;
    @FXML
    private Label statusText;
    @FXML
    private AreaChart<String, Number> chart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private StackPane mainBlock;

    @FXML
    private AnchorPane showDevices;
    @FXML
    private VBox currentDevices;
    @FXML
    private Button editDevices;
    @FXML
    private AnchorPane editorDevices;
    @FXML
    private VBox allDevices;
    @FXML
    private TextField deviceName;
    @FXML
    private TextField deviceAmperage;
    @FXML
    private TextField deviceItem;
    @FXML
    private Button addDevice;
    @FXML
    private Button saveDevices;
    @FXML
    private StackPane devicesBlock;

    @FXML
    private ComboBox language;
    @FXML
    private TextField openHabIp;
    @FXML
    private TextField openHabPort;
    @FXML
    private TextField updateInterval;
    @FXML
    private TextField itemAmperage;
    @FXML
    private TextField itemVoltage;
    @FXML
    private TextField itemTemperature;
    @FXML
    private TextField minAmperage;
    @FXML
    private TextField maxAmperage;
    @FXML
    private TextField warningPercentage;
    @FXML
    private TextField itemMessage;
    @FXML
    private TextField mysqlIp;
    @FXML
    private TextField mysqlPort;
    @FXML
    private TextField mysqlUser;
    @FXML
    private TextField mysqlPassword;
    @FXML
    private TextField mysqlDatabase;
    @FXML
    private Button saveSettings;

    /**
     * Init GUI
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initWindowDragEvents();
        this.initMinimizeButtonEvents();
        this.initCloseButtonEvents();
        this.settingsManager = new SettingsManager();
        this.localizationManager = new LocalizationManager(settingsManager, layout, resources);
        this.openHabManager = new OpenHabManager(settingsManager);
        this.dataBaseManager = new DataBaseManager(settingsManager);
        this.deviceManager = new DeviceManager(dataBaseManager, localizationManager);
        this.analyzeManager = new AnalyzeManager(settingsManager, localizationManager, openHabManager, deviceManager);
        this.notifyManager = new NotifyManager(settingsManager, localizationManager, openHabManager, analyzeManager);
        this.mainTab = new MainTab(settingsManager, localizationManager, openHabManager, analyzeManager, currentVoltage, currentAmperage, currentPercent, nominalAmperage, currentTemperature, statusBorder, statusIcon, statusText, chart, xAxis, yAxis, mainBlock);
        this.devicesTab = new DevicesTab(settingsManager, localizationManager, deviceManager, analyzeManager, showDevices, currentDevices, editDevices, editorDevices, allDevices, deviceName, deviceAmperage, deviceItem, addDevice, saveDevices, devicesBlock);
        this.settingsTab = new SettingsTab(settingsManager, localizationManager, language, openHabIp, openHabPort, updateInterval, itemAmperage, itemVoltage, itemTemperature, minAmperage, maxAmperage, warningPercentage, itemMessage, mysqlIp, mysqlPort, mysqlUser, mysqlPassword, mysqlDatabase, saveSettings);
    }

    /**
     * Show GUI
     */
    public void show(Parent root, Stage window) {
        this.window = window;
        this.window.setTitle("OpenHAB Helper");
        this.window.initStyle(StageStyle.UNDECORATED);
        this.window.setResizable(false);
        this.window.setScene(new Scene(root, 600, 400));
        this.window.show();
    }

    /**
     * Init window drag events
     */
    private void initWindowDragEvents() {
        title.setOnMouseDragged(e -> {
            window.setX(e.getScreenX() + xOffset);
            window.setY(e.getScreenY() + yOffset);
        });

        title.setOnMousePressed(e -> {
            xOffset = window.getX() - e.getScreenX();
            yOffset = window.getY() - e.getScreenY();
        });
    }

    /**
     * Init minimize button events
     */
    private void initMinimizeButtonEvents() {
        minimizeButton.setOnMouseEntered(e -> {
            minimizeButton.setBackground(new Background(new BackgroundFill(Color.valueOf("#BFBFBF"), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        minimizeButton.setOnMouseExited(e -> {
            minimizeButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        minimizeButton.setOnMouseClicked(e -> window.setIconified(true));
    }

    /**
     * Init close button events
     */
    private void initCloseButtonEvents() {
        closeButton.setOnMouseEntered(e -> {
            closeButton.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            FontAwesomeIconView icon = (FontAwesomeIconView) closeButton.getChildren().stream().findAny().get();
            icon.setFill(Color.WHITE);
        });

        closeButton.setOnMouseExited(e -> {
            closeButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            FontAwesomeIconView icon = (FontAwesomeIconView) closeButton.getChildren().stream().findAny().get();
            icon.setFill(Color.BLACK);
        });

        closeButton.setOnMouseClicked(e -> {
            analyzeManager.getAnalyzerThread().shutdown();
            Platform.exit();
        });
    }
}
