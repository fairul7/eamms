/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

import kacang.services.security.User;
import kacang.util.Log;

import java.util.*;


public class ClaimFormIndexDao extends DataSourceDao {
    public static final String TABLENAME = "claim_form_index";

    public void init() throws DaoException{

        super.update("ALTER TABLE "+TABLENAME+" ADD userApprover1Date datetime, ADD userApprover2Date datetime",null);


    }


    public void insertRecord(ClaimFormIndex obj) throws DaoException {
        super.update("INSERT INTO " + TABLENAME +
            " (id, timeEdit, userOriginator, userOwner, userApprover1, userApprover2, userApprover3, userApprover4, userAssessor, currency, amount, approvalLevelRequired, approvalLevelGranted, remarks, rejectReason, info , state, status )" +
            " VALUES " +
            " (#id#, #timeEdit#, #userOriginator#, #userOwner#, #userApprover1#, #userApprover2#, #userApprover3#, #userApprover4#, #userAssessor#, #currency#, #amount#, #approvalLevelRequired#, #approvalLevelGranted#, #remarks#, #rejectReason#, #info#, #state#, #status# )",
            obj);
    }

    public void updateRecord(ClaimFormIndex obj) throws DaoException {
        super.update("UPDATE " + TABLENAME + " SET " +
            " timeEdit = #timeEdit# , " +
            " userOriginator = #userOriginator# , " +
            " userOwner = #userOwner# , " +
            " userApprover1 = #userApprover1# , " +
            " userApprover2 = #userApprover2# , " +
            " userApprover3 = #userApprover3# , " +
            " userApprover4 = #userApprover4# , " +
            " userAssessor = #userAssessor# , " + " currency = #currency# , " +
            " amount = #amount# , " +
            " approvalLevelRequired = #approvalLevelRequired# , " +
            " approvalLevelGranted = #approvalLevelGranted# , " +
            " remarks = #remarks# , " + " rejectReason = #rejectReason# , " +
            " info= #info# , " + " state = #state# , " +
            " status = #status#, " + " claimMonth = #claimMonth#, " +
            "approvedBy = #approvedBy#, " + "rejectedBy = #rejectedBy# " +
            " WHERE id = #id# ", obj);
    }


    public void updateObjectApprover1(ClaimFormIndex obj) throws DaoException{

          super.update("UPDATE " + TABLENAME + " SET " +
            " timeEdit = #timeEdit# , " +
            " userOriginator = #userOriginator# , " +
            " userOwner = #userOwner# , " +
            " userApprover1 = #userApprover1# , " +
            " userApprover2 = #userApprover2# , " +
            " userApprover3 = #userApprover3# , " +
            " userApprover4 = #userApprover4# , " +
            " userAssessor = #userAssessor# , " + " currency = #currency# , " +
            " amount = #amount# , " +
            " approvalLevelRequired = #approvalLevelRequired# , " +
            " approvalLevelGranted = #approvalLevelGranted# , " +
            " remarks = #remarks# , " + " rejectReason = #rejectReason# , " +
            " info= #info# , " + " state = #state# , " +
            " status = #status#, " + " claimMonth = #claimMonth#, " +
            "approvedBy = #approvedBy#, " + "rejectedBy = #rejectedBy# , userApprover1Date =now() " +
            " WHERE id = #id# ", obj);

    }


        public void updateObjectApprover2(ClaimFormIndex obj) throws DaoException{

          super.update("UPDATE " + TABLENAME + " SET " +
            " timeEdit = #timeEdit# , " +
            " userOriginator = #userOriginator# , " +
            " userOwner = #userOwner# , " +
            " userApprover1 = #userApprover1# , " +
            " userApprover2 = #userApprover2# , " +
            " userApprover3 = #userApprover3# , " +
            " userApprover4 = #userApprover4# , " +
            " userAssessor = #userAssessor# , " + " currency = #currency# , " +
            " amount = #amount# , " +
            " approvalLevelRequired = #approvalLevelRequired# , " +
            " approvalLevelGranted = #approvalLevelGranted# , " +
            " remarks = #remarks# , " + " rejectReason = #rejectReason# , " +
            " info= #info# , " + " state = #state# , " +
            " status = #status#, " + " claimMonth = #claimMonth#, " +
            "approvedBy = #approvedBy#, " + "rejectedBy = #rejectedBy# , userApprover2Date =now() " +
            " WHERE id = #id# ", obj);

    }


    public Collection selectRecords(String field1, String value1, int start,
        int number) throws DaoException {
        /// super.select
        //// starting row and max number of records.. 0,1
        //// -1 if grep everything
        Collection col = super.select(" SELECT " + "  id , " + "  timeEdit, " +
                " userOriginator, " + " userOwner, " + " userApprover1, " +
                " userApprover2, " + " userApprover3, " + " userApprover4, " +
                " userAssessor, " + " currency , " + " amount, " +
                " approvalLevelRequired, " + " approvalLevelGranted, " +
                " remarks, " + " rejectReason, " + " info, " + " state, " +
                " status, " + "approvedBy, " + "rejectedBy " + " FROM " +
                TABLENAME + " WHERE " + field1 + " = ? ", ClaimFormIndex.class,
                new Object[] { value1 }, start, number);

        /*                ClaimFormIndex obj = null;
                        if(col.size()>0)
                        {
                                Iterator iterator = col.iterator();
                                oppArc = (OpportunityArchive) iterator.next();
                        }
                        return (oppArc);
        */
        return col;
    }

    public ClaimFormIndex selectRecord(String id) throws DaoException {
        Collection col = super.select(" SELECT " + "  id , " + "  timeEdit, " +
                " userOriginator, " + " userOwner, " + " userApprover1, " +
                " userApprover2, " + " userApprover3, " + " userApprover4, " +
                " userAssessor, " + " currency , " + " amount, " +
                " approvalLevelRequired, " + " approvalLevelGranted, " +
                " remarks, " + " rejectReason, " + " info, " + " state, " +
                " status, claimMonth, approvedBy, rejectedBy " + " FROM " +
                TABLENAME + " WHERE id = ? ", ClaimFormIndex.class,
                new Object[] { id }, 0, 1);

        ClaimFormIndex obj = null;

        if (col.size() > 0) {
            Iterator iterator = col.iterator();
            obj = (ClaimFormIndex) iterator.next();
        }

        return (obj);
    }

    public Date retrieveApprover1Date(String id) throws DaoException{

        Collection col =super.select("SELECT userApprover1Date FROM claim_form_index WHERE id=?", ClaimFormIndex.class,new Object[]{id},0,1);

        if(col.size()>0){
            Iterator iterator = col.iterator();
              ClaimFormIndex obj = (ClaimFormIndex) iterator.next();
              return obj.getUserApprover1Date();


        }

        return null;

    }


    public Date retrieveApprover2Date(String id) throws DaoException{

        Collection col =super.select("SELECT userApprover2Date FROM claim_form_index WHERE id=?", ClaimFormIndex.class,new Object[]{id},0,1);

        if(col.size()>0){
            Iterator iterator = col.iterator();
              ClaimFormIndex obj = (ClaimFormIndex) iterator.next();
              return obj.getUserApprover2Date();


        }

        return null;

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
                "SELECT COUNT(*) AS total FROM claim_form_index WHERE status = '" +
                status + "' ", HashMap.class, null, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int count(String[] conditions) throws DaoException {
        String sqlStmt = "SELECT COUNT(*) AS total FROM claim_form_index ";

        if (conditions.length > 0) {
            sqlStmt += " WHERE ";
        }

        for (int cnt1 = 0; cnt1 < conditions.length; cnt1++) {
            if (cnt1 > 0) {
                sqlStmt += " AND ";
            }

            sqlStmt += (" " + conditions[cnt1] + " ");
        }

        Collection list = super.select(sqlStmt, HashMap.class, null, 0, 1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countWithDate(String[] conditions, Date startDate, Date endDate)
        throws DaoException {
        String sqlStmt = "SELECT COUNT(*) AS total FROM claim_form_index ";

        if (conditions.length > 0) {
            sqlStmt += " WHERE ";
        }

        for (int cnt1 = 0; cnt1 < conditions.length; cnt1++) {
            if (cnt1 > 0) {
                sqlStmt += " AND ";
            }

            sqlStmt += (" " + conditions[cnt1] + " ");
        }

        if ((startDate != null) && (endDate != null)) {
            if (startDate.compareTo(endDate) == 0) {
                sqlStmt += " AND (timeEdit = ? ) ";
            }
        }  if ((startDate != null) && (endDate != null) &&
                startDate.before(endDate)) {
            sqlStmt += " AND (timeEdit >= ? AND timeEdit <= ?) ";
        }

        Collection list = null;

        if ((startDate != null) && (endDate != null)) {
            if (startDate.compareTo(endDate) == 0) {
                list = super.select(sqlStmt, HashMap.class,
                        new Object[] { startDate }, 0, 1);
            }
        }
        if ((startDate != null) && (endDate != null) &&
                startDate.before(endDate)) {
            list = super.select(sqlStmt, HashMap.class,
                    new Object[] { startDate, endDate }, 0, 1);
        } else {
            list = super.select(sqlStmt, HashMap.class, null, 0, 1);
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
            "SELECT id, timeEdit, userOriginator, userOwner, userApprover1, userApprover2, userApprover3, userApprover4, userAssessor, currency, amount, approvalLevelRequired, approvalLevelGranted, remarks, rejectReason, info, state, status, approvedBy, rejectedBy FROM claim_form_index WHERE name LIKE ? ORDER BY " +
            orderBy, ClaimFormIndex.class, args, start, rows);
    }

    public Collection query(String[] conditions, String sort, boolean desc,
        int start, int rows) throws DaoException {
        String sqlStmt = "SELECT id, timeEdit, userOriginator, userOwner, userApprover1, userApprover2, userApprover3, userApprover4, userAssessor, currency, amount, approvalLevelRequired, approvalLevelGranted, remarks, rejectReason, info, state, status, approvedBy, rejectedBy FROM claim_form_index ";

        if (conditions.length > 0) {
            sqlStmt += " WHERE ";
        }

        for (int cnt1 = 0; cnt1 < conditions.length; cnt1++) {
            if (cnt1 > 0) {
                sqlStmt += " AND ";
            }

            sqlStmt += (" " + conditions[cnt1] + " ");
        }

        if (sort != null) {
            sqlStmt += (" ORDER BY " + sort + " " + (desc ? "DESC" : ""));
        }

        return super.select(sqlStmt, ClaimFormIndex.class, null, start, rows);
    }

    public Collection queryWithDate(String[] conditions, Date startDate,
        Date endDate, String sort, boolean desc, int start, int rows)
        throws DaoException {
        String sqlStmt = "SELECT claim_form_index.id, claim_form_index.timeEdit, claim_form_index.userOriginator, claim_form_index.userOwner, claim_form_index.userApprover1, claim_form_index.userApprover2, claim_form_index.userApprover3, claim_form_index.userApprover4, claim_form_index.userAssessor, claim_form_index.currency, claim_form_index.amount, claim_form_index.approvalLevelRequired, claim_form_index.approvalLevelGranted, claim_form_index.remarks, claim_form_index.rejectReason, claim_form_index.info, claim_form_index.state, claim_form_index.status, claim_form_index.approvedBy, claim_form_index.rejectedBy FROM claim_form_index LEFT JOIN security_user s1 ON claim_form_index.userOriginator=s1.id LEFT JOIN security_user s2 ON claim_form_index.userOwner=s2.id ";

        
        
        if(sort !=null && sort.equals(""))
        {
          sort = "s1.lastName";	
        }
        
        if(sort !=null && sort.equals("userOriginator"))
        {
          sort = "s1.lastName";	
        }
        
        
       
        
        if(sort !=null && sort.equals("userOwner"))
        {
          sort = "s2.lastName";	
        }
        
        
        
        if (conditions.length > 0) {
            sqlStmt += " WHERE ";
        }

        for (int cnt1 = 0; cnt1 < conditions.length; cnt1++) {
            if (cnt1 > 0) {
                sqlStmt += " AND ";
            }

            sqlStmt += (" " + conditions[cnt1] + " ");
        }

        if ((startDate != null) && (endDate != null)) {
            if (startDate.compareTo(endDate) == 0) {
                sqlStmt += " AND (claim_form_index.timeEdit = ? ) ";
            }
        }
        if ((startDate != null) && (endDate != null) &&
                startDate.before(endDate)) {
            sqlStmt += " AND (claim_form_index.timeEdit >= ? AND claim_form_index.timeEdit <= ?) ";
        }

        if (sort != null) {
            sqlStmt += (" ORDER BY " + sort + " " + (desc ? "DESC" : ""));
        }

        if ((startDate != null) && (endDate != null)) {
            if (startDate.compareTo(endDate) == 0) {
                return super.select(sqlStmt, ClaimFormIndex.class,
                    new Object[] { startDate }, start, rows);
            }
        }
         if ((startDate != null) && (endDate != null) ) {

           if(startDate.before(endDate)){
            return super.select(sqlStmt, ClaimFormIndex.class,
                new Object[] { startDate, endDate }, start, rows);
           }

         }
            return super.select(sqlStmt, ClaimFormIndex.class, null, start, rows);



    }

    public String selectHierarchy(String employeeID) throws DaoException {
        String sql =
            "SELECT eh_report_to AS reportTo FROM employee_hierarchy WHERE eh_employee_id='" +
            employeeID + "'";

        try {
            Map row = (Map) select(sql, HashMap.class, null, 0, -1).iterator()
                                .next();

            return row.get("reportTo").toString();
        } catch (Exception ex) {
           

            return (String) null;
        }
    }

    public List generateReport(Date date, String employeeId,
        String departmentId, List categoryList) throws DaoException {
        StringBuffer sb;
        List resultList;

        sb = new StringBuffer();
        sb.append(
            "SELECT user.username, category.id AS catId, department.dm_dept_desc AS department, category.code, SUM(item.amount) AS amount ");
        sb.append("FROM employee_main employee  ");
        sb.append(" ");
        sb.append(
            "JOIN claim_form_index form ON employee.em_dept_code=department.dm_dept_code ");
        sb.append("JOIN claim_form_item item ON form.id=item.formId ");
        sb.append(
            "JOIN claim_form_item_category category ON item.categoryId=category.id ");
        sb.append("JOIN security_user user ON form.userOwner=user.id ");
        sb.append(
            "JOIN department_main department ON user.username=employee.em_employee_name ");
        sb.append(" ");

        //sb.append("WHERE item.projectId='default' AND claimMonth=? ");
        sb.append("WHERE claimMonth=? ");

        if (!("".equals(employeeId) && "".equals(departmentId))) {
            sb.append("AND ( ");
        }

        if (!"".equals(employeeId)) {
            sb.append("user.id='" + employeeId + "' ");
        }

        if (!"".equals(employeeId) && !"".equals(departmentId)) {
            sb.append("OR ");
        }

        if (!"".equals(departmentId)) {
            sb.append("department.dm_dept_code='" + departmentId + "' ");
        }

        if (!("".equals(employeeId) && "".equals(departmentId))) {
            sb.append(") ");
        }

        // support multiple categoryId
        if ((categoryList != null) && (categoryList.size() > 0)) {
            sb.append("AND (");

            for (int i = 0; i < categoryList.size(); i++) {
                String categoryId = (String) categoryList.get(i);
                sb.append("category.id='" + categoryId + "' ");

                if ((i + 1) < categoryList.size()) {
                    sb.append("OR ");
                }
            }

            sb.append(") ");
        }

        sb.append("GROUP BY username, category.code ");
        sb.append("ORDER BY user.username ");

        Object[] args = { date };

        resultList = (List) super.select(sb.toString(), HashMap.class, args, 0,
                -1);

        return resultList;
    }

    public List generateProjectReport(Date date, String employeeId,
        String departmentId) throws DaoException {
        StringBuffer sb;
        List resultList;

        sb = new StringBuffer();
        sb.append("SELECT user.username, SUM(item.amount) AS amount ");
        sb.append("FROM employee_main employee  ");
        sb.append(" ");
        sb.append(
            "JOIN claim_form_index form ON employee.em_dept_code=department.dm_dept_code ");
        sb.append("JOIN claim_form_item item ON form.id=item.formId ");
        sb.append("JOIN security_user user ON form.userOwner=user.id ");
        sb.append(
            "JOIN department_main department ON user.username=employee.em_employee_name ");
        sb.append(" ");
        sb.append("WHERE item.projectId!='default' AND claimMonth=? ");

        if (!("".equals(employeeId) && "".equals(departmentId))) {
            sb.append("AND ( ");
        }

        if (!"".equals(employeeId)) {
            sb.append("user.id='" + employeeId + "' ");
        }

        if (!"".equals(employeeId) && !"".equals(departmentId)) {
            sb.append("OR ");
        }

        if (!"".equals(departmentId)) {
            sb.append("department.dm_dept_code='" + departmentId + "' ");
        }

        if (!("".equals(employeeId) && "".equals(departmentId))) {
            sb.append(") ");
        }

        sb.append("GROUP BY username ");
        sb.append("ORDER BY user.username ");

        Object[] args = { date };

        resultList = (List) super.select(sb.toString(), HashMap.class, args, 0,
                -1);

        return resultList;
    }

    // add for generate personal monthly report
    public Collection selectForMonthlyReport(String userId, String monthStr)
        throws DaoException {
        Collection col = null;
        String sql = " SELECT " + "  id , " + "  timeEdit, " +
            " userOriginator, " + " userOwner, " + " userApprover1, " +
            " userApprover2, " + " userApprover3, " + " userApprover4, " +
            " userAssessor, " + " currency , " + " amount, " +
            " approvalLevelRequired, " + " approvalLevelGranted, " +
            " remarks, " + " rejectReason, " + " info, " + " state, " +
            " status, claimMonth, approvedBy, rejectedBy " + " FROM " +
            TABLENAME + " WHERE userOwner='" + userId + "' " +
            "AND claimMonth like '" + monthStr + "%' " + "AND state='clo'";
        col = super.select(sql, ClaimFormIndex.class, null, 0, -1);

        return col;
    }

    public String remarkName(String id) throws DaoException {
        ClaimFormItem object = new ClaimFormItem();
        Collection result;
        result = super.select("SELECT * from claim_form_item WHERE id=?",
                ClaimFormItem.class, new Object[] { id }, 0, 1);

        if (result.size() > 0) {
            object = (ClaimFormItem) result.iterator().next();

            return object.getRemarks();
        }

        return null;
    }
    
    
    
    /**
     * Loads users with a specific username. allow retrieve of multiple users, existing kacang only allow retrieve one user
     */
    public Collection selectUsersByUsername(String username) throws DaoException, DataObjectNotFoundException {
        Object[] args = new Object[]{username};
        Collection list = super.select("SELECT id, username, password, weakpass, firstName, lastName, nickName, title, designation, email1, email2, email3, company, homepage, address, postcode, city, state, country, telOffice, telHome, telMobile, fax, notes, property1, property2, property3, property4, property5, active, locale FROM security_user WHERE username LIKE ?", User.class, args, 0, -1);
        if (list.size() <= 0) throw new DataObjectNotFoundException("User " + username + " unavailable");
        return list;
    }
}
