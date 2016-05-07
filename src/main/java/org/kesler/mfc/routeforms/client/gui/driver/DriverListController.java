package org.kesler.mfc.routeforms.client.gui.driver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.domain.Driver;
import org.kesler.mfc.routeforms.client.gui.AbstractListController;
import org.kesler.mfc.routeforms.client.security.LoginHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Контроллер для управления списком типов водителей
 */
@Component
public class DriverListController extends AbstractListController<Driver> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected ListView<Driver> driverListView;
    @FXML protected ToolBar buttonsToolBar;
    private ObservableList<Driver> observableDrivers;

    @Autowired private RouteFormsService routeFormsService;
    @Autowired private DriverController driverController;
    @Autowired protected LoginHolder loginHolder;


    protected Branch branch;

    @FXML
    protected void initialize() {
        driverListView.setCellFactory(new Callback<ListView<Driver>, ListCell<Driver>>() {
            @Override
            public ListCell<Driver> call(ListView<Driver> param) {
                return new DriverListCell();
            }
        });
        observableDrivers = FXCollections.observableArrayList();
        driverListView.setItems(observableDrivers);
    }

    @Override
    public void show(Window owner) {
        log.info("Show dialog");
        this.branch = null;
        String title = "Водители";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/pilot2.png"));
        super.show(owner, title, icon);
    }

    @Override
    public void showAndWait(Window owner) {
        String title = "Водители";
        this.branch = null;
        Image icon = new Image(this.getClass().getResourceAsStream("/images/pilot2.png"));
        super.showAndWait(owner, title, icon);
    }

    @Override
    public void showAndWaitSelect(Window owner) {
        log.info("Show select dialog");
        this.branch = null;
        String title = "Выберите водителя";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/pilot2.png"));
        super.showAndWaitSelect(owner, title, icon);
    }


    public void showAndWaitSelect(Window owner, Branch branch) {
        log.info("Show select dialog");
        this.branch = branch;
        String title = "Выберите водителя";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/pilot2.png"));
        super.showAndWaitSelect(owner, title, icon);
    }

    @Override
    protected void updateContent() {
        updateItemsAndSelect(null);
    }

    @Override
    protected void afterUpdatingContent() {
        if (!loginHolder.isAdmin()) {
            buttonsToolBar.setDisable(true);
        } else {
            buttonsToolBar.setDisable(false);
        }

    }

    @Override
    protected void handleOk() {
        selectedItem = driverListView.getSelectionModel().getSelectedItem();

        if (select && selectedItem == null) {
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Ничего не выбрано")
                    .showWarning();
        } else {
            result = Result.OK;
            stage.hide();
        }

    }

    @FXML protected void handleAddDriverButtonAction(ActionEvent ev) {
        addDriver();
    }

    @FXML protected void handleEditDriverButtonAction(ActionEvent ev) {
        editDriver();
    }

    @FXML protected void handleDriverListViewMouseClick(MouseEvent ev) {
        if (ev.getClickCount()==2) {
            if (select) {
                handleOk();
            } else {
                editDriver();
            }
        }

    }

    @FXML protected void handleRemoveDriverButtonAction(ActionEvent ev) {
        removeDriver();
    }


    protected void checkSoleDriverAndSelect() {
        if (select && observableDrivers.size() == 1) {
            selectedItem = observableDrivers.get(0);
            driverListView.getSelectionModel().select(selectedItem);
//            handleOk();
        }
    }

    private void addDriver() {
        log.info("Add new Driver");
        Driver driver = new Driver();
        driver.setFioShort("");
        driverController.showAndWait(stage, driver);
        updateItemsAndSelect(driver);
     }

    private void editDriver() {
        Driver selectedDriver = driverListView.getSelectionModel().getSelectedItem();
        if (selectedDriver!=null) {
            log.info("Edit Driver: " + selectedDriver.getFioShort());
            driverController.showAndWait(stage,selectedDriver);
            updateItemsAndSelect(selectedDriver);

        }
    }

    private void removeDriver() {
        log.info("Remove Driver");
        Driver selectedDriver = driverListView.getSelectionModel().getSelectedItem();
        if (selectedDriver!=null) {
            log.info("Removing driver: " + selectedDriver.getFioShort());
            removeItem(selectedDriver);
        }
    }


    @Override
    protected Collection<Driver> updateListAsync() {
        if (branch==null) return routeFormsService.findDrivers();
        else return routeFormsService.findActiveDriversByBranch(branch);
    }

    @Override
    protected void updatingListComplete(Collection<Driver> items, Driver item) {
        log.debug("Update observableDrivers ...");
        observableDrivers.clear();
        observableDrivers.addAll(items);

        driverListView.getSelectionModel().select(item);

        if (select) checkSoleDriverAndSelect();
    }

    @Override
    protected void removeItemAsync(Driver item) {
        routeFormsService.removeDriver(item);
    }

    // Вспомогательные классы для списка сотрудников
    class DriverListCell extends ListCell<Driver> {

        @Override
        public void updateItem(Driver item, boolean empty) {
            super.updateItem(item, empty);
            setText(item==null?null:item.getFioShort());
        }
    }




}


