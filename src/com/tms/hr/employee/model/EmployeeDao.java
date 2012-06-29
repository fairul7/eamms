package com.tms.hr.employee.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;

import kacang.util.Log;
import kacang.util.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class EmployeeDao extends DataSourceDao {
    Log log = Log.getLog(getClass());

    public void init() throws DaoException {
        try {
            // update existing tables
            super.update("ALTER TABLE employee_main ADD em_shift_worker CHAR(1) NOT NULL DEFAULT '0'",
                null);
            super.update("ALTER TABLE employee_main ADD em_work_location VARCHAR(255)",
                null);
        } catch (DaoException e) {
            ;
        }

        super.update("CREATE TABLE department_main(dm_dept_code varchar(10) NOT NULL default '', dm_dept_desc varchar(100) default NULL, dm_head varchar(255) default NULL, dm_recommender varchar(255) default NULL, dm_approver varchar(255) default NULL, PRIMARY KEY(dm_dept_code))",
            null);
        super.update("CREATE TABLE employee_hierarchy(eh_employee_id VARCHAR(255) NOT NULL DEFAULT '', eh_report_to VARCHAR(255) NOT NULL DEFAULT '', PRIMARY KEY(eh_employee_id))",
            null);
        super.update(
            "CREATE TABLE employee_main (em_employee_id VARCHAR(255) NOT NULL DEFAULT '', em_employee_name VARCHAR(255) NOT NULL DEFAULT '', " +
            "em_status VARCHAR(255) NOT NULL DEFAULT '', em_dept_code VARCHAR(10) DEFAULT NULL, " +
            "em_gender VARCHAR(10) DEFAULT NULL, em_ic_no VARCHAR(10) DEFAULT NULL, " +
            "em_birth_date DATETIME DEFAULT NULL, em_birth_place VARCHAR(50) DEFAULT NULL, " +
            "em_birth_state VARCHAR(50) DEFAULT NULL, em_marital_state VARCHAR(10) DEFAULT NULL, " +
            "em_child_no INT(2) DEFAULT NULL, em_race VARCHAR(10) DEFAULT NULL, " +
            "em_religion VARCHAR(10) DEFAULT NULL, em_citizen VARCHAR(10) DEFAULT NULL, " +
            "em_current_address VARCHAR(255) DEFAULT NULL, em_current_state VARCHAR(10) DEFAULT NULL, " +
            "em_current_country VARCHAR(10) DEFAULT NULL, em_current_pcode VARCHAR(6) DEFAULT NULL, " +
            "em_permanent_address VARCHAR(255) DEFAULT NULL, em_permanent_state VARCHAR(10) DEFAULT NULL, " +
            "em_permanent_country VARCHAR(10) DEFAULT NULL, em_permanent_pcode VARCHAR(6) DEFAULT NULL, " +
            "em_telno_work VARCHAR(12) DEFAULT NULL, em_telno_home VARCHAR(12) DEFAULT NULL, " +
            "em_handphone_no VARCHAR(12) DEFAULT NULL, em_fax_no VARCHAR(12) DEFAULT NULL, " +
            "em_drv_licence_no VARCHAR(15) DEFAULT NULL, em_join_date DATETIME DEFAULT NULL, " +
            "em_resign_date DATETIME DEFAULT NULL, em_email_addr VARCHAR(50) DEFAULT NULL, " +
            "em_shift_worker CHAR(1) NOT NULL DEFAULT '0', em_work_location VARCHAR(255), " +
            "em_spouse_name VARCHAR(255) DEFAULT NULL, em_sal_incr_date DATETIME DEFAULT NULL, PRIMARY KEY(em_employee_id))",
            null);
        super.update("CREATE TABLE users_departments(ud_employee_id VARCHAR(255) NOT NULL DEFAULT '', ud_dept_code VARCHAR(10) NOT NULL DEFAULT '', ud_recommender_status CHAR(1) DEFAULT NULL, ud_service_class VARCHAR(10) DEFAULT NULL, PRIMARY KEY(ud_employee_id))",
            null);
    }

    public Collection selectAllEmployees() throws DaoException {
        Collection employee = new ArrayList();
        String sql =
            "SELECT em_employee_id AS employeeID,em_employee_name AS name, u.firstName, u.lastName, em_shift_worker AS shiftWorker " +
            " FROM employee_main e " +
            " LEFT JOIN security_user u ON e.em_employee_id=u.id" +
            " ORDER BY u.firstName";
        employee = super.select(sql, EmployeeDataObject.class, null, 0, -1);

        return employee;
    }

    public Collection selectUsersForEmployeeSetup() throws DaoException {
        Collection employee = new ArrayList();
        String sql =
            "SELECT su.id AS employeeID, su.username AS name, su.firstName, su.lastName FROM security_user su" +
            " LEFT OUTER JOIN employee_main em ON su.id = em.em_employee_id " +
            " WHERE em.em_employee_id is NULL" + " ORDER BY su.firstName";
        employee = super.select(sql, EmployeeDataObject.class, null, 0, -1);

        return employee;
    }

    public Collection selectEmployeesForDepartmentSetup()
        throws DaoException {
        Collection employee = new ArrayList();
        String sql =
            "SELECT em_employee_id AS employeeID,em_employee_name AS name, u.firstName, u.lastName " +
            " FROM employee_main " +
            " LEFT OUTER JOIN users_departments ud ON em_employee_id = ud_employee_id " +
            " LEFT JOIN security_user u ON em_employee_id=u.id " +
            " WHERE ud_employee_id IS NULL";
        employee = super.select(sql, EmployeeDataObject.class, null, 0, -1);

        return employee;
    }

    public int countEmployeesForDepartmentSetup() throws DaoException {
        String sql = "SELECT count(*) AS total FROM employee_main " +
            " LEFT OUTER JOIN users_departments ud ON em_employee_id = ud_employee_id " +
            " WHERE ud_employee_id IS NULL";
        Map row = (Map) super.select(sql, HashMap.class, null, 0, -1).iterator()
                             .next();

        return Integer.parseInt(row.get("total").toString());
    }

    public Collection selectEmployeesForHierarchySetup(String deptCode)
        throws DaoException {
        Collection employee = new ArrayList();
        String sql =
            "SELECT em_employee_id AS employeeID,em_employee_name AS name, u.firstName, u.lastName " +
            " FROM employee_main " +
            " LEFT OUTER JOIN employee_hierarchy eh ON em_employee_id = eh_employee_id " +
            " LEFT OUTER JOIN users_departments ud ON em_employee_id = ud_employee_id " +
            " LEFT JOIN security_user u ON em_employee_id=u.id " +
            " WHERE (eh_employee_id IS NULL  OR eh_report_to = '') AND ud_dept_code='" +
            deptCode + "'";
        employee = super.select(sql, EmployeeDataObject.class, null, 0, -1);

        return employee;
    }

    public Collection selectAllServiceClass() throws DaoException {
        Collection service = new ArrayList();
        String sql = "SELECT sc_class_code AS serviceCode ,sc_class_desc AS serviceDesc FROM service_classfication ";
        service = super.select(sql, DepartmentDataObject.class, null, 0, -1);

        return service;
    }

    public Collection selectDepartmentHierarchy(String searchBy, String name,
        String sort, int start, int rows) throws DaoException {
        Collection hierarchy = new ArrayList();
        String sql =
            "SELECT eh.eh_employee_id AS employeeID, em1.em_employee_name AS name, eh_report_to AS reportTo, em2.em_employee_name AS reportToName," +
            " u1.firstName AS firstName, u1.lastName AS lastName, u2.firstName AS reportToFirstName, u2.lastName AS reportToLastName" +
            " FROM employee_hierarchy eh,employee_main em1,employee_main em2,users_departments ud,security_user u1,security_user u2" +
            " WHERE eh.eh_employee_id = em1.em_employee_id AND eh.eh_report_to=em2.em_employee_id" +
            " AND ud.ud_employee_id = eh.eh_employee_id " +
            " AND em1.em_employee_id=u1.id AND em2.em_employee_id=u2.id";

        if ((searchBy != null) && !searchBy.equals("")) {
            sql = sql + " AND ud.ud_dept_code='" + searchBy + "'";
        }

        if ((name != null) && !("".equals(name))) {
            sql = sql + "  AND (u1.firstName LIKE '%" + name +
                "%' OR u1.lastName LIKE '%" + name +
                "%' OR em1.em_employee_name LIKE '%" + name + "%')";
        }

        hierarchy = super.select(sql, DepartmentDataObject.class, null, start,
                rows);

        return hierarchy;
    }

    public int selectDepartmentHierarchyCount(String searchBy, String name)
        throws DaoException {
        /*
                String sql = "SELECT count(*) AS total" +
                        " FROM employee_hierarchy eh,users_departments ud" +
                        " WHERE ud.ud_employee_id = eh.eh_employee_id ";
        */
        String sql = "SELECT COUNT(*) AS total" +
            " FROM employee_hierarchy eh,employee_main em1,employee_main em2,users_departments ud,security_user u1,security_user u2" +
            " WHERE eh.eh_employee_id = em1.em_employee_id AND eh.eh_report_to=em2.em_employee_id" +
            " AND ud.ud_employee_id = eh.eh_employee_id " +
            " AND em1.em_employee_id=u1.id AND em2.em_employee_id=u2.id";

        if ((searchBy != null) && !searchBy.equals("")) {
            sql = sql + " AND ud.ud_dept_code='" + searchBy + "'";
        }

        if ((name != null) && !("".equals(name))) {
            sql = sql + "  AND (u1.firstName LIKE '%" + name +
                "%' OR u1.lastName LIKE '%" + name +
                "%' OR em1.em_employee_name LIKE '%" + name + "%')";
        }

        Map row = (Map) super.select(sql, HashMap.class, null, 0, -1).iterator()
                             .next();

        return Integer.parseInt(row.get("total").toString());
    }

    public Collection selectEmployee(String employeeID)
        throws DaoException {
        Collection employee = new ArrayList();
        String sql =
            "SELECT em.em_employee_id AS employeeID, em.em_employee_name AS name,dm.dm_dept_desc AS deptDesc," +
            "em.em_dept_code AS deptCode,em.em_join_date AS joinDate,em.em_resign_date AS resignDate,em.em_email_addr AS email,em.em_gender AS gender,em.em_shift_worker AS shiftWorker,em_work_location AS workLocation" +
            " FROM employee_main em,department_main dm" +
            " WHERE dm.dm_dept_code=em.em_dept_code AND em.em_employee_id=?";
        employee = super.select(sql, EmployeeDataObject.class,
                new String[] { employeeID }, 0, -1);

        return employee;
    }

    public Collection selectEmployeeProfile(String searchBy,
        String departmentCode, String sort, boolean desc, int start, int rows)
        throws DaoException {
        Collection hierarchy = new ArrayList();
        Collection args = new ArrayList();
        String sql =
            "SELECT em.em_employee_id AS employeeID, em.em_employee_name AS name,dm.dm_dept_desc AS deptDesc," +
            " em.em_dept_code AS deptCode,em.em_join_date AS joinDate,em.em_resign_date AS resignDate,u.email1 AS email,em.em_shift_worker AS shiftWorker,em_work_location AS workLocation," +
            " u.firstName, u.lastName" +
            " FROM employee_main em, department_main dm, security_user u" +
            " WHERE dm.dm_dept_code=em.em_dept_code AND em.em_employee_id=u.id";

        if ((searchBy != null) && (searchBy.trim().length() > 0)) {
            searchBy = "%" + searchBy.trim() + "%";
            sql += " AND (u.firstName LIKE ? OR u.lastName LIKE ? OR em.em_employee_name LIKE ?)";
            args.add(searchBy);
            args.add(searchBy);
            args.add(searchBy);
        }

        if ((departmentCode != null) && (departmentCode.trim().length() > 0)) {
            sql += " AND em.em_dept_code=?";
            args.add(departmentCode);
        }

        if (sort.equals("name")) {
            sql = sql + " ORDER BY em.em_employee_name";
        } else if (sort.equals("deptDesc")) {
            sql = sql + " ORDER BY dm.dm_dept_desc";
        } else if (sort.equals("joinDate")) {
            sql = sql + " ORDER BY em.em_join_date";
        } else if (sort.equals("email")) {
            sql = sql + " ORDER BY u.email1";
        } else if (sort.equals("firstName")) {
            sql = sql + " ORDER BY u.firstName";
        } else if (sort.equals("lastName")) {
            sql = sql + " ORDER BY u.lastName";
        }

        if (desc) {
            sql = sql + " DESC";
        }

        hierarchy = super.select(sql, EmployeeDataObject.class, args.toArray(),
                start, rows);

        return hierarchy;
    }

    public int selectEmployeeProfileCount(String searchBy, String departmentCode)
        throws DaoException {
        Collection args = new ArrayList();
        String sql = "SELECT COUNT(*) AS total" +
            " FROM employee_main em, department_main dm, security_user u" +
            " WHERE dm.dm_dept_code=em.em_dept_code AND em.em_employee_id=u.id";

        if ((searchBy != null) && (searchBy.trim().length() > 0)) {
            searchBy = "%" + searchBy.trim() + "%";
            sql += " AND (u.firstName LIKE ? OR u.lastName LIKE ? OR em.em_employee_name LIKE ?)";
            args.add(searchBy);
            args.add(searchBy);
            args.add(searchBy);
        }

        if ((departmentCode != null) && (departmentCode.trim().length() > 0)) {
            sql += " AND em.em_dept_code=?";
            args.add(departmentCode);
        }

        Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
                             .iterator().next();

        return Integer.parseInt(row.get("total").toString());
    }

    public Collection selectDepartmentEmployees(String searchBy,
        String departmentCode, String serviceClass, String sort, boolean desc,
        int start, int rows) throws DaoException {
        Collection hierarchy = new ArrayList();
        String sql =
            "SELECT em.em_employee_id AS employeeID, em.em_employee_name AS name,dm.dm_dept_desc AS deptDesc," +
            "ud.ud_recommender_status AS recommender,sc.sc_class_desc AS serviceDesc," +
            "u.firstName, u.lastName" + " FROM employee_main em" +
            " LEFT OUTER JOIN  users_departments ud  ON em.em_employee_id = ud.ud_employee_id" +
            " LEFT OUTER JOIN  service_classfication sc ON sc.sc_class_code = ud.ud_service_class" +
            " LEFT OUTER JOIN  department_main dm ON dm.dm_dept_code=ud.ud_dept_code" +
            " LEFT JOIN security_user u ON em.em_employee_id=u.id" +
            " WHERE dm.dm_dept_desc IS NOT NULL";

        Collection args = new ArrayList();

        if ((searchBy != null) && (searchBy.trim().length() > 0)) {
            searchBy = "%" + searchBy.trim() + "%";
            sql += " AND (u.firstName LIKE ? OR u.lastName LIKE ? OR em.em_employee_name LIKE ?)";
            args.add(searchBy);
            args.add(searchBy);
            args.add(searchBy);
        }

        if ((departmentCode != null) && (departmentCode.trim().length() > 0)) {
            sql += " AND dm.dm_dept_code=?";
            args.add(departmentCode);
        }

        if ((serviceClass != null) && (serviceClass.trim().length() > 0)) {
            sql += " AND sc.sc_class_code=?";
            args.add(serviceClass);
        }

        if (sort.equals("name")) {
            sql = sql + " ORDER BY em.em_employee_name";
        } else if (sort.equals("deptDesc")) {
            sql = sql + " ORDER BY dm.dm_dept_desc";
        } else if (sort.equals("recommender")) {
            sql = sql + " ORDER BY ud.ud_recommender_status";
        } else if (sort.equals("serviceDesc")) {
            sql = sql + " ORDER BY sc.sc_class_desc";
        } else if (sort.equals("firstName")) {
            sql = sql + " ORDER BY u.firstName";
        } else if (sort.equals("lastName")) {
            sql = sql + " ORDER BY u.lastName";
        }

        if (desc) {
            sql = sql + " DESC";
        }

        hierarchy = super.select(sql, DepartmentDataObject.class,
                args.toArray(), start, rows);

        return hierarchy;
    }

    public int selectDepartmentEmployeesCount(String searchBy,
        String departmentCode, String serviceClass) throws DaoException {
        String sql = "SELECT COUNT(*) AS total" + " FROM employee_main em" +
            " LEFT OUTER JOIN  users_departments ud  ON em.em_employee_id = ud.ud_employee_id" +
            " LEFT OUTER JOIN  service_classfication sc ON sc.sc_class_code = ud.ud_service_class" +
            " LEFT OUTER JOIN  department_main dm ON dm.dm_dept_code=ud.ud_dept_code" +
            " LEFT JOIN security_user u ON em.em_employee_id=u.id" +
            " WHERE dm.dm_dept_desc IS NOT NULL";

        Collection args = new ArrayList();

        if ((searchBy != null) && (searchBy.trim().length() > 0)) {
            searchBy = "%" + searchBy.trim() + "%";
            sql += " AND (u.firstName LIKE ? OR u.lastName LIKE ? OR em.em_employee_name LIKE ?)";
            args.add(searchBy);
            args.add(searchBy);
            args.add(searchBy);
        }

        if ((departmentCode != null) && (departmentCode.trim().length() > 0)) {
            sql += " AND em.em_dept_code=?";
            args.add(departmentCode);
        }

        if ((serviceClass != null) && (serviceClass.trim().length() > 0)) {
            sql += " AND sc.sc_class_code=?";
            args.add(serviceClass);
        }

        Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
                             .iterator().next();

        return Integer.parseInt(row.get("total").toString());
    }

    public DepartmentDataObject selectEmployeeDepartment(String employeeID)
        throws DaoException {
        String sql =
            "SELECT ud.ud_dept_code AS deptCode,dm.dm_dept_desc AS deptDesc,ud.ud_recommender_status AS recommender," +
            " ud.ud_service_class AS serviceCode,sc.sc_class_desc AS serviceDesc" +
            " FROM users_departments ud" +
            " LEFT OUTER JOIN department_main dm ON dm.dm_dept_code=ud.ud_dept_code " +
            " LEFT OUTER JOIN service_classfication sc ON  sc.sc_class_code = ud.ud_service_class " +
            " WHERE ud.ud_employee_id='" + employeeID + "'";
        Collection row = super.select(sql, DepartmentDataObject.class, null, 0,
                -1);

        return (DepartmentDataObject) row.iterator().next();
    }

    public void insertHierarchy(DepartmentDataObject setup)
        throws DaoException {
        String sql1 = "DELETE FROM employee_hierarchy WHERE eh_employee_id=#employeeID#";
        String sql2 =
            "INSERT INTO employee_hierarchy(eh_employee_id,eh_report_to)" +
            " VALUES(#employeeID#,#reportTo#)";
        super.update(sql1, setup);
        super.update(sql2, setup);
    }

    public void insertEmployeeDepartment(DepartmentDataObject department)
        throws DaoException {
        String sql =
            "INSERT INTO users_departments(ud_employee_id,ud_dept_code,ud_recommender_status,ud_service_class) " +
            " VALUES(#employeeID#,#deptCode#,#recommender#,#serviceCode#)";
        super.update(sql, department);
    }

    public int isUsersExists() throws DaoException {
        String sql = "SELECT count(*) AS total FROM security_user su" +
            " LEFT OUTER JOIN employee_main em ON su.id = em.em_employee_id " +
            " WHERE em.em_employee_id is NULL";
        Map row = (Map) super.select(sql, HashMap.class, null, 0, -1).iterator()
                             .next();

        return Integer.parseInt(row.get("total").toString());
    }

    public int isEmailExists(String email) throws DaoException {
        String sql =
            " SELECT count(*) AS total FROM employee_main where em_email_addr='" +
            email + "'";
        Map row = (Map) super.select(sql, HashMap.class, null, 0, -1).iterator()
                             .next();

        return Integer.parseInt(row.get("total").toString());
    }

    public void insertEmployee(EmployeeDataObject employee)
        throws DaoException {
        Transaction tx = null;
        String sql =
            "INSERT INTO employee_main(em_employee_id,em_employee_name,em_dept_code,em_gender,em_join_date,em_resign_date,em_email_addr,em_shift_worker,em_work_location)" + //, sm_status
            " VALUES(#employeeID#,#name#,#deptCode#,#gender#,#joinDate#,#resignDate#,#email#,#shiftWorker#,#workLocation#)";
        String sql_department =
            "INSERT INTO users_departments(ud_employee_id,ud_dept_code,ud_recommender_status)" +
            " VALUES(#employeeID#,#deptCode#,#recommender#)";

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, employee);
            tx.update(sql_department, employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public void updateEmployee(EmployeeDataObject employee)
        throws DaoException {
        Transaction tx = null;
        String sql =
            "UPDATE employee_main SET em_employee_name=#name#,em_dept_code=#deptCode#," +
            "em_gender=#gender#,em_join_date=#joinDate#,em_resign_date=#resignDate#,em_email_addr=#email#,em_shift_worker=#shiftWorker#,em_work_location=#workLocation#" +
            " WHERE em_employee_id=#employeeID#";

        /*sql_department="INSERT INTO users_departments(ud_employee_id,ud_dept_code,ud_recommender_status)"+
                              " VALUES(#employeeID#,#deptCode#,#recommender#)";*/
        String sql_department =
            "UPDATE users_departments SET ud_dept_code=#deptCode#,ud_recommender_status=#recommender#" +
            " WHERE ud_employee_id=#employeeID#";

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, employee);
            tx.update(sql_department, employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public void updateEmployeeDepartment(DepartmentDataObject department)
        throws DaoException {
        String sql =
            "UPDATE users_departments SET ud_dept_code=#deptCode#,ud_recommender_status=#recommender#,ud_service_class=#serviceCode#" +
            " WHERE ud_employee_id=#employeeID#";
        super.update(sql, department);
    }

    public void updateHierarchy(DepartmentDataObject setup)
        throws DaoException {
        String sql = " UPDATE employee_hierarchy SET eh_report_to= #reportTo#" +
            " WHERE eh_employee_id=#employeeID#";
        super.update(sql, setup);
    }

    public void deleteHierarchy(DepartmentDataObject department)
        throws DaoException {
        String sql = " DELETE FROM employee_hierarchy " +
            " WHERE eh_employee_id=#employeeID#";
        super.update(sql, department);
    }

    public void deleteEmployee(String employeeID) throws DaoException {
        Transaction tx = null;
        String sql = "DELETE FROM employee_main WHERE em_employee_id='" +
            employeeID + "'";
        String sql_del = "DELETE FROM users_departments WHERE ud_employee_id='" +
            employeeID + "'";
        String sql_hierarchy =
            "DELETE FROM employee_hierarchy WHERE eh_employee_id='" +
            employeeID + "'";

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, null);
            tx.update(sql_del, null);
            tx.update(sql_hierarchy, null);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public void deleteDepartmentEmployee(DepartmentDataObject department)
        throws DaoException {
        Transaction tx = null;
        String sql = "DELETE FROM users_departments " +
            " WHERE ud_employee_id=#employeeID#";
        String sql_hierarchy = "DELETE FROM employee_hierarchy WHERE eh_employee_id=#employeeID#";

        try {
            tx = getTransaction();
            tx.begin();
            tx.update(sql, department);
            tx.update(sql_hierarchy, department);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }

            Log.getLog(getClass()).error(e);
            throw new DaoException(e.toString());
        }
    }

    public void insertDepartment(DepartmentDataObject department)
        throws DaoException {
        String sql =
            "INSERT INTO department_main (dm_dept_code, dm_dept_desc, dm_head, dm_recommender, dm_approver) " +
            " VALUES(#deptCode#,#deptDesc#,#headCode#,#recommenderCode#,#approverCode#)";
        super.update(sql, department);
    }

    public void deleteDepartment(String key) throws DaoException {
        String sql = "DELETE FROM department_main WHERE dm_dept_code = '" +
            key + "'";
        super.update(sql, null);
    }

    public void updateDepartment(DepartmentDataObject department)
        throws DaoException {
        String sql =
            "UPDATE department_main SET dm_dept_desc = #deptDesc#, dm_head = #headCode#, " +
            "dm_recommender = #recommenderCode#, dm_approver = #approverCode# WHERE dm_dept_code = #deptCode#";
        super.update(sql, department);
    }

    public Collection selectDepartmentList(String deptCode, String sort,
        boolean desc, int start, int rows) throws DaoException {
        String condition = (deptCode != null) ? ("%" + deptCode + "%") : "%";
        String orderBy = (sort != null) ? sort : "dm_dept_code";

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { condition };

        return super.select(
            "SELECT dm_dept_code AS deptCode, dm_dept_desc AS deptDesc FROM department_main WHERE dm_dept_code LIKE ? ORDER BY " +
            orderBy, DepartmentDataObject.class, args, start, rows);
    }

    public int selectDepartmentListCount(String deptCode)
        throws DaoException {
        String condition = (deptCode != null) ? ("%" + deptCode + "%") : "%";
        Object[] args = { condition };
        String sql = "SELECT count(dm_dept_code) AS total FROM department_main WHERE dm_dept_code LIKE ?";
        Collection list = super.select(sql, HashMap.class, args, 0, -1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }
    
    public int selectDepartmentCodeCount(String deptCode)
        throws DaoException {
        Object[] args = { deptCode };
        String sql = "SELECT count(dm_dept_code) AS total FROM department_main WHERE dm_dept_code = ?";
        Collection list = super.select(sql, HashMap.class, args, 0, -1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public DepartmentDataObject selectDepartment(String key)
        throws DaoException {
        String sql =
            "SELECT dm_dept_code AS deptCode, dm_dept_desc AS deptDesc FROM department_main " +
            "WHERE dm_dept_code ='" + key + "'";
        Collection row = super.select(sql, DepartmentDataObject.class, null, 0,
                -1);

        return (DepartmentDataObject) row.iterator().next();
    }

    public void insertServiceClass(DepartmentDataObject service)
        throws DaoException {
        String sql =
            "INSERT INTO service_classfication (sc_class_code, sc_class_desc) " +
            "VALUES (#serviceCode#,#serviceDesc#)";
        super.update(sql, service);
    }

    public void deleteServiceClass(String key) throws DaoException {
        String sql = "DELETE FROM service_classfication WHERE sc_class_code = '" +
            key + "'";
        super.update(sql, null);
    }

    public void updateServiceClass(DepartmentDataObject service)
        throws DaoException {
        String sql = "UPDATE service_classfication SET sc_class_desc = #serviceDesc# WHERE sc_class_code = #serviceCode#";
        super.update(sql, service);
    }

    public Collection selectServiceClassList(String serviceCode, String sort,
        boolean desc, int start, int rows) throws DaoException {
        String condition = (serviceCode != null) ? ("%" + serviceCode + "%") : "%";
        String orderBy = (sort != null) ? sort : "sc_class_code";

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { condition };

        return super.select(
            "SELECT sc_class_code AS serviceCode, sc_class_desc AS serviceDesc FROM service_classfication WHERE sc_class_code LIKE ? ORDER BY " +
            orderBy, GlobalDataObject.class, args, start, rows);
    }

    public int selectServiceClassListCount(String serviceCode)
        throws DaoException {
        String condition = (serviceCode != null) ? ("%" + serviceCode + "%") : "%";
        Object[] args = { condition };
        String sql = "SELECT count(sc_class_code) AS total FROM service_classfication WHERE sc_class_code LIKE ?";
        Collection list = super.select(sql, HashMap.class, args, 0, -1);
        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }
    
    public int selectServiceClassCodeCount(String serviceCode)
	    throws DaoException {
	    Object[] args = { serviceCode };
	    String sql = "SELECT count(sc_class_code) AS total FROM service_classfication WHERE sc_class_code = ?";
	    Collection list = super.select(sql, HashMap.class, args, 0, -1);
	    HashMap map = (HashMap) list.iterator().next();
	
	    return Integer.parseInt(map.get("total").toString());
	}

    public DepartmentDataObject selectServiceClass(String key)
        throws DaoException {
        String sql =
            "SELECT sc_class_code AS serviceCode, sc_class_desc AS serviceDesc FROM service_classfication " +
            "WHERE sc_class_code ='" + key + "'";
        Collection row = super.select(sql, DepartmentDataObject.class, null, 0,
                -1);

        return (DepartmentDataObject) row.iterator().next();
    }

    public Collection selectDepartmentList() throws DaoException {
        Collection deptList = new ArrayList();
        String sql = "SELECT dm_dept_code AS deptCode, dm_dept_desc AS deptDesc " +
            " FROM department_main " + " ORDER BY dm_dept_desc";
        deptList = super.select(sql, DepartmentDataObject.class, null, 0, -1);

        return deptList;
    }

    public Collection selectEmployeeReportTo(String employeeID)
        throws DaoException {
        Collection department = new ArrayList();
        String sql =
            "SELECT su.username AS name,su.firstName,su.lastName,ud.ud_dept_code AS deptCode,dm.dm_dept_desc AS deptDesc,eh.eh_report_to AS reportTo" +
            " FROM users_departments ud,security_user su,employee_hierarchy eh ,department_main dm WHERE ud_employee_id='" +
            employeeID + "'" +
            " AND ud.ud_employee_id = su.id AND eh.eh_employee_id = ud.ud_employee_id AND dm.dm_dept_code = ud.ud_dept_code ";
        department = super.select(sql, DepartmentDataObject.class, null, 0, -1);

        return department;
    }

    public Collection selectAllDepartmentEmployees(String deptCode)
        throws DaoException {
        Collection usersList = new ArrayList();
        String sql =
            "SELECT ud.ud_employee_id AS employeeID,em.em_employee_name AS name" +
            " FROM users_departments ud,employee_main em" +
            " WHERE ud.ud_employee_id= em.em_employee_id AND ud_dept_code='" +
            deptCode + "'";
        usersList = super.select(sql, DepartmentDataObject.class, null, 0, -1);

        return usersList;
    }
}
