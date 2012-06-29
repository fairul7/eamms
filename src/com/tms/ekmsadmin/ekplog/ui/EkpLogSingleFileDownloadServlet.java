package com.tms.ekmsadmin.ekplog.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.util.Log;

import com.tms.collab.messaging.model.StorageFileDataSource;
import com.tms.ekmsadmin.ekplog.model.EkpLogModule;

public class EkpLogSingleFileDownloadServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("fileName") != null) {
			String fileName = request.getParameter("fileName");
			
			if(!"".equals(fileName)) {
				EkpLogModule logModule = (EkpLogModule) Application.getInstance().getModule(EkpLogModule.class);
				String tomcatLogsRealPath = logModule.getTomcatLogsRealPath();
				if(tomcatLogsRealPath != null && !"".equals(tomcatLogsRealPath)) {
					File tomcatLogsFolder = new File(tomcatLogsRealPath);
					
					if(tomcatLogsFolder.exists()) {
						try {
							File logFile = new File(tomcatLogsRealPath + fileName);
							if(logFile.exists()) {
								FileInputStream fileInputStream = new FileInputStream(logFile);
								response.setHeader("Content-Disposition", "attachment; filename=\"" + logFile.getName() + "\"");
					            StorageFileDataSource.copy(fileInputStream, response.getOutputStream());
							}
						}
						catch(Exception error) {
							Log.getLog(getClass()).error(error, error);
						}
					}
				}
			}
		}
	}
}
