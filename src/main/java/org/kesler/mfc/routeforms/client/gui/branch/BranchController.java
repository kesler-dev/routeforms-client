package org.kesler.mfc.routeforms.client.gui.branch;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Window;
import org.kesler.mfc.routeforms.client.domain.Branch;
import org.kesler.mfc.routeforms.client.gui.AbstractItemController;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.util.LongTextFieldChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер редактирования филиала
 */
@Component
public class BranchController extends AbstractItemController implements Initializable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired private RouteFormsService routeFormsService;


    @FXML protected TextField nameTextField;
    @FXML protected TextField seriesTextField;
    @FXML protected TextField curNumberTextField;

    private Branch branch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTextField.textProperty().addListener(invalidationListener);
        seriesTextField.textProperty().addListener(invalidationListener);
        curNumberTextField.textProperty().addListener(invalidationListener);

        curNumberTextField.textProperty().addListener(new LongTextFieldChangeListener());
    }


    public void showAndWait(Window owner, Branch branch) {
        this.branch = branch;
        nameTextField.requestFocus();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/office-building.png"));
        super.showAndWait(owner, "Цех", icon);
    }


    @Override
    protected void afterUpdatingContent() {
        if (branch.getId() == null) {
            setButtonSet(ButtonSet.SAVE_CANCEL);
        } else {
            setButtonSet(ButtonSet.CLOSE);
        }
    }

    @Override
    protected void updateContent() {
        nameTextField.setText(branch.getName());
        seriesTextField.setText(branch.getSeries());
        curNumberTextField.setText(String.format("%06d",branch.getRouteFormCounter()));
    }

    @Override
    protected void updateResult() {
        branch.setName(nameTextField.getText());
        branch.setSeries(seriesTextField.getText());
        Integer curNumber = null;
        try {
            curNumber = Integer.parseInt(curNumberTextField.getText());
        } catch (NumberFormatException e) { }
        branch.setRouteFormCounter(curNumber);
    }

    @Override
    protected void saveAsync() {
        log.debug("Saving branch " + branch.getName());
        routeFormsService.saveBranch(branch);

    }

}
