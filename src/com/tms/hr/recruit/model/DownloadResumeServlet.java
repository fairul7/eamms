package com.tms.hr.recruit.model;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import com.tms.collab.messaging.model.StorageFileDataSource;

public class DownloadResumeServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Application app = Application.getInstance();
		SecurityService service = (SecurityService) app.getService(SecurityService.class);
		String resumeId = request.getParameter("resumeId");
		StorageService ss;
        StorageFile sf;
        
        if(resumeId != null){
	        try{
					ss = (StorageService) app.getService(StorageService.class);
			        sf = new StorageFile("/recruit/"+ resumeId);
			        sf = ss.get(sf);
			             
					String newName = sf.getName();
			        response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");
			        StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());
			}
			catch(Exception e){
				Log.getLog(getClass()).error("error in download file servlet: " + e.getMessage(), e);
				PrintWriter out = response.getWriter();
	            out.print(e.getMessage());
			}
        }	
	}
	
}
