package org.kesler.mfc.routeforms.client.gui.report;

import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.Driver;
import org.kesler.mfc.routeforms.client.export.report.branch.BranchReport;
import org.kesler.mfc.routeforms.client.export.report.branch.BranchReportExporter;
import org.kesler.mfc.routeforms.client.export.report.branch.BranchReportService;
import org.kesler.mfc.routeforms.client.export.report.branch.EmptyListBranchReportServiceException;
import org.kesler.mfc.routeforms.client.gui.AbstractController;
import org.kesler.mfc.routeforms.client.gui.driver.DriverListController;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.util.LongTextFieldChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by alex on 25.09.15.
 */
@Component
public class BranchReportController extends AbstractController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @FXML protected Label branchLabel;
    @FXML protected Label autoLabel;
    @FXML protected Label driverLabel;
    @FXML protected ComboBox<Month> monthComboBox;
    @FXML protected TextField yearTextField;
    @FXML protected ProgressIndicator calculateProgressIndicator;

    @FXML protected TextField odoBeginTextField;
    @FXML protected TextField odoTotalTextField;
    @FXML protected TextField odoSityTextField;
    @FXML protected TextField odoVilTextField;
    @FXML protected TextField idleTimeTextField;
    @FXML protected TextField specTimeTextField;
    @FXML protected TextField odoEndTextField;


    @FXML protected Label fuelTypeLabel;
    @FXML protected TextField fuelBeginTextField;
    @FXML protected TextField fuelingTotalTextField;
    @FXML protected TextField fuelingCardTextField;
    @FXML protected TextField fuelingCacheTextField;
    @FXML protected TextField fuelConsumeTextField;
    @FXML protected TextField fuelEndTextField;




    @Autowired protected BranchReportService branchReportService;
    @Autowired protected BranchReportExporter branchReportExporter;
    @Autowired protected DriverListController driverListController;
    @Autowired protected RouteFormsService routeFormsService;

    protected Auto auto;
    protected Driver driver;
    protected LocalDate date;

    protected BranchReport branchReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthComboBox.getItems().addAll(Month.values());
        yearTextField.textProperty().addListener(new LongTextFieldChangeListener());
    }

    public void showAndWait(Window owner, Auto auto, Driver driver) {
        this.auto = auto;
        this.driver = driver;
        date = LocalDate.now().minusMonths(1);
        clear();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/calculator.png"));
        super.showAndWait(owner, "Отчет об эксплуатации", icon);
    }


    @Override
    protected void updateContent() {
        branchLabel.setText(auto.getBranch().getName());
        autoLabel.setText(auto.getModel() + " " + auto.getRegNum());
        driverLabel.setText(driver == null ? "" : driver.getFioFull());
        monthComboBox.setValue(date.getMonth());
        monthComboBox.setCellFactory(new Callback<ListView<Month>, ListCell<Month>>() {
            @Override
            public ListCell<Month> call(ListView<Month> param) {
                return new ListCell<Month>() {
                    @Override
                    protected void updateItem(Month item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            setText(item.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
                        }
                    }
                };
            }
        });
        monthComboBox.setButtonCell(new ListCell<Month>() {
            @Override
            protected void updateItem(Month item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
                }
            }
        });
        yearTextField.setText(date.getYear()+"");

    }

    @FXML protected void handleSelectDriverButtonAction(ActionEvent ev) {
        selectDriver();
    }

    @FXML protected void handleCalculateButtonAction(ActionEvent ev) {
        calculate();
    }

    @FXML protected void handlePrintButtonAction(ActionEvent ev) {
        print();
    }




    private void selectDriver() {
        driverListController.showAndWaitSelect(stage, auto.getBranch());
        if (driverListController.getResult() == Result.OK) {
            driver = driverListController.getSelectedItem();
            driverLabel.setText(driver.getFioShort());
        }
    }

    private void calculate() {
        if (driver == null) {
            Dialogs.create()
                    .title("Внимание")
                    .message("Выберите водителя")
                    .showWarning();
            return;
        }
        date = date.withMonth(monthComboBox.getSelectionModel().getSelectedItem().getValue());
        date = date.withYear(Integer.parseInt(yearTextField.getText()));
        BranchReportCalculator branchReportCalculator = new BranchReportCalculator();

        BooleanBinding runningBinding = branchReportCalculator.stateProperty().isEqualTo(Task.State.RUNNING);
        calculateProgressIndicator.visibleProperty().bind(runningBinding);

        log.info("Calculating report..");
        new Thread(branchReportCalculator).start();
    }

    private void print() {
        BranchReportExporterTask exporterTask = new BranchReportExporterTask();

        new Thread(exporterTask).start();
    }


    private void updateCalculatedContent() {
        odoBeginTextField.setText(branchReport.beginOdo+"");
        odoTotalTextField.setText(branchReport.totalOdo+"");
        odoSityTextField.setText(branchReport.sityOdo+"");
        odoVilTextField.setText(branchReport.vilOdo+"");
        odoEndTextField.setText(branchReport.endOdo+"");
        idleTimeTextField.setText(branchReport.idleTime+"");
        specTimeTextField.setText(branchReport.specTime+"");

        fuelTypeLabel.setText(branchReport.fuelType.toString());
        fuelBeginTextField.setText(branchReport.beginFuel+"");
        fuelingTotalTextField.setText(branchReport.totalFueling+"");
        fuelingCardTextField.setText(branchReport.cardFueling+"");
        fuelingCacheTextField.setText(branchReport.cacheFueling+"");
        fuelConsumeTextField.setText(branchReport.consumedFuel+"");
        fuelEndTextField.setText(branchReport.endFuel+"");


    }

    private void clear() {

        odoBeginTextField.setText("");
        odoTotalTextField.setText("");
        odoSityTextField.setText("");
        odoVilTextField.setText("");
        odoEndTextField.setText("");
        idleTimeTextField.setText("");
        specTimeTextField.setText("");

        fuelTypeLabel.setText("");
        fuelBeginTextField.setText("");
        fuelingTotalTextField.setText("");
        fuelingCardTextField.setText("");
        fuelingCacheTextField.setText("");
        fuelConsumeTextField.setText("");
        fuelEndTextField.setText("");

    }

    class BranchReportCalculator extends Task<BranchReport> {
        @Override
        protected BranchReport call() throws Exception {
            return branchReportService.create(auto, driver, date);
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            branchReport = getValue();
            updateCalculatedContent();
            log.info("Report calculated");
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            if (ex.getCause() instanceof EmptyListBranchReportServiceException) {
                Dialogs.create()
                        .owner(stage)
                        .title("Внимание")
                        .message("Ошибка при формировании отчета: " + ex)
                        .showWarning();
                log.warn("RouteForms list is empty");
            } else {
                Dialogs.create()
                        .owner(stage)
                        .title("Внимание")
                        .message("Ошибка при формировании отчета: " + getException().getMessage())
                        .showException(getException());
                log.error("Error calculating report ", getException());
            }

        }
    }

    class BranchReportExporterTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            if (branchReport!=null)
                branchReportExporter.exportBranchReport(branchReport);
            return null;
        }
    }

}
