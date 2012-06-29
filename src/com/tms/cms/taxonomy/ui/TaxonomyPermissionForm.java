package com.tms.cms.taxonomy.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ComboSelectBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.IDDatatypeValidator;
import com.tms.cms.taxonomy.model.TaxonomyModule;

public class TaxonomyPermissionForm extends Form{
	
	private String PERMISSION_ID="Taxonomy";
	
	protected ComboSelectBox permissionRoleComboBox;
	protected Button btnSubmit;
	protected Button btnCancel;
	
	public void init() {
		permissionRoleComboBox = new ComboSelectBox("permissionComboSelect");
		btnSubmit = new Button("btnSubmit",Application.getInstance().getMessage("taxonomy.label.submit","Submit"));
		btnCancel = new Button("btnCancel",Application.getInstance().getMessage("taxonomy.label.cancel","Cancel"));
		
		
		addChild(permissionRoleComboBox);
		addChild(btnSubmit);
		addChild(btnCancel);
		permissionRoleComboBox.init();

		
		
	}
	
	public void onRequest(Event ev) {
		init();
		
		// set up permission select box value
		TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
		Collection allRole = mod.getAllRole();
		Collection permissionRole = mod.getAllRoleWithPermission(PERMISSION_ID);
		
		Map selectedRoleMap = new SequencedHashMap();
		if (permissionRole!=null && permissionRole.size()>0) {
			
			for (Iterator i=permissionRole.iterator();i.hasNext();) {
				HashMap map = (HashMap)i.next();
				String roleId = (String)map.get("roleId");
				selectedRoleMap.put(roleId,getRoleDescription(roleId));
			}
			permissionRoleComboBox.setRightValues(selectedRoleMap);
		}
		if(allRole!=null && allRole.size()>0) {
			Map allRoleMap = new SequencedHashMap();
			for (Iterator i=allRole.iterator();i.hasNext();) {
				HashMap map = (HashMap)i.next();
				String roleId = (String)map.get("roleId");
				if (!selectedRoleMap.containsKey(roleId) && !roleId.equals("reader")) { 
					allRoleMap.put(roleId,getRoleDescription(roleId));
				}
			}
			permissionRoleComboBox.setLeftValues(allRoleMap);
		}
		
		
	}
	
	private String getRoleDescription(String id) {
		if (id.equals("editor")) {
			return Application.getInstance().getMessage("general.label.editors");
		}
		else if (id.equals("author")) {
			return Application.getInstance().getMessage("general.label.authors");
		}
		else if (id.equals("manager")) {
			return Application.getInstance().getMessage("general.label.managers");
		}
		return id;
	}
	
	public Forward onValidate(Event ev) {
		Forward forward = new Forward();
		
		if (btnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
			forward = new Forward("cancel");
		}
		else if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
			Map selectedRole = permissionRoleComboBox.getRightValues();
			if (selectedRole!=null && selectedRole.size()>0) {
				TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
				mod.deletePermission();
				for (Iterator i=selectedRole.keySet().iterator();i.hasNext();) {
					String roleId = (String)i.next();
					mod.addPermission(roleId, PERMISSION_ID);
				}
				forward = new Forward("success");
			}
			else {
				forward = new Forward("noRole");
			}
		}
		return forward;
	}
	
	public String getDefaultTemplate() {
		return "taxonomy/permissionForm";
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public ComboSelectBox getPermissionRoleComboBox() {
		return permissionRoleComboBox;
	}

	public void setPermissionRoleComboBox(ComboSelectBox permissionRoleComboBox) {
		this.permissionRoleComboBox = permissionRoleComboBox;
	}

	
}
