package org.kesler.mfc.routeforms.client.util;

/**
 * Created by alex on 06.10.15.
 */
public abstract class Converter {
    public static Double getDouble(String string) {
        if (string==null || string.isEmpty()) return null;
        Double value = Double.parseDouble(string);
        return round(value);
    }

    public static Double round(Double value) {
        if (value==null) return null;
        value = Math.floor(value*10+0.5) /10;
        return value;
    }

    public static Long getLong(String string) {
        if (string==null || string.isEmpty()) return null;
        Long value = Long.parseLong(string);
        return value;
    }


}
