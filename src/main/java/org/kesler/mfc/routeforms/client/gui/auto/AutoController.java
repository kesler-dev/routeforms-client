package org.kesler.mfc.routeforms.client.gui.auto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.FuelType;
import org.kesler.mfc.routeforms.client.domain.Norm;
import org.kesler.mfc.routeforms.client.gui.AbstractItemController;
import org.kesler.mfc.routeforms.client.gui.branch.BranchListController;
import org.kesler.mfc.routeforms.client.gui.norm.NormListController;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер редактирования автомобиля
 */
@Component
public class AutoController extends AbstractItemController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired protected RouteFormsService routeFormsService;
    @Autowired protected BranchListController branchListController;
    @Autowired protected NormListController normListController;

    @FXML protected TextField modelTextField;
    @FXML protected TextField regNumTextField;
    @FXML protected ComboBox<Auto.Type> typeComboBox;
    @FXML protected ComboBox<FuelType> fuelTypeComboBox;
    @FXML protected TextField branchTextField;
    @FXML protected TextField normTextField;

    private Auto auto;

    private Branch branch;
    private Norm norm;

    private ValidationSupport validationSupport = new ValidationSupport();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        typeComboBox.getItems().addAll(Auto.Type.values());
        fuelTypeComboBox.getItems().addAll(FuelType.values());

        modelTextField.textProperty().addListener(invalidationListener);
        regNumTextField.textProperty().addListener(invalidationListener);
        typeComboBox.valueProperty().addListener(invalidationListener);
        fuelTypeComboBox.valueProperty().addListener(invalidationListener);
        branchTextField.textProperty().addListener(invalidationListener);
        normTextField.textProperty().addListener(invalidationListener);


        validationSupport.registerValidator(modelTextField, false, Validator.createEmptyValidator("Поле Модель не должно быть пустым"));
        validationSupport.registerValidator(regNumTextField, false, Validator.createEmptyValidator("Поле РегНомер не должно быть пустым"));
        validationSupport.registerValidator(typeComboBox, false, Validator.createEmptyValidator("Поле Тип не должно быть пустым"));
        validationSupport.registerValidator(fuelTypeComboBox, false, Validator.createEmptyValidator("Поле Топливо не должно быть пустым"));
        validationSupport.registerValidator(branchTextField, false, Validator.createEmptyValidator("Поле Филиал не должно быть пустым"));
        validationSupport.registerValidator(normTextField, false, Validator.createEmptyValidator("Поле Норма не должно быть пустым"));

    }


    public void showAndWait(Window owner, Auto auto) {
        this.auto = auto;
        modelTextField.requestFocus();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/car_sedan_green.png"));
        super.showAndWait(owner, "Автомобиль", icon);
    }


    @Override
    protected void afterUpdatingContent() {
        if (auto.getId() == null) {
            setButtonSet(ButtonSet.SAVE_CANCEL);
        } else {
            setButtonSet(ButtonSet.CLOSE);
        }
    }

    @Override
    protected void updateContent() {
        modelTextField.setText(auto.getModel());
        regNumTextField.setText(auto.getRegNum());
        typeComboBox.setValue(auto.getType());
        fuelTypeComboBox.setValue(auto.getFuelType());
        branch = auto.getBranch();
        branchTextField.setText(branch==null?"":branch.toString());
        norm = auto.getNorm();
        normTextField.setText(norm==null?"":norm.toString());

        validationSupport.redecorate();
    }

    @Override
    protected void updateResult() {
        auto.setModel(modelTextField.getText());
        auto.setRegNum(regNumTextField.getText());
        auto.setType(typeComboBox.getValue());
        auto.setFuelType(fuelTypeComboBox.getValue());
        auto.setBranch(branch);
        auto.setNorm(norm);
    }

    @Override
    protected void handleSave() {
        if (validationSupport.isInvalid()) {
            validationSupport.redecorate();
            Notifications.create()
                    .owner(stage)
                    .position(Pos.CENTER)
                    .text("Заполните необходимые поля")
                    .hideAfter(Duration.millis(1200))
                    .showWarning();
            return;
        }
        super.handleSave();
    }


    @Override
    protected void saveAsync() {
//        log.debug("Saving auto " + auto.getFioShort());
        routeFormsService.saveAuto(auto);

    }


    @FXML protected void handleSelectBranchButtonAction(ActionEvent ev) {
        log.info("Select Branch..");
        branchListController.showAndWaitSelect(stage);
        if (branchListController.getResult() == Result.OK) {
            branch = branchListController.getSelectedItem();
            branchTextField.setText(branch==null?"":branch.toString());
            log.info("Selected Branch: " + branch);
        }
    }

    @FXML protected void handleSelectNormButtonAction(ActionEvent ev) {
        log.info("Select Norm..");
        normListController.showAndWaitSelect(stage);
        if (normListController.getResult() == Result.OK) {
            norm = normListController.getSelectedItem();
            normTextField.setText(norm==null?"":norm.toString());
            log.info("Selected Norm: " + norm);
        }
    }

}
