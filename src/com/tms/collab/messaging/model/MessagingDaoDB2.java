package com.tms.collab.messaging.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DefaultDataObject;

public class MessagingDaoDB2 extends MessagingDao{

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

                argsList.add(searchCriteria);
                if (sql.toUpperCase().indexOf(" WHERE ") > -1)
                    sql += " AND " + searchBy + " = ?";
                else
                    sql += " WHERE " + searchBy + " = ?";
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

    public static String getSort(String sort, boolean descending)
    {
        String strSort = "";
        if(sort != null)
        {
            strSort += " ORDER BY cast(" + sort + " AS varchar(255))";
            if(descending)
                strSort += " DESC";
        }
        return strSort;
    }
}
