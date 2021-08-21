package nuzp.yuliy_gorichenko.openhab_helper.manager;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class LocalizationManager extends SettingsManager {
    private SettingsManager settingsManager;
    private Pane layout;
    private ResourceBundle resources;
    private Locale locale;

    /**
     * Constructor
     */
    public LocalizationManager(SettingsManager settingsManager, AnchorPane layout, ResourceBundle resources) {
        this.settingsManager = settingsManager;
        this.layout = layout;
        this.resources = resources;
        this.locale = loadLocale();
        this.localize(layout);
    }

    /**
     * Get supported locales
     */
    public List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(
                new Locale("ru", "RU"),
                new Locale("uk", "UA"),
                new Locale("en", "US")
        ));
    }

    /**
     * Load locale
     */
    private Locale loadLocale() {
        if (settingsManager.getLanguage() == -1) {
            return getSupportedLocales().get(1);
        } else {
            return getSupportedLocales().get(settingsManager.getLanguage());
        }
    }

    /**
     * Get locale
     */
    public int getLocale() {
        return getSupportedLocales().indexOf(locale);
    }

    /**
     * Set locale
     */
    public void setLocale(int index) {
        locale = getSupportedLocales().get(index);
        settingsManager.setLanguage(index);
        localize(layout);
    }

    /**
     * Localize all elements
     */
    private void localize(Pane parent) {
        resources = ResourceBundle.getBundle("lang.lang", locale);

        for (Node node : parent.getChildren()) {
            if (node instanceof Label) {
                localizeLabel(node);
                continue;
            } else if (node instanceof Button) {
                localizeButton(node);
                continue;
            } else if (node instanceof TextField) {
                localizeTextField(node);
                continue;
            }

            if (node instanceof Pane) {
                localize((Pane) node);
            } else if (node instanceof TabPane) {
                for (Tab tab : ((TabPane) node).getTabs()) {
                    if (tab.getGraphic() instanceof Pane) {
                        localize((Pane) tab.getGraphic());
                    }

                    if (tab.getContent() instanceof Pane) {
                        localize((Pane) tab.getContent());
                    } else if (tab.getContent() instanceof ScrollPane) {
                        localize((Pane) ((ScrollPane) tab.getContent()).getContent());
                    }
                }
            }
        }
    }

    /**
     * Localize Label
     */
    private void localizeLabel(Node node) {
        Label label = (Label) node;
        if (label.getAccessibleText() == null) {
            label.setAccessibleText(label.getText());
        }

        label.setText(translate(label.getAccessibleText()));
    }

    /**
     * Localize Button
     */
    private void localizeButton(Node node) {
        Button button = (Button) node;
        if (button.getAccessibleText() == null) {
            button.setAccessibleText(button.getText());
        }

        button.setText(translate(button.getAccessibleText()));
    }

    /**
     * Localize TextField
     */
    private void localizeTextField(Node node) {
        TextField textField = (TextField) node;
        if (textField.getAccessibleText() == null) {
            textField.setAccessibleText(textField.getPromptText());
        }

        textField.setPromptText(translate(textField.getAccessibleText()));
    }

    /**
     * Translate
     */
    public String translate(String key) {
        try {
            return new String(resources.getString(key).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return key;
        }
    }
}
