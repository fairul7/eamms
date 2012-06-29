/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;

import kacang.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class ClaimFormItemCategoryDao extends DataSourceDao {
    public static final String TABLENAME = "claim_form_item_category";

    public void init() {
        // add column for category table
        try {
            super.update("ALTER TABLE " + TABLENAME +
                " ADD type VARCHAR(10) DEFAULT 'default'", null);
        } catch (Exception e) {
        }

        try {
            super.update("CREATE TABLE claim_form_item_category_dependency (" +
                "categoryId VARCHAR(35)," + "departmentCode VARCHAR(10) )", null);

            super.update("CREATE TABLE " + TABLENAME + " (" +
                "id VARCHAR(35)," + "code VARCHAR(255)," +
                "name VARCHAR(255)," + "description VARCHAR(255)," +
                "userEdit VARCHAR(35)," + "timeEdit DATETIME, " +
                "state CHAR(3)," + "status CHAR(3)," +
                "type VARCHAR(10) DEFAULT 'default' )", null);
        } catch (Exception e) {
        }
    }

    public void insertRecord(ClaimFormItemCategory obj)
        throws DaoException {
        super.update("INSERT INTO " + TABLENAME + " (" +
            " id, code, name, description, userEdit, timeEdit, state, status, type )" +
            " VALUES  (" +
            " #id#, #code#, #name#, #description#, #userEdit#, #timeEdit#, #state#, #status#, #type# )",
            obj);
    }

    public void updateRecord(ClaimFormItemCategory obj)
        throws DaoException {
        super.update("UPDATE " + TABLENAME + " SET " + " code = #code# , " +
            " name = #name# , " + " description = #description# , " +
            " userEdit = #userEdit# , " + " timeEdit = #timeEdit# , " +
            " state = #state# , " + " status = #status#,  " +
            " type = #type# " + " WHERE id = #id# ", obj);
    }

    public Collection selectRecords(String field1, String value1, int start,
        int number, String sort, boolean desc) throws DaoException {
        String s = "" + ((sort == null) ? "" : (" ORDER BY " + sort)) + " " +
            (desc ? "DESC" : "");

        Collection col = super.select(" SELECT " + "  id , " + "  code, " +
                " name  , " + " description , " + " userEdit, " +
                " timeEdit, " + " state, " + " status, " + "type " + " FROM " +
                TABLENAME + " WHERE " + field1 + " = ? AND type='default'  " +
                s, ClaimFormItemCategory.class, new Object[] { value1 }, start,
                number);

        return col;
    }


     public Collection selectRecordsIgnoreDefault(String field1, String value1, int start,
        int number, String sort, boolean desc) throws DaoException {
        String s = "" + ((sort == null) ? "" : (" ORDER BY " + sort)) + " " +
            (desc ? "DESC" : "");

        Collection col = super.select(" SELECT " + "  id , " + "  code, " +
                " name  , " + " description , " + " userEdit, " +
                " timeEdit, " + " state, " + " status, " + "type " + " FROM " +
                TABLENAME + " WHERE " + field1 + " = ? AND type='default' AND id!='default' AND id!='travel-mileage' AND id!='travel-toll' AND id!='travel-parking' " +
                s, ClaimFormItemCategory.class, new Object[] { value1 }, start,
                number);

        return col;
    }


    public Collection selectRecords(String keyword, String field1, String value1, String type,
        int start, int number, String sort, boolean desc)
        throws DaoException {
        String s = "" + ((sort == null) ? "" : (" ORDER BY " + sort)) + " " +
            (desc ? "DESC" : "");
        type = ((type == null) || type.equals("")) ? "%" : type;

        Collection col = null;

        if(keyword != null && !("".equals(keyword))){

        keyword = " '%"+keyword+"%' ";
        col =super.select(" SELECT " + "  id , " + "  code, " +
                            " name  , " + " description , " + " userEdit, " +
                            " timeEdit, " + " state, " + " status, " + "type " + " FROM " +
                            TABLENAME + " WHERE id!='default' AND id!='travel-mileage' AND id!='travel-toll' AND id!='travel-parking' AND " + field1 + " = ? AND type=? AND name LIKE "+ keyword+" " + s,
                            ClaimFormItemCategory.class, new Object[] { value1, type },
                            start, number);



        }

        else
        col =super.select(" SELECT " + "  id , " + "  code, " +
                " name  , " + " description , " + " userEdit, " +
                " timeEdit, " + " state, " + " status, " + "type " + " FROM " +
                TABLENAME + " WHERE id!='default' AND id!='travel-mileage' AND id!='travel-toll' AND id!='travel-parking' AND " + field1 + " = ? AND type=? " + s,
                ClaimFormItemCategory.class, new Object[] { value1, type },
                start, number);



        return col;
    }

    public ClaimFormItemCategory selectRecord(String id)
        throws DaoException {
        Collection col = super.select(" SELECT " + " id , " + " code , " +
                " name  , " + " description , " + " userEdit , " +
                " timeEdit , " + " state , " + " status, " + "type " +
                " FROM " + TABLENAME + " WHERE id = ? ",
                ClaimFormItemCategory.class, new Object[] { id }, 0, 1);

        ClaimFormItemCategory obj = null;

        if (col.size() > 0) {
            Iterator iterator = col.iterator();
            obj = (ClaimFormItemCategory) iterator.next();
        }

        return (obj);
    }

    public void deleteRecord(String claimStandardTypeID)
        throws DaoException {
        Log.getLog(this.getClass()).debug("Deleting......." +
            claimStandardTypeID);
        super.update("DELETE FROM " + TABLENAME + " WHERE id = ? ",
            new String[] { claimStandardTypeID });
    }

    public int count(String status) throws DaoException {
        Collection list = super.select(
                "SELECT COUNT(*) AS total FROM claim_form_item_category WHERE status = '" +
                status + "' ", HashMap.class, null, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int count(String keyword,String status, String type) throws DaoException {
        type = ((type == null) || type.equals("")) ? "%" : type;



        Collection list = null;

        if(keyword !=null && !("".equals(keyword))){

        keyword ="'%"+keyword+"%'";
        list =super.select(
                        "SELECT COUNT(*) AS total FROM claim_form_item_category WHERE id!='default' AND id!='travel-mileage' AND id!='travel-toll' AND id!='travel-parking' AND status = '" +
                        status + "' AND type='" + type + "' AND "+keyword, HashMap.class, null, 0, 1);


        }
        else{
        list =super.select(
                "SELECT COUNT(*) AS total FROM claim_form_item_category WHERE id!='default' AND id!='travel-mileage' AND id!='travel-toll' AND id!='travel-parking' AND status = '" +
                status + "' AND type='" + type + "' ", HashMap.class, null, 0, 1);

       }
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public Collection query(String name, String sort, boolean desc, int start,
        int rows) throws DaoException {
        String condition = (name != null) ? ("%" + name + "%") : "%";
        String orderBy = (sort != null) ? sort : "name";

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { condition };

        return super.select(
            "SELECT id, code, name, description, userEdit, timeEdit, state, status, type FROM claim_form_item_category WHERE name LIKE ? ORDER BY " +
            orderBy, ClaimFormItemCategory.class, args, start, rows);
    }

    public Collection query(String[] conditions, String s)
        throws DaoException {
        String sql = "SELECT id, code, name, description, userEdit, timeEdit, state, status, type FROM claim_form_item_category ";
        String condition = "WHERE ";
        String orderBy = ((s == null) || s.equals("")) ? "" : (" ORDER BY " +
            s);

        if ((conditions != null) && (conditions.length > 0)) {
            for (int i = 0; i < conditions.length; i++) {
                if (i > 0) {
                    condition += (" AND " + conditions[i]);
                } else {
                    condition += conditions[i];
                }
            }
        }

        sql = sql + condition + orderBy;

        return super.select(sql, ClaimFormItemCategory.class, null, 0, -1);
    }

    public Collection countTotalType() throws DaoException {
        int dbl = 0;
        String sql = "SELECT distinct(id),typeName,accountcode from claim_form_type c";

        //Collection col = super.select(sql,HashMap.class,new String[] {categoryId,userId},0,-1);
        Collection col = super.select(sql, ClaimTypeObject.class, null, 0, -1);

        return col;
    }

    public void insertDependencies(String categoryId, String departmentCode)
        throws DaoException {
        super.update("INSERT INTO claim_form_item_category_dependency (" +
            "categoryId,departmentCode) VALUES ('" + categoryId + "','" +
            departmentCode + "')", null);
    }

    public void deleteDependencies(String categoryId, String departmentCode)
        throws DaoException {
        super.update("DELETE FROM claim_form_item_category_dependency WHERE " +
            "categoryId='" + categoryId + "' AND departmentCode='" +
            departmentCode + "'", null);
    }

    public Collection selectDependencies(String categoryId)
        throws DaoException {
        String sql =
            "SELECT departmentCode FROM claim_form_item_category_dependency WHERE categoryId='" +
            categoryId + "'";

        return super.select(sql, HashMap.class, null, 0, -1);
    }

    public boolean dependencyExisted(String categoryId, String departmentCode)
        throws DaoException {
        boolean bRet = false;
        String sql =
            "SELECT COUNT(*) AS total FROM claim_form_item_category_dependency WHERE " +
            "categoryId='" + categoryId + "' AND departmentCode='" +
            departmentCode + "'";
        Collection col = super.select(sql, HashMap.class, null, 0, -1);

        if ((col != null) && (col.size() > 0)) {
            HashMap map = (HashMap) col.iterator().next();
            String total = map.get("total").toString();

            try {
                int iTotal = Integer.parseInt(total);

                if (iTotal > 0) {
                    bRet = true;
                }
            } catch (Exception e) {
            }
        }

        return bRet;
    }
}
