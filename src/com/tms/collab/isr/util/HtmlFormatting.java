package com.tms.collab.isr.util;

public class HtmlFormatting {
	/**
     * Convert a String into HTML compatible codes. For example, new line character is converted into BR.
     * <br />Please don't use the text generated from this method to be displayed in text area or editable view; or you will get some unwanted HTML characters  
     * @param rawText the String of raw text
     * @return the HTML compatible String which is rendered to be displayed in browser as plain text
     */
    public static String getHtmlText(String text) {
        String htmlText = "";
        char textChar[] = text.toCharArray();
        
        for(int i=0; i<textChar.length; i++) {
            int ascii = textChar[i];
            if(ascii == 13) {
                htmlText += "<br />";
            }
            else {
                htmlText += textChar[i];
            }
        }
        
        return htmlText;
    }
    
    public static String getEscapedXmlText(String text) {
    	StringBuffer escapedXmlText = new StringBuffer();
    	
    	for (int i = 0; i < text.length(); i++) { 
            char c = text.charAt(i); 
            if (c == '&') 
            	escapedXmlText.append("&amp;");
            else if (c == '<') {
            	if(i + 5 <= text.length()) {
            		if(text.substring(i, i+6).equals("<br />")) {
            			escapedXmlText.append("<br />");
            			i += 5;
            		}
            		else {
            			escapedXmlText.append("&lt;");
            		}
            	}
            	else {
            		escapedXmlText.append("&lt;");
            	}
            }
            else if (c == '>') 
            	escapedXmlText.append("&gt;");
            else if (c == '"') 
            	escapedXmlText.append("&#034;");
            else if (c == '\'') 
            	escapedXmlText.append("&#039;");
            else 
            	escapedXmlText.append(c);
        } 
    	
    	return escapedXmlText.toString();
    }
}
