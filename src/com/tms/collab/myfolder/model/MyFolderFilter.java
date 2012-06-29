package com.tms.collab.myfolder.model;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;

public class MyFolderFilter implements Filter{
	
	private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException{
    	String path;
        try{
        	HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;
            
            //check path
            path = getPathInfo(req, resp);
            
            if (path.startsWith("/my_folder/")) {
            	
                int dotPos = path.lastIndexOf(".");
                int spacePost = path.lastIndexOf(" ");
                
                String mfId = path.substring(spacePost + 1, dotPos);
                
                if(!isValidUser(mfId.trim(), req)){
                	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                	return;
                }
            }
            
            filterChain.doFilter(request, response);
        }
        catch(Exception e){
        	Log.getLog(getClass()).error("error in my folder filter: " + e.getMessage(), e);
        }
    }
    
    public boolean isValidUser(String mfId, HttpServletRequest req){
    	
    	
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
    		
    		//Log.getLog(getClass()).info("----- fileFodler ==> " + fileFolder.getFileName());
    		
    		if(fileFolder != null){
				
				//if mfid is a file
				if(!fileFolder.isFolder()){
					//get the folder of the file
					FileFolder parent = mf.loadFileFolder(fileFolder.getParentId());
					
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
    
    protected String getPathInfo(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        if (path == null) {
            path = request.getParameter("name");
        }
        return (path != null) ? path : "";
    }
    
    public void destroy() {
    }

}
