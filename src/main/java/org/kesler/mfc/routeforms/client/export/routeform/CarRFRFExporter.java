package org.kesler.mfc.routeforms.client.export.routeform;


import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Created by alex on 29.06.15.
 */
@Component
public class CarRFRFExporter extends RFExporter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void prepareAndSaveXlsx(String outFilePath) throws Exception{
        InputStream inputStream = getTemplateInputStream(optionsHolder.getOptions().getCarForm());
        if (inputStream==null) return;
        log.info("Open template wb..");

        XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(inputStream));

        inputStream.close();


        XSSFSheet firstSheet = wb.getSheetAt(0);

        ///// Заполняем все позиции

        XSSFCell seriesCell = firstSheet.getRow(3).getCell(61);
        seriesCell.setCellValue(routeForm.getSeries());

        XSSFCell numberCell = firstSheet.getRow(3).getCell(75);
        numberCell.setCellValue(routeForm.getNumber());

        XSSFCell dayCell = firstSheet.getRow(4).getCell(29);
        dayCell.setCellValue(routeForm.getDate().getDayOfMonth());

        XSSFCell monthCell = firstSheet.getRow(4).getCell(34);
        monthCell.setCellValue(routeForm.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

        XSSFCell yearCell = firstSheet.getRow(4).getCell(48);
        yearCell.setCellValue(routeForm.getDate().getYear() - 2000);

        XSSFCell orgNameCell = firstSheet.getRow(7).getCell(17);
        orgNameCell.setCellValue(routeForm.getOrgName());

        XSSFCell autoModelCell = firstSheet.getRow(9).getCell(21);
        autoModelCell.setCellValue(routeForm.getAuto().getModel());

        XSSFCell autoRegNumCell = firstSheet.getRow(10).getCell(34);
        autoRegNumCell.setCellValue(routeForm.getAuto().getRegNum());

        XSSFCell driverFIOCell = firstSheet.getRow(11).getCell(12);
        driverFIOCell.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getFioFull());

        XSSFCell driverLicenseCell = firstSheet.getRow(13).getCell(17);
        driverLicenseCell.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getDriverLicense());

        XSSFCell departureODOCell = firstSheet.getRow(18).getCell(72);
        departureODOCell.setCellValue(routeForm.getDepartureODO()==null?"":routeForm.getDepartureODO().toString());

        String orgName = routeForm.getOrgName();
        int maxLength = 30;

        if (orgName!=null && !orgName.isEmpty()) {
            if (orgName.length()<maxLength) {
                XSSFCell taskOrgNameCell = firstSheet.getRow(19).getCell(16);
                taskOrgNameCell.setCellValue(orgName);

            } else {
                String[] orgNameSplit = orgName.split(" ");
                StringBuilder orgName1 = new StringBuilder();
                StringBuilder orgName2 = new StringBuilder();

                boolean complete1 = false;
                for (String word : orgNameSplit) {
                    if (!complete1) {
                        if ((orgName1.length()+1+word.length()) < maxLength) {
                            orgName1.append(" ").append(word);
                        } else {
                            orgName2.append(word);
                            complete1=true;
                        }
                    } else {
                        orgName2.append(" ").append(word);

                    }
                }

                XSSFCell taskOrgNameCell = firstSheet.getRow(19).getCell(16);
                taskOrgNameCell.setCellValue(orgName1.toString());

                XSSFCell taskOrgNameCell1 = firstSheet.getRow(21).getCell(0);
                taskOrgNameCell1.setCellValue(orgName2.toString());

            }
        }


        XSSFCell taskAddressCell = firstSheet.getRow(26).getCell(0);
        taskAddressCell.setCellValue(routeForm.getAddress());

        XSSFCell driverFIOShortCell = firstSheet.getRow(25).getCell(67);
        driverFIOShortCell.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getFioShort());

        XSSFCell driverTabelNumCell = firstSheet.getRow(11).getCell(67);
        driverTabelNumCell.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getTabelNum());

        XSSFCell departureTimeCell = firstSheet.getRow(29).getCell(30);
        departureTimeCell.setCellValue(routeForm.getDepartureTime() == null ? "" : routeForm.getDepartureTime().toString());

        XSSFCell combackTimeCell = firstSheet.getRow(34).getCell(32);
        combackTimeCell.setCellValue(routeForm.getCombackTime() == null ? "" : routeForm.getCombackTime().toString());

        XSSFCell fuelTypeCell = firstSheet.getRow(28).getCell(57);
        fuelTypeCell.setCellValue(routeForm.getAuto().getFuelType()==null?"":routeForm.getAuto().getFuelType().toString());

        XSSFCell fuellingCell = firstSheet.getRow(33).getCell(71);
        fuellingCell.setCellValue(routeForm.getFuelling()==null?"":routeForm.getFuelling().toString());

        XSSFCell departureFuelCell = firstSheet.getRow(36).getCell(71);
        departureFuelCell.setCellValue(routeForm.getDepartureFuel() == null ? "" : routeForm.getDepartureFuel().toString());

        XSSFCell combackFuelCell = firstSheet.getRow(37).getCell(71);
        combackFuelCell.setCellValue(routeForm.getCombackFuel() == null ? "" : routeForm.getCombackFuel().toString());

        XSSFCell consumptionRateCell = firstSheet.getRow(38).getCell(71);
        consumptionRateCell.setCellValue(routeForm.getConsumptionRate() == null ? "" : routeForm.getConsumptionRate().toString());

        XSSFCell consumptionCell = firstSheet.getRow(39).getCell(71);
        consumptionCell.setCellValue(routeForm.getConsumption() == null ? "" : routeForm.getConsumption().toString());

        XSSFCell combackODOCell = firstSheet.getRow(44).getCell(71);
        combackODOCell.setCellValue(routeForm.getCombackODO() == null ? "" : routeForm.getCombackODO().toString());

        XSSFCell driverFIOShortCell1 = firstSheet.getRow(43).getCell(30);
        driverFIOShortCell1.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getFioShort());

        XSSFSheet secondSheet = wb.getSheetAt(1);

        XSSFCell workTimeCell = secondSheet.getRow(22).getCell(4);
        Duration workTime = routeForm.getWorkTime();
        workTimeCell.setCellValue(workTime == null ? "" : Math.rint(workTime.toMinutes()/6)/10 +"");

        XSSFCell mileageCell = secondSheet.getRow(24).getCell(4);
        mileageCell.setCellValue(routeForm.getMileage()==null?"":routeForm.getMileage().toString());


        log.info("Save wb..");

        FileOutputStream fileOutputStream = new FileOutputStream(outFilePath);
        wb.write(fileOutputStream);
        fileOutputStream.close();

    }
}
