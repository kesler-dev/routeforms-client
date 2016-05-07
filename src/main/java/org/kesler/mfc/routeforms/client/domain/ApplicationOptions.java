package org.kesler.mfc.routeforms.client.domain;


/**
 * Настройки приложения
 */
public class ApplicationOptions extends AbstractEntity {

    private String orgName;
    private Integer winterToSummerMonth;
    private Integer winterToSummerDay;
    private Integer summerToWinterMonth;
    private Integer summerToWinterDay;
    private byte[] carForm;
    private byte[] truckForm;
    private byte[] branchReportForm;

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public Integer getWinterToSummerMonth() { return winterToSummerMonth; }
    public void setWinterToSummerMonth(Integer winterToSummerMonth) { this.winterToSummerMonth = winterToSummerMonth; }

    public Integer getWinterToSummerDay() { return winterToSummerDay; }
    public void setWinterToSummerDay(Integer winterToSummerDay) { this.winterToSummerDay = winterToSummerDay; }

    public Integer getSummerToWinterMonth() { return summerToWinterMonth; }
    public void setSummerToWinterMonth(Integer summerToWinterMonth) { this.summerToWinterMonth = summerToWinterMonth; }

    public Integer getSummerToWinterDay() { return summerToWinterDay; }
    public void setSummerToWinterDay(Integer summerToWinterDay) { this.summerToWinterDay = summerToWinterDay; }

    public byte[] getCarForm() { return carForm; }
    public void setCarForm(byte[] carForm) { this.carForm = carForm; }

    public byte[] getTruckForm() { return truckForm; }
    public void setTruckForm(byte[] truckForm) { this.truckForm = truckForm; }

    public byte[] getBranchReportForm() { return branchReportForm; }
    public void setBranchReportForm(byte[] branchReportForm) { this.branchReportForm = branchReportForm; }
}
