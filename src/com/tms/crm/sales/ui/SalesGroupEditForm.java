package com.tms.crm.sales.ui;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.crm.sales.model.SalesGroupModule;
import com.tms.crm.sales.model.SalesGroup;
import com.tms.crm.sales.model.SalesGroupException;

import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 30, 2004
 * Time: 11:30:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class SalesGroupEditForm extends SalesGroupForm{

    public static final String FORWARD_UPDATED = "updated";
    public SalesGroupEditForm() {
    }

    public SalesGroupEditForm(String s) {
        super(s);
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
        if(getId()!=null&&getId().trim().length()>0)
            populateForm();

    }

    private void populateForm(){
        SalesGroupModule sgm   = (SalesGroupModule) Application.getInstance().getModule(SalesGroupModule.class);
        try {
            SalesGroup group = sgm.getSalesGroup(getId());
            if(group!=null){
                nameTF.setValue(group.getName());
                descriptionTB.setValue(group.getDescription());
                userSelectBox.setIds((String[]) group.getMemberIdSet().toArray(new String[0]));
            }
        } catch (SalesGroupException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
    }


    public Forward onValidate(Event evt) {
        if(submit.getAbsoluteName().equals(findButtonClicked(evt))){
            SalesGroup group = assembleGroup();
            SalesGroupModule sgm = (SalesGroupModule) Application.getInstance().getModule(SalesGroupModule.class);
            try {
                sgm.updateGroup(group);
                init();
                return new Forward(FORWARD_UPDATED);
            } catch (SalesGroupException e) {
                Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
            }

        }
        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }


    private SalesGroup assembleGroup(){
        SalesGroup salesGroup = new SalesGroup();
        salesGroup.setId(getId());
        salesGroup.setName(nameTF.getValue().toString());
        salesGroup.setDescription(descriptionTB.getValue().toString());
        String [] ids  = userSelectBox.getIds();
        TreeSet idSet = new TreeSet();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            idSet.add(id);
        }
        salesGroup.setMemberIdSet(idSet);
        return salesGroup;
    }

	public boolean isEditMode() {
		return true;
	}
}
