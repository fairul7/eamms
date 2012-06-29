package com.tms.cms.document.filter;

import kacang.util.Log;
import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * Filters a PDF document by extracting the textual content.
 */
public class PdfFilter extends DocumentFilter {

    public String filter(InputStream in) throws IOException {
        COSDocument document = null;
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setLineSeparator("\n");
            PDFParser parser = new PDFParser(in);
            parser.parse();
            document = parser.getDocument();
            return stripper.getText(document);
        }
        catch (IOException e) {
            Log.getLog(getClass()).error("Unable to index PDF: " + e.toString());
            throw e;
        }
        finally {
            if (document != null) {
                try {
                    document.close();
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    public final static void main(String[] args) {
        PdfFilter filter = new PdfFilter();
        try {
            System.out.println("contents:\n" + filter.filter(new java.io.FileInputStream(
                "D:/Work/Documents/ebooks/thread-pooling.pdf")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
}
