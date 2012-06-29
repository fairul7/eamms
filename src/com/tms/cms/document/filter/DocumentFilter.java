package com.tms.cms.document.filter;

import java.io.IOException;
import java.io.InputStream;

/**
 * A filter that is used to parse and extract text from 
 * a document for indexing purposes. This default implementation
 * reads the whole content and is suitable for text files.
 * To handle different document types, subclass this class and 
 * register with the DocumentFilterFactory class.
 */
public class DocumentFilter {
 
    /**
     * Performs the filtering (parsing and/or text extraction).
     * @param in The InputStream of the source content.
     * @return a String representing the extracted text.
     */
    public String filter(InputStream in) throws IOException {
        String content = "";
        byte[] buffer = new byte[4096];
        int len = in.read(buffer);
        while(len > 0) {
            content += new String(buffer, 0, len,"UTF-8");
            len = in.read(buffer);
        }
        return content;
    }
    
}
