package org.kesler.mfc.routeforms.client.gui.stat;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.RouteForm;
import org.kesler.mfc.routeforms.client.gui.AbstractController;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by alex on 29.10.15.
 */
@Component
public class DayStatController extends AbstractController implements Initializable{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected DatePicker datePicker;
    @FXML protected TableView<AutoRouteForm> autoRouteFormTableView;
    @FXML protected HBox updatingHBox;

    protected final ObservableList<AutoRouteForm> observableAutoRouteForms = FXCollections.observableArrayList();

    @Autowired
    protected RouteFormsService routeFormsService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        autoRouteFormTableView.setItems(observableAutoRouteForms);
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (newValue != null) {
                    update(newValue);
                }
            }
        });
    }

    public void show(Window owner) {
        Image icon = new Image(this.getClass().getResourceAsStream("/images/date_next.png"));
        super.show(owner,"Отчет по путевым листам на дату", icon);
    }

    @Override
    protected void updateContent() {
        datePicker.setValue(LocalDate.now());
    }

    private void update(LocalDate date) {
        if (date==null) return;
        Updater updater = new Updater(date);
        BooleanBinding runnning = updater.stateProperty().isEqualTo(Task.State.RUNNING);

        updatingHBox.visibleProperty().bind(runnning);
        new Thread(updater).start();
    }

    class Updater extends Task<Collection<AutoRouteForm>> {

        protected LocalDate date;

        public Updater(LocalDate date) {
            this.date = date;
        }

        @Override
        protected Collection<AutoRouteForm> call() throws Exception {
            List<AutoRouteForm> autoRouteForms = new ArrayList<>();
            Collection<Auto> autos = routeFormsService.findAutos();
            Collection<RouteForm> routeForms = routeFormsService.findRouteFormsByDates(date, date);

            for (Auto auto : autos) {
                RouteForm routeForm = null;
                for (RouteForm rf : routeForms) {
                    if (rf.getAuto().equals(auto)) {
                        routeForm = rf;
                        break;
                    }
                }
                AutoRouteForm autoRouteForm = new AutoRouteForm(auto,routeForm);
                autoRouteForms.add(autoRouteForm);
            }

            return autoRouteForms;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            observableAutoRouteForms.clear();
            observableAutoRouteForms.addAll(getValue());
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            log.error("Error reading RouteForms: " + ex, ex);
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Ошибка чтения данных:" + ex)
                    .showException(ex);
        }
    }


}
