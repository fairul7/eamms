package com.tms.cms.core.model;

import kacang.model.DefaultModule;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.Application;

import java.util.Collection;
import java.util.Date;

public class ContentAuditor extends DefaultModule {

    private Log log = Log.getLog(getClass());

    public void audit(String key, String event, String param, User user) {
        if (!isDisabled()) {
            // create ContentEvent
            ContentEvent ce = new ContentEvent();
            ce.setId(key);
            ce.setDate(new Date());
            ce.setEvent(event);
            ce.setParam(param);
            ce.setUserId(user.getId());
            ce.setUsername(user.getUsername());
            ce.setHost((String)user.getProperty(SecurityService.PROPERTY_KEY_HOST));
            ce.setMessage(null);

            // save
            addContentEvent(ce);

            // log
            log.debug("audit [" + key + ", " + event + ", " + ", " + param + ", " + user.getId() + "]");
        }
    }

    public boolean isDisabled() {
        return Boolean.valueOf(Application.getInstance().getProperty(ContentManager.APPLICATION_PROPERTY_AUDITING_DISABLED)).booleanValue();
    }

    public void addContentEvent(ContentEvent event) {
        try {
            ContentAuditorDao dao = (ContentAuditorDao)getDao();
            dao.insert(event);
        }
        catch(Exception e) {
            log.error("addContentEvent: " + e.toString());
        }
    }

    public Collection viewAudit(String key, Date startDate, Date endDate, String sort, boolean desc, int start, int rows) throws ContentException {
        try {
            ContentAuditorDao dao = (ContentAuditorDao)getDao();
            return dao.select(key, startDate, endDate, sort, desc, start, rows);
        }
        catch(Exception e) {
            log.error("viewAudit: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public int viewAuditCount(String key, Date startDate, Date endDate) throws ContentException {
        try {
            ContentAuditorDao dao = (ContentAuditorDao)getDao();
            return dao.count(key, startDate, endDate);
        }
        catch(Exception e) {
            log.error("viewAuditCount: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

    public void clearAudit(String key, Date startDate, Date endDate) throws ContentException {
        try {
            ContentAuditorDao dao = (ContentAuditorDao)getDao();
            dao.delete(key, startDate, endDate);
        }
        catch(Exception e) {
            log.error("viewContentEvent: " + e.toString());
            throw new ContentException(e.getMessage());
        }
    }

}
