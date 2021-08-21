package nuzp.yuliy_gorichenko.openhab_helper.gui.tabs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import nuzp.yuliy_gorichenko.openhab_helper.Settings;
import nuzp.yuliy_gorichenko.openhab_helper.manager.AnalyzeManager;
import nuzp.yuliy_gorichenko.openhab_helper.manager.LocalizationManager;
import nuzp.yuliy_gorichenko.openhab_helper.manager.OpenHabManager;
import nuzp.yuliy_gorichenko.openhab_helper.manager.SettingsManager;
import nuzp.yuliy_gorichenko.openhab_helper.utils.FormatUtils;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.AnalyzeManager.StatusType;

public class MainTab {
    private final SettingsManager settingsManager;
    private final LocalizationManager localizationManager;
    private final OpenHabManager openHabManager;
    private final AnalyzeManager analyzeManager;
    private final Label currentVoltage;
    private final Label currentAmperage;
    private final Label currentPercent;
    private final Label nominalAmperage;
    private final Label currentTemperature;
    private final Rectangle statusBorder;
    private final FontAwesomeIconView statusIcon;
    private final Label statusText;
    private final AreaChart<String, Number> chart;
    private final CategoryAxis xAxis;
    private final NumberAxis yAxis;
    private final StackPane mainBlock;
    private final XYChart.Series<String, Number> series;

    /**
     * Constructor
     */
    public MainTab(SettingsManager settingsManager, LocalizationManager localizationManager, OpenHabManager openHabManager, AnalyzeManager analyzeManager, Label currentVoltage, Label currentAmperage, Label currentPercent, Label nominalAmperage, Label currentTemperature, Rectangle statusBorder, FontAwesomeIconView statusIcon, Label statusText, AreaChart<String, Number> chart, CategoryAxis xAxis, NumberAxis yAxis, StackPane mainBlock) {
        this.settingsManager = settingsManager;
        this.localizationManager = localizationManager;
        this.openHabManager = openHabManager;
        this.analyzeManager = analyzeManager;
        this.currentVoltage = currentVoltage;
        this.currentAmperage = currentAmperage;
        this.currentPercent = currentPercent;
        this.nominalAmperage = nominalAmperage;
        this.currentTemperature = currentTemperature;
        this.statusBorder = statusBorder;
        this.statusIcon = statusIcon;
        this.statusText = statusText;
        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.mainBlock = mainBlock;
        this.series = new XYChart.Series<>();
        this.chart.getData().add(series);
        this.mainBlock.setVisible(!settingsManager.isSettingsDone());
        this.setStatus(StatusType.CONNECTING);
        this.initListener();
    }

    /**
     * Listen to AnalyzeManager
     */
    private void initListener() {
        analyzeManager.addStatusListener(this::setStatus);
        analyzeManager.addElectricListener(this::renderElectric);
        analyzeManager.addTemperatureListener(this::renderTemperature);
    }

    /**
     * Render current amperage and voltage
     */
    private void renderElectric(double amperage, double voltage) {
        Platform.runLater(() -> {
            currentVoltage.setText(FormatUtils.formatVolts(voltage, localizationManager));
            currentAmperage.setText(FormatUtils.formatAmperes(amperage, localizationManager) + " / " + FormatUtils.formatWatts(amperage * voltage, localizationManager));
            currentPercent.setText((int) (amperage * 100 / settingsManager.getMaxAmperage()) + "%" + " / " + "100%");
            nominalAmperage.setText(FormatUtils.formatAmperes(settingsManager.getMaxAmperage(), localizationManager));
            renderChart(amperage);
        });
    }

    /**
     * Render chart
     */
    private void renderChart(double amperage) {
        final int xAxisWidth = (int) (Settings.CHART_HISTORY / settingsManager.getUpdateInterval());

        if (series.getData().isEmpty()) {
            for (int i = 0; i < xAxisWidth; i++) {
                series.getData().add(new XYChart.Data<>(String.valueOf(i), 0));
            }
        }

        if (series.getData().size() >= xAxisWidth) {
            series.getData().remove(0);
        }

        yAxis.setUpperBound(settingsManager.getMaxAmperage());
        series.getData().add(new XYChart.Data<>(String.valueOf(System.currentTimeMillis()), amperage));
    }

    /**
     * Render current temperature
     */
    private void renderTemperature(double temperature) {
        Platform.runLater(() -> {
            currentTemperature.setText(temperature + " \u2103");
        });
    }

    /**
     * Set current status
     */
    private void setStatus(StatusType status) {
        Platform.runLater(() -> {
            Color color = null;
            FontAwesomeIcon icon = null;
            String message = null;

            switch (status) {
                case GOOD:
                    color = Color.valueOf("#4CAF50");
                    icon = FontAwesomeIcon.CHECK_CIRCLE;
                    message = localizationManager.translate("main.good");
                    break;

                case WARNING:
                    color = Color.valueOf("#FF9800");
                    icon = FontAwesomeIcon.EXCLAMATION_CIRCLE;
                    message = localizationManager.translate("main.warning");
                    break;

                case DANGER:
                    color = Color.valueOf("#F44336");
                    icon = FontAwesomeIcon.EXCLAMATION_TRIANGLE;
                    message = localizationManager.translate("main.danger");
                    break;

                case CONNECTING:
                    color = Color.valueOf("#2196F3");
                    icon = FontAwesomeIcon.FEED;
                    message = localizationManager.translate("main.connecting");
                    break;

                case ERROR:
                    color = Color.valueOf("#F44336");
                    icon = FontAwesomeIcon.TIMES_CIRCLE;
                    message = localizationManager.translate("main.error");
                    break;
            }

            statusBorder.setStroke(color);
            statusIcon.setFill(color);
            statusIcon.setGlyphName(icon.name());
            statusText.setTextFill(color);
            statusText.setText(message);
        });
    }
}
