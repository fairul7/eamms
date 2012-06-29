
package com.tms.collab.myfolder.model;

import java.util.Collection;
import java.util.Date;

import kacang.Application;
import kacang.model.DefaultDataObject;


public class FileFolder extends DefaultDataObject{
	
	private String mfId;
	private String userId;
	private String userName;
	private String fileName;
	private String filePath;
	private double fileSize;
	private String fileDescription;
	private String fileType;
	private String fileAccess;
	private Date lastModifiedDate;
	private String parentId;
	private long accessCountPrivate;
	private long accessCountPublic;
	private Collection subFolders;
	private boolean isFolder;
	
	public static final String MY_FOLDER = "My Folder";
	public static final String FOLDER = "folder";
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return Returns the isFolder.
	 */
	public boolean isFolder() {
		return isFolder;
	}
	/**
	 * @param isFolder The isFolder to set.
	 */
	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}
	/**
	 * @return Returns the subFolders.
	 */
	public Collection getSubFolders() {
		return subFolders;
	}
	/**
	 * @param subFolders The subFolders to set.
	 */
	public void setSubFolders(Collection subFolders) {
		this.subFolders = subFolders;
	}
	/**
	 * @return Returns the mY_FOLDER.
	 */
	public static String getMY_FOLDER() {
		return MY_FOLDER;
	}
	/**
	 * @return Returns the accessCountPrivate.
	 */
	public long getAccessCountPrivate() {
		return accessCountPrivate;
	}
	/**
	 * @param accessCountPrivate The accessCountPrivate to set.
	 */
	public void setAccessCountPrivate(long accessCountPrivate) {
		this.accessCountPrivate = accessCountPrivate;
	}
	/**
	 * @return Returns the accessCountPublic.
	 */
	public long getAccessCountPublic() {
		return accessCountPublic;
	}
	/**
	 * @param accessCountPublic The accessCountPublic to set.
	 */
	public void setAccessCountPublic(long accessCountPublic) {
		this.accessCountPublic = accessCountPublic;
	}
	/**
	 * @return Returns the fileAccess.
	 */
	public String getFileAccess() {
		return fileAccess;
	}
	/**
	 * @param fileAccess The fileAccess to set.
	 */
	public void setFileAccess(String fileAccess) {
		this.fileAccess = fileAccess;
	}
	/**
	 * @return Returns the fileDescription.
	 */
	public String getFileDescription() {
		return fileDescription;
	}
	/**
	 * @param fileDescription The fileDescription to set.
	 */
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		if(fileName.equalsIgnoreCase(MY_FOLDER)){
			this.fileName = Application.getInstance().getMessage("mf.label.myFolder", "My Folder");
		}else{
			this.fileName = fileName;
		}
	}
	/**
	 * @return Returns the filePath.
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath The filePath to set.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return Returns the fileSize.
	 */
	public double getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize The fileSize to set.
	 */
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return Returns the fileType.
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * @param fileType The fileType to set.
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/**
	 * @return Returns the lastModifiedData.
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @param lastModifiedData The lastModifiedData to set.
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
	/**
	 * @return Returns the parentId.
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
