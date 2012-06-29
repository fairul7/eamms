package com.tms.collab.myfolder.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kacang.model.DaoException;

public class MyFolderDaoMsSql extends MyFolderDao{
	
	public void init() throws DaoException{
		try {
	        super.update("CREATE TABLE mf_file ( " +
	        		"mfId varchar(35) NOT NULL default '',  " +
	        		"userId varchar(255) default '',  " +
	        		"fileName varchar(255) default '',  " +
	        		"filePath varchar(255) default '',  " +
	        		"fileDescription text,  " +
	        		"fileType varchar(255) default '',  " +
	        		"fileAccess char(1) default '0',  " +
	        		"lastModifiedDate datetime default '',  " +
	        		"parentId varchar(35) default '',  " +
	        		"accessCountPrivate integer default 0,  " +
	        		"accessCountPublic integer default 0,  " +
	        		"fileSize decimal(11,2) default 0.00,  " +
                    "userName varchar(255) default '', " +
	        		"PRIMARY KEY  (mfId)" +
	        		")",null);
	        
	        super.update("CREATE TABLE mf_log (  " +
	        		"id varchar(35) default '0',  " +
	        		"userId varchar(255) default '',  " +
	        		"mfId varchar(35) default '',  " +
	        		"action varchar(255) default '',  " +
	        		"logDate datetime default ''" +
	        		")",null);
	        
	        super.update("CREATE TABLE mf_quota (  " +
	        		"groupId varchar(255) NOT NULL default '',  " +
	        		"folderQuota integer default 0,  " +
	        		"PRIMARY KEY  (groupId)" +
	        		")",null);
	        
	        super.update("CREATE TABLE mf_shared_user (  " +
	        		"mfId varchar(35) NOT NULL default '',  " +
	        		"userId varchar(255) NOT NULL default ''" +
	        		")",null);
	        
		}catch(Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
	}
	
	public Collection selectSearch(String userId, String s, int start, int maxResults, String sort, boolean descending) throws MyFolderDaoException{
		String sql = new String();
		String like = new String();
		try{
			sql = "SELECT f.mfId, f.fileName, f.filePath, f.fileDescription, f.fileType, " +
					"f.fileAccess, f.lastModifiedDate, f.parentId, f.accessCountPrivate, f.accessCountPublic, " +
					"f.fileSize, f.userId, f.userName " +
					"FROM mf_file f, mf_shared_user share " +
					"WHERE " +
					"(fileName LIKE ? OR fileDescription LIKE ?) " +
					"AND ( " +
						"(f.fileAccess = '1' " +
						     "AND f.userId = ?) " +
						"OR f.fileAccess = '2' " +
						"OR (	f.fileAccess = '3' " +
						    "AND f.mfId = share.mfId " +
						    "AND share.userId = ? " +
						    ")" +
						") ";
			
			sql += " ORDER BY " + sort;
			if(descending){
				sql += " DESC";
			}
			if(s != null){
				like = "%" + s + "%";
			}
			
			return super.select(sql, FileFolder.class, new Object[]{like, like, userId, userId}, start, maxResults);
		}
		catch(DaoException e){
			throw new MyFolderDaoException("error in select search: " + e.getMessage(), e);
		}
	}
	
	public ArrayList selectAllUsers(String paramUserId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList userList = new ArrayList();
        String userId;
        
		try{
			conn = getDataSource().getConnection();
			pstmt = conn.prepareStatement("SELECT DISTINCT userId FROM mf_file");
			
            rs = pstmt.executeQuery();
            
            while(rs.next()){
            	userId = new String();
            	userId = rs.getString(1);
            	userList.add(userId);
            }
            return userList;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error in selectAllUsers: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public String selectRootFolder(String userId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String rootFolderId = new String();
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("SELECT mfId FROM mf_file WHERE userId = ? AND fileType='Folder' AND parentId='0' ");
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				rootFolderId = rs.getString(1);
			}
			
			return rootFolderId;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error loading file or folder: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public void updateFile(FileFolder file) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("UPDATE mf_file SET fileName = ?, fileDescription = ?, lastModifiedDate = getDate() WHERE mfId = ?");
			
			pstmt.setString(1, file.getFileName());
			pstmt.setString(2, file.getFileDescription());
			pstmt.setString(3, file.getId());
			
            pstmt.executeUpdate();
            
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update file: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public void updateFolder(FileFolder folder) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("UPDATE mf_file SET fileName = ?, fileDescription = ?, fileAccess = ?, lastModifiedDate = getDate() WHERE mfId = ?");
			
			pstmt.setString(1, folder.getFileName());
			pstmt.setString(2, folder.getFileDescription());
			pstmt.setString(3, folder.getFileAccess());
			pstmt.setString(4, folder.getId());
			
            pstmt.executeUpdate();
            
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update folder: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public Collection selectFilesFolders(String userId, int folderFlag) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FileFolder folder = null;
        List folders = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT mfId, fileName, parentId, fileAccess FROM mf_file WHERE userId=?");
		
        if(folderFlag == 1){
        	sql.append(" AND fileType = 'Folder' ");
        }
        
        sql.append(" ORDER BY fileType, fileName");
        
		try{
			conn = getDataSource().getConnection();
			pstmt = conn.prepareStatement(sql.toString());
            
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            
            while(rs.next()){
            	folder = new FileFolder();
            	folder.setId(rs.getString(1));
            	folder.setFileName(rs.getString(2));
            	folder.setParentId(rs.getString(3));
            	folder.setFileAccess(rs.getString(4));
            	folder.setUserId(userId);
            	folders.add(folder);
            }
            
            return folders;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error retrieving file or folder: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
		
	}
	
	public void createLog(String id, String userId, String mfId, String action) throws MyFolderDaoException{
		try{
			String sql = new String("INSERT INTO mf_log (id, userId, mfId, action, logDate) " +
					"VALUES (?, ?, ?, ?, getDate())");
			
            super.update(sql, new Object[] {id, userId, mfId, action});
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error in loggin: " + e.getMessage(), e);
		}
	}
	
	public void insertFileFolder(FileFolder fileFolder) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("INSERT INTO mf_file (mfId, userId, fileName, filePath, fileSize, fileDescription, " +
					"fileType, fileAccess, lastModifiedDate, parentId, accessCountPrivate, accessCountPublic, userName) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, getDate(), ?, ?, ?, ?)");
			
			pstmt.setString(1, fileFolder.getId());
			pstmt.setString(2, fileFolder.getUserId());
			pstmt.setString(3, fileFolder.getFileName());
			pstmt.setString(4, fileFolder.getFilePath());
			pstmt.setDouble(5, fileFolder.getFileSize());
			pstmt.setString(6, fileFolder.getFileDescription());
			pstmt.setString(7, fileFolder.getFileType());
			pstmt.setString(8, fileFolder.getFileAccess());
			pstmt.setString(9, fileFolder.getParentId());
			pstmt.setLong(10, fileFolder.getAccessCountPrivate());
            pstmt.setLong(11, fileFolder.getAccessCountPublic());
            pstmt.setString(12, fileFolder.getUserName());
            
            pstmt.executeUpdate();
            
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error creating file or folder: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}

}
