package org.kesler.mfc.routeforms.client.domain;


/**
 * Норма потребления
 */
public class Norm extends AbstractEntity{

    protected String name;

    protected Double sumConsumptionRate;
    protected Double winConsumptionRate;
    protected Double idleConsumptionRate;
    protected Double specConsumptionRate;
    protected Boolean noSpecSummer;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getSumConsumptionRate() {
        return sumConsumptionRate;
    }

    public void setSumConsumptionRate(Double sumConsumptionRate) {
        this.sumConsumptionRate = sumConsumptionRate;
    }

    public Double getWinConsumptionRate() {
        return winConsumptionRate;
    }

    public void setWinConsumptionRate(Double winConsumptionRate) {
        this.winConsumptionRate = winConsumptionRate;
    }

    public Double getIdleConsumptionRate() { return idleConsumptionRate; }
    public void setIdleConsumptionRate(Double idleConsumptionRate) { this.idleConsumptionRate = idleConsumptionRate; }

    public Double getSpecConsumptionRate() { return specConsumptionRate; }
    public void setSpecConsumptionRate(Double specConsumptionRate) { this.specConsumptionRate = specConsumptionRate; }

    public Boolean isNoSpecSummer() { return noSpecSummer; }
    public void setNoSpecSummer(Boolean noSpecSummer) { this.noSpecSummer = noSpecSummer; }

    public Double getConsumptionRate(SeasonType seasonType) {
        Double consumptionRate = null;

        switch (seasonType) {
            case SUMMER:
                consumptionRate = sumConsumptionRate;
                break;
            case WINTER:
                consumptionRate = winConsumptionRate;
                break;
        }

        return consumptionRate;
    }


    @Override
    public String toString() {
        return name;
    }


    public enum SeasonType {
        SUMMER("лето"),
        WINTER("зима");

        private String desc;

        SeasonType(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }

    }

}
