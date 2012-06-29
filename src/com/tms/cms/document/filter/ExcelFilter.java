package com.tms.cms.document.filter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * Filters an MS Excel document.
 */
public class ExcelFilter extends DocumentFilter {
    
    public String filter(InputStream in) throws IOException {
        return parseExcel(in);
    }
    
    
    public String parseExcel(InputStream in) throws IOException {
        StringBuffer buffer = new StringBuffer();
        try {
            Workbook workbook = Workbook.getWorkbook(in);
            
            for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
                Sheet s = workbook.getSheet(sheet);
                
                buffer.append(s.getName());
                buffer.append("\n");
                
                Cell[] row = null;
                
                for (int i = 0 ; i < s.getRows() ; i++) {
                    row = s.getRow(i);
                    
                    if (row.length > 0) {
                        buffer.append(row[0].getContents());
                        for (int j = 1; j < row.length; j++) {
                            buffer.append(", ");
                            buffer.append(row[j].getContents());
                        }
                    }
                    buffer.append("\n");
                }
            }
            return buffer.toString();
        }
        catch (Exception e) {
            throw new IOException(e.toString());
        }
    }
    
    public final static void main(String[] args) {
        ExcelFilter filter = new ExcelFilter();
        try {
            System.out.println("contents:\n" + filter.filter(new java.io.FileInputStream(
                "C:/Documents and Settings/kahliang/My Documents/test.xls")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
}

