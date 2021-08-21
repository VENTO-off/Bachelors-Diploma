package nuzp.yuliy_gorichenko.openhab_helper.gui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import nuzp.yuliy_gorichenko.openhab_helper.manager.LocalizationManager;
import nuzp.yuliy_gorichenko.openhab_helper.utils.FormatUtils;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.DeviceManager.Device;

public class OnlineDevice extends HBox {
    private final Device device;
    private final Label name;
    private final Label wattage;
    private final StackPane indicator;
    private final Label status;

    /**
     * Constructor
     */
    public OnlineDevice(Device device, boolean isEnabled, double currentVoltage, LocalizationManager localizationManager) {
        super();
        this.setPrefHeight(30);
        this.getStyleClass().add("list-item-white");

        this.device = device;

        name = new Label();
        name.setText(device.getName());
        name.setPrefSize(250, this.getPrefHeight());
        name.setFont(Font.font(14));

        wattage = new Label();
        wattage.setText(FormatUtils.formatWatts(device.getAmperage() * currentVoltage, localizationManager));
        wattage.setPrefSize(60, this.getPrefHeight());
        wattage.setFont(Font.font(null, FontPosture.ITALIC, 14));

        indicator = new StackPane();
        indicator.setPrefSize(this.getPrefHeight(), this.getPrefHeight());
        Circle circle = new Circle(5);
        indicator.getChildren().add(circle);

        status = new Label();
        status.setPrefHeight(this.getPrefHeight());
        status.setFont(Font.font(null, FontWeight.BOLD, 14));

        if (isEnabled) {
            status.setText(localizationManager.translate("devices.on"));
            status.setTextFill(Color.valueOf("#4caf50"));
            circle.getStyleClass().add("indicator-green");
        } else {
            status.setText(localizationManager.translate("devices.off"));
            status.setTextFill(Color.valueOf("#F44336"));
            circle.getStyleClass().add("indicator-red");
            wattage.setText(null);
        }

        this.getChildren().addAll(name, wattage, indicator, status);
    }
}
