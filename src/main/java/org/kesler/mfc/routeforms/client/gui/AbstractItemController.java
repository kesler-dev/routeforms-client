package org.kesler.mfc.routeforms.client.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToolBar;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.validation.ValidationSupport;

/**
 * Created by alex on 10.06.15.
 */
public abstract class AbstractItemController extends AbstractController {

    @FXML protected ToolBar closeButtonToolBar;
    @FXML protected ToolBar okCancelButtonToolBar;
    @FXML protected ToolBar saveCancelButtonToolBar;
    @FXML protected ProgressIndicator saveProgressIndicator;


    protected FieldsInvalidationListener invalidationListener = new FieldsInvalidationListener();

    protected ValidationSupport validationSupport = new ValidationSupport();


    @FXML protected void handleSaveButtonAction(ActionEvent ev) {
        handleSave();
    }

    @FXML protected void handleCancelSaveButtonAction(ActionEvent ev) {
        handleCancelSave();
    }


    protected void save() {
        SaveTask saveTask = new SaveTask();

        BooleanBinding runningBinding = saveTask.stateProperty().isEqualTo(Task.State.RUNNING);
        saveProgressIndicator.visibleProperty().bind(runningBinding);

        log.info("Saving item..");
        new Thread(saveTask).start();

    }

    protected void setButtonSet(ButtonSet buttonSet) {
        switch (buttonSet) {
            case OK_CANCEL:
                okCancelButtonToolBar.setVisible(true);
                saveCancelButtonToolBar.setVisible(false);
                closeButtonToolBar.setVisible(false);
                break;
            case SAVE_CANCEL:
                okCancelButtonToolBar.setVisible(false);
                saveCancelButtonToolBar.setVisible(true);
                closeButtonToolBar.setVisible(false);
                break;
            case CLOSE:
                okCancelButtonToolBar.setVisible(false);
                saveCancelButtonToolBar.setVisible(false);
                closeButtonToolBar.setVisible(true);
                break;
        }
    }

    protected void handleSave() {
        log.info("Handle Save action");
        updateResult();
        if (validation())
            save();
    }

    protected boolean validation() {
        if (validationSupport.isInvalid()) {
            validationSupport.redecorate();
            Notifications.create()
                    .owner(stage)
                    .position(Pos.CENTER)
                    .text(validationSupport.getValidationResult().getMessages().iterator().next().getText())
                    .hideAfter(Duration.millis(1200))
                    .showWarning();
            return false;
        }
        return true;
    }

    protected void handleCancelSave() {
        log.info("Handle Cancel Save action");
        hideStage();
    }


    /**
     * Сохраняет в отдельном потоке
     */
    protected abstract void saveAsync();

    class SaveTask extends Task<Void> {


        public SaveTask() { }

        @Override
        protected Void call() throws Exception {
            saveAsync();
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            log.info("Saving complete.");

            updateContent();

            setButtonSet(ButtonSet.CLOSE);

            Notifications.create()
                    .owner(stage)
                    .position(Pos.CENTER)
                    .text("Сохранено")
                    .hideAfter(Duration.millis(500))
                    .showInformation();
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable exception = getException();
            log.error("Error saving: " + exception, exception);
            Dialogs.create()
                    .owner(stage)
                    .title("Ошибка")
                    .message("Ошибка при сохранении: " + exception)
                    .showException(exception);

        }
    }

    public  class FieldsInvalidationListener implements InvalidationListener {
        @Override
        public void invalidated(Observable observable) {
            setButtonSet(ButtonSet.SAVE_CANCEL);
        }
    }

}
