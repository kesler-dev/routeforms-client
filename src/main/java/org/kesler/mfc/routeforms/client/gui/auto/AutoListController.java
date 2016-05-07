package org.kesler.mfc.routeforms.client.gui.auto;

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
import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.gui.AbstractListController;
import org.kesler.mfc.routeforms.client.security.LoginHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Контроллер для управления списком типов автомобилей
 */
@Component
public class AutoListController extends AbstractListController<Auto> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected ListView<Auto> autoListView;
    @FXML protected ToolBar buttonsToolBar;

    private ObservableList<Auto> observableAutos;

    @Autowired private RouteFormsService routeFormsService;
    @Autowired private AutoController autoController;

    @Autowired protected LoginHolder loginHolder;

    protected Branch branch;

    @FXML
    protected void initialize() {
        autoListView.setCellFactory(new Callback<ListView<Auto>, ListCell<Auto>>() {
            @Override
            public ListCell<Auto> call(ListView<Auto> param) {
                return new AutoListCell();
            }
        });
        observableAutos = FXCollections.observableArrayList();

        autoListView.setItems(observableAutos);
    }

    @Override
    public void show(Window owner) {
        log.info("Show dialog");
        this.branch = null;
        String title = "Автомобили";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/car_sedan_green.png"));
        super.show(owner, title, icon);
    }

    @Override
    public void showAndWait(Window owner) {
        String title = "Автомобили";
        this.branch = null;
        Image icon = new Image(this.getClass().getResourceAsStream("/images/car_sedan_green.png"));
        super.showAndWait(owner, title, icon);
    }

    @Override
    public void showAndWaitSelect(Window owner) {
        log.info("Show select dialog");
        this.branch = null;
        String title = "Выберите Автомобиль";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/car_sedan_green.png"));
        super.showAndWaitSelect(owner, title, icon);
    }


    public void showAndWaitSelect(Window owner, Branch branch) {
        log.info("Show select dialog for branch: " + branch);
        this.branch = branch;
        String title = "Выберите Автомобиль";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/car_sedan_green.png"));
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
        selectedItem = autoListView.getSelectionModel().getSelectedItem();

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

    @FXML protected void handleAddAutoButtonAction(ActionEvent ev) {
        addAuto();
    }

    @FXML protected void handleEditAutoButtonAction(ActionEvent ev) {
        editAuto();
    }

    @FXML protected void handleAutoListViewMouseClick(MouseEvent ev) {
        if (ev.getClickCount()==2) {
            if (select) {
                handleOk();
            } else {
                editAuto();
            }
        }

    }

    @FXML protected void handleRemoveAutoButtonAction(ActionEvent ev) {
        removeAuto();
    }

    protected void checkSoleAutoAndSelect() {
        if (select && observableAutos.size() == 1) {
            selectedItem = observableAutos.get(0);
            autoListView.getSelectionModel().select(selectedItem);
            handleOk();
        }
    }

    private void addAuto() {
        log.info("Add new Auto");
        Auto auto = new Auto();
        auto.setModel("");
        autoController.showAndWait(stage, auto);
        updateItemsAndSelect(auto);
     }

    private void editAuto() {
        Auto selectedAuto = autoListView.getSelectionModel().getSelectedItem();
        if (selectedAuto!=null) {
            log.info("Edit Auto: " + selectedAuto.toString());
            autoController.showAndWait(stage,selectedAuto);
            updateItemsAndSelect(selectedAuto);

        }
    }

    private void removeAuto() {
        log.info("Remove Auto");
        Auto selectedAuto = autoListView.getSelectionModel().getSelectedItem();
        if (selectedAuto!=null) {
            log.info("Removing auto: " + selectedAuto.toString());
            removeItem(selectedAuto);
        }
    }


    @Override
    protected Collection<Auto> updateListAsync() {
        if (branch != null) {
            return routeFormsService.findAutosByBranch(branch);
        } else {
            return routeFormsService.findAutos();
        }
    }

    @Override
    protected void updatingListComplete(Collection<Auto> items, Auto item) {
        log.debug("Update observableAutos ...");
        observableAutos.clear();
        observableAutos.addAll(items);

        autoListView.getSelectionModel().select(item);

        if (select) checkSoleAutoAndSelect();
    }

    @Override
    protected void removeItemAsync(Auto item) {
        routeFormsService.removeAuto(item);
    }

    // Вспомогательные классы для списка сотрудников
    class AutoListCell extends ListCell<Auto> {

        @Override
        public void updateItem(Auto item, boolean empty) {
            super.updateItem(item, empty);
            setText(item==null?null:item.toString());
        }
    }




}


