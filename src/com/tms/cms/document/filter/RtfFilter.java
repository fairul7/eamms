package com.tms.cms.document.filter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Filters an RTF document.
 */
public class RtfFilter extends DocumentFilter {
    
    public String filter(InputStream in) throws IOException {
        String str = super.filter(in);
        return parseRtf(str);
    }    
    
    
    public static final int STATE_TEXT = 0;
    public static final int STATE_CONTROL_START = 1;
    public static final int STATE_CONTROL_WORD = 2;
    public static final int STATE_CONTROL_PARAM = 3;
    public static final int STATE_DESTINATION = 4;
    
    public String parseRtf(String str) {
        // locate first section
        int index = str.indexOf("\\sect");
        if (index > 0) {
            str = str.substring(index);
        }
        
        // extract text
        StringBuffer buffer = new StringBuffer();
        int state = STATE_TEXT;
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            switch(state) {
                case STATE_TEXT:
                    if (c == '\\') {
                        state = STATE_CONTROL_START;
                    }
                    else if (c == '{') {
                        ;
                    }
                    else if (c == '}') {
                        ;
                    }
                    else {
                        buffer.append(c);
                    }
                    break;
                case STATE_CONTROL_START:
                    if (c == '*') {
                        state = STATE_DESTINATION;
                    }
                    else if (Character.isLetter(c) && Character.isLowerCase(c)) {
                        state = STATE_CONTROL_WORD;
                    }
                    else {
                        state = STATE_TEXT;
                    }
                    break;
                case STATE_CONTROL_WORD:
                    if (c == '\\') {
                        state = STATE_CONTROL_START;
                    }
                    else if (Character.isLetter(c)) {
                    }
                    else if (c == ' ') {
                        state = STATE_TEXT;
                    }
                    else if (Character.isDigit(c)) {
                        state = STATE_CONTROL_PARAM;
                    }
                    else if (c == '-') {
                        state = STATE_CONTROL_PARAM;
                    }
                    else if (!Character.isLetter(c) && !Character.isDigit(c)) {
                        state = STATE_TEXT;
                    }
                    break;
                case STATE_CONTROL_PARAM:
                    if (c == '\\') {
                        state = STATE_CONTROL_START;
                    }
                    else if (Character.isDigit(c)) {
                    }
                    else if (c == ' ') {
                        state = STATE_TEXT;
                        buffer.append(" ");
                    }
                    else if (!Character.isLetter(c) && !Character.isDigit(c)) {
                        state = STATE_TEXT;
                    }
                    break;
                case STATE_DESTINATION:
                    if (c == '}') {
                        state = STATE_TEXT;
                    }
                    break;
                default:
                    break;
            }
        }
        return buffer.toString();
    }
    

    public final static void main(String[] args) {
        RtfFilter filter = new RtfFilter();
        try {
            System.out.println("contents:\n" + filter.filter(new java.io.FileInputStream(
                "C:/Documents and Settings/kahliang/My Documents/Java and UML books.rtf")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
