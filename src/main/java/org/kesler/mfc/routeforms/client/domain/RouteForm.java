package org.kesler.mfc.routeforms.client.domain;


import javafx.scene.paint.Color;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Путевой лист
 */
public class RouteForm extends AbstractEntity {


    public RouteForm() {
        created = LocalDateTime.now();
    }

    public static RouteForm createFromPrevious(RouteForm previousRouteForm) {
        RouteForm newRouteForm = new RouteForm();

        newRouteForm.setPreviousRouteFormID(previousRouteForm==null?null:previousRouteForm.getId());
        newRouteForm.setAuto(previousRouteForm.getAuto());
        newRouteForm.setDate(previousRouteForm.getDate().plus(1L, ChronoUnit.DAYS));
        newRouteForm.setDepartureODO(previousRouteForm.getCombackODO());
        newRouteForm.setDepartureFuel(previousRouteForm.getCombackFuel());
        newRouteForm.setSeasonType(previousRouteForm.getSeasonType());
        newRouteForm.setOrgName(previousRouteForm.getOrgName());
        newRouteForm.setAddress(previousRouteForm.getAddress());

        return newRouteForm;
    }

    protected String series;

    protected String number;

    protected Auto auto;
    protected Driver driver;
    protected LocalDate date;
    protected UUID previousRouteFormID;
    protected LocalDateTime created;
    protected Employee employee;
    protected String orgName;
    protected String address;
    protected LocalTime departureTime;
    protected LocalTime combackTime;
    protected Long departureODO;
    protected Long combackODO;
    protected Double departureFuel;
    protected Double combackFuel;
    protected Double fuelling;
    protected FuellingType fuellingType;
    protected Double idleTime;
    protected Double specTime;
    protected Duration workTime;
    protected Norm.SeasonType seasonType;
    protected Norm.ModeType modeType;
    protected Double consumptionRate;
    protected Double consumption;
    protected State state;


    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public void setIntNumber(Integer intNumber) { this.number = String.format("%06d",intNumber); }

    public String getSeriesAndNumber() {
        return series + " " + number;
    }

    public Auto getAuto() { return auto; }
    public void setAuto(Auto auto) { this.auto = auto; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public UUID getPreviousRouteFormID() { return previousRouteFormID; }
    public void setPreviousRouteFormID(UUID previousRouteFormID) { this.previousRouteFormID = previousRouteFormID; }

    public LocalDateTime getCreated() { return created; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public LocalTime getCombackTime() { return combackTime; }
    public void setCombackTime(LocalTime combackTime) { this.combackTime = combackTime; }

    public Long getDepartureODO() { return departureODO; }
    public void setDepartureODO(Long departureODO) { this.departureODO = departureODO; }

    public Long getCombackODO() { return combackODO; }
    public void setCombackODO(Long combackODO) { this.combackODO = combackODO; }

    public Long getMileage() {
        if (departureODO != null && combackODO != null) {
            return combackODO - departureODO;
        } else {
            return null;
        }
    }

    public Double getDepartureFuel() { return departureFuel; }
    public void setDepartureFuel(Double departureFuel) { this.departureFuel = departureFuel; }

    public Double getCombackFuel() { return combackFuel; }
    public void setCombackFuel(Double combackFuel) { this.combackFuel = combackFuel; }

    public Double getFuelling() { return fuelling; }
    public void setFuelling(Double fuelling) { this.fuelling = fuelling; }

    public FuellingType getFuellingType() { return fuellingType; }
    public void setFuellingType(FuellingType fuellingType) { this.fuellingType = fuellingType; }

    public Double getIdleTime() { return idleTime; }
    public void setIdleTime(Double idleTime) { this.idleTime = idleTime; }

    public Double getSpecTime() { return specTime; }
    public void setSpecTime(Double specTime) { this.specTime = specTime; }

    public Double getConsumptionRate() { return consumptionRate; }
    public void setConsumptionRate(Double consumptionRate) { this.consumptionRate = consumptionRate; }

    public Double getConsumption() { return consumption; }
    public void setConsumption(Double consumption) { this.consumption = consumption; }

    public Duration getWorkTime() { return workTime; }
    public void setWorkTime(Duration workTime) { this.workTime = workTime; }

    public Norm.SeasonType getSeasonType() { return seasonType; }
    public void setSeasonType(Norm.SeasonType seasonType) { this.seasonType = seasonType; }

    public Norm.ModeType getModeType() { return modeType; }
    public void setModeType(Norm.ModeType modeType) { this.modeType = modeType; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public State getState() {
        return state;
    }

    public void updateState(RouteForm previous) {

        if (departureODO==null && previous==null) {state = State.FIRST; return;}
        if (previous!=null && !departureODO.equals(previous.getCombackODO())) {state = State.UNCONSISTENT; return;}
        if (departureFuel==null && previous==null) {state = State.FIRST; return;}
        if (previous!=null && !departureFuel.equals(previous.getCombackFuel())) {state = State.UNCONSISTENT; return;}
        if (driver==null) {state = State.UNREADY; return;}
        if (address==null || address.isEmpty()) {state = State.UNREADY; return;}
        if (departureTime==null) {state = State.UNREADY; return;}
        if (combackTime==null) {state = State.UNREADY; return;}
        if (combackODO==null) {state = State.UNREADY; return;}
        if (combackFuel==null || combackFuel <= 0.0) {state = State.UNREADY; return;}
        if (seasonType==null) {state = State.UNREADY; return;}
        if (modeType==null) {state = State.UNREADY; return;}

        state = State.READY;
    }


    @Override
    public String toString() {
        return series +" " + number +" от " + date.format(DateTimeFormatter.ISO_DATE);
    }

    public enum State {
        NONE("не определен",Color.BLACK),
        UNREADY("не заполнен", Color.GOLDENROD),
        READY("заполнен",Color.GREEN),
        UNCONSISTENT("не соответствует показаниям из предыдущего",Color.DARKRED),
        FIRST("первый",Color.BLACK);

        private String desc;
        private Color color;

        State(String desc, Color color) {
            this.desc = desc;
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
        @Override
        public String toString() {
            return desc;
        }
    }


}
