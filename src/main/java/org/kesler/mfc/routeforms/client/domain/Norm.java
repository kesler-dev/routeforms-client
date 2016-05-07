package org.kesler.mfc.routeforms.client.domain;


/**
 * Норма потребления
 */
public class Norm extends AbstractEntity{

    protected String name;

    protected Double sumSitConsumptionRate;
    protected Double sumVilConsumptionRate;
    protected Double winSitConsumptionRate;
    protected Double winVilConsumptionRate;
    protected Double idleConsumptionRate;
    protected Double specConsumptionRate;
    protected Boolean noSpecSummer;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getSumSitConsumptionRate() { return sumSitConsumptionRate; }
    public void setSumSitConsumptionRate(Double sumSitConsumptionRate) { this.sumSitConsumptionRate = sumSitConsumptionRate; }

    public Double getSumVilConsumptionRate() { return sumVilConsumptionRate; }
    public void setSumVilConsumptionRate(Double sumVilConsumptionRate) { this.sumVilConsumptionRate = sumVilConsumptionRate; }

    public Double getWinSitConsumptionRate() { return winSitConsumptionRate; }
    public void setWinSitConsumptionRate(Double winSitConsumptionRate) { this.winSitConsumptionRate = winSitConsumptionRate; }

    public Double getWinVilConsumptionRate() { return winVilConsumptionRate; }
    public void setWinVilConsumptionRate(Double winVilConsumptionRate) { this.winVilConsumptionRate = winVilConsumptionRate; }

    public Double getIdleConsumptionRate() { return idleConsumptionRate; }
    public void setIdleConsumptionRate(Double idleConsumptionRate) { this.idleConsumptionRate = idleConsumptionRate; }

    public Double getSpecConsumptionRate() { return specConsumptionRate; }
    public void setSpecConsumptionRate(Double specConsumptionRate) { this.specConsumptionRate = specConsumptionRate; }

    public Boolean isNoSpecSummer() { return noSpecSummer; }
    public void setNoSpecSummer(Boolean noSpecSummer) { this.noSpecSummer = noSpecSummer; }

    public Double getConsumptionRate(SeasonType seasonType,ModeType modeType) {
        Double consumptionRate = null;

        switch (seasonType) {
            case SUMMER:
                switch (modeType) {
                    case SITY:
                        consumptionRate = sumSitConsumptionRate;
                        break;
                    case VILAGE:
                        consumptionRate = sumVilConsumptionRate;
                        break;
                }
                break;
            case WINTER:
                switch (modeType) {
                    case SITY:
                        consumptionRate = winSitConsumptionRate;
                        break;
                    case VILAGE:
                        consumptionRate = winVilConsumptionRate;
                        break;
                }
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

    public enum ModeType {
        SITY("город"),
        VILAGE("загород");

        private String desc;

        ModeType(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }

    }

}
