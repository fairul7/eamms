package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.*;

import kacang.Application;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorIsDate;

import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Vector;
import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Jan 5, 2006
 * Time: 5:01:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PopDraftEdit extends Form {
    private String id;
    private Button submitButton;
    private DateField selectDate;

    private ClaimFormItem object;
    private TextField amountObject;
    private TextField objectPurpose;
    private SelectBox categorySelectBox;
    private SelectBox sb_ProjectId;
    private ValidatorNotEmpty notEmpty;
    private ValidatorIsNumeric isNumber;
    private ValidatorIsDate isDate;

    public void init() {
    }

    public void onRequest(Event evt) {
        setMethod("post");
        setColumns(2);

        removeChildren();

        notEmpty = new ValidatorNotEmpty("notEmpty");
        isNumber = new ValidatorIsNumeric("isNumber");
        isDate = new ValidatorIsDate("isDate");

        Application app = Application.getInstance();
        ClaimFormItemModule module = (ClaimFormItemModule) app.getModule(ClaimFormItemModule.class);
        ClaimFormItemCategoryModule moduleCategory = (ClaimFormItemCategoryModule) app.getModule(ClaimFormItemCategoryModule.class);
        ClaimProjectModule moduleProject = (ClaimProjectModule) app.getModule(ClaimProjectModule.class);

        if ((getId() != null) && !("".equals(getId()))) {
             object = module.retriveDraftItem(getId());

            addChild(new Label("date", app.getMessage("claims.label.date")+" *"));

            addChild(selectDate = new DatePopupField("selectDate"));
            selectDate.addChild(isDate);
            selectDate.setValue(object.getTimeFromStr());


            if (object.getStandardTypeId().equalsIgnoreCase("default")) {
                addChild(new Label("category",
                        app.getMessage("claims.label.category")));

                //addChild(new TextField("objectCategory", object.getCategoryId()));
                categorySelectBox = new SelectBox("objectCategory");

                Vector colCat = new Vector(moduleCategory.selectObjects(
                            "status", "act", 0, -1));

                for (int cnt1 = 0; cnt1 < colCat.size(); cnt1++) {
                    ClaimFormItemCategory valObj = (ClaimFormItemCategory) colCat.get(cnt1);

                    if (!("default".equals(valObj.getId())) &&
                            !("travel-mileage".equals(valObj.getId())) &&
                            !("travel-toll".equals(valObj.getId())) &&
                            !("travel-parking".equals(valObj.getId()))) {
                        categorySelectBox.addOption(valObj.getId(),
                            valObj.getName());
                    }
                }

                categorySelectBox.setSelectedOption(object.getCategoryId());

                addChild(categorySelectBox);

            }

            addChild(new Label("project",
                    app.getMessage("claims.label.projects")));

            sb_ProjectId = new SelectBox("projectObject");
            sb_ProjectId.addOption("", "- N/A -");

            {
                Vector colCat = new Vector(moduleProject.selectObjects(
                            "status", "act", 0, -1));

                for (int cnt1 = 0; cnt1 < colCat.size(); cnt1++) {
                    ClaimProject valObj = (ClaimProject) colCat.get(cnt1);
                    sb_ProjectId.addOption(valObj.getId(), valObj.getName());
                }
            }

            sb_ProjectId.setSelectedOption(object.getProjectId());
            addChild(sb_ProjectId);

            
            addChild(new Label("purpose", app.getMessage("claims.label.purpose")+" *"));

            addChild(objectPurpose = new TextField("objectPurpose", object.getRemarks()));
            objectPurpose.addChild(notEmpty);
            
            if (!object.getStandardTypeId().equalsIgnoreCase("default") && object.getCategoryId().equals("travel-mileage")) {
                addChild( new Label("mileage", app.getMessage("claims.label.mileage")+"(KM) "));
            }
            else
            addChild( new Label("amount", app.getMessage("claims.label.amount")+" *"));

            if (!object.getStandardTypeId().equalsIgnoreCase("default") && object.getCategoryId().equals("travel-mileage")) {

              addChild(amountObject= new TextField("amounObject", new DecimalFormat("0").format((object.getAmount().divide(ClaimConfigMileage.getDollarPerKM(app),0) ))));

            }
            else
           addChild(amountObject= new TextField("amounObject", new DecimalFormat("0.00").format(object.getAmount())));

           amountObject.addChild(isNumber);
           amountObject.setSize("9");


           addChild(new Label("a", ""));
           addChild(submitButton = new Button("submit",app.getMessage("claims.label.submit")));

        }



    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);

        Application app = Application.getInstance();
        String buttonClicked = findButtonClicked(evt);
        ClaimFormItemModule module = (ClaimFormItemModule)app.getModule(ClaimFormItemModule.class);

        if (buttonClicked.equals(submitButton.getAbsoluteName())) {


            if(selectDate.getDate() !=null && !("".equals(selectDate.getDate()))){
            object.setTimeFrom(selectDate.getDate());
            object.setTimeTo(selectDate.getDate());
            object.setTimeFinancial(selectDate.getDate());
            }
            else{
                setInvalid(true); return null;
            }


            ClaimStandardTypeModule cstModule = (ClaimStandardTypeModule) app.getModule(ClaimStandardTypeModule.class);

            if (object.getStandardTypeId().equalsIgnoreCase("default")) {

            ClaimStandardType cstObj = cstModule.selectObject(object.getStandardTypeId());

          
            
            try{
          
            
            	  if(Double.parseDouble(amountObject.getValue().toString()) ==0.00)
              	{amountObject.setInvalid(true);
              	 return null;
              	}
            	
            	
            cstObj.setAmount(new BigDecimal((String) amountObject.getValue()));


            object.setCurrency(cstObj.getCurrency());
            object.setAmount(cstObj.getAmount());
            object.setUnitPrice(cstObj.getAmount());
            }
            catch(NumberFormatException e){

                setInvalid(true);
                return null;
            }



            }
            else
            {

                if(object.getCategoryId().equalsIgnoreCase("travel-mileage")){


                  try{
                  BigDecimal dollarPerKM = ClaimConfigMileage.getDollarPerKM(app);
                  BigDecimal theMileage = new BigDecimal((String) amountObject.getValue());


                                 object.setQty(theMileage);
                                 object.setAmount(theMileage.multiply(dollarPerKM));
                                    object.setUnitPrice(dollarPerKM);
                  }
                  catch(NumberFormatException e){

                      setInvalid(true);
                      return null;
                  }



                }
                else{

                try{
                object.setAmount(new BigDecimal((String) amountObject.getValue()));
                object.setUnitPrice(new BigDecimal((String) amountObject.getValue()));
                }
                catch(NumberFormatException e){

                    setInvalid(true);
                    return null;
                }


            }


            }

            if((String) objectPurpose.getValue() !=null && !("".equals((String) objectPurpose.getValue())))
            object.setRemarks((String) objectPurpose.getValue());
            else{
                objectPurpose.setValue("");
                setInvalid(true);
                return null;

            }



            if (object.getStandardTypeId().equalsIgnoreCase("default")) {
            object.setCategoryId((String)categorySelectBox.getSelectedOptions().keySet().iterator().next());

            }
            object.setProjectId((String)sb_ProjectId.getSelectedOptions().keySet().iterator().next());

            if(module.checkSameDayDiffNameOther(selectDate.getDate(), (String)objectPurpose.getValue(), object.getCategoryId(), getId()) == true)
                return new Forward("sameNameExist");
            
            
            if(object.getAmount().longValue() >-999999999   && object.getAmount().longValue() < 999999999){
            module.deleteObject(object.getId());
            if(!module.addObject(object)) return new Forward("fail");

            }else {
                setInvalid(true);
                return new Forward("fail");
            }

           return  new Forward("submit");
        }


        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
