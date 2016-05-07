package org.kesler.mfc.routeforms.client.update;

import javafx.concurrent.Task;
import javafx.stage.Window;
import jupar.Downloader;
import jupar.Updater;
import jupar.objects.Modes;
import jupar.objects.ProgressListener;
import jupar.objects.Release;
import jupar.parsers.ReleaseXMLParser;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.kesler.mfc.routeforms.client.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обновляльщик
 */
public class AppUpdater {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Window owner;

    public AppUpdater(Window owner) {
        this.owner = owner;
    }

    public void checkForUpdates() {
        log.info("Checking for updates..");
        new Thread(new Checker()).start();
    }


    protected void update() {
        log.info("Updating..");
        UpdaterTask updaterTask = new UpdaterTask();

        Dialogs.create()
                .owner(owner)
                .lightweight()
                .styleClass(Dialog.STYLE_CLASS_UNDECORATED)
                .title("Обновление")
                .showWorkerProgress(updaterTask);
        Thread daemon = new Thread(updaterTask);
        daemon.setDaemon(true);
        daemon.start();
    }

    class Checker extends Task<Release> {
        @Override
        protected Release call() throws Exception {

            ReleaseXMLParser parser = new ReleaseXMLParser();
            return parser.parse("http://10.10.0.118/routeforms/latest.xml", Modes.URL);

        }

        @Override
        protected void succeeded() {
            super.succeeded();

            Release current = new Release();
            current.setpkgver(Version.getVersion());
            current.setPubDate(Version.getReleaseDate());

            Release avail = getValue();



            if (avail.compareTo(current)>0) {

                Dialogs.create()
                        .owner(owner)
                        .title("Доступно обновление")
                        .message("Доступна новая версия приложения: \n" +
                                avail.getpkgver() + " от " + avail.getPubDate() + "\n" +
                                avail.getMessage() + "\n\n" +
                                "Приложение будет обновлено.")
                        .showInformation();
                update();

//                Action response = Dialogs.create()
//                        .owner(owner)
//                        .title("Доступно обновление")
//                        .message("Доступна новая версия приложения: \n" +
//                                avail.getpkgver() + " от " + avail.getPubDate() + "\n" +
//                                avail.getMessage() + "\n\n" +
//                                "Обновить?")
//                        .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
//                        .showConfirm();
//                if (response == Dialog.ACTION_YES) {
//                    update();
//                }
            }
        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            log.warn("Error checking updates "+ ex.getMessage());
        }
    }


    class UpdaterTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            log.debug("Downloading...");
            Downloader downloader = new Downloader();
            downloader.addProgressListener(new ProgressListener() {
                @Override
                public void update(int i, int total, String s) {
                    log.debug(s);
                    updateProgress(i, total);
                    updateMessage(s);
                }
            });
            downloader.download("http://10.10.0.118/routeforms/files.xml", "tmp", Modes.URL);

            log.debug("Updating...");
            Updater updater = new Updater();
            updater.addProgressListener(new ProgressListener() {
                @Override
                public void update(int i, int total, String s) {
                    log.debug(s);
                    updateProgress(i, total);
                    updateMessage(s);
                }
            });
            updater.update("update.xml", "tmp", Modes.FILE);

            log.debug("Removing tmp...");
            updater.delete("tmp");

            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            log.info("Updating complete");
            Dialogs.create()
                    .owner(owner)
                    .title("Успешно")
                    .message("Обновление завершено. Перезапустите приложение")
                    .showInformation();
            owner.hide();

        }

        @Override
        protected void failed() {
            super.failed();
            Throwable ex = getException();
            log.warn("Error downloading updates " + ex.getMessage(), ex);
            Dialogs.create()
                    .owner(owner)
                    .title("Обновление")
                    .message("Ошибка при обновлении")
                    .showException(ex);

        }

    }


}
