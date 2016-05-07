package org.kesler.mfc.routeforms.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;


public abstract class SpringOptionsUtil {
    private static final Logger log = LoggerFactory.getLogger(SpringOptionsUtil.class);

    public static Properties loadOptions() {
        Properties options = new Properties();

        String configFilePath = FileUtils.getJarPath()+"config"+ File.separator+"RouteForms.properties";


        try(FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
            options.load(fileInputStream);
        } catch (IOException e) {
            log.warn("Error reading properties file: " + e);
        }

        return options;
    }

    public static void saveOptions(Properties options) {
        String configDirPath = FileUtils.getJarPath()+"config" + File.separator;
        String configFilePath = configDirPath + "RouteForms.properties";

        File configDir = new File(configDirPath);
        File configFile = new File(configFilePath);

        try {
            if (!configDir.exists())
                configDir.mkdir();
            if (!configFile.exists())
                configFile.createNewFile();
        } catch (IOException e) {
            log.error("Error creating properties file: " + e, e);
            return;
        }

        try(FileOutputStream fileOutputStream = new FileOutputStream(configFile)) {
            options.store(fileOutputStream,"Changed: " + new Date().toString());
        } catch (IOException e) {
            log.error("Error writing properties file: " + e, e);
        }
    }

    public static Properties getDefaultOptions() {
        Properties properties = new Properties();

        properties.setProperty("server.url","http://10.10.0.170:8080/routeforms/api");


        return properties;
    }
}
