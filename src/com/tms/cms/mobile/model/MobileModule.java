package com.tms.cms.mobile.model;

import kacang.model.DefaultModule;
import kacang.model.DataObjectNotFoundException;

import java.util.Collection;

import com.tms.cms.core.model.ContentException;

public class MobileModule extends DefaultModule {

    public void saveChannel(MobileChannel channel) throws ContentException {
        try {
            MobileDao dao = (MobileDao)getDao();
            dao.save(channel);
        } catch (Exception e) {
            throw new ContentException("Unable to save channel " + e.toString());
        }
    }

    public MobileChannel getChannel(String title) throws ContentException, DataObjectNotFoundException {
        try {
            MobileDao dao = (MobileDao)getDao();
            return dao.load(title);
        } catch(DataObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ContentException("Unable to get channel " + e.toString());
        }
    }

    public Collection getChannelList(String title, int start, int rows) throws ContentException {
        try {
            MobileDao dao = (MobileDao)getDao();
            return dao.list(title, start, rows);
        } catch (Exception e) {
            throw new ContentException("Unable to get channels " + e.toString());
        }
    }

    public int getChannelCount(String title) throws ContentException {
        try {
            MobileDao dao = (MobileDao)getDao();
            return dao.count(title);
        } catch (Exception e) {
            throw new ContentException("Unable to get channels " + e.toString());
        }
    }

    public void deleteChannel(String title) throws ContentException {
        try {
            MobileDao dao = (MobileDao)getDao();
            dao.delete(title);
        } catch (Exception e) {
            throw new ContentException("Unable to delete channel " + e.toString());
        }
    }


}
