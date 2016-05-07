package org.kesler.mfc.routeforms.client.gui.login;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.stage.Window;
import org.controlsfx.dialog.Dialogs;

import org.kesler.mfc.routeforms.client.domain.Employee;
import org.kesler.mfc.routeforms.client.security.OptionsHolder;
import org.kesler.mfc.routeforms.client.security.LoginHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.util.fx.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Контроллер окна входа в систему
 */
@Component
public class LoginController extends AbstractController implements Initializable{
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @FXML protected ComboBox<Employee> employeeComboBox;
    @FXML protected PasswordField passwordField;


    @Autowired
    protected RouteFormsService routeFormsService;

    @Autowired
    protected LoginHolder loginHolder;

    @Autowired
    protected OptionsHolder optionsHolder;

    private boolean loginOk;
    private ObservableList<Employee> observableEmployees = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SortedList<Employee> sortedEmployees = new SortedList<Employee>(observableEmployees, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getFio().compareTo(o2.getFio());
            }
        });
        employeeComboBox.setItems(sortedEmployees);
        employeeComboBox.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        passwordField.requestFocus();
                        passwordField.selectAll();
                    }
                });
            }
        });
    }


    public void showAndWait(Window owner, Collection<Employee> employees) {
        observableEmployees.clear();
        observableEmployees.addAll(employees);
        Image icon = new Image(this.getClass().getResourceAsStream("/images/id_card_ok.png"));
        super.showAndWait(owner, "Вход в систему", icon);
    }

    @Override
    protected void updateContent() {
        loginOk = false;

    }


    @Override
    protected void handleOk() {
        Employee employee = employeeComboBox.getValue();
        if (employee==null) return;

        String password = passwordField.getText();
        if (password.equals(employee.getPassword())) {
            log.info("Login ok.");
            loginOk = true;
            result = Result.OK;
            loginHolder.setCurrentEmployee(employee);
            optionsHolder.setOptions(routeFormsService.loadOptions());
            hideStage();
        } else {
            log.info("Login fail for: " + employee.getFio());
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Попытка входа неудачна")
                    .showWarning();
            result = Result.NONE;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    passwordField.requestFocus();
                    passwordField.selectAll();
                }
            });
        }

    }

    public boolean isLoginOk() { return loginOk; }
}
