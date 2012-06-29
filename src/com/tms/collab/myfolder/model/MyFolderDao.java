/*
 * Created on Jun 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tms.collab.myfolder.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

/**
 * @author kenwei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyFolderDao extends DataSourceDao{
	
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
	        		"accessCountPrivate int(10) default '0',  " +
	        		"accessCountPublic int(10) unsigned default '0',  " +
	        		"fileSize double(11,2) default '0.00',  " +
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
	        		"folderQuota int(11) default '0',  " +
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
	
	public Collection selectPublicFolder(String userId) throws DaoException{
		StringBuffer query = new StringBuffer("SELECT mfId, userId, fileName, filePath, fileDescription, fileType, fileAccess " +
				"lastModifiedDate, parentId, accessCountPrivate, accessCountPublic, fileSize, userName " +
				"FROM mf_file WHERE fileType = 'Folder' " +
				"AND (fileAccess = '4' OR parentId = '0') " +
				"AND userId = ? " +
				"ORDER BY fileName ");
		return super.select(query.toString(), FileFolder.class, new Object[]{userId}, 0, -1);
	}
	
	public void deleteSharedToUser(String userId) throws MyFolderDaoException{
		try{
			String sql = new String("DELETE FROM mf_shared_user WHERE userId = ?");
			
			super.update(sql, new Object[]{userId});
		}
		catch(DaoException e){
			throw new MyFolderDaoException("error in updateFileFolderUserId: " + e.getMessage(), e);
		}
	}
	
	public void updateFileFolderUserId(String mfId, String newUserId, String newUserName) throws MyFolderDaoException{
		try{
			String sql = new String("UPDATE mf_file SET userId = ?, userName = ? WHERE mfId = ?");
			
			super.update(sql, new Object[]{newUserId, newUserName, mfId});
		}
		catch(DaoException e){
			throw new MyFolderDaoException("error in updateFileFolderUserId: " + e.getMessage(), e);
		}
	}
	
	public Collection selectDeletedUserFilesFoldersList(String field, String folderId, ArrayList deletedUserList, String sort, boolean desc, int start, int rows) throws MyFolderDaoException{
		try{
			StringBuffer sql = new StringBuffer("SELECT mfId, fileName, fileSize, fileDescription, fileType, fileAccess, lastModifiedDate, userId, userName, parentId  FROM mf_file " +
					"WHERE parentId = ? AND fileName LIKE ? " +
					"AND userId IN (");
			String condition = "%%";
			
			List argList = new ArrayList();
			
			if(field != null){
				condition = "%" + field + "%";
			}
			
			argList.add(folderId);
			argList.add(condition);
			
			for(int i=0; i<deletedUserList.size(); i++){
				String userId = (String)deletedUserList.get(i);
				
				if(i > 0){
					sql.append(",");
				}
				sql.append("?");
				argList.add(userId);
			}
			sql.append(") ");
			
			sql.append("ORDER BY ");
            if (sort != null) {
                sql.append(sort);
            }
            else {
                sql.append("fileName, fileType");
            }
            if (desc) {
                sql.append(" DESC");
            }
			
			Collection col = super.select(sql.toString(), FileFolder.class, argList.toArray(), start, rows);
			
			return col;
		}
		catch(Exception e){
			throw new MyFolderDaoException("error in select search count: " + e.getMessage(), e);
		}
	}
	
	public int selectDeletedUserFileFoldersCount(String field, String folderId, ArrayList deletedUserList) throws MyFolderDaoException{
		
		try{
			StringBuffer sql = new StringBuffer("SELECT COUNT(mfId) AS total FROM mf_file " +
					"WHERE parentId = ? AND fileName LIKE ? " +
					"AND userId IN (");
			String condition = "%%";
			
			List argList = new ArrayList();
			
			if(field != null){
				condition = "%" + field + "%";
			}
			
			argList.add(folderId);
			argList.add(condition);
			
			for(int i=0; i<deletedUserList.size(); i++){
				String userId = (String)deletedUserList.get(i);
				
				if(i > 0){
					sql.append(",");
				}
				sql.append("?");
				argList.add(userId);
			}
			sql.append(")");
			
			Collection col = super.select(sql.toString(), HashMap.class, argList.toArray(), 0, -1);
			Map map = (Map) col.iterator().next();
			int count = Integer.parseInt(map.get("total").toString());
			
			return count;
		}
		catch(Exception e){
			throw new MyFolderDaoException("error in select search count: " + e.getMessage(), e);
		}
	}
	
	public int selectSearchCount(String userId, String s) throws MyFolderDaoException{
		String sql;
		String like = new String();
		
		sql = "SELECT COUNT(DISTINCT f.mfId) AS total " +
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
		
		try{
			if(s != null){
				like = "%" + s + "%";
			}
			
			Collection col = super.select(sql, HashMap.class, new Object[]{like, like, userId, userId}, 0, -1);
            Map map = (Map) col.iterator().next();
            return Integer.parseInt(map.get("total").toString());
		}
		catch(DaoException e){
			throw new MyFolderDaoException("error in select search count: " + e.getMessage(), e);
		}
	}
	
	public Collection selectSearch(String userId, String s, int start, int maxResults, String sort, boolean descending) throws MyFolderDaoException{
		String sql = new String();
		String like = new String();
		try{
			sql = "SELECT distinct f.mfId, f.fileName, f.filePath, f.fileDescription, f.fileType, " +
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
	
	public void updateParentSize(String mfId, double size) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			pstmt = conn.prepareStatement("UPDATE mf_file SET fileSize = ? WHERE mfId = ?");
			pstmt.setDouble(1, size);
			pstmt.setString(2, mfId);
			
            pstmt.executeUpdate();
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update parent folder file size: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public double getFolderFileSize(String mfId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double folderSize = 0;
        
		try{
			conn = getDataSource().getConnection();
			pstmt = conn.prepareStatement("SELECT SUM(fileSize) FROM mf_file WHERE parentId = ?");
			pstmt.setString(1, mfId);
			
            rs = pstmt.executeQuery();
            
            if(rs.next()){
            	folderSize = rs.getDouble(1);
            }
            return folderSize;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update file folder file path: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public void createLog(String id, String userId, String mfId, String action) throws MyFolderDaoException{
		try{
			String sql = new String("INSERT INTO mf_log (id, userId, mfId, action, logDate) " +
					"VALUES (?, ?, ?, ?, now())");
			
            super.update(sql, new Object[] {id, userId, mfId, action});
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error in loggin: " + e.getMessage(), e);
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
			pstmt = conn.prepareStatement("SELECT DISTINCT userId FROM mf_file ORDER BY userName");
			
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
	
	public Collection selectDeletedUserFolder(String deletedUserId) throws MyFolderDaoException{
		try{
			String sql = "SELECT mfId, fileName, parentId, fileAccess, userId, userName FROM mf_file " +
					"WHERE fileType = 'Folder' AND userId = ? ORDER BY fileName, fileType";
			
			return super.select(sql, FileFolder.class, new Object[]{deletedUserId}, 0, -1);
		}
		catch(DaoException e){
			throw new MyFolderDaoException("error in selecting deleted user folder: " + e.getMessage(), e);
		}
	}
	
	public Collection selectNotMyFolder(String sharedUserId, String loginId) throws MyFolderDaoException{
		try {
			Collection temp = null;
			Collection temp2 = null;
            List argList1 = new ArrayList();
            List argList2 = new ArrayList();
            
            StringBuffer sql = new StringBuffer("SELECT DISTINCT mfId, fileName, parentId, fileAccess, userId, userName " +
            		"FROM mf_file WHERE fileType = 'Folder' " +
            		"AND userId = ? " +
            		"AND userId <> ? " +
            		"AND fileAccess = '2' ORDER BY parentId");
            
            argList1.add(sharedUserId);
            argList1.add(loginId);
            
            
            temp = super.select(sql.toString(), FileFolder.class, argList1.toArray(), 0, -1);
            
            sql = new StringBuffer("SELECT distinct f.mfId, f.fileName, f.parentId, f.fileAccess, f.userId, f.userName " +
            		"FROM mf_file f, mf_shared_user share WHERE f.fileType = 'Folder' " +
            		"AND f.userId = ? " +
            		"AND f.userId <> ? " +
            		"AND f.fileAccess = '3' AND share.userId = ? " +
            		"AND share.mfId = f.mfId " +
            		"ORDER BY f.parentId");
            
            argList2.add(sharedUserId);
            argList2.add(loginId);
            argList2.add(loginId);
            
            temp2 = super.select(sql.toString(), FileFolder.class, argList2.toArray(), 0, -1);
            temp2.addAll(temp);
            
            return temp2;
            
        } catch(Exception e) {
            throw new MyFolderDaoException("Error in getting not my folder", e);
        }
	}
	
	//select all folder that the login user are allow to access
	public Collection selectNotMyFolder(String userId) throws MyFolderDaoException{
		try {
			
            List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT distinct f.mfId, f.fileName, f.parentId, f.fileAccess, f.userId " +
            		"FROM mf_file f, mf_shared_user share " +
            		"WHERE f.fileType = 'Folder' " +
            		"AND f.userId <> ? " +
            		"AND (f.fileAccess = '2' OR (f.fileAccess = '3' AND share.userId = ? AND share.mfId = f.mfId)) " +
            		"ORDER BY f.parentId");
            
            argList.add(userId);
            argList.add(userId);
            
            return super.select(sql.toString(), FileFolder.class, argList.toArray(), 0, -1);
            
        } catch(Exception e) {
            throw new MyFolderDaoException("Error getting principal max disk quota", e);
        }
	}
	
	public Collection selectNotMyFolder(String[] selectedUsers, String userId) throws MyFolderDaoException{
		try {
			
            List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT distinct f.mfId, f.fileName, f.parentId, f.fileAccess " +
            		"FROM mf_file f, mf_shared_user share WHERE ");

            sql.append(" (f.fileAccess = '2' OR (f.fileAccess = '3' AND share.userId = ? AND share.mfId = f.mfId)) " +
            		"ORDER BY f.parentId");
            argList.add(userId);

            return super.select(sql.toString(), FileFolder.class, argList.toArray(), 0, -1);
            
        } catch(Exception e) {
            throw new MyFolderDaoException("Error getting principal max disk quota", e);
        }
	}
	
	public void updateFileAccessCount(String mfId, String userId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        FileFolder temp = new FileFolder();
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("SELECT userId, accessCountPrivate, accessCountPublic FROM mf_file WHERE mfId = ?");
			
			pstmt.setString(1, mfId);
			
            rs = pstmt.executeQuery();
            
            if(rs.next()){
            	temp.setUserId(rs.getString(1));
            	temp.setAccessCountPrivate(rs.getLong(2));
            	temp.setAccessCountPublic(rs.getLong(3));
            }
            
            if(userId.equals(temp.getUserId())){
            	pstmt = conn.prepareStatement("UPDATE mf_file SET accessCountPrivate = ? WHERE mfId = ?");
            	pstmt.setLong(1, temp.getAccessCountPrivate()+1);
            }else{
            	pstmt = conn.prepareStatement("UPDATE mf_file SET accessCountPublic = ? WHERE mfId = ?");
            	pstmt.setLong(1, temp.getAccessCountPublic()+1);
            }
            
            pstmt.setString(2, mfId);
            
            pstmt.executeUpdate();
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update file folder file path: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public String selectNumOfItemsInFolder(String mfId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String fileCount = new String();
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("SELECT COUNT(*) FROM mf_file WHERE parentId = ?");
			
			pstmt.setString(1, mfId);
			
            rs = pstmt.executeQuery();
            
            if(rs.next()){
            	fileCount = rs.getString(1);
            }
            
            return fileCount;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update file folder file path: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public void updateFileFolderFilePath(String mfId, String newFilePath) throws MyFolderDaoException{

		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("UPDATE mf_file SET filePath = ? WHERE mfId = ?");
			
			pstmt.setString(1, newFilePath);
			pstmt.setString(2, mfId);
			
            pstmt.executeUpdate();
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update file folder file path: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
		
	}
	
	public Collection selectChildFileFolder(String mfId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List childFileFolder = new ArrayList();
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("SELECT mfId, parentId, fileName, fileType, filePath FROM mf_file WHERE parentId = ?");
			pstmt.setString(1, mfId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				FileFolder temp = new FileFolder();
				temp.setId(rs.getString(1));
				temp.setParentId(rs.getString(2));
				temp.setFileName(rs.getString(3));
				temp.setFileType(rs.getString(4));
				if("Folder".equalsIgnoreCase(rs.getString(4))){
					temp.setIsFolder(true);
				}else{
					temp.setIsFolder(false);
				}
				temp.setFilePath(rs.getString(5));
				childFileFolder.add(temp);
			}
			
			return childFileFolder;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error select child file or folder: " + e.getMessage(), e);
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
			
			pstmt = conn.prepareStatement("SELECT mfId FROM mf_file WHERE userId = ? AND fileType='folder' AND parentId='0' ");
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

	// To acquire the folderName/fileName to be displayed at the title/label
	public FileFolder selectFileFolder(String mfId) throws MyFolderDaoException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getDataSource().getConnection();

			pstmt = conn.prepareStatement(
					"SELECT mfId, parentId, fileName, fileType, filePath " +
					"FROM mf_file " +
					"WHERE mfId = ?");
			pstmt.setString(1, mfId);

			rs = pstmt.executeQuery();
			FileFolder fileFolder = null;
			if (rs.next()) {
				fileFolder = new FileFolder();
				fileFolder.setId(rs.getString(1));
				fileFolder.setParentId(rs.getString(2));
				fileFolder.setFileName(rs.getString(3));
				fileFolder.setFileType(rs.getString(4));
				if ("Folder".equalsIgnoreCase(rs.getString(4))) {
					fileFolder.setIsFolder(true);
				} else {
					fileFolder.setIsFolder(false);
				}
				fileFolder.setFilePath(rs.getString(5));
			}

			return fileFolder;
		} catch (SQLException e) {
			throw new MyFolderDaoException("Error select folder: " + e.getMessage(), e);
		} finally {
			closeConnection(conn, pstmt, rs);
		}
	}


	public Long selectMaxPrincipalQuota(String[] principalIdArray) throws MyFolderDaoException {
        try {
            if (principalIdArray == null || principalIdArray.length == 0) {
                return null;
            }

            List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT MAX(folderQuota) AS maxQuota FROM mf_quota WHERE groupId IN (");
            for(int i=0; i<principalIdArray.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
                argList.add(principalIdArray[i]);
            }
            sql.append(")");

            HashMap map = (HashMap) super.select(sql.toString(), HashMap.class, argList.toArray(), 0, -1).iterator().next();
            Object maxQuota = map.get("maxQuota");
            if (maxQuota != null) {
                Double d = new Double(maxQuota.toString());
                return new Long(d.longValue());
            }
            else {
                return null;
            }
        } catch(Exception e) {
            throw new MyFolderDaoException("Error getting principal max disk quota", e);
        }
    }
	
	public void updateFolderFileSize(String mfId, String ope, double paramFileSize) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = new String();
        double fileSize =0;
        
		try{
			conn = getDataSource().getConnection();
			
			sql = "SELECT fileSize FROM mf_file WHERE mfId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mfId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				fileSize = rs.getDouble(1);
			}
			
			if("+".equals(ope)){
				fileSize = fileSize + paramFileSize;
			}else{
				fileSize = fileSize - paramFileSize;
			}
			
			sql = "UPDATE mf_file SET fileSize = ? WHERE mfId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, fileSize);
			pstmt.setString(2, mfId);
			
			pstmt.executeUpdate();
			
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error update folder size: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public FileFolder loadFileFolder(String mfId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        FileFolder fileFolder;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("SELECT fileName, fileDescription, fileType, fileAccess, parentId, mfId, filePath, fileSize, userId, userName FROM mf_file WHERE mfId = ? ");
			pstmt.setString(1, mfId);
			
			rs = pstmt.executeQuery();
			
			fileFolder = new FileFolder();
			
			if(rs.next()){
				fileFolder.setFileName(rs.getString(1));
				fileFolder.setFileDescription(rs.getString(2));
				fileFolder.setFileType(rs.getString(3));
				fileFolder.setFileAccess(rs.getString(4));
				fileFolder.setParentId(rs.getString(5));
				fileFolder.setId(rs.getString(6));
				fileFolder.setFilePath(rs.getString(7));
				fileFolder.setFileSize(rs.getDouble(8));
				fileFolder.setUserId(rs.getString(9));
				fileFolder.setUserName(rs.getString(10));
				if("Folder".equalsIgnoreCase(rs.getString(3))){
					fileFolder.setIsFolder(true);
				}else{
					fileFolder.setIsFolder(false);
				}
			}
			
			return fileFolder;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error loading file or folder: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public ArrayList selectFolderSharedUsers(String mfId) throws MyFolderDaoException{
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        ArrayList sharedUser = new ArrayList();
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("SELECT userId FROM mf_shared_user WHERE mfId = ? ");
			pstmt.setString(1, mfId);
				
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				String user = rs.getString(1);
				sharedUser.add(user);
			}
			
			return sharedUser;
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error selecting folder shared user: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public void insertFolderSharedUsers(String mfId, String[] sharedUserId) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			for(int i=0; i<sharedUserId.length; i++){
				pstmt = conn.prepareStatement("INSERT INTO mf_shared_user (mfId, userId) VALUES(?, ?) ");
				pstmt.setString(1, mfId);
				pstmt.setString(2, sharedUserId[i]);
				
				pstmt.executeUpdate();
			}
			            
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error insert folder shared user: " + e.getMessage(), e);
		}
		finally{
			closeConnection(conn, pstmt, rs);
		}
	}
	
	public void deleteFolderSharedUsers(String mfId) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("DELETE FROM mf_shared_user WHERE mfId = ?");
			pstmt.setString(1, mfId);
            pstmt.executeUpdate();
		}
		catch(SQLException e){
			throw new MyFolderDaoException("Error deleting folder shared users: " + e.getMessage(), e);
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
			
			pstmt = conn.prepareStatement("UPDATE mf_file SET fileName = ?, fileDescription = ?, fileAccess = ?, lastModifiedDate = now() WHERE mfId = ?");
			
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
	
	public void updateFile(FileFolder file) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("UPDATE mf_file SET fileName = ?, fileDescription = ?, lastModifiedDate = now() WHERE mfId = ?");
			
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
	
	public int updateFilesFoldersFilePath(String[] selected, String newPath) throws MyFolderDaoException{
		try{
			if(selected == null || selected.length == 0){
				return 0;
			}
			
			List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("UPDATE mf_file SET filePath = ? WHERE mfId IN (");
            
            argList.add(newPath);
            
            for(int i=0; i<selected.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
                argList.add(selected[i]);
            }
            sql.append(")");

            int count = super.update(sql.toString(), argList.toArray());
            return count;
			
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error update files or folders parent id: " + e.getMessage(), e);
		}
	}
	
	public int updateFilesFoldersParentId(String selected, String newParentId) throws MyFolderDaoException{
		try{
			
			List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("UPDATE mf_file SET parentId = ? WHERE mfId = ? ");
            
            argList.add(newParentId);
            argList.add(selected);
            
            int count = super.update(sql.toString(), argList.toArray());
            return count;
			
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error update files or folders parent id: " + e.getMessage(), e);
		}
	}
	
	public int updateFilesFoldersParentId(String[] selected, String newParentId) throws MyFolderDaoException{
		try{
			if(selected == null || selected.length == 0){
				return 0;
			}
			
			List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("UPDATE mf_file SET parentId = ? WHERE mfId IN (");
            
            argList.add(newParentId);
            
            for(int i=0; i<selected.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
                argList.add(selected[i]);
            }
            sql.append(")");

            int count = super.update(sql.toString(), argList.toArray());
            return count;
			
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error update files or folders parent id: " + e.getMessage(), e);
		}
	}
	
	public int selectSharedFilesCount(String fileName, String userId, String parentId) throws MyFolderDaoException{
		try {
        	
        	String condition = "%%";
			
    		StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS total " +
            		"FROM mf_file WHERE parentId = ? AND fileName LIKE ?");
    		
    		if(fileName != null){
    			condition = "%" + fileName + "%";
    		}
    		
    		Object[] args = {parentId, condition};
    		
    		Collection list = super.select(sql.toString(), HashMap.class, args, 0, 1);
        	
    		HashMap map = (HashMap) list.iterator().next();
        	
    		return Integer.parseInt(map.get("total").toString());
    		
        }
        catch (Exception e) {
            throw new MyFolderDaoException("Error in files and folders count: " + e.getMessage(), e);
        }
	}
	
	public int selectFilesFoldersCount(String fileName, String userId, String parentId) throws MyFolderDaoException{
		
        try {
        	
        	String condition = "%%";
			
    		StringBuffer sql = new StringBuffer("SELECT COUNT(*) AS total " +
            		"FROM mf_file WHERE userId = ? AND parentId = ? AND fileName LIKE ?");
    		
    		if(fileName != null){
    			condition = "%" + fileName + "%";
    		}
    		
    		Object[] args = {userId, parentId, condition};
    		
    		Collection list = super.select(sql.toString(), HashMap.class, args, 0, 1);
        	
    		HashMap map = (HashMap) list.iterator().next();
        	
    		return Integer.parseInt(map.get("total").toString());
    		
        }
        catch (Exception e) {
            throw new MyFolderDaoException("Error in files and folders count: " + e.getMessage(), e);
        }
	}
	
	public Collection selectSharedFilesList(String fileName, String parentId, String userId, String sort, boolean desc, int start, int rows) throws MyFolderDaoException{
		try{
			String condition = "%%";
			
    		StringBuffer sql = new StringBuffer("SELECT mfId, fileName, fileSize, fileDescription, fileType, fileAccess, lastModifiedDate " +
            		"FROM mf_file WHERE parentId = ? AND fileName LIKE ? AND fileType <> 'Folder' ");
    		
    		if(fileName != null){
    			condition = "%" + fileName + "%";
    		}
    		
    		sql.append(" ORDER BY ");
    		if(sort != null){
    			sql.append(sort);
    		}else{
    			sql.append(" fileName ");
    		}
    		
    		if(desc){
    			sql.append(" DESC");
    		}
    		
    		Object[] args = {parentId, condition};
    		
    		return super.select(sql.toString(), FileFolder.class, args, start, rows);
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error retrieveing file and folder list: " + e.getMessage(), e);
		}
	}
	
	public Collection selectFilesFoldersList(String fileName, String parentId, String userId, String sort, boolean desc, int start, int rows) throws MyFolderDaoException{
		try {
			String condition = "%%";
			
    		StringBuffer sql = new StringBuffer("SELECT mfId, fileName, fileSize, fileDescription, fileType, fileAccess, lastModifiedDate " +
            		"FROM mf_file WHERE userId = ? AND parentId = ? AND fileName LIKE ?");
    		
    		if(fileName != null){
    			condition = "%" + fileName + "%";
    		}
    		
    		sql.append(" ORDER BY ");
    		if(sort != null){
    			sql.append(sort);
    		}else{
    			sql.append(" fileType, fileName ");
    		}
    		
    		if(desc){
    			sql.append(" DESC");
    		}
    		
    		Object[] args = {userId, parentId, condition};
    		
    		Collection list = super.select(sql.toString(), FileFolder.class, args, start, rows);
    		
    		return list;
        }
        catch (Exception e) {
            throw new MyFolderDaoException("Error retrieving file and folder list: " + e.getMessage(), e);
        }
        
	}
	
	public int deleteFilesFolders(String selected) throws MyFolderDaoException{

		try{
			
			List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("DELETE FROM mf_file WHERE mfId = ?");
            argList.add(selected);
            
            int count = super.update(sql.toString(), argList.toArray());
            return count;
			
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error deleting files or folders: " + e.getMessage(), e);
		}
	}
	
	public int deleteFilesFolders(String[] selected) throws MyFolderDaoException{

		try{
			if(selected == null || selected.length == 0){
				return 0;
			}
			
			List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("DELETE FROM mf_file WHERE mfId IN (");
            for(int i=0; i<selected.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
                argList.add(selected[i]);
            }
            sql.append(")");

            int count = super.update(sql.toString(), argList.toArray());
            return count;
			
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error deleting files or folders: " + e.getMessage(), e);
		}
	}
	
	public double selectUsedSpace(String userId) throws MyFolderDaoException{
		double usedSpace = 0;
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
        	conn = getDataSource().getConnection();
        	pstmt = conn.prepareStatement("SELECT SUM(fileSize) FROM mf_file WHERE userId = ? AND fileType <> 'Folder'");
        	pstmt.setString(1, userId);
        	rs = pstmt.executeQuery();
        	
        	if(rs.next()){
        		usedSpace = rs.getDouble(1);
        	}
        	
        	return usedSpace;
        }
        catch(SQLException e){
        	throw new MyFolderDaoException("Error retrieving used space: " + e.getMessage(), e);
        }
        finally{
        	closeConnection(conn, pstmt, rs);
        }
		
	}
	
	public MyFolderQuota selectQuota(String userGroupId) throws MyFolderDaoException{
		MyFolderQuota quota;
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
        	conn = getDataSource().getConnection();
        	pstmt = conn.prepareStatement("SELECT folderQuota FROM mf_quota WHERE groupId = ?");
        	pstmt.setString(1, userGroupId);
        	rs = pstmt.executeQuery();
        	
        	quota = new MyFolderQuota();
        	
        	if(rs.next()){
        		quota.setQuota(rs.getLong(1));
        	}
        	
        	return quota;
        }
        catch(SQLException e){
        	throw new MyFolderDaoException("Error retrieving quota: " + e.getMessage(), e);
        }
        finally{
        	closeConnection(conn, pstmt, rs);
        }
	}
	
	//folderFlag parameter is to select either
	//1 - folders only
	//other than 1 - files and folders
	public Collection selectFilesFolders(String userId, int folderFlag) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FileFolder folder = null;
        List folders = new ArrayList();
        StringBuffer sql = new StringBuffer("SELECT mfId, fileName, parentId, fileAccess FROM mf_file WHERE userId=?");
		
        if(folderFlag == 1){
        	sql.append(" AND fileType = 'folder' ");
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
	
	public void insertFileFolder(FileFolder fileFolder) throws MyFolderDaoException{
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
		try{
			conn = getDataSource().getConnection();
			
			pstmt = conn.prepareStatement("INSERT INTO mf_file (mfId, userId, fileName, filePath, fileSize, fileDescription, " +
					"fileType, fileAccess, lastModifiedDate, parentId, accessCountPrivate, accessCountPublic, userName) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, now(), ?, ?, ?, ?)");
			
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
	
	public int deleteGroupQuota(String[] selectedGroupArray) throws MyFolderDaoException{
		
		try{
			if(selectedGroupArray == null || selectedGroupArray.length == 0){
				return 0;
			}
			
			List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("DELETE FROM mf_quota WHERE groupId IN (");
            for(int i=0; i<selectedGroupArray.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
                argList.add(selectedGroupArray[i]);
            }
            sql.append(")");

            int count = super.update(sql.toString(), argList.toArray());
            return count;
			
			
		}
		catch(Exception e){
			throw new MyFolderDaoException("Error deleting group quota: " + e.getMessage(), e);
		}
	}
	
	
	public Collection selectGroupQuotaList(String name, String sort, boolean desc, int start, int rows) throws MyFolderDaoException{
		try {
            Collection argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT g.id, groupName, folderQuota " +
                    "FROM security_group g INNER JOIN mf_quota q ON g.id=q.groupId ");
            if (name != null) {
                sql.append(" WHERE groupName LIKE ?");
                argList.add("%" + name + "%");
            }
            sql.append("ORDER BY ");
            if (sort != null) {
                sql.append(sort);
            }
            else {
                sql.append("groupName");
            }
            if (desc) {
                sql.append(" DESC");
            }
            Collection results = super.select(sql.toString(), HashMap.class, argList, start, rows);
            return results;
        }
        catch (Exception e) {
            throw new MyFolderDaoException("Error retrieving quotas: " + e.getMessage(), e);
        }
	}
	
	public int selectGroupQuotaCount(String group) throws MyFolderDaoException{
		
        try {
            Collection argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT COUNT(groupId) AS total " +
                    "FROM security_group g INNER JOIN mf_quota q ON g.id=q.groupId ");
            if (group != null) {
                sql.append(" WHERE groupName LIKE ?");
                argList.add("%" + group + "%");
            }
            Map count = (Map)super.select(sql.toString(), HashMap.class, argList, 0, 1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        }
        catch (Exception e) {
            throw new MyFolderDaoException("Error retrieving quota count: " + e.getMessage(), e);
        }
	}
	
	public int updateMyFolderQuota(String groupId, long quota) throws MyFolderDaoException{
		try {
            String sql = "UPDATE mf_quota SET folderQuota=? WHERE groupId=?";
            String sql2 = "INSERT INTO mf_quota (groupId,folderQuota) VALUES (?,?)";
            int count = super.update(sql, new Object[] { new Long(quota), groupId });
            if (count == 0) {
                count = super.update(sql2, new Object[] { groupId, new Long(quota) });
            }
            return count;
        } catch(Exception e) {
            throw new MyFolderDaoException("Error updating quota", e);
        }
	}
	
	protected void closeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // do nothing, just proceed
            	Log.getLog(getClass()).error("Exception while closing ResultSet", e);
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // do nothing, just proceed
            	Log.getLog(getClass()).error("Exception while closing PreparedStatement", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // do nothing, just proceed
            	Log.getLog(getClass()).error("Exception while closing Connection", e);
            }
        }
    }
	
}
