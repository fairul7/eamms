package com.tms.cms.syndication.model;

import kacang.model.DefaultModule;

import java.util.Collection;

public class SyndicationModule extends DefaultModule {

    public void init() {
    }

    public void addSynObject(SynObject object) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        dao.insertSynObject(object);
    }


    public void deleteSynObject(String id) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        dao.deleteSynObject(id);
    }

    public void deleteSynObject(String link, String userId) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        dao.deleteSynObject(link, userId);
    }

    public Collection getSynObjectByUserId(String userId) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        return dao.selectSynObjectByUserId(userId);
    }

    public Collection getSynObject(String id) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        return dao.selectSynObject(id);
    }

    public Collection getSynObject(String link, String userId) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        return dao.selectSynObject(link,userId);
    }

    public void updateSynObject(SynObject object,String id) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        dao.updateSynObject(object,id);
    }

   
    public Collection getSynFeedPredefined() throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        return dao.selectSynFeedPredefined();
    }

    public Collection getSynFeedPredefined(String id) throws SyndicationDaoException {
        SyndicationDao dao = (SyndicationDao) getDao();
        return dao.selectSynFeedPredefined(id);
    }

}
