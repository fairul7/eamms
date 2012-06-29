
package com.tms.collab.myfolder.model;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultModule;
import kacang.services.indexing.SearchResult;
import kacang.services.indexing.SearchResultItem;
import kacang.services.indexing.SearchableModule;
import kacang.services.security.Group;
import kacang.services.security.SecurityEvent;
import kacang.services.security.SecurityEventListener;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageService;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

public class MyFolderModule extends DefaultModule implements SearchableModule, SecurityEventListener{
	
	public static final String ROOT_PATH = "/my_folder/";
	public static final String PERMISSION_QUOTA = "com.tms.collab.myfolder.quota";
	public static final String PERMISSION_DEL_USER_FOLDER = "com.tms.collab.myfolder.deletedUser";
	public static final String PERSONAL_ACCESS = "Personal";
	public static final String EKP_ACCESS = "All EKP User";
	public static final String SELECTED_ACCESS = "Selected User(s)";
	public static final String CMS_PUBLIC_ACCESS = "Public Access";
	
	public void init(){
		super.init();
		SecurityService ser = (SecurityService) Application.getInstance().getService(SecurityService.class);
		ser.addEventListener(this);
	}
	
	public FileFolder formCmsPublicFolderTree() throws MyFolderException{
		MyFolderDao dao;
		try{
			ArrayList userList = new ArrayList();
			dao = (MyFolderDao) getDao();
			//get all the existing user 
			//Collection users = service.getUsers(new DaoQuery(), 0, -1, null, false);
			
			FileFolder rootFolder = new FileFolder();
			Collection subfolders = new ArrayList();
			
			//generate the dummy folder (Shared Folder)
			rootFolder = createDummySharedFolder("Public Folder");
			
			FileFolder tempFold = new FileFolder();
			
			userList = dao.selectAllUsers(null);
			
			if(userList != null && userList.size() > 0){
				for(int i=0; i<userList.size(); i++){
					String user = (String) userList.get(i);
					tempFold = getUserCmsPublicSharedFolder(user);
					
					if(tempFold.getSubFolders() != null){
						tempFold.setFileName(tempFold.getUserName() + " " + Application.getInstance().getMessage("mf.label.folder", "Folder"));
						subfolders.add(tempFold);
					}
				}
			}
			
			rootFolder.setSubFolders(subfolders);
	        return rootFolder;
		}
		catch(Exception e){
			throw new MyFolderException("error in formDeletedUserFolderTree: " + e.getMessage(), e);
		}
	} 
	
	public FileFolder getUserCmsPublicSharedFolder(String selectedUser) throws MyFolderException{
		MyFolderDao dao = (MyFolderDao) getDao();
		//get all the user's folders
		try{
			Collection folders = dao.selectPublicFolder(selectedUser);
			FileFolder rootFolder = new FileFolder();
			FileFolder folder;
			FileFolder folderTemp;
			
			rootFolder.setSubFolders(null);
			
			//place folders into map
			Map folderMap = new SequencedHashMap();
	        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
	        	folderTemp = (FileFolder) iterator.next();
	        	
	        	if("0".equals(folderTemp.getParentId())){
	        		rootFolder = folderTemp;
	        	}
	        	folderMap.put(folderTemp.getMfId(), folderTemp);
	        }
	        
	        //iterate thru folders to formulate the tree
	        FileFolder parent = new FileFolder();
	        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
	            folder = (FileFolder) iterator.next();
	            if(! "0".equals(folder.getParentId())){
		            parent = (FileFolder) folderMap.get(folder.getParentId());
		            if (parent == null) {
		                parent = rootFolder;
		            }
		            Collection subfolders = parent.getSubFolders();
		            if (subfolders == null) {
		                subfolders = new ArrayList();
		                parent.setSubFolders(subfolders);
		            }
		            subfolders.add(folder);
		            parent.setSubFolders(subfolders);
	            }
	        }
	        
	        return rootFolder;
	        
		}catch(DaoException e){
			Log.getLog(getClass()).error("Error in forming public tree: ", e);
			return null;
		}
		
	}
	
	public void handleSecurityEvent(SecurityEvent event){
		try{
			if(SecurityService.EVENT_USER_REMOVED.equals(event.getEventName())){
				User user = (User) event.getPrincipal();
				MyFolderDao dao = (MyFolderDao)getDao();
				dao.deleteSharedToUser(user.getId());
			}
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in handleSecurityEvent: " + e.getMessage(), e);
		}		
	}
	
	public SearchResult search(String query, int start, int rows, String userId){
		return search(query, start, rows, userId, null, null);
	}
	
	public SearchResult search(String s, int start, int rows, String userId, Date startDate, Date endDate){
		MyFolderDao dao;
		Map valueMap;
		FileFolder f;
		SearchResultItem item;
		SearchResult sr;
		String desc;
		try{
			dao = (MyFolderDao)getDao();
			sr = new SearchResult();
			
			Collection f_col = dao.selectSearch(userId, s, start, rows, "fileType", true);
			
			for(Iterator i=f_col.iterator(); i.hasNext(); ){
				f = (FileFolder)i.next();
				
				desc = f.getFileDescription();
				
				if(desc.trim().length() == 0){
					desc = "-Description is empty-";
				}
				
				if("0".equals(f.getParentId()) && "Folder".equalsIgnoreCase(f.getFileType()) && !userId.equals(f.getMfId())){
					f.setFileName(f.getUserName() + " Folder");
				}
				
				valueMap = new SequencedHashMap();
                valueMap.put(SearchableModule.SEARCH_PROPERTY_MODULE_CLASS, getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_OBJECT_CLASS, f.getClass().getName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_KEY, f.getMfId());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_TITLE, f.getFileName());
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DESCRIPTION, desc);
                valueMap.put(SearchableModule.SEARCH_PROPERTY_DATE, f.getLastModifiedDate());
                
                if("Folder".equalsIgnoreCase(f.getFileType())){
                	valueMap.put("folder", f);
                }else{
                	valueMap.put("file", f);
                }
                
                item = new SearchResultItem();
                item.setValueMap(valueMap);                

                sr.add(item);
			}
			
            int totalSize = dao.selectSearchCount(userId, s);
            
            sr.setTotalSize(totalSize);

            return sr;
		}
		catch(MyFolderDaoException e){
			Log.getLog(getClass()).error("error in search: " + e.getMessage(), e);
			return null;
		}
	}
	
	public SearchResult searchFullText(String query, int start, int rows, String userId, Date startDate, Date endDate){
		return null;
	}
	
	public SearchResult searchFullText(String query, int start, int rows, String userId){
		return null;
	}
	
	public boolean isSearchSupported(){
		return true;
	}
	
	public boolean isFullTextSearchSupported(){
		return false;
	}
	
	
	public void updateParentFileSizeRecur(String parentId) throws MyFolderException{
		
		MyFolderDao dao;
		try{
			dao = (MyFolderDao)getDao();
			if(!"0".equals(parentId)){
				FileFolder temp = dao.loadFileFolder(parentId);
				
				double fileSize = dao.getFolderFileSize(parentId);
				//log.info("=== update [" + temp.getFileName() + "] file size to " + fileSize);
				dao.updateParentSize(temp.getId(), fileSize);
				temp = dao.loadFileFolder(parentId);
				//log.info("=== updated [" + temp.getFileName() + "] file size to " + fileSize);
				updateParentFileSizeRecur(temp.getParentId());
			}
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	
	public String getFolderPath(String mfId) throws MyFolderException{
		
		try{
			String path = new String("");
			path = getFolderPathRecur(mfId, path);
			return path;
		}
		catch(Exception e){
			throw new MyFolderException ("error in loggging: " + e.getMessage(), e);
		}
	}
	
	public String getFolderPathRecur(String mfId, String path) throws MyFolderException{
		
		MyFolderDao dao;
		try{
			dao = (MyFolderDao) getDao();
			FileFolder f = dao.loadFileFolder(mfId);
			
			if(!"0".equals(f.getParentId())){
				path = ">" +  f.getFileName() + path;
				return getFolderPathRecur(f.getParentId(), path);
			}else{
				return path = f.getFileName().trim() + path;
			}
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException ("error in loggging: " + e.getMessage(), e);
		}
	}
	
	public void logAction(String userId, String mfId, String action) throws MyFolderException{
		MyFolderDao dao;
		try{
			dao = (MyFolderDao) getDao();
			UuidGenerator uuid = UuidGenerator.getInstance();
			String logId = uuid.getUuid();
			dao.createLog(logId, userId, mfId, action);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException ("error in loggging: " + e.getMessage(), e);
		}
		
	}
	
	public Collection getDeletedUserFilesFoldersList(String field, String folderId, String sort, boolean desc, int start, int rows) throws MyFolderException{
		MyFolderDao dao;
		try{
			ArrayList deletedUserList = getDeletedUserList();
			dao = (MyFolderDao)getDao();
			Collection col = null;
			
			if(deletedUserList != null && deletedUserList.size() > 0){
				FileFolder f = new FileFolder();
				String folderName = new String();
				String fileAccess = new String();
				
				col = dao.selectDeletedUserFilesFoldersList(field, folderId, deletedUserList, sort, desc, start, rows);
				
				for(Iterator i = col.iterator(); i.hasNext(); ){
					f = (FileFolder)i.next();
					
					if("0".equals(f.getParentId())){
						f.setFileName(f.getUserName() + " Folder");
					}
					if("Folder".equals(f.getFileType())){
						folderName = f.getFileName();
						NumberFormat formatter = NumberFormat.getInstance();
						formatter.setMaximumFractionDigits(0);
						
						folderName = folderName + " (" + getNumOfItemsInFolder(f.getMfId()) + ")";
						
						fileAccess = f.getFileAccess();
						if("1".equals(fileAccess)){
							fileAccess = PERSONAL_ACCESS;
						}else if("2".equals(fileAccess)){
							fileAccess = EKP_ACCESS;
						}else{
							fileAccess = SELECTED_ACCESS;
						}
						f.setFileAccess(fileAccess);
						f.setFileName(folderName);
					}else{
						f.setFileAccess("");
					}
				}
				
				return col;
				
			}else{
				return null;
			}
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException("error in getDeletedUserFilesFoldersCount: " + e.getMessage(), e);
		}
	}
	
	public int getDeletedUserFilesFoldersCount(String field, String folderId) throws MyFolderException{
		MyFolderDao dao;
		try{
			ArrayList deletedUserList = getDeletedUserList();
			dao = (MyFolderDao)getDao();
			int count = 0;
			
			if(deletedUserList != null && deletedUserList.size() > 0){
				count = dao.selectDeletedUserFileFoldersCount(field, folderId, deletedUserList);
			}
			return count;
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException("error in getDeletedUserFilesFoldersCount: " + e.getMessage(), e);
		}
	}
	
	
	
	public FileFolder createDummySharedFolder(String folderName){
		
		FileFolder sharedFolder = new FileFolder();
		sharedFolder.setMfId("-1");
		sharedFolder.setParentId("0");
		sharedFolder.setFileName(folderName);
		sharedFolder.setFileType("Folder");
		sharedFolder.setSubFolders(null);
		
		return sharedFolder;
	}
	
	public Collection selectDeletedUserFolder(String deletedUserId) throws MyFolderException{
		MyFolderDao dao;
		Collection folders;
		
		try{
			dao = (MyFolderDao)getDao();
			folders = dao.selectDeletedUserFolder(deletedUserId);
			return folders;
		}
		catch(Exception e){
			throw new MyFolderException("error in selectDeletedUserFolder(String, int) : " + e.getMessage(), e);
		}
	}
	
	public FileFolder getDeletedUserFolder(String deletedUserId) throws MyFolderException{

		//get all the user's folders
		Collection folders = selectDeletedUserFolder(deletedUserId);

		FileFolder rootFolder = new FileFolder();
		FileFolder folder;
		FileFolder folderTemp;
		
		rootFolder.setSubFolders(null);
		
		//place folders into map
		Map folderMap = new SequencedHashMap();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
        	folderTemp = (FileFolder) iterator.next();
        	
        	if("0".equals(folderTemp.getParentId())){
        		rootFolder = folderTemp;
        	}
        	folderMap.put(folderTemp.getMfId(), folderTemp);
        }
        
        //iterate thru folders to formulate the tree
        FileFolder parent = new FileFolder();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            folder = (FileFolder) iterator.next();
            if(! "0".equals(folder.getParentId())){
	            parent = (FileFolder) folderMap.get(folder.getParentId());
	            if (parent == null) {
	                parent = rootFolder;
	            }
	            Collection subfolders = parent.getSubFolders();
	            if (subfolders == null) {
	                subfolders = new ArrayList();
	                parent.setSubFolders(subfolders);
	            }
	            subfolders.add(folder);
	            parent.setSubFolders(subfolders);
            }
        }
        
        return rootFolder;
	}
	
	public FileFolder formDeletedUserFolderTree() throws MyFolderException{
		String tempUserId;
		try{
			
			FileFolder rootFolder = new FileFolder();
			Collection subfolders = new ArrayList();
			FileFolder tempFold = new FileFolder();
			
			//get all user from My Folder database
			ArrayList userList = getDeletedUserList();
			
			//generate dummy root folder
			rootFolder = createDummySharedFolder("Deleted User Folder");
			
			//if there is any deleted user
			if(userList.size() > 0){
				for(int i=0; i<userList.size(); i++){
					tempUserId = (String)userList.get(i);
					
					if(tempUserId != null){
						tempFold = getDeletedUserFolder(tempUserId);
						tempFold.setFileName(tempFold.getUserName() + " Folder");
						subfolders.add(tempFold);
					}
				}
			}
			
			rootFolder.setSubFolders(subfolders);
	        
	        return rootFolder;
		}
		catch(Exception e){
			throw new MyFolderException("error in formDeletedUserFolderTree: " + e.getMessage(), e);
		}
	}
	
	public ArrayList getDeletedUserList() throws MyFolderException{
		MyFolderDao dao;
		String tempUserId;
		try{
			SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
			dao = (MyFolderDao)getDao();
			
			//get all the existing system user 
			Collection users = service.getUsers(new DaoQuery(), 0, -1, null, false);
			
			//get all user from My Folder database
			ArrayList userList = dao.selectAllUsers(null);
			
			//find the user of My Folder that have been deleted from the system
			for(Iterator j = users.iterator(); j.hasNext(); ){
				User user = (User) j.next();
				//log.info("---- user.getUserName() ----------->  " + user.getUsername());
				for(int i=0; i<userList.size(); i++){
					tempUserId = (String)userList.get(i);
						
					if(tempUserId != null){
						//log.info("--- tempUserId != null ---");
						//log.info("---- userList.size() ===> " + userList.size());
						//log.info("---- users.size() ===> " + users.size());
						if(tempUserId.equals(user.getId())){
							//log.info("++++++++++++++++= tempUserId == user.getId() ---");
							userList.remove(i);
							j.remove();
							break;
						}
					}
				}
			}
			
			return userList;
		}
		catch(Exception e){
			throw new MyFolderException("error in getDeletedUserList: " + e.getMessage(), e);
		}
	}
	
	public Collection selectSpecificUserSharedFolder(String selectedUser, String userId) throws MyFolderException{
		MyFolderDao dao;
		Collection folders;
		try{
			dao = (MyFolderDao)getDao();
			folders = dao.selectNotMyFolder(selectedUser, userId);
			return folders;
		}
		catch(Exception e){
			throw new MyFolderException("error in getFilesFolders(String, int) : " + e.getMessage(), e);
		}
	}
	
	public FileFolder getSpecificUserSharedFolder(String selectedUser, String userId) throws MyFolderException{
		
		//get all the user's folders
		Collection folders = selectSpecificUserSharedFolder(selectedUser, userId);

		FileFolder rootFolder = new FileFolder();
		FileFolder folder;
		FileFolder folderTemp;
		
		rootFolder.setSubFolders(null);
		
		//place folders into map
		Map folderMap = new SequencedHashMap();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
        	folderTemp = (FileFolder) iterator.next();
        	
        	if("0".equals(folderTemp.getParentId())){
        		rootFolder = folderTemp;
        	}
        	folderMap.put(folderTemp.getMfId(), folderTemp);
        }
        
        //iterate thru folders to formulate the tree
        FileFolder parent = new FileFolder();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            folder = (FileFolder) iterator.next();
            if(! "0".equals(folder.getParentId())){
	            parent = (FileFolder) folderMap.get(folder.getParentId());
	            if (parent == null) {
	                parent = rootFolder;
	            }
	            Collection subfolders = parent.getSubFolders();
	            if (subfolders == null) {
	                subfolders = new ArrayList();
	                parent.setSubFolders(subfolders);
	            }
	            subfolders.add(folder);
	            parent.setSubFolders(subfolders);
            }
        }
        
        return rootFolder;
	}
	
	public FileFolder formSharedFolderTree(String[] selectedUsers, String userId) throws MyFolderException{
		MyFolderDao dao;
		try{
			ArrayList userList = new ArrayList();
			dao = (MyFolderDao) getDao();
			//get all the existing user 
			//Collection users = service.getUsers(new DaoQuery(), 0, -1, null, false);
			
			
			FileFolder rootFolder = new FileFolder();
			Collection subfolders = new ArrayList();
			
			//generate the dummy folder (Shared Folder)
			rootFolder = createDummySharedFolder(Application.getInstance().getMessage("mf.label.shareFolder","Shared Folders"));
			
			FileFolder tempFold = new FileFolder();
			
			userList = dao.selectAllUsers(null);
			
			if(userList != null && userList.size() > 0){
				for(int i=0; i<userList.size(); i++){
					String user = (String) userList.get(i);
					tempFold = getSpecificUserSharedFolder(user, userId);
					
					if(tempFold.getSubFolders() != null){
						tempFold.setFileName(tempFold.getUserName() + " " + Application.getInstance().getMessage("mf.label.folder", "Folder"));
						subfolders.add(tempFold);
					}
				}
			}
			
			//loop thru every user to form their shared folder tree
//			for(Iterator i = users.iterator(); i.hasNext(); ){
//				User user = (User) i.next();
//				tempFold = getSpecificUserSharedFolder(user.getId(), userId);
//				
//				//discard those user that doesn't share anything
//				if(tempFold.getSubFolders() != null){
//					tempFold.setFileName(tempFold.getUserName() + " Folder");
//					subfolders.add(tempFold);
//				}
//			}
			
			rootFolder.setSubFolders(subfolders);
			
	        return rootFolder;
		}
		catch(Exception e){
			throw new MyFolderException("error in get not my folder list: " + e.getMessage(), e);
		}
	}
	
	public Collection getNotMyFolder(String selectedUser, String userId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectNotMyFolder(selectedUser, userId);
			//return dao.selectNotMyFolder(selectedUsers, userId);
		}
		catch(Exception e){
			throw new MyFolderException("error in getFilesFolders(String, int) : " + e.getMessage(), e);
		}
	}
	
	public Collection getNotMyFolder(String[] selectedUsers, String userId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectNotMyFolder(userId);
			//return dao.selectNotMyFolder(selectedUsers, userId);
		}
		catch(Exception e){
			throw new MyFolderException("error in getFilesFolders(String, int) : " + e.getMessage(), e);
		}
	}
	
	public void updateFileAccessCount(String mfId, String userId){
		
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			dao.updateFileAccessCount(mfId, userId);
		}
		catch(MyFolderDaoException e){
			Log.getLog(getClass()).error("error in selecting root folder", e);
		}
	}
	
	public String getRootFolder(String userId){
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			String rootFolderId = dao.selectRootFolder(userId);
			
			return rootFolderId;
		}
		catch(MyFolderDaoException e){
			Log.getLog(getClass()).error("error in selecting root folder", e);
			return new String();
		}
	}

	public String getFileFolderPath(String mfId, String delimiter) {
		try {
			MyFolderDao dao = (MyFolderDao)getDao();
			String currentId = mfId;

			String fileFolderPath = "";
			while (currentId != null) {
				FileFolder fileFolder = dao.selectFileFolder(currentId);
				currentId = null;
				if (fileFolder != null) {
					String parentId = fileFolder.getParentId();
					if (!parentId.equals("0")) {
						fileFolderPath = delimiter + fileFolder.getFileName() + fileFolderPath;
						currentId = parentId;
					}
				}
			}
			return fileFolderPath;
		} catch (MyFolderDaoException e) {
			Log.getLog(getClass()).error("error in selecting file folder ", e);
			return "";
		}
	}

	public void createFiles(FileFolder file) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			dao.insertFileFolder(file);
			dao.updateFolderFileSize(file.getParentId(), "+", file.getFileSize());
			
			updateParentFileSizeRecur(file.getParentId());
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public void createFolderSharedUser(String mfId, String[] sharedUserId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			dao.insertFolderSharedUsers(mfId, sharedUserId);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public FileFolder loadFileFolder(String mfId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.loadFileFolder(mfId);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public ArrayList getFolderSharedUsers(String mfId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectFolderSharedUsers(mfId);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public void editFolder(FileFolder folder, String[] sharedUserId) throws MyFolderException{
		MyFolderDao dao;
		try{
			dao = (MyFolderDao)getDao();
			dao.updateFolder(folder);
			
			User user = Application.getInstance().getCurrentUser();
			
			dao.deleteFolderSharedUsers(folder.getId());
			
			if("3".equals(folder.getFileAccess())){
				if(sharedUserId != null || sharedUserId.length > 0){
					if(!sharedUserId.equals(user.getId())){
						dao.insertFolderSharedUsers(folder.getId(), sharedUserId);
					}
				}
			}
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	
	
	public void editFile(FileFolder file) throws MyFolderException{
		
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			dao.updateFile(file);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	

	public boolean noLooping(String dest, String fileToBeMoved) throws MyFolderException{
		try{
			
			FileFolder f = loadFileFolder(dest);
			
			//FileFolder temp = loadFileFolder(fileToBeMoved);
			//log.info("----- current dest ==> " + f.getFileName() + "(" + f.getId() + ")");
			//log.info("----- current filetobemoved ==> " + temp.getFileName() + "(" + temp.getId() + ")");
			
			if(fileToBeMoved.equals(f.getId())){
				return false;
			}
			else if(!"0".equals(f.getParentId())){
				return noLooping(f.getParentId(), fileToBeMoved);
			}
			//reach My Folder
			else{
				return true;
			}
		}
		catch(Exception e){
			throw new MyFolderException("error in moveFilesFolders: " + e.getMessage(), e);
		}
	}
	
	
	public void moveFilesFolders(String[] selected, String newParentId) throws MyFolderException{
		MyFolderDao dao;
		FileFolder temp = new FileFolder();
		FileFolder parent = new FileFolder();
		try{
			User user = Application.getInstance().getCurrentUser();
			dao = (MyFolderDao)getDao();
			parent = loadFileFolder(newParentId);
			
			if(selected != null || selected.length > 0){
				
				for(int i=0; i<selected.length; i++){
					if(!selected[i].equals(newParentId)){
						temp = loadFileFolder(selected[i]);
						
						if(!temp.isFolder()){
							dao.updateFilesFoldersParentId(selected[i], newParentId);
							dao.updateFolderFileSize(temp.getParentId(), "-", temp.getFileSize());
							updateParentFileSizeRecur(temp.getParentId());
							updateParentFileSizeRecur(newParentId);
							logAction(user.getId(), selected[i], "move");
						}
						else{
							//prevent infinite looping when move parent to its child
							if(noLooping(newParentId, selected[i])){
								//update parent id for the selected file/folder
								dao.updateFilesFoldersParentId(selected[i], newParentId);
								//minus the x parent file size
								dao.updateFolderFileSize(temp.getParentId(), "-", temp.getFileSize());
								//recursively update the file size of the x parent and new parent till the my folder 
								updateParentFileSizeRecur(temp.getParentId());
								updateParentFileSizeRecur(newParentId);
								logAction(user.getId(), selected[i], "move");
							}
						}
						
						//here only execute when in the <deleted user folder>, admin move a folder/file of a user to another user
						if(!parent.getUserId().equals(temp.getUserId())){
							dao.updateFileFolderUserId(selected[i], parent.getUserId(), parent.getUserName());
						}
					}
				}
			}
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException("error in moveFilesFolders: " + e.getMessage(), e);
		}
	}
	
	public void updateChildFilesFoldersFilePathRecur(String mfId) throws MyFolderException{
		
		MyFolderDao dao;
		try{
			dao = (MyFolderDao)getDao();
			FileFolder parent;
			FileFolder tempChild;
			String newFilePath = new String();
			String xFilePath = new String();
			String fileOriName = new String();
			
			//get all the children of this id
			Collection childFilesFolders = getChildFilesFolders(mfId);
				
				int lastSlashIndex = 0;
				for(Iterator iterator = childFilesFolders.iterator(); iterator.hasNext();){
					
					//get each child FileFolder
					tempChild = new FileFolder();
					tempChild = (FileFolder)iterator.next();
					
					//get new parent file path
					parent = loadFileFolder(mfId);
					
					if(tempChild.isFolder()){
						newFilePath = parent.getFilePath() + tempChild.getId() + "/";
					}else{
						xFilePath = tempChild.getFilePath();
						lastSlashIndex = xFilePath.lastIndexOf("/");
						fileOriName = xFilePath.substring(lastSlashIndex + 1);
						newFilePath = parent.getFilePath() + fileOriName;
					}
					
					//update this child's file path
					dao.updateFileFolderFilePath(tempChild.getId(), newFilePath);
					
					//update his child's file path 
					updateChildFilesFoldersFilePathRecur(tempChild.getId());
				}
		}
		catch(Exception e){
			throw new MyFolderException("error in updating child file path recursively: " + e.getMessage(), e);
		}
	
	}
	
	public int getSharedFilesCount(String fileName, String userId, String parentId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectSharedFilesCount(fileName, userId, parentId);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public int getFilesFoldersCount(String fileName, String userId, String parentId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectFilesFoldersCount(fileName, userId, parentId);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public Collection getSharedFilesList(String name, String parentId, String userId, String sort, boolean desc, int start, int rows) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectSharedFilesList(name, parentId, userId, sort, desc, start, rows);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public Collection getFilesFoldersList(String name, String parentId, String userId, String sort, boolean desc, int start, int rows) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			Collection filesFoldersList = dao.selectFilesFoldersList(name, parentId, userId, sort, desc, start, rows);
			
			FileFolder temp;
			String folderName = new String();
			String fileAccess = new String();
			
			for(Iterator i = filesFoldersList.iterator(); i.hasNext(); ){
				temp = new FileFolder();
				temp = (FileFolder) i.next();
				
				if("Folder".equals(temp.getFileType())){
					folderName = temp.getFileName();
					NumberFormat formatter = NumberFormat.getInstance();
					formatter.setMaximumFractionDigits(0);
					
					folderName = folderName + " (" + getNumOfItemsInFolder(temp.getMfId()) + ")";
					
					fileAccess = temp.getFileAccess();
					if("1".equals(fileAccess)){
						fileAccess = Application.getInstance().getMessage("mf.label.privateAccess",PERSONAL_ACCESS) ;
					}else if("2".equals(fileAccess)){
						fileAccess = Application.getInstance().getMessage("mf.label.publicAccess",EKP_ACCESS) ;
					}else if("3".equals(fileAccess)){
						fileAccess = Application.getInstance().getMessage("mf.label.selectedAccess",SELECTED_ACCESS) ;
					}else if("4".equals(fileAccess)){
						fileAccess = Application.getInstance().getMessage("mf.label.cmsPublicAccess",CMS_PUBLIC_ACCESS) ;
					}
					temp.setFileAccess(fileAccess);
					temp.setFileName(folderName);
					temp.setFileType(Application.getInstance().getMessage("mf.label.folder", "Folder"));
				}else{
					temp.setFileAccess("");
				}
			}
			
			return filesFoldersList;
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public String getNumOfItemsInFolder(String mfId) throws MyFolderException{
		MyFolderDao dao;
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectNumOfItemsInFolder(mfId);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException("error in get selected files/folders child" + e.getMessage(), e);
		}
	}
	
	public Collection getChildFilesFolders(String mfId) throws MyFolderException{
		MyFolderDao dao;
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectChildFileFolder(mfId);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException("error in get selected files/folders child" + e.getMessage(), e);
		}
	}
	
	public int deleteFilesFolders(String userId, String[] selected) throws MyFolderException{
		MyFolderDao dao;
		FileFolder temp = new FileFolder();
		String parentId = new String();
		try{
			
			if(selected != null || selected.length > 0){
				dao = (MyFolderDao)getDao();
				
		        for(int i=0; i<selected.length; i++){
					
		        	temp = loadFileFolder(selected[i]);
		        	parentId = temp.getParentId();
		        	//update the parent file size
					dao.updateFolderFileSize(temp.getParentId(), "-", temp.getFileSize());
					dao.deleteFolderSharedUsers(selected[i]);
					
					deleteFilesFoldersRecur(selected[i]);
				}
		        updateParentFileSizeRecur(parentId);
				return 0;
			}
			else{
				return 0;
			}
			
		}
		catch(Exception e){
			throw new MyFolderException("error in deleting selected files/folders" + e.getMessage(), e);
		}
	}
	
	public void deleteFilesFoldersRecur(String mfId){
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			Collection childFilesFolders = getChildFilesFolders(mfId);
			FileFolder temp;
			
			for(Iterator iterator = childFilesFolders.iterator(); iterator.hasNext();){
				temp = new FileFolder();
				temp = (FileFolder)iterator.next();
				
				deleteFilesFoldersRecur(temp.getId());
				dao.deleteFolderSharedUsers(temp.getId());
			}
			dao.deleteFilesFolders(mfId);
		}
		catch(Exception e){
			Log.getLog(getClass()).error("error in delete files folder recur", e);
		}
	}
	
	public double getUserUsedSpace(String userId) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			
			double usedSpace = dao.selectUsedSpace(userId);
			return usedSpace;
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public long getUserQuota(String userId) throws MyFolderException {
        
        long quota = 0;

        try {
            // get user groups
            SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
            Collection principalIds = new ArrayList();
            Collection groups = ss.getUserGroups(userId);
            for (Iterator i = groups.iterator(); i.hasNext();) {
                Group g = (Group) i.next();
                if (g.isActive()) {
                    principalIds.add(g.getId());
                }
            }

            if (principalIds.size() > 0) {
                // get max quota from groups
                MyFolderDao dao = (MyFolderDao) getDao();
                String[] principalIdArray = (String[])principalIds.toArray(new String[0]);
                Long quotaLong = dao.selectMaxPrincipalQuota(principalIdArray);
                if (quotaLong != null) {
                    quota = quotaLong.longValue();
                }
            }

        } catch(Exception e) {
            quota = 0;
        }

        return quota;
    }
	
	public FileFolder getFolderTree(String userId, String userName) throws MyFolderException{
		
		//get all the user's folders
		Collection folders = getFilesFolders(userId, userName, 1);
		
		FileFolder rootFolder = new FileFolder();
		FileFolder folder;
		FileFolder folderTemp;
		
		//place folders into map
		Map folderMap = new SequencedHashMap();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
        	folderTemp = (FileFolder) iterator.next();
        	
        	if("0".equals(folderTemp.getParentId())){
        		rootFolder = folderTemp;
        	}
        	folderMap.put(folderTemp.getId(), folderTemp);
        }
        
        //iterate thru folders to formulate the tree
        FileFolder parent = new FileFolder();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            folder = (FileFolder) iterator.next();
            if(! "0".equals(folder.getParentId())){
	            parent = (FileFolder) folderMap.get(folder.getParentId());
	            if (parent == null) {
	                parent = rootFolder;
	            }
	            Collection subfolders = parent.getSubFolders();
	            if (subfolders == null) {
	                subfolders = new ArrayList();
	                parent.setSubFolders(subfolders);
	            }
	            subfolders.add(folder);
	            parent.setSubFolders(subfolders);
            }
        }
        
        return rootFolder;
	}
	
	//folderFlag parameter is to select either
	//1 - folders only
	//other than 1 - files and folders
	public Collection getFilesFolders(String userId, String userName, int folderFlag) throws MyFolderException{
		MyFolderDao dao;
		Collection folders;
		
		try{
			dao = (MyFolderDao)getDao();
			//second parameter is a flag to select only folders / files and folders
			folders = dao.selectFilesFolders(userId, folderFlag);
			
			//if select folders only and none of the folder exist (new user)
			if(folderFlag == 1 && (folders.size() == 0 || folders == null)){
				UuidGenerator uuidGenerator = UuidGenerator.getInstance();
				
				StorageService ss;
				StorageDirectory dir;
				
				String path = new String();
				
				ss = (StorageService)Application.getInstance().getService(StorageService.class);
				
				path = ROOT_PATH + userId;
				dir = new StorageDirectory(path);
				
				FileFolder rootFolder = new FileFolder();
				
				rootFolder.setId(uuidGenerator.getUuid());
				rootFolder.setUserId(userId);
				rootFolder.setUserName(userName);
				rootFolder.setFileName(FileFolder.MY_FOLDER);
				rootFolder.setFilePath(path + "/");
				rootFolder.setFileSize(0);
				rootFolder.setFileDescription("This the main folder of each user and it can't be deleted!!!");
				rootFolder.setFileType("Folder");
				rootFolder.setFileAccess("2");
				rootFolder.setParentId("0");
				rootFolder.setAccessCountPrivate(0);
				rootFolder.setAccessCountPublic(0);
				
				createNewFileFolder(rootFolder);
				
				folders = dao.selectFilesFolders(userId, folderFlag);
				ss.store(dir);
			}
			
			return folders;
		}
		catch(Exception e){
			throw new MyFolderException("error in getFilesFolders(String, int) : " + e.getMessage(), e);
		}
		
	}
	
	public void createNewFileFolder(FileFolder fileFolder) throws MyFolderException{
		MyFolderDao dao;
		try{
			Application app = Application.getInstance();
			logAction(app.getCurrentUser().getId(), fileFolder.getId(), "create folder");
			dao = (MyFolderDao)getDao();
			dao.insertFileFolder(fileFolder);
			dao.updateFolderFileSize(fileFolder.getParentId(), "+", fileFolder.getFileSize());
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public int deleteGroupQuota(String[] selectedGroup) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.deleteGroupQuota(selectedGroup);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public Collection getGroupQuotaList(String name, String sort, boolean desc, int start, int rows) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectGroupQuotaList(name, sort, desc, start, rows);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
		
	}
	
	public int getGroupQuotaCount(String group) throws MyFolderException{
		MyFolderDao dao;
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.selectGroupQuotaCount(group);
		}catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
	}
	
	public int updateMyFolderQuota(String groupId, long quota) throws MyFolderException{
		MyFolderDao dao;
		
		if(groupId == null || groupId.trim().length() == 0){
			return 0;
		}
		
		try{
			dao = (MyFolderDao)getDao();
			return dao.updateMyFolderQuota(groupId, quota);
		}
		catch(MyFolderDaoException e){
			throw new MyFolderException(e.getMessage(), e);
		}
		
	}

}
