package org.kesler.mfc.routeforms.client;

/**
 * Класс для хранения версии приложения
 */
public abstract class Version {

    private static String version = "2.0.0.1";
    private static String releaseDate = "10.05.2016";

    public static String getVersion() {
        return version;
    }

    public static String getReleaseDate() {
        return releaseDate;
    }
}
