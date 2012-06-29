package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.claim.model.ClaimFormItemCategory;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeException;
import com.tms.hr.employee.model.EmployeeModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportForm extends Form {
    private Panel pnlRow1, pnlRow2, pnlRow3;
    private SelectBox sbMonth, sbYear;
    private SelectBox sbEmployee, sbDepartment;
    private SelectBox sbCategory;

    public void init() {
        super.init();

        setWidth("100%");
        setMethod("POST");

        pnlRow1 = new Panel("pnlRow1");
        pnlRow1.addChild(new Label("lblMonth", "Month"));
        sbMonth = new SelectBox("sbMonth", getMonthMap(), ClaimFormIndexActionForm.getCurrentMonthMap());
        pnlRow1.addChild(sbMonth);

        pnlRow1.addChild(new Label("lblYear", "Year"));
        sbYear = new SelectBox("sbYear", getYearMap(), ClaimFormIndexActionForm.getCurrentYearMap());
        pnlRow1.addChild(sbYear);

        pnlRow2 = new Panel("pnlRow2");
        pnlRow2.addChild(new Label("lblEmployee", "Employee"));
        sbEmployee = new SelectBox("sbEmployee", getEmployeeMap(), getEmptyMap());
        pnlRow2.addChild(sbEmployee);

        pnlRow2.addChild(new Label("lblDepartment", "Department"));
        sbDepartment = new SelectBox("sbDepartment", getDepartmentMap(), getEmptyMap());
        pnlRow2.addChild(sbDepartment);

        pnlRow3 = new Panel("pnlRow3");
        pnlRow3.addChild(new Label("lblCategory", "Category"));
        sbCategory = new SelectBox("sbCategory", getCategoryMap(false), getEmptyMap());
        sbCategory.setMultiple(true);
        sbCategory.setRows(5);
        pnlRow3.addChild(sbCategory);

        setColumns(1);

        addChild(pnlRow1);
        addChild(pnlRow2);
        addChild(pnlRow3);

        addChild(new Button("btnGenerate", "Generate"));
    }

    public Forward onValidate(Event evt) {
        String monthStr, yearStr;
        int month, year;
        Calendar cal;
        Date date;
        String employeeId, departmentId;
        List categoryList;

        try {
            monthStr = (String) ((List) sbMonth.getValue()).get(0);
            yearStr = (String) ((List) sbYear.getValue()).get(0);
            month = Integer.parseInt(monthStr) - 1;
            year = Integer.parseInt(yearStr);
            cal = Calendar.getInstance();
            cal.set(year, month, 1, 0, 0, 0);
            date = cal.getTime();

            employeeId = (String) ((List) sbEmployee.getValue()).get(0);
            departmentId = (String) ((List) sbDepartment.getValue()).get(0);
            categoryList = (List) sbCategory.getValue();

            generateReport(evt.getRequest(), date, employeeId, departmentId, categoryList);

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    private void generateReport(HttpServletRequest request, Date date, String employeeId, String departmentId, List categoryList) {
        List reportList;
        ClaimFormIndexModule module;
        Map categoryMap;
        StringBuffer content;

        try {
            module = (ClaimFormIndexModule) Application.getInstance().getModule(ClaimFormIndexModule.class);

            reportList = module.generateReport(date, employeeId, departmentId, categoryList);
            categoryMap = getCategoryMap(false);

            // loop and loop to generate report
            content = new StringBuffer();
            content.append("No, Employee Name, Department");

            for (Iterator iterator = categoryMap.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();

                if (categoryList==null || categoryList.size()==0 || categoryList.contains(key)) {
                    content.append(", ");
                    content.append(escapeCsv((String) categoryMap.get(key)));
                }
            }

            List reportProjectList = module.generateProjectReport(date, employeeId, departmentId);

            content.append(", Projects, Total");
            content.append("\n");

            // get a list of username (rows to display)
            List usernameList;
            usernameList = new ArrayList();
            for (int i = 0; i < reportList.size(); i++) {
                Map rowMap = (Map) reportList.get(i);
                String username = (String) rowMap.get("username");
                if (!usernameList.contains(username)) {
                    usernameList.add(username);
                }
            }

            // for each username, list their data (row)
            NumberFormat nf = NumberFormat.getCurrencyInstance();

            for (int i = 0; i < usernameList.size(); i++) {
                String username = (String) usernameList.get(i);
                Number total = new Double(0);

                // get department
                String department = "";
                for (int j = 0; j < reportList.size(); j++) {
                    Map rowMap = (Map) reportList.get(j);
                    String u2 = (String) rowMap.get("username");

                    if (username.equals(u2)) {
                        department = (String) rowMap.get("department");
                        break;
                    }
                }

                content.append(i + 1);
                content.append(", " + escapeCsv(username));
                content.append(", " + escapeCsv(department));

                // for each category, find the value in reportList
                for (Iterator iterator = categoryMap.keySet().iterator(); iterator.hasNext();) {
                    String category = (String) iterator.next();
                    String valStr = "$0.00";

                    if (categoryList!=null && categoryList.size()!=0 && !categoryList.contains(category)) {
                        continue;
                    }

                    // find in reportList
                    for (int j = 0; j < reportList.size(); j++) {
                        Map rowMap = (Map) reportList.get(j);
                        String u2 = (String) rowMap.get("username");

                        if (username.equals(u2)) {
                            // user match, continue to match category
                            String cat = (String) rowMap.get("catId");
                            if (category.equals(cat)) {
                                // category match, get value
                                Number amt = (Number) rowMap.get("amount");
                                total = new Double(total.doubleValue() + amt.doubleValue());
                                valStr = nf.format(amt);
                                break;  // move on
                            }
                        }
                    }
                    content.append(", ");
                    content.append(escapeCsv(valStr));
                }

                // add project total column
                String valStr = "$0.00";
                for (int j = 0; j < reportProjectList.size(); j++) {
                    Map rowMap = (Map) reportProjectList.get(j);
                    if (username.equals(rowMap.get("username"))) {
                        // username match, now get project total
                        Number amt = (Number) rowMap.get("amount");
                        total = new Double(total.doubleValue() + amt.doubleValue());
                        valStr = nf.format(amt);
                        break;  // move on
                    }
                }
                content.append(", ");
                content.append(escapeCsv(valStr));

                content.append(", ");
                content.append(escapeCsv(nf.format(total)));
                content.append("\n");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
            String reportTitle = "Claims Report For " + sdf.format(date) + "\n\n";
            String reportFooter = "\n\nEnd of Report.";

            request.setAttribute("exportedData", reportTitle + content.toString() + reportFooter);

        } catch (Exception e) {
            Log.getLog(ReportForm.class).error("Error generating report", e);
            request.setAttribute("exportedData", "An error occurred while generating report");
        }

    }

    private static Map emptyMap = new HashMap();

    private Map getEmptyMap() {
        return emptyMap;
    }

    private Map getCategoryMap() {
        return getCategoryMap(true);
    }

    private Map getCategoryMap(boolean select) {
        ClaimFormItemCategoryModule module;
        Map categoryMap = new SequencedHashMap();

        module = (ClaimFormItemCategoryModule) Application.getInstance().getModule(ClaimFormItemCategoryModule.class);
        Vector colCat = new Vector(module.selectObjects("status", "act", 0, -1));
        if (select) {
            categoryMap.put("", "All Categories");
        }
        for (int cnt1 = 0; cnt1 < colCat.size(); cnt1++) {
            ClaimFormItemCategory valObj = (ClaimFormItemCategory) colCat.get(cnt1);
            categoryMap.put(valObj.getId(), valObj.getName());
        }

        return categoryMap;
    }

    private Map getDepartmentMap() {
        Map departmentMap = new SequencedHashMap();
        EmployeeModule em;
        Collection col;

        em = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
        departmentMap.put("", "--Please Select--");
        try {
            col = em.getDepartmentList();
            for (Iterator i = col.iterator(); i.hasNext();) {
                DepartmentDataObject o = (DepartmentDataObject) i.next();
                departmentMap.put(o.getDeptCode(), o.getDeptDesc());
            }

        } catch (EmployeeException e) {
            Log.getLog(ReportForm.class).error("Error getting department Map", e);
        }

        return departmentMap;
    }

    private Map getEmployeeMap() {
        Map employeeMap = new SequencedHashMap();
        EmployeeModule em;
        Collection col;

        em = (EmployeeModule) Application.getInstance().getModule(EmployeeModule.class);
        try {
            col = em.getAllEmployees();
            employeeMap.put("", "--Please Select--");
            for (Iterator i = col.iterator(); i.hasNext();) {
                EmployeeDataObject o = (EmployeeDataObject) i.next();
                employeeMap.put(o.getEmployeeID(), o.getFullName());
            }

        } catch (EmployeeException e) {
            Log.getLog(ReportForm.class).error("Error getting employee Map", e);
        }

        return employeeMap;
    }

    private Map getYearMap() {
        return ClaimFormIndexActionForm.getYearMap();
    }

    private Map getMonthMap() {
        return ClaimFormIndexActionForm.getMonthMap();
    }

    /**
     * TODO: refactor to util class
     * Method to convert a string into a CSV string.
     *
     * @param str String object to convert to CSV format
     * @return CSV format string
     */
    public static String escapeCsv(String str) {
        StringBuffer buffer = new StringBuffer();

        for (int i =0; i < str.length(); i++) {
            if (str.charAt(i) == '"' ||str.charAt(i) == '\\') {
                buffer.append("\\");
            }
            buffer.append(str.charAt(i));
        }


        return buffer.toString();
    }

}
