package org.kesler.mfc.routeforms.client.gui.options;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Window;
import org.kesler.mfc.routeforms.client.gui.AbstractItemController;
import org.kesler.mfc.routeforms.client.security.OptionsHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.util.SpringOptionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

@Component
public class ConnectOptionsController extends AbstractItemController implements Initializable{

    @FXML protected TextField serverUrlTextField;

    private Properties fileOptions;

    @Autowired protected RouteFormsService routeFormsService;
    @Autowired protected OptionsHolder optionsHolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverUrlTextField.textProperty().addListener(invalidationListener);
    }

    @Override
    public void showAndWait(Window owner) {
        loadOptions();
        Image icon = new Image(this.getClass().getResourceAsStream("/images/server_connect.png"));
        super.showAndWait(owner, "Настройки соединения", icon);
    }


    @Override
    protected void updateContent() {
        if (fileOptions==null) return;

        String serverUrl = fileOptions.getProperty("server.url");
        serverUrlTextField.setText(serverUrl == null ? "" : serverUrl);

    }



    @Override
    protected void afterUpdatingContent() {
        setButtonSet(ButtonSet.SAVE_CANCEL);
    }

    @Override
    protected void updateResult() {

        if (fileOptions==null) return;

        String serverUrl = serverUrlTextField.getText();
        fileOptions.setProperty("server.url", serverUrl == null ? "" : serverUrl);

    }


    @Override
    protected void handleClose() {
        optionsHolder.setServerUrl(fileOptions.getProperty("server.url"));
        super.handleClose();
    }

    @Override
    protected void saveAsync() {
        SpringOptionsUtil.saveOptions(fileOptions);
    }


    private void loadOptions() {
        OptionsLoader optionsLoader = new OptionsLoader();
        new Thread(optionsLoader).start();
    }

    class OptionsLoader extends Task<Void> {
        private final Logger log = LoggerFactory.getLogger(this.getClass());
        @Override
        protected Void call() throws Exception {
            log.info("Loadind file options..");
            fileOptions = SpringOptionsUtil.loadOptions();
            if (fileOptions.size() == 0) {
                fileOptions = SpringOptionsUtil.getDefaultOptions();
            }
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            log.info("Loadind options complete..");
            updateContent();
        }
    }
}
