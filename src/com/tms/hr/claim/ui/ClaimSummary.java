package com.tms.hr.claim.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.stdui.SelectBox;
import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.Label;
import com.tms.hr.claim.model.ClaimFormItemCategory;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import com.tms.hr.claim.model.ClaimFormItemModule;
import com.tms.collab.timesheet.TimeSheetUtil;

import java.util.*;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 7, 2005
 * Time: 1:35:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimSummary extends Form {

    protected String userId;
    protected String[] categoryName;
    protected String[] monthName;
    protected Collection[] monthList;
    protected Collection[] monthListStr;
    protected double[] categoryTotal;
    protected String[] categoryTotalStr;
    protected double[] monthTotal={0,0,0,0,0,0,0,0,0,0,0,0};
    protected String[] monthTotalStr ={"0","0","0","0","0","0","0","0","0","0","0","0"};
    protected double grandTotal=0.0;
    protected String grandTotalStr="0.0";
    protected SelectBox sbSelect;
    protected Button bnSubmit;
    protected String year="";

    protected String act;

    public void init(){
        setColumns(2);
        initForm();
    }

    public void initForm() {
        removeChildren();
        sbSelect = new SelectBox("sbSelect");
        int iYear = Calendar.getInstance().get(Calendar.YEAR);
        sbSelect.addOption(""+iYear,""+iYear);
        sbSelect.addOption(""+(iYear-1),""+(iYear-1));
        sbSelect.addOption(""+(iYear-2),""+(iYear-2));
        sbSelect.setSelectedOption(""+iYear);
        addChild(new Label("l1","Select a year"));
        addChild(sbSelect);

        bnSubmit=new Button("bnSubmit","Submit");
        addChild(new Label("l2",""));
        addChild(bnSubmit);
    }

    public void onRequest() {
        if (act!=null && act.equals("summary")) {
            getSummary();
        }
        else {
            initForm();
        }



    }

    public String getDefaultTemplate() {
        if (act!=null && act.equals("summary")) {
            return "claims/claimSummary";
        }
        return super.getDefaultTemplate();
    }

    public Forward onValidate(Event ev) {
        if (bnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            String year = (String)sbSelect.getSelectedOptions().keySet().iterator().next();
            return new Forward("getSummary","summary.jsp?act=summary&year="+year,true);
        }
        return null;
    }

    public void getSummary() {
        if (userId==null || userId.equals("")) {
                userId = getWidgetManager().getUser().getId();
        }
        getAllMonthName();
        ClaimFormItemCategoryModule mod = (ClaimFormItemCategoryModule)Application.getInstance().getModule(ClaimFormItemCategoryModule.class);

        // get all categoryList
        Collection col = mod.selectObjects("status","act",0,-1);
        if (col!=null && col.size()>0) {
            int iCounter=0;
/*          categoryName = new String[col.size()+4];
            monthList = new Collection[col.size()+4];
            categoryTotal=new double[col.size()+4];*/
            categoryName = new String[col.size()];
            monthList = new Collection[col.size()];
            monthListStr= new Collection[col.size()];
            categoryTotal=new double[col.size()];
            categoryTotalStr = new String[col.size()];
            monthTotal=new double[]{0,0,0,0,0,0,0,0,0,0,0,0};
            grandTotal=0.0;

            for (Iterator i=col.iterator();i.hasNext();) {
                ClaimFormItemCategory obj = (ClaimFormItemCategory)i.next();

                if(!("default".equalsIgnoreCase(obj.getName()))  && !("parking".equalsIgnoreCase(obj.getName()))  && !("toll".equalsIgnoreCase(obj.getName()))  &&  !("allowance".equalsIgnoreCase(obj.getName())) && !("mileage".equalsIgnoreCase(obj.getName())))
                { categoryName[iCounter] = obj.getName();

                  monthList[iCounter]=getMonthlyTotal(obj.getId(),iCounter);
                  iCounter++;
                 }

            }

            categoryName[iCounter]="Allowance";
            monthList[iCounter]=getMonthlyTotal(ClaimFormItemCategoryModule.TRAVEL_ALLOWANCE,iCounter);


            iCounter++;
            categoryName[iCounter]="Parking";
            monthList[iCounter]=getMonthlyTotal(ClaimFormItemCategoryModule.TRAVEL_PARKING,iCounter);


            iCounter++;
            categoryName[iCounter]="Toll";
            monthList[iCounter]=getMonthlyTotal(ClaimFormItemCategoryModule.TRAVEL_TOLL,iCounter);



            iCounter++;
            categoryName[iCounter]="Mileage";
            monthList[iCounter]=getMonthlyTotal(ClaimFormItemCategoryModule.TRAVEL_MILEAGE,iCounter);



          //  iCounter++;
        }


            //do double to str

             for(int i =0 ; i< monthList.length ; i++){
                  List tempList = new ArrayList();

                     tempList.clear();
                   for (Iterator icount = monthList[i].iterator(); icount.hasNext(); ) {


                      String tempValue = (String)icount.next();
                      tempValue = new DecimalFormat("0.00").format(new Double(tempValue));


                      if(tempValue.charAt(0)=='-')
                      tempList.add(  "("+tempValue.substring(1,tempValue.length())+")");
                      else
                      tempList.add(""+tempValue);





               }

                 monthListStr[i] =(List) tempList;

           }



           for(int i=0; i< categoryTotal.length; i++)
           {
               String tempValue = "";
               tempValue = new DecimalFormat("0.00").format(categoryTotal[i]);
               if(tempValue !=null && tempValue.charAt(0)=='-')
               categoryTotalStr[i] = "("+tempValue.substring(1,tempValue.length())+")";
               else
               categoryTotalStr[i] = tempValue;

           }

           for(int i=0 ; i< monthTotal.length ; i++){
               String tempValue="";
               tempValue = new DecimalFormat("0.00").format(monthTotal[i]);
               if(tempValue !=null && tempValue.charAt(0)=='-')
               monthTotalStr[i] = "("+tempValue.substring(1,tempValue.length())+")";
               else
               monthTotalStr[i] = tempValue;

           }


           String tempGrandTotal="";
           tempGrandTotal = new DecimalFormat("0.00").format(grandTotal);
           if(tempGrandTotal !=null && tempGrandTotal.charAt(0)=='-')
           grandTotalStr = "("+tempGrandTotal.substring(1,tempGrandTotal.length())+")";
           else
           grandTotalStr = tempGrandTotal;









           }

           public List getMonthlyTotal(String categoryId,int iC) {
        ClaimFormItemModule itemModule = (ClaimFormItemModule)Application.getInstance().getModule(ClaimFormItemModule.class);

        int currentYear = (year!=null&&!year.equals(""))?Integer.parseInt(year):Calendar.getInstance().get(Calendar.YEAR);
        year = ""+currentYear;

        String monthStr="";
        List total = new ArrayList();
        categoryTotal[iC]=0;
        for (int j=0;j<12;j++) {
            if (j<9) {
                monthStr = currentYear + "-0"+(j+1);
            }
            else {
                monthStr = currentYear+"-"+(j+1);
            }
            double dbl = itemModule.getTotalByMonthAndCategory(userId,categoryId,monthStr);
            total.add(""+dbl);
            categoryTotal[iC]+=dbl;
            monthTotal[j]+=dbl;
            grandTotal+=dbl;
        }
        return total;
    }

    public void getAllMonthName() {
        monthName = new String[12];
        for (int i=0;i<12;i++) {
            monthName[i] = TimeSheetUtil.getMonthDescription((i+1));
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setYear(String s) {
        year=s;
    }
    public String getYear() {
        return year;
    }

    public String[] getCategoryName() {
        return categoryName;
    }

    public String[] getMonthName() {
        return monthName;
    }

    public Collection[] getMonthList() {
        return monthList;
    }

    public Button getBnSubmit() {
        return bnSubmit;
    }

    public SelectBox getSbSelect() {
        return sbSelect;
    }

    public void setAct(String s) {
        act=s;
        getSummary();
    }

    public String getAct() {
        return act;
    }

    public double[] getCategoryTotal() {
        return categoryTotal;
    }

    public double[] getMonthTotal() {
        return monthTotal;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public String[] getCategoryTotalStr() {
        return categoryTotalStr;
    }

    public void setCategoryTotalStr(String[] categoryTotalStr) {
        this.categoryTotalStr = categoryTotalStr;
    }

    public Collection[] getMonthListStr() {
        return monthListStr;
    }

    public void setMonthListStr(Collection[] monthListStr) {
        this.monthListStr = monthListStr;
    }

    public String[] getMonthTotalStr() {
        return monthTotalStr;
    }

    public void setMonthTotalStr(String[] monthTotalStr) {
        this.monthTotalStr = monthTotalStr;
    }

    public String getGrandTotalStr() {
        return grandTotalStr;
    }

    public void setGrandTotalStr(String grandTotalStr) {
        this.grandTotalStr = grandTotalStr;
    }


}
