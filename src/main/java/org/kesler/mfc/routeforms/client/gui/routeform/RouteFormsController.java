package org.kesler.mfc.routeforms.client.gui.routeform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.domain.Driver;
import org.kesler.mfc.routeforms.client.domain.RouteForm;
import org.kesler.mfc.routeforms.client.gui.AbstractListController;
import org.kesler.mfc.routeforms.client.gui.driver.DriverListController;
import org.kesler.mfc.routeforms.client.gui.report.BranchReportController;
import org.kesler.mfc.routeforms.client.security.OptionsHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Контроллер управления списком путевых листов
 */
@Component
public class RouteFormsController extends AbstractListController<RouteForm> implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @FXML protected TableView<RouteForm> routeFormTableView;

    protected Auto auto;

    protected final ObservableList<RouteForm> observableRouteForms = FXCollections.observableArrayList();


    @Autowired protected RouteFormsService routeFormsService;
    @Autowired protected RouteFormController routeFormController;
    @Autowired protected BranchReportController branchReportController;
    @Autowired protected DriverListController driverListController;
    @Autowired protected OptionsHolder optionsHolder;


    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        routeFormTableView.setItems(observableRouteForms);

    }

    public void showAndWait(Window owner, Auto auto) {
        this.auto = auto;
        Image icon = new Image(this.getClass().getResourceAsStream("/images/form_green.png"));
        super.showAndWait(owner,"Путевые листы", icon);
    }


    @FXML protected void handleRouteFormTableViewMouseClick(MouseEvent ev) {
        if (ev.getClickCount()==2)
            editRouteForm();
    }

    @FXML protected void handleAddButtonAction(ActionEvent ev) {
        addRouteForm();
    }

    @FXML protected void handleEditButtonAction(ActionEvent ev) {
        editRouteForm();
    }

    @FXML protected void handleRemoveButtonAction(ActionEvent ev) {
        removeRouteForm();
    }

    @FXML protected void handleReportButtonAction(ActionEvent ev) {
        makeReport();
    }


    private void addRouteForm() {
        RouteForm newRouteForm = null;
        if (observableRouteForms.size() > 0) {
            RouteForm lastRouteForm = observableRouteForms.get(0);
            if (!lastRouteForm.getState().equals(RouteForm.State.READY)) {
                Dialogs.create()
                        .owner(stage)
                        .title("Внимание")
                        .message("Необходимо заполнить последний путевой лист.")
                        .showWarning();
                return;
            }
            newRouteForm = RouteForm.createFromPrevious(lastRouteForm);
        } else {
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Не обнаружено путевых листов. Заполните первый в цепочке")
                    .showInformation();
            newRouteForm = new RouteForm();
            newRouteForm.setAuto(auto);
            newRouteForm.setDate(LocalDate.now());
        }

        newRouteForm.setSeries(auto.getBranch().getSeries());
        newRouteForm.setSeasonType(routeFormsService.getSeasonForDate(newRouteForm.getDate()));
        newRouteForm.setOrgName(optionsHolder.getOptions().getOrgName());

        Integer counter = auto.getBranch().getRouteFormCounter();
        if (counter==null) counter=0;
        counter++;
//        auto.getBranch().setRouteFormCounter(counter);
        newRouteForm.setIntNumber(counter);


        routeFormController.showAndWaitNew(stage, newRouteForm);
        updateItemsAndSelect(newRouteForm);
    }

    private void editRouteForm() {
        RouteForm selectedRouteForm = routeFormTableView.getSelectionModel().getSelectedItem();
        if (selectedRouteForm != null) {
            RouteForm lastRouteForm = observableRouteForms.get(0);
            if (selectedRouteForm.equals(lastRouteForm))
                routeFormController.showAndWaitLast(stage, selectedRouteForm);
            else
                routeFormController.showAndWait(stage, selectedRouteForm);
            updateItemsAndSelect(selectedRouteForm);
        }

    }

    private void removeRouteForm() {
        if (observableRouteForms.size() > 0) {
            RouteForm lastRouteForm = observableRouteForms.get(0);
            Action response = Dialogs.create()
                    .title("Внимание")
                    .message("Удалить последний путевой лист: " + lastRouteForm.getSeriesAndNumber() + "?")
                    .actions(Dialog.ACTION_YES, Dialog.ACTION_CANCEL)
                    .showConfirm();
            if (response == Dialog.ACTION_YES) {
                removeItem(lastRouteForm);
                updateItemsAndSelect(null);
            }
        } else {
            Notifications.create()
                    .text("Ничего нет - не удалить ничего")
                    .hideAfter(Duration.millis(1700))
                    .showInformation();
        }

    }


    private void makeReport() {
        Driver driver = null;
        Branch branch = auto.getBranch();

        if (branch != null) {
            driverListController.showAndWaitSelect(stage,branch);
            if (driverListController.getResult() == Result.OK) {
                driver = driverListController.getSelectedItem();
            }
        }

        if (auto != null && driver != null) {
            branchReportController.showAndWait(stage, auto, driver);
        }

    }



    @Override
    protected Collection<RouteForm> updateListAsync() {
        if (auto == null) {
            return routeFormsService.findRouteForms();
        } else {
            return routeFormsService.findRouteFormsByAuto(auto);
        }
    }

    @Override
    protected void updatingListComplete(Collection<RouteForm> items, RouteForm item) {
        observableRouteForms.clear();
        observableRouteForms.addAll(items);
        observableRouteForms.sort(new RouteFormsDateDescComparator());
//        observableRouteForms.sort(new RouteFormsPrevDescComparator());

        routeFormTableView.getSelectionModel().select(item);
    }

    @Override
    protected void removeItemAsync(RouteForm item) {
        routeFormsService.removeRouteForm(item);
    }
}
