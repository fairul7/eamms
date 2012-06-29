package com.tms.collab.myfolder.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

import com.tms.collab.messaging.model.StorageFileDataSource;

public class DownloadFileServlet extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		User loginUser = service.getCurrentUser(request);
		String mfId = request.getParameter("id");
		
		if(mfId == null){
			mfId = request.getParameter("mfId");
		}
		
		StorageService ss;
        StorageFile sf;
		
		try{
			if(mfId != null){
				
				if(isAuthorizedUser(mfId, request)){
					
					FileFolder f = mf.loadFileFolder(mfId);
					
					ss = (StorageService) Application.getInstance().getService(StorageService.class);
		            sf = new StorageFile(f.getFilePath());
		            sf = ss.get(sf);
		            
		            int spacePos = sf.getName().lastIndexOf(" ");
		            int dotPos = sf.getName().lastIndexOf(".");
		            
		            String newName = sf.getName().substring(0, spacePos) + sf.getName().substring(dotPos, sf.getName().length());
		            
		            mf.updateFileAccessCount(mfId, loginUser.getId());
		            mf.logAction(loginUser.getId(), mfId, "download");
		            
		            response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"");
		            StorageFileDataSource.copy(sf.getInputStream(), response.getOutputStream());
		            
				}else{
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                	return;
				}
			}
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in download file servlet: " + e.getMessage(), e);
			PrintWriter out = response.getWriter();
            out.print(e.getMessage());
		}
		
		
	}
	
public boolean isAuthorizedUser(String mfId, HttpServletRequest req){
    	
		ArrayList selectedUserList;
		String sharedUser = new String();
		ArrayList deletedUserList = new ArrayList();
		boolean authorize = true;
		boolean isDeletedUserFileFolder = false;
		
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		User loginUser = service.getCurrentUser(req);
		
		MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
		FileFolder fileFolder;
		
    	try{
    		boolean delUserFolderView = service.hasPermission(loginUser.getId(), MyFolderModule.PERMISSION_DEL_USER_FOLDER, null, null);
    		
    		fileFolder = mf.loadFileFolder(mfId);
    		
    		if(fileFolder != null){
				
				//if mfid is a file
				if(!fileFolder.isFolder()){
					
					//get the folder of the file
					FileFolder parent = mf.loadFileFolder(fileFolder.getParentId());
					
					//check whether the file is belongs to the deleted user
					deletedUserList = mf.getDeletedUserList();
					
					if(deletedUserList != null && deletedUserList.size() > 0){
						//if the file belongs to a deleted user and the login user have the authority to edit deleted user file/folder
						for(int i=0; i<deletedUserList.size(); i++){
							String delUser = (String)deletedUserList.get(i);
							
							if(delUser.equals(parent.getUserId())){
								isDeletedUserFileFolder = true;
								break;
							}
						}
					}
					
					//if parent folder access is personal but the user id of the parent folder is not current login user 
					if(!loginUser.getId().equals(parent.getUserId())){
						
						if("1".equals(parent.getFileAccess())){
							
							if(isDeletedUserFileFolder && delUserFolderView){
								authorize = true;
							}else{
								authorize = false;
							}
						}
						else if("2".equals(parent.getFileAccess())){
							
							if(isDeletedUserFileFolder && delUserFolderView){
								authorize = true;
							}else{
								authorize = true;
							}
							
						}
						//if parent folder access is selected user
						else if("3".equals(parent.getFileAccess())){
							
							boolean match = false;
							// get list of shared user of the parent folder
							selectedUserList = mf.getFolderSharedUsers(parent.getId());
							
							//check whether the login user is in the list 
							for(int i=0; i<selectedUserList.size(); i++){
								sharedUser = (String) selectedUserList.get(i);
								
								if(sharedUser.equals(loginUser.getId())){
									match = true;
									break;
								}
							}
							
							if(isDeletedUserFileFolder && delUserFolderView){
								authorize = true;
							}
							else if(match){
								authorize = true;
							}
							else{
								authorize = false;
							}
						}else if("4".equals(parent.getFileAccess())){
							authorize = true;
						}else{
							authorize = false;
						}
					}
					
			    //if mf is a folder
				}else{
					
					deletedUserList = mf.getDeletedUserList();
					
					if(deletedUserList != null && deletedUserList.size() > 0){
						//if the file belongs to a deleted user and the login user have the authority to edit deleted user file/folder
						for(int i=0; i<deletedUserList.size(); i++){
							String delUser = (String)deletedUserList.get(i);
							
							if(delUser.equals(fileFolder.getUserId())){
								isDeletedUserFileFolder = true;
								break;
							}
						}
					}
					
					//the folder is not mine
					if(!loginUser.getId().equals(fileFolder.getUserId())){
						
						if("1".equals(fileFolder.getFileAccess())){
							
							if(isDeletedUserFileFolder && delUserFolderView){
								authorize = true;
							}else{
								authorize = false;
							}
							
						}
						else if("2".equals(fileFolder.getFileAccess())){
							
							if(isDeletedUserFileFolder && delUserFolderView){
								authorize = true;
							}else{
								authorize = true;
							}
							
						}
						//if parent folder access is selected user
						else if("3".equals(fileFolder.getFileAccess())){
							boolean match = false;
							
							// get list of shared user of the parent folder
							selectedUserList = mf.getFolderSharedUsers(mfId);
							
							//check whether the login user is in the list 
							for(int i=0; i<selectedUserList.size(); i++){
								sharedUser = (String) selectedUserList.get(i);
								
								if(sharedUser.equals(loginUser.getId())){
									match = true;
									break;
								}
							}
							
							if(isDeletedUserFileFolder && delUserFolderView){
								authorize = true;
							}
							else if(match){
								authorize = true;
							}
							else{
								authorize = false;
							}
						}
						else if("4".equals(fileFolder.getFileAccess())){
							authorize = true;
						}else{
							authorize = false;
						}
					}
				}				
			}
    		
    		return authorize;
    	}
    	catch(Exception e){
    		Log.getLog(getClass()).error("error in my folder filter: " + e.getMessage(), e);
    		return false;
    	}
    	
    }
	
	

}
