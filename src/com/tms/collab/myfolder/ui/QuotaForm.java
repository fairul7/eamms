
package com.tms.collab.myfolder.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.formwizard.ui.validator.ValidatorIsInteger;
import com.tms.collab.myfolder.model.MyFolderModule;


public class QuotaForm extends Form{
	
	private SelectBox group;
    private TextField quota;
    private Button update;
    
    public QuotaForm() {
        super();
    }
    
    public void init() {
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
        
        addChild(new Label("l1", app.getMessage("messaging.label.quota")+" *"));
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
                MyFolderModule mf = (MyFolderModule)Application.getInstance().getModule(MyFolderModule.class);
                mf.updateMyFolderQuota(groupId, quotaVal);
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

	/**
	 * @return Returns the group.
	 */
	public SelectBox getGroup() {
		return group;
	}
	/**
	 * @param group The group to set.
	 */
	public void setGroup(SelectBox group) {
		this.group = group;
	}
	/**
	 * @return Returns the quota.
	 */
	public TextField getQuota() {
		return quota;
	}
	/**
	 * @param quota The quota to set.
	 */
	public void setQuota(TextField quota) {
		this.quota = quota;
	}
	/**
	 * @return Returns the update.
	 */
	public Button getUpdate() {
		return update;
	}
	/**
	 * @param update The update to set.
	 */
	public void setUpdate(Button update) {
		this.update = update;
	}
}
