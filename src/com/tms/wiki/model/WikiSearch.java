package com.tms.wiki.model;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;

public class WikiSearch extends HttpServlet{

	protected void doGet(HttpServletRequest req	, HttpServletResponse res) throws ServletException, IOException {
		String category = req.getParameter("category");
		String tag = req.getParameter("tag");
		String keyword = req.getParameter("keyword");
				
		Collection col = null; 
		WikiArticle article = null;
		
		WikiModule module = (WikiModule) Application.getInstance().getModule(WikiModule.class);
		/*if(category!=null){
			col = module.searchArticlesByCategory(category);	
		} else if(tag !=null){
			col =  module.searchArticlesByTag(tag);
		} else*/ if(keyword!=null){
			article = module.searchArticlesByArticleTitle(keyword);
		}
		
		/*if(col!=null ){
			req.setAttribute("articles", col);
			RequestDispatcher rd = req.getRequestDispatcher("searchResults.jsp");
			rd.forward(req, res);
		} else*/ if(article!=null){
			/*RequestDispatcher rd = req.getRequestDispatcher("viewArticle.jsp?articleId="+article.getArticleId());
			rd.forward(req,res);*/
			res.sendRedirect("viewArticle.jsp?articleId="+article.getArticleId());
		}else{
			/*RequestDispatcher rd = req.getRequestDispatcher("advanceSearchResults.jsp"); 
			rd.forward(req, res);*/
			res.sendRedirect("advanceSearchResults.jsp");
		
		}	
		
	}
}


