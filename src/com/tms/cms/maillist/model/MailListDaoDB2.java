package com.tms.cms.maillist.model;

import java.util.Collection;
import java.util.HashMap;

import kacang.model.DaoException;

public class MailListDaoDB2 extends MailListDao{
	
	public Collection selectMailLogs(String search, String sort, boolean direction, int start, int maxRows) throws MailListDaoException {
        Collection collection;

        String sql = "SELECT id, startDate, endDate, message, mailListType, " +
        "html, senderEmail, recipientsEmail, subject, content " +
        "FROM ml_log WHERE id=? OR UPPER(message) LIKE ? OR UPPER(senderEmail) LIKE ? OR " +
        "recipientsEmail LIKE ? OR UPPER(subject) LIKE ? OR content LIKE ?";

        String sql2 = "SELECT id, startDate, endDate, message, mailListType, " +
        "html, senderEmail, recipientsEmail, subject, content " +
        "FROM ml_log";

        Object[] args = new Object[]{
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%",
            "%" + search + "%",
            "%" + search.toUpperCase() + "%",
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
	
	public int selectMailLogCount(String search) throws MailListDaoException {
        Collection collection;
        HashMap map;

        String sql = "SELECT COUNT(*) AS total FROM ml_log WHERE id=? OR " +
        "UPPER(message) LIKE ? OR UPPER(senderEmail) LIKE ? OR recipientsEmail LIKE ? OR " +
        "UPPER(subject) LIKE ? OR content LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ml_log";

        Object[] args = new Object[]{
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%",
            "%" + search + "%",
            "%" + search.toUpperCase() + "%",
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
        "FROM ml_template WHERE id=? OR UPPER(name) LIKE ? OR description LIKE ?";
        String sql2 = "SELECT id, name, description, html, header, footer " +
        "FROM ml_template";

        Object[] args = new Object[]{
            search,
            "%" + search.toUpperCase() + "%",
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
	
	public int selectMailTemplateCount(String search) throws MailListDaoException {
        Collection collection;
        HashMap map;

        String sql = "SELECT COUNT(*) AS total FROM ml_template WHERE id=? OR UPPER(name) LIKE ? OR description LIKE ?";
        String sql2 = "SELECT COUNT(*) AS total FROM ml_template";

        Object[] args = new Object[]{
            search,
            "%" + search.toUpperCase() + "%",
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
	
	public int selectMailListCount(int mailListType, String search) throws MailListDaoException {
        Collection collection;
        HashMap map;

        String sql = "SELECT COUNT(*) AS total FROM ml_maillist " +
        "WHERE mailListType=? AND (id=? OR UPPER(name) LIKE ? OR description LIKE ? OR " +
        "UPPER(senderEmail) LIKE ? OR UPPER(subject) LIKE ? OR recipientsEmail LIKE ? OR " +
        "body LIKE ?)";

        String sql2 = "SELECT COUNT(*) AS total FROM ml_maillist WHERE mailListType=?";

        Object[] args = new Object[]{
            new Integer(mailListType),
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search + "%",
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%",
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
        "WHERE mailListType=? AND (id=? OR UPPER(name) LIKE ? OR description LIKE ? OR " +
        "UPPER(senderEmail) LIKE ? OR UPPER(subject) LIKE ? OR recipientsEmail LIKE ? OR " +
        "body LIKE ?)";

        String sql2 = "SELECT id, mailListType, name, " +
        "description, html, senderEmail, subject, recipientsEmail, " +
        "templateId, body, header, footer, contentIds, active, startDate, " +
        "endDate, contentId, scheduleRepeatInterval, scheduleDay, " +
        "scheduleStartTime, scheduleEndTime, unsubscribedEmails FROM ml_maillist mailListType=?";

        Object[] args = new Object[]{
            new Integer(mailListType),
            search,
            "%" + search.toUpperCase() + "%",
            "%" + search + "%",
            "%" + search.toUpperCase() + "%",
            "%" + search.toUpperCase() + "%",
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
	
	private Collection localSelect(String sql, Class resultClass, Object args, int start, int maxRows) throws MailListDaoException {
        try {
            return super.select(sql, resultClass, args, start, maxRows);
        } catch (DaoException e) {
            throw new MailListDaoException(e);
        }
    }

}
