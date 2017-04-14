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
public class TruckRFRFExporter extends RFExporter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void prepareAndSaveXlsx(String outFilePath) throws Exception{
        InputStream inputStream = getTemplateInputStream(optionsHolder.getOptions().getTruckForm());
        if (inputStream==null) return;
        log.info("Open template wb..");

        XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(inputStream));

        inputStream.close();


        XSSFSheet firstSheet = wb.getSheetAt(0);

        ///// Заполняем все позиции

        XSSFCell seriesCell = firstSheet.getRow(2).getCell(80);
        seriesCell.setCellValue(routeForm.getSeries());

        XSSFCell numberCell = firstSheet.getRow(2).getCell(100);
        numberCell.setCellValue(routeForm.getNumber());

        XSSFCell dayCell = firstSheet.getRow(4).getCell(58);
        dayCell.setCellValue(routeForm.getDate().getDayOfMonth());

        XSSFCell monthCell = firstSheet.getRow(4).getCell(67);
        monthCell.setCellValue(routeForm.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

        XSSFCell yearCell = firstSheet.getRow(4).getCell(89);
        yearCell.setCellValue(routeForm.getDate().getYear());



//        XSSFCell orgNameCell = firstSheet.getRow(5).getCell(16);
//        orgNameCell.setCellValue(routeForm.getOrgName());

        XSSFCell autoModelCell = firstSheet.getRow(12).getCell(16);
        autoModelCell.setCellValue(routeForm.getAuto().getModel());

        XSSFCell autoRegNumCell = firstSheet.getRow(13).getCell(27);
        autoRegNumCell.setCellValue(routeForm.getAuto().getRegNum());



        XSSFCell driverFIOCell = firstSheet.getRow(14).getCell(8);
        driverFIOCell.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getFioFull());

        XSSFCell driverTabelNumCell = firstSheet.getRow(14).getCell(86);
        driverTabelNumCell.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getTabelNum());

        XSSFCell driverLicenseCell = firstSheet.getRow(16).getCell(15);
        driverLicenseCell.setCellValue(routeForm.getDriver()==null?"":routeForm.getDriver().getDriverLicense());

        XSSFCell taskAddressCell = firstSheet.getRow(36).getCell(0);
        taskAddressCell.setCellValue(routeForm.getAddress());



        XSSFCell departureDayCell = firstSheet.getRow(13).getCell(116);
        departureDayCell.setCellValue(routeForm.getDate() == null ? "" : routeForm.getDate().getDayOfMonth()+"");

        XSSFCell departureMonthCell = firstSheet.getRow(13).getCell(123);
        departureMonthCell.setCellValue(routeForm.getDate() == null ? "" : routeForm.getDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));

        XSSFCell departureHourCell = firstSheet.getRow(13).getCell(130);
        departureHourCell.setCellValue(routeForm.getDepartureTime() == null ? "" : routeForm.getDepartureTime().getHour()+"");

        XSSFCell departureMinuteCell = firstSheet.getRow(13).getCell(137);
        departureMinuteCell.setCellValue(routeForm.getDepartureTime() == null ? "" : routeForm.getDepartureTime().getMinute()+"");

        XSSFCell departureODOCell = firstSheet.getRow(13).getCell(151);
        departureODOCell.setCellValue(routeForm.getDepartureODO()==null?"":routeForm.getDepartureODO().toString());

        XSSFCell combackDayCell = firstSheet.getRow(14).getCell(116);
        combackDayCell.setCellValue(routeForm.getDate() == null ? "" : routeForm.getDate().getDayOfMonth()+"");

        XSSFCell combackMonthCell = firstSheet.getRow(14).getCell(123);
        combackMonthCell.setCellValue(routeForm.getDate() == null ? "" : routeForm.getDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));

        XSSFCell combackHourCell = firstSheet.getRow(14).getCell(130);
        combackHourCell.setCellValue(routeForm.getCombackTime() == null ? "" : routeForm.getCombackTime().getHour()+"");

        XSSFCell combackMinuteCell = firstSheet.getRow(14).getCell(137);
        combackMinuteCell.setCellValue(routeForm.getCombackTime() == null ? "" : routeForm.getCombackTime().getMinute()+"");

        XSSFCell combackODOCell = firstSheet.getRow(14).getCell(151);
        combackODOCell.setCellValue(routeForm.getCombackODO()==null?"":routeForm.getCombackODO().toString());




        XSSFCell fuellingCell = firstSheet.getRow(23).getCell(118);
        fuellingCell.setCellValue(routeForm.getFuelling()==null?"":routeForm.getFuelling().toString());

        XSSFCell departureFuelCell = firstSheet.getRow(23).getCell(127);
        departureFuelCell.setCellValue(routeForm.getDepartureFuel() == null ? "" : routeForm.getDepartureFuel().toString());

        XSSFCell combackFuelCell = firstSheet.getRow(23).getCell(136);
        combackFuelCell.setCellValue(routeForm.getCombackFuel() == null ? "" : routeForm.getCombackFuel().toString());



        XSSFCell specTimeCell = firstSheet.getRow(23).getCell(166);
        specTimeCell.setCellValue(routeForm.getSpecTime() == null ? "" : routeForm.getSpecTime().toString());

        XSSFCell idleTimeCell = firstSheet.getRow(23).getCell(178);
        idleTimeCell.setCellValue(routeForm.getIdleTime() == null ? "" : routeForm.getIdleTime().toString());


        XSSFCell driverFIOShortCell1 = firstSheet.getRow(45).getCell(92);
        driverFIOShortCell1.setCellValue(routeForm.getDriver().getFioShort());

        XSSFCell driverFIOShortCell2 = firstSheet.getRow(51).getCell(83);
        driverFIOShortCell2.setCellValue(routeForm.getDriver().getFioShort());






        XSSFSheet secondSheet = wb.getSheetAt(1);



        XSSFCell consumptionRateCell = secondSheet.getRow(34).getCell(0);
        consumptionRateCell.setCellValue(routeForm.getConsumptionRate() == null ? "" : routeForm.getConsumptionRate().toString());

        XSSFCell consumptionCell = secondSheet.getRow(34).getCell(8);
        consumptionCell.setCellValue(routeForm.getConsumption() == null ? "" : routeForm.getConsumption().toString());



        XSSFCell workTimeCell = secondSheet.getRow(34).getCell(17);
        Duration workTime = routeForm.getWorkTime();
        workTimeCell.setCellValue(workTime == null ? "" : Math.rint(workTime.toMinutes()/6)/10 +"");

        XSSFCell mileageCell = secondSheet.getRow(34).getCell(112);
        mileageCell.setCellValue(routeForm.getMileage()==null?"":routeForm.getMileage().toString());


        log.info("Save wb..");

        FileOutputStream fileOutputStream = new FileOutputStream(outFilePath);
        wb.write(fileOutputStream);
        fileOutputStream.close();

    }
}
