package com.tms.hr.claim.ui;

import kacang.stdui.Form;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.Button;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import com.tms.hr.claim.model.ClaimFormItemCategory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 8, 2005
 * Time: 12:10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DependencyForm extends Form {
    protected SelectBox combo;
    protected Button bnSubmit;
    protected Button bnCancel;
    protected String[] attachedDepartment;
    protected String categoryId;
    protected String categoryName;

    public void init() {
        combo = new SelectBox("combo");
        combo.setRows(5);
        combo.setMultiple(true);
        addChild(combo);
        EmployeeModule handler;
        Application application = Application.getInstance();
        handler = (EmployeeModule) application.getModule(EmployeeModule.class);
        try {
            Collection cDeptList = handler.getDepartmentList();
            Map map = new SequencedHashMap();
            for (Iterator i=cDeptList.iterator();i.hasNext();) {
                DepartmentDataObject obj = (DepartmentDataObject)i.next();
                map.put(obj.getDeptCode(),obj.getDeptDesc());
            }
            combo.setOptionMap(map);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }


        bnSubmit = new Button("bnSubmit","Submit");
        addChild(bnSubmit);
        bnCancel = new Button("bnCancel","Cancel");
        addChild(bnCancel);
    }

    public void onRequest(Event ev) {
        populateForm();
    }

    public void populateForm() {
        removeChildren();
        init();
        ClaimFormItemCategoryModule mod = (ClaimFormItemCategoryModule)
            Application.getInstance().getModule(ClaimFormItemCategoryModule.class);
        EmployeeModule empMod = (EmployeeModule)Application.getInstance().getModule(EmployeeModule.class);

        ClaimFormItemCategory obj = mod.selectObject(categoryId);
        categoryName=obj.getName();

        String[] dependencies = mod.selectDependencies(categoryId);

        if (dependencies!=null && dependencies.length>0) {
             attachedDepartment=new String[dependencies.length];
            Map rMap = new SequencedHashMap();
            Map lMap = combo.getOptionMap();
            try {
                for (int i=0;i<dependencies.length;i++) {
                    DepartmentDataObject deptObj = empMod.getDepartment(dependencies[i]);
                    attachedDepartment[i] = deptObj.getDeptDesc();
                    rMap.put(deptObj.getDeptCode(),deptObj.getDeptDesc());
                    lMap.remove(deptObj.getDeptCode());
                }
            }
            catch(Exception e) {}
            combo.setOptionMap(lMap);
        }

    }

    public Forward onValidate(Event ev) {
        if (bnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            Map selectedDependencies = combo.getSelectedOptions();
            ClaimFormItemCategoryModule mod = (ClaimFormItemCategoryModule)
                                        Application.getInstance().getModule(ClaimFormItemCategoryModule.class);
            if (selectedDependencies.size()>0) {
                // add
                for (Iterator i=selectedDependencies.keySet().iterator();i.hasNext();) {
                    String departmentCode = (String)i.next();
                    mod.addDependency(categoryId,departmentCode);
                }
                return new Forward("success");
            }
            else {
                String[] original = mod.selectDependencies(categoryId);
                if (original!=null && original.length>0) {
                    for (int i=0;i<original.length;i++) {
                        mod.deleteDependency(categoryId,original[i]);
                    }
                }
                return new Forward("fail");
            }
        }
        else if (bnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
            return new Forward("cancel");
        }
        return null;
    }

    public String getDefaultTemplate() {
        return "claims/dependencyForm";
    }

    public void setCategoryId(String id) {
        categoryId=id;
        populateForm();
    }

    public SelectBox getCombo() {
        return combo;
    }

    public void setCombo(SelectBox combo) {
        this.combo = combo;
    }

    public Button getBnSubmit() {
        return bnSubmit;
    }

    public void setBnSubmit(Button bnSubmit) {
        this.bnSubmit = bnSubmit;
    }

    public String[] getAttachedDepartment() {
        return attachedDepartment;
    }

    public void setAttachedDepartment(String[] attachedDepartment) {
        this.attachedDepartment = attachedDepartment;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public Button getBnCancel(){
        return bnCancel;
    }
}
