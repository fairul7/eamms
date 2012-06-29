/*
 * Created on Jun 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.ui;

import java.util.ArrayList;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.collab.myfolder.model.FileFolder;
import com.tms.collab.myfolder.model.MyFolderModule;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileFolderViewWidget extends Widget{
	public static final String EVENT_DOWNLOAD = "download";
	
	private FileFolder fileFolder;
	private String mfId;
	private String[] selectedUsers;
	private boolean canEdit;
	
	private String path;
	
	public void init(){
		loadFileFolder();
	}

	public FileFolderViewWidget(){
		
	}
	
	public FileFolderViewWidget(String name){
		super(name);
	}
	
	public void onRequest(Event evt){
		loadFileFolder();
	}
	
	public Forward actionPerformed(Event evt){
		if(EVENT_DOWNLOAD.equals(evt.getType())){
			path = "/storage" + updateFileAccess(evt);
			evt.getRequest().setAttribute("path", path);
			return new Forward("download");
		}

		return new Forward("mainPg");
	}
	
	public String updateFileAccess(Event evt){
		try{
			MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
			
			mf.updateFileAccessCount(mfId, getWidgetManager().getUser().getId());
			FileFolder f = mf.loadFileFolder(mfId);
			
			mf.logAction(Application.getInstance().getCurrentUser().getId(), mfId, "download");
			
			return f.getFilePath();
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in updateting file access: " + e.getMessage(), e);
			return null;
		}
		
	}
	
	public void loadFileFolder(){
		try{
			User user = new User();
			User loginUser = Application.getInstance().getCurrentUser();
			ArrayList selectedUserList;
			String sharedUser = new String();
			ArrayList deletedUserList = new ArrayList();
			boolean authorize = true;
			boolean isDeletedUserFileFolder = false;
			
			SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
			boolean delUserFolderView = service.hasPermission(loginUser.getId(), MyFolderModule.PERMISSION_DEL_USER_FOLDER, null, null);
			
			if(getMfId() != null){
				MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
				
				fileFolder = mf.loadFileFolder(getMfId());
				
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
							
							canEdit = false;
							if("1".equals(parent.getFileAccess())){
								
								if(isDeletedUserFileFolder && delUserFolderView){
									canEdit = true;
									authorize = true;
								}else{
									authorize = false;
								}
							}
							else if("2".equals(parent.getFileAccess())){
								
								if(isDeletedUserFileFolder && delUserFolderView){
									authorize = true;
									canEdit = true;
								}else{
									authorize = true;
									canEdit = false;
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
									canEdit = true;
								}
								else if(match){
									authorize = true;
									canEdit = false;
								}
								else{
									authorize = false;
								}
							}
						}else{
							canEdit = true;
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
									canEdit = true;
									authorize = true;
								}else{
									authorize = false;
								}
								
							}
							else if("2".equals(fileFolder.getFileAccess())){
								
								if(isDeletedUserFileFolder && delUserFolderView){
									authorize = true;
									canEdit = true;
								}else{
									authorize = true;
									canEdit = false;
								}
								
							}
							//if parent folder access is selected user
							else if("3".equals(fileFolder.getFileAccess())){
								boolean match = false;
								
								// get list of shared user of the parent folder
								selectedUserList = mf.getFolderSharedUsers(getMfId());
								
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
									canEdit = true;
								}
								else if(match){
									authorize = true;
									canEdit = false;
								}
								else{
									authorize = false;
								}
							}
						}else{
							canEdit = true;
						}
						
						if("0".equals(fileFolder.getParentId())){
							canEdit = false;
						}
					}
					
					if(!authorize){
						fileFolder = null;
					}else{
						//get the list of shared user if folder access is public
						if(fileFolder.isFolder() && "3".equals(fileFolder.getFileAccess())){
							ArrayList sharedUserList = new ArrayList();
							sharedUserList = mf.getFolderSharedUsers(getMfId());
							
							selectedUsers = new String[sharedUserList.size()];
							
							for(int i=0; i<sharedUserList.size(); i++){
								 user = service.getUser((String)sharedUserList.get(i));
								 selectedUsers[i] = user.getName();
							}
						}
					}
				}
			}
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in file folder view: " + e.getMessage(), e);
		}
	}
	
	public String getDefaultTemplate(){
		return "myfolder/fileFolderView";
	}
	
	
	

	public boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	/**
	 * @return Returns the selectedUsers.
	 */
	public String[] getSelectedUsers() {
		return selectedUsers;
	}
	/**
	 * @param selectedUsers The selectedUsers to set.
	 */
	public void setSelectedUsers(String[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	/**
	 * @return Returns the fileFolder.
	 */
	public FileFolder getFileFolder() {
		return fileFolder;
	}
	/**
	 * @param fileFolder The fileFolder to set.
	 */
	public void setFileFolder(FileFolder fileFolder) {
		this.fileFolder = fileFolder;
	}
	/**
	 * @return Returns the mfId.
	 */
	public String getMfId() {
		return mfId;
	}
	/**
	 * @param mfId The mfId to set.
	 */
	public void setMfId(String mfId) {
		this.mfId = mfId;
	}
}
