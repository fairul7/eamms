/*
 * DownloadFileServlet
 * Date Created: Jul 12, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.collab.messaging.model.StorageFileDataSource;

public class DownloadMediaServlet extends HttpServlet {
    private Log log = Log.getLog(getClass());
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MediaModule mediaModule = (MediaModule)Application.getInstance().getModule(MediaModule.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		User loginUser = service.getCurrentUser(request);
		String id = request.getParameter("id");
		StorageService ss;
        StorageFile sf;
		
		try{
			if(! "".equals(id.trim())){
				if(mediaModule.isAccessible(id, request)){
					MediaObject media = mediaModule.selectMedia(id);
					
					ss = (StorageService) Application.getInstance().getService(StorageService.class);
		            sf = new StorageFile("/medialib/" + media.getAlbumId() + "/" + media.getFileName());
		            sf = ss.get(sf);
		            
		            int spacePos = sf.getName().lastIndexOf(" ");
		            int dotPos = sf.getName().lastIndexOf(".");
		            
		            String newName = sf.getName().substring(0, spacePos) + sf.getName().substring(dotPos, sf.getName().length());
		            
		            // Update media statistics
		            MediaStatObject mediaStat = new MediaStatObject();
		            mediaStat.setId(UuidGenerator.getInstance().getUuid());
		            mediaStat.setActionType("download");
		            mediaStat.setCreatedBy(loginUser.getId());
		            mediaStat.setIp(request.getRemoteAddr());
		            mediaStat.setMediaId(id);
		            mediaModule.insertMediaStat(mediaStat);
		            
		            response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");
		            StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());
				}
				else {
				    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				    return;
				}
			}
		}
		catch(Exception error){
			log.error("error downloading media file, id: " + id, error);
		}
	}
}
