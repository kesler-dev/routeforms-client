package org.kesler.mfc.routeforms.client.export.report.branch;

import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.Driver;
import org.kesler.mfc.routeforms.client.domain.FuellingType;
import org.kesler.mfc.routeforms.client.domain.RouteForm;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

/**
 * Производит необходимые расчеты
 */
@Component
public class BranchReportService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected RouteFormsService routeFormsService;

    public BranchReport create(Auto auto, Driver driver, LocalDate date) throws Exception{
        log.info("Creating new report..");
        BranchReport branchReport = new BranchReport(auto, driver, date);

        LocalDate begDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());

        Collection<RouteForm> routeForms = routeFormsService.findRouteFormsByAutoAndDates(auto, begDate, endDate);

        if (routeForms.size() == 0) {
            throw new EmptyListBranchReportServiceException("Список путевых листов пуст");
        }

        Iterator<RouteForm> rfIterator = routeForms.iterator();
        while (rfIterator.hasNext()) {
            RouteForm routeForm = rfIterator.next();
            if (!routeForm.getState().equals(RouteForm.State.READY)) rfIterator.remove();
        }

        List<RouteForm> sortedRouteForms = new ArrayList<>(routeForms);
        sortedRouteForms.sort(new Comparator<RouteForm>() {
            @Override
            public int compare(RouteForm o1, RouteForm o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        log.debug("Collected " + sortedRouteForms.size() + " routeForms");

        RouteForm firstRouteForm = sortedRouteForms.get(0);
        branchReport.beginOdo = firstRouteForm.getDepartureODO()==null?0:firstRouteForm.getDepartureODO();
        branchReport.beginFuel = firstRouteForm.getDepartureFuel()==null?0:firstRouteForm.getDepartureFuel();

        RouteForm lastRouteForm = sortedRouteForms.get(sortedRouteForms.size()-1);
        branchReport.endOdo = lastRouteForm.getCombackODO()==null?0:lastRouteForm.getCombackODO();
        branchReport.endFuel = lastRouteForm.getCombackFuel()==null?0:lastRouteForm.getCombackFuel();

        long mileage;
        double fuelling;
        long consumption=0;

        for (RouteForm routeForm : routeForms) {
//            if (!routeForm.getState().equals(RouteForm.State.READY)) continue;
            branchReport.routeFormsCount++;
            log.debug("RouteForm: " + routeForm.getSeriesAndNumber() + " " + routeForm.getDate());
            log.debug("--departFuel: " + routeForm.getDepartureFuel());
            log.debug("--combackFuel: " + routeForm.getCombackFuel());
            mileage = routeForm.getMileage()==null?0:routeForm.getMileage();
            branchReport.totalOdo += mileage;
            switch (routeForm.getModeType()) {
                case SITY:
                    branchReport.sityOdo += mileage;
                    log.debug("--sity mileage: " + mileage);
                    break;
                case VILAGE:
                    branchReport.vilOdo += mileage;
                    log.debug("--village mileage: " + mileage);
                    break;
            }
            branchReport.idleTime += routeForm.getIdleTime()==null?0:routeForm.getIdleTime();
            branchReport.specTime += routeForm.getSpecTime()==null?0:routeForm.getSpecTime();


            fuelling = routeForm.getFuelling()==null?0:routeForm.getFuelling();
            branchReport.totalFueling += fuelling;
            FuellingType fuellingType = routeForm.getFuellingType()==null?FuellingType.CARD:routeForm.getFuellingType();
            switch (fuellingType) {
                case CARD:
                    branchReport.cardFueling += fuelling;
                    log.debug("--card fuelling: " + mileage);
                    break;
                case CACHE:
                    branchReport.cacheFueling += fuelling;
                    log.debug("--cache fuelling: " + mileage);
                    break;
            }

            log.debug("--consumption: " + routeForm.getConsumption());
            consumption += routeForm.getConsumption()==null?0:Math.round(routeForm.getConsumption()*10);

            log.debug("--consumption rounded: " + consumption);

        }

        branchReport.consumedFuel = consumption * 1.0/10;

        log.debug("--consumption total: " + branchReport.consumedFuel);

        log.info("Report created");

        return branchReport;
    }
}
