package com.tms.crm.sales.ui;

import kacang.stdui.Radio;
import kacang.stdui.ButtonGroup;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.tms.crm.sales.model.AccountManagerModule;
import com.tms.crm.sales.model.AccountManager;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 14, 2004
 * Time: 3:28:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class ReportByIndividualsForm extends ReportForm{
    private ReportsUsersSelectBox users;
    private Radio viewAll;
    private Radio viewSelected;
    private ButtonGroup viewGroup;
    private Collection userIdList;
	private ValidatorMessage vmsg_start;
	private ValidatorMessage vmsg_close;



    public void init() {

        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        users = new ReportsUsersSelectBox("users");
		addChild(users);
        users.init();
	    viewAll = new Radio("viewall",Application.getInstance().getMessage("sfa.label.viewAll","View All"));
        viewAll.setChecked(true);
        viewSelected = new Radio("viewSelected",Application.getInstance().getMessage("sfa.label.viewSelected","View Selected"));
        viewGroup = new ButtonGroup("viewGroup",new Radio[]{viewAll,viewSelected});
        addChild(viewAll);
        addChild(viewSelected);
		vmsg_start = new ValidatorMessage("vmsg_start");
		start.addChild(vmsg_start);
		vmsg_close = new ValidatorMessage("vmsg_close");
		close.addChild(vmsg_close);
    }


	public Forward onSubmit(Event evt) {
		Forward fwd = super.onSubmit(evt);
		if (!start.isChecked() && !close.isChecked()) {
			vmsg_start.showError("");
			vmsg_close.showError("");
			setInvalid(true);
		}
		return fwd;
	}

    public Forward onValidate(Event evt) {
        if(view.getAbsoluteName().equals(findButtonClicked(evt))){
			userIdList = new ArrayList();
            if(viewSelected.isChecked()){
                userIdList.addAll(Arrays.asList(users.getIds()));
            }else{
                AccountManagerModule am = (AccountManagerModule) Application.getInstance().getModule(AccountManagerModule.class);
                Collection col = am.getAccountManagers();
                for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                    AccountManager accountManager = (AccountManager) iterator.next();
                    userIdList.add(accountManager.getId());
                }
            }
            toDate = to.getDate();
            fromDate = from.getDate();
			startFromDate = startFrom.getDate();
			startToDate = startTo.getDate();
			startBool = start.isChecked();
			closeBool = close.isChecked();

            return new Forward("view");

		}

        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

	public ReportsUsersSelectBox getUsers() {
		return users;
	}

	public void setUsers(ReportsUsersSelectBox users) {
		this.users = users;
	}

    public Radio getViewAll() {
        return viewAll;
    }

    public void setViewAll(Radio viewAll) {
        this.viewAll = viewAll;
    }

    public Radio getViewSelected() {
        return viewSelected;
    }

    public void setViewSelected(Radio viewSelected) {
        this.viewSelected = viewSelected;
    }

    public ButtonGroup getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(ButtonGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

	public Collection getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(Collection userIdList) {
        this.userIdList = userIdList;
    }

    public String getDefaultTemplate() {
        return "sfa/reportbyindividuals";
    }
}
