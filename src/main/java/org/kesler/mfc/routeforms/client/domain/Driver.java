package org.kesler.mfc.routeforms.client.domain;



/**
 * Водитель
 */
public class Driver extends AbstractEntity {
    protected String fioFull;
    protected String fioShort;
    protected String driverLicense;
    protected String tabelNum;
    protected Branch branch;
    protected Boolean active = true;


    public String getFioFull() { return fioFull; }
    public void setFioFull(String fioFull) { this.fioFull = fioFull; }

    public String getFioShort() { return fioShort; }
    public void setFioShort(String fioShort) { this.fioShort = fioShort; }

    public String getDriverLicense() { return driverLicense; }
    public void setDriverLicense(String driverLicense) { this.driverLicense = driverLicense; }

    public String getTabelNum() { return tabelNum; }
    public void setTabelNum(String tabelNum) { this.tabelNum = tabelNum; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    public Boolean isActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    @Override
    public String toString() {
        return fioShort;
    }
}
