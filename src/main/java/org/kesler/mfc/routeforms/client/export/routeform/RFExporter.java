package org.kesler.mfc.routeforms.client.export.routeform;

import org.kesler.mfc.routeforms.client.domain.RouteForm;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Created by alex on 29.06.15.
 */
@Component
public abstract class RFExporter extends org.kesler.mfc.routeforms.client.export.Exporter {

    protected RouteForm routeForm;

    public void exportRouteForm(RouteForm routeForm) throws Exception{
        this.routeForm = routeForm;

        String outFilePath = getOutDir() +
                "Путевой лист " +
                routeForm.getAuto().toString() +
                " - " +
                routeForm.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                ".xlsx";

        prepareAndSaveXlsx(outFilePath);
        openFile(outFilePath);
    }


}
