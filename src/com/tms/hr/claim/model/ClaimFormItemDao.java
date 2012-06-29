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
import java.util.Date;


public class ClaimFormItemDao extends DataSourceDao {
    public static final String TABLENAME = "claim_form_item";

    // init
    public void init() {
        try {
            super.update("ALTER TABLE " + TABLENAME +
                " ADD travelFrom VARCHAR(255)", null);
            super.update("ALTER TABLE " + TABLENAME +
                " ADD travelTo VARCHAR(255)", null);
        } catch (Exception e) {
        }

        try {
            super.update("CREATE TABLE " + TABLENAME + " (" +
                "id VARCHAR(35), " + "formId VARCHAR(35), " +
                "categoryId VARCHAR(35), " + "standardTypeId VARCHAR(35), " +
                "projectId VARCHAR(35), " + "timeFrom DATETIME, " +
                "timeTo DATETIME, " + "timeFinancial DATETIME, " +
                "currency CHAR(3), " + "amount DECIMAL(12,4), " +
                "qty DECIMAL(12,4), " + "unitPrice DECIMAL(12,4), " +
                "uom VARCHAR(255), " + "description VARCHAR(255), " +
                "remarks VARCHAR(255), " + "rejectReason VARCHAR(255), " +
                "state CHAR(3), " + "status CHAR(3), " +
                "travelTo VARCHAR(255), " + "travelFrom VARCHAR(255) )", null);
        } catch (Exception e) {
        }
    }

    public void insertRecord(ClaimFormItem obj) throws DaoException {
        try {
            Log.getLog(this.getClass()).debug(" inseeeeeeeerrrrrrrrrrrrrr ");
            super.update("INSERT INTO " + TABLENAME +
                " (id, formId, categoryId , standardTypeId, projectId, timeFrom, timeTo, timeFinancial, currency, amount, qty, unitPrice, uom, description, remarks, rejectReason, state, status" +
                " ,travelTo,travelFrom)" + " VALUES " +
                " (#id#, #formId#, #categoryId#, #standardTypeId#, #projectId#, #timeFrom#, #timeTo#, #timeFinancial#, #currency#, #amount#, #qty#, #unitPrice#, #uom#, #description#, #remarks#, #rejectReason#, #state#, #status#" +
                ",#travelTo#,#travelFrom# )", obj);
            Log.getLog(this.getClass()).debug(" eeeeeeeddddnnnnnnnnnnnnnnn");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DaoException("error!!!!" + ex.getMessage());
        }
    }

    //check for same date ,diff name
    public boolean checkSameDayDiffName(Date thatDay, String remarks, String categoryId) throws DaoException{

        Collection result=null;
        result =super.select("select * from claim_form_item where timeFrom = ? AND remarks =? AND categoryId = ? ",ClaimFormItem.class, new Object[]{thatDay, remarks,categoryId}, 0,-1);

        if(result.size() > 0)
          return true;

       return false;
    }

    

    public boolean checkSameDayDiffNameOther(Date thatDay, String remarks, String categoryId, String id) throws DaoException{

        Collection result=null;
        result =super.select("select * from claim_form_item where timeFrom = ? AND remarks =? AND categoryId = ? AND ID != ? ",ClaimFormItem.class, new Object[]{thatDay, remarks,categoryId, id}, 0,-1);

        if(result.size() > 0)
          return true;

       return false;
    }


    
    public boolean checkSameDayDiffName2(Date thatDay, String remarks) throws DaoException{

        Collection result=null;
        result =super.select("select * from claim_form_item where timeFrom = ? AND remarks =? ",ClaimFormItem.class, new Object[]{thatDay, remarks}, 0,-1);

        if(result.size() > 0)
          return true;

       return false;
    }
    

    public void updateRecord(ClaimFormItem obj) throws DaoException {
        super.update("UPDATE " + TABLENAME + " SET " + " formId= #formId# , " +
            " categoryId= #categoryId# , " +
            " standardTypeId= #standardTypeId# , " +
            " projectId= #projectId# , " + " timeFrom = #timeFrom# , " +
            " timeTo = #timeTo# , " + " timeFinancial = #timeFinancial# , " +
            " currency = #currency# , " + " amount = #amount# , " +
            " qty= #qty# , " + " unitPrice = #unitPrice# , " +
            " uom = #uom# , " + " description = #description# , " +
            " remarks = #remarks# , " + " rejectReason = #rejectReason# , " +
            " state = #state# , " + " status = #status#  " +
            ",travelTo=#travelTo#" + ",travelFrom=#travelFrom# " +
            " WHERE id = #id# ", obj);
    }

    public Collection selectRecords(String field1, String value1, int start,
                                    int number) throws DaoException {
        /// super.select
        //// starting row and max number of records.. 0,1
        //// -1 if grep everything
        Collection col = super.select(" SELECT " + "  id , " + "  formId, " +
                "  categoryId, " + "  standardTypeId, " + "  projectId, " +
                " timeFrom, " + " timeTo, " + " timeFinancial, " +
                " currency, " + " amount, " + " qty, " + " unitPrice, " +
                " uom, " + " description, " + " remarks, " + " rejectReason, " +
                " state, " + " status " + ",travelTo,travelFrom " + " FROM " +
                TABLENAME + " WHERE " + field1 + " = ? ", ClaimFormItem.class,
                new Object[] { value1 }, start, number);

        return col;
    }

    public ClaimFormItem selectRecord(String id) throws DaoException {
        Collection col = super.select(" SELECT " + "  id , " + "  formId, " +
                "  categoryId, " + "  standardTypeId, " + "  projectId, " +
                " timeFrom, " + " timeTo, " + " timeFinancial, " +
                " currency, " + " amount, " + " qty, " + " unitPrice, " +
                " uom, " + " description, " + " remarks, " + " rejectReason, " +
                " state, " + " status " + ",travelTo,travelFrom " + " FROM " +
                TABLENAME + " WHERE id = ? ", ClaimFormItem.class,
                new Object[] { id }, 0, 1);

        ClaimFormItem obj = null;

        if (col.size() > 0) {
            Iterator iterator = col.iterator();
            obj = (ClaimFormItem) iterator.next();
        }

        return (obj);
    }

    public void deleteRecord(String id) throws DaoException {
        Log.getLog(this.getClass()).debug("Deleting......." + id);
        super.update("DELETE FROM " + TABLENAME + " WHERE id = ? ",
            new String[] { id });
    }

    public int count(String status) throws DaoException {
        Collection list = super.select(
                "SELECT COUNT(*) AS total FROM claim_form_item WHERE status = '" +
                status + "' ", HashMap.class, null, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int count(String field, String value) throws DaoException {
        Collection list = super.select(
                "SELECT COUNT(*) AS total FROM claim_form_item WHERE " + field +
                "= '" + value + "' ", HashMap.class, null, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public Collection query(String formId, String sort, boolean desc,
                            int start, int rows) throws DaoException {
        //String condition = (name != null) ? "%" + name + "%" : "%";
        String orderBy = (sort != null) ? sort : "id";

        if (desc) {
            orderBy += " DESC ";
        }

        Object[] args = { formId };

        return super.select(
            "SELECT id, formId, categoryId, standardTypeId, projectId, timeFrom, timeTo, timeFinancial, currency, amount, qty, unitPrice, uom, description, remarks, rejectReason, state, status,travelTo,travelFrom FROM claim_form_item WHERE formId = ? ORDER BY " +
            orderBy, ClaimFormItem.class, args, start, rows);
    }

    public Collection query(String keyword, String[] conditions, String sort, boolean desc,
                            int start, int rows) throws DaoException {
        String sqlStmt = "SELECT claim_form_item.id as id, formId, categoryId, claim_form_type.typeName as standardTypeId, projectId, timeFrom, timeTo, timeFinancial, currency, amount, qty, unitPrice, uom, description, remarks, rejectReason, state, status,travelTo,travelFrom FROM claim_form_item left join claim_form_type on claim_form_item.standardTypeId= claim_form_type.id";
        //String sqlStmt = "SELECT id, formId, categoryId, standardTypeId, projectId, timeFrom, timeTo, timeFinancial, currency, amount, qty, unitPrice, uom, description, remarks, rejectReason, state, status,travelTo,travelFrom FROM claim_form_item ";

        if (conditions.length > 0) {
            sqlStmt += " WHERE ";
        }

        for (int cnt1 = 0; cnt1 < conditions.length; cnt1++) {
            if (cnt1 > 0) {
                sqlStmt += " AND ";
            }

            sqlStmt += (" " + conditions[cnt1] + " ");
        }

        if(keyword !=null && !("".equals(keyword))){

            keyword = "'%"+keyword+"%'";
            sqlStmt +=" AND remarks LIKE "+keyword+" ";
        }


        if (sort != null) {
            sqlStmt += (" ORDER BY " + sort + " ");
             
            if(desc)
            sqlStmt += " DESC";
        
        }

        return super.select(sqlStmt, ClaimFormItem.class, null, start, rows);
    }

    public int count(String keyword,String[] conditions) throws DaoException {
        String sqlStmt = "SELECT COUNT(*) AS total FROM claim_form_item left join claim_form_type on claim_form_item.standardTypeId= claim_form_type.id ";

        if (conditions.length > 0) {
            sqlStmt += " WHERE ";
        }

        for (int cnt1 = 0; cnt1 < conditions.length; cnt1++) {
            if (cnt1 > 0) {
                sqlStmt += " AND ";
            }

            sqlStmt += (" " + conditions[cnt1] + " ");
        }

        if(keyword !=null && !("".equals(keyword))){

            keyword = "'%"+keyword+"%'";

            sqlStmt += " AND remarks LIKE "+keyword+" ";
        }

        Collection list = super.select(sqlStmt, HashMap.class, null, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    // add in for reporting purposes
    public double selectTotalByMonthAndCategory(String userId,
                                                String categoryId, String type, String monthStr)
        throws DaoException {
        double dbl = 0.0;
        String sql =
            "SELECT SUM(t.amount) as total FROM claim_form_item t LEFT JOIN claim_form_index i ON " +
            "t.formId=i.id WHERE t.categoryId=?   AND standardTypeId=? " +
            "AND i.userOwner=? " + "AND i.claimMonth like '" + monthStr +
            "%' " + "AND i.state='clo'";

        //Collection col = super.select(sql,HashMap.class,new String[] {categoryId,userId},0,-1);
        Collection col = super.select(sql, HashMap.class,
                new String[] { categoryId, type, userId }, 0, -1);
        HashMap map = (HashMap) col.iterator().next();

        try {
            String s = map.get("total").toString();

            if (s != null) {
                dbl = Double.parseDouble(s);
            }
        } catch (Exception e) {
        }

        return dbl;
    }

    public double selectTotalByMonthAndCategory(String userId,
                                                String categoryId, String monthStr) throws DaoException {
        double dbl = 0.0;
        String sql =
            "SELECT SUM(t.amount) as total FROM claim_form_item t LEFT JOIN claim_form_index i ON " +
            "t.formId=i.id WHERE t.categoryId=?  " + "AND i.userOwner=? " +
            "AND i.claimMonth like '" + monthStr + "%' " + "AND i.state='clo'";

        //Collection col = super.select(sql,HashMap.class,new String[] {categoryId,userId},0,-1);
        Collection col = super.select(sql, HashMap.class,
                new String[] { categoryId, userId }, 0, -1);
        HashMap map = (HashMap) col.iterator().next();

        try {
            String s = map.get("total").toString();

            if (s != null) {
                dbl = Double.parseDouble(s);
            }
        } catch (Exception e) {
        }

        return dbl;
    }

    public ClaimFormItem retriveDraftItem(String id) throws DaoException{

        Collection col = super.select("SELECT * from claim_form_item WHERE id=?", ClaimFormItem.class, new Object[]{id}, 0,1);

         if (col.size() > 0) {
            Iterator iterator = col.iterator();
            ClaimFormItem obj = (ClaimFormItem) iterator.next();
            return obj;
        }
        else
         return null;

    }

}
