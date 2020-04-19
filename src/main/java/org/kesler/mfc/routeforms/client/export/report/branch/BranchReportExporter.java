package org.kesler.mfc.routeforms.client.export.report.branch;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kesler.mfc.routeforms.client.export.Exporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Created by alex on 25.09.15.
 */
@Component
public class BranchReportExporter extends Exporter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private BranchReport branchReport;

    public void exportBranchReport(BranchReport branchReport) throws Exception {
        this.branchReport = branchReport;

        String outFilePath = getOutDir() +
                "Отчет об эксплуатации " +
                branchReport.getAuto().getModel() + " " +
                branchReport.getAuto().getRegNum() + " за " +
                branchReport.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " +
                branchReport.getDate().getYear() + "г.xlsx";
        prepareAndSaveXlsx(outFilePath);
        openFile(outFilePath);

    }


    @Override
    protected void prepareAndSaveXlsx(String outFilePath) throws Exception {
        InputStream inputStream = getTemplateInputStream(optionsHolder.getOptions().getBranchReportForm());
        if (inputStream==null) return;
        log.info("Open template wb..");

        XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(inputStream));

        inputStream.close();


        XSSFSheet firstSheet = wb.getSheetAt(0);

        ///// Заполняем все позиции

        XSSFCell monthCell = firstSheet.getRow(6).getCell(2);
        monthCell.setCellValue(branchReport.getDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE,Locale.getDefault()));

        XSSFCell yearCell = firstSheet.getRow(6).getCell(4);
        yearCell.setCellValue(branchReport.getDate().getYear());

        XSSFCell branchCell = firstSheet.getRow(7).getCell(2);
        branchCell.setCellValue(branchReport.getAuto().getBranch().getName());

        XSSFCell autoCell = firstSheet.getRow(8).getCell(2);
        autoCell.setCellValue(branchReport.getAuto().getModel());

        XSSFCell autoRegNumCell = firstSheet.getRow(9).getCell(2);
        autoRegNumCell.setCellValue(branchReport.getAuto().getRegNum());

        XSSFCell driverCell = firstSheet.getRow(10).getCell(2);
        driverCell.setCellValue(branchReport.getDriver().getFioFull());


        /// Выгрузка километража

        XSSFCell beginOdoCell = firstSheet.getRow(14).getCell(2);
        beginOdoCell.setCellValue(branchReport.beginOdo);

        XSSFCell totalOdoCell = firstSheet.getRow(15).getCell(2);
        totalOdoCell.setCellValue(branchReport.totalOdo);

        XSSFCell idleTimeCell = firstSheet.getRow(18).getCell(2);
        idleTimeCell.setCellValue(branchReport.idleTime);

        XSSFCell specTimeCell = firstSheet.getRow(19).getCell(2);
        specTimeCell.setCellValue(branchReport.specTime);

        XSSFCell endOdoCell = firstSheet.getRow(20).getCell(2);
        endOdoCell.setCellValue(branchReport.endOdo);


        // Выгрузка расхода


        XSSFCell fuelTypeCell = firstSheet.getRow(24).getCell(2);
        fuelTypeCell.setCellValue(branchReport.fuelType.toString());

        XSSFCell beginFuelCell = firstSheet.getRow(25).getCell(2);
        beginFuelCell.setCellValue(branchReport.beginFuel);

        XSSFCell totalFuellingCell = firstSheet.getRow(26).getCell(2);
        totalFuellingCell.setCellValue(branchReport.totalFueling);

        XSSFCell cardFuellingCell = firstSheet.getRow(27).getCell(2);
        cardFuellingCell.setCellValue(branchReport.cardFueling);

        XSSFCell cacheFuellingCell = firstSheet.getRow(28).getCell(2);
        cacheFuellingCell.setCellValue(branchReport.cacheFueling);

        XSSFCell consumedFuelCell = firstSheet.getRow(29).getCell(2);
        consumedFuelCell.setCellValue(branchReport.consumedFuel);

        XSSFCell endFuelCell = firstSheet.getRow(30).getCell(2);
        endFuelCell.setCellValue(branchReport.endFuel);


        // Всего путевых листов

        XSSFCell rfCountCell = firstSheet.getRow(32).getCell(5);
        rfCountCell.setCellValue(branchReport.routeFormsCount);


        log.info("Save wb..");

        FileOutputStream fileOutputStream = new FileOutputStream(outFilePath);
        wb.write(fileOutputStream);
        fileOutputStream.close();


    }
}
