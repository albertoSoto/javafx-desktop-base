package com.deicos.lince;

import com.deicos.lince.data.Person;
import com.deicos.lince.data.PersonListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * lince-scientific-base
 * com.deicos.lince
 * Created by alber in 24/02/2017.
 * Description:
 */
public class LinceDataHelper {
    private static LinceDataHelper instance = null;

    protected LinceDataHelper() {
        // Exists only to defeat instantiation.
    }

    public static LinceDataHelper getInstance() {
        if (instance == null) {
            instance = new LinceDataHelper();
        }
        return instance;
    }

    private static ObservableList<Person> personData = FXCollections.observableArrayList();

    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file, Stage primaryStage) {
        Preferences prefs = Preferences.userNodeForPackage(App.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
            // Update the stage title.
            primaryStage.setTitle("LinceApp - " + file.getName());
        } else {
            prefs.remove("filePath");
            // Update the stage title.
            primaryStage.setTitle("LinceApp");
        }
    }

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadPersonDataFromFile(File file, Stage primaryStage) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            // Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);
            personData.clear();
            personData.addAll(wrapper.getPersons());
            // Save the file path to the registry.
            setPersonFilePath(file, primaryStage);
        } catch (Exception e) { // catches ANY exception
            JavaFXLoader.showMessage(Alert.AlertType.ERROR
                    , "Could not load data"
                    , "Could not load data from file:\n" + file.getPath());
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void savePersonDataToFile(File file, Stage primaryStage) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Wrapping our person data.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);
            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);
            // Save the file path to the registry.
            setPersonFilePath(file, primaryStage);
        } catch (Exception e) { // catches ANY exception
            JavaFXLoader.showMessage(Alert.AlertType.ERROR
                    , "Could not save data"
                    , "Could not save data to file:\n" + file.getPath());
        }
    }
}
