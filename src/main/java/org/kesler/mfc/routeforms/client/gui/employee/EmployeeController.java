package org.kesler.mfc.routeforms.client.gui.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Window;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.domain.Employee;
import org.kesler.mfc.routeforms.client.gui.AbstractItemController;
import org.kesler.mfc.routeforms.client.gui.branch.BranchListController;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер редактирования Сотрудника
 */
@Component
public class EmployeeController extends AbstractItemController implements Initializable{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected TextField nameTextField;
    @FXML protected TextField branchTextField;
    @FXML protected TextField passwordTextField;
    @FXML protected CheckBox adminCheckBox;
    @FXML protected CheckBox activeCheckBox;

    private Employee employee;

    private Branch branch;

    @Autowired protected RouteFormsService routeFormsService;
    @Autowired protected BranchListController branchListController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTextField.textProperty().addListener(invalidationListener);
        passwordTextField.textProperty().addListener(invalidationListener);
        adminCheckBox.selectedProperty().addListener(invalidationListener);
        activeCheckBox.selectedProperty().addListener(invalidationListener);
    }

    public void showAndWait(Window owner, Employee employee) {
        this.employee = employee;
        nameTextField.requestFocus();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/id_cards.png"));
        super.showAndWait(owner, "Сотрудник", icon);
    }

    @Override
    protected void updateContent() {
        nameTextField.setText(employee.getFio());
        passwordTextField.setText(employee.getPassword());
        adminCheckBox.setSelected(employee.isAdmin()==null?false:employee.isAdmin());
        activeCheckBox.setSelected(employee.isActive()==null?false:employee.isActive());
        this.branch = employee.getBranch();
        branchTextField.setText(branch==null?"":branch.toString());
    }

    @Override
    protected void afterUpdatingContent() {
        if (employee.getId() == null) {
            setButtonSet(ButtonSet.SAVE_CANCEL);
        } else {
            setButtonSet(ButtonSet.CLOSE);
        }
    }


    @FXML protected void handleSelectBranchButtonAction(ActionEvent ev) {
        log.info("Handle select Branch action");
        selectBranch();
    }

    @Override
    protected void updateResult() {
        employee.setFio(nameTextField.getText());
        employee.setPassword(passwordTextField.getText());
        employee.setAdmin(adminCheckBox.isSelected());
        employee.setActive(activeCheckBox.isSelected());
        employee.setBranch(branch);
    }

    @Override
    protected void saveAsync() {
        log.info("Saving employee..");
        routeFormsService.saveEmployee(employee);
    }


    private void selectBranch() {
        branchListController.showAndWaitSelect(stage);
        if (branchListController.getResult() == Result.OK) {
            branch = branchListController.getSelectedItem();
            branchTextField.setText(branch==null?"":branch.toString());
            log.info("Selected branch: " + branch);
        }
    }

}
