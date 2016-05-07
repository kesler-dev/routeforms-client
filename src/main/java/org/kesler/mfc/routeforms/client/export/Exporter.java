package org.kesler.mfc.routeforms.client.export;

import org.kesler.mfc.routeforms.client.export.routeform.support.ExporterException;
import org.kesler.mfc.routeforms.client.security.OptionsHolder;
import org.kesler.mfc.routeforms.client.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by alex on 25.09.15.
 */
public abstract class Exporter {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected OptionsHolder optionsHolder;

    protected InputStream getTemplateInputStream(byte[] data) throws Exception{
        if (data==null) throw new ExporterException("Шаблон для не загружен");
        InputStream inputStream = new ByteArrayInputStream(data);
        return inputStream;
    }

    protected String getOutDir() {
        String jarPath = FileUtils.getJarPath();
        String outDir = new File(jarPath) + File.separator + "out" + File.separator;

        File outDirFile = new File(outDir);
        if (!outDirFile.exists()) outDirFile.mkdir();

        return outDir;
    }

    protected void openFile(String filePath) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        } else {
            return;
        }

        //Открытие файла:
        try {
            desktop.open(new File(filePath));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    protected abstract void prepareAndSaveXlsx(String outFilePath) throws Exception;
}
