package nuzp.yuliy_gorichenko.openhab_helper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import nuzp.yuliy_gorichenko.openhab_helper.gui.Controller;

/**************************************
*     Powered by Yuliy Gorichenko     *
***************************************/

public class Main extends Application {
    /**
     * Start JavaFX application
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/form.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.show(root, stage);
    }

    /**
     * Run application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
