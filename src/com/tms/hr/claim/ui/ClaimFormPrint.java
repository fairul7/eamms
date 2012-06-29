package com.tms.hr.claim.ui;

import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;
import com.tms.hr.claim.model.ClaimFormItem;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import com.tms.hr.claim.model.ClaimFormItemModule;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeModule;

import kacang.Application;

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.ui.Event;
import kacang.ui.Widget;

import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.*;
import java.math.BigDecimal;


public class ClaimFormPrint extends Widget {
    private String id;
    private ClaimFormIndex index;
    private Collection items;
    private User originator;
    private User owner;
    private User approver1;
    private User approver2;
    private EmployeeDataObject employee;
    private String department;
    private String designation;
    
    
    //  company name and logo path
    protected String companyName;
    protected String companyLogo;

    //custom
    Map categoryNames;
    Map categoryNames2;
    Map categoryDates;
    
    
    public void init(){
    	
    	
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
    

    public void onRequest(Event event) {
        Application application;
        ClaimFormIndexModule module;
        ClaimFormItemModule itemModule;
        SecurityService ss;
        UuidGenerator uuid = UuidGenerator.getInstance();

        id = event.getParameter("id");

        if (id == null) {
            return;
        }

        try {
            reset();
            application = Application.getInstance();

            module = (ClaimFormIndexModule) application.getModule(ClaimFormIndexModule.class);
            index = module.selectObject(id);

            if (index != null) {
                itemModule = (ClaimFormItemModule) application.getModule(ClaimFormItemModule.class);
                items = itemModule.findObjects(null,new String[] {
                            " formId = '" + id + "' "
                        }, "timeFrom DESC ", false, 0, -1);

                ss = (SecurityService) Application.getInstance().getService(SecurityService.class);

                try {
                    originator = ss.getUser(index.getUserOriginator());
                } catch (SecurityException e) {
                    originator = null;
                }

                try {
                    owner = ss.getUser(index.getUserOwner());
                } catch (SecurityException e) {
                    owner = null;
                }

                try {
                    approver1 = ss.getUser(index.getUserApprover1());
                } catch (SecurityException e) {
                    approver1 = null;
                }

                try {
                    approver2 = ss.getUser(index.getUserApprover2());
                } catch (SecurityException e) {
                    approver2 = null;
                }

                EmployeeModule handler = (EmployeeModule) application.getModule(EmployeeModule.class);

                if ((index.getUserOwner() != null) &&
                        !index.getUserOwner().equals("")) {
                    try {
                        Collection cEmployee = handler.getEmployee(index.getUserOwner());
                        employee = (EmployeeDataObject) cEmployee.iterator()
                                                                 .next();

                        DepartmentDataObject dObj = handler.getEmployeeDepartment(employee.getEmployeeID());
                        department = dObj.getDeptDesc();
                        designation = dObj.getServiceDesc();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            Log.getLog(ClaimFormPrint.class).error("Error printing claim form",
                e);
        }

        //start doing filtering for generating table
        categoryNames = new SequencedHashMap();
        categoryNames2 = new SequencedHashMap();
        categoryDates = new SequencedHashMap();

        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            ClaimFormItem object = (ClaimFormItem) iterator.next();

            //System.out.println("\n"+object.getRemarks()+"  "+ object.getCategoryName()+"  "+object.getAmountInStr());
            Map tempMap = new SequencedHashMap();

            for (Iterator iterator2 = items.iterator(); iterator2.hasNext();) {
                ClaimFormItem object2 = (ClaimFormItem) iterator2.next();

                if (object.getRemarks().equals(object2.getRemarks()) && object.getTimeFrom().compareTo(object2.getTimeFrom()) ==0) {


                    ///////
                    if (!(object2.getCategoryId().equalsIgnoreCase("travel-mileage")) &&
                            !(object2.getCategoryId().equalsIgnoreCase("travel-toll")) &&
                            !(object2.getCategoryId().equalsIgnoreCase("travel-allowance")) &&
                            !(object2.getCategoryId().equalsIgnoreCase("travel-parking"))) {

                        if (!(object2.getCategoryId().equalsIgnoreCase("travel-mileage"))) {
                            tempMap.put(object2.getCategoryId(),
                                "" + object2.getAmount());
                        }
                    }

/*                    else {
                            List mileageList = new ArrayList();
                            mileageList.add(object2.getTravelFrom());
                            mileageList.add(object2.getTravelTo());
                            mileageList.add("" + object2.getQty());
                            mileageList.add("" +
                                new DecimalFormat("0.00").format(
                                    object2.getAmount()));

                            tempMap.put(object2.getCategoryId(), mileageList);
                        }*/
                     /////
                }
            }



        /*  if(categoryNames.get(object.getRemarks()+"*>>"+object.getTimeFrom().getTime()) !=null){




              for (Iterator iterator2 = items.iterator(); iterator2.hasNext();) {
                            ClaimFormItem object2 = (ClaimFormItem) iterator2.next();

                           if (!(object2.getCategoryId().equalsIgnoreCase("travel-mileage"))) {

                             double partAmount = 0;
                               Map tempAddmap = (Map)categoryNames.get(object.getRemarks()+"*>>"+object.getTimeFrom().getTime());
                                         for (Iterator iteratorMap = tempAddmap.entrySet().iterator();
                                           iteratorMap.hasNext();) {
                                             Map.Entry entry = (Map.Entry) iteratorMap.next();
                                               String key = (String) entry.getKey();
                                               String value = (String) entry.getValue();
                                                partAmount = Double.parseDouble(value);
                                         }
                              // BigDecimal totalAmount= partAmount.add(object2.getAmount());
                               double totalAmount = partAmount + object2.getAmount().doubleValue();

                            tempMap.put(object2.getCategoryId(),
                                "" + totalAmount);
                        }
              }




          }*/


            categoryNames.put(object.getRemarks()+"*>>"+object.getTimeFrom().getTime(), tempMap);
            categoryDates.put(object.getRemarks()+"*>>"+object.getTimeFrom().getTime(),

                new SimpleDateFormat("yyyy-MM-dd").format(object.getTimeFrom()));



        }

        //do filtering of categoryNames, put null for empty category
        ClaimFormItemCategoryModule catModule = (ClaimFormItemCategoryModule) Application.getInstance().getModule(ClaimFormItemCategoryModule.class);
        ClaimFormIndexModule remarkModule = (ClaimFormIndexModule)Application.getInstance().getModule(ClaimFormIndexModule.class);
        Vector colCat = new Vector(catModule.selectObjectsIgnoreDefault("status", "act", 0, -1));
        String[] categoryList = new String[colCat.size()];
        
        
        
        
        
        
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            ClaimFormItem object = (ClaimFormItem) iterator.next();

            Map tempMap = new SequencedHashMap();

            for (Iterator iterator2 = items.iterator(); iterator2.hasNext();) {
                ClaimFormItem object2 = (ClaimFormItem) iterator2.next();

            //    if (object.getId().equals(object2.getId())) {
               if (object.getRemarks().equals(object2.getRemarks()) && object.getTimeFrom().compareTo(object2.getTimeFrom()) ==0) {
                    if (object2.getCategoryId().equalsIgnoreCase("travel-toll") ||
                            object2.getCategoryId().equalsIgnoreCase("travel-allowance") ||
                            object2.getCategoryId().equalsIgnoreCase("travel-parking")) {
                        tempMap.put(object2.getCategoryId(),
                            "" + object2.getAmount());
                    }

                    if (object2.getCategoryId().equalsIgnoreCase("travel-mileage")) {
                        List mileageList = new ArrayList();
                        mileageList.add(object2.getTravelFrom());
                        mileageList.add(object2.getTravelTo());
                        mileageList.add("" +
                            new DecimalFormat("0").format(object2.getQty()));
                        mileageList.add("" +
                            new DecimalFormat("0.00").format(
                                object2.getAmount()));

                        tempMap.put(object2.getCategoryId(), mileageList);
                    }

                    if (tempMap.get("travel-allowance") == null) {
                        tempMap.put("travel-allowance", null);
                    }

                    if (tempMap.get("travel-toll") == null) {
                        tempMap.put("travel-toll", null);
                    }

                    if (tempMap.get("travel-parking") == null) {
                        tempMap.put("travel-parking", null);
                    }

                    if (tempMap.get("travel-mileage") == null) {
                        List mileageList = new ArrayList();
                        mileageList.add("");
                        mileageList.add("");
                        mileageList.add("");
                        mileageList.add("0");

                        tempMap.put("travel-mileage", mileageList);
                    }



                    categoryNames2.put(object.getRemarks()+"*>>"+object.getTimeFrom().getTime(), tempMap);



                }
            }
        }

        //refiltered sequence
        for (Iterator iterator = categoryNames2.entrySet().iterator();
             iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();

            if (entry.getValue() instanceof Map) {
                Map tempMap = (Map) entry.getValue();
                String allowance = "";
                String toll = "";
                String parking = "";
                List mileage = null;

                if (tempMap.size() > 1) {
                    allowance = (String) tempMap.get("travel-allowance");
                    toll = (String) tempMap.get("travel-toll");
                    parking = (String) tempMap.get("travel-parking");
                    mileage = (List) tempMap.get("travel-mileage");

                    //    categoryNames.put(key, arrangeMap);
                    Map temporaryMap = new SequencedHashMap();

                    temporaryMap.put("travel-allowance", allowance);
                    temporaryMap.put("travel-toll", toll);
                    temporaryMap.put("travel-parking", parking);
                    temporaryMap.put("travel-mileage", mileage);

                    entry.setValue(temporaryMap);
                }
            }
        }


    }

    // === [ getters/setters ] =================================================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClaimFormIndex getIndex() {
        return index;
    }

    public void setIndex(ClaimFormIndex index) {
        this.index = index;
    }

    public Collection getItems() {
        return items;
    }

    public void setItems(Collection items) {
        this.items = items;
    }

    public User getOriginator() {
        return originator;
    }

    public void setOriginator(User originator) {
        this.originator = originator;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getApprover1() {
        return approver1;
    }

    public void setApprover1(User approver1) {
        this.approver1 = approver1;
    }

    public User getApprover2() {
        return approver2;
    }

    public void setApprover2(User approver2) {
        this.approver2 = approver2;
    }

    private void reset() {
        index = null;
        items = null;
        originator = null;
        owner = null;
        approver1 = null;
        approver2 = null;
    }

    public void setEmployee(EmployeeDataObject employee) {
        this.employee = employee;
    }

    public EmployeeDataObject getEmployee() {
        return employee;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public Map getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(Map categoryNames) {
        this.categoryNames = categoryNames;
    }

    public Map getCategoryDates() {
        return categoryDates;
    }

    public void setCategoryDates(Map categoryDates) {
        this.categoryDates = categoryDates;
    }

    public Map getCategoryNames2() {
        return categoryNames2;
    }

    public void setCategoryNames2(Map categoryNames2) {
        this.categoryNames2 = categoryNames2;
    }

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
