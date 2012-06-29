package com.tms.cms.ad.model;

import kacang.Application;
import kacang.model.DefaultModule;
import kacang.model.DataObjectNotFoundException;
import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class AdModule extends DefaultModule {
    public static final String CONTEXT_PARAM_NAME = "id";

    private Log log = Log.getLog(getClass());

    // key=location name; value=AdLocation object
    private Map adLocationMap = Collections.synchronizedMap(new HashMap());

    // key=location name; value=index to com.tms.cms.ad in the location
    private Map adIndexMap = Collections.synchronizedMap(new HashMap());

    public void init() {
        try {
            refreshModule();
        } catch(AdException e) {
            log.debug(e, e);
        }
    }
    // === [ front end ] =======================================================

    /**
     * Call this method to indicate a view com.tms.cms.ad event. This will update the
     * view statistics for this com.tms.cms.ad, given an com.tms.cms.ad location name and request
     * object.
     *
     * @param request
     * @param name the com.tms.cms.ad location's name
     * @return the com.tms.cms.ad to be viewed
     * @throws AdException
     */
    public Ad viewAdEvent(HttpServletRequest request, String name, boolean preview) throws AdException, DataObjectNotFoundException {
        AdLocation adLocation;
        Integer adIndex;
        Ad ad;

        // get the AdLocation
        adLocation = (AdLocation) adLocationMap.get(name);
        if(adLocation == null) {
            throw new AdException("Ad location name (" + name + ") not found or not active");
        }
        // AdLocation has no Ad
        if(adLocation.getActiveAdList().size() == 0) {
            throw new DataObjectNotFoundException("Ad location name (" + name + ") has no active Ad");
        }
        // get the index to Ad in AdLocation
        adIndex = (Integer) adIndexMap.get(name);
        if(adIndex == null) {
            throw new AdException("Ad index for location name (" + name + ") not found");
        }
        // Ad index out or range, reset to zero
        if(adIndex.intValue() >= adLocation.getActiveAdList().size()) {
            // reset to zero
            adIndex = new Integer(0);
            adIndexMap.put(name, adIndex);
        }
        // get the Ad
        ad = (Ad) adLocation.getActiveAdList().get(adIndex.intValue());

        // move to next ad for this ad location (rotate)
        if((adIndex.intValue() + 1) < adLocation.getActiveAdList().size()) {
            // move to next com.tms.cms.ad in com.tms.cms.ad location
            adIndex = new Integer(adIndex.intValue() + 1);
            adIndexMap.put(name, adIndex);
        } else {
            // reset to zero
            adIndex = new Integer(0);
            adIndexMap.put(name, adIndex);
        }
        // update view stats
        if(!preview) {
            AdDao dao = (AdDao) getDao();
            try {
                dao.insertViewOrClick(false, ad.getAdId(), new java.util.Date(), request.getRemoteAddr());
            } catch(AdDaoException e) {
                throw new AdException("Error inserting com.tms.cms.ad view log entry", e);
            }
        }
        return ad;
    }

    /**
     * Call this method to update statistics for an com.tms.cms.ad to indicate a click-thru.
     *
     * @param adId unique ID for the com.tms.cms.ad.
     * @return the com.tms.cms.ad which was 'clicked'.
     * @throws AdException
     */
    public Ad clickAdEvent(HttpServletRequest request, String adId, boolean preview) throws AdException {
        AdDao dao;
        Ad ad;

        dao = (AdDao) getDao();

        try {
            ad = dao.selectAd(adId);

            // update click-thru stats
            if(!preview) {
                try {
                    dao.insertViewOrClick(true, ad.getAdId(), new java.util.Date(), request.getRemoteAddr());
                } catch(AdDaoException e) {
                    throw new AdException("Error inserting com.tms.cms.ad click log entry", e);
                }
            }
            return ad;

        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    public boolean hasReport(boolean click, String adId) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.hasReport(click, adId);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    public List getValidReportYears(boolean click, String adId) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectValidReportYears(click, adId);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    public Map getDailyReport(boolean click, boolean unique, String adId, String year, String month) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectDailyReport(click, unique, adId, year, month);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    public Map getMonthlyReport(boolean click, boolean unique, String adId, String year) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectMonthlyReport(click, unique, adId, year);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }
    // === [ AdLocation ] ======================================================

    /**
     * Creates a new com.tms.cms.ad location.
     * @param adLocation
     * @throws AdException
     */
    public void createAdLocation(AdLocation adLocation) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            dao.insertAdLocation(adLocation);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    public int getAdLocationCount(String search) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectAdLocationCount(search);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    /**
     * Retrieves an existing com.tms.cms.ad location.
     * @param adLocationId
     * @return
     * @throws AdException
     */
    public AdLocation getAdLocation(String adLocationId) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectAdLocation(adLocationId);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    /**
     * Retrieves an existing com.tms.cms.ad location by name.
     * @param name
     * @return
     * @throws AdException
     */
    public AdLocation getAdLocationByName(String name) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectAdLocationByName(name);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    public Collection getAdLocations(String search, String sort, boolean direction, int start, int maxRows) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectAdLocations(search, sort, direction, start, maxRows);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    /**
     * Updates an existing com.tms.cms.ad location.
     * @param adLocation
     * @throws AdException
     */
    public void updateAdLocation(AdLocation adLocation) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            dao.updateAdLocation(adLocation);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    /**
     * Deletes an existing com.tms.cms.ad location.
     * @param adLocationId
     * @throws AdException
     */
    public void deleteAdLocation(String adLocationId) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            dao.deleteAdLocation(adLocationId);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }
    // === [ Ad ] ==============================================================

    /**
     * Creates a new com.tms.cms.ad.
     * @param ad
     * @throws AdException
     */
    public void createAd(Ad ad, StorageFile storageFile) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {

            if(storageFile != null) {
                storageFile.setParentDirectoryPath(AdModule.class.getName() + "/ads" + "/" + ad.getAdId());
                storageFile.setName(storageFile.getName());
                ad.setImageFile(storageFile.getAbsolutePath());
            }
            dao.insertAd(ad);

            if(storageFile != null) {
                StorageService ss;
                ss = (StorageService) Application.getInstance().getService(StorageService.class);
                ss.store(storageFile);
            }
            scheduleAdStart(ad);
            scheduleAdEnd(ad);

        } catch(AdDaoException e) {
            throw new AdException(e);
        } catch(StorageException e) {
            throw new AdException(e);
        }
    }

    public int getAdCount(String search) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectAdCount(search);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    /**
     * Retrieves an existing com.tms.cms.ad.
     * @param adId
     * @return
     * @throws AdException
     */
    public Ad getAd(String adId) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectAd(adId);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    public Collection getAds(String search, String sort, boolean direction, int start, int maxRows) throws AdException {
        AdDao dao = (AdDao) getDao();
        try {
            return dao.selectAds(search, sort, direction, start, maxRows);
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
    }

    /**
     * Updates an existing com.tms.cms.ad.
     * @param ad
     * @throws AdException
     */
    public void updateAd(Ad ad, StorageFile storageFile) throws AdException {
        updateAd(ad, storageFile, true);
    }

    public void updateAd(Ad ad, StorageFile storageFile, boolean runSchedule) throws AdException {
        AdDao dao = (AdDao) getDao();

        try {
            if(storageFile != null) {
                storageFile.setParentDirectoryPath(AdModule.class.getName() + "/ads" + "/" + ad.getAdId());
                storageFile.setName(storageFile.getName());
                ad.setImageFile(storageFile.getAbsolutePath());
            }
            dao.updateAd(ad);

            if(storageFile != null) {
                StorageService ss;
                StorageFile sdir;
                ss = (StorageService) Application.getInstance().getService(StorageService.class);

                // remove previous image
                sdir = new StorageFile(AdModule.class.getName() + "/ads", ad.getAdId());
                ss.delete(sdir);

                // if new file provided, store new image
                ss.store(storageFile);
            }
            if(runSchedule) {
                scheduleAdStart(ad);
                scheduleAdEnd(ad);
            }
            refreshModule();

        } catch(AdDaoException e) {
            throw new AdException(e);
        } catch(StorageException e) {
            throw new AdException(e);
        }
    }

    /**
     * Deletes an existing com.tms.cms.ad.
     * @param adId
     * @throws AdException
     */
    public void deleteAd(String adId) throws AdException {
        Ad ad;
        AdDao dao = (AdDao) getDao();
        try {
            ad = getAd(adId);
            dao.deleteAd(adId);

            if(ad.getImageFile() != null) {
                StorageService ss;
                StorageFile sdir;

                ss = (StorageService) Application.getInstance().getService(StorageService.class);
                sdir = new StorageFile(AdModule.class.getName() + "/ads", ad.getAdId());
                if(ss.delete(sdir)) {
                    log.debug("Ad image file delete " + ad.getImageFile());
                } else {
                    log.warn("Ad image file NOT delete " + ad.getImageFile());
                }
            }
        } catch(AdDaoException e) {
            throw new AdException(e);
        } catch(StorageException e) {
            throw new AdException(e);
        }
    }

    public void refreshModule() throws AdException {
        Collection collection;
        Iterator iterator;
        AdDao dao;
        AdLocation adLocation;

        dao = (AdDao) getDao();

        adLocationMap.clear();
        adIndexMap.clear();

        try {
            collection = dao.selectAdLocations("*", null, false, 0, -1);
            iterator = collection.iterator();
            while(iterator.hasNext()) {
                adLocation = (AdLocation) iterator.next();
                if(adLocation.isActive()) {
                    adLocationMap.put(adLocation.getName(), adLocation);
                    adIndexMap.put(adLocation.getName(), new Integer(0));
                }
            }
        } catch(AdDaoException e) {
            throw new AdException(e);
        }
        log.debug(AdModule.class.getName() + " refreshed");
    }

    // === [ private methods ] =================================================
    private void scheduleAdStart(Ad ad) {
        JobSchedule jobSchedule;
        AdJob job;
        SchedulingService ss;
        String jobName, jobGroup;

        ss = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        if(ss == null) {
            log.error("Error getting scheduling service");
            return;
        }

        // remove from scheduler first, if exist
        jobName = "start-" + ad.getAdId();
        jobGroup = AdModule.class.getName();
        jobSchedule = null;
        try {
            jobSchedule = ss.getJobSchedule(jobName, jobGroup);

            try {
                ss.unscheduleJob(jobSchedule);
            } catch(SchedulingException e) {
                log.error("Error unscheduling AdJob", e);
            }
        } catch(Exception e) {
            // job is not in schedule, no need to remove
        }
        if(ad.isStartDateEnabled()) {
            // add the scheduler, if active

            // create the schedule/trigger
            jobSchedule = new JobSchedule(jobName, JobSchedule.SECONDLY);
            jobSchedule.setGroup(jobGroup);
            jobSchedule.setStartTime(ad.getStartDate());
            jobSchedule.setRepeatCount(0);

            // create the job
            job = new AdJob();
            job.setName(jobName);
            job.setGroup(jobGroup);
            job.getJobTaskData().put("id", ad.getAdId());
            job.getJobTaskData().put("active", true);

            // schedule the job
            try {
                ss.scheduleJob(job, jobSchedule);
            } catch(SchedulingException e) {
                log.error("Error scheduling AdJob", e);
            }
        }
    }

    private void scheduleAdEnd(Ad ad) {
        JobSchedule jobSchedule;
        AdJob job;
        SchedulingService ss;
        String jobName, jobGroup;

        ss = (SchedulingService) Application.getInstance().getService(SchedulingService.class);
        if(ss == null) {
            log.error("Error getting scheduling service");
            return;
        }

        // remove from scheduler first, if exist
        jobName = "end-" + ad.getAdId();
        jobGroup = AdModule.class.getName();
        jobSchedule = null;
        try {
            jobSchedule = ss.getJobSchedule(jobName, jobGroup);

            try {
                ss.unscheduleJob(jobSchedule);
            } catch(SchedulingException e) {
                log.error("Error unscheduling AdJob", e);
            }

        } catch(Exception e) {
            // job is not in schedule, no need to remove
        }
        if(ad.isEndDateEnabled()) {
            // add the scheduler, if active

            // create the schedule/trigger
            jobSchedule = new JobSchedule(jobName, JobSchedule.SECONDLY);
            jobSchedule.setGroup(jobGroup);
            Calendar cal = Calendar.getInstance();
            cal.setTime(ad.getEndDate());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            jobSchedule.setStartTime(cal.getTime());
            jobSchedule.setRepeatCount(0);

            // create the job
            job = new AdJob();
            job.setName(jobName);
            job.setGroup(jobGroup);
            job.getJobTaskData().put("id", ad.getAdId());
            job.getJobTaskData().put("active", false);

            // schedule the job
            try {
                ss.scheduleJob(job, jobSchedule);
            } catch(SchedulingException e) {
                log.error("Error scheduling AdJob", e);
            }
        }
    }
}
