package com.deicos.lince.view;

import com.deicos.lince.App;
import com.deicos.lince.JavaFXLoader;
import com.deicos.lince.LinceDataHelper;
import com.deicos.lince.generic.JavaFXLinceBaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Marco Jakob
 */
public class RootLayoutController extends JavaFXLinceBaseController {

    // Reference to the main application
    private App mainApp;
    LinceDataHelper dataHelper = LinceDataHelper.getInstance();

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        mainApp.getPersonData().clear();
        dataHelper.setPersonFilePath(null, mainApp.getPrimaryStage());
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            dataHelper.loadPersonDataFromFile(file, mainApp.getPrimaryStage());
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File personFile = LinceDataHelper.getInstance().getPersonFilePath();
        if (personFile != null) {
            dataHelper.savePersonDataToFile(personFile, mainApp.getPrimaryStage());
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            dataHelper.savePersonDataToFile(file, mainApp.getPrimaryStage());
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        JavaFXLoader.showMessage(AlertType.INFORMATION
                , "About"
                , "Author: Alberto Soto\nWebsite: http://www.albertosoto.es");
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        JavaFXLoader.exit();
    }

    /**
     * Opens the birthday statistics.
     */
    @FXML
    private void handleShowBirthdayStatistics() {
        mainApp.showBirthdayStatistics();
    }
}