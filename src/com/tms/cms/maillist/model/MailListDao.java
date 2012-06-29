package com.tms.cms.maillist.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.util.Log;
import kacang.util.Transaction;

import java.sql.Time;
import java.util.*;

public class MailListDao extends DataSourceDao {
    private Log log = Log.getLog(getClass());

    /**
     * Creates database tables.
     */
    public void init() throws DaoException {
        super.update("CREATE TABLE ml_maillist(id VARCHAR(35) PRIMARY KEY, mailListType INT, " +
        "name VARCHAR(255), description TEXT, html CHAR(1), senderEmail VARCHAR(255), " +
        "subject VARCHAR(255), recipientsEmail TEXT, templateId VARCHAR(35), " +
        "body TEXT, header TEXT, footer TEXT, contentIds TEXT, active CHAR(1), " +
        "startDate DATETIME, endDate DATETIME, contentId VARCHAR(255), " +
        "scheduleRepeatInterval VARCHAR(255), scheduleDay INT, " +
        "scheduleStartTime DATETIME, scheduleEndTime DATETIME, unsubscribedEmails TEXT) ", null);

        super.update("CREATE TABLE ml_log(id VARCHAR(35) PRIMARY KEY, " +
        "startDate DATETIME, endDate DATETIME, message VARCHAR(255), " +
        "mailListType INT, html CHAR(1), senderEmail VARCHAR(255),  " +
        "recipientsEmail TEXT, subject VARCHAR(255), content TEXT) ", null);

        super.update("CREATE TABLE ml_template(id VARCHAR(35) PRIMARY KEY, " +
        "name VARCHAR(255), description TEXT, html CHAR(1), header TEXT, footer TEXT)", null);

        super.update("CREATE TABLE ml_maillisthistory(mailListId VARCHAR(35), contentId VARCHAR(255))", null);
    }


    // === [ MailList ] ========================================================
    public void insertMailList(MailList ml) throws MailListDaoException {
        Transaction tx = null;

        String sql = "INSERT INTO ml_maillist(id, mailListType, name, " +
        "description, html, senderEmail, subject, recipientsEmail, " +
        "templateId, body, header, footer, contentIds, active, startDate, " +
        "endDate, contentId, scheduleRepeatInterval, scheduleDay, " +
        "scheduleStartTime, scheduleEndTime, unsubscribedEmails) " +
        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        // 22 arguments!
        Object[] args = {
            ml.getId(),
            new Integer(ml.getMailListType()),
            ml.getName(),
            ml.getDescription(),
            new Boolean(ml.isHtml()),
            ml.getSenderEmail(),
            ml.getSubject(),
            ml.getRecipientsEmail(),
            ml.getTemplateId(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        };

        // populate for specific MailList implementation
        if(ml instanceof ScheduledMailList) {
            ScheduledMailList sml = (ScheduledMailList) ml;
            args[13] = new Boolean(sml.isActive());
            args[14] = sml.getStartDate();
            args[15] = sml.getEndDate();
            args[16] = sml.getContentId();
            args[17] = sml.getScheduleRepeatInterval();
            args[18] = new Integer(sml.getScheduleDay());
            args[19] = new Time(sml.getScheduleStartTime().getTime());
            args[20] = new Time(sml.getScheduleEndTime().getTime());
            args[21] = sml.getUnsubscribedEmails();

        } else if(ml instanceof ContentMailList) {
            ContentMailList cml = (ContentMailList) ml;
            args[10] = cml.getHeader();
            args[11] = cml.getFooter();
            args[12] = cml.getContentIds();

        } else if(ml instanceof ComposedMailList) {
            ComposedMailList composed = (ComposedMailList) ml;
            args[9] = composed.getBody();
        }

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }

    public MailList selectMailList(String id) throws MailListDaoException {
        Collection collection;
        Map map;
        int mailListType;

        // <<< [ determine mailListType ] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String sqlMailListType = "SELECT mailListType FROM ml_maillist WHERE id=?";
        Object[] argsMailListType = { id };

        collection = localSelect(sqlMailListType, HashMap.class, argsMailListType, 0, 1);
        if(!collection.iterator().hasNext()) {
            throw new MailListDaoException("MailList not found");
        } else {
            map = (HashMap) collection.iterator().next();
            mailListType = Integer.parseInt(map.get("mailListType").toString());
        }
        // >>> [ determine mailListType ] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        String sql = "SELECT id, mailListType, name, " +
        "description, html, senderEmail, subject, recipientsEmail, " +
        "templateId, body, header, footer, contentIds, active, startDate, " +
        "endDate, contentId, scheduleRepeatInterval, scheduleDay, " +
        "scheduleStartTime, scheduleEndTime, unsubscribedEmails FROM ml_maillist WHERE id=?";
        Object[] args = { id };

        if(mailListType==MailList.MAIL_LIST_TYPE_SCHEDULED) {
            collection = localSelect(sql, ScheduledMailList.class, args, 0, 1);
        } else if(mailListType==MailList.MAIL_LIST_TYPE_CONTENT) {
            collection = localSelect(sql, ContentMailList.class, args, 0, 1);
        } else if(mailListType==MailList.MAIL_LIST_TYPE_COMPOSED) {
            collection = localSelect(sql, ComposedMailList.class, args, 0, 1);
        } else {
            throw new MailListDaoException("Invalid mailListType=" + mailListType);
        }

        if(!collection.iterator().hasNext()) {
            throw new MailListDaoException("MailList not found");
        } else {
            MailList mailList  = (MailList) collection.iterator().next();
            return mailList;
        }
    }

    public int selectMailListCount(int mailListType, String search) throws MailListDaoException {
        Collection collection;
        HashMap map;

        String sql = "SELECT COUNT(*) AS total FROM ml_maillist " +
        "WHERE mailListType=? AND (id=? OR name LIKE ? OR description LIKE ? OR " +
        "senderEmail LIKE ? OR subject LIKE ? OR recipientsEmail LIKE ? OR " +
        "body LIKE ?)";

        String sql2 = "SELECT COUNT(*) AS total FROM ml_maillist WHERE mailListType=?";

        Object[] args = new Object[]{
            new Integer(mailListType),
            search,
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%"
        };

        Object[] args2 = { new Integer(mailListType) };

        if("*".equals(search)) {
            collection = localSelect(sql2, HashMap.class, args2, 0, 1);
        } else {
            collection = localSelect(sql, HashMap.class, args, 0, 1);
        }

        map = (HashMap) collection.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public Collection selectMailLists(int mailListType, String search, String sort, boolean direction, int start, int maxRows) throws MailListDaoException {
        Collection collection;

        String sql = "SELECT id, mailListType, name, " +
        "description, html, senderEmail, subject, recipientsEmail, " +
        "templateId, body, header, footer, contentIds, active, startDate, " +
        "endDate, contentId, scheduleRepeatInterval, scheduleDay, " +
        "scheduleStartTime, scheduleEndTime, unsubscribedEmails FROM ml_maillist " +
        "WHERE mailListType=? AND (id=? OR name LIKE ? OR description LIKE ? OR " +
        "senderEmail LIKE ? OR subject LIKE ? OR recipientsEmail LIKE ? OR " +
        "body LIKE ?)";

        String sql2 = "SELECT id, mailListType, name, " +
        "description, html, senderEmail, subject, recipientsEmail, " +
        "templateId, body, header, footer, contentIds, active, startDate, " +
        "endDate, contentId, scheduleRepeatInterval, scheduleDay, " +
        "scheduleStartTime, scheduleEndTime, unsubscribedEmails FROM ml_maillist mailListType=?";

        Object[] args = new Object[]{
            new Integer(mailListType),
            search,
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%"
        };

        Object[] args2 = { new Integer(mailListType) };

        if(sort != null) {
            sql += " ORDER BY " + sort;
            if(direction) {
                sql += " DESC";
            }

            sql2 += " ORDER BY " + sort;
            if(direction) {
                sql2 += " DESC";
            }
        }

        if("*".equals(search)) {
            if(mailListType == MailList.MAIL_LIST_TYPE_SCHEDULED) {
                collection = localSelect(sql2, ScheduledMailList.class, args2, start, maxRows);
            } else if(mailListType == MailList.MAIL_LIST_TYPE_CONTENT) {
                collection = localSelect(sql2, ContentMailList.class, args2, start, maxRows);
            } else if(mailListType == MailList.MAIL_LIST_TYPE_COMPOSED) {
                collection = localSelect(sql2, ComposedMailList.class, args2, start, maxRows);
            } else {
                throw new MailListDaoException("Invalid mailListType=" + mailListType);
            }

        } else {
            if(mailListType == MailList.MAIL_LIST_TYPE_SCHEDULED) {
                collection = localSelect(sql, ScheduledMailList.class, args, start, maxRows);
            } else if(mailListType == MailList.MAIL_LIST_TYPE_CONTENT) {
                collection = localSelect(sql, ContentMailList.class, args, start, maxRows);
            } else if(mailListType == MailList.MAIL_LIST_TYPE_COMPOSED) {
                collection = localSelect(sql, ComposedMailList.class, args, start, maxRows);
            } else {
                throw new MailListDaoException("Invalid mailListType=" + mailListType);
            }

        }

        return collection;
    }

    public void updateMailList(MailList ml) throws MailListDaoException {
        Transaction tx = null;

        String sql = "UPDATE ml_maillist SET mailListType=?, name=?, " +
        "description=?, html=?, senderEmail=?, subject=?, recipientsEmail=?, " +
        "templateId=?, body=?, header=?, footer=?, contentIds=?, active=?, startDate=?, " +
        "endDate=?, contentId=?, scheduleRepeatInterval=?, scheduleDay=?, " +
        "scheduleStartTime=?, scheduleEndTime=?, unsubscribedEmails=? WHERE id=?";

        // 22 arguments!
        Object[] args = {
            new Integer(ml.getMailListType()),
            ml.getName(),
            ml.getDescription(),
            new Boolean(ml.isHtml()),
            ml.getSenderEmail(),
            ml.getSubject(),
            ml.getRecipientsEmail(),
            ml.getTemplateId(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            ml.getId()
        };

        // populate for specific MailList implementation
        if(ml instanceof ScheduledMailList) {
            ScheduledMailList sml = (ScheduledMailList) ml;
            args[9] = sml.getHeader();
            args[10] = sml.getFooter();

            args[12] = new Boolean(sml.isActive());
            args[13] = sml.getStartDate();
            args[14] = sml.getEndDate();
            args[15] = sml.getContentId();
            args[16] = sml.getScheduleRepeatInterval();
            args[17] = new Integer(sml.getScheduleDay());
            args[18] = sml.getScheduleStartTime();
            args[19] = sml.getScheduleEndTime();
            args[20] = sml.getUnsubscribedEmails();

        } else if(ml instanceof ContentMailList) {
            ContentMailList cml = (ContentMailList) ml;
            args[9] = cml.getHeader();
            args[10] = cml.getFooter();
            args[11] = cml.getContentIds();

        } else if(ml instanceof ComposedMailList) {
            ComposedMailList composed = (ComposedMailList) ml;
            args[8] = composed.getBody();
        }

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }

    public void deleteMailList(String id) throws MailListDaoException {
        Transaction tx = null;

        String sql = "DELETE FROM ml_maillist WHERE id=?";

        Object[] args = {
            id
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }


    // === [ MailLog ] ========================================================
    public void insertMailLog(MailLog mailLog) throws MailListDaoException {
        Transaction tx = null;

        String sql = "INSERT INTO ml_log(id, startDate, endDate, " +
        "message, mailListType, html, senderEmail, recipientsEmail, " +
        "subject, content) VALUES(?,?,?,?,?,?,?,?,?,?)";

        Object[] args = {
            mailLog.getId(),
            mailLog.getStartDate(),
            mailLog.getEndDate(),
            mailLog.getMessage(),
            new Integer(mailLog.getMailListType()),
            new Boolean(mailLog.isHtml()),
            mailLog.getSenderEmail(),
            mailLog.getRecipientsEmail(),
            mailLog.getSubject(),
            mailLog.getContent()
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }

    public MailLog selectMailLog(String id) throws MailListDaoException {
        Collection collection;

        String sql = "SELECT id, startDate, endDate, message, mailListType, " +
        "html, senderEmail, recipientsEmail, subject, content " +
        "FROM ml_log WHERE id=?";
        Object[] args = { id };

        collection = localSelect(sql, MailLog.class, args, 0, 1);
        if(!collection.iterator().hasNext()) {
            throw new MailListDaoException("MailLog not found");
        } else {
            MailLog mailLog = (MailLog) collection.iterator().next();
            return mailLog;
        }
    }

    public int selectMailLogCount(String search) throws MailListDaoException {
        Collection collection;
        HashMap map;

        String sql = "SELECT COUNT(*) AS total FROM ml_log WHERE id=? OR " +
        "message LIKE ? OR senderEmail LIKE ? OR recipientsEmail LIKE ? OR " +
        "subject LIKE ? OR content LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ml_log";

        Object[] args = new Object[]{
            search,
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%"
        };

        if("*".equals(search)) {
            collection = localSelect(sql2, HashMap.class, null, 0, 1);
        } else {
            collection = localSelect(sql, HashMap.class, args, 0, 1);
        }

        map = (HashMap) collection.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public Collection selectMailLogs(String search, String sort, boolean direction, int start, int maxRows) throws MailListDaoException {
        Collection collection;

        String sql = "SELECT id, startDate, endDate, message, mailListType, " +
        "html, senderEmail, recipientsEmail, subject, content " +
        "FROM ml_log WHERE id=? OR message LIKE ? OR senderEmail LIKE ? OR " +
        "recipientsEmail LIKE ? OR subject LIKE ? OR content LIKE ?";

        String sql2 = "SELECT id, startDate, endDate, message, mailListType, " +
        "html, senderEmail, recipientsEmail, subject, content " +
        "FROM ml_log";

        Object[] args = new Object[]{
            search,
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%",
            "%" + search + "%"
        };

        if(sort != null) {
            sql += " ORDER BY " + sort;
            if(direction) {
                sql += " DESC";
            }

            sql2 += " ORDER BY " + sort;
            if(direction) {
                sql2 += " DESC";
            }
        }

        if("*".equals(search)) {
            collection = localSelect(sql2, MailLog.class, null, start, maxRows);
        } else {
            collection = localSelect(sql, MailLog.class, args, start, maxRows);
        }

        return collection;
    }

    public void updateMailLog(MailLog mailLog) throws MailListDaoException {
        Transaction tx = null;

        String sql = "UPDATE ml_log SET startDate=?, endDate=?, " +
        "message=?, mailListType=?, html=?, senderEmail=?, recipientsEmail=?, " +
        "subject=?, content=? WHERE id=?";

        Object[] args = {
            mailLog.getStartDate(),
            mailLog.getEndDate(),
            new Integer(mailLog.getMailListType()),
            new Boolean(mailLog.isHtml()),
            mailLog.getSenderEmail(),
            mailLog.getRecipientsEmail(),
            mailLog.getSubject(),
            mailLog.getContent(),
            mailLog.getId()
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }

    public void deleteMailLog(String id) throws MailListDaoException {
        Transaction tx = null;

        String sql = "DELETE FROM ml_log WHERE id=?";

        Object[] args = {
            id
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }


    // === [ MailTemplate ] ========================================================
    public void insertMailTemplate(MailTemplate template) throws MailListDaoException {
        Transaction tx = null;

        String sql = "INSERT INTO ml_template(id, name, description, html, " +
        "header, footer) VALUES(?,?,?,?,?,?)";

        Object[] args = {
            template.getId(),
            template.getName(),
            template.getDescription(),
            new Boolean(template.isHtml()),
            template.getHeader(),
            template.getFooter()
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }

    public MailTemplate selectMailTemplate(String id) throws MailListDaoException {
        Collection collection;

        String sql = "SELECT id, name, description, html, header, footer " +
        "FROM ml_template WHERE id=?";
        Object[] args = { id };

        collection = localSelect(sql, MailTemplate.class, args, 0, 1);
        if(!collection.iterator().hasNext()) {
            throw new MailListDaoException("MailTemplate not found");
        } else {
            MailTemplate template = (MailTemplate) collection.iterator().next();
            return template;
        }
    }

    public int selectMailTemplateCount(String search) throws MailListDaoException {
        Collection collection;
        HashMap map;

        String sql = "SELECT COUNT(*) AS total FROM ml_template WHERE id=? OR name LIKE ? OR description LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ml_template";

        Object[] args = new Object[]{
            search,
            "%" + search + "%",
            "%" + search + "%"
        };

        if("*".equals(search)) {
            collection = localSelect(sql2, HashMap.class, null, 0, 1);
        } else {
            collection = localSelect(sql, HashMap.class, args, 0, 1);
        }

        map = (HashMap) collection.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public Collection selectMailTemplates(String search, String sort, boolean direction, int start, int maxRows) throws MailListDaoException {
        Collection collection;

        String sql = "SELECT id, name, description, html, header, footer " +
        "FROM ml_template WHERE id=? OR name LIKE ? OR description LIKE ?";
        String sql2 = "SELECT id, name, description, html, header, footer " +
        "FROM ml_template";

        Object[] args = new Object[]{
            search,
            "%" + search + "%",
            "%" + search + "%"
        };

        if(sort != null) {
            sql += " ORDER BY " + sort;
            if(direction) {
                sql += " DESC";
            }

            sql2 += " ORDER BY " + sort;
            if(direction) {
                sql2 += " DESC";
            }
        }

        if("*".equals(search)) {
            collection = localSelect(sql2, MailTemplate.class, null, start, maxRows);
        } else {
            collection = localSelect(sql, MailTemplate.class, args, start, maxRows);
        }

        return collection;
    }

    public void updateMailTemplate(MailTemplate template) throws MailListDaoException {
        Transaction tx = null;

        String sql = "UPDATE ml_template SET name=?, description=?, html=?, " +
        "header=?, footer=? WHERE id=?";

        Object[] args = {
            template.getName(),
            template.getDescription(),
            new Boolean(template.isHtml()),
            template.getHeader(),
            template.getFooter(),
            template.getId()
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }

    public void deleteMailTemplate(String id) throws MailListDaoException {
        Transaction tx = null;

        String sql = "DELETE FROM ml_template WHERE id=?";

        Object[] args = {
            id
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }


    // === [ MailList history (sent contentIds) ] ==============================
    public void insertSentItem(String mailListId, String contentId) throws MailListDaoException {
        String[] contentIds = { contentId };
        insertSentItems(mailListId, contentIds);
    }

    public void insertSentItems(String mailListId, String[] contentIds) throws MailListDaoException {
        Transaction tx = null;

        String sql = "INSERT INTO ml_maillisthistory(mailListId, contentId) VALUES(?,?)";

        Object[] args = {
            mailListId,
            null
        };

        try {
            tx = getTransaction();
            tx.begin();
            for(int i=0; i<contentIds.length; i++) {
                args[1] = contentIds[i];
                tx.update(sql, args);
            }
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }

    public List selectSentItems(String mailListId) throws MailListDaoException {
        Collection collection;
        Map map;
        List sentItemList = new ArrayList();

        String sql = "SELECT contentId FROM ml_maillisthistory WHERE mailListId=?";
        Object[] args = { mailListId };

        collection = localSelect(sql, HashMap.class, args, 0, -1);
        for(Iterator iter=collection.iterator(); iter.hasNext(); ) {
            map = (Map) iter.next();
            sentItemList.add(map.get("contentId"));
        }

        return sentItemList;
    }

    public void deleteSentItems(String mailListId) throws MailListDaoException {
        Transaction tx = null;

        String sql = "DELETE FROM ml_maillisthistory WHERE mailListId=?";
        Object[] args = {
            mailListId
        };

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, args);
            tx.commit();

        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
            throw new MailListDaoException(e);
        }
    }


    // === [ private methods ] =================================================
    private void localUpdate(String sql, Object args) throws MailListDaoException {
        try {
            super.update(sql, args);
        } catch (DaoException e) {
            throw new MailListDaoException(e);
        }
    }

    private Collection localSelect(String sql, Class resultClass, Object args, int start, int maxRows) throws MailListDaoException {
        try {
            return super.select(sql, resultClass, args, start, maxRows);
        } catch (DaoException e) {
            throw new MailListDaoException(e);
        }
    }

}
