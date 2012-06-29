package com.tms.cms.document.filter;

/**
 * A Factory class that returns a DocumentFilter that is appropriate
 * for a specifed content MIME type.
 */
public class DocumentFilterFactory {
    
    public static final String MIME_TEXT = "text/plain";
    public static final String MIME_HTML = "text/html";
    public static final String MIME_XML = "text/xml";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_PS = "application/postscript";
    public static final String MIME_RTF = "application/rtf";
    public static final String MIME_WORD = "application/msword";
    public static final String MIME_EXCEL = "application/msexcel";
    public static final String MIME_POWERPOINT = "application/mspowerpoint";
 
    /**
     * @param contentType The MIME type e.g. text/plain
     * @return A DocumentFilter object for the specified content type, 
     *         null if no filter is available.
     */
    public static DocumentFilter getDocumentFilter(String fileName, String contentType) {
        if (fileName == null)
            return null;
        
        if (MIME_TEXT.equals(contentType)) {
            return new DocumentFilter();
        }
        else if (MIME_HTML.equals(contentType)) {
            return new HtmlFilter();
        }
        else if (MIME_XML.equals(contentType) || fileName.toLowerCase().endsWith(".xml")) {
            return new HtmlFilter();
        }
        else if (MIME_PDF.equals(contentType) || fileName.toLowerCase().endsWith(".pdf")) {
            return new PdfFilter();
        }
        else if (MIME_RTF.equals(contentType) || fileName.toLowerCase().endsWith(".rtf")) {
            return new RtfFilter();
        }
        else if (MIME_WORD.equals(contentType) || fileName.toLowerCase().endsWith(".doc")) {
            return new WordFilter();
        }
        else if (MIME_EXCEL.equals(contentType) || fileName.toLowerCase().endsWith(".xls")) {
            return new ExcelFilter();
        }
        else if (MIME_POWERPOINT.equals(contentType) || fileName.toLowerCase().endsWith(".ppt") || fileName.toLowerCase().endsWith(".pps")) {
            return new PowerpointFilter();
        }
        else {
            return null;
        }
    }
    
}
