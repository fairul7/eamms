package com.tms.mugshot.model;

import kacang.model.DefaultModule;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.util.Log;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageException;
import kacang.Application;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Jul 6, 2005
 * Time: 2:56:29 PM
 */
public class MugshotModule extends DefaultModule{
    public static String ROOT_DIR = "/mugshot";
    public static String DEFAULT_MUGSHOT = "/mugshot/default.jpg";
    private MugshotDao dao;
    private Log log = Log.getLog(getClass());
    public void init() {
        super.init();
    }

    public Mugshot get(String userId){
        Mugshot mugshot = null;
        dao = (MugshotDao) getDao();
        try {
            mugshot = dao.get(userId);
            return mugshot;
        } catch (DaoException e) {
            log.error("Error in Dao for Get Mugshot.", e);
        } catch (DataObjectNotFoundException e) {
        }
        return mugshot;
    }

    /**
     * Save or update a user mugshot. If update, remove existing mugshot from storage
     * before uploading.
     * @param mug
     */
    public void save(Mugshot mug, StorageFile sf){
        dao = (MugshotDao) getDao();
        try {
            Mugshot currentMug = dao.get(mug.getUserId());
            remove(currentMug);
        } catch (DataObjectNotFoundException e) {
            log.info("New Mugshot added for:"+mug.getUserId());
        } catch (DaoException e) {
            log.error("error in removing mugshot", e);
        }
        try {
            StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
            ss.store(sf);
            dao.save(mug);
        } catch (DaoException e) {
            log.error("error in save mugshot dao", e);
        } catch (StorageException e) {
            log.error("error in storing mugshot", e);
        }
    }

    public void remove(Mugshot mug){
        dao = (MugshotDao) getDao();
            StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
            StorageFile sf = new StorageFile(mug.getFilePath());
        try {
            ss.delete(sf);
            dao.remove(mug.getUserId());
        } catch (StorageException e) {
            log.error("error in deleting mugshot from storage", e);
        } catch (DaoException e) {
            log.error("Error in removing mugshot for:"+ mug.getUserId(), e);
        }
    }
}
