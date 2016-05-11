package org.kesler.mfc.routeforms.client.gui.main;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.domain.*;
import org.kesler.mfc.routeforms.client.gui.AbstractController;
import org.kesler.mfc.routeforms.client.gui.about.AboutController;
import org.kesler.mfc.routeforms.client.gui.auto.AutoListController;
import org.kesler.mfc.routeforms.client.gui.branch.BranchListController;
import org.kesler.mfc.routeforms.client.gui.driver.DriverListController;
import org.kesler.mfc.routeforms.client.gui.employee.EmployeeListController;
import org.kesler.mfc.routeforms.client.gui.login.LoginController;
import org.kesler.mfc.routeforms.client.gui.norm.NormListController;
import org.kesler.mfc.routeforms.client.gui.options.ApplicationOptionsController;
import org.kesler.mfc.routeforms.client.gui.options.ConnectOptionsController;
import org.kesler.mfc.routeforms.client.gui.report.BranchReportController;
import org.kesler.mfc.routeforms.client.gui.routeform.RouteFormController;
import org.kesler.mfc.routeforms.client.gui.routeform.RouteFormsController;
import org.kesler.mfc.routeforms.client.gui.routeform.RouteFormsCreatedDescComparator;
import org.kesler.mfc.routeforms.client.gui.stat.DayStatController;
import org.kesler.mfc.routeforms.client.security.LoginHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class MainController extends AbstractController implements Initializable{
    private final Logger log = LoggerFactory.getLogger(MainController.class);


    @Autowired
    protected LoginHolder loginHolder;

    @Autowired protected LoginController loginController;
    @Autowired protected EmployeeListController employeeListController;
    @Autowired protected BranchListController branchListController;
    @Autowired protected DriverListController driverListController;
    @Autowired protected AutoListController autoListController;
    @Autowired protected NormListController normListController;
    @Autowired protected RouteFormsController routeFormsController;
    @Autowired protected RouteFormController routeFormController;
    @Autowired protected DayStatController dayStatController;
    @Autowired protected BranchReportController branchReportController;
    @Autowired protected ConnectOptionsController connectOptionsController;
    @Autowired protected ApplicationOptionsController applicationOptionsController;
    @Autowired protected AboutController aboutController;


    @Autowired protected RouteFormsService routeFormsService;

    @FXML protected MenuItem loginMenuItem;
    @FXML protected MenuItem routeFormsMenuItem;
    @FXML protected MenuItem dayStatMenuItem;
    @FXML protected MenuItem branchReportMenuItem;

    @FXML protected MenuItem employeeMenuItem;
    @FXML protected MenuItem branchesMenuItem;
    @FXML protected MenuItem driversMenuItem;
    @FXML protected MenuItem autosMenuItem;
    @FXML protected MenuItem normsMenuItem;
    @FXML protected MenuItem applicationOptionsMenuItem;

    @FXML protected Label branchLabel;
    @FXML protected Label currentDateLabel;
    @FXML protected Button updateRouteFormsButton;
    @FXML protected ProgressIndicator updateProgressIndicator;
    @FXML protected TableView<RouteForm> routeFormsTableView;


    protected final ObservableList<RouteForm> observableRouteForms = FXCollections.observableArrayList();

    protected final int updateDaysCount = 30; // За сколько дней обновлять путевые листы на главной странице

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        routeFormsTableView.setItems(observableRouteForms.sorted(new RouteFormsCreatedDescComparator()));
    }

    @FXML
    protected void handleLoginMenuItem(ActionEvent ev) {
        log.info("Handle Login action");
        login();
    }

    @FXML
    protected void handleRouteFormsMenuItem(ActionEvent ev) {
        log.info("Handle RouteForms action");
        showRouteForms();
    }

    @FXML
    protected void handleDayStatMenuItem(ActionEvent ev) {
        log.info("Handle DayStat action");
        showDayStat();
    }

    @FXML
    protected void handleBranchReportMenuItem(ActionEvent ev) {
        log.info("Handle BranchReport action");
        showBranchReport();
    }

    @FXML
    protected void handleCloseMenuItem(ActionEvent ev) {
        log.info("Handle Close action");

        hideStage();
    }

    @FXML
    protected void handleEmployeeMenuItem(ActionEvent ev) {
        log.info("Handle Employee List action");
        employeeListController.showAndWait(stage);
    }

    @FXML
    protected void handleBranchesMenuItem(ActionEvent ev) {
        log.info("Handle Branches action");
        branchListController.show(stage);
    }

    @FXML
    protected void handleDriversMenuItem(ActionEvent ev) {
        log.info("Handle Drivers action");
        driverListController.show(stage);
    }

    @FXML
    protected void handleAutosMenuItem(ActionEvent ev) {
        log.info("Handle Autos action");
        autoListController.show(stage);
    }

    @FXML
    protected void handleNormsMenuItem(ActionEvent ev) {
        log.info("Handle Norms action");
        normListController.show(stage);
    }


    @FXML
    protected void handleConnectOptionsMenuItem(ActionEvent ev) {
        log.info("Handle ConnectOptions action");
        connectOptionsController.showAndWait(stage);
    }

    @FXML
    protected void handleApplicationOptionsMenuItem(ActionEvent ev) {
        log.info("Handle ApplicationOptions action");
        applicationOptionsController.showAndWait(stage);
    }


    @FXML
    protected void handleDocsMenuItem(ActionEvent ev) {
        log.info("Handle Docs action");
        openDocs();
    }

    @FXML
    protected void handleAboutMenuItem(ActionEvent ev) {
        log.info("Handle About action");
        aboutController.showAndWait(stage);
    }

    @FXML
    protected void handleUpdateButtonAction(ActionEvent ev) {
        log.info("Handle UpdateButton action");
        updateRouteForms();
    }

    @FXML
    protected void handleRouteFormsTableViewMouseClick(MouseEvent ev) {
        log.info("Handle RouteFormsClick action");
        if (ev.getClickCount() == 2) {
            openRouteForm();
        }

    }

    private void login() {
        Callback<Collection<Employee>,Void> callback = new Callback<Collection<Employee>, Void>() {
            @Override
            public Void call(Collection<Employee> param) {
                loginController.showAndWait(stage, param);
                if (loginController.isLoginOk()) {
                    Notifications.create()
                            .owner(stage)
                            .text("Добро пожаловать!")
                            .hideAfter(new Duration(1800))
                            .position(Pos.CENTER)
                            .hideCloseButton()
                            .showInformation();
                    updateSecurity();
                }
                return null;
            }
        };

        new Thread(new EmployeeReader(callback)).start();

    }


    private void showRouteForms() {
        Auto auto = null;
        Branch branch = null;
        if (loginHolder.isAdmin()) {
            branchListController.showAndWaitSelect(stage);
            if (branchListController.getResult() == Result.OK) {
                branch = branchListController.getSelectedItem();
            }
        } else {
            branch = loginHolder.getCurrentEmployee().getBranch();
        }

        if (branch != null) {
            autoListController.showAndWaitSelect(stage, branch);
            if (autoListController.getResult() == Result.OK) {
                auto = autoListController.getSelectedItem();
            }
        }

        if (auto != null) {
            routeFormsController.showAndWait(stage, auto);
            updateRouteForms();
        }

    }


    private void showDayStat() {
        dayStatController.show(stage);
    }

    private void showBranchReport() {
        Auto auto = null;
        Driver driver = null;
        Branch branch = null;
        if (loginHolder.isAdmin()) {
            branchListController.showAndWaitSelect(stage);
            if (branchListController.getResult() == Result.OK) {
                branch = branchListController.getSelectedItem();
            }
        } else {
            branch = loginHolder.getCurrentEmployee().getBranch();
        }

        if (branch != null) {
            autoListController.showAndWaitSelect(stage, branch);
            if (autoListController.getResult() == Result.OK) {
                auto = autoListController.getSelectedItem();
            }

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
    protected void updateContent() {
        updateSecurity();
        updateControls();
    }

    private void updateSecurity() {
        Employee currentEmployee = loginHolder.getCurrentEmployee();
        log.info("Updating security for Employee: " + currentEmployee);
        if (currentEmployee == null) {
            loginMenuItem.setDisable(false);
            routeFormsMenuItem.setDisable(true);
            dayStatMenuItem.setDisable(true);
            branchReportMenuItem.setDisable(true);
            employeeMenuItem.setDisable(true);
            branchesMenuItem.setDisable(true);
            driversMenuItem.setDisable(true);
            autosMenuItem.setDisable(true);
            normsMenuItem.setDisable(true);
            applicationOptionsMenuItem.setDisable(true);
            updateRouteFormsButton.setDisable(true);
        } else {
            loginMenuItem.setDisable(true);
            routeFormsMenuItem.setDisable(false);
            branchReportMenuItem.setDisable(false);
            employeeMenuItem.setDisable(true);
            branchesMenuItem.setDisable(true);
            driversMenuItem.setDisable(true);
            autosMenuItem.setDisable(true);
            normsMenuItem.setDisable(true);
            applicationOptionsMenuItem.setDisable(true);
            updateRouteFormsButton.setDisable(false);
            if (currentEmployee.isAdmin()) {
                dayStatMenuItem.setDisable(false);
                employeeMenuItem.setDisable(false);
                branchesMenuItem.setDisable(false);
                driversMenuItem.setDisable(false);
                autosMenuItem.setDisable(false);
                normsMenuItem.setDisable(false);
                applicationOptionsMenuItem.setDisable(false);
            }

        }

        updateRouteForms();


    }

    private void updateControls() {

        branchLabel.setText(loginHolder.getCurrentEmployee() == null ?
                "" : loginHolder.isAdmin() ? "Все филиалы" : loginHolder.getCurrentEmployee().getBranch().getName());
        currentDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    private void updateRouteForms() {

        if (loginHolder.getCurrentEmployee() != null) {
            RouteFormsUpdater routeFormsUpdater = new RouteFormsUpdater(loginHolder.getCurrentEmployee().getBranch());
            if (loginHolder.isAdmin()) {
                routeFormsUpdater = new RouteFormsUpdater(null);
            } else if (loginHolder.getCurrentEmployee().getBranch()==null) {
                observableRouteForms.clear();
                return;
            }

            BooleanBinding runningBinding = routeFormsUpdater.stateProperty().isEqualTo(Task.State.RUNNING);
            updateProgressIndicator.visibleProperty().bind(runningBinding);
            log.info("Update RouteForms");
            new Thread(routeFormsUpdater).start();
        } else {
            observableRouteForms.clear();
        }

    }

    private void openRouteForm() {
        RouteForm routeForm = routeFormsTableView.getSelectionModel().getSelectedItem();
        if (routeForm != null) {
            routeFormController.showAndWait(stage, routeForm);
            updateRouteForms();
        }
    }

    private void openDocs() {
        String jarPath = FileUtils.getJarPath();
        String docPath = new File(jarPath) + File.separator + "docs" + File.separator + "RouteFormsDocs.docx";

        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        } else {
            return;
        }

        //Открытие файла:
        try {
            desktop.open(new File(docPath));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    class EmployeeReader extends Task<Collection<Employee>> {
        private Callback<Collection<Employee>,Void> callback;

        public EmployeeReader(Callback<Collection<Employee>, Void> callback) {
            this.callback = callback;
        }

        @Override
        protected Collection<Employee> call() throws Exception {
            Collection<Employee> employees = routeFormsService.findActiveEmployees();
            return employees;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            callback.call(getValue());
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            log.error("Failed reading employees", ex);
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Не удалось подсоединиться к серверу.")
                    .showWarning();
        }
    }


    class RouteFormsUpdater extends Task<Collection<RouteForm>>  {

        private Branch branch;

        public RouteFormsUpdater(Branch branch) {
            this.branch = branch;
        }

        @Override
        protected Collection<RouteForm> call() throws Exception {
            Collection<RouteForm> routeForms;
            LocalDate begDate = LocalDate.now().minusDays(updateDaysCount);
            if (branch!=null) {
                log.debug("Reading RouteForms for branch: " + branch + " from " + begDate );
                routeForms = routeFormsService.findRouteFormsByBranchAndBegDate(branch, begDate);
            }
            else {
                log.debug("Reading RouteForms from "+ begDate);
                routeForms = routeFormsService.findRouteFormsByBegDate(begDate);
            }

            return routeForms;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            observableRouteForms.clear();
            observableRouteForms.addAll(getValue());
            log.info("Server returned " + observableRouteForms.size() + " RouteForms");
            log.info("List update complete.");
        }

        @Override
        protected void failed() {
            Throwable exception = getException();
            log.error("Error updating list: " + exception, exception);
            Dialogs.create()
                    .owner(stage)
                    .title("Ошибка")
                    .message("Ошибка при получении данных: " + exception)
                    .showException(exception);
        }
    }


}
