package com.tms.wiki.model;

import info.bliki.wiki.filter.WikiModel;
import info.bliki.wiki.filter.WikipediaParser;

/**
 * Date: Jan 1, 2007
 * Time: 11:18:07 PM
 */
public class WikiConverter {
    public static void main(String[] args)
	{
		WikiModel wikiModel =
                            new WikiModel("http://www.tmswiki.com/wiki/${image}",
                                          "http://www.tmswiki.com/wiki/${title}");
	/*	String htmlStr = wikiModel.render("This is a simple [[Hello World]] wiki tag. more '''example'''" +
                " = one = == two == === three ===");*/
        
       
        String results = WikipediaParser.parse("This is a simple [[Hello World]] wiki tag. more   '''bold''' \r\n==one==",wikiModel);
        System.out.println(results);
		
	}
    
    public static String convertToHtml(String story){
    	WikiModel wikiModel =
            new WikiModel("${image}",
                          "wikiSearch?title=${title}");    
    	    	
    	String results = WikipediaParser.parse(story,wikiModel);
    	
    	return results;
    }
}
