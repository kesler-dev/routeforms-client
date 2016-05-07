package org.kesler.mfc.routeforms.client.gui.norm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.domain.Norm;
import org.kesler.mfc.routeforms.client.gui.AbstractListController;
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
public class NormListController extends AbstractListController<Norm> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected ListView<Norm> normListView;
    private ObservableList<Norm> observableNorms;

    @Autowired
    private RouteFormsService routeFormsService;

    @Autowired
    private NormController normController;

    @FXML
    protected void initialize() {
        normListView.setCellFactory(new Callback<ListView<Norm>, ListCell<Norm>>() {
            @Override
            public ListCell<Norm> call(ListView<Norm> param) {
                return new NormListCell();
            }
        });
        observableNorms = FXCollections.observableArrayList();
        normListView.setItems(observableNorms);
    }

    @Override
    public void show(Window owner) {
        log.info("Show dialog");
        String title = "Нормы";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/document_edit.png"));
        super.show(owner, title, icon);
    }

    @Override
    public void showAndWait(Window owner) {
        String title = "Нормы";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/document_edit.png"));
        super.showAndWait(owner, title, icon);
    }

    @Override
    public void showAndWaitSelect(Window owner) {
        log.info("Show select dialog");
        String title = "Выберите норму";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/document_edit.png"));
        super.showAndWaitSelect(owner, title, icon);
    }

    @Override
    protected void updateContent() {
        updateItemsAndSelect(null);
    }

    @Override
    protected void handleOk() {
        selectedItem = normListView.getSelectionModel().getSelectedItem();

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

    @FXML protected void handleAddNormButtonAction(ActionEvent ev) {
        addNorm();
    }

    @FXML protected void handleEditNormButtonAction(ActionEvent ev) {
        editNorm();
    }

    @FXML protected void handleNormListViewMouseClick(MouseEvent ev) {
        if (ev.getClickCount()==2) {
            if (select) {
                handleOk();
            } else {
                editNorm();
            }
        }

    }

    @FXML protected void handleRemoveNormButtonAction(ActionEvent ev) {
        removeNorm();
    }


    private void addNorm() {
        log.info("Add new Norm");
        Norm norm = new Norm();
        normController.showAndWait(stage, norm);
        updateItemsAndSelect(norm);
     }

    private void editNorm() {
        Norm selectedNorm = normListView.getSelectionModel().getSelectedItem();
        if (selectedNorm!=null) {
            log.info("Edit Norm: " + selectedNorm.toString());
            normController.showAndWait(stage,selectedNorm);
            updateItemsAndSelect(selectedNorm);

        }
    }

    private void removeNorm() {
        log.info("Remove Norm");
        Norm selectedNorm = normListView.getSelectionModel().getSelectedItem();
        if (selectedNorm!=null) {
            log.info("Removing norm: " + selectedNorm.toString());
            removeItem(selectedNorm);
        }
    }


    @Override
    protected Collection<Norm> updateListAsync() {
        return routeFormsService.findNorms();
    }

    @Override
    protected void updatingListComplete(Collection<Norm> items, Norm item) {
        log.debug("Update observableNorms ...");
        observableNorms.clear();
        observableNorms.addAll(items);

        normListView.getSelectionModel().select(item);
    }

    @Override
    protected void removeItemAsync(Norm item) {
        routeFormsService.removeNorm(item);
    }

    // Вспомогательные классы для списка сотрудников
    class NormListCell extends ListCell<Norm> {

        @Override
        public void updateItem(Norm item, boolean empty) {
            super.updateItem(item, empty);
            setText(item==null?null:item.toString());
        }
    }




}


