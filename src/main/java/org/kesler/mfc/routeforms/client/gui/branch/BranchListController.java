package org.kesler.mfc.routeforms.client.gui.branch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.gui.AbstractListController;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Контроллер для управления списком типов картриджей
 */
@Component
public class BranchListController extends AbstractListController<Branch> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected ListView<Branch> branchListView;
    @FXML protected ProgressIndicator updateProgressIndicator;
    private ObservableList<Branch> observableBranches;

    @Autowired private RouteFormsService routeFormsService;
    @Autowired private BranchController branchController;

    @FXML
    protected void initialize() {
        branchListView.setCellFactory(new Callback<ListView<Branch>, ListCell<Branch>>() {
            @Override
            public ListCell<Branch> call(ListView<Branch> param) {
                return new BranchListCell();
            }
        });
        observableBranches = FXCollections.observableArrayList();
        branchListView.setItems(observableBranches);
    }

    @Override
    public void show(Window owner) {
        log.info("Show dialog");
        String title = "Филиалы";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/office-building.png"));
        super.show(owner, title, icon);
    }

    @Override
    public void showAndWait(Window owner) {
        String title = "Филиалы";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/office-building.png"));
        super.showAndWait(owner, title, icon);
    }

    @Override
    public void showAndWaitSelect(Window owner) {
        log.info("Show select dialog");
        String title = "Выберите филиал";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/office-building.png"));
        super.showAndWaitSelect(owner, title, icon);
    }

    @Override
    protected void updateContent() {
        updateItemsAndSelect(null);
    }

    @Override
    protected void handleOk() {
        selectedItem = branchListView.getSelectionModel().getSelectedItem();

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

    @FXML protected void handleAddBranchButtonAction(ActionEvent ev) {
        addBranch();
    }

    @FXML protected void handleEditBranchButtonAction(ActionEvent ev) {
        editBranch();
    }

    @FXML protected void handleBranchListViewMouseClick(MouseEvent ev) {
        if (ev.getClickCount()==2) {
            if (select) {
                handleOk();
            } else {
                editBranch();
            }
        }

    }

    @FXML protected void handleRemoveBranchButtonAction(ActionEvent ev) {
        removeBranch();
    }


    private void addBranch() {
        log.info("Add new Branch");
        Branch branch = new Branch();
        branch.setName("");
        branchController.showAndWait(stage, branch);
        updateItemsAndSelect(branch);
    }

    private void editBranch() {
        log.info("Edit Branch");
        Branch selectedBranch = branchListView.getSelectionModel().getSelectedItem();
        if (selectedBranch!=null) {
            branchController.showAndWait(stage,selectedBranch);
            updateItemsAndSelect(selectedBranch);
        }
    }

    private void removeBranch() {
        log.info("Remove Branch");
        Branch selectedBranch = branchListView.getSelectionModel().getSelectedItem();
        if (selectedBranch!=null) {
            log.info("Removing branch: " + selectedBranch.getName());
            removeItem(selectedBranch);
        }
    }

    @Override
    protected Collection<Branch> updateListAsync() {
        log.debug("Updating branches list...");

        Collection<Branch> branches = routeFormsService.findBranches();
        return branches;
    }

    @Override
    protected void updatingListComplete(Collection<Branch> items, Branch item) {
        log.debug("Update observableBranches ...");
        observableBranches.clear();
        observableBranches.addAll(items);

        branchListView.getSelectionModel().select(item);
    }

    @Override
    protected void removeItemAsync(Branch item) {
        routeFormsService.removeBranch(item);
    }

    // Вспомогательные классы для списка сотрудников
    class BranchListCell extends ListCell<Branch> {

        @Override
        public void updateItem(Branch item, boolean empty) {
            super.updateItem(item, empty);
            setText(item==null?null:item.getName());
        }
    }





}


