package com.sky.test;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 使用POI操作excel文件
 */

public class POITest {


    /**
     * 通过POI创建文件并写入内容
     */
    public static void write() throws Exception{
        //在内存中创建excel
        XSSFWorkbook excel = new XSSFWorkbook();

        //在excel文件中创建一个sheet页
        XSSFSheet sheet = excel.createSheet("标签1");

        //在sheet中创建行对象
        XSSFRow row = sheet.createRow(1);

        //创建单元格并写入内容
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("城市");

        row = sheet.createRow(2);
        row.createCell(1).setCellValue("雷振杰");
        row.createCell(2).setCellValue("洛杉矶");

        row = sheet.createRow(3);
        row.createCell(1).setCellValue("金三");
        row.createCell(2).setCellValue("纽约");

        FileOutputStream outputStream = new FileOutputStream(new File("/mnt/d/UserData/desktop/my.xlsx"));
        excel.write(outputStream);
        outputStream.close();
        excel.close();
    }

    /**
     * 通过POI读取excel内容
     * @throws Exception
     */
    public static void read() throws Exception{
        FileInputStream input = new FileInputStream(new File("/mnt/d/UserData/Desktop/my.xlsx"));
        XSSFWorkbook excel = new XSSFWorkbook(input);

        XSSFSheet sheet = excel.getSheetAt(0);

        int row = sheet.getLastRowNum();

        for(int i = 1; i <= row; i++){
            XSSFRow content = sheet.getRow(i);
            String name = content.getCell(1).getStringCellValue();
            String city =  content.getCell(2).getStringCellValue();

            System.out.println(name+"\t"+city);
        }

        input.close();
        excel.close();

    }

    public static void main(String[] args) throws Exception{
        write();
        read();
    }
}
