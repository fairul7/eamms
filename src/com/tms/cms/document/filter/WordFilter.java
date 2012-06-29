package com.tms.cms.document.filter;

import kacang.util.Log;

import java.io.IOException;
import java.io.InputStream;

import org.textmining.text.extraction.WordExtractor;

/**
 * Filters an MS Word document.
 */
public class WordFilter extends DocumentFilter {
    
    public String filter(InputStream in) throws IOException {
        return parseWord(in);
    }
   
    public String parseWord(InputStream in) throws IOException {
        try {
            WordExtractor extractor = new WordExtractor();
            String content = extractor.extractText(in);
            return content;
        }
        catch (Exception e) {

            Log.getLog(getClass()).debug("Error parsing Word document: " + e.toString(), e);

            try {
                StringBuffer buffer = new StringBuffer();
                StringBuffer wordBuffer = new StringBuffer();

                int sameCharCount = 0;
                for (int i=0; i < in.available(); i++) {
                    char c = (char)in.read();

                    // ignore header
                    if (i <= 1472)
                        continue;

                    if (Character.isLetterOrDigit(c)) {
                        wordBuffer.append(c);
                        if (wordBuffer.length() > 1 && c == wordBuffer.charAt(wordBuffer.length()-2)) {
                            sameCharCount++;
                        }
                        if (sameCharCount >= 5) {
                            wordBuffer.delete(wordBuffer.length()-6, wordBuffer.length());
                            sameCharCount = 0;
                        }
                    }
                    else if (wordBuffer.length() > 1) {
                        buffer.append(wordBuffer);
                        buffer.append(" ");
                        wordBuffer = new StringBuffer();
                        sameCharCount = 0;
                    }
                }
                if (wordBuffer.length() > 1) {
                    buffer.append(wordBuffer);
                }
                return buffer.toString();
            }
            catch (IOException e1) {
                Log.getLog(getClass()).error("Error parsing Word document: " + e.toString(), e);
                throw new IOException("Error parsing Word document: " + e.getMessage());
            }

        }
    }
    
    public final static void main(String[] args) {
        DocumentFilter filter = new WordFilter();
        try {
            System.out.println("contents:\n" + filter.filter(new java.io.FileInputStream(
                "C:/Documents and Settings/kahliang/My Documents/Java and UML books.doc")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
}