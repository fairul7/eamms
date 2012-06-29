package com.tms.wiki.model;

import java.util.StringTokenizer;

public class WikiUtil {

	public static String splitTags(String tags){
		StringBuffer result = new StringBuffer("");
		if(!tags.equals("")){
			StringTokenizer st = new StringTokenizer(tags,",");
			while(st.hasMoreTokens()){
				String temp = st.nextToken().trim();
				if(result.length()>0){
					result.append(", ");
				}
				result.append("<a class=\"tag\" href=\"categoryTagSearch.jsp?keyword="+temp+"&type=tag\"/>"+temp+"</a>");
			}
		}
		return result.toString();	
	}
	
	public static String removeToc(String story){
		StringBuffer sb = new StringBuffer(story);				
		int start = sb.indexOf("<table id=\"toc\" border=\"0\">");
		
		int end = 0;
		if(start!=-1 ){
			end = sb.indexOf("</table><hr/>");
			sb.replace(start, end+13," ");
		}		
		return sb.toString();
		
	}
}
 