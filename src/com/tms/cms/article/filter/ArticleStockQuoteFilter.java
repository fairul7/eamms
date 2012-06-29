package com.tms.cms.article.filter;

import javax.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;

/**
 * A filter that is used to parse and process special embedded comments.
 * To add more filters, subclass this class and
 * register with the ArticleFilterFactory class.
 */
public class ArticleStockQuoteFilter extends ArticleFilter {

    public String getPrefix() {
        return "<!-- StockQuote: ";
    }
    
    public String getSuffix() {
        return " -->";
    }

    public String process(HttpServletRequest request, String params) {
        try {
            // parse parameters
            StringTokenizer st = new StringTokenizer(params, " ");
            String exchangeName = st.nextToken();
            String counterName = st.nextToken();

            // generate HTML
            StringBuffer output = new StringBuffer();
            output.append("<p><a href=\"#\" onclick=\"try { articleStockQuoteFilter('" + exchangeName + "','" + counterName + "'); } catch(e) {}; return false\">");
            output.append("Stock Quote: " + exchangeName + " " + counterName);
            output.append("</a></p>");
            return output.toString();
        }
        catch (Exception e) {
            return "<!-- " + params + ": " + e.toString() + " -->";
        }
    }

}
