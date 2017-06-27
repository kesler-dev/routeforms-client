package org.kesler.mfc.routeforms.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.gui.main.MainController;
import org.kesler.mfc.routeforms.client.update.AppUpdater;
import org.kesler.mfc.routeforms.client.util.SpringOptionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Properties;

public class RouteFormsApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(RouteFormsApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        log.info("Starting RouteForms application");

        Properties options = SpringOptionsUtil.loadOptions();
        if (options.size() == 0 || !options.containsKey("server.url")) {
            log.debug("Options not found: creating default..");
            options = SpringOptionsUtil.getDefaultOptions();
            SpringOptionsUtil.saveOptions(options);
        }

        AnnotationConfigApplicationContext context;
        try {
            context = new AnnotationConfigApplicationContext(RouteFormsAppFactory.class);
        } catch (Exception e) {
            Throwable cause = getCause(e);
            log.error("Error while open application", cause);
            stage.setScene(new Scene(new VBox(), 10, 10));
            stage.show();
                Dialogs.create()
                        .owner(stage)
                        .title("Учет путевых листов")
                        .message("Ошибка при отрытии приложения. Обратитесь к системному администратору")
                        .showException(e);

            stage.hide();
            Platform.exit();
            System.exit(0);
            return;
        }
        context.registerShutdownHook();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                log.debug("Main stage hide - closing app..");
                context.close();
                Platform.exit();
                System.exit(0);
            }
        });

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                log.debug("Main stage hide - closing app..");
                context.close();
                Platform.exit();
                System.exit(0);
            }
        });

        MainController mainController = context.getBean(MainController.class);
        mainController.showMain(stage,
                "Учет путевых листов",
                new Image(RouteFormsApp.class.getResourceAsStream("/images/book_blue.png")),
                800, 650
        );

//        AppUpdater appUpdater = new AppUpdater(stage);
//        appUpdater.checkForUpdates();
    }

    Throwable getCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;

        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }
}
