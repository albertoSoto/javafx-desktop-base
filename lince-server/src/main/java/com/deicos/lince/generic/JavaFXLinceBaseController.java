package com.deicos.lince.generic;

import com.deicos.lince.App;
import com.deicos.lince.JavaFXLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lince-scientific-base
 * com.deicos.lince.generic
 * Created by alber in 24/02/2017.
 * Description:
 */
public abstract class JavaFXLinceBaseController {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    // Reference to the main application.
    private App mainApp;
    public App getMainApp() {
        return mainApp;
    }
    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleAbout() {
        JavaFXLoader.showMessage(Alert.AlertType.INFORMATION
                , "About"
                , "Author: Alberto Soto Fernandez\nWebsite: http://www.albertosoto.es");
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        JavaFXLoader.exit();
    }
}
