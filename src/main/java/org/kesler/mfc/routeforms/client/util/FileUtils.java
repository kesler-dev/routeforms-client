package org.kesler.mfc.routeforms.client.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;


public class FileUtils {

    public static String getJarPath() {

        URL url = FileUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = null;

        try {
            jarPath = URLDecoder.decode(url.getFile(), "UTF-8"); //Should fix it to be read correctly by the system
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String parentPath = new File(jarPath).getParentFile().getPath(); //Path of the jar
        parentPath = parentPath + File.separator;

        return parentPath;

    }


}
