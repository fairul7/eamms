package com.tms.collab.messaging.model;

import java.sql.*;
import java.util.*;

public class MessagingDaoSybase extends MessagingDaoMsSql {
	public void init() {
		try {
			super.init();
		} catch(Exception e) {
		}
		
		/* Indexes */
		try {
			super.update("CREATE INDEX accountType ON emlAccount(accountType)", null);
			super.update("CREATE INDEX userId ON emlAccount(userId)", null);
			super.update("CREATE INDEX userId ON emlFilter(userId)", null);
			super.update("CREATE INDEX filterId ON emlFilterRule(filterId)", null);
			super.update("CREATE INDEX userId ON emlFolder(userId)", null);
			super.update("CREATE INDEX folderId ON emlMessage(folderId)", null);
			super.update("CREATE INDEX accountId ON emlMessage(accountId)", null);
			super.update("CREATE INDEX messageDate ON emlMessage(messageDate)", null);
			super.update("CREATE INDEX attachmentCount ON emlMessage(attachmentCount)", null);
			super.update("CREATE INDEX userId ON emlSmtpAccount(userId)", null);
		} catch(Exception e) {
		}
	}
	
	// the column in Sybase is "userId" and not "userID"
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
					
					argsList.add("%" + searchCriteria + "%");
					if (sql.toUpperCase().indexOf(" WHERE ") > -1)
						sql += " AND " + searchBy + " LIKE ?";
					else
						sql += " WHERE " + searchBy + " LIKE ?";
                } else {
                    if (searchCriteria.equals("" + Account.ACCOUNT_TYPE_INTRANET))
                        numOfPop3Acc = 0;
					
					argsList.add(new Integer(searchCriteria));
                    if (sql.toUpperCase().indexOf(" WHERE ") > -1)
                        sql += " AND accountType=?";
                    else
                        sql += " WHERE accountType=?";
                }
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
				if (searchBy.equalsIgnoreCase("accountType")) {
					argsList.add(new Integer(searchCriteria));
				} else {
                	argsList.add(searchCriteria);
				}
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
}
