package com.tms.ekmsadmin.ekplog.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kacang.Application;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import org.josql.Query;
import org.josql.QueryResults;

public class EkpLogZipRemovalJob extends BaseJob {
	public static final String JOB_ACTIVE = "ekplog.zipStorageClear.active";
	
	public void execute(JobTaskExecutionContext jobTaskExecutionContext)
	throws SchedulingException {
		String isJobActive = Application.getInstance().getProperty(JOB_ACTIVE);
		
		if(isJobActive != null && "true".equals(isJobActive)) {
			Log.getLog(getClass()).debug("Job Named 'Ekp Log Zip Storage House Keeping' is Triggered");
			
			StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
			String zipOutputFolder = storage.getRootPath() + "/" + EkpLogDao.STORAGE_ROOT + "/";
			
			File zipFileStorageFolder = new File(zipOutputFolder);
			if(zipFileStorageFolder.exists()) {
				File[] zipFiles = zipFileStorageFolder.listFiles();
				List zipFilesList = new ArrayList();
				
				for(int i=0; i<zipFiles.length; i++) {
					File zipFile = zipFiles[i];
					if(zipFile.isFile() && !zipFile.isHidden()) {
						zipFilesList.add(zipFiles[i]);
					}
				}
				
				try {
					Query fileQuery = new Query();
					// This is not DBMS SQL query. But SQL-like statement powered by JoSQL lib to manipulate java.io.File objects
					fileQuery.parse("SELECT * FROM java.io.File WHERE (toMillis(now(false)) - lastModified) > 86400000");
					QueryResults queryResults = fileQuery.execute(zipFilesList);
					List removableZipFiles = queryResults.getResults();
					
					for(int i=0; i<removableZipFiles.size(); i++) {
						File removableZipFile = (File) removableZipFiles.get(i);
						removableZipFile.delete();
					}
				}
				catch(Exception error) {
					Log.getLog(getClass()).error(error, error);
				}
			}
		}
		else {
			Log.getLog(getClass()).debug("Job Named 'Ekp Log Zip Storage House Keeping' is Disabled");
		}
	}
}
