package nuzp.yuliy_gorichenko.openhab_helper.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import nuzp.yuliy_gorichenko.openhab_helper.exceptions.OpenHabItemException;
import nuzp.yuliy_gorichenko.openhab_helper.manager.LocalizationManager;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DialogUtils {

    /**
     * Show error message
     */
    public static void showErrorMessage(Exception exception, LocalizationManager localizationManager) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(localizationManager.translate("error.title"));
        alert.setHeaderText(localizationManager.translate(exception.getMessage()));
        alert.setContentText(localizationManager.translate("error.stacktrace"));

        if (exception instanceof OpenHabItemException) {
            alert.setHeaderText(String.format(alert.getHeaderText(), ((OpenHabItemException) exception).getItemName()));
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane content = new GridPane();
        content.setMaxWidth(Double.MAX_VALUE);
        content.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(content);
        alert.showAndWait();
    }
}
