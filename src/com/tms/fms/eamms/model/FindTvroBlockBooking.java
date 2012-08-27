package com.tms.fms.eamms.model;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;

public class FindTvroBlockBooking extends HttpServlet 
{
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String tvroServiceId = request.getParameter("tvroServiceId");
		
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		boolean blockbooking = em.isBlockbookingTvroService(tvroServiceId);
		if(blockbooking)
		{
			out.print("1");
		}
		else
		{
			out.print("0");
		}
	}
}
