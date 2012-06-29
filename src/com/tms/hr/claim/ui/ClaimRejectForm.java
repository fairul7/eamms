package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimFormIndex;
import com.tms.hr.claim.model.ClaimFormIndexModule;

import kacang.Application;

import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextBox;

import kacang.ui.Event;
import kacang.ui.Forward;


/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 7, 2005
 * Time: 9:29:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimRejectForm extends Form {
    protected TextBox tfReason;
    protected Button bnSubmit;
    protected Button bnCancel;
    protected String id;
    protected String approveType;

    public void init() {
        removeChildren();
        tfReason = new TextBox("tfReason");

        //tfReason.addChild(new ValidatorNotEmpty("vne"));
        bnSubmit = new Button("bnSubmit", "Submit");
        bnCancel = new Button("bnCancel", "Cancel");
        addChild(tfReason);
        addChild(bnSubmit);
        addChild(bnCancel);

        setMethod("POST");
    }

    public Forward onSubmit(Event ev) {
        super.onSubmit(ev);

        if (bnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
            return new Forward("cancel");
        }

        return null;
    }

    public Forward onValidate(Event ev) {
        if (bnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
            if ((id != null) && !id.equals("")) {
                String reason = (String) tfReason.getValue();
                Application app = Application.getInstance();
                ClaimFormIndexModule cim = (ClaimFormIndexModule) app.getModule(ClaimFormIndexModule.class);
                ClaimFormIndex obj = cim.selectObject(id);
                obj.setRejectReason(reason);
                cim.updateObject(obj);

                ClaimFormIndexTableModel.procAction(ev, "reject",
                    new String[] { id });

                if ((approveType != null) && approveType.equals("approve")) {
                    return new Forward("approverRejectSuccess");
                } else {
                    return new Forward("assessorRejectSuccess");
                }
            } else {
                return new Forward("rejectFail");
            }
        } else if (bnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
            return new Forward("cancel");
        }

        return null;
    }

    public String getDefaultTemplate() {
        return "claims/rejectForm";
    }

    public void setId(String id) {
        this.id = id;
        init();
    }

    public TextBox getTfReason() {
        return tfReason;
    }

    public Button getBnSubmit() {
        return bnSubmit;
    }

    public Button getBnCancel() {
        return bnCancel;
    }

    public void setApproveType(String type) {
        this.approveType = type;
    }

    public String getApproveType() {
        return approveType;
    }
}
