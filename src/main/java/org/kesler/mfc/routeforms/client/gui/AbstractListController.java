package org.kesler.mfc.routeforms.client.gui;

import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.stage.Window;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Контроллер для окна со списком
 */
public abstract class AbstractListController<T> extends AbstractController {
    protected final static Logger log = LoggerFactory.getLogger(AbstractListController.class);

    protected T selectedItem;
    protected boolean select;

    @FXML protected ToolBar okCancelButtonToolBar;
    @FXML protected ToolBar closeButtonToolBar;
    @FXML protected ProgressIndicator updateProgressIndicator;

    public T getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void show(Window owner) {
        log.info("Show dialog");
        select=false;
        switchToolBar();
        super.show(owner);
    }

    @Override
    public void show(Window owner, String title) {
        log.info("Show dialog with title " + title);
        select=false;
        switchToolBar();
        super.show(owner, title);
    }

    @Override
    public void show(Window owner, String title, Image icon) {
        log.info("Show dialog with title " + title + " and icon");
        select=false;
        switchToolBar();
        super.show(owner, title, icon);
    }

    @Override
    public void showAndWait(Window owner) {
        log.info("Show and wait dialog");
        select=false;
        switchToolBar();
        super.showAndWait(owner);    }

    @Override
    public void showAndWait(Window owner, String title) {
        log.info("Show and wait dialog with title " + title);
        select=false;
        switchToolBar();
        super.showAndWait(owner, title);
    }

    @Override
    public void showAndWait(Window owner, String title, Image icon) {
        log.info("Show and wait dialog with title " + title + " and icon");
        select=false;
        switchToolBar();
        super.showAndWait(owner, title, icon);
    }


    public void showAndWaitSelect(Window owner) {
        log.info("Show and wait select dialog");
        select=true;
        switchToolBar();
        super.showAndWait(owner);
    }

    public void showAndWaitSelect(Window owner, String title) {
        log.info("Show and wait select dialog with title " + title);
        select=true;
        switchToolBar();
        super.showAndWait(owner, title);
    }

    public void showAndWaitSelect(Window owner, String title, Image icon) {
        log.info("Show and wait select dialog with title " + title + " and icon");
        select=true;
        switchToolBar();
        super.showAndWait(owner, title, icon);
    }


    private void switchToolBar () {
        if (okCancelButtonToolBar==null || closeButtonToolBar==null) return;

        if (select) {
            log.debug("Switch ButtonToolbar to Select");
            okCancelButtonToolBar.setVisible(true);
            closeButtonToolBar.setVisible(false);
        } else {
            log.debug("Switch ButtonToolbar to Close");
            okCancelButtonToolBar.setVisible(false);
            closeButtonToolBar.setVisible(true);
        }
    }


    protected final void updateItemsAndSelect(T item) {
        UpdateListTask updateListTask = new UpdateListTask(item);

        BooleanBinding runningBinding = updateListTask.stateProperty().isEqualTo(Task.State.RUNNING);
        updateProgressIndicator.visibleProperty().bind(runningBinding);

        new Thread(updateListTask).start();
    }

    protected final void removeItem(T item) {
        RemoveTask removeTask = new RemoveTask(item);
        BooleanBinding runningBinding = removeTask.stateProperty().isEqualTo(Task.State.RUNNING);
        updateProgressIndicator.visibleProperty().bind(runningBinding);

        new Thread(removeTask).start();
    }

    @Override
    protected void updateContent() {
        updateItemsAndSelect(null);
    }

    protected abstract Collection<T> updateListAsync();

    protected abstract void updatingListComplete(Collection<T> items, T selectedItem);

    protected abstract void removeItemAsync(T item);

    /// Классы для обновления и сохранения данных в отдельном потоке

    class UpdateListTask extends Task<Collection<T>> {
        private final Logger log = LoggerFactory.getLogger(this.getClass());

        private T selectedItem = null;

        public UpdateListTask(T selectedItem) {
            this.selectedItem = selectedItem;
        }

        @Override
        protected Collection<T> call() throws Exception {
            log.debug("Updating list...");

            Collection<T> items = updateListAsync();
            log.debug("Server return " + items.size() + " items");
            return items;
        }

        @Override
        protected void succeeded() {
            Collection<T> items = getValue();

            updatingListComplete(items, selectedItem);
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



    class RemoveTask extends Task<Void> {
        private final Logger log = LoggerFactory.getLogger(this.getClass());
        private final T item;

        RemoveTask(T item) {
            this.item = item;
        }
        @Override
        protected Void call() throws Exception {
            log.debug("Removing item ");
            removeItemAsync(item);
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();

            updateItemsAndSelect(null);

            log.info("Removing item complete");
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable exception = getException();
            log.error("Error removing item: " + exception, exception);
            Dialogs.create()
                    .owner(stage)
                    .title("Ошибка")
                    .message("Ошибка при удалении: " + exception)
                    .showException(exception);
        }
    }

}
