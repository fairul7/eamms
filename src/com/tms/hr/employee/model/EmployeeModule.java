package com.tms.hr.employee.model;

import kacang.model.DefaultModule;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;

import com.tms.hr.employee.model.DepartmentDataObject;

public class EmployeeModule extends DefaultModule {
    Log log = Log.getLog(getClass());

    public Collection getAllEmployees() throws EmployeeException {
        Collection employee = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            employee = dao.selectAllEmployees();
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return employee;
    }

    public Collection getEmployeesForHierarchySetup(String deptCode) throws EmployeeException {
        Collection employee = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            employee = dao.selectEmployeesForHierarchySetup(deptCode);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return employee;
    }

    public Collection getUsersForEmployeeSetup() throws EmployeeException {
        Collection employee = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            employee = dao.selectUsersForEmployeeSetup();
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return employee;
    }

    public Collection getEmployeesForDepartmentSetup() throws EmployeeException {
        Collection employee = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            employee = dao.selectEmployeesForDepartmentSetup();
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return employee;
    }

    public int countEmployeesForDepartmentSetup() throws EmployeeException {
        int result = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            result = dao.countEmployeesForDepartmentSetup();
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return result;
    }

    public Collection getAllServiceClass() throws EmployeeException {
        Collection service = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            service = dao.selectAllServiceClass();
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return service;
    }

    public Collection getEmployee(String employeeID) throws EmployeeException {
        Collection employee = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            employee = dao.selectEmployee(employeeID);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return employee;
    }

    public Collection getDepartmentHierarchy(String searchBy,String name, String sort, int start, int rows) throws EmployeeException {
        Collection deptList = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        if (searchBy.equals("0"))
            searchBy = "";
        try {
            deptList = dao.selectDepartmentHierarchy(searchBy, name, sort, start, rows);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return deptList;
    }

    public int getDepartmentHierarchyCount(String searchBy, String name) throws EmployeeException {
        int total = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        if (searchBy.equals("0"))
            searchBy = "";
        try {
            total = dao.selectDepartmentHierarchyCount(searchBy,name);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return total;
    }

    public Collection getEmployeeProfile(String searchBy, String departmentCode, String sort, boolean desc, int start, int rows) throws EmployeeException {
        Collection deptList = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        if (sort == null)
            sort = "name";
        try {
            deptList = dao.selectEmployeeProfile(searchBy, departmentCode, sort, desc, start, rows);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return deptList;
    }

    public int getEmployeeProfileCount(String searchBy, String departmentCode) throws EmployeeException {
        int total = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            total = dao.selectEmployeeProfileCount(searchBy, departmentCode);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return total;
    }

    public Collection getDepartmentEmployees(String searchBy, String departmentCode, String serviceClass, String sort, boolean desc, int start, int rows) throws EmployeeException {
        Collection deptList = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        if (sort == null)
            sort = "name";
        try {
            deptList = dao.selectDepartmentEmployees(searchBy, departmentCode, serviceClass, sort, desc, start, rows);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return deptList;
    }

    public int getDepartmentEmployeesCount(String searchBy, String department, String serviceClass) throws EmployeeException {
        int total = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            total = dao.selectDepartmentEmployeesCount(searchBy, department, serviceClass);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return total;
    }

    public DepartmentDataObject getEmployeeDepartment(String employeeID) throws EmployeeException {
        DepartmentDataObject department = new DepartmentDataObject();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            department = dao.selectEmployeeDepartment(employeeID);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return department;
    }

    public void deleteHierarchy(String employeeID) throws EmployeeException {
        DepartmentDataObject department = new DepartmentDataObject();
        department.setEmployeeID(employeeID);
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.deleteHierarchy(department);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void deleteEmployee(String employeeID) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.deleteEmployee(employeeID);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void deleteDepartmentEmployee(String employeeID) throws EmployeeException {
        DepartmentDataObject department = new DepartmentDataObject();
        department.setEmployeeID(employeeID);
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.deleteDepartmentEmployee(department);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void setupHierarchy(DepartmentDataObject setup) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.insertHierarchy(setup);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void addEmployeeDepartment(DepartmentDataObject department) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.insertEmployeeDepartment(department);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void addEmployee(EmployeeDataObject employee) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.insertEmployee(employee);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void editEmployee(EmployeeDataObject employee) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.updateEmployee(employee);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void updateHierarchy(DepartmentDataObject setup) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.updateHierarchy(setup);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void updateEmployeeDepartment(DepartmentDataObject department) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.updateEmployeeDepartment(department);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
    }

    public void addDepartment(DepartmentDataObject department) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.insertDepartment(department);
        }
        catch (Exception e) {
            throw new EmployeeException("Error adding department " + e.toString());
        }
    }

    public void updateDepartment(DepartmentDataObject department) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.updateDepartment(department);
        }
        catch (Exception e) {
            throw new EmployeeException("Error updating department " + e.toString());
        }
    }

    public void deleteDepartment(String key) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.deleteDepartment(key);
        }
        catch (Exception e) {
            throw new EmployeeException("Error deleting department " + key + " " + e.toString());
        }
    }

    public Collection getDepartmentList(String deptCode, String sort, boolean desc, int start, int rows) throws EmployeeException {
        Collection employeeList = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
             employeeList = dao.selectDepartmentList(deptCode, sort, desc, start, rows);
        }
        catch (Exception e) {
            throw new EmployeeException("Error listing departments " + e.toString());
        }

        return employeeList;
    }

    public int getDepartmentListCount(String deptCode) throws EmployeeException {
        int count = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            count = dao.selectDepartmentListCount(deptCode);
        }
        catch (Exception e) {
            throw new EmployeeException("Error counting department " + e.toString());
        }

        return count;
    }
    
    public int getDepartmentCodeCount(String deptCode) throws EmployeeException{
    	int count = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            count = dao.selectDepartmentCodeCount(deptCode);
        }
        catch (Exception e) {
            throw new EmployeeException("Error counting department " + e.toString());
        }

        return count;
    }

    public DepartmentDataObject getDepartment(String deptCode) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        DepartmentDataObject valueObject = null;
        try {
            valueObject = dao.selectDepartment(deptCode);
        }
        catch (Exception e) {
            throw new EmployeeException("Error retrieving department " + e.toString());
        }

        return valueObject;
    }


    public void addServiceClass(DepartmentDataObject service) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.insertServiceClass(service);
        }
        catch (Exception e) {
            throw new EmployeeException("Error adding service classsification " + e.toString());
        }
    }

    public void updateServiceClass(DepartmentDataObject service) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.updateServiceClass(service);
        }
        catch (Exception e) {
            throw new EmployeeException("Error updating service classification " + e.toString());
        }
    }

    public void deleteServiceClass(String key) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            dao.deleteServiceClass(key);
        }
        catch (Exception e) {
            throw new EmployeeException("Error deleting service classification " + key + " " + e.toString());
        }
    }

    public Collection getServiceClassList(String serviceCode, String sort, boolean desc, int start, int rows) throws EmployeeException {
        Collection employeeList = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
             employeeList = dao.selectServiceClassList(serviceCode, sort, desc, start, rows);
        }
        catch (Exception e) {
            throw new EmployeeException("Error listing service classification " + e.toString());
        }

        return employeeList;
    }

    public int getServiceClassListCount(String serviceCode) throws EmployeeException {
        int count = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            count = dao.selectServiceClassListCount(serviceCode);
        }
        catch (Exception e) {
            throw new EmployeeException("Error counting service classification " + e.toString());
        }

        return count;
    }
    
    public int getServiceClassCodeCount(String serviceCode) throws EmployeeException {
        int count = 0;
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            count = dao.selectServiceClassCodeCount(serviceCode);
        }
        catch (Exception e) {
            throw new EmployeeException("Error counting service classification " + e.toString());
        }

        return count;
    }

    public DepartmentDataObject getServiceClass(String serviceCode) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        DepartmentDataObject valueObject = null;
        try {
            valueObject = dao.selectServiceClass(serviceCode);
        }
        catch (Exception e) {
            throw new EmployeeException("Error retrieving service classification " + e.toString());
        }

        return valueObject;
    }

    public Collection getDepartmentList() throws EmployeeException {
        Collection deptList = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            deptList = dao.selectDepartmentList();
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return deptList;
    }

    public Collection getEmployeeReportTo(String employeeID) throws EmployeeException {
        EmployeeDao dao = (EmployeeDao) getDao();
        Collection department = new ArrayList();
        try {
            department = dao.selectEmployeeReportTo(employeeID);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return department;
    }

    public Collection getAllDepartmentEmployees(String deptCode) throws EmployeeException {
        Collection deptList = new ArrayList();
        EmployeeDao dao = (EmployeeDao) getDao();
        try {
            deptList = dao.selectAllDepartmentEmployees(deptCode);
        }
        catch (Exception e) {
            throw new EmployeeException(e.toString());
        }
        return deptList;
    }
}
