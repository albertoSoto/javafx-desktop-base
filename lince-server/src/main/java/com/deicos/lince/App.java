/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deicos.lince;


import com.deicos.lince.data.Person;
import com.deicos.lince.view.BirthdayStatisticsController;
import com.deicos.lince.view.PersonEditDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import java.io.File;


/**
 * @author Thomas Darimont
 */
@Lazy
@SpringBootApplication
public class App extends AbstractJavaFxApplicationSupport {

    /**
     * Note that this is configured in application.properties
     */
    @Value("${app.ui.title:Example App}")//
    private String windowTitle;

    private LinceDataHelper dataHelper = LinceDataHelper.getInstance();

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    /**
     * Constructor
     */
    public App() {
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
    }

    public static void main(String[] args) {
        //launch(args);
        launchApp(App.class, args); // para salvar los parametros
    }

    @Override
    public void initRootLayout() {
        JavaFXLoader loader = new JavaFXLoader("view/RootLayout.fxml", this);
        loader.loadFXMLStage();
        rootLayout = (BorderPane) loader.getPane();
        // Try to load last opened person file.
        File file = dataHelper.getPersonFilePath();
        if (file != null) {
            dataHelper.loadPersonDataFromFile(file, getPrimaryStage());
        }
        JavaFXLoader personPaneLoader = new JavaFXLoader("view/PersonOverview.fxml", this);
        personPaneLoader.initController();
        Pane personOverview = personPaneLoader.getPane();
        rootLayout.setCenter(personOverview);
    }

    @Override
    public String getWindowTitle() {
        return windowTitle;
    }

    @Override
    public String getWindowIcon() {
        return "file:resources/images/address_book_32.png";
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            JavaFXLoader fxLoader = new JavaFXLoader<PersonEditDialogController>("view/PersonEditDialog.fxml",this);
            // Create the dialog Stage & Set the dialog icon.
            Stage dialogStage = fxLoader.getDialog("Edit Person" , "file:resources/images/edit.png" );
            // Set the person into the controller.
            PersonEditDialogController controller = (PersonEditDialogController)fxLoader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (Exception e) {
            log.error(getClass().getEnclosingMethod().getName(), e);
            return false;
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            JavaFXLoader fxLoader = new JavaFXLoader<BirthdayStatisticsController>("view/BirthdayStatistics.fxml",this);
            Stage dialogStage = fxLoader.getDialog("Birthday Statistics" , "file:resources/images/calendar.png" );
            // Set the persons into the controller.
            BirthdayStatisticsController controller = (BirthdayStatisticsController)fxLoader.getController();
            controller.setPersonData(personData);
            // Set the dialog icon.
            dialogStage.show();
        } catch (Exception e) {
            log.error(getClass().getEnclosingMethod().getName(), e);
        }
    }
}
