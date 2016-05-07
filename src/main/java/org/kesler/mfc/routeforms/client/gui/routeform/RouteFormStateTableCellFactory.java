package org.kesler.mfc.routeforms.client.gui.routeform;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.kesler.mfc.routeforms.client.domain.RouteForm;


/**
 * Created by alex on 07.06.15.
 */
public class RouteFormStateTableCellFactory implements Callback<TableColumn<Object,RouteForm.State>,TableCell<Object,RouteForm.State>> {
    @Override
    public TableCell<Object, RouteForm.State> call(TableColumn<Object, RouteForm.State> param) {
        return new TableCell<Object,RouteForm.State>() {
            @Override
            protected void updateItem(RouteForm.State item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) {
                    setText("");
                    return;
                }

                setText(item.toString());
                setTextFill(item.getColor());
            }
        };
    }
}
