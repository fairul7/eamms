package com.tms.cms.article.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * A filter that is used to parse and process special embedded comments.
 * To add more filters, subclass this class and
 * register with the ArticleFilterFactory class.
 */
public abstract class ArticleFilter {

    public abstract String getPrefix();

    public abstract String getSuffix();

    public abstract String process(HttpServletRequest request, String params);

    /**
     * Performs the filtering for an article.
     * @return a String representing the extracted text.
     */
    public String filter(HttpServletRequest request, String contents) {
        if (contents == null) {
            return null;
        }

        StringBuffer result = new StringBuffer();
        int base = 0;
        int start = contents.indexOf(getPrefix());
        int end;
        String params;
        String filtered;
        while(start >= 0) {
            end = contents.indexOf(getSuffix(), start);
            if (end < 0) {
                break;
            }
            result.append(contents.substring(base, start));
            params = contents.substring(start + getPrefix().length(), end);
            filtered = process(request, params);
            result.append(filtered);
            base = start;
            start = contents.indexOf(getPrefix(), end);
        }
        result.append(contents.substring(base, contents.length()));

        return result.toString();
    }
    
}
