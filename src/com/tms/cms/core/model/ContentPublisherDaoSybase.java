package com.tms.cms.core.model;

import kacang.model.*;
import java.util.*;

public class ContentPublisherDaoSybase extends ContentPublisherDaoMsSql {
	// note: selectCountByPermission is the same as the one in ContentPublisherDao except for "[role]"
	// changed: to INNER JOIN
    public int selectCountByPermission(String[] principalIds, String permissionId, String[] keys, String[] classes, Date startDate, Date endDate, String name, String parentId, Boolean archived) throws DaoException {
        try {
            Collection paramList = new ArrayList();
            StringBuffer sql = new StringBuffer(
                    "SELECT COUNT(DISTINCT s.id) AS total " +
                    "FROM " + getTableName() + " p " +
                    "INNER JOIN " + getManagerTablePrefix() + "_status s ON s.id = p.id " +
                    "INNER JOIN " + getManagerTablePrefix() + "_role_principal rp1 ON rp1.objectId = s.aclId " +
                    "INNER JOIN " + getManagerTablePrefix() + "_role_permission rp2 ON rp1.[role] = rp2.[role] " +
                    "WHERE 1=1");

            if (keys != null && keys.length > 0) {
                sql.append(" AND p.id IN (");
                for (int i=0; i<keys.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(keys));
            }

            if (classes != null && classes.length > 0) {
                sql.append(" AND className IN (");
                for (int i=0; i<classes.length; i++) {
                    if (i > 0)
                        sql.append(",");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(classes));
            }

            if (name != null) {
                sql.append(" AND name LIKE ?");
                paramList.add("%" + name + "%");
            }

            if (principalIds != null && principalIds.length > 0) {
                sql.append(" AND principalId IN (");
                for (int i=0; i<principalIds.length; i++) {
                    if (i > 0)
                        sql.append(", ");
                    sql.append("?");
                }
                sql.append(")");
                paramList.addAll(Arrays.asList(principalIds));
            }

            if (permissionId != null) {
                sql.append(" AND permissionId=?");
                paramList.add(permissionId);
            }

            if (parentId != null) {
                sql.append(" AND parentId=?");
                paramList.add(parentId);
            }

            if (archived != null) {
                sql.append(" AND archived=?");
                paramList.add(archived);
            }

            if(startDate!=null) {
                // only startDate specified
                sql.append(" AND date>=?");
                paramList.add(startDate);

            }

            if(endDate!=null) {
                // only endDate specified
                sql.append(" AND date<=?");
                paramList.add(endDate);
            }

            Collection results = super.select(sql.toString(), HashMap.class, paramList.toArray(), 0, 1);
            if (results.size() == 0) {
                return 0;
            }
            else {
                Map resultMap = (Map)results.iterator().next();
                int total = ((Number)resultMap.get("total")).intValue();
                return total;
            }
        }
        catch(Exception e) {
            throw new DaoException(e.toString());
        }
    }
}
