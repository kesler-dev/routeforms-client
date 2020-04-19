package org.kesler.mfc.routeforms.client.export.report.branch;

import org.kesler.mfc.routeforms.client.domain.Auto;
import org.kesler.mfc.routeforms.client.domain.Driver;
import org.kesler.mfc.routeforms.client.domain.FuelType;

import java.time.LocalDate;

/**
 * Created by alex on 25.09.15.
 */
public class BranchReport {

    private Auto auto;
    private Driver driver;
    private LocalDate date;

    public BranchReport(Auto auto, Driver driver, LocalDate date) {
        this.auto = auto;
        this.driver = driver;
        this.date = date;

        fuelType = auto.getFuelType();
    }

    public Auto getAuto() { return auto; }
    public Driver getDriver() { return driver; }
    public LocalDate getDate() { return date; }


    public long beginOdo;
    public long endOdo;

    public long totalOdo;
    public double idleTime;
    public double specTime;


    public FuelType fuelType;
    public double beginFuel;
    public double totalFueling;
    public double cardFueling;
    public double cacheFueling = 0.0;
    public double consumedFuel = 0.0;
    public double endFuel;

    public int routeFormsCount;

}
