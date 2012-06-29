package com.tms.collab.messaging.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import kacang.model.DaoQuery;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import javax.mail.internet.AddressException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

import org.apache.commons.collections.SequencedHashMap;

public class MessagingDao extends DataSourceDao {

    private static Log log = Log.getLog(MessagingDao.class);

    /**
     * Message-ID header as defined in RFC
     */
    private static String MESSAGE_ID_HEADER = "MESSAGE-ID";

    public void init() throws DaoException {
        super.init();
        try {
            super.update("CREATE TABLE emlQuota (principalId varchar(255) NOT NULL, quota int(11) NOT NULL default '0',PRIMARY KEY  (principalId))", null);
        } catch(DaoException e) {
            log.debug("Cannot (No need to) create messaging quota table", e);
        }
        try {
            super.update("ALTER TABLE emlFolder ADD parentId VARCHAR(50)", null);
        } catch(DaoException e) {
            log.debug("Cannot (No need to) update messaging table", e);
        }
        try {
            super.update("ALTER TABLE emlAccount ADD signature TEXT", null);
        } catch(DaoException e) {
            log.debug("Cannot (No need to) update messaging table", e);
        }
        try {
            super.update("ALTER TABLE emlAccount ADD accountLog MEDIUMTEXT", null);
            super.update("ALTER TABLE emlAccount ADD lastCheckDate DATETIME", null);
            super.update("ALTER TABLE emlAccount ADD checkCount INT", null);
            super.update("ALTER TABLE emlAccount ADD lastDownloadDate DATETIME", null);
            super.update("ALTER TABLE emlAccount ADD downloadCount INT", null);
        } catch(DaoException e) {
            log.debug("Cannot (No need to) update messaging table", e);
        }
        try {
            super.update("CREATE TABLE emlAccount( accountId VARCHAR(35) PRIMARY KEY, " +
                    "accountType INT, userId VARCHAR(255), name VARCHAR(255), indicator INT, " +
                    "deliveryFolderId VARCHAR(35), filterEnabled CHAR(1), " +
                    "intranetUserName VARCHAR(255), fromAddress VARCHAR(255), serverName VARCHAR(255), serverPort INT, " +
                    "username VARCHAR(255), password VARCHAR(255), " +
                    "leaveMailOnServer CHAR(1), lastCheckDate DATETIME, checkCount INT, lastDownloadDate DATETIME, downloadCount INT, accountLog MEDIUMTEXT, signature TEXT) ", null);
        }
        catch(DaoException e) {
            log.debug(e.toString(), e);
        }
        try {
            super.update("CREATE TABLE emlFilter( filterId VARCHAR(35) PRIMARY KEY, " +
                    "userId VARCHAR(255), name VARCHAR(255), " +
                    "filterField VARCHAR(255), filterOperation INT, filterValue " +
                    "VARCHAR(255), filterOrder INT, filterActive CHAR(1), " +
                    "folderId VARCHAR(35))", null);
        }
        catch(DaoException e) {
            log.debug(e.toString(), e);
        }
        try {
            super.update("CREATE TABLE emlFolder( folderId VARCHAR(35) PRIMARY KEY, userId VARCHAR(255), " +
                    " name VARCHAR(35), specialFolder CHAR(1), diskUsage INT, parentId VARCHAR(50) ) ", null);
        }
        catch(DaoException e) {
            log.debug(e.toString(), e);
        }
        try {
            super.update("CREATE TABLE emlMessage( messageId VARCHAR(35) PRIMARY KEY, " +
                    "folderId VARCHAR(35), accountId VARCHAR(35), readFlag CHAR(1), fromField MEDIUMTEXT, " +
                    "toField MEDIUMTEXT, ccField MEDIUMTEXT, bccField MEDIUMTEXT, " +
                    "subject VARCHAR(255), body MEDIUMTEXT, messageFormat INT, " +
                    "messageDate DATETIME, attachmentCount INT, storageFilename " +
                    "VARCHAR(255), headers MEDIUMTEXT, messageIdHeader VARCHAR(255) )", null);
        }
        catch(DaoException e) {
            log.debug(e.toString(), e);
        }
        try {
            super.update("CREATE TABLE emlSmtpAccount( smtpAccountId VARCHAR(35) PRIMARY KEY, " +
                    "userId VARCHAR(255), name VARCHAR(255), serverName VARCHAR(255), " +
                    "serverPort INT, anonymousAccess CHAR(1), username VARCHAR(255), password " +
                    "VARCHAR(255) )", null);
        }
        catch(DaoException e) {
            log.debug(e.toString(), e);
        }

        // === start changes added to accommodate message filtering ============
        try {
            update("alter table emlFilter add filterCondition varchar(255)", null);
        }
        catch(Exception e) {
            log.debug("cannot add column filterCondition to table emlfilter");
        }
        try {
            update("alter table emlFilter add filterAction varchar(255)", null);
        }
        catch(Exception e) {
            log.debug("cannot add column filterAction to table emlfilter");
        }
        try {
            update("alter table emlFilter drop filterField", null);
        }
        catch(Exception e) {
            log.debug("cannot drop column filterField from table emlfilter");
        }
        try {
            update("alter table emlFilter drop folderId", null);
        }
        catch(Exception e) {
            log.debug("cannot drop column folderId from table emlfilter");
        }
        try {
            update("alter table emlFilter drop filterOperation", null);
        }
        catch(Exception e) {
            log.debug("cannot dop emailFilter table column (filterorder, filteroperation)");
        }
        try {
            update("create table emlFilterRule ("+
                    " 	ruleId varchar(255) primary key, "+
                    "	criteria varchar(255), "+
                    "	predicate varchar(255), "+
                    "	predicateValue varchar(255), "+
                    " 	filterId varchar(255)"+
                    ")", null);
        }
        catch(Exception e) {
            log.debug("cannot create table emlfilterrule");
        }

        // index filterId fk column
        try {
            update("alter table emlFilterRule add index filterId_fk_ (filterId)",
                    null);
        }
        catch(Exception e) {
            log.debug("cannot create index (filterId_fk_) for emlfilterrule");
        }
        try {
            update("alter table emlFilter add filterOrder int", null);
        }
        catch(Exception e) {
            log.debug("cannot add column 'filterOrder' into table emlfilter");
        }
        // === end changes added to accommodate message filtering ==============

        // === start changes added to implement outbox feature =================
        try {
            update("alter table emlMessage add errorFlag varchar(35)", null);
        }
        catch(Exception e) {
            log.debug("cannot add column 'errorFlag' to table 'emlmessage'");
        }
        try {
            update("alter table emlMessage add copyToSent char(1)", null);
        }
        catch(Exception e) {
            log.debug("cannot add column 'copyToSent' to table 'emlmessage'");
        }
        // === end changes added to implement outbox feature ===================

        // create default outbox folder //
        createDefaultOutboxFolder();

        // create indices
        try
        {
            super.update("ALTER TABLE emlAccount ADD INDEX accountType(accountType)", null);
            super.update("ALTER TABLE emlAccount ADD INDEX userId(userId)", null);
            super.update("ALTER TABLE emlFilter ADD INDEX userId(userId)", null);
            super.update("ALTER TABLE emlFilterRule ADD INDEX filterId(filterId)", null);
            super.update("ALTER TABLE emlFolder ADD INDEX userId(userId)", null);
            super.update("ALTER TABLE emlMessage ADD INDEX folderId(folderId)", null);
            super.update("ALTER TABLE emlMessage ADD INDEX accountId(accountId)", null);
            super.update("ALTER TABLE emlMessage ADD INDEX messageDate(messageDate)", null);
            super.update("ALTER TABLE emlMessage ADD INDEX attachmentCount(attachmentCount)", null);
            super.update("ALTER TABLE emlSmtpAccount ADD UNIQUE userId(userId)", null);
        }
        catch(Exception e) {}

        try
        {
            super.update("ALTER TABLE emlFolder ADD INDEX name(name)", null);
        }
        catch(Exception e) {}
    }

    public void createDefaultOutboxFolder() {
        String sql1 = "insert into emlFolder " +
                "(folderId, userId, name, specialFolder, diskUsage, parentId) " +
                "values (?, ?, ?, ?, ?, ?)";
        String sql2 = "select distinct user.id as userId from security_user as user where user.id <> 'anonymous'";
        String sql3 =  "select distinct user.id as userId from security_user as user "+
                "inner join emlFolder as folder on folder.userId = user.id "+
                "where user.id <> 'anonymous' and folder.specialFolder = '1' "+
                "and folder.name in ('"+Folder.FOLDER_OUTBOX+"')";
        Set allUserIds = new HashSet(); // userIds without OUTBOX
        Set userIdsWithOutbox = new HashSet();

        try {
            Collection col = super.select(sql2,HashMap.class,null,0,-1);
            if (col!=null && col.size()>0) {
                for (Iterator i=col.iterator();i.hasNext();) {
                    HashMap map = (HashMap)i.next();
                    String userId = (String)map.get("userId");
                    allUserIds.add(userId);
                }
            }
            col = super.select(sql3,HashMap.class,null,0,-1);
            if (col!=null && col.size()>0) {
                for (Iterator i=col.iterator();i.hasNext();) {
                    HashMap map = (HashMap)i.next();
                    String userId = (String)map.get("userId");
                    userIdsWithOutbox.add(userId);
                    allUserIds.remove(userId);
                }
            }
            for (Iterator i = allUserIds.iterator(); i.hasNext(); ) {
                String userId = (String) i.next();
                Object[] objs=new Object[]{
                        UuidGenerator.getInstance().getUuid(),
                        userId,
                        Folder.FOLDER_OUTBOX,
                        Boolean.TRUE,
                        new Integer(0),
                        null};

                super.update(sql1,objs);
            }
        }
        catch(Exception e) {
        }
    }

    // === [ Account ] =========================================================
    public void insertAccount(Account account) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        Pop3Account pop3;
        IntranetAccount intranet;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("INSERT INTO emlAccount(accountId, " +
                    "accountType, userId, name, indicator, deliveryFolderId, " +
                    "filterEnabled, intranetUsername, fromAddress, serverName, serverPort, username, password, " +
                    "leaveMailOnServer, lastCheckDate, checkCount, lastDownloadDate, downloadCount, accountLog, signature) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, account.getAccountId());
            pstmt.setInt(2, account.getAccountType());
            pstmt.setString(3, account.getUserId());
            pstmt.setString(4, account.getName());
            pstmt.setInt(5, account.getIndicator());
            pstmt.setString(6, account.getDeliveryFolderId());
            pstmt.setBoolean(7, account.isFilterEnabled());

            // for Pop3 account
            if (account.getAccountType() == Account.ACCOUNT_TYPE_POP3) {
                pop3 = (Pop3Account) account;
                pstmt.setString(8, "");
                pstmt.setString(9, "");
                pstmt.setString(10, pop3.getServerName());
                pstmt.setInt(11, pop3.getServerPort());
                pstmt.setString(12, pop3.getUsername());
                pstmt.setString(13, pop3.getPassword());
                pstmt.setBoolean(14, pop3.isLeaveMailOnServer());
                if(pop3.getLastCheckDate()!=null) {
                    pstmt.setTimestamp(15, new Timestamp(pop3.getLastCheckDate().getTime()));
                } else {
                    pstmt.setTimestamp(15, null);
                }
                pstmt.setInt(16, pop3.getCheckCount());
                if(pop3.getLastDownloadDate()!=null) {
                    pstmt.setTimestamp(17, new Timestamp(pop3.getLastDownloadDate().getTime()));
                } else {
                    pstmt.setTimestamp(17, null);
                }
                pstmt.setInt(18, pop3.getDownloadCount());
                pstmt.setString(19, pop3.getAccountLog());
                pstmt.setString(20, "");

            } else {
                intranet = (IntranetAccount) account;
                pstmt.setString(8, intranet.getIntranetUsername());
                pstmt.setString(9, intranet.getFromAddress());
                pstmt.setString(10, "");
                pstmt.setInt(11, 110);
                pstmt.setString(12, "");
                pstmt.setString(13, "'");
                pstmt.setBoolean(14, true);
                pstmt.setTimestamp(15, null);
                pstmt.setInt(16, 0);
                pstmt.setTimestamp(17, null);
                pstmt.setInt(18, 0);
                pstmt.setString(19, "");
                pstmt.setString(20, intranet.getSignature());
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void updateAccount(Account account) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        Pop3Account pop3;
        IntranetAccount intranet;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("UPDATE emlAccount SET " +
                    "accountType=?, userId=?, name=?, indicator=?, deliveryFolderId=?, " +
                    "filterEnabled=?, intranetUsername=?, fromAddress=?, serverName=?, serverPort=?, username=?, password=?, " +
                    "leaveMailOnServer=?, lastCheckDate=?, checkCount=?, lastDownloadDate=?, downloadCount=?, accountLog=?, signature=? WHERE accountId=?");
            pstmt.setInt(1, account.getAccountType());
            pstmt.setString(2, account.getUserId());
            pstmt.setString(3, account.getName());
            pstmt.setInt(4, account.getIndicator());
            pstmt.setString(5, account.getDeliveryFolderId());
            pstmt.setBoolean(6, account.isFilterEnabled());

            // for Pop3 account
            if (account.getAccountType() == Account.ACCOUNT_TYPE_POP3) {
                pop3 = (Pop3Account) account;
                pstmt.setString(7, "");
                pstmt.setString(8, "");
                pstmt.setString(9, pop3.getServerName());
                pstmt.setInt(10, pop3.getServerPort());
                pstmt.setString(11, pop3.getUsername());
                pstmt.setString(12, pop3.getPassword());
                pstmt.setBoolean(13, pop3.isLeaveMailOnServer());
                if(pop3.getLastCheckDate()!=null) {
                    pstmt.setTimestamp(14, new Timestamp(pop3.getLastCheckDate().getTime()));
                } else {
                    pstmt.setTimestamp(14, null);
                }
                pstmt.setInt(15, pop3.getCheckCount());
                if(pop3.getLastDownloadDate()!=null) {
                    pstmt.setTimestamp(16, new Timestamp(pop3.getLastDownloadDate().getTime()));
                } else {
                    pstmt.setTimestamp(16, null);
                }
                pstmt.setInt(17, pop3.getDownloadCount());
                pstmt.setString(18, pop3.getAccountLog());
                pstmt.setString(19, "");

            } else {
                intranet = (IntranetAccount) account;
                pstmt.setString(7, intranet.getIntranetUsername());
                pstmt.setString(8, intranet.getFromAddress());
                pstmt.setString(9, "'");
                pstmt.setInt(10, 110);
                pstmt.setString(11, "");
                pstmt.setString(12, "");
                pstmt.setBoolean(13, true);
                pstmt.setTimestamp(14, null);
                pstmt.setInt(15, 0);
                pstmt.setTimestamp(16, null);
                pstmt.setInt(17, 0);
                pstmt.setString(18, "");
                pstmt.setString(19, intranet.getSignature());
            }
            pstmt.setString(20, account.getAccountId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void deleteAccount(String accountId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("DELETE FROM emlAccount WHERE accountId=?");
            pstmt.setString(1, accountId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public Account selectAccount(String accountId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Account account = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT accountType, userId, name, " +
                    "indicator, deliveryFolderId, filterEnabled, intranetUsername, fromAddress, serverName, serverPort, " +
                    "username, password, leaveMailOnServer, lastCheckDate, checkCount, lastDownloadDate, downloadCount, accountLog, signature " +
                    "FROM emlAccount WHERE accountId=?");
            pstmt.setString(1, accountId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // create appropriate account type object
                if (rs.getInt("accountType") == Account.ACCOUNT_TYPE_POP3) {
                    account = new Pop3Account();
                } else {
                    account = new IntranetAccount();
                }

                account.setAccountId(accountId);
                // accountType no need to set. hard-coded in account sub-class
                account.setUserId(rs.getString("userId"));
                account.setName(rs.getString("name"));
                account.setIndicator(rs.getInt("indicator"));
                account.setDeliveryFolderId(rs.getString("deliveryFolderId"));
                account.setFilterEnabled(rs.getBoolean("filterEnabled"));

                if (rs.getInt("accountType") == Account.ACCOUNT_TYPE_POP3) {
                    ((Pop3Account) account).setServerName(rs.getString("serverName"));
                    ((Pop3Account) account).setServerPort(rs.getInt("serverPort"));
                    ((Pop3Account) account).setUsername(rs.getString("username"));
                    ((Pop3Account) account).setPassword(rs.getString("password"));
                    ((Pop3Account) account).setLeaveMailOnServer(rs.getBoolean("leaveMailOnServer"));
                    ((Pop3Account) account).setLastCheckDate(rs.getTimestamp("lastCheckDate"));
                    ((Pop3Account) account).setCheckCount(rs.getInt("checkCount"));
                    ((Pop3Account) account).setLastDownloadDate(rs.getTimestamp("lastDownloadDate"));
                    ((Pop3Account) account).setDownloadCount(rs.getInt("downloadCount"));
                    ((Pop3Account) account).setAccountLog(rs.getString("accountLog"));
                } else {
                    ((IntranetAccount) account).setIntranetUsername(rs.getString("intranetUsername"));
                    ((IntranetAccount) account).setFromAddress(rs.getString("fromAddress"));
                    ((IntranetAccount) account).setSignature(rs.getString("signature"));
                }
                return account;
            } else {
                throw new MessagingDaoException("Account not found");
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    /**
     * Deletes the specified user's intranet account and all database records
     * relating to this user.
     * @param userId specifies the userId
     * @throws MessagingDaoException if an error occurred
     */
    public void deleteIntranetAccount(String userId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        Collection folders;
        Iterator iterator;
        String folderId;

        try {
            conn = getDataSource().getConnection();

            // delete SMTP accounts
            pstmt = conn.prepareStatement("DELETE FROM emlSmtpAccount WHERE userId=?");
            pstmt.setString(1, userId);
            pstmt.executeUpdate();

            // delete messages in every folder for this userId
            folders = selectFolders(userId);
            iterator = folders.iterator();
            while (iterator.hasNext()) {
                folderId = ((Folder) iterator.next()).getFolderId();
                pstmt = conn.prepareStatement("DELETE FROM emlMessage WHERE folderId=?");
                pstmt.setString(1, folderId);
                pstmt.executeUpdate();
            }

            // delete folders
            pstmt = conn.prepareStatement("DELETE FROM emlFolder WHERE userId=?");
            pstmt.setString(1, userId);
            pstmt.executeUpdate();

            // TODO:delete filter
            Collection filters = selectFilters(userId);
            for (Iterator i = filters.iterator(); i.hasNext(); ) {
                String filterId = ((Filter) i.next()).getId();
                log.debug("deleting filter "+filterId);
                deleteFilter(filterId);
            }

            // delete accounts
            pstmt = conn.prepareStatement("DELETE FROM emlAccount WHERE userId=?");
            pstmt.setString(1, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    /**
     * Selects the intranet account for the specified intranet username.
     * @param intranetUsername specifies the prefix for intranet email
     * @return IntranetAccount object for the specified intranet username
     * @throws MessagingDaoException if IntranetAccount for the intranetUsername
     * is not found, or an error occurred while selecting the account
     */
    public IntranetAccount selectIntranetAccount(String intranetUsername) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        IntranetAccount account = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT accountId, userId, name, " +
                    "indicator, deliveryFolderId, filterEnabled, fromAddress " +
                    "FROM emlAccount, security_user " +
                    "WHERE emlAccount.userId=security_user.id AND accountType=? AND intranetUsername=?");
            pstmt.setInt(1, Account.ACCOUNT_TYPE_INTRANET);
            pstmt.setString(2, intranetUsername);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                account = new IntranetAccount();

                account.setAccountId(rs.getString("accountId"));
                account.setUserId(rs.getString("userId"));
                account.setName(rs.getString("name"));
                account.setIndicator(rs.getInt("indicator"));
                account.setDeliveryFolderId(rs.getString("deliveryFolderId"));
                account.setFilterEnabled(rs.getBoolean("filterEnabled"));
                account.setFromAddress(rs.getString("fromAddress"));
                account.setIntranetUsername(intranetUsername);

                return account;
            } else {
                throw new MessagingDaoException("Intranet account not found");
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    /**
     * Selects the intranet account for the specified userId.
     * @param userId specifies the userId
     * @return IntranetAccount object for the specified intranet userId, or null
     * if no IntranetAccount is associated to the userId
     * @throws MessagingDaoException if IntranetAccount for the userId
     * is not found, or an error occurred while selecting the account
     */
    public IntranetAccount selectIntranetAccountByUserId(String userId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        IntranetAccount account = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT accountId, intranetUsername, fromAddress, name, " +
                    "indicator, deliveryFolderId, filterEnabled, signature " +
                    "FROM emlAccount WHERE accountType=? AND userId=?");
            pstmt.setInt(1, Account.ACCOUNT_TYPE_INTRANET);
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                account = new IntranetAccount();

                account.setAccountId(rs.getString("accountId"));
                account.setUserId(userId);
                account.setIntranetUsername(rs.getString("intranetUsername"));
                account.setFromAddress(rs.getString("fromAddress"));
                account.setName(rs.getString("name"));
                account.setIndicator(rs.getInt("indicator"));
                account.setDeliveryFolderId(rs.getString("deliveryFolderId"));
                account.setFilterEnabled(rs.getBoolean("filterEnabled"));
                account.setSignature(rs.getString("signature"));

                return account;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    /**
     * Retrieves all intranet accounts from the module
     * @return Collection of IntranetAccount objects
     * @throws MessagingDaoException
     */
    public Collection selectIntranetAccounts() throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        IntranetAccount intranetAccount = null;
        List intranetAccountList = new ArrayList();

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT accountId, userId, name, " +
                    "indicator, deliveryFolderId, filterEnabled, intranetUsername, fromAddress " +
                    "FROM emlAccount WHERE accountType=? ORDER BY intranetUsername");
            pstmt.setInt(1, Account.ACCOUNT_TYPE_INTRANET);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                intranetAccount = new IntranetAccount();

                intranetAccount.setAccountId(rs.getString("accountId"));
                // accountType no need to set. hard-coded in account sub-class
                intranetAccount.setUserId(rs.getString("userId"));
                intranetAccount.setName(rs.getString("name"));
                intranetAccount.setIndicator(rs.getInt("indicator"));
                intranetAccount.setDeliveryFolderId(rs.getString("deliveryFolderId"));
                intranetAccount.setFilterEnabled(rs.getBoolean("filterEnabled"));
                intranetAccount.setIntranetUsername(rs.getString("intranetUsername"));
                intranetAccount.setFromAddress(rs.getString("fromAddress"));
                intranetAccountList.add(intranetAccount);
            }
            return intranetAccountList;

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public Collection selectAccounts(String userId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Account account = null;
        List accounts = new ArrayList();

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT accountType, accountId, name, " +
                    "indicator, deliveryFolderId, filterEnabled, intranetUsername, fromAddress, signature, serverName, serverPort, " +
                    "username, password, leaveMailOnServer, lastCheckDate, checkCount, lastDownloadDate, downloadCount, accountLog " +
                    "FROM emlAccount WHERE userId=? ORDER BY name");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int accType = rs.getInt("accountType");
                // create appropriate account type object
                if (accType == Account.ACCOUNT_TYPE_POP3) {
                    account = new Pop3Account();
                } else {
                    account = new IntranetAccount();
                }

                account.setAccountId(rs.getString("accountId"));
                // accountType no need to set. hard-coded in account sub-class
                account.setUserId(userId);
                account.setName(rs.getString("name"));
                account.setIndicator(rs.getInt("indicator"));
                account.setDeliveryFolderId(rs.getString("deliveryFolderId"));
                account.setFilterEnabled(rs.getBoolean("filterEnabled"));

                if (accType == Account.ACCOUNT_TYPE_POP3) {
                    ((Pop3Account) account).setServerName(rs.getString("serverName"));
                    ((Pop3Account) account).setServerPort(rs.getInt("serverPort"));
                    ((Pop3Account) account).setUsername(rs.getString("username"));
                    ((Pop3Account) account).setPassword(rs.getString("password"));
                    ((Pop3Account) account).setLeaveMailOnServer(rs.getBoolean("leaveMailOnServer"));
                    ((Pop3Account) account).setLastCheckDate(rs.getTimestamp("lastCheckDate"));
                    ((Pop3Account) account).setCheckCount(rs.getInt("checkCount"));
                    ((Pop3Account) account).setLastDownloadDate(rs.getTimestamp("lastDownloadDate"));
                    ((Pop3Account) account).setDownloadCount(rs.getInt("downloadCount"));
                    ((Pop3Account) account).setAccountLog(rs.getString("accountLog"));
                } else {
                    ((IntranetAccount) account).setIntranetUsername(rs.getString("intranetUsername"));
                    ((IntranetAccount) account).setFromAddress(rs.getString("fromAddress"));
                    ((IntranetAccount) account).setSignature(rs.getString("signature"));
                }
                accounts.add(account);
            }
            return accounts;

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public Collection selectAccounts(String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws MessagingDaoException {
        Collection accounts;
        String sql;
        Object[] args;
        List argsList = new ArrayList();
        try {
            int numOfPop3Acc = selectAccountCount(userId, "accountType", "" + Account.ACCOUNT_TYPE_POP3);
            sql = "SELECT accountId, userId, accountType, name, indicator, deliveryFolderId, filterEnabled, intranetUsername, " +
                    "fromAddress, serverName, serverPort, username, password, leaveMailOnServer, lastCheckDate, checkCount, lastDownloadDate, downloadCount, accountLog, signature " +
                    "FROM emlAccount";

            if (userId != null && !userId.trim().equals("")) {
                sql += " WHERE userId=?";
                argsList.add(userId);
            }
            if (searchCriteria != null && !searchCriteria.trim().equals("") && !searchBy.trim().equals("userId")) {
                if (!searchBy.equalsIgnoreCase("accountType")) {
                    if (sql.toUpperCase().indexOf(" WHERE ") > -1)
                        sql += " AND accountType=?";
                    else
                        sql += " WHERE accountType=?";

                    if (numOfPop3Acc > 0)
                        argsList.add(new Integer(Account.ACCOUNT_TYPE_POP3));
                    else
                        argsList.add(new Integer(Account.ACCOUNT_TYPE_INTRANET));
                } else {
                    if (searchCriteria.equals("" + Account.ACCOUNT_TYPE_INTRANET))
                        numOfPop3Acc = 0;
                }
                argsList.add("%" + searchCriteria + "%");
                if (sql.toUpperCase().indexOf(" WHERE ") > -1)
                    sql += " AND " + searchBy + " LIKE ?";
                else
                    sql += " WHERE " + searchBy + " LIKE ?";
            } else if (searchCriteria == null || searchCriteria.trim().equals("")) {
                if (sql.toUpperCase().indexOf(" WHERE ") > -1)
                    sql += " AND accountType=?";
                else
                    sql += " WHERE accountType=?";

                if (numOfPop3Acc > 0)
                    argsList.add(new Integer(Account.ACCOUNT_TYPE_POP3));
                else
                    argsList.add(new Integer(Account.ACCOUNT_TYPE_INTRANET));
            }
            if (sortBy != null && !sortBy.trim().equals("")) {
                sql += " ORDER BY " + sortBy;
            } else
                sql += " ORDER BY name";

            if (isDescending && (sql.toUpperCase().lastIndexOf("DESC") != (sql.length() - 4))) {
                sql += " DESC";
            }
            args = argsList.toArray();

            if (numOfPop3Acc > 0) {
                accounts = super.select(sql, Pop3Account.class, args, start, numOfRows);
                if (searchCriteria == null || searchCriteria.trim().equals("") || (searchBy != null && !searchBy.equalsIgnoreCase("accountType"))) {
                    int pos = -1;
                    for (int i = 0; i < argsList.size(); i++) {
                        if (argsList.get(i).equals(new Integer(Account.ACCOUNT_TYPE_POP3))) ;
                        pos = i;
                    }
                    if (pos != -1) {
                        argsList.remove(pos);
                        argsList.add(pos, new Integer(Account.ACCOUNT_TYPE_INTRANET));
                    }
                    args = argsList.toArray();
                    if (accounts.size() == 0 || numOfRows == -1) {
                        accounts.addAll(super.select(sql, IntranetAccount.class, args, start - numOfPop3Acc, numOfRows));
                    } else if (accounts.size() < numOfRows) {
                        numOfRows = numOfRows - accounts.size();
                        accounts.addAll(super.select(sql, IntranetAccount.class, args, 0, numOfRows));
                    }
                }
            } else
                accounts = super.select(sql, IntranetAccount.class, args, start, numOfRows);
        } catch (Exception e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
        return new ArrayList(accounts);
    }

    public int selectAccountCount(String userId, String searchBy, String searchCriteria) throws MessagingDaoException {
        try {
            String sql = "SELECT COUNT(accountId) AS total FROM emlAccount";
            List argsList = new ArrayList();

            if (userId != null && !userId.trim().equals("")) {
                sql += " WHERE userId=?";
                argsList.add(userId);
            }
            if (searchCriteria != null && !searchCriteria.trim().equals("")) {
                argsList.add(searchCriteria);
                if (sql.toUpperCase().indexOf(" WHERE ") > -1)
                    sql += " AND " + searchBy + "=?";
                else
                    sql += " WHERE " + searchBy + "=?";
            }
            HashMap count = (HashMap) super.select(sql, HashMap.class, argsList.toArray(), 0, -1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        } catch (Exception e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
    }

    // ---------- [Filter] ----------
    public void insertFilter(Filter filter) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement(
                    "INSERT INTO emlFilter (filterId, filterValue, filterActive, userId, "+
                            "name, filterAction, filterCondition, filterOrder) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            pstmt.setString(1, filter.getFilterId());
            pstmt.setString(2, filter.getFilterValue());
            pstmt.setBoolean(3, filter.isFilterActive());
            pstmt.setString(4, filter.getUserId());
            pstmt.setString(5, filter.getName());
            pstmt.setString(6, filter.getFilterAction());
            pstmt.setString(7, filter.getFilterCondition());
            pstmt.setInt(8, filter.getFilterOrder());

            pstmt.executeUpdate();

            // persist the rules
            for (Iterator i = filter.getRules().iterator(); i.hasNext(); ) {

                try {
                    Filter.Rule rule = (Filter.Rule) i.next();
                    pstmt2 = conn.prepareStatement(
                            "INSERT INTO emlFilterRule (ruleId, criteria, predicate, predicateValue, "+
                                    "filterId) VALUES (?, ?, ?, ?, ?)"
                    );

                    pstmt2.setString(1, rule.getId());
                    pstmt2.setString(2, rule.getCriteria());
                    pstmt2.setString(3, rule.getPredicate());
                    pstmt2.setString(4, rule.getPredicateValue());
                    pstmt2.setString(5, filter.getFilterId());

                    pstmt2.executeUpdate();
                }
                finally {
                    closeConnection(null, pstmt2, null);
                }
            }
        } catch (SQLException e) {
            log.error(e.toString(), e);
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void updateFilter(Filter filter) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("UPDATE emlFilter SET filterValue=?, " +
                    "filterActive=?, userId=?, name=?, filterAction=?, "+
                    "filterCondition=?, filterOrder=? WHERE filterId=?");

            pstmt.setString(1, filter.getFilterValue());
            pstmt.setBoolean(2, filter.isFilterActive());
            pstmt.setString(3, filter.getUserId());
            pstmt.setString(4, filter.getName());
            pstmt.setString(5, filter.getFilterAction());
            pstmt.setString(6, filter.getFilterCondition());
            pstmt.setInt(7, filter.getFilterOrder());
            pstmt.setString(8, filter.getFilterId());

            pstmt.executeUpdate();

            for (Iterator i = filter.getRules().iterator(); i.hasNext(); ) {
                try {
                    Filter.Rule rule = (Filter.Rule) i.next();

                    pstmt2 = conn.prepareStatement("UPDATE emlFilterRule SET criteria = ?, "+
                            "predicate = ?, predicateValue = ? WHERE filterId = ? AND ruleId = ?");

                    pstmt2.setString(1, rule.getCriteria());
                    pstmt2.setString(2, rule.getPredicate());
                    pstmt2.setString(3, rule.getPredicateValue());
                    pstmt2.setString(4, filter.getFilterId());
                    pstmt2.setString(5, rule.getRuleId());

                    pstmt2.executeUpdate();
                }
                finally {
                    closeConnection(null, pstmt2, null);
                }
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void deleteFilter(String filterId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;

        try {
            conn = getDataSource().getConnection();

            // remove all the filter rules
            pstmt2 = conn.prepareStatement("DELETE FROM emlFilterRule WHERE filterId = ?");
            pstmt2.setString(1, filterId);
            pstmt2.executeUpdate();

            // then the filter itself
            pstmt = conn.prepareStatement("DELETE FROM emlFilter WHERE filterId=?");
            pstmt.setString(1, filterId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(null, pstmt2, null);
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
                filter = new Filter(rs.getString("filterId"), rs.getString("userId"),
                        rs.getString("name"), rs.getString("filterValue"),
                        rs.getBoolean("filterActive"), rs.getString("filterAction"),
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

    /**
     * Returns back a collection of{@link Filter} objects (representing the filter) 
     * own by this particular user (identified by userId), does not include 
     * the {@link Filter.Rule} that might be in it.
     * 
     * @param userId
     * @return Collection
     * @throws MessagingDaoException
     */
    public Collection selectFilters(String userId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List filters = new ArrayList();

        try {
            conn = getDataSource().getConnection();

            // find filterId of this particular user
            pstmt = conn.prepareStatement("SELECT filterId as filterId FROM "+
                    "emlFilter WHERE userId=? ORDER BY filterOrder ASC");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            // then grab filter(s) by filterId(s)
            while (rs.next()) {
                String tmpFilterId = rs.getString("filterId");
                Filter filter = selectFilter(tmpFilterId);
                filters.add(filter);
            }
            return filters;

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public int selectMaxFilterOrder(String userId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT max(filterOrder) as maxFilterOrder FROM emlFilter WHERE userId = ?");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("maxFilterOrder");
            }
            return 0;
        }
        catch(SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
        finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public int selectFiltersCount(String userId)throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List filters = new ArrayList();

        try {
            conn = getDataSource().getConnection();

            // find number of filters for this user
            pstmt = conn.prepareStatement("SELECT count(*) as numOfFilters from emlFilter WHERE userId = ?");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                // there is some filter associated with this user
                int numOfFilters = rs.getInt("numOfFilters");
                return numOfFilters;
            }
            else {
                // this user does not have any filters defined
                return 0;
            }
        }
        catch(SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
        finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    /**
     * Find all {@link Filter}'s id which has Action as 
     * {@link com.tms.collab.messaging.model.FilterActionEnum.MOVE_TO_FOLDER} and the folder to move to is
     * actually identified by <code>folderId</code>
     * 
     * @param folderId
     * @return String[]
     * @throws MessagingDaoException
     */
    public String[] findFilterWithFolderId(String folderId) throws MessagingDaoException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List filters = new ArrayList();


        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("select filterId as id from emlFilter where filterAction = ? and filterValue = ?");
            pstmt.setString(1, FilterActionEnum.MOVE_TO_FOLDER.getName());
            pstmt.setString(2, folderId);
            rs = pstmt.executeQuery();

            List filterIds = new ArrayList();
            while(rs.next()) {
                filterIds.add(rs.getString("id"));
            }
            return (String[]) filterIds.toArray(new String[0]);
        }
        catch(SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
        finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    // ---------- Folder ----------
    public void insertFolder(Folder folder) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("INSERT INTO emlFolder(folderId, userId, name, specialFolder, diskUsage, parentId) VALUES(?,?,?,?,?,?)");
            pstmt.setString(1, folder.getFolderId());
            pstmt.setString(2, folder.getUserId());
            pstmt.setString(3, folder.getName());
            pstmt.setBoolean(4, folder.isSpecialFolder());
            pstmt.setLong(5, folder.getDiskUsage());
            pstmt.setString(6, folder.getParentId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void updateFolder(Folder folder) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("UPDATE emlFolder SET userId=?, name=?, specialFolder=?, diskUsage=?, parentId=? WHERE folderId=?");
            pstmt.setString(1, folder.getUserId());
            pstmt.setString(2, folder.getName());
            pstmt.setBoolean(3, folder.isSpecialFolder());
            pstmt.setLong(4, folder.getDiskUsage());
            pstmt.setString(5, folder.getParentId());
            pstmt.setString(6, folder.getFolderId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void deleteFolder(String folderId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();

            // delete messages with this folderId
            pstmt = conn.prepareStatement("DELETE FROM emlMessage WHERE folderId=?");
            pstmt.setString(1, folderId);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("DELETE FROM emlFolder WHERE folderId=?");
            pstmt.setString(1, folderId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public Folder selectFolder(String folderId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Folder folder = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT userId, " +
                    "name, specialFolder, diskUsage, parentId FROM emlFolder WHERE folderId=?");
            pstmt.setString(1, folderId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                folder = new Folder();
                folder.setFolderId(folderId);
                folder.setUserId(rs.getString("userId"));
                folder.setName(rs.getString("name"));
                folder.setSpecialFolder(rs.getBoolean("specialFolder"));
                folder.setDiskUsage(rs.getLong("diskUsage"));
                folder.setParentId(rs.getString("parentId"));

                return folder;
            } else {
                throw new MessagingDaoException("Folder not found");
            }

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public Folder selectSpecialFolder(String userId, String name) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Folder folder = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT folderId, diskUsage " +
                    "FROM emlFolder WHERE specialFolder=? AND userId=? AND name=?");
            pstmt.setBoolean(1, true);
            pstmt.setString(2, userId);
            pstmt.setString(3, name);
            rs = pstmt.executeQuery();

            if (rs!=null&&rs.next()) {
                folder = new Folder();
                folder.setFolderId(rs.getString("folderId"));
                folder.setUserId(userId);
                folder.setName(name);
                folder.setSpecialFolder(true);
                folder.setDiskUsage(rs.getLong("diskUsage"));

                return folder;
            } else {
                throw new MessagingDaoException("Folder not found");
            }
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public Collection selectFolders(String userId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Folder folder = null;
        List folders = new ArrayList();

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT folderId, " +
                    "name, specialFolder, diskUsage, parentId FROM emlFolder WHERE userId=? ORDER BY name");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                folder = new Folder();
                folder.setUserId(userId);
                folder.setFolderId(rs.getString("folderId"));
                folder.setName(rs.getString("name"));
                folder.setSpecialFolder(rs.getBoolean("specialFolder"));
                folder.setDiskUsage(rs.getLong("diskUsage"));
                folder.setParentId(rs.getString("parentId"));
                folders.add(folder);
            }
            return folders;
        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public Collection selectFolders(String userId, int start, int numOfRows, String sortBy, boolean isDescending, String searchBy, String searchCriteria) throws MessagingDaoException {
        Collection folders;
        String sql;
        Object[] args;
        List argsList = new ArrayList();

        try {
            sql = "SELECT folderId, userId, name, specialFolder, diskUsage, parentId FROM emlFolder";

            if (userId != null && !userId.trim().equals("")) {
                sql += " WHERE userId=?";
                argsList.add(userId);
            }
            if (searchCriteria != null && !searchCriteria.trim().equals("") && !searchBy.trim().equals("userId") && !searchBy.trim().equals("groupId")) {
                if (sql.toUpperCase().indexOf(" WHERE ") > -1)
                    sql += " AND " + searchBy + " LIKE ?";
                else
                    sql += " WHERE " + searchBy + " LIKE ?";

                argsList.add("%" + searchCriteria + "%");
            }

            if (sortBy != null && !sortBy.trim().equals("")) {
                sql += " ORDER BY " + sortBy;
            } else
                sql += " ORDER BY name";

            if (isDescending && (sql.toUpperCase().lastIndexOf("DESC") != (sql.length() - 4))) {
                sql += " DESC";
            }
            args = argsList.toArray();
            folders = super.select(sql, Folder.class, args, start, numOfRows);
            return folders;

        } catch (DaoException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
    }

    public int selectFoldersCount(String userId, String searchBy, String searchCriteria) throws DaoException {
        try {
            String sql = "SELECT COUNT(folderId) AS total FROM emlFolder WHERE userId=?";
            List argsList = new ArrayList();
            argsList.add(userId);

            if (searchCriteria != null && !searchCriteria.trim().equals("")) {
                sql += " AND " + searchBy;
                sql += " LIKE ?";
                argsList.add("%" + searchCriteria + "%");
            }
            HashMap count = (HashMap) super.select(sql, HashMap.class, argsList.toArray(), 0, -1).iterator().next();
            return Integer.parseInt(count.get("total").toString());

        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public long selectDiskUsage(String userId) throws MessagingDaoException {
        try {
            String sql = "SELECT SUM(diskUsage) AS diskUsage FROM emlFolder WHERE userId=? GROUP BY userId";
            List argList = new ArrayList();
            argList.add(userId);

            HashMap map = (HashMap) super.select(sql, HashMap.class, argList.toArray(), 0, -1).iterator().next();
            Double d = new Double(map.get("diskUsage").toString());
            return (d.longValue()/1024);
        } catch(Exception e) {
            throw new MessagingDaoException("Error getting user disk quota", e);
        }
    }
    // ---------- Quota ----------
    /**
     * Returns the maximum quota (in KB) set for the specified principals
     * @param principalIdArray
     * @return null if no quota set
     */
    public Long selectMaxPrincipalQuota(String[] principalIdArray) throws MessagingDaoException {
        try {
            if (principalIdArray == null || principalIdArray.length == 0) {
                return null;
            }
            List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT MAX(quota) AS maxQuota FROM emlQuota WHERE principalId IN (");
            for(int i=0; i<principalIdArray.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
                argList.add(principalIdArray[i]);
            }
            sql.append(")");

            HashMap map = (HashMap) super.select(sql.toString(), HashMap.class, argList.toArray(), 0, -1).iterator().next();
            Object maxQuota = map.get("maxQuota");
            if (maxQuota != null) {
                Double d = new Double(maxQuota.toString());
                return new Long(d.longValue());
            }
            else {
                return null;
            }
        } catch(Exception e) {
            throw new MessagingDaoException("Error getting principal max disk quota", e);
        }
    }

    /**
     * @param name
     * @param sort
     * @param desc
     * @param start
     * @param rows
     * @return Collection containing Map of groupName (String) -> quota (Long)
     * @throws MessagingDaoException
     */
    public Collection selectPrincipalQuotaList(String name, String sort, boolean desc, int start, int rows) throws MessagingDaoException {
        try {
            Collection argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT g.id, groupName, quota " +
                    "FROM security_group g INNER JOIN emlQuota q ON g.id=q.principalId ");
            if (name != null) {
                sql.append(" WHERE groupName LIKE ?");
                argList.add("%" + name + "%");
            }
            sql.append("ORDER BY ");
            if (sort != null) {
                sql.append(sort);
            }
            else {
                sql.append("groupName");
            }
            if (desc) {
                sql.append(" DESC");
            }
            Collection results = super.select(sql.toString(), HashMap.class, argList, start, rows);
            return results;
        }
        catch (Exception e) {
            throw new MessagingDaoException("Error retrieving quotas: " + e.getMessage(), e);
        }
    }

    public int selectPrincipalQuotaCount(String name) throws MessagingDaoException {
        Map quotaMap = new SequencedHashMap();
        try {
            Collection argList = new ArrayList();
            StringBuffer sql = new StringBuffer("SELECT COUNT(principalId) AS total " +
                    "FROM security_group g INNER JOIN emlQuota q ON g.id=q.principalId ");
            if (name != null) {
                sql.append(" WHERE groupName LIKE ?");
                argList.add("%" + name + "%");
            }
            Map count = (Map)super.select(sql.toString(), HashMap.class, argList, 0, 1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        }
        catch (Exception e) {
            throw new MessagingDaoException("Error retrieving quota count: " + e.getMessage(), e);
        }
    }

    public int updatePrincipalQuota(String principalId, long quota) throws MessagingDaoException {
        try {
            String sql = "UPDATE emlQuota SET quota=? WHERE principalId=?";
            String sql2 = "INSERT INTO emlQuota (principalId,quota) VALUES (?,?)";
            int count = super.update(sql, new Object[] { new Long(quota), principalId });
            if (count == 0) {
                count = super.update(sql2, new Object[] { principalId, new Long(quota) });
            }
            return count;
        } catch(Exception e) {
            throw new MessagingDaoException("Error updating quota", e);
        }
    }

    public int deletePrincipalQuota(String[] principalIdArray) throws MessagingDaoException {
        try {
            if (principalIdArray == null || principalIdArray.length == 0) {
                return 0;
            }

            List argList = new ArrayList();
            StringBuffer sql = new StringBuffer("DELETE FROM emlQuota WHERE principalId IN (");
            for(int i=0; i<principalIdArray.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
                argList.add(principalIdArray[i]);
            }
            sql.append(")");

            int count = super.update(sql.toString(), argList.toArray());
            return count;
        } catch(Exception e) {
            throw new MessagingDaoException("Error deleting quota", e);
        }
    }
    // ----------[Message] ----------

    public String[] getUserIdBasedOnIntranetUsername(String[] intranetUserName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        Set userIds = new HashSet();

        try {
            StringBuffer buf = new StringBuffer(
                    "SELECT DISTINCT account.userId as userId from emlaccount as account "+
                            "WHERE account.accountType = ? and ");
            for (int a=0; a< intranetUserName.length; a++) {
                if (a == 0) {
                    // first time
                    buf.append("(");
                }
                else {
                    // not the first time
                    buf.append(" OR ");
                }
                buf.append("account.intranetUserName = ? ");
                if (a == (intranetUserName.length - 1)) {
                    // last time
                    buf.append(")");
                }
            }
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement(buf.toString());
            pstmt.setInt(1, Account.ACCOUNT_TYPE_INTRANET);

            for (int a=0; a< intranetUserName.length; a++) {
                pstmt.setString(a+2, intranetUserName[a]);
            }
            rst = pstmt.executeQuery();

            while (rst.next()) {
                String userId = rst.getString("userId");
                userIds.add(userId);
            }
        }
        catch(SQLException e) {
            log.error(e.toString(), e);
        }
        finally {
            closeConnection(conn, pstmt, rst);
        }
        return (String[]) userIds.toArray(new String[0]);
    }

    /**
     * Mark the emlmessage table with column with <code>messageId</code> with 
     * value <code>errorFlag</code>
     * 
     * @see Message.ERROR_FLAG_FAIL
     * @see Message.ERROR_FLAG_NORMAL
     * 
     */
    public void markMessageErrorFlag(String messageId, String errorFlag) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("update emlMessage set errorFlag = ? where messageId = ? ");

            pstmt.setString(1, errorFlag);
            pstmt.setString(2, messageId);

            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            log.error(e.toString(), e);
        }
        finally {
            closeConnection(conn, pstmt, null);
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
                            "msg.subject as subject, " +
                            "msg.body as body, "+
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
                            "and msg.errorFlag <> ? AND msg.errorFlag <> ?"
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
                Date messageDate = rs.getTimestamp("messageDate");
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
                //message.setBody("");
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
            log.error("Error finding messages in outbox: " + e.toString(), e);
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
            pstmt.setString(16, (String) message.getHeaders().get(MESSAGE_ID_HEADER));
            pstmt.setBoolean(17, message.getCopyToSent());
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

    public void updateMessage(Message message) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        List toList, ccList, bccList;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("UPDATE emlMessage SET " +
                    "folderId=?, accountId=?, readFlag=?, fromField=?, toField=?, ccField=?, " +
                    "bccField=?, subject=?, body=?, " +
                    "messageFormat=?, messageDate=?, attachmentCount=?, " +
                    "storageFilename=?, headers=?, messageIdHeader=?,  "+
                    "errorFlag=? "+
                    "WHERE messageId=?");

            pstmt.setString(1, message.getFolderId());
            pstmt.setString(2, message.getAccountId());
            pstmt.setBoolean(3, message.isRead());
            pstmt.setString(4, message.getFrom());

            toList = new ArrayList(message.getToList());
            toList.addAll(message.getToIntranetList());
            pstmt.setString(5, Util.convertRecipientsListToString(toList));

            ccList = new ArrayList(message.getCcList());
            ccList.addAll(message.getCcIntranetList());
            pstmt.setString(6, Util.convertRecipientsListToString(ccList));

            bccList = new ArrayList(message.getBccList());
            bccList.addAll(message.getBccIntranetList());
            pstmt.setString(7, Util.convertRecipientsListToString(bccList));

            pstmt.setString(8, message.getSubject());
            pstmt.setString(9, message.getBody());
            pstmt.setInt(10, message.getMessageFormat());
            pstmt.setTimestamp(11, new Timestamp(message.getDate().getTime()));
            pstmt.setInt(12, message.getAttachmentCount());
            pstmt.setString(13, message.getStorageFilename());
            pstmt.setString(14, Util.convertMessageHeadersToString(message.getHeaders()));
            pstmt.setString(15, (String) message.getHeaders().get(MESSAGE_ID_HEADER));
            pstmt.setString(16, message.getErrorFlag());
            pstmt.setString(17, message.getMessageId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void deleteMessage(String messageId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("DELETE FROM emlMessage WHERE messageId=?");
            pstmt.setString(1, messageId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public Message selectNextMessage(Date date,String folderId) throws MessagingDaoException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT messageId,folderId, accountId, readFlag, fromField, toField, " +
                    "ccField, bccField, subject, " +
                    //"body, " +
                    "messageFormat, messageDate, " +
                    "attachmentCount, storageFilename, headers FROM emlMessage " +
                    "WHERE messageDate > ? AND folderId = ? ORDER BY messageDate");
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
                //message.setBody(rs.getString("body"));
                message.setBody("");
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

    public Message selectPreviousMessage(Date date,String folderId) throws MessagingDaoException
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT messageId,folderId, accountId, readFlag, fromField, toField, " +
                    "ccField, bccField, subject, " +
                    //"body, " +
                    "messageFormat, messageDate, " +
                    "attachmentCount, storageFilename, headers FROM emlMessage " +
                    "WHERE messageDate < ? AND folderId = ? ORDER BY messageDate DESC ");
            pstmt.setTimestamp(1, new Timestamp(date.getTime()));
            pstmt.setString(2,folderId);
            pstmt.setMaxRows(1);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                message = new Message();
                message.setMessageId(rs.getString("messageId"));
                message.setFolderId(rs.getString("folderId"));
                message.setAccountId(rs.getString("accountId"));
                message.setRead(rs.getBoolean("readFlag"));
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
                //message.setBody(rs.getString("body"));
                message.setBody("");
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

    public Message selectMessage(String messageId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT folderId, accountId, readFlag, fromField, toField, " +
                    "ccField, bccField, subject, " +
                    //"body, " +
                    "messageFormat, messageDate, " +
                    "attachmentCount, storageFilename, headers FROM emlMessage " +
                    "WHERE messageId=?");
            pstmt.setString(1, messageId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                message = new Message();
                message.setMessageId(messageId);
                message.setFolderId(rs.getString("folderId"));
                message.setAccountId(rs.getString("accountId"));
                message.setRead(rs.getBoolean("readFlag"));
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
                //message.setBody(rs.getString("body"));
                message.setBody("");
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

    public Collection selectMessages(String folderId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;
        List messages = new ArrayList();

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT messageId, accountId, readFlag, fromField, toField, " +
                    "ccField, bccField, subject, " +
                    //"body, " +
                    "messageFormat, messageDate, " +
                    "attachmentCount, storageFilename, headers FROM emlMessage " +
                    "WHERE folderId=? ORDER BY messageDate DESC");
            pstmt.setString(1, folderId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                message = new Message();
                message.setFolderId(folderId);
                message.setMessageId(rs.getString("messageId"));
                message.setAccountId(rs.getString("accountId"));
                message.setRead(rs.getBoolean("readFlag"));
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
                //message.setBody(rs.getString("body"));
                message.setBody("");
                message.setMessageFormat(rs.getInt("messageFormat"));
                message.setDate(rs.getTimestamp("messageDate"));
                message.setAttachmentCount(rs.getInt("attachmentCount"));
                message.setStorageFilename(rs.getString("storageFilename"));
                message.setHeaders(Util.convertStringToMessageHeaders(rs.getString("headers")));

                messages.add(message);
            }
            return messages;

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    /**
     * Returns a Collection of MESSAGE-ID header values. Used to determine what
     * POP3 email messages has been downloaded so that only new messages are
     * retrieved from an account.
     *
     * @param accountId specifies the POP3 account's ID
     * @return a Collection of MESSAGE-ID header values
     * @throws MessagingDaoException
     */
    public Collection selectMessageIdHeaders(String accountId, String messageIdHeader) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Collection messageIdHeaders = new HashSet();

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT messageIdHeader FROM emlMessage WHERE accountId=? AND messageIdHeader=?");
            pstmt.setString(1, accountId);
            pstmt.setString(2, messageIdHeader);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                messageIdHeaders.add(rs.getString("messageIdHeader"));
            }

            return messageIdHeaders;

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    /**
     * @deprecated don't use this
     * @param folderId
     * @param searchBy
     * @param searchCriteria
     * @return
     * @throws MessagingDaoException
     */
    public int selectMessagesCount(String folderId, String searchBy, String searchCriteria) throws MessagingDaoException {
        HashMap count = null;
        String sql = "SELECT COUNT(messageId) AS total FROM emlMessage WHERE folderId=?";
        List argsList = new ArrayList();
        argsList.add(folderId);

        if (searchCriteria != null && !searchCriteria.trim().equals("")) {
            sql += " AND " + searchBy;
            sql += " LIKE ?";
            argsList.add("%" + searchCriteria + "%");
        }

        try {
            count = (HashMap) super.select(sql, HashMap.class, argsList.toArray(), 0, -1).iterator().next();
            return Integer.parseInt(count.get("total").toString());
        } catch (DaoException e) {
            throw new MessagingDaoException(e);
        }
    }

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
                "mm.ccField, mm.bccField, mm.subject, " +
                //"mm.body, " +
                "mm.messageFormat, mm.messageDate, " +
                "mm.attachmentCount, mm.storageFilename, mm.headers, ma.indicator, " +
                "mm.errorFlag, mm.copyToSent "+
                "FROM emlMessage as mm " +
                "INNER JOIN emlAccount as ma ON mm.accountId = ma.accountId";

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
                "mm.ccField, mm.bccField, mm.subject, " +
                //"mm.body, " +
                "mm.messageFormat, mm.messageDate, " +
                "mm.attachmentCount, mm.storageFilename, mm.headers, ma.indicator, " +
                "mm.errorFlag, mm.copyToSent "+
                "FROM emlMessage as mm " +
                "INNER JOIN emlAccount as ma ON mm.accountId = ma.accountId "+
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
                "FROM emlMessage as mm " +
                /*"INNER JOIN emlAccount as ma ON mm.accountId = ma.accountId "+*/
                "WHERE 1=1 " + query.getStatement();

        try {
            Collection col = super.select(sql, HashMap.class, query.getArray(), 0, -1);
            Map map = (Map) col.iterator().next();
            return Integer.parseInt(map.get("total").toString());

        } catch (DaoException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        }
    }

    // ---------- SmtpAccount ----------
    public void insertSmtpAccount(SmtpAccount smtpAccount) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("INSERT INTO emlSmtpAccount(smtpAccountId, userId, name, serverName, serverPort, anonymousAccess, username, password) VALUES(?,?,?,?,?,?,?,?)");
            pstmt.setString(1, smtpAccount.getSmtpAccountId());
            pstmt.setString(2, smtpAccount.getUserId());
            pstmt.setString(3, smtpAccount.getName());
            pstmt.setString(4, smtpAccount.getServerName());
            pstmt.setInt(5, smtpAccount.getServerPort());
            pstmt.setBoolean(6, smtpAccount.isAnonymousAccess());
            pstmt.setString(7, smtpAccount.getUsername());
            pstmt.setString(8, smtpAccount.getPassword());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void updateSmtpAccount(SmtpAccount smtpAccount) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();

            pstmt = conn.prepareStatement("UPDATE emlSmtpAccount SET userId=?, name=?, serverName=?, serverPort=?, anonymousAccess=?, username=?, password=? WHERE smtpAccountId=?");
            pstmt.setString(1, smtpAccount.getUserId());
            pstmt.setString(2, smtpAccount.getName());
            pstmt.setString(3, smtpAccount.getServerName());
            pstmt.setInt(4, smtpAccount.getServerPort());
            pstmt.setBoolean(5, smtpAccount.isAnonymousAccess());
            pstmt.setString(6, smtpAccount.getUsername());
            pstmt.setString(7, smtpAccount.getPassword());
            pstmt.setString(8, smtpAccount.getSmtpAccountId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public void deleteSmtpAccount(String smtpAccountId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("DELETE FROM emlSmtpAccount WHERE smtpAccountId=?");
            pstmt.setString(1, smtpAccountId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, null);
        }
    }

    public SmtpAccount selectSmtpAccount(String smtpAccountId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SmtpAccount smtpAccount = null;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT userId, name, " +
                    "serverName, serverPort, anonymousAccess, username, password " +
                    "FROM emlSmtpAccount WHERE smtpAccountId=?");
            pstmt.setString(1, smtpAccountId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                smtpAccount = new SmtpAccount();
                smtpAccount.setSmtpAccountId(smtpAccountId);
                smtpAccount.setUserId(rs.getString("userId"));
                smtpAccount.setName(rs.getString("name"));
                smtpAccount.setServerName(rs.getString("serverName"));
                smtpAccount.setServerPort(rs.getInt("serverPort"));
                smtpAccount.setAnonymousAccess(rs.getBoolean("anonymousAccess"));
                smtpAccount.setUsername(rs.getString("username"));
                smtpAccount.setPassword(rs.getString("password"));

                return smtpAccount;
            } else {
                throw new MessagingDaoException("SmtpAccount not found");
            }

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    public Collection selectSmtpAccounts(String userId) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SmtpAccount smtpAccount = null;
        List smtpAccounts = new ArrayList();

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT smtpAccountId, name, " +
                    "serverName, serverPort, anonymousAccess, username, password " +
                    "FROM emlSmtpAccount WHERE userId=? ORDER BY name");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                smtpAccount = new SmtpAccount();
                smtpAccount.setSmtpAccountId(rs.getString("smtpAccountId"));
                smtpAccount.setUserId(userId);
                smtpAccount.setName(rs.getString("name"));
                smtpAccount.setServerName(rs.getString("serverName"));
                smtpAccount.setServerPort(rs.getInt("serverPort"));
                smtpAccount.setAnonymousAccess(rs.getBoolean("anonymousAccess"));
                smtpAccount.setUsername(rs.getString("username"));
                smtpAccount.setPassword(rs.getString("password"));
                smtpAccounts.add(smtpAccount);
            }

            return smtpAccounts;

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }

    // === [ private methods ] =================================================
    /**
     * Close database connection and returns resources to the system. This
     * method should be called in the finally block of a try/catch statement
     * that uses JDBC.
     * @param conn specifies the connection to close
     * @param pstmt specifies the statement to close
     * @param rs specifies the resultset to close, null if not applicable
     */
    protected void closeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // do nothing, just proceed
                log.error("Exception while closing ResultSet", e);
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // do nothing, just proceed
                log.error("Exception while closing PreparedStatement", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // do nothing, just proceed
                log.error("Exception while closing Connection", e);
            }
        }
    }

    protected Collection transformToMessages(Collection defaultDataObjects) throws MessagingDaoException {
        Collection messages = new ArrayList();
        DefaultDataObject obj;
        Message message;
        Iterator itDefaultDataObjects = defaultDataObjects.iterator();

        while (itDefaultDataObjects.hasNext()) {
            message = new Message();
            obj = (DefaultDataObject) itDefaultDataObjects.next();

            message.setMessageId((String) obj.getProperty("messageId"));
            message.setFolderId((String) obj.getProperty("folderId"));
            message.setAccountId((String) obj.getProperty("accountId"));
            message.setRead((new Integer((String) obj.getProperty("readFlag"))).intValue() == 1 ? true : false);
            message.setFrom((String) obj.getProperty("fromField"));
            message.setErrorFlag((String) obj.getProperty("errorFlag"));
            String copyToSent = (String) obj.getProperty("copyToSent");
            message.setCopyToSent((copyToSent != null && new Integer(copyToSent).intValue() == 1) ? true : false);

            try {
                message.setToList(Util.convertStringToInternetRecipientsList((String) obj.getProperty("toField")));
            }
            catch (AddressException e) {
                // ignore;
            }
            try {
                message.setCcList(Util.convertStringToInternetRecipientsList((String) obj.getProperty("ccField")));
            }
            catch (AddressException e) {
                // ignore;
            }
            try {
                message.setBccList(Util.convertStringToInternetRecipientsList((String) obj.getProperty("bccField")));
            }
            catch (AddressException e) {
                // ignore;
            }
            try {
                message.setToIntranetList(Util.convertStringToIntranetRecipientsList((String) obj.getProperty("toField")));
            }
            catch (AddressException e) {
                // ignore;
            }
            try {
                message.setCcIntranetList(Util.convertStringToIntranetRecipientsList((String) obj.getProperty("ccField")));
            }
            catch (AddressException e) {
                // ignore;
            }
            try {
                message.setBccIntranetList(Util.convertStringToIntranetRecipientsList((String) obj.getProperty("bccField")));
            }
            catch (AddressException e) {
                // ignore;
            }
            message.setSubject((String) obj.getProperty("subject"));
            message.setBody((String) obj.getProperty("body"));
            message.setMessageFormat(((Number) obj.getProperty("messageFormat")).intValue());
            message.setDate((Date) obj.getProperty("messageDate"));
            message.setAttachmentCount(((Number) obj.getProperty("attachmentCount")).intValue());
            message.setStorageFilename((String) obj.getProperty("storageFilename"));
            try {
                message.setHeaders(Util.convertStringToMessageHeaders((String) obj.getProperty("headers")));
            } catch (IOException e) {
                throw new MessagingDaoException(e);
            }
            if (obj.getProperty("indicator") != null)
                message.setIndicator(((Number) obj.getProperty("indicator")).intValue());
            messages.add(message);
        }
        return messages;
    }

    public static String getSort(String sort, boolean descending)
    {
        String strSort = "";
        if(sort != null)
        {
            strSort += " ORDER BY " + sort;
            if(descending)
                strSort += " DESC";
        }
        return strSort;
    }

    /**
     * Returns a Collection of the most recently Message objects in a specifiec folder name.
     * This method is used for the autocomplete feature.
     * @param userId
     * @param max Number of messages to retrieve
     * @return Only the folderId, messageId, fromField, toField, ccField and bccField fields are populated.
     * @throws MessagingDaoException
     */
    public Collection selectRecentMessages(String folderName, String userId, int max) throws MessagingDaoException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;
        List messages = new ArrayList();
        int count = 0;

        try {
            conn = getDataSource().getConnection();
            pstmt = conn.prepareStatement("SELECT f.folderId, messageId, fromField, toField, ccField, bccField " +
                    "FROM emlFolder f " +
                    "INNER JOIN emlMessage m " +
                    "ON f.folderId=m.folderId " +
                    "WHERE f.name=? AND f.userId=? " +
                    "ORDER BY messageDate DESC");
            pstmt.setString(1, folderName);
            pstmt.setString(2, userId);
            pstmt.setMaxRows(max);
            rs = pstmt.executeQuery();

            while (rs.next() && count < max) {
                count++;
                message = new Message();
                message.setFolderId(rs.getString("folderId"));
                message.setMessageId(rs.getString("messageId"));

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

                messages.add(message);
            }
            return messages;

        } catch (SQLException e) {
            throw new MessagingDaoException(e.getMessage(), e);
        } finally {
            closeConnection(conn, pstmt, rs);
        }
    }
}
