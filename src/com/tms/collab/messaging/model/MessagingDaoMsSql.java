package com.tms.collab.messaging.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.mail.internet.AddressException;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

public class MessagingDaoMsSql extends MessagingDao {

    private static Log log = Log.getLog(MessagingDao.class);

    public void init() throws DaoException {
        super.init();

        try {
            super.update("ALTER TABLE emlFolder alter column specialFolder smallint", null);
        }catch(DaoException e) {
            log.debug("Cannot (No need to) update messaging table", e);
        }

    }
    
    public Message selectMessage(String messageId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT folderId, accountId, readFlag, fromField, toField, " +
                    "ccField, bccField, subject, body, messageFormat, messageDate, " +
                    "attachmentCount, storageFilename, headers FROM emlMessage " +
                    "WHERE messageId=?");
            pstmt.setString(1, messageId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                message = new Message();
                message.setMessageId(messageId);
                message.setFolderId(rs.getString("folderId"));
                message.setAccountId(rs.getString("accountId"));
                message.setRead(rs.getString("readFlag").equals("0")?false:true);
                message.setFrom(rs.getString("fromField"));

                String toField = rs.getString("toField");
                String ccField = rs.getString("ccField");
                String bccField = rs.getString("bccField");

                try {
                    message.setToList(Util.convertStringToInternetRecipientsList(toField));
                }
                catch (AddressException e) {
                    // ignore;
                }
                try {
                    message.setCcList(Util.convertStringToInternetRecipientsList(ccField));
                }
                catch (AddressException e) {
                    // ignore;
                }
                try {
                    message.setBccList(Util.convertStringToInternetRecipientsList(bccField));
                }
                catch (AddressException e) {
                    // ignore;
                }
                try {
                    message.setToIntranetList(Util.convertStringToIntranetRecipientsList(toField));
                }
                catch (AddressException e) {
                    // ignore;
                }
                try {
                    message.setCcIntranetList(Util.convertStringToIntranetRecipientsList(ccField));
                }
                catch (AddressException e) {
                    // ignore;
                }
                try {
                    message.setBccIntranetList(Util.convertStringToIntranetRecipientsList(bccField));
                }
                catch (AddressException e) {
                    // ignore;
                }

                message.setSubject(rs.getString("subject"));
                message.setBody(rs.getString("body"));
                message.setMessageFormat(rs.getInt("messageFormat"));
                message.setDate(rs.getTimestamp("messageDate"));
                message.setAttachmentCount(rs.getInt("attachmentCount"));
                message.setStorageFilename(rs.getString("storageFilename"));
                message.setHeaders(Util.convertStringToMessageHeaders(rs.getString("headers")));

                return message;
            } else {
                throw new MessagingDaoException("Message not found");
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }
    
    public Message[] findMessagesInOutBox() throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List messages = new ArrayList();

        try {
            conn = getDataSource().getConnection();
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
                            "from emlMessage as msg "+
                            "inner join emlFolder as folder on folder.folderId = msg.folderId "+
                            "and folder.specialFolder= ? "+
                            "and folder.name= ? "+
                            "and msg.errorFlag <> ? "
            );

            pstmt.setBoolean(1, true);
            pstmt.setString(2, Folder.FOLDER_OUTBOX);
            pstmt.setString(3, Message.ERROR_FLAG_FAIL);

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
                Date messageDate = rs.getTimestamp("messageDate");
                int attachmentCount = rs.getInt("attachmentCount");
                String storageFilename = rs.getString("storageFilename");
                String headers = rs.getString("headers");
                String errorFlag = rs.getString("errorFlag");
                boolean copyToSent = rs.getString("copyToSent").equals("0")?false:true;

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
            }
        }
        catch(SQLException e) {
            log.error(e.toString(), e);
        }
        catch(AddressException e) {
            log.error(e.toString(), e);
        }
        catch(IOException e) {
            log.error(e.toString(), e);
        }
        finally {
            closeConnection(conn, pstmt, rs);
        }
        log.debug("number messages from outbox = "+messages.size());
        return (Message[]) messages.toArray(new Message[0]);
    }
    
    public void insertMessage(Message message) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        List toList, ccList, bccList;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("INSERT INTO emlMessage(messageId, " +
                    "folderId, accountId, readFlag, fromField, toField, ccField, bccField, subject, body, " +
                    "messageFormat, messageDate, attachmentCount, " +
                    "storageFilename, headers, messageIdHeader, copyToSent, errorFlag) "+
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setString(1, message.getMessageId());
            pstmt.setString(2, message.getFolderId());
            pstmt.setString(3, message.getAccountId());
            pstmt.setBoolean(4, message.isRead());
            pstmt.setString(5, message.getFrom());

            toList = new ArrayList(message.getToList());
            if(!(message.getToIntranetList() == null))
                toList.addAll(message.getToIntranetList());
            pstmt.setString(6, Util.convertRecipientsListToString(toList));

            ccList = new ArrayList(message.getCcList());
            if(!(message.getCcIntranetList() == null))
                ccList.addAll(message.getCcIntranetList());
            pstmt.setString(7, Util.convertRecipientsListToString(ccList));

            bccList = new ArrayList(message.getBccList());
            if(!(message.getBccIntranetList() == null))
                bccList.addAll(message.getBccIntranetList());
            pstmt.setString(8, Util.convertRecipientsListToString(bccList));

            pstmt.setString(9, message.getSubject());
            pstmt.setString(10, message.getBody());
            pstmt.setInt(11, message.getMessageFormat());
            pstmt.setTimestamp(12, new Timestamp(message.getDate().getTime()));
            pstmt.setInt(13, message.getAttachmentCount());
            pstmt.setString(14, message.getStorageFilename());
            pstmt.setString(15, Util.convertMessageHeadersToString(message.getHeaders()));
            pstmt.setString(16, (String) message.getHeaders().get("MESSAGE-ID"));
            pstmt.setString(17, message.getCopyToSent()?"1":"0");
            pstmt.setString(18, message.getErrorFlag());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }
    
    public Filter selectFilter(String filterId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        Filter filter = null;

        try {
            conn = getDataSource().getConnection();

            // find the filter
            pstmt = conn.prepareStatement(
                    "SELECT "+
                            "filterId as filterId, "+
                            "filterValue as filterValue, " +
                            "filterActive as filterActive, "+
                            "userId as userId, "+
                            "name as name, "+
                            "filterAction as filterAction, "+
                            "filterCondition as filterCondition, "+
                            "filterOrder as filterOrder "+
                            "FROM emlFilter WHERE filterId = ? ORDER BY filterOrder ASC");
            pstmt.setString(1, filterId);
            rs = pstmt.executeQuery();

            // then all of its rules
            pstmt2 = conn.prepareStatement(
                    "SELECT "+
                            "ruleId as ruleId, "+
                            "criteria as criteria, "+
                            "predicate as predicate, "+
                            "predicateValue as predicateValue "+
                            "FROM emlFilterRule WHERE filterId = ?");
            pstmt2.setString(1, filterId);
            rs2 = pstmt2.executeQuery();

            // extract rules info and populate value object 
            List rules = new ArrayList();
            while(rs2.next()) {
                Filter.Rule rule = new Filter.Rule(rs2.getString("ruleId"),
                        rs2.getString("criteria"), rs2.getString("predicate"),
                        rs2.getString("predicateValue"));
                rules.add(rule);
            }

            // extract filter info and populate value object
            if (rs.next()) {
            	//original code, there is a bug in ms sql jdbc driver in rs.getBoolean()
            	//refer http://support.microsoft.com/default.aspx?scid=kb%3Ben-us%3B920765 for more info
            	
//                filter = new Filter(rs.getString("filterId"), rs.getString("userId"),
//                        rs.getString("name"), rs.getString("filterValue"),
//                        rs.getBoolean("filterActive"), rs.getString("filterAction"),
//                        rs.getString("filterCondition"), rules);
                
                filter = new Filter(rs.getString("filterId"), rs.getString("userId"),
                        rs.getString("name"), rs.getString("filterValue"),
                        rs.getString("filterActive").equals("1")?true:false, 
                        rs.getString("filterAction"),
                        rs.getString("filterCondition"), rules);
                
                filter.setFilterOrder(rs.getInt("filterOrder"));

                // if we are dealing with MOVE_TO_FOLDER
                if (FilterActionEnum.getEnum(
                        rs.getString("filterAction").trim()).equals(FilterActionEnum.MOVE_TO_FOLDER)) {
                    String folderId = rs.getString("filterValue");
                    pstmt3 = conn.prepareStatement("SELECT name as name from emlFolder where folderId = ?");
                    pstmt3.setString(1, folderId);
                    rs3 = pstmt3.executeQuery();

                    if (rs3.next()) {
                        String folderName = rs3.getString("name");
                        filter.setFolderName(folderName);
                    }
                }
                return filter;
            }
            else {
                throw new MessagingDaoException("Filter not found");
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(null, pstmt3, rs3);
            closeConnection(null, pstmt2, rs2);
            closeConnection(conn, pstmt, rs);
        }
    }
    
    public Collection selectMessages(DaoQuery query, int start, int maxResults, String sort, boolean descending) throws MessagingDaoException {
        Collection messages = new ArrayList();
        String sql;

        sql = "SELECT mm.messageId, mm.accountId, mm.readFlag, mm.fromField, mm.toField, " +
                "mm.ccField, mm.bccField, mm.subject, mm.body, mm.messageFormat, mm.messageDate, " +
                "mm.attachmentCount, mm.storageFilename, mm.headers, ma.indicator, " +
                "mm.errorFlag, mm.copyToSent "+
                "FROM emlMessage as mm " +
                "LEFT OUTER JOIN emlAccount as ma ON mm.accountId = ma.accountId "+
                "WHERE 'a'='a' " + query.getStatement();

        sql += getSort(sort, descending);

        try {
            messages = super.select(sql, DefaultDataObject.class, query.getArray(), start, maxResults);
            return transformToMessages(messages);

        } catch (DaoException e) {
            throw new MessagingDaoException(e);
        }

    }

    public static String getSort(String sort, boolean descending)
    {
        String strSort = "";
        if(sort != null)
        {
            strSort += " ORDER BY " + (sort.equals("fromField")?"cast(fromField AS varchar(255))":sort);
            if(descending)
                strSort += " DESC";
        }
        return strSort;
    }

    public Message selectPreviousMessage(Date date,String folderId) throws MessagingDaoException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT TOP 1 messageId,folderId, accountId, readFlag, fromField, toField, " +
                    "ccField, bccField, subject, body, messageFormat, messageDate, " +
                    "attachmentCount, storageFilename, headers FROM emlMessage " +
                    "WHERE messageDate < ? AND folderId = ? ORDER BY messageDate DESC");
            pstmt.setTimestamp(1, new Timestamp(date.getTime()));
            pstmt.setString(2,folderId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                message = new Message();
                message.setMessageId(rs.getString("messageId"));
                message.setFolderId(rs.getString("folderId"));
                message.setAccountId(rs.getString("accountId"));
                message.setRead(rs.getBoolean("readFlag"));
                message.setFrom(rs.getString("fromField"));
                try {
                    String toField = rs.getString("toField");
                    String ccField = rs.getString("ccField");
                    String bccField = rs.getString("bccField");
                    message.setToList(Util.convertStringToInternetRecipientsList(toField));
                    message.setCcList(Util.convertStringToInternetRecipientsList(ccField));
                    message.setBccList(Util.convertStringToInternetRecipientsList(bccField));
                    message.setToIntranetList(Util.convertStringToIntranetRecipientsList(toField));
                    message.setCcIntranetList(Util.convertStringToIntranetRecipientsList(ccField));
                    message.setBccIntranetList(Util.convertStringToIntranetRecipientsList(bccField));
                } catch (AddressException e) {
                    throw new MessagingDaoException(e.getMessage(), e);
                }
                message.setSubject(rs.getString("subject"));
                message.setBody(rs.getString("body"));
                message.setMessageFormat(rs.getInt("messageFormat"));
                message.setDate(rs.getTimestamp("messageDate"));
                message.setAttachmentCount(rs.getInt("attachmentCount"));
                message.setStorageFilename(rs.getString("storageFilename"));
                message.setHeaders(Util.convertStringToMessageHeaders(rs.getString("headers")));

                return message;
            } else {
                throw new MessagingDaoException("Message not found");
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }
}
