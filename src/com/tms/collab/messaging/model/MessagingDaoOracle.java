package com.tms.collab.messaging.model;

import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DaoQuery;
import kacang.util.Log;

import javax.mail.internet.AddressException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.io.IOException;

public class MessagingDaoOracle extends MessagingDao
{
    private static String MESSAGE_ID_HEADER = "MESSAGE-ID";
    /**
     * @deprecated don't use this
     * @param folderId
     * @param start
     * @param numOfRows
     * @param sortBy
     * @param isDescending
     * @param searchBy
     * @param searchCriteria
     * @return
     * @throws MessagingDaoException
     */
    public Collection selectMessages(String folderId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws MessagingDaoException {
        Collection messages = new ArrayList();
        String sql;
        Object[] args;
        List argsList = new ArrayList();

        sql = "SELECT mm.messageId, mm.accountId, mm.readFlag, mm.fromField, mm.toField, " +
                "mm.ccField, mm.bccField, mm.subject, mm.body, mm.messageFormat, mm.messageDate, " +
                "mm.attachmentCount, mm.storageFilename, mm.headers, ma.indicator " +
                "FROM emlMessage mm " +
                "LEFT OUTER JOIN emlAccount ma ON mm.accountId = ma.accountId";

        if (folderId != null && !folderId.trim().equals("")) {
            sql += " WHERE mm.folderId=?";
            argsList.add(folderId);
        }
        if (searchCriteria != null && !searchCriteria.trim().equals("") && !searchBy.trim().equals("userId") && !searchBy.trim().equals("groupId")) {
            if (folderId != null && !folderId.trim().equals(""))
                sql += " AND mm." + searchBy + " LIKE ?";
            else
                sql += " WHERE mm." + searchBy + " LIKE ?";

            argsList.add("%" + searchCriteria + "%");
        }
        if (sortBy != null && !sortBy.trim().equals("")) {
            if (sortBy.equalsIgnoreCase("indicator"))
                sql += " ORDER BY ma." + sortBy;
            else
                sql += " ORDER BY mm." + sortBy;
        } else
            sql += " ORDER BY mm.messageDate DESC";

        if (isDescending && (sql.toUpperCase().lastIndexOf("DESC") != (sql.length() - 4))) {
            sql += " DESC";
        }

        args = argsList.toArray();
        try {
            messages = super.select(sql, DefaultDataObject.class, args, start, numOfRows);
            return transformToMessages(messages);

        } catch (DaoException e) {
            throw new MessagingDaoException(e);
        }
    }

    public Collection selectMessages(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws MessagingDaoException {
        Collection messages = new ArrayList();
        String sql;

        sql = "SELECT mm.messageId, mm.accountId, mm.readFlag, mm.fromField, mm.toField, " +
                "mm.ccField, mm.bccField, mm.subject, mm.body, mm.messageFormat, mm.messageDate, " +
                "mm.attachmentCount, mm.storageFilename, mm.headers, ma.indicator " +
                "FROM emlMessage mm " +
                "LEFT OUTER JOIN emlAccount ma ON mm.accountId = ma.accountId "+
                "WHERE 'a'='a' " + query.getStatement();

        sql += getSort(sort, descending);

        try {
            messages = super.select(sql, DefaultDataObject.class, query.getArray(), start, maxResults);
            return transformToMessages(messages);

        } catch (DaoException e) {
            throw new MessagingDaoException(e);
        }
    }

    public int selectMessagesCount(DaoQuery query) throws MessagingDaoException {
        String sql;

        sql = "SELECT COUNT(messageId) AS total " +
                "FROM emlMessage mm " +
                "LEFT OUTER JOIN emlAccount ma ON mm.accountId = ma.accountId "+
                "WHERE 'a'='a' " + query.getStatement();

        try {

            Collection col = super.select(sql, HashMap.class, query.getArray(), 0, -1);
            Map map = (Map) col.iterator().next();
            return Integer.parseInt(map.get("total").toString());

        } catch (DaoException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
    }

    /**
     * Find messages in outbox that does not have its error flag set to
     * {@link com.tms.collab.messaging.model.Message.ERROR_FLAG_FAIL}.
     * {@link com.tms.collab.messaging.model.Message} that is marked with
     * {@link com.tms.collab.messaging.model.Message.ERROR_FLAG_FAIL} will
     * not be send unless the flag is cleared.
     *
     * @return Message[]
     * @throws MessagingDaoException
     */
    public Message[] findMessagesInOutBox() throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        List messages = new ArrayList();
        boolean autoCommit = true;
        int isolationLevel = 0;

        try {
            conn = getDataSource().getConnection();
            autoCommit = conn.getAutoCommit();
            isolationLevel = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(
                    "SELECT msg.messageId as messageId, msg.folderId as folderId, "+
                            "msg.accountId as accountId, msg.readFlag as readFlag, " +
                            "msg.fromField as fromField, msg.toField as toField, "+
                            "msg.ccField as ccField, msg.bccField as bccField, " +
                            "msg.subject as subject, msg.body as body, "+
                            "msg.messageFormat as messageFormat, "+
                            "msg.messageDate as messageDate, "+
                            "msg.attachmentCount as attachmentCount, " +
                            "msg.storageFilename as storageFilename, "+
                            "msg.headers as headers, "+
                            "msg.errorFlag as errorFlag, "+
                            "msg.copyToSent as copyToSent "+
                            "from emlMessage msg "+
                            "inner join emlFolder folder on folder.folderId = msg.folderId "+
                            "and folder.specialFolder= ? "+
                            "and folder.name= ? "+
                            "and ((msg.errorFlag <> ? AND msg.errorFlag <> ?) or msg.errorFlag is null)"
            );
            pstmt2 = conn.prepareStatement("UPDATE emlMessage SET errorFlag=? WHERE messageId=?");

            pstmt.setBoolean(1, true);
            pstmt.setString(2, Folder.FOLDER_OUTBOX);
            pstmt.setString(3, Message.ERROR_FLAG_FAIL);
            pstmt.setString(4, Message.ERROR_FLAG_PENDING);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                String messageId = rs.getString("messageId");
                String folderId = rs.getString("folderId");
                String accountId = rs.getString("accountId");
                boolean readFlag = rs.getBoolean("readFlag");
                String fromField = rs.getString("fromField");
                String toField = rs.getString("toField");
                String ccField = rs.getString("ccField");
                String bccField = rs.getString("bccField");
                String subject = rs.getString("subject");
                String body = rs.getString("body");
                String messageFormat = rs.getString("messageFormat");
                Date messageDate = rs.getDate("messageDate");
                int attachmentCount = rs.getInt("attachmentCount");
                String storageFilename = rs.getString("storageFilename");
                String headers = rs.getString("headers");
                String errorFlag = rs.getString("errorFlag");
                boolean copyToSent = rs.getBoolean("copyToSent");

                Message message = new Message();
                message.setMessageId(messageId);
                message.setFolderId(folderId);
                message.setAccountId(accountId);
                message.setReadFlag(readFlag);
                message.setFrom(fromField);
                message.setToList(Util.convertStringToInternetRecipientsList(toField));
                message.setToIntranetList(Util.convertStringToIntranetRecipientsList(toField));
                message.setCcList(Util.convertStringToInternetRecipientsList(ccField));
                message.setCcIntranetList(Util.convertStringToIntranetRecipientsList(ccField));
                message.setBccList(Util.convertStringToInternetRecipientsList(bccField));
                message.setBccIntranetList(Util.convertStringToIntranetRecipientsList(bccField));
                message.setSubject(subject);
                message.setBody(body);
                message.setDate(messageDate);
                message.setAttachmentCount(attachmentCount);
                message.setStorageFilename(storageFilename);
                message.setHeaders(Util.convertStringToMessageHeaders(headers));
                message.setErrorFlag(errorFlag);
                message.setCopyToSent(copyToSent);

                messages.add(message);

                // update flag
                pstmt2.setString(1, Message.ERROR_FLAG_PENDING);
                pstmt2.setString(2, messageId);
                pstmt2.executeUpdate();
            }
            
            conn.commit();
        }
        catch(Exception e) {
        	try {
				conn.rollback();
			} 
        	catch (Exception e1) {
			}
        	Log.getLog(getClass()).error("Error finding messages in outbox: " + e.toString(), e);
        }
        finally {
    		if (pstmt2 != null) {
    			try {
					pstmt2.close();
				} 
    			catch (Exception e) {
				}
    		}
        	if (conn != null) {
        		try {
        			conn.setTransactionIsolation(isolationLevel);
        			conn.setAutoCommit(autoCommit);
        		}
        		catch(Exception e) {
        		}
        	}
            closeConnection(conn, pstmt, rs);
        }
        Log.getLog(getClass()).debug("number messages from outbox = "+messages.size());
        return (Message[]) messages.toArray(new Message[0]);
    }

    public void insertMessage(Message message) throws MessagingDaoException {
        List toList, ccList, bccList;
        try {
            StringBuffer statement = new StringBuffer("INSERT INTO emlMessage(messageId, " +
                    "folderId, accountId, readFlag, fromField, toField, ccField, bccField, subject, body, " +
                    "messageFormat, messageDate, attachmentCount, " +
                    "storageFilename, headers, messageIdHeader, copyToSent, errorFlag) "+
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ArrayList objects = new ArrayList();

            objects.add(message.getMessageId());
            objects.add(message.getFolderId());
            objects.add(message.getAccountId());
            objects.add(message.isRead()? "1":"0");
            objects.add(message.getFrom());

            toList = new ArrayList(message.getToList());
            if(!(message.getToIntranetList() == null))
                toList.addAll(message.getToIntranetList());
            objects.add(Util.convertRecipientsListToString(toList));

            ccList = new ArrayList(message.getCcList());
            if(!(message.getCcIntranetList() == null))
                ccList.addAll(message.getCcIntranetList());
            objects.add(Util.convertRecipientsListToString(ccList));

            bccList = new ArrayList(message.getBccList());
            if(!(message.getBccIntranetList() == null))
                bccList.addAll(message.getBccIntranetList());
            objects.add(Util.convertRecipientsListToString(bccList));

            objects.add(message.getSubject());
            objects.add(message.getBody());
            objects.add(new Integer(message.getMessageFormat()));
            objects.add(message.getDate());
            objects.add(new Integer(message.getAttachmentCount()));
            objects.add(message.getStorageFilename());
            objects.add(Util.convertMessageHeadersToString(message.getHeaders()));
            objects.add(message.getHeaders().get(MESSAGE_ID_HEADER));
            objects.add(message.getCopyToSent()?"1":"0");
            objects.add(message.getErrorFlag());

            super.update(statement.toString(), objects.toArray(new Object[] {}));

        } catch (Exception e) {
            throw new MessagingDaoException("Error occured while inserting message", e);
        }
    }

    public void updateMessage(Message message) throws MessagingDaoException {
        List toList, ccList, bccList;

        try {
            String sql = "UPDATE emlMessage SET " +
                    "folderId=?, accountId=?, readFlag=?, fromField=?, toField=?, ccField=?, " +
                    "bccField=?, subject=?, body=?, " +
                    "messageFormat=?, messageDate=?, attachmentCount=?, " +
                    "storageFilename=?, headers=?, messageIdHeader=?,  "+
                    "errorFlag=? "+
                    "WHERE messageId=?";

            ArrayList objects = new ArrayList();
            objects.add(message.getFolderId());
            objects.add(message.getAccountId());
            objects.add(message.isRead()?"1":"0");
            objects.add(message.getFrom());

            toList = new ArrayList(message.getToList());
            toList.addAll(message.getToIntranetList());
            objects.add(Util.convertRecipientsListToString(toList));

            ccList = new ArrayList(message.getCcList());
            ccList.addAll(message.getCcIntranetList());
            objects.add(Util.convertRecipientsListToString(ccList));

            bccList = new ArrayList(message.getBccList());
            bccList.addAll(message.getBccIntranetList());
            objects.add(Util.convertRecipientsListToString(bccList));

            objects.add(message.getSubject());
            objects.add(message.getBody());
            objects.add(new Integer(message.getMessageFormat()));
            objects.add(new Timestamp(message.getDate().getTime()));
            objects.add(new Integer(message.getAttachmentCount()));
            objects.add(message.getStorageFilename());
            objects.add(Util.convertMessageHeadersToString(message.getHeaders()));
            objects.add((String) message.getHeaders().get(MESSAGE_ID_HEADER));
            objects.add(message.getErrorFlag());
            objects.add(message.getMessageId());

            super.update(sql,objects.toArray(new Object[] {}));

        } catch (Exception e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
    }
}
