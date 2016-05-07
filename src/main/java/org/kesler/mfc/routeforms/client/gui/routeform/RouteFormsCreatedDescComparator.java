package org.kesler.mfc.routeforms.client.gui.routeform;

import org.kesler.mfc.routeforms.client.domain.RouteForm;

import java.util.Comparator;

/**
 * Created by alex on 09.07.15.
 */
public class RouteFormsCreatedDescComparator implements Comparator<RouteForm> {
    @Override
    public int compare(RouteForm o1, RouteForm o2) {
        return o2.getCreated().compareTo(o1.getCreated());
    }
}
