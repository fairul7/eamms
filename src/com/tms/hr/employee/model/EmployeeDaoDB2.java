package com.tms.hr.employee.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kacang.model.DaoException;

public class EmployeeDaoDB2 extends EmployeeDao{
	
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
	            sql = sql + "  AND (UPPER(u1.firstName) LIKE '%" + name.toUpperCase() +
	                "%' OR UPPER(u1.lastName) LIKE '%" + name.toUpperCase() +
	                "%' OR UPPER(em1.em_employee_name) LIKE '%" + name.toUpperCase() + "%')";
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
	            sql = sql + "  AND (UPPER(u1.firstName) LIKE '%" + name.toUpperCase() +
	                "%' OR UPPER(u1.lastName) LIKE '%" + name.toUpperCase() +
	                "%' OR UPPER(em1.em_employee_name) LIKE '%" + name.toUpperCase() + "%')";
	        }

	        Map row = (Map) super.select(sql, HashMap.class, null, 0, -1).iterator()
	                             .next();

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
	            sql += " AND (UPPER(u.firstName) LIKE ? OR UPPER(u.lastName) LIKE ? OR UPPER(em.em_employee_name) LIKE ?)";
	            args.add(searchBy.toUpperCase());
	            args.add(searchBy.toUpperCase());
	            args.add(searchBy.toUpperCase());
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
	            sql += " AND (UPPER(u.firstName) LIKE ? OR UPPER(u.lastName) LIKE ? OR UPPER(em.em_employee_name) LIKE ?)";
	            args.add(searchBy.toUpperCase());
	            args.add(searchBy.toUpperCase());
	            args.add(searchBy.toUpperCase());
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

}
