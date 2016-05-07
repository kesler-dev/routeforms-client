package org.kesler.mfc.routeforms.client.gui.employee;

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
import org.kesler.mfc.routeforms.client.domain.Employee;
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
public class EmployeeListController extends AbstractListController<Employee> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected ListView<Employee> employeeListView;
    private ObservableList<Employee> observableEmployees;

    @Autowired
    private RouteFormsService routeFormsService;

    @Autowired
    private EmployeeController employeeController;

    @FXML
    protected void initialize() {
        employeeListView.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>() {
            @Override
            public ListCell<Employee> call(ListView<Employee> param) {
                return new EmployeeListCell();
            }
        });
        observableEmployees = FXCollections.observableArrayList();
        employeeListView.setItems(observableEmployees);
    }

    @Override
    public void show(Window owner) {
        log.info("Show dialog");
        String title = "Сотрудники";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/id_cards.png"));
        super.show(owner, title, icon);
    }

    @Override
    public void showAndWait(Window owner) {
        String title = "Сотрудники";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/id_cards.png"));
        super.showAndWait(owner, title, icon);
    }

    @Override
    public void showAndWaitSelect(Window owner) {
        log.info("Show select dialog");
        String title = "Выберите сотрудника";
        Image icon = new Image(this.getClass().getResourceAsStream("/images/id_cards.png"));
        super.showAndWaitSelect(owner, title, icon);
    }

    @Override
    protected void updateContent() {
        updateItemsAndSelect(null);
    }

    @Override
    protected void handleOk() {
        selectedItem = employeeListView.getSelectionModel().getSelectedItem();

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

    @FXML protected void handleAddEmployeeButtonAction(ActionEvent ev) {
        addEmployee();
    }

    @FXML protected void handleEditEmployeeButtonAction(ActionEvent ev) {
        editEmployee();
    }

    @FXML protected void handleEmployeeListViewMouseClick(MouseEvent ev) {
        if (ev.getClickCount()==2) {
            if (select) {
                handleOk();
            } else {
                editEmployee();
            }
        }

    }

    @FXML protected void handleRemoveEmployeeButtonAction(ActionEvent ev) {
        removeEmployee();
    }


    private void addEmployee() {
        log.info("Add new Employee");
        Employee employee = new Employee();
        employee.setFio("");
        employee.setActive(true);
        employeeController.showAndWait(stage, employee);
        updateItemsAndSelect(employee);

    }

    private void editEmployee() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        if (selectedEmployee!=null) {
            log.info("Edit Employee: " + selectedEmployee.getFio());
            employeeController.showAndWait(stage,selectedEmployee);
            updateItemsAndSelect(selectedEmployee);
        }
    }

    private void removeEmployee() {
        log.info("Remove Employee");
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        if (selectedEmployee!=null) {
            log.info("Removing employee: " + selectedEmployee.getFio());
            removeItem(selectedEmployee);
        }
    }


    @Override
    protected Collection<Employee> updateListAsync() {
        return routeFormsService.findEmployees();
    }

    @Override
    protected void updatingListComplete(Collection<Employee> items, Employee item) {
        log.debug("Update observableEmployees ...");
        observableEmployees.clear();
        observableEmployees.addAll(items);

        employeeListView.getSelectionModel().select(item);
    }

    @Override
    protected void removeItemAsync(Employee item) {
        routeFormsService.removeEmployee(item);
    }

    // Вспомогательные классы для списка сотрудников
    class EmployeeListCell extends ListCell<Employee> {

        @Override
        public void updateItem(Employee item, boolean empty) {
            super.updateItem(item, empty);
            setText(item==null?null:item.getFio());
        }
    }



}


