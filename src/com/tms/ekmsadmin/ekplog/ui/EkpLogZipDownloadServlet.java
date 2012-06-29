package com.tms.ekmsadmin.ekplog.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import com.tms.collab.messaging.model.StorageFileDataSource;
import com.tms.ekmsadmin.ekplog.model.EkpLogDao;
import com.tms.ekmsadmin.ekplog.model.EkpLogZipFileDownloadObject;

public class EkpLogZipDownloadServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session != null) {
			if(session.getAttribute("logZipDownloadObject") != null) {
				EkpLogZipFileDownloadObject downloadObject = (EkpLogZipFileDownloadObject) session.getAttribute("logZipDownloadObject");
				
				try {
	    			StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
	    			StorageFile sf = new StorageFile("/" + EkpLogDao.STORAGE_ROOT + "/" + downloadObject.getZipOutputFileName());
					sf = storage.get(sf);
					
					response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadObject.getZipDownloadFileName() + "\"");
		            StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());
		            
					session.setAttribute("logZipDownloadObject", null);
				}
				catch(Exception error) {
					Log.getLog(getClass()).error(error, error);
				}
			}
		}
	}
}
