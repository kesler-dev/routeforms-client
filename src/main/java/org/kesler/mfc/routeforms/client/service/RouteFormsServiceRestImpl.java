package org.kesler.mfc.routeforms.client.service;

import org.kesler.mfc.routeforms.client.domain.*;
import org.kesler.mfc.routeforms.client.security.OptionsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by alex on 04.05.16.
 */
@Service
public class RouteFormsServiceRestImpl implements RouteFormsService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired protected RestTemplate restTemplate;
    @Autowired protected OptionsHolder optionsHolder;

    // Branches

    @Override
    public Collection<Branch> findBranches() {
        log.info("Getting branches..");
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/branches")
                .build().toUri();
        log.debug("URI: " + uri);
        Branch[] branchArray = restTemplate.getForObject(uri, Branch[].class);
        ArrayList<Branch> branches = new ArrayList<>(branchArray.length);
        Collections.addAll(branches,branchArray);
        log.info("Got "+ branches.size() + " branches");
        return branches;
    }

    @Override
    public void saveBranch(Branch branch) {
        log.info("Saving branch " + branch);
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/branches")
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.postForObject(uri, branch, Branch.class);
    }

    @Override
    public void removeBranch(Branch branch) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/branches")
                .path("/"+branch.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.delete(uri);
    }

    // Employees

    @Override
    public Collection<Employee> findEmployees() {
        log.info("Getting employees..");
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/employees")
                .build().toUri();
        log.debug("URI: " + uri);
        Employee[] employeeArray = restTemplate.getForObject(uri, Employee[].class);
        ArrayList<Employee> employees = new ArrayList<>(employeeArray.length);
        Collections.addAll(employees,employeeArray);
        return employees;
    }

    @Override
    public Collection<Employee> findActiveEmployees() {
        log.info("Getting active employees..");
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/employees")
                .path("/active")
                .build().toUri();
        log.debug("URI: " + uri);
        Employee[] employeeArray = restTemplate.getForObject(uri, Employee[].class);
        ArrayList<Employee> employees = new ArrayList<>(employeeArray.length);
        Collections.addAll(employees,employeeArray);
        return employees;
    }

    @Override
    public void saveEmployee(Employee employee) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/employees")
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.postForObject(uri, employee, Employee.class);
    }

    @Override
    public void removeEmployee(Employee employee) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/employees")
                .path("/"+employee.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.delete(uri);
    }

    /// Drivers

    @Override
    public Collection<Driver> findDrivers() {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/drivers")
                .build().toUri();
        log.debug("URI: " + uri);
        Driver[] driversArray = restTemplate.getForObject(uri, Driver[].class);
        ArrayList<Driver> drivers = new ArrayList<>(driversArray.length);
        Collections.addAll(drivers,driversArray);
        return drivers;
    }

    @Override
    public Collection<Driver> findActiveDriversByBranch(Branch branch) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/drivers")
                .path("/active")
                .path("/by-branch")
                .path("/"+branch.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        Driver[] driversArray = restTemplate.getForObject(uri, Driver[].class);
        ArrayList<Driver> drivers = new ArrayList<>(driversArray.length);
        Collections.addAll(drivers,driversArray);
        return drivers;
    }

    @Override
    public void saveDriver(Driver driver) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/drivers")
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.postForObject(uri, driver, Driver.class);
    }

    @Override
    public void removeDriver(Driver driver) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/drivers")
                .path("/"+driver.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.delete(uri);
    }

    // Autos

    @Override
    public Collection<Auto> findAutos() {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/autos")
                .build().toUri();
        log.debug("URI: " + uri);
        Auto[] autosArray = restTemplate.getForObject(uri, Auto[].class);
        ArrayList<Auto> autos = new ArrayList<>(autosArray.length);
        Collections.addAll(autos,autosArray);
        return autos;
    }

    @Override
    public Collection<Auto> findAutosByBranch(Branch branch) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/autos")
                .path("/by-branch")
                .path("/" + branch.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        Auto[] autosArray = restTemplate.getForObject(uri, Auto[].class);
        ArrayList<Auto> autos = new ArrayList<>(autosArray.length);
        Collections.addAll(autos,autosArray);
        return autos;
    }

    @Override
    public void saveAuto(Auto auto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/autos")
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.postForObject(uri, auto, Auto.class);
    }

    @Override
    public void removeAuto(Auto auto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/autos")
                .path("/"+auto.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.delete(uri);
    }

    @Override
    public Collection<Norm> findNorms() {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/norms")
                .build().toUri();
        log.debug("URI: " + uri);
        Norm[] normsArray = restTemplate.getForObject(uri, Norm[].class);
        ArrayList<Norm> norms = new ArrayList<>(normsArray.length);
        Collections.addAll(norms, normsArray);
        return norms;
    }

    @Override
    public void saveNorm(Norm norm) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/norms")
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.postForObject(uri, norm, Norm.class);
    }

    @Override
    public void removeNorm(Norm norm) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/norms")
                .path("/"+norm.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.delete(uri);
    }

    @Override
    public Collection<RouteForm> findRouteForms() {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .build().toUri();
        log.debug("URI: " + uri);
        RouteForm[] routeFormsArray = restTemplate.getForObject(uri, RouteForm[].class);
        ArrayList<RouteForm> routeForms = new ArrayList<>(routeFormsArray.length);
        Collections.addAll(routeForms, routeFormsArray);
        return routeForms;
    }

    @Override
    public Collection<RouteForm> findRouteFormsByDates(LocalDate begDate, LocalDate endDate) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .path("/by-dates")
                .path("/"+begDate)
                .path("/" + endDate)
                .build().toUri();
        log.debug("URI: " + uri);
        RouteForm[] routeFormsArray = restTemplate.getForObject(uri, RouteForm[].class);
        ArrayList<RouteForm> routeForms = new ArrayList<>(routeFormsArray.length);
        Collections.addAll(routeForms, routeFormsArray);
        return routeForms;
    }

    @Override
    public RouteForm findRouteFormById(UUID id) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .path("/"+id)
                .build().toUri();
        log.debug("URI: " + uri);
        RouteForm routeForm = restTemplate.getForObject(uri, RouteForm.class);
        return routeForm;
    }

    @Override
    public Collection<RouteForm> findRouteFormsByAuto(Auto auto) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .path("/by-auto")
                .path("/" + auto.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        RouteForm[] routeFormsArray = restTemplate.getForObject(uri, RouteForm[].class);
        ArrayList<RouteForm> routeForms = new ArrayList<>(routeFormsArray.length);
        Collections.addAll(routeForms, routeFormsArray);
        return routeForms;
    }

    @Override
    public Collection<RouteForm> findRouteFormsByAutoAndDates(Auto auto, LocalDate begDate, LocalDate endDate) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .path("/by-auto")
                .path("/" + auto.getId())
                .path("/by-dates")
                .path("/"+begDate)
                .path("/" + endDate)
                .build().toUri();
        log.debug("URI: " + uri);
        RouteForm[] routeFormsArray = restTemplate.getForObject(uri, RouteForm[].class);
        ArrayList<RouteForm> routeForms = new ArrayList<>(routeFormsArray.length);
        Collections.addAll(routeForms, routeFormsArray);
        return routeForms;
    }

    @Override
    public Collection<RouteForm> findRouteFormsByBranch(Branch branch) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .path("/by-branch")
                .path("/" + branch.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        RouteForm[] routeFormsArray = restTemplate.getForObject(uri, RouteForm[].class);
        ArrayList<RouteForm> routeForms = new ArrayList<>(routeFormsArray.length);
        Collections.addAll(routeForms, routeFormsArray);
        return routeForms;
    }

    @Override
    public Collection<RouteForm> findRouteFormsByBranchAndDates(Branch branch, LocalDate begDate, LocalDate endDate) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .path("/by-branch")
                .path("/" + branch.getId())
                .path("/by-dates")
                .path("/"+begDate)
                .path("/" + endDate)
                .build().toUri();
        log.debug("URI: " + uri);
        RouteForm[] routeFormsArray = restTemplate.getForObject(uri, RouteForm[].class);
        ArrayList<RouteForm> routeForms = new ArrayList<>(routeFormsArray.length);
        Collections.addAll(routeForms, routeFormsArray);
        return routeForms;
    }


    @Override
    public void saveRouteForm(RouteForm routeForm) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.postForObject(uri, routeForm, RouteForm.class);
    }

    @Override
    public void removeRouteForm(RouteForm routeForm) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/routeforms")
                .path("/"+routeForm.getId())
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.delete(uri);
    }

    @Override
    public ApplicationOptions loadOptions() {
        log.info("Getting options..");
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/options")
                .build().toUri();
        log.debug("URI: " + uri);
        ApplicationOptions options = restTemplate.getForObject(uri, ApplicationOptions.class);
        return options;
    }

    @Override
    public void saveOptions(ApplicationOptions applicationOptions) {
        URI uri = UriComponentsBuilder.fromHttpUrl(optionsHolder.getServerUrl())
                .path("/options")
                .build().toUri();
        log.debug("URI: " + uri);
        restTemplate.postForObject(uri,applicationOptions,ApplicationOptions.class);
    }

    @Override
    public Norm.SeasonType getSeasonForDate(LocalDate date) {
        log.debug("Get season for date: " + date);
        if (date == null) return null;

        ApplicationOptions options = optionsHolder.getOptions();

        if (options == null) optionsHolder.setOptions(loadOptions());
        options = optionsHolder.getOptions();

        Integer winterToSummerMonth = options.getWinterToSummerMonth();
        Integer winterToSummerDay = options.getWinterToSummerDay();
        Integer summerToWinterMonth = options.getSummerToWinterMonth();
        Integer summerToWinterDay = options.getSummerToWinterDay();
        log.debug("WinterToSummer month: " + winterToSummerMonth);
        log.debug("SummerToWinter month: " + summerToWinterMonth);

        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        if (month > winterToSummerMonth && month < summerToWinterMonth) {
            log.debug("Return SUMMER");
            return Norm.SeasonType.SUMMER;
        }
        if (month > summerToWinterMonth || month < winterToSummerMonth) {
            log.debug("Return WINTER");
            return Norm.SeasonType.WINTER;
        }
        if (month == winterToSummerMonth) {
            if (day < winterToSummerDay) {
                log.debug("Return WINTER");
                return Norm.SeasonType.WINTER;
            }
            else {
                log.debug("Return SUMMER");
                return Norm.SeasonType.SUMMER;
            }
        }

        if (month == summerToWinterMonth) {
            if (day < summerToWinterDay) {
                log.debug("Return SUMMER");
                return Norm.SeasonType.SUMMER;
            }
            else {
                log.debug("Return WINTER");
                return Norm.SeasonType.WINTER;
            }
        }

        return null;

    }
}
