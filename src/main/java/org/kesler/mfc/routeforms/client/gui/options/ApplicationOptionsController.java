package org.kesler.mfc.routeforms.client.gui.options;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.validation.Validator;
import org.kesler.mfc.routeforms.client.domain.ApplicationOptions;
import org.kesler.mfc.routeforms.client.domain.RouteForm;
import org.kesler.mfc.routeforms.client.gui.AbstractItemController;
import org.kesler.mfc.routeforms.client.security.OptionsHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.util.LongTextFieldChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Component
public class ApplicationOptionsController extends AbstractItemController implements Initializable{

    @FXML protected TextField orgNameTextField;
    @FXML protected TextField winterToSummerMonthTextField;
    @FXML protected TextField winterToSummerDayTextField;
    @FXML protected TextField summerToWinterMonthTextField;
    @FXML protected TextField summerToWinterDayTextField;
    @FXML protected TextField carRFTemplateTextField;
    @FXML protected TextField truckRFTemplateTextField;
    @FXML protected TextField branchReportTemplateTextField;

    private ApplicationOptions applicationOptions;

    private byte[] carRFTemplate;
    private byte[] truckRFTemplate;
    private byte[] branchReportTemplate;

    @Autowired
    protected RouteFormsService routeFormsService;
    @Autowired
    protected OptionsHolder optionsHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Predicate<String> dayOfMonthPredicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                if (s.isEmpty()) return false;
                Integer day = Integer.parseInt(s);
                return day > 0 && day < 32;
            }
        };

        Predicate<String> monthsPredicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                if (s.isEmpty()) return false;
                Integer month = Integer.parseInt(s);
                if (month < 1 || month > 12) {
                    return false;
                }
                String springString = winterToSummerMonthTextField.getText();
                String autumnString = summerToWinterMonthTextField.getText();

                if (!springString.isEmpty() && !autumnString.isEmpty()) {
                    Integer springMonth = Integer.parseInt(springString);
                    Integer autumnMonth = Integer.parseInt(autumnString);
                    if (autumnMonth < springMonth) return false;
                }

                return true;
            }
        };

        orgNameTextField.textProperty().addListener(invalidationListener);
        winterToSummerMonthTextField.textProperty().addListener(invalidationListener);
        winterToSummerDayTextField.textProperty().addListener(invalidationListener);
        summerToWinterMonthTextField.textProperty().addListener(invalidationListener);
        summerToWinterDayTextField.textProperty().addListener(invalidationListener);
        carRFTemplateTextField.textProperty().addListener(invalidationListener);
        truckRFTemplateTextField.textProperty().addListener(invalidationListener);
        branchReportTemplateTextField.textProperty().addListener(invalidationListener);

        validationSupport.registerValidator(winterToSummerDayTextField, false, Validator.createPredicateValidator(dayOfMonthPredicate,"День месяца за разрешенными рамками"));
        validationSupport.registerValidator(summerToWinterDayTextField, false, Validator.createPredicateValidator(dayOfMonthPredicate,"День месяца за разрешенными рамками"));
        validationSupport.registerValidator(winterToSummerMonthTextField, false, Validator.createPredicateValidator(monthsPredicate,"Месяц весны должен быть меньше месяца осени"));
        validationSupport.registerValidator(summerToWinterMonthTextField, false, Validator.createPredicateValidator(monthsPredicate,"Месяц весны должен быть меньше месяца осени"));


        LongTextFieldChangeListener longTextFieldChangeListener = new LongTextFieldChangeListener();

        winterToSummerMonthTextField.textProperty().addListener(longTextFieldChangeListener);
        winterToSummerDayTextField.textProperty().addListener(longTextFieldChangeListener);
        summerToWinterMonthTextField.textProperty().addListener(longTextFieldChangeListener);
        summerToWinterDayTextField.textProperty().addListener(longTextFieldChangeListener);
    }

    @Override
    public void showAndWait(Window owner) {
        loadOptions();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/gears_preferences.png"));
        super.showAndWait(owner, "Настройки приложения", icon);
    }


    @Override
    protected void updateContent() {

        if (applicationOptions ==null) return;

        orgNameTextField.setText(applicationOptions.getOrgName());

        winterToSummerMonthTextField.setText(applicationOptions.getWinterToSummerMonth()+"");
        winterToSummerDayTextField.setText(applicationOptions.getWinterToSummerDay()+"");
        summerToWinterMonthTextField.setText(applicationOptions.getSummerToWinterMonth()+"");
        summerToWinterDayTextField.setText(applicationOptions.getSummerToWinterDay()+"");

        carRFTemplateTextField.setText(carRFTemplate == null ? "Не загружен" : "Загружен");
        truckRFTemplateTextField.setText(truckRFTemplate==null?"Не загружен" : "Загружен");
        branchReportTemplateTextField.setText(branchReportTemplate==null?"Не загружен" : "Загружен");

    }


    @FXML protected void handleSelectCarRFTemplateButtonAction(ActionEvent ev) {
        carRFTemplate = selectXlsxFile();
        carRFTemplateTextField.setText(carRFTemplate == null ? "Не загружен" : "Загружен");
    }

    @FXML protected void handleViewCarRFTemplateButtonAction(ActionEvent ev) {
        showTemplate(carRFTemplate, "Шаблон легкового автомобиля");
    }

    @FXML protected void handleSelectTruckRFTemplateButtonAction(ActionEvent ev) {
        truckRFTemplate = selectXlsxFile();
        truckRFTemplateTextField.setText(truckRFTemplate == null ? "Не загружен" : "Загружен");
    }

    @FXML protected void handleViewTruckRFTemplateButtonAction(ActionEvent ev) {
        showTemplate(truckRFTemplate, "Шаблон грузового автомобиля");
    }

    @FXML protected void handleSelectBranchReportTemplateButtonAction(ActionEvent ev) {
        branchReportTemplate = selectXlsxFile();
        branchReportTemplateTextField.setText(branchReportTemplate == null ? "Не загружен" : "Загружен");
    }

    @FXML protected void handleViewBranchReportTemplateButtonAction(ActionEvent ev) {
        showTemplate(branchReportTemplate, "Шаблон отчета филиала");
    }

    @FXML protected void handleRecalculateStatusesButtonAction(ActionEvent ev) {
        log.info("Recalculating statuses");
        Collection<RouteForm> routeForms = routeFormsService.findRouteForms();
        for (RouteForm routeForm : routeForms) {
            RouteForm previous = routeFormsService.findRouteFormById(routeForm.getPreviousRouteFormID());
            routeForm.updateState(previous);
            routeFormsService.saveRouteForm(routeForm);
        }
    }

    @Override
    protected void afterUpdatingContent() {
        setButtonSet(ButtonSet.CLOSE);
    }

    @Override
    protected void updateResult() {
        if (applicationOptions == null) return;

        applicationOptions.setOrgName(orgNameTextField.getText());
        Integer winterToSummerMonth = null;
        if (winterToSummerMonthTextField.getText()!=null && !winterToSummerMonthTextField.getText().isEmpty()) {
            winterToSummerMonth = Integer.parseInt(winterToSummerMonthTextField.getText());
        }

        Integer winterToSummerDay = null;
        if (winterToSummerDayTextField.getText()!=null && !winterToSummerDayTextField.getText().isEmpty()) {
            winterToSummerDay = Integer.parseInt(winterToSummerDayTextField.getText());
        }

        Integer summerToWinterMonth = null;
        if (summerToWinterMonthTextField.getText()!=null && !summerToWinterMonthTextField.getText().isEmpty()) {
            summerToWinterMonth = Integer.parseInt(summerToWinterMonthTextField.getText());
        }

        Integer summerToWinterDay = null;
        if (summerToWinterDayTextField.getText()!=null && !summerToWinterDayTextField.getText().isEmpty()) {
            summerToWinterDay = Integer.parseInt(summerToWinterDayTextField.getText());
        }

        applicationOptions.setWinterToSummerMonth(winterToSummerMonth);
        applicationOptions.setWinterToSummerDay(winterToSummerDay);
        applicationOptions.setSummerToWinterMonth(summerToWinterMonth);
        applicationOptions.setSummerToWinterDay(summerToWinterDay);
        applicationOptions.setCarForm(carRFTemplate);
        applicationOptions.setTruckForm(truckRFTemplate);
        applicationOptions.setBranchReportForm(branchReportTemplate);

        optionsHolder.setOptions(applicationOptions);

    }


    @Override
    protected void saveAsync() {
        if (applicationOptions !=null) routeFormsService.saveOptions(applicationOptions);
    }


    private void loadOptions() {
        OptionsLoader optionsLoader = new OptionsLoader();
        new Thread(optionsLoader).start();
    }

    class OptionsLoader extends Task<Void> {
        private final Logger log = LoggerFactory.getLogger(this.getClass());
        @Override
        protected Void call() throws Exception {

            log.info("Loading db options..");
            applicationOptions = routeFormsService.loadOptions();
            carRFTemplate = applicationOptions.getCarForm();
            truckRFTemplate = applicationOptions.getTruckForm();
            branchReportTemplate = applicationOptions.getBranchReportForm();

            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            log.info("Loadind options complete..");
            updateContent();
            afterUpdatingContent();
        }
    }


    private byte[] selectXlsxFile() {
        byte[] template = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл шаблона");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы Excel", "*.xlsx"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                int length = fileInputStream.available();
                template = new byte[length];
                fileInputStream.read(template);

            } catch (IOException ex) {
                Dialogs.create()
                        .owner(stage)
                        .message("Не удалось открыть файл: " + ex.getMessage())
                        .showError();
            }
        }

        return template;

    }

    protected void showTemplate(byte[] data, String templateName) {
        if (data==null) return;


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл для сохранения шаблона");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы Excel", "*.xlsx"));
        fileChooser.setInitialFileName(templateName + " "+ LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        File templateFile = fileChooser.showSaveDialog(stage);
        if (templateFile == null) return;

        try {
            templateFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(templateFile);
            fileOutputStream.write(data);
            fileOutputStream.close();

        } catch (IOException ex) {
            log.error("Error writing template: " + ex, ex);
        }


        openFile(templateFile);

    }

    protected void openFile(File file) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        } else {
            return;
        }

        //Открытие файла:

        try {
            desktop.open(file);
        } catch (IOException ioe) {
            log.error("Error while opening file: " + ioe);
        }

    }


}
