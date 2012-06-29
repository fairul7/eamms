package com.tms.collab.messaging.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.*;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;

import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.formwizard.ui.validator.ValidatorIsInteger;

public class QuotaForm extends Form {

    private SelectBox group;
    private TextField quota;
    private Button update;

    public QuotaForm() {
        super();
    }

    public QuotaForm(String name) {
        super(name);
    }

    public void init() {
        initForm();
    }

    public void onRequest(Event evt) {
        initForm();
    }

    public void initForm() {
        Application app = Application.getInstance();
        setMethod("POST");
        removeChildren();

        group = new SelectBox("group");
        try {
            Map optionMap = new SequencedHashMap();
            optionMap.put("", "---" + app.getMessage("general.label.pleaseSelect") + "---");

            SecurityService ss = (SecurityService)app.getService(SecurityService.class);
            DaoQuery dq = new DaoQuery();
            dq.addProperty(new OperatorEquals("active", "1", OperatorEquals.OPERATOR_AND));
            Collection list = ss.getGroups(dq, 0, -1, "groupName", false);
            for (Iterator i=list.iterator(); i.hasNext();) {
                Group g = (Group)i.next();
                optionMap.put(g.getId(), g.getName());
            }
            group.setOptionMap(optionMap);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error("Error retrieving groups", e);
        }
        group.addChild(new ValidatorNotEmpty("v1"));
        addChild(new Label("l0", app.getMessage("security.label.group")));
        addChild(group);

        quota = new TextField("quota");
        quota.setSize("10");
        quota.addChild(new ValidatorIsInteger("v2"));
        addChild(new Label("l1", app.getMessage("messaging.label.quota") + " *"));
        addChild(quota);
        addChild(new Label("l2", "KB"));

        update = new Button("update", app.getMessage("general.label.update"));
        addChild(update);

    }

    public Forward onValidate(Event evt) {
        Map selected = group.getSelectedOptions();
        if (selected != null && selected.size() > 0) {
            String groupId = selected.keySet().iterator().next().toString();

            String quotaStr = (String)quota.getValue();
            long quotaVal = Long.parseLong(quotaStr);

            try {
                MessagingModule mm = (MessagingModule)Application.getInstance().getModule(MessagingModule.class);
                mm.updatePrincipalQuota(groupId, quotaVal);
                initForm();
                return new Forward("updated");
            }
            catch (Exception e) {
                Log.getLog(getClass()).error("Error updating principal quota", e);
                return new Forward("error");
            }

        }
        return super.onValidate(evt);
    }

}
