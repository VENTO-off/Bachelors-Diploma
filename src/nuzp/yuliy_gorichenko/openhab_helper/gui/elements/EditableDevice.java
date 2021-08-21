package nuzp.yuliy_gorichenko.openhab_helper.gui.elements;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import nuzp.yuliy_gorichenko.openhab_helper.gui.tabs.DevicesTab;
import nuzp.yuliy_gorichenko.openhab_helper.manager.LocalizationManager;
import nuzp.yuliy_gorichenko.openhab_helper.utils.FormatUtils;

import static nuzp.yuliy_gorichenko.openhab_helper.manager.DeviceManager.Device;

public class EditableDevice extends HBox {
    private final DevicesTab devicesTab;
    private final Device device;
    private final Label name;
    private final Label item;
    private final Button delete;

    /**
     * Constructor
     */
    public EditableDevice(DevicesTab devicesTab, Device device, LocalizationManager localizationManager) {
        super();
        this.setPrefHeight(30);
        this.getStyleClass().add("list-item");

        this.devicesTab = devicesTab;
        this.device = device;

        name = new Label();
        name.setText(device.getName() + " (" + FormatUtils.formatAmperes(device.getAmperage(), localizationManager) + ")");
        name.setPrefSize(250, this.getPrefHeight());
        name.setFont(Font.font(14));

        item = new Label();
        item.setText(device.getItem());
        item.setPrefSize(135, this.getPrefHeight());
        item.setFont(Font.font(null, FontPosture.ITALIC, 14));

        delete = new Button();
        delete.setPrefSize(this.getPrefHeight(), this.getPrefHeight());
        delete.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        delete.setAlignment(Pos.CENTER);
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        icon.setFill(Color.valueOf("#F44336"));
        delete.setGraphic(icon);
        delete.getStyleClass().add("button-list");
        delete.setOnMouseClicked(e -> devicesTab.deleteDevice(device));

        this.getChildren().addAll(name, item, delete);
    }
}
