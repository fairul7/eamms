package com.tms.sam.po.model;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tms.collab.messaging.model.StorageFileDataSource;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

public class DownloadAttachmentServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		PrePurchaseModule module = (PrePurchaseModule)Application.getInstance().getModule(PrePurchaseModule.class);
		SupplierModule suppModule = (SupplierModule)Application.getInstance().getModule(SupplierModule.class);
				
		String attachmentID = request.getParameter("attachID");
		String type = request.getParameter("type");
		//module.getAttachment();
		
		StorageService ss;
        StorageFile sf;
        AttachmentObject obj = null;
        
        try{
        	if(attachmentID != null){
        		if(type.equals("supplier")){
        			obj = suppModule.downloadFile(attachmentID);
        		}else{
        			obj = module.downloadFile(attachmentID);
        		}
        		
				ss = (StorageService) Application.getInstance().getService(StorageService.class);
	            sf = new StorageFile(obj.getPath()+ obj.getNewFileName());
	            sf = ss.get(sf);
	            
	            
	            String newName = sf.getName();
	            response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");
	            StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());
	            
			}else{
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            	return;
			}
        	
        	
        }catch(Exception e){
			Log.getLog(getClass()).error("error in download file servlet: " + e.getMessage(), e);
			PrintWriter out = response.getWriter();
            out.print(e.getMessage());
		}
	}
}
