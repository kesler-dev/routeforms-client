package org.kesler.mfc.routeforms.client.util;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by alex on 25.06.15.
 */
public class LongTextFieldChangeListener implements ChangeListener<String> {
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null && newValue.length() > 0) {
            if (!newValue.matches("^[0-9]+")) {
                ((StringProperty) observable).setValue(oldValue);
            }
        }
    }
}
