package org.kesler.mfc.routeforms.client.util;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by alex on 25.06.15.
 */
public class TimeTextFieldChangeListener implements ChangeListener<String> {

    protected TextField textField;

    public TimeTextFieldChangeListener(TextField textField) {
        this.textField = textField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null && newValue.length() > 0) {
            if (!newValue.matches("^[0-2]{1}[0-9]?[:]?[0-5]?[0-9]?")) {
                ((StringProperty) observable).setValue(oldValue);
            }
            if (oldValue.length()==1 && newValue.length()==2) {
                ((StringProperty) observable).setValue(newValue+":");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textField.end();
                    }
                });
            }
        }
    }
}
