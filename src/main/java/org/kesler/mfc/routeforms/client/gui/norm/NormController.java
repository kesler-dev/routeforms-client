package org.kesler.mfc.routeforms.client.gui.norm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kesler.mfc.routeforms.client.domain.Norm;
import org.kesler.mfc.routeforms.client.gui.AbstractItemController;
import org.kesler.mfc.routeforms.client.gui.branch.BranchListController;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.util.DecimalTextFieldChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер редактирования водителя
 */
@Component
public class NormController extends AbstractItemController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired protected RouteFormsService routeFormsService;
    @Autowired protected BranchListController branchListController;

    @FXML protected TextField nameTextField;
    @FXML protected TextField sumSitTextField;
    @FXML protected TextField sumVilTextField;
    @FXML protected TextField winSitTextField;
    @FXML protected TextField winVilTextField;
    @FXML protected TextField idleTextField;
    @FXML protected TextField specTextField;
    @FXML protected CheckBox noSpecSummerCheckBox;

    protected Norm norm;

    protected ValidationSupport validationSupport = new ValidationSupport();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nameTextField.textProperty().addListener(invalidationListener);

        sumSitTextField.textProperty().addListener(invalidationListener);
        sumVilTextField.textProperty().addListener(invalidationListener);
        winSitTextField.textProperty().addListener(invalidationListener);
        winVilTextField.textProperty().addListener(invalidationListener);
        idleTextField.textProperty().addListener(invalidationListener);
        specTextField.textProperty().addListener(invalidationListener);
        noSpecSummerCheckBox.selectedProperty().addListener(invalidationListener);


        DecimalTextFieldChangeListener decimalTextFieldChangeListener = new DecimalTextFieldChangeListener();

        sumSitTextField.textProperty().addListener(decimalTextFieldChangeListener);
        sumVilTextField.textProperty().addListener(decimalTextFieldChangeListener);
        winSitTextField.textProperty().addListener(decimalTextFieldChangeListener);
        winVilTextField.textProperty().addListener(decimalTextFieldChangeListener);
        idleTextField.textProperty().addListener(decimalTextFieldChangeListener);
        specTextField.textProperty().addListener(decimalTextFieldChangeListener);

        validationSupport.registerValidator(nameTextField, false, Validator.createEmptyValidator("Наименование не должно быть пустым"));
        validationSupport.registerValidator(sumSitTextField, false, Validator.createEmptyValidator("Введите значение"));
//        validationSupport.registerValidator(sumVilTextField, false, Validator.createEmptyValidator("Введите значение"));
        validationSupport.registerValidator(winSitTextField, false, Validator.createEmptyValidator("Введите значение"));
//        validationSupport.registerValidator(winVilTextField, false, Validator.createEmptyValidator("Введите значение"));
        validationSupport.registerValidator(idleTextField, false, Validator.createEmptyValidator("Введите значение"));

    }


    public void showAndWait(Window owner, Norm norm) {
        this.norm = norm;
        nameTextField.requestFocus();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/document_edit.png"));
        super.showAndWait(owner, "Норма", icon);
    }


    @Override
    protected void afterUpdatingContent() {
        if (norm.getId() == null) {
            setButtonSet(ButtonSet.SAVE_CANCEL);
        } else {
            setButtonSet(ButtonSet.CLOSE);
        }
    }

    @Override
    protected void updateContent() {
        nameTextField.setText(norm.getName());

        sumSitTextField.setText(norm.getSumSitConsumptionRate() + "");
        sumVilTextField.setText(norm.getSumVilConsumptionRate() + "");
        winSitTextField.setText(norm.getWinSitConsumptionRate() + "");
        winVilTextField.setText(norm.getWinVilConsumptionRate() + "");
        idleTextField.setText(norm.getIdleConsumptionRate() + "");
        specTextField.setText(norm.getSpecConsumptionRate() + "");
        noSpecSummerCheckBox.setSelected(norm.isNoSpecSummer()==null?true:norm.isNoSpecSummer());

    }

    @Override
    protected void updateResult() {
        norm.setName(nameTextField.getText());

        String sumSitString = sumSitTextField.getText();
        Double sumSit = null;
        try {
            if (sumSitString!=null&&!sumSitString.isEmpty()) sumSit = Double.parseDouble(sumSitString);
        } catch (NumberFormatException e) {
            log.error("Error parsing SumSitConsumption: " + e.getMessage());
        }
        norm.setSumSitConsumptionRate(sumSit);

        String sumVilString = sumVilTextField.getText();
        Double sumVil = null;
        try {
            if (sumVilString!=null && !sumVilString.isEmpty()) sumVil = Double.parseDouble(sumVilString);
        } catch (NumberFormatException e) {
            log.error("Error parsing SumVilConsumption: " + e.getMessage());
        }
        norm.setSumVilConsumptionRate(sumVil);
        
        String winSitString = winSitTextField.getText();
        Double winSit = null;
        try {
            if (winSitString!=null && !winSitString.isEmpty()) winSit = Double.parseDouble(winSitString);
        } catch (NumberFormatException e) {
            log.error("Error parsing WinSitConsumption: " + e.getMessage());
        }
        norm.setWinSitConsumptionRate(winSit);

        String winVilString = winVilTextField.getText();
        Double winVil = null;
        try {
            if (winVilString!=null && !winVilString.isEmpty()) winVil = Double.parseDouble(winVilString);
        } catch (NumberFormatException e) {
            log.error("Error parsing WinVilConsumption: " + e.getMessage());
        }
        norm.setWinVilConsumptionRate(winVil);

        String idleString = idleTextField.getText();
        Double idle = null;
        try {
            if (idleString!=null && !idleString.isEmpty()) idle = Double.parseDouble(idleString);
        } catch (NumberFormatException e) {
            log.error("Error parsing IdleConsumption: " + e.getMessage());
        }
        norm.setIdleConsumptionRate(idle);

        String specString = specTextField.getText();
        Double spec = null;
        try {
            if (specString!=null && !specString.isEmpty()) spec = Double.parseDouble(specString);
        } catch (NumberFormatException e) {
            log.error("Error parsing SpecConsumption: " + e.getMessage());
        }
        norm.setSpecConsumptionRate(spec);

        norm.setNoSpecSummer(noSpecSummerCheckBox.isSelected());

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
        log.debug("Saving norm " + norm.toString());
        routeFormsService.saveNorm(norm);

    }


}
