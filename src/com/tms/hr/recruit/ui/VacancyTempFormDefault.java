package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.RichTextBox;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.stdui.validator.ValidatorMessage;

import com.tms.hr.orgChart.model.OrgChartHandler;
import com.tms.hr.orgChart.model.OrgSetup;
import com.tms.hr.recruit.model.RecruitModule;

public class VacancyTempFormDefault extends Form{
	
	protected SelectBox sbPosition;
    protected SelectBox sbCountry;
    protected SelectBox sbDepartment;
    protected TextBox tbJobRespon;
    protected TextBox tbJobRequire;
	
    private Map countries = new LinkedHashMap();
    private Map positions = new LinkedHashMap();
    private Map depts = new LinkedHashMap();
    protected ValidatorMessage vaMsgPositionFound;
    
    public void init(){
    	initFormDefault();
    }
    
    public void initFormDefault(){
    	//setColumns(2);
    	//setMethod("POST");
    	Application app = Application.getInstance();
    	
    	sbPosition = new SelectBox("sbPosition");
    	//sbPosition.addChild(new ValidateSelectBox("sbPositionVSB", app.getMessage("recruit.general.warn.sbPosition")));
    	Label lblPosition = new Label("lblPosition", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.position") + "*</span>");
    	lblPosition.setAlign("right");
    	addChild(lblPosition);
    	addChild(sbPosition);
    	
    	sbCountry = new SelectBox("sbCountry");
    	//sbCountry.addChild(new ValidateSelectBox("sbCountry", app.getMessage("recruit.general.warn.sbCountry")));
    	sbCountry.setOnChange("javascript:setDeptCountryChange()");
    	Label lblCountry = new Label("lblCountry", "<span class=\"fieldTitle\">" +app.getMessage("recruit.general.label.country") + "*</span>");
    	lblCountry.setAlign("right");
    	addChild(lblCountry);
    	addChild(sbCountry);
    	vaMsgPositionFound = new ValidatorMessage("vaMsgPositionFound");
        sbPosition.addChild(vaMsgPositionFound);
         
    	sbDepartment = new SelectBox("sbDepartment");
    	//sbDepartment.addChild(new ValidateSelectBox("sbDepartment", app.getMessage("recruit.general.warn.sbDepartment")));
    	Label lblDepartment = new Label("lblDepartment","<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.department") + "*</span>");
    	lblDepartment.setAlign("right");
    	addChild(lblDepartment);
    	addChild(sbDepartment);
    	
    	tbJobRespon = new RichTextBox("tbJobRespon");
    	//tbJobRespon.addChild(new ValidateRichBox("tbJobResponVNE", app.getMessage("recruit.general.warn.empty")));

    	tbJobRespon.setRows("20");
    	tbJobRespon.setCols("10");
    	Label lblJobRespon = new Label("lblJobRespon", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tbJobRespon") + "*</span>");
    	lblJobRespon.setAlign("right");
    	addChild(lblJobRespon);
    	addChild(tbJobRespon);
    	
    	tbJobRequire = new RichTextBox("tbJobRequire");
    	//tbJobRequire.addChild(new ValidateRichBox("tbJobRequireVNE", app.getMessage("recruit.general.warn.empty")));

    	tbJobRequire.setRows("20");
    	tbJobRequire.setCols("10");
    	Label lblJobRequire = new Label("lblJobRequire", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.tbJobRequire") + "*</span>");
    	lblJobRequire.setAlign("right");
    	addChild(lblJobRequire);
    	addChild(tbJobRequire);
    	
    	 OrgChartHandler oc = (OrgChartHandler) app.getModule(OrgChartHandler.class);
         Collection countriesCol = oc.findAllSetup(OrgChartHandler.TYPE_COUNTRY, null, 0, -1, "shortDesc", false, true);
         Collection positionsCol = oc.findAllSetup(OrgChartHandler.TYPE_TITLE, null, 0, -1, "shortDesc", false, true);
         Collection deptsCol = oc.findAllSetup(OrgChartHandler.TYPE_DEPT, null, 0, -1, "shortDesc", false, true);

         positions.put("---", app.getMessage("recruit.general.hierachy.selectPosition"));
         for(Iterator itr = positionsCol.iterator(); itr.hasNext();){
             OrgSetup obj = (OrgSetup) itr.next();
             positions.put(obj.getCode(), obj.getShortDesc());
         }
         
         countries.put("---", app.getMessage("recruit.general.hierachy.selectCountry"));
         for(Iterator itr = countriesCol.iterator(); itr.hasNext();){
             OrgSetup obj = (OrgSetup) itr.next();
             countries.put(obj.getCode(), obj.getShortDesc());
         }
         
         depts.put("---", app.getMessage("recruit.general.hierachy.selectDept"));
         for(Iterator itr = deptsCol.iterator(); itr.hasNext();){
             OrgSetup obj = (OrgSetup) itr.next();
             depts.put(obj.getCode(), obj.getShortDesc());
         }
         
         sbPosition.setOptionMap(positions);
         sbCountry.setOptionMap(countries);
         sbDepartment.setOptionMap(depts);
         
        
    }
       
    //Validation for onSubmit
	public boolean validate(FormField ff, String field, String type) {
        boolean status=true;
        Application app = Application.getInstance();
        if(field.equals("sb")){
			List list = (List) ff.getValue();
	        String strSb = (String) list.get(0);
	        if(strSb.startsWith("---")){
	        	status=false;
	        	ff.setMessage(app.getMessage("recruit.general.warn.sbPosition"));
	        	ff.setInvalid(true);
	        	setInvalid(true);
	        }
        }
        else if(field.equals("tb")){
        	String defaultValue = "<body />";
        	String defaultValue1 = "<body><p>&nbsp;</p>";
    		String rbox = (String) ff.getValue();
    		if(rbox == null || rbox.equals(defaultValue) || rbox.equals("") || rbox.equals(defaultValue1)){
    			status=false;
    			ff.setMessage(app.getMessage("recruit.general.warn.empty"));
    			ff.setInvalid(true);
	        	setInvalid(true);
    		}	
        }
        else if(field.equals("codeExist")){
        	String vacancyCode = (String) ff.getValue();
    		if(vacancyCode == null || vacancyCode.equals("")){
    			status=false;
    			ff.setInvalid(true);
	        	setInvalid(true);
    		}else{
    			RecruitModule rm = (RecruitModule) app.getModule(RecruitModule.class);
    			try{
    				if(rm.codeExist(vacancyCode, type)){
    					status=false;
    					ff.setMessage(app.getMessage("recruit.general.warn.codeKey"));
    	    			ff.setInvalid(true);
    		        	setInvalid(true);
    				}
    			}catch(DaoException e){
    				status=false;
    			}
    		}
        }
        else if(field.equals("txtInt")){
        	ValidatorIsNumeric VIN = new ValidatorIsNumeric("isNotNumeric",app.getMessage("recruit.general.warn.notNumericVIN")); 
        	boolean isNotNumeric = VIN.validate(ff);
        	
        	if(!isNotNumeric){
        		status=false;
        		//ff.setMessage(app.getMessage("recruit.general.warn.codeKey"));
    			ff.setInvalid(true);
	        	setInvalid(true);
        	}
        }
        
        return status;
    }
    
    //getter setter
	public SelectBox getSbCountry() {
		return sbCountry;
	}

	public void setSbCountry(SelectBox sbCountry) {
		this.sbCountry = sbCountry;
	}

	public SelectBox getSbDepartment() {
		return sbDepartment;
	}

	public void setSbDepartment(SelectBox sbDepartment) {
		this.sbDepartment = sbDepartment;
	}

	public SelectBox getSbPosition() {
		return sbPosition;
	}

	public void setSbPosition(SelectBox sbPosition) {
		this.sbPosition = sbPosition;
	}

	public TextBox getTbJobRequire() {
		return tbJobRequire;
	}

	public void setTbJobRequire(TextBox tbJobRequire) {
		this.tbJobRequire = tbJobRequire;
	}

	public TextBox getTbJobRespon() {
		return tbJobRespon;
	}

	public void setTbJobRespon(TextBox tbJobRespon) {
		this.tbJobRespon = tbJobRespon;
	}

	public ValidatorMessage getVaMsgPositionFound() {
		return vaMsgPositionFound;
	}

	public void setVaMsgPositionFound(ValidatorMessage vaMsgPositionFound) {
		this.vaMsgPositionFound = vaMsgPositionFound;
	}
}
