package org.kesler.mfc.routeforms.client.gui.driver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Window;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.domain.Driver;
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
 * Контроллер редактирования водителя
 */
@Component
public class DriverController extends AbstractItemController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired protected RouteFormsService routeFormsService;
    @Autowired protected BranchListController branchListController;

    @FXML protected TextField fioFullTextField;
    @FXML protected TextField fioShortTextField;
    @FXML protected TextField driverLicenseTextField;
    @FXML protected TextField driverCategoryTextField;
    @FXML protected TextField tabelNumTextField;
    @FXML protected TextField branchTextField;
    @FXML protected CheckBox activeCheckBox;

    private Driver driver;

    private Branch branch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fioFullTextField.textProperty().addListener(invalidationListener);
        fioShortTextField.textProperty().addListener(invalidationListener);
        driverLicenseTextField.textProperty().addListener(invalidationListener);
        driverCategoryTextField.textProperty().addListener(invalidationListener);
        tabelNumTextField.textProperty().addListener(invalidationListener);
        branchTextField.textProperty().addListener(invalidationListener);
        activeCheckBox.selectedProperty().addListener(invalidationListener);
    }


    public void showAndWait(Window owner, Driver driver) {
        this.driver = driver;
        fioFullTextField.requestFocus();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/pilot2.png"));
        super.showAndWait(owner, "Водитель", icon);
    }


    @Override
    protected void afterUpdatingContent() {
        if (driver.getId() == null) {
            setButtonSet(ButtonSet.SAVE_CANCEL);
        } else {
            setButtonSet(ButtonSet.CLOSE);
        }
    }

    @Override
    protected void updateContent() {
        fioFullTextField.setText(driver.getFioFull());
        fioShortTextField.setText(driver.getFioShort());
        driverLicenseTextField.setText(driver.getDriverLicense());
        driverCategoryTextField.setText(driver.getDriverCategory());
        tabelNumTextField.setText(driver.getTabelNum());
        branch = driver.getBranch();
        branchTextField.setText(branch==null?"":branch.toString());
        activeCheckBox.setSelected(driver.isActive()==null?false:driver.isActive());
    }

    @Override
    protected void updateResult() {
        driver.setFioFull(fioFullTextField.getText());
        driver.setFioShort(fioShortTextField.getText());
        driver.setDriverLicense(driverLicenseTextField.getText());
        driver.setDriverCategory(driverCategoryTextField.getText());
        driver.setTabelNum(tabelNumTextField.getText());
        driver.setBranch(branch);
        driver.setActive(activeCheckBox.isSelected());
    }

    @Override
    protected void saveAsync() {
        log.debug("Saving driver " + driver.getFioShort());
        routeFormsService.saveDriver(driver);

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

}
