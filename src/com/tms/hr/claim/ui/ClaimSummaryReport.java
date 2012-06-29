package com.tms.hr.claim.ui;

import com.tms.collab.timesheet.TimeSheetUtil;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;

import com.tms.hr.claim.model.*;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeModule;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.ui.Event;
import kacang.ui.Widget;

import org.apache.commons.collections.SequencedHashMap;

import java.util.*;
import java.text.DecimalFormat;


/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 8, 2005
 * Time: 4:08:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimSummaryReport extends Widget {
    protected EmployeeDataObject[] employees;
    protected ClaimFormItemCategory[] categories;
    protected String[] categoryNames;
    protected Collection[] expensesList;
    protected String[] expensesTotal;
    protected String[] categoryTotal;
    protected String grandTotal;
    protected String month;
    protected String selectedEmployee;
    protected String title;
    
    //company name and logo path
    protected String companyName;
    protected String companyLogo;

    //customize
    protected Map typeName;
    protected Map typeCategories;
    protected Map countCategoriesMap;
    protected String[] expenses;
    protected String countColumn;
    protected Collection col = null;

    public String getDefaultTemplate() {
        return "claims/claimSummaryReport";
    }

    public void init() {
        employees = null;
        categories = null;
        categoryNames = null;
        expensesList = null;
        categoryTotal = null;
        expensesTotal = null;
        grandTotal = "0.0";
        
        //grab company name and log
        SetupModule setup = (SetupModule) Application.getInstance()
        .getModule(SetupModule.class);
         try {
			companyName = setup.get("siteName");
		} catch (SetupException e) {
			
			//company name is not entered at the setup page
		}
		
		 try {
				companyLogo = setup.get("siteLogo");
			} catch (SetupException e) {
				
				//company name is not entered at the setup page
			}
			
        
    }

    public void onRequest(Event ev) {
        if ((month == null) || month.equals("")) {
            int iMonth = Calendar.getInstance().get(Calendar.MONTH);

            if (iMonth < 10) {
                month = "0" + iMonth;
            } else {
                month = "" + iMonth;
            }
        }

        populateDetails();
    }

    public void populateDetails() {
        grandTotal = "0.0";

        Application app = Application.getInstance();
        EmployeeModule empMod = (EmployeeModule) app.getModule(EmployeeModule.class);
        ClaimFormItemCategoryModule mod = (ClaimFormItemCategoryModule) app.getModule(ClaimFormItemCategoryModule.class);

        try {
            int iCounter = 0;

            if ((selectedEmployee == null) || selectedEmployee.equals("")) {
                col = empMod.getAllEmployees();

                if ((col != null) && (col.size() > 0)) {
                    employees = new EmployeeDataObject[col.size()];
                    expensesList = new Collection[col.size()];
                    expensesTotal = new String[col.size()];

                    iCounter = 0;

                    for (Iterator i = col.iterator(); i.hasNext();) {
                        employees[iCounter] = (EmployeeDataObject) i.next();

                        String firstName = employees[iCounter].getFirstName();
                        String lastName = employees[iCounter].getLastName();
                        Collection empCol = empMod.getEmployee(employees[iCounter].getEmployeeID());
                        employees[iCounter] = (EmployeeDataObject) empCol.iterator()
                                                                         .next();
                        employees[iCounter].setFirstName(firstName);
                        employees[iCounter].setLastName(lastName);
                        iCounter++;
                    }
                }
            } else {
                employees = new EmployeeDataObject[1];
                expensesList = new Collection[1];
                expensesTotal = new String[1];

                try {
                    Collection c = empMod.getEmployee(selectedEmployee);
                    employees[0] = (EmployeeDataObject) c.iterator().next();

                    SecurityService ss = (SecurityService) Application.getInstance()
                                                                      .getService(SecurityService.class);
                    User user = ss.getUser(selectedEmployee);
                    employees[0].setFirstName(user.getName());
                    iCounter = 1;
                } catch (Exception e) {
                }
            }

            //get type first
            Collection typeCol = mod.countTotalType();
            ClaimFormItemModule itemMod = (ClaimFormItemModule) Application.getInstance()
                                                                           .getModule(ClaimFormItemModule.class);
            ClaimConfigTypeModule configModule = (ClaimConfigTypeModule) Application.getInstance()
                                                                                    .getModule(ClaimConfigTypeModule.class);
            int countType = 1;
            int countCategories = 0;
            int countValue = 0;

            typeName = new SequencedHashMap();
            typeCategories = new SequencedHashMap();
            countCategoriesMap = new SequencedHashMap();
            typeName.put("0", "    ");

            //do the default type first
            Collection coltetDefault = mod.query(new String[] {
                        "status='act'", "type='" + "default" + "'"
                    }, "");

            countCategoriesMap.put(String.valueOf(0),
                String.valueOf(coltetDefault.size()-4));

            if (iCounter <= 1) {
                expenses = new String[1 * (coltetDefault.size() +
                    typeCol.size())];
            } else {
                expenses = new String[col.size() * (coltetDefault.size() +
                    typeCol.size()-4)];
            }

            countColumn = String.valueOf(coltetDefault.size() + typeCol.size()-4);

            for (Iterator iterator = coltetDefault.iterator();
                 iterator.hasNext();) {
                ClaimFormItemCategory object = (ClaimFormItemCategory) iterator.next();


                if (!("default".equals(object.getId())) &&
                        !("travel-mileage".equals(object.getId())) &&
                        !("travel-toll".equals(object.getId())) &&
                        !("travel-parking".equals(object.getId())))
                { typeCategories.put(String.valueOf(countCategories),
                    (String) object.getCode()== null ? "" : (String) object.getCode() +"\n "+(String) object.getName());
                countCategories++;
                }
            }


            for (int i = 0; i < iCounter; i++) {

                      double tempexpensesTotal=0.0;
                for (Iterator iterator = coltetDefault.iterator();
                     iterator.hasNext();) {
                    ClaimFormItemCategory object = (ClaimFormItemCategory) iterator.next();

                    double something = 0.0;

                    if (!(object.getId().equals("default")) &&
                            !(object.getId().equals("travel-mileage")) &&
                            !(object.getId().equals("travel-parking")) &&
                            !(object.getId().equals("travel-toll"))) {
                        something = itemMod.getTotalByMonthAndCategory(employees[i].getEmployeeID(),
                                object.getId(), "default", month);

                   if(something <0)
                   expenses[countValue++] = "("+new DecimalFormat("0.00").format(something).substring(1,new DecimalFormat("0.00").format(something).length())+")";
                   else
                    expenses[countValue++] = new DecimalFormat("0.00").format(something);


                    tempexpensesTotal += something;

                           }
                }


                if(tempexpensesTotal <0)
                expensesTotal[i] = "("+new Double(tempexpensesTotal).toString().substring(1,new Double(tempexpensesTotal).toString().length())+")";
                else
                expensesTotal[i] = new Double(tempexpensesTotal).toString();

                //do other besides "default"
                for (Iterator iterator = typeCol.iterator();
                     iterator.hasNext();) {
                    ClaimTypeObject object = (ClaimTypeObject) iterator.next();

                    Collection coltet = mod.query(new String[] {
                                "status='act'",
                                "type='" + object.getTypeName() + "'"
                            }, "");

                    ClaimTypeObject type = null;

                    if (!(object.getTypeName().equals("default"))) {
                        countCategoriesMap.put(String.valueOf(countType),
                            String.valueOf(coltet.size() ));
                    } else {

                        if (!("default".equals(object.getId())) &&
                        !("travel-mileage".equals(object.getId())) &&
                        !("travel-toll".equals(object.getId())) &&
                        !("travel-parking".equals(object.getId()))) {


                        countCategoriesMap.put(String.valueOf(countType),
                            String.valueOf(coltet.size()));

                        }

                    }


                    if (!(object.getTypeName().equals("default"))) {

                        double something = itemMod.getTotalByMonthAndCategory(employees[i].getEmployeeID(),
                                "travel-allowance", object.getId(), month);


                        double something2 = itemMod.getTotalByMonthAndCategory(employees[i].getEmployeeID(),
                                "travel-mileage", object.getId(), month);


                        double something3 = itemMod.getTotalByMonthAndCategory(employees[i].getEmployeeID(),
                                "travel-parking", object.getId(), month);


                        double something4 = itemMod.getTotalByMonthAndCategory(employees[i].getEmployeeID(),
                                "travel-toll", object.getId(), month);


                        double value = 0.0;
                        value = something + something2 + something3 +
                            something4;

                       if(value<0)
                        {expenses[countValue++] = "("+new DecimalFormat("0.00").format(value).substring(1,new DecimalFormat("0.00").format(value).length())+")";
                         System.out.println("test"+new DecimalFormat("0.00").format(value).substring(1,new DecimalFormat("0.00").format(value).length()));
                        }
                        else
                        expenses[countValue++] = new DecimalFormat("0.00").format(value);


                       // expensesTotal[i] += value;

                         tempexpensesTotal += value;




                    }





                    if (i == 0) {
                        typeName.put(String.valueOf(countType),
                            ( object.getAccountcode() == null ? "" : object.getAccountcode())+"\n"+object.getTypeName());


                        countType++;

                    }
                }


                if(tempexpensesTotal <0)
                expensesTotal[i] = "("+new DecimalFormat("0.00").format(tempexpensesTotal).substring(1,new DecimalFormat("0.00").format(tempexpensesTotal).length())+")";
                  else
               expensesTotal[i] = new DecimalFormat("0.00").format(tempexpensesTotal);



            }

            // get all category
            col = mod.query(new String[] { "status='act'" }, "");

            ClaimConfigTypeModule configTypeModule = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);


            if ((col != null) && (col.size() > 0)) {
                categories = new ClaimFormItemCategory[col.size()];
                categoryNames = new String[col.size()];
                categoryTotal = new String[Integer.parseInt(countColumn)];
                iCounter = 0;

                for (Iterator i = col.iterator(); i.hasNext();) {
                    categories[iCounter] = (ClaimFormItemCategory) i.next();
                    categoryNames[iCounter] = categories[iCounter].getName();
                    iCounter++;
                }
            }



            // get total for each employees
            for (int i = 0; i < employees.length; i++) {
                expensesList[i] = getExpensesList(employees[i], categories, i);
            }
        } catch (Exception e) {
        }

        //do loop through all the expenses and get sum of each row
        double tempGrandTotal=0.0;
        if(countColumn==null) countColumn="0";
        for (int ii = 0; ii < (Integer.parseInt(countColumn)); ii++) {
            categoryTotal[ii] = "0.00"; //reset current
              double tempCategoryTotal=0.0;
            for (int iii = ii; iii <= (expenses.length);
                 iii += (Integer.parseInt(countColumn))) {

                if (iii < expenses.length  && expenses[iii ]!=null ) {
               //     categoryTotal[ii] += expenses[iii];
                   if(expenses[iii].charAt(0)=='(' )
                    tempCategoryTotal += -(Double.parseDouble(expenses[iii].substring(1,expenses[iii].length()-1)));
                    else
                    tempCategoryTotal += Double.parseDouble(expenses[iii]);
                }



            }
            if(tempCategoryTotal <0)
            categoryTotal[ii] = "("+new DecimalFormat("0.00").format(tempCategoryTotal).substring(1,new DecimalFormat("0.00").format(tempCategoryTotal).length())+")";
            else
            categoryTotal[ii] = new DecimalFormat("0.00").format(tempCategoryTotal);


            tempGrandTotal += new Double(tempCategoryTotal).doubleValue();

            //grandTotal += categoryTotal[ii];
        }

        if(tempGrandTotal <0 )
        grandTotal = "("+new DecimalFormat("0.00").format(tempGrandTotal).substring(1,new DecimalFormat("0.00").format(tempGrandTotal).length())+")";
        else
        grandTotal =new DecimalFormat("0.00").format(tempGrandTotal);

    }

    // return expenses total for an employee
    public Collection getExpensesList(EmployeeDataObject obj,
                                      ClaimFormItemCategory[] array, int iCurrent) {
        ClaimFormItemCategoryModule mod = (ClaimFormItemCategoryModule) Application.getInstance()
                                                                                   .getModule(ClaimFormItemCategoryModule.class);
        ClaimFormItemModule itemMod = (ClaimFormItemModule) Application.getInstance()
                                                                       .getModule(ClaimFormItemModule.class);

        List list = new ArrayList();

        for (int i = 0; i < array.length; i++) {
            if (array[i].getType().equals("travel")) {
                //for travel type, get from travel details
                if (mod.dependencyExisted(array[i].getId(), obj.getDeptCode())) {

                } else {
                    list.add("0.0");
                }
            } else {

            }
        }

        return list;
    }

    public EmployeeDataObject[] getEmployees() {
        return employees;
    }

    public void setEmployees(EmployeeDataObject[] employees) {
        this.employees = employees;
    }

    public ClaimFormItemCategory[] getCategories() {
        return categories;
    }

    public void setCategories(ClaimFormItemCategory[] categories) {
        this.categories = categories;
    }

    public String[] getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String[] categoryNames) {
        this.categoryNames = categoryNames;
    }

    public Collection[] getExpensesList() {
        return expensesList;
    }

    public void setExpensesList(Collection[] expensesList) {
        this.expensesList = expensesList;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;

        String syear = month.substring(0, month.indexOf("-"));
        String smonth = month.substring(month.indexOf("-") + 1, month.length());
        int iMonth = Integer.parseInt(smonth);
        title = Application.getInstance().getMessage("claims.label.summaryReportFor","Summary Report for the month") +": " +
            TimeSheetUtil.getMonthDescription(iMonth) + " " + syear;

        //populateDetails();
    }

    public String[] getExpensesTotal() {
        return expensesTotal;
    }

    public void setExpensesTotal(String[] expensesTotal) {
        this.expensesTotal = expensesTotal;
    }

    public String[] getCategoryTotal() {
        return categoryTotal;
    }

    public void setCategoryTotal(String[] categoryTotal) {
        this.categoryTotal = categoryTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setSelectedEmployee(String selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
        populateDetails();
    }

    public String getSelectedEmployee() {
        return selectedEmployee;
    }

    public String getTitle() {
        return title;
    }

    public Map getTypeName() {
        return typeName;
    }

    public void setTypeName(Map typeName) {
        this.typeName = typeName;
    }

    public Map getTypeCategories() {
        return typeCategories;
    }

    public void setTypeCategories(Map typeCategories) {
        this.typeCategories = typeCategories;
    }

    public Map getCountCategoriesMap() {
        return countCategoriesMap;
    }

    public void setCountCategoriesMap(Map countCategoriesMap) {
        this.countCategoriesMap = countCategoriesMap;
    }

    public String[] getExpenses() {
        return expenses;
    }

    public void setExpenses(String[] expenses) {
        this.expenses = expenses;
    }

    public String getCountColumn() {
        return countColumn;
    }

    public void setCountColumn(String countColumn) {
        this.countColumn = countColumn;
    }

	
    //grab company name and logo
    public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
    
}
