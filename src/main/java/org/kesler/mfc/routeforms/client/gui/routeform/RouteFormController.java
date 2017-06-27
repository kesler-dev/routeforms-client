package org.kesler.mfc.routeforms.client.gui.routeform;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Window;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.Validator;
import org.kesler.mfc.routeforms.client.domain.*;
import org.kesler.mfc.routeforms.client.export.routeform.CarRFRFExporter;
import org.kesler.mfc.routeforms.client.export.routeform.RFExporter;
import org.kesler.mfc.routeforms.client.export.routeform.TruckRFRFExporter;
import org.kesler.mfc.routeforms.client.gui.AbstractItemController;
import org.kesler.mfc.routeforms.client.gui.driver.DriverListController;
import org.kesler.mfc.routeforms.client.security.LoginHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.util.Converter;
import org.kesler.mfc.routeforms.client.util.DecimalTextFieldChangeListener;
import org.kesler.mfc.routeforms.client.util.LongTextFieldChangeListener;
import org.kesler.mfc.routeforms.client.util.TimeTextFieldChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Путевой лист
 */
@Component
public class RouteFormController extends AbstractItemController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());



    @FXML protected TextField seriesTextField;
    @FXML protected TextField numberTextField;
    @FXML protected TextField autoTextField;
    @FXML protected TextField driverTextField;
    @FXML protected DatePicker datePicker;
    @FXML protected Label statusLabel;
    @FXML protected Label previousLabel;
    @FXML protected Button copyFromPreviousButton;
    @FXML protected TextField orgTextField;
    @FXML protected TextField addressTextField;
    @FXML protected TextField departureTimeTextField;
    @FXML protected TextField departureODOTextField;
    @FXML protected TextField departureFuelTextField;
    @FXML protected TextField fuellingTextField;
    @FXML protected RadioButton cardFuellingRadioButton;
    @FXML protected TextField combackTimeTextField;
    @FXML protected TextField combackODOTextField;
    @FXML protected TextField combackFuelTextField;
    @FXML protected ComboBox<Norm.SeasonType> seasonComboBox;
    @FXML protected ComboBox<Norm.ModeType> modeComboBox;
    @FXML protected TextField idleTimeTextField;
    @FXML protected TextField specTimeTextField;
    @FXML protected TextField consumptionRateTextField;
    @FXML protected TextField mileageTextField;
    @FXML protected TextField workTimeTextField;
    @FXML protected TextField consumptionTextField;



    protected RouteForm routeForm;
    protected RouteForm previousRouteForm;
    protected Auto auto;
    protected Driver driver;
    protected boolean newRouteForm;
    protected boolean lastRouteForm;

    @Autowired protected RouteFormsService routeFormsService;
    @Autowired protected DriverListController driverListController;
    @Autowired protected LoginHolder loginHolder;

    @Autowired protected CarRFRFExporter carRFExporter;
    @Autowired protected TruckRFRFExporter truckRFExporter;

    protected NormChangeListener normChangeListener = new NormChangeListener();
    protected RecalculateTimeListener recalculateTimeListener = new RecalculateTimeListener();
    protected RecalculateFuelListener recalculateFuelListener = new RecalculateFuelListener();
    protected DateChangeListener dateChangeListener = new DateChangeListener();


    public void showAndWait(Window owner, RouteForm routeForm) {
        this.routeForm = routeForm;
        newRouteForm = false;
        lastRouteForm = false;
        this.previousRouteForm = routeFormsService.findRouteFormById(routeForm.getPreviousRouteFormID());
        auto = routeForm.getAuto();
        driver = routeForm.getDriver();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/form_green.png"));
        super.showAndWait(owner, "Путевой лист", icon);
    }

    public void showAndWaitNew(Window owner, RouteForm routeForm) {
        this.routeForm = routeForm;
        newRouteForm = true;
        lastRouteForm = true;
        this.previousRouteForm = routeFormsService.findRouteFormById(routeForm.getPreviousRouteFormID());
        auto = routeForm.getAuto();
        driver = routeForm.getDriver();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/form_green.png"));
        super.showAndWait(owner, "Путевой лист", icon);
    }

    public void showAndWaitLast(Window owner, RouteForm routeForm) {
        this.routeForm = routeForm;
        newRouteForm = false;
        lastRouteForm = true;
        this.previousRouteForm = routeFormsService.findRouteFormById(routeForm.getPreviousRouteFormID());
        auto = routeForm.getAuto();
        driver = routeForm.getDriver();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/form_green.png"));
        super.showAndWait(owner, "Путевой лист", icon);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        seasonComboBox.getItems().addAll(Norm.SeasonType.values());
        modeComboBox.getItems().addAll(Norm.ModeType.values());



        DecimalTextFieldChangeListener decimalTextFieldChangeListener = new DecimalTextFieldChangeListener();
        LongTextFieldChangeListener longTextFieldChangeListener = new LongTextFieldChangeListener();

        numberTextField.textProperty().addListener(longTextFieldChangeListener);
        departureODOTextField.textProperty().addListener(longTextFieldChangeListener);
        departureFuelTextField.textProperty().addListener(decimalTextFieldChangeListener);
        fuellingTextField.textProperty().addListener(decimalTextFieldChangeListener);
        combackODOTextField.textProperty().addListener(longTextFieldChangeListener);
//        combackFuelTextField.textProperty().addListener(decimalTextFieldChangeListener);
        idleTimeTextField.textProperty().addListener(decimalTextFieldChangeListener);
        specTimeTextField.textProperty().addListener(decimalTextFieldChangeListener);
        consumptionRateTextField.textProperty().addListener(decimalTextFieldChangeListener);
        consumptionTextField.textProperty().addListener(decimalTextFieldChangeListener);


        departureTimeTextField.textProperty().addListener(new TimeTextFieldChangeListener(departureTimeTextField));
        combackTimeTextField.textProperty().addListener(new TimeTextFieldChangeListener(combackTimeTextField));
//        workTimeTextField.textProperty().addListener(new TimeTextFieldChangeListener(workTimeTextField));


        validationSupport.registerValidator(seriesTextField, false, Validator.createEmptyValidator("Введите серию путевого листа"));
        validationSupport.registerValidator(numberTextField, false, Validator.createEmptyValidator("Введите номер путевого листа"));

        validationSupport.registerValidator(departureTimeTextField,
                false,
                Validator.createRegexValidator("Время выезда - неправильный формат",
                        "^([0-2]{1}[0-9]{1}[:]{1}[0-5]{1}[0-9]{1})?", Severity.ERROR));
        validationSupport.registerValidator(departureODOTextField, false, Validator.createEmptyValidator("Задайте показания спидометра при выезде", Severity.ERROR));
        validationSupport.registerValidator(departureFuelTextField, false, Validator.createEmptyValidator("Задайте остаток горючего при выезде", Severity.ERROR));


        validationSupport.registerValidator(combackTimeTextField,
                false,
                Validator.createRegexValidator("Время возвращения - неправильный формат",
                        "^([0-2]{1}[0-9]{1}[:]{1}[0-5]{1}[0-9]{1})?", Severity.ERROR));

        validationSupport.registerValidator(workTimeTextField,
                false,
                Validator.createRegexValidator("Время работы - неправильный формат",
                        "^([0-2]{1}[0-9]{1}[:]{1}[0-5]{1}[0-9]{1})?", Severity.ERROR));

        validationSupport.registerValidator(combackFuelTextField,
                false,
                Validator.createPredicateValidator(new CombackODOValidPredicate(),"Топливо по возвращении должно быть больше нуля"));



    }

    @Override
    protected void updateContent() {
        removeGuiListeners();
        seriesTextField.setText(routeForm.getSeries());
        numberTextField.setText(routeForm.getNumber());
        autoTextField.setText(auto == null ? "" : auto.toString());
        driverTextField.setText(driver == null ? "" : driver.getFioShort());
        datePicker.setValue(routeForm.getDate());
        statusLabel.setText(routeForm.getState()==null?"":routeForm.getState().toString());
        if (routeForm.getState()!=null) statusLabel.setTextFill(routeForm.getState().getColor());
        previousLabel.setText(previousRouteForm==null?"не определен":(previousRouteForm.getSeries() +
        " " + previousRouteForm.getNumber() + " от " + previousRouteForm.getDate()));
        copyFromPreviousButton.setVisible(routeForm.getState()!=null && routeForm.getState().equals(RouteForm.State.UNCONSISTENT));
        orgTextField.setText(routeForm.getOrgName());
        addressTextField.setText(routeForm.getAddress());
        departureTimeTextField.setText(routeForm.getDepartureTime() == null ? "" : routeForm.getDepartureTime().toString());
        departureODOTextField.setText(routeForm.getDepartureODO() == null ? "" : routeForm.getDepartureODO().toString());
        departureFuelTextField.setText(routeForm.getDepartureFuel() == null ? "" : routeForm.getDepartureFuel().toString());
        fuellingTextField.setText(routeForm.getFuelling() == null ? "" : routeForm.getFuelling().toString());
        cardFuellingRadioButton.setSelected(routeForm.getFuellingType()==null?true:routeForm.getFuellingType().equals(FuellingType.CARD));
        combackTimeTextField.setText(routeForm.getCombackTime() == null ? "" : routeForm.getCombackTime().toString());
        workTimeTextField.setText(routeForm.getWorkTime() == null ? "" : String.format("%02d:%02d", routeForm.getWorkTime().toHours(), routeForm.getWorkTime().toMinutes() % 60));
        combackODOTextField.setText(routeForm.getCombackODO() == null ? "" : routeForm.getCombackODO().toString());
        combackFuelTextField.setText(routeForm.getCombackFuel() == null ? "" : routeForm.getCombackFuel().toString());
        seasonComboBox.setValue(routeForm.getSeasonType());
//        modeComboBox.setValue(routeForm.getModeType());
        idleTimeTextField.setText(routeForm.getIdleTime() == null ? "" : routeForm.getIdleTime().toString());
        String specTimeString = routeForm.getSpecTime() == null ? "" : routeForm.getSpecTime().toString();
        boolean noSpecSummer = routeForm.getAuto().getNorm().isNoSpecSummer()==null?true:routeForm.getAuto().getNorm().isNoSpecSummer();
        if (Norm.SeasonType.SUMMER.equals(routeForm.getSeasonType()) && noSpecSummer) {
            specTimeString = "";
            specTimeTextField.setDisable(true);
        } else {
            specTimeTextField.setDisable(false);
        }
        specTimeTextField.setText(specTimeString);
        consumptionRateTextField.setText(routeForm.getConsumptionRate() == null ? "" : routeForm.getConsumptionRate().toString());
        consumptionTextField.setText(routeForm.getConsumption() == null ? "" : routeForm.getConsumption().toString());
        Long mileage = null;
        if (routeForm.getDepartureODO()!=null && routeForm.getCombackODO()!=null) {
            mileage = routeForm.getCombackODO()-routeForm.getDepartureODO();
        }

        mileageTextField.setText(mileage==null?"":mileage+"");

//        recalculateFuel();
//        recalculateTime();
        setGuiListeners();
        // для автоматического расчета нормы
        modeComboBox.setValue(routeForm.getModeType());

    }

    @Override
    protected void afterUpdatingContent() {
        if (newRouteForm) {
            setButtonSet(ButtonSet.SAVE_CANCEL);
        } else {
            setButtonSet(ButtonSet.CLOSE);
        }
    }

    @Override
    protected void setGuiListeners() {

        datePicker.valueProperty().addListener(dateChangeListener);
        datePicker.valueProperty().addListener(invalidationListener);
        seriesTextField.textProperty().addListener(invalidationListener);
        numberTextField.textProperty().addListener(invalidationListener);
        autoTextField.textProperty().addListener(invalidationListener);
        driverTextField.textProperty().addListener(invalidationListener);
        datePicker.valueProperty().addListener(invalidationListener);
        orgTextField.textProperty().addListener(invalidationListener);
        addressTextField.textProperty().addListener(invalidationListener);
        departureTimeTextField.textProperty().addListener(invalidationListener);
        departureODOTextField.textProperty().addListener(invalidationListener);
        departureFuelTextField.textProperty().addListener(invalidationListener);
        fuellingTextField.textProperty().addListener(invalidationListener);
        cardFuellingRadioButton.selectedProperty().addListener(invalidationListener);
        combackTimeTextField.textProperty().addListener(invalidationListener);
        workTimeTextField.textProperty().addListener(invalidationListener);
        combackODOTextField.textProperty().addListener(invalidationListener);
        combackFuelTextField.textProperty().addListener(invalidationListener);
        seasonComboBox.valueProperty().addListener(invalidationListener);
        modeComboBox.valueProperty().addListener(invalidationListener);
        idleTimeTextField.textProperty().addListener(invalidationListener);
        specTimeTextField.textProperty().addListener(invalidationListener);
        consumptionRateTextField.textProperty().addListener(invalidationListener);
        consumptionTextField.textProperty().addListener(invalidationListener);

        seasonComboBox.valueProperty().addListener(normChangeListener);
        modeComboBox.valueProperty().addListener(normChangeListener);

        departureTimeTextField.textProperty().addListener(recalculateTimeListener);
        combackTimeTextField.textProperty().addListener(recalculateTimeListener);

        departureODOTextField.textProperty().addListener(recalculateFuelListener);
        departureFuelTextField.textProperty().addListener(recalculateFuelListener);
        fuellingTextField.textProperty().addListener(recalculateFuelListener);
        combackODOTextField.textProperty().addListener(recalculateFuelListener);
        idleTimeTextField.textProperty().addListener(recalculateFuelListener);
        specTimeTextField.textProperty().addListener(recalculateFuelListener);
        consumptionRateTextField.textProperty().addListener(recalculateFuelListener);

    }

    @Override
    protected void removeGuiListeners() {
        datePicker.valueProperty().removeListener(dateChangeListener);
        datePicker.valueProperty().removeListener(invalidationListener);
        seriesTextField.textProperty().removeListener(invalidationListener);
        numberTextField.textProperty().removeListener(invalidationListener);
        autoTextField.textProperty().removeListener(invalidationListener);
        driverTextField.textProperty().removeListener(invalidationListener);
        datePicker.valueProperty().removeListener(invalidationListener);
        orgTextField.textProperty().removeListener(invalidationListener);
        addressTextField.textProperty().removeListener(invalidationListener);
        departureTimeTextField.textProperty().removeListener(invalidationListener);
        departureODOTextField.textProperty().removeListener(invalidationListener);
        departureFuelTextField.textProperty().removeListener(invalidationListener);
        fuellingTextField.textProperty().removeListener(invalidationListener);
        cardFuellingRadioButton.selectedProperty().removeListener(invalidationListener);
        combackTimeTextField.textProperty().removeListener(invalidationListener);
        workTimeTextField.textProperty().removeListener(invalidationListener);
        combackODOTextField.textProperty().removeListener(invalidationListener);
        combackFuelTextField.textProperty().removeListener(invalidationListener);
        seasonComboBox.valueProperty().removeListener(invalidationListener);
        modeComboBox.valueProperty().removeListener(invalidationListener);
        idleTimeTextField.textProperty().removeListener(invalidationListener);
        specTimeTextField.textProperty().removeListener(invalidationListener);
        consumptionRateTextField.textProperty().removeListener(invalidationListener);
        consumptionTextField.textProperty().removeListener(invalidationListener);

        seasonComboBox.valueProperty().removeListener(normChangeListener);
        modeComboBox.valueProperty().removeListener(normChangeListener);



        departureTimeTextField.textProperty().removeListener(recalculateTimeListener);
        combackTimeTextField.textProperty().removeListener(recalculateTimeListener);



        departureODOTextField.textProperty().removeListener(recalculateFuelListener);
        departureFuelTextField.textProperty().removeListener(recalculateFuelListener);
        fuellingTextField.textProperty().removeListener(recalculateFuelListener);
        combackODOTextField.textProperty().removeListener(recalculateFuelListener);
        idleTimeTextField.textProperty().removeListener(recalculateFuelListener);
        specTimeTextField.textProperty().removeListener(recalculateFuelListener);
        consumptionRateTextField.textProperty().removeListener(recalculateFuelListener);


    }

    @FXML
    protected void handlePrintRouteFormButtonAction(ActionEvent ev) {
        log.info("Handle PrintRouteForm action");
        printRouteForm();
    }

    @FXML
    protected void handleCopyFromPreviousButtonAction(ActionEvent ev) {
        log.info("Handle copy from previous action");
        departureODOTextField.setText(previousRouteForm.getCombackODO()+"");
        departureFuelTextField.setText(previousRouteForm.getCombackFuel()+"");
        recalculateFuel();
    }

    @FXML
    protected void handleRecalcButtonAction(ActionEvent ev) {
        log.info("Handle recalculate button action");
        renorm();
        recalculateTime();
        recalculateFuel();
    }



    @Override
    protected void updateResult() {
        routeForm.setSeries(seriesTextField.getText());
        routeForm.setNumber(numberTextField.getText());
        routeForm.setAuto(auto);
        routeForm.setDriver(driver);
        routeForm.setDate(datePicker.getValue());
        routeForm.setOrgName(orgTextField.getText());
        routeForm.setAddress(addressTextField.getText());

        LocalTime departureTime = null;
        if (departureTimeTextField.getText()!=null&&!departureTimeTextField.getText().isEmpty()) {
            try {
                departureTime = LocalTime.parse(departureTimeTextField.getText());
            } catch (Exception e) {
                Dialogs.create()
                        .owner(stage)
                        .title("Внимание")
                        .message("Время выезда - неправильный формат")
                        .showWarning();
            }
        }
        routeForm.setDepartureTime(departureTime);

        Long departureODO = Converter.getLong(departureODOTextField.getText());
        routeForm.setDepartureODO(departureODO);

        routeForm.setDepartureFuel(Converter.getDouble(departureFuelTextField.getText()));
        routeForm.setFuelling(Converter.getDouble(fuellingTextField.getText()));
        routeForm.setFuellingType(cardFuellingRadioButton.isSelected()?FuellingType.CARD:FuellingType.CACHE);

        LocalTime combackTime = null;
        if (combackTimeTextField.getText()!=null&&!combackTimeTextField.getText().isEmpty()) {
            try {
                combackTime = LocalTime.parse(combackTimeTextField.getText());
            } catch (Exception e) {
                Dialogs.create()
                        .owner(stage)
                        .title("Внимание")
                        .message("Время возвращения - неправильный формат")
                        .showWarning();
            }
        }
        routeForm.setCombackTime(combackTime);


        routeForm.setCombackODO(Converter.getLong(combackODOTextField.getText()));

        routeForm.setCombackFuel(Converter.getDouble(combackFuelTextField.getText()));
        routeForm.setSeasonType(seasonComboBox.getValue());
        routeForm.setModeType(modeComboBox.getValue());
        routeForm.setIdleTime(Converter.getDouble(idleTimeTextField.getText()));
        if (routeForm.getIdleTime() != null && routeForm.getAuto().getNorm().getIdleConsumptionRate() == null) {
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Норма потребления в простое не задана. Обратитесь в дирекцию.")
                    .showWarning();
        }
        routeForm.setSpecTime(Converter.getDouble(specTimeTextField.getText()));
        if (routeForm.getSpecTime() != null && routeForm.getAuto().getNorm().getSpecConsumptionRate() == null) {
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Норма потребления при работе спецоборудования не задана. Обратитесь в дирекцию.")
                    .showWarning();
        }

        routeForm.setConsumptionRate(Converter.getDouble(consumptionRateTextField.getText()));
        routeForm.setConsumption(Converter.getDouble(consumptionTextField.getText()));


        Duration workTime = null;
        try {
            String[] dur = workTimeTextField.getText().split(":");
            if (dur.length==2) {
                int hours = Integer.parseInt(dur[0]);
                int minutes = Integer.parseInt(dur[1]);
                workTime = Duration.ofHours(hours).plusMinutes(minutes);
            }
        } catch (Exception e) {
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Время работы - неправильный формат")
                    .showWarning();
        }

        if (workTime!=null && workTime.compareTo(Duration.ofHours(8)) > 0) {
            Action response = Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Время работы больше 8 часов.\n" +
                            "Установить время работы 8 часов?")
                    .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
                    .showConfirm();

            if (response == Dialog.ACTION_YES) {
                workTime = Duration.ofHours(8);
            }

        }


        routeForm.setWorkTime(workTime);


        // запоминаем текущий счетчик
        Integer newCurrentCount = Integer.parseInt(numberTextField.getText());
        if (lastRouteForm) routeForm.getAuto().getBranch().setRouteFormCounter(newCurrentCount);

        routeForm.updateState(previousRouteForm);

        if (routeForm.getEmployee()==null) routeForm.setEmployee(loginHolder.getCurrentEmployee());

    }

    @Override
    protected void saveAsync() {
        if (lastRouteForm) {
            routeForm.getAuto().getBranch().setRouteFormCounter(Integer.parseInt(routeForm.getNumber()));
            routeFormsService.saveBranch(routeForm.getAuto().getBranch());
        }

        routeFormsService.saveRouteForm(routeForm);
        if (!lastRouteForm) {
            RouteForm nextRouteForm = routeFormsService.findRouteFormByPreviousId(routeForm.getId());
            if (nextRouteForm!=null) {
                nextRouteForm.updateState(routeForm);
                routeFormsService.saveRouteForm(nextRouteForm);
            }
        }
    }

    @FXML protected void handleSelectDriverButtonAction(ActionEvent ev) {
        driverListController.showAndWaitSelect(stage, auto.getBranch());
        if (driverListController.getResult() == Result.OK) {
            driver = driverListController.getSelectedItem();
        }
        driverTextField.setText(driver == null ? "" : driver.getFioShort());
    }


    private void renorm(){
        Double consumptionRate = null;
        Norm.SeasonType season = seasonComboBox.getValue();
        Norm.ModeType mode = modeComboBox.getValue();
        if (season != null && mode != null) {
            consumptionRate = auto.getNorm().getConsumptionRate(season, mode);
        }
        consumptionRateTextField.setText(consumptionRate==null?"":consumptionRate+"");
    }

    private void recalculateFuel() {

        Long departureODO = Converter.getLong(departureODOTextField.getText());

        Long combackODO = Converter.getLong(combackODOTextField.getText());

        Long mileage = null;
        if (departureODO!=null && combackODO!=null && combackODO>departureODO) {
            mileage = combackODO-departureODO;
        }

        mileageTextField.setText(mileage==null?"":mileage+"");

        Double departureFuel = Converter.getDouble(departureFuelTextField.getText());

        Double fuelling = Converter.getDouble(fuellingTextField.getText());

        Double totalInitialFuel = null;
        if (departureFuel!=null) {
            totalInitialFuel = departureFuel;
            if (fuelling!=null) totalInitialFuel+=fuelling;
        }


        Double consumptionRate = Converter.getDouble(consumptionRateTextField.getText());

        Double idleTime = Converter.getDouble(idleTimeTextField.getText());

        Double specTime = Converter.getDouble(specTimeTextField.getText());

        // считаем расход топлива

        Double mileageConsumption = null;
        if (mileage != null && consumptionRate != null) {
            mileageConsumption = Converter.round(mileage*consumptionRate/100);
        }

        Double idleConsumptionRate = routeForm.getAuto().getNorm().getIdleConsumptionRate();

        Double idleConsumption = null;
        if (idleTime != null && idleConsumptionRate!=null) {
            idleConsumption = Converter.round(idleTime*idleConsumptionRate);
        }


        Double specConsumptionRate = routeForm.getAuto().getNorm().getSpecConsumptionRate();

        Double specConsumption = null;
        if (specTime != null && specConsumptionRate != null) {
            specConsumption = Converter.round(specTime * specConsumptionRate);
        }


        Double totalConsumption = null;
        if (mileageConsumption!=null) {
            totalConsumption = mileageConsumption;
            totalConsumption += idleConsumption==null?0:idleConsumption;
            totalConsumption += specConsumption==null?0:specConsumption;
            totalConsumption = Converter.round(totalConsumption); //округляем до десятых

        }



        consumptionTextField.setText(totalConsumption==null?"":totalConsumption+"");

        Double combackFuel = null;
        if (totalInitialFuel != null && totalConsumption != null) {
            combackFuel = totalInitialFuel - totalConsumption;
            combackFuel = Converter.round(combackFuel); //округляем до десятых
//            if (combackFuel<0) combackFuel = 0.0;
        }

        combackFuelTextField.setText(combackFuel==null?"":combackFuel+"");


    }

    protected void recalculateTime() {
        String departureTimeString = departureTimeTextField.getText();
        LocalTime departureTime = null;
        if (departureTimeString != null && !departureTimeString.isEmpty()) {
            try {
                departureTime = LocalTime.parse(departureTimeString);
            } catch (Exception e) { }
        }

        String combackTimeString = combackTimeTextField.getText();
        LocalTime combackTime = null;
        if (combackTimeString != null && !combackTimeString.isEmpty()) {
            try {
                combackTime = LocalTime.parse(combackTimeString);
            } catch (Exception e) { }
        }

        Duration workTime = null;
        if (departureTime != null && combackTime != null) {
            workTime = Duration.between(departureTime,combackTime);
            if (workTime.compareTo(Duration.ofHours(8))>0) workTime = Duration.ofHours(8);
        }

        workTimeTextField.setText(workTime==null?"":String.format("%02d:%02d",workTime.toHours(), workTime.toMinutes()-workTime.toHours()*60));

    }


    protected void printRouteForm() {
        RFExporter RFExporter = null;
        switch (routeForm.getAuto().getType()) {
            case CAR:
                RFExporter = carRFExporter;
                break;
            case TRUCK:
                RFExporter = truckRFExporter;
                break;
        }

        if (RFExporter ==null) return;

        if (routeForm.getDriver() == null) {
            Dialogs.create()
                    .owner(stage)
                    .title("Внимание")
                    .message("Выберите водителя")
                    .showWarning();
            return;
        }

        try {
            RFExporter.exportRouteForm(routeForm);
        } catch (Exception e) {
            Dialogs.create()
                    .owner(stage)
                    .title("Ошибка")
                    .message("Ошибка вышла: " + e.getMessage())
                    .showException(e);

        }
    }







    class NormChangeListener implements InvalidationListener{
        @Override
        public void invalidated(Observable observable) {
            renorm();
        }
    }


    class RecalculateFuelListener implements InvalidationListener {

        @Override
        public void invalidated(Observable observable) {
            recalculateFuel();
        }
    }

    class RecalculateTimeListener implements InvalidationListener {
        @Override
        public void invalidated(Observable observable) {
            recalculateTime();
        }
    }

    class DateChangeListener implements ChangeListener<LocalDate> {
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            if (previousRouteForm != null &&
                    newValue != null &&
                    !newValue.isAfter(previousRouteForm.getDate())) {
                    Dialogs.create()
                            .owner(stage)
                            .title("Внимание")
                            .message("Дата путевого листа не может быть ранее даты предыдущего")
                            .showWarning();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            datePicker.setValue(oldValue);
                        }
                    });
            }
            seasonComboBox.setValue(routeFormsService.getSeasonForDate(newValue));
        }
    }

    class CombackODOValidPredicate implements Predicate<String> {
        @Override
        public boolean test(String s) {
            if (s.isEmpty()) return true;
            Double value = Converter.getDouble(s);
            return value!=null && value > 0.0;
        }
    }

}
