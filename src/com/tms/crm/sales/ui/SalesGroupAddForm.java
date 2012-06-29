package com.tms.crm.sales.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import com.tms.crm.sales.model.SalesGroupModule;
import com.tms.crm.sales.model.SalesGroup;
import com.tms.crm.sales.model.SalesGroupException;

import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jun 29, 2004
 * Time: 3:39:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesGroupAddForm extends SalesGroupForm{

    public static final String FORWARD_GROUP_ADDED ="groupAdded";

    public SalesGroupAddForm() {
    }

    public SalesGroupAddForm(String s) {
        super(s);
    }

    public Forward onValidate(Event event) {
        if( submit.getAbsoluteName().equals(findButtonClicked(event))){
            SalesGroup salesGroup = assembleGroup();
            SalesGroupModule sm = (SalesGroupModule) Application.getInstance().getModule(SalesGroupModule.class);
            try {
                sm.addGroup(salesGroup);
                init();
                return new Forward(FORWARD_GROUP_ADDED);
            } catch (SalesGroupException e) {
                Log.getLog(getClass()).error("Error Adding group"+e,e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return super.onValidate(event);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private SalesGroup assembleGroup(){
        SalesGroup salesGroup = new SalesGroup();
        salesGroup.setId(UuidGenerator.getInstance().getUuid());
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


}
