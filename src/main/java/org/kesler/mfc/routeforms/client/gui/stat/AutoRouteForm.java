package org.kesler.mfc.routeforms.client.gui.stat;

import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.RouteForm;

/**
 * Created by alex on 29.10.15.
 */
public class AutoRouteForm {
    private Auto auto;
    private RouteForm routeForm;

    public AutoRouteForm(Auto auto, RouteForm routeForm) {
        this.auto = auto;
        this.routeForm = routeForm;
    }

    public String getAutoString() {return auto==null?"":auto.toString();}

    public String getRouteFormSeriesAndNumber() {
        return routeForm==null?"":routeForm.getSeriesAndNumber();
    }

    public RouteForm.State getRouteFormState() {
        return routeForm==null?null:routeForm.getState();
    }

    public String getMileage() {
        return routeForm==null?"":routeForm.getMileage()==null?"":routeForm.getMileage().toString();
    }

    public String getEmployeeString() {
        return routeForm==null?"":routeForm.getEmployee().toString();
    }

}
