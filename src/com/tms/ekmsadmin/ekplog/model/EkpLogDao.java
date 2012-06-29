package com.tms.ekmsadmin.ekplog.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.storage.StorageDirectory;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageService;
import kacang.ui.Event;
import kacang.util.Log;

import org.josql.Query;
import org.josql.QueryResults;

public class EkpLogDao extends DataSourceDao {
	private static String TOMCAT_FOLDER_NAME = "jakarta-tomcat";
	private static final int BUFFER = 2048;
	public static String TOMCAT_LOGS_FOLDER_NAME = "logs";
	public static final String STORAGE_ROOT = "ekplogzip";
	
	/**
	 * Get Collection of EkpLogObject according to the table filter. 
	 * A total row count for all (regardless of paging) the matching results is also initialized to minimize the processing needed to scan through java.io.File objects
	 * @param lastUpdatedDateBegin The beginning Last Updated (inclusive) Date specified in table filter
	 * @param lastUpdatedDateEnd The ending Last Updated (inclusive) Date specified in table filter
	 * @param sort The column to be sorted
	 * @param desc True if descending sort, or false otherwise
	 * @param start The start index 
	 * @param rows Total of rows to be shown in a pages
	 * @return Returns Collection of EkpLogObject and total row count as com.tms.ekmsadmin.ekplog.model.EkpLogResult
	 */
	public EkpLogResult getLogs(Date lastUpdatedDateBegin, Date lastUpdatedDateEnd, String sort, boolean desc, int start, int rows) {
		EkpLogResult logResult = new EkpLogResult();
		Collection logObjects = new ArrayList();
		String tomcatLogsRealPath = getTomcatLogsRealPath();
		
		if(tomcatLogsRealPath != null && !"".equals(tomcatLogsRealPath)) {
			File tomcatLogsFolder = new File(tomcatLogsRealPath);
			if(tomcatLogsFolder.exists()) {
				File[] logFiles = tomcatLogsFolder.listFiles();
				List logFilesList = new ArrayList();
				
				for(int i=0; i<logFiles.length; i++) {
					File logFile = logFiles[i];
					if(logFile.isFile() && !logFile.isHidden()) {
						logFilesList.add(logFiles[i]);
					}
				}
				
				List filteredFiles = filterAndSortFiles(logFilesList, lastUpdatedDateBegin, lastUpdatedDateEnd, sort, desc, start, rows);
				List allMatchedFiles = filterAndSortFiles(logFilesList, lastUpdatedDateBegin, lastUpdatedDateEnd, sort, desc, 0, -1);
				HttpServletRequest httpRequest = Application.getThreadRequest();
				
				for(int i=0; i<filteredFiles.size(); i++) {
					File logFile = (File) filteredFiles.get(i);
					EkpLogObject object = new EkpLogObject();
					
					String downloadByFile = "<a href=\"" + httpRequest.getContextPath() + "/ekplog/downloadSingleFile?fileName=" + logFile.getName() + "\">"
											+ logFile.getName() + "</a>";
					
					object.setFileName(logFile.getName());
					object.setSingleFileDownloadUrl(downloadByFile);
					object.setFileSize(logFile.length()/1000);
					
					Calendar lastModifiedDate = Calendar.getInstance();
					lastModifiedDate.setTimeInMillis(logFile.lastModified());
					object.setLastUpdatedDate(lastModifiedDate.getTime());
					
					logObjects.add(object);
				}
				
				logResult.setLogObjects(logObjects);
				logResult.setTotalResult(allMatchedFiles.size());
			}
		}
		
		return logResult;
	}
	
	/**
	 * Filter the result of java.io.File objects according to user-specified parameters, and sort the result
	 * @param logFilesList List of Tomcat log files before filtering
	 * @param lastUpdatedDateBegin The beginning Last Updated Date (inclusive) specified in table filter
	 * @param lastUpdatedDateEnd The ending Last Updated Date (inclusive) specified in table filter
	 * @param sort The column to be sorted
	 * @param desc  True if descending sort, or false otherwise
	 * @param start The start index 
	 * @param rows Total of rows to be shown in a pages
	 * @return Returns List of log files matching user specified parameters, and sorted accordingly 
	 */
	private List filterAndSortFiles(List logFilesList, Date lastUpdatedDateBegin, Date lastUpdatedDateEnd, String sort, boolean desc, int start, int rows) {
		Query fileQuery = new Query();
		List sortedFiles = null;
		String whereClause = " WHERE 0=0 ";
		String orderByClause = " ORDER BY lastModified ";
		
		// Compose WHERE clause
		if(lastUpdatedDateBegin != null) {
			whereClause += " AND lastModified >= " + lastUpdatedDateBegin.getTime() + " ";
		}
		if(lastUpdatedDateEnd != null) {
			lastUpdatedDateEnd.setHours(23);
			lastUpdatedDateEnd.setMinutes(59);
			lastUpdatedDateEnd.setSeconds(59);
			whereClause += " AND lastModified <= " + lastUpdatedDateEnd.getTime() + " ";
		}
		
		// Compoase ORDER BY clause
		if(sort != null && !"".equals(sort)) {
			if("lastModified".equals(sort)) {
				if(!desc) {
					orderByClause += " DESC ";
				}
			}
			else {
				if("singleFileDownloadUrl".equals(sort)) {
					orderByClause = " ORDER BY name ";
				}
				else if("fileSize".equals(sort)) {
					orderByClause = " ORDER BY length ";
				}
				
				if(desc) {
					orderByClause += " DESC ";
				}
			}
		}
		else {
			if(!desc) {
				orderByClause += " DESC ";
			}
		}
		
		try {
			// This is not DBMS SQL query. But SQL-like statement powered by JoSQL lib to manipulate java.io.File objects
			fileQuery.parse("SELECT * FROM java.io.File " + whereClause + orderByClause + " LIMIT " + (start + 1) + ", " + rows);
			QueryResults queryResults = fileQuery.execute(logFilesList);
			sortedFiles = queryResults.getResults();
		}
		catch(Exception error) {
			Log.getLog(getClass()).error(error, error);
			sortedFiles = logFilesList;
		}
		
		return sortedFiles;
	}
	
	/**
	 * Compress the selected log files into a single zip, and save in EKP storage
	 * @param evt
	 * @param selectedKeys
	 * @throws DaoException
	 */
	public void downloadLogs(Event evt, String[] selectedKeys) throws DaoException {
		String tomcatLogsRealPath = getTomcatLogsRealPath();
		HttpServletRequest request = evt.getRequest();
		StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
		String zipOutputFolder = storage.getRootPath() + "/" + STORAGE_ROOT + "/";
		boolean isSuccess = true;
		boolean isSingleDownload = (selectedKeys.length > 1) ? false : true;
		
		if(tomcatLogsRealPath != null && !"".equals(tomcatLogsRealPath) && request != null) {
			File tomcatLogsFolder = new File(tomcatLogsRealPath);
			
			if(tomcatLogsFolder.exists()) {
				BufferedInputStream origin = null;
				FileOutputStream destFile = null;
				ZipOutputStream out = null;
				String zipOutputFileName = request.getSession().getId() + ".zip";
				
				try {
					// Create the appropriate storage folder if it doesn't exist
					StorageDirectory ekpZipStorageFolder = new StorageDirectory("/" + STORAGE_ROOT + "/");
					storage.store(ekpZipStorageFolder);
					
					// Compress the selected log files into a single zip, and store in STORAGE_ROOT
					destFile = new FileOutputStream(zipOutputFolder + "/" + zipOutputFileName);
					out = new ZipOutputStream(new BufferedOutputStream(destFile));
					byte data[] = new byte[BUFFER];
					File logFile = null;
					
					for (int i=0; i<selectedKeys.length; i++) {
						logFile = new File(tomcatLogsRealPath + selectedKeys[i]);
						if(logFile.exists()) {
							FileInputStream fi = new FileInputStream(logFile);
							origin = new BufferedInputStream(fi, BUFFER);
							ZipEntry entry = new ZipEntry(selectedKeys[i]);
							out.putNextEntry(entry);
							int count;
				            while((count = origin.read(data, 0, BUFFER)) != -1) {
				               out.write(data, 0, count);
				            }
				            origin.close();
						}
					}
					
					out.close();
					
					// If zip is successfully created, then initialize the appropriate parameter into session,
					// so that JSP can redirect the request to download from STORAGE_ROOT
					if(isSuccess) {
						// Compose the file name to be shown in user download dialog 
						String zipDownloadFileName = "ekplogs.zip";
						if(isSingleDownload) {
							zipDownloadFileName = selectedKeys[0] + ".zip";
						}
						
						EkpLogZipFileDownloadObject downloadObject = new EkpLogZipFileDownloadObject();
						downloadObject.setZipOutputFileName(zipOutputFileName);
						downloadObject.setZipDownloadFileName(zipDownloadFileName);
						
						HttpSession session = request.getSession();
						if(session != null)
							session.setAttribute("logZipDownloadObject", downloadObject);
					}
				}
				catch(FileNotFoundException error) {
					isSuccess = false;
					throw new DaoException(error.toString(), error);
				}
				catch(IOException error) {
					isSuccess = false;
					throw new DaoException(error.toString(), error);
				}
				catch(StorageException error) {
					isSuccess = false;
					throw new DaoException(error.toString(), error);
				}
			}
		}
	}
	
	public String getTomcatLogsRealPath() {
		String tomcatRealPath = "";
		String tomcatLogsRealPath = null;
		Application app = Application.getInstance();
		String applicationRealPath = app.getApplicationRealPath();
		
		String configuredTomcatFolderName = app.getProperty("ekplog.tomcat.folderName");
		if(configuredTomcatFolderName != null) {
			TOMCAT_FOLDER_NAME = configuredTomcatFolderName;
			
			int subStringEndIndex = applicationRealPath.lastIndexOf(TOMCAT_FOLDER_NAME) + TOMCAT_FOLDER_NAME.length() + 1;
			tomcatRealPath = applicationRealPath.substring(0, subStringEndIndex);
			
			if(tomcatRealPath != null && !"".equals(tomcatRealPath)) {
				tomcatLogsRealPath = tomcatRealPath + TOMCAT_LOGS_FOLDER_NAME + "/";
			}
		}
		else {
			String configuredLogLocation = app.getProperty("ekplog.tomcat.externalLogFolder");
			if(configuredLogLocation != null && !"".equals(configuredLogLocation)) {
				if(! (configuredLogLocation.endsWith("/") || configuredLogLocation.endsWith("\\"))) {
					configuredLogLocation += "/";
				}
				tomcatLogsRealPath = configuredLogLocation;
			}
		}
		
		return tomcatLogsRealPath;
	}
	
	public boolean isTomcatLogsFolderReadable() {
		boolean isTomcatLogsFolderReadable = false;
		String tomcatLogsRealPath = getTomcatLogsRealPath();
		
		if(tomcatLogsRealPath != null && !"".equals(tomcatLogsRealPath)) {
			File tomcatLogsFolder = new File(tomcatLogsRealPath);
			if(tomcatLogsFolder.exists()) {
				isTomcatLogsFolderReadable = tomcatLogsFolder.canRead();
			}
		}
		
		return isTomcatLogsFolderReadable;
	}
	
	public boolean isTomcatLogsFolderReadable(String tomcatLogsRealPath) {
		boolean isTomcatLogsFolderReadable = false;
		
		if(tomcatLogsRealPath != null && !"".equals(tomcatLogsRealPath)) {
			File tomcatLogsFolder = new File(tomcatLogsRealPath);
			if(tomcatLogsFolder.exists()) {
				isTomcatLogsFolderReadable = tomcatLogsFolder.canRead();
			}
		}
		
		return isTomcatLogsFolderReadable;
	}
}
