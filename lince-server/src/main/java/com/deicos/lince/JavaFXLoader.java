package com.deicos.lince;

import com.deicos.lince.generic.JavaFXLinceBaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lince-scientific-base
 * com.deicos.lince
 * Created by alber in 23/02/2017.
 * Description:
 */
public class JavaFXLoader<T extends JavaFXLinceBaseController> {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected FXMLLoader loader;
    private App mainApp;
    private String location;
    private Pane pane;
    private T controller;

    public T getController() {
        if (controller==null){
            this.controller = loader.getController();
        }
        return controller;
    }


    public Pane getPane() {
        return pane;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public JavaFXLoader(String location, App mainApp){
        try {
            this.location = location;
            this.loader = new FXMLLoader();
            this.mainApp=mainApp;
            loader.setLocation(this.mainApp.getClass().getResource(location));
            this.pane = loader.load();
        }catch (Exception e){
            log.error(this.getClass().getEnclosingMethod().toString(), e);
        }
    }


    public void initController() {
        try {
            // Load root layout from fxml file.
            //FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(mainApp.getClass().getResource(location));
            //Pane pane = loader.load();
            T controller = loader.getController();
            controller.setMainApp(mainApp);

        } catch (Exception e) {
            log.error("initController", e);

        }
    }

    public void loadFXMLStage() {
        try {
            initController();
            mainApp.setRootLayout((BorderPane) pane);
            // Show the scene containing the root layout.
            Scene scene = new Scene(mainApp.getRootLayout());
            mainApp.getPrimaryStage().setScene(scene);
            // Give the controller access to the main app.
            mainApp.getPrimaryStage().show();
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("loadFXMLStage", e);
        }
    }

    public Stage getDialog(String title, String icon) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
            dialogStage.getIcons().add(new Image(icon));
            // Show the dialog and wait until the user closes it
            Scene scene = new Scene(this.pane);
            dialogStage.setScene(scene);
            //dialogStage.showAndWait();
            return dialogStage;
        } catch (Exception e) {
            log.error("createDialog", e);
            return null;
        }
    }




    public static void exit() {
        System.exit(0);
    }

    public static void showMessage(AlertType alertType, String header, String txt) {
        JavaFXLoader.showMessage(alertType, header, txt, null);
    }

    public static void showMessage(AlertType alertType, String header, String txt, Stage stage) {
        Alert alert = new Alert(alertType);
        String title;
        switch (alertType) {
            case NONE:
                title = "";
                break;
            case INFORMATION:
                title = "Información";
                break;
            case WARNING:
                title = "Cuidado!";
                break;
            case CONFIRMATION:
                title = "Operación finalizada correctamente";
                break;
            case ERROR:
                title = "Error";
                break;
            default:
                title = "Mensaje de sistema";
                break;
        }
        if (stage != null) {
            alert.initOwner(stage);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(txt);
        alert.showAndWait();
    }
}
