package org.kesler.mfc.routeforms.client.service;

import org.kesler.mfc.routeforms.client.domain.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

/**
 * Единая точка входа для всех запросов к даннным
 */
public interface RouteFormsService {

    Collection<Branch> findBranches();
    void saveBranch(Branch branch);
    void removeBranch(Branch branch);

    Collection<Employee> findEmployees();
    Collection<Employee> findActiveEmployees();
    void saveEmployee(Employee employee);
    void removeEmployee(Employee employee);

    Collection<Driver> findDrivers();
    Collection<Driver> findActiveDriversByBranch(Branch branch);
    void saveDriver(Driver driver);
    void removeDriver(Driver driver);

    Collection<Auto> findAutos();
    Collection<Auto> findAutosByBranch(Branch branch);
    void saveAuto(Auto auto);
    void removeAuto(Auto auto);

    Collection<Norm> findNorms();
    void saveNorm(Norm norm);
    void removeNorm(Norm norm);


    Collection<RouteForm> findRouteForms();
    Collection<RouteForm> findRouteFormsByDates(LocalDate begDate, LocalDate endDate);
    Collection<RouteForm> findRouteFormsByBegDate(LocalDate begDate);
    RouteForm findRouteFormById(UUID id);
    Collection<RouteForm> findRouteFormsByAuto(Auto auto);
    Collection<RouteForm> findRouteFormsByAutoAndDates(Auto auto, LocalDate begDate, LocalDate endDate);
    Collection<RouteForm> findRouteFormsByBranch(Branch branch);
    Collection<RouteForm> findRouteFormsByBranchAndDates(Branch branch, LocalDate begDate, LocalDate endDate);
    Collection<RouteForm> findRouteFormsByBranchAndBegDate(Branch branch, LocalDate begDate);
    void saveRouteForm(RouteForm routeForm);
    void removeRouteForm(RouteForm routeForm);

    ApplicationOptions loadOptions();
    void saveOptions(ApplicationOptions applicationOptions);
    Norm.SeasonType getSeasonForDate(LocalDate date);

}
