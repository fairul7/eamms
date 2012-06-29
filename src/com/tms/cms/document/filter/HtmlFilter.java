package com.tms.cms.document.filter;

/*
import com.kizna.html.HTMLNode;
import com.kizna.html.HTMLParser;
import com.kizna.html.HTMLReader;
import com.kizna.html.HTMLStringNode;
*/

import org.htmlparser.util.ParserUtils;
import org.htmlparser.visitors.TextExtractingVisitor;
import org.htmlparser.Parser;
import org.htmlparser.NodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import kacang.util.Log;

/**
 * Filters an HTML document by stripping away
 * HTML tags and comments.
 */
public class HtmlFilter extends DocumentFilter {
    
    public String filter(InputStream in) throws IOException {
        return filterHtml(super.filter(in));
    }

    protected String filterHtml(String s) {
        try {
            Parser parser = new Parser(new NodeReader(new StringReader(s), s.length()));
            TextExtractingVisitor visitor = new TextExtractingVisitor();
            parser.visitAllNodesWith(visitor);
            String cleanText = ParserUtils.removeEscapeCharacters(visitor.getExtractedText());
            return cleanText;
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error filtering HTML " + e.toString());
            return s;
        }

/*
        StringBuffer text = new StringBuffer();
        BufferedReader r = new BufferedReader(new StringReader(s));
        HTMLReader reader = new HTMLReader(r, s.length());
        HTMLParser parser = new HTMLParser(reader);
        for (Enumeration e = parser.elements();e.hasMoreElements();) {
            HTMLNode node = (HTMLNode)e.nextElement();
            if (node instanceof HTMLStringNode) {
                HTMLStringNode stringNode = (HTMLStringNode)node;
                text.append(stringNode.getText());
                text.append(" ");
            }
         }
         return text.toString();
*/
    }

}
