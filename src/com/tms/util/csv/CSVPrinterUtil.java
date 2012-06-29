/*
 * ExcelCSVPrinterUtil
 * Date Created: Aug 3, 2005
 * Author: Tien Soon, Law
 * Description: Print values as a comma separated list that can be read by the Excel spreadsheet.
 * Company: TMS Berhad
 */
package com.tms.util.csv;


public class CSVPrinterUtil {
    private boolean alwaysQuote = false;
    private char delimiterChar = ',';
    private char quoteChar = '"';
    private StringBuffer outputString;
    private boolean newLine = true;
    
    /**
     * Initialize StringBuffer
     *
     * @param out stream to which to print.
     */
    public CSVPrinterUtil() {
        outputString = new StringBuffer();
    }
    
    /**
     * Print the string as the last value on the line.  The value
     * will be quoted if needed.
     *
     * @param value value to be outputted.
     */
    public void writeln(String value) {
        write(value);
        writeln();
    }
    
    /**
     * Output a blank line.
     */
    public void writeln() {
        outputString.append("\n");
        newLine = true;
    }
    
    /**
     * Print a single line of comma separated values.
     * The values will be quoted if needed.  Quotes and
     * and other characters that need it will be escaped.
     *
     * @param values values to be outputted.
     */
    public void writeln(String[] values) {
        write(values);
        writeln();
    }
    
    /**
     * Print a single line of comma separated values.
     * The values will be quoted if needed.  Quotes and
     * and other characters that need it will be escaped.
     *
     * @param values values to be outputted.
     */
    public void write(String[] values) {
        for (int i=0; i<values.length; i++){
            write(values[i]);
        }
    }
    
    /**
     * Print several lines of comma separated values.
     * The values will be quoted if needed.  Quotes and
     * newLine characters will be escaped.
     *
     * @param values values to be outputted.
     */
    public void writeln(String[][] values) {
        for (int i=0; i<values.length; i++){
            writeln(values[i]);
        }
        if (values.length == 0){
            writeln();
        }
    }
    
    /**
     * Print the string as the next value on the line.  The value
     * will be quoted if needed.  If value is null, an empty value is printed.
     *
     * @param value value to be outputted.
     */
    public void write(String value) {
        if (value == null) value = "";
        boolean quote = false;
        if (alwaysQuote){
            quote = true;
        } 
        else if (value.length() > 0){
            for (int i=0; i<value.length(); i++){
                char c = value.charAt(i);
                if (c==quoteChar || c==delimiterChar || c=='\n' || c=='\r'){
                    quote = true;
                }
            }
        } 
        else if (newLine) {
            // always quote an empty token that is the firs
            // on the line, as it may be the only thing on the
            // line.  If it were not quoted in that case,
            // an empty line has no tokens.
            quote = true;
        }
        if (newLine){
            newLine = false;
        } 
        else {
            outputString.append(delimiterChar);
        }
        if (quote){
            outputString.append(escapeAndQuote(value));
        } else {
            outputString.append(value);
        }
    }

    /**
     * Enclose the value in quotes and escape the quote
     * and comma characters that are inside.
     *
     * @param value needs to be escaped and quoted.
     *
     * @return the value, escaped and quoted.
     */
    private String escapeAndQuote(String value) {
        String s = value.replaceAll(String.valueOf(quoteChar), String.valueOf(quoteChar) + String.valueOf(quoteChar));
        return (new StringBuffer(2 + s.length())).append(quoteChar).append(s).append(quoteChar).toString();
    }
    
    /**
     * Return the outputString as StringBuffer
     * 
     * @return outputString
     */
    public StringBuffer getOutputString() {
        return outputString;
    }
}
