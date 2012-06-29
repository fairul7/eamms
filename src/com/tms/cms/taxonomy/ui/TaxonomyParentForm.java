package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class TaxonomyParentForm extends Form {
	
	public Radio[] radioButtons;
	public Button btnSubmit;
	public Button btnChild;
	public Button btnEdit;
	public Button btnMove;
	public Collection nodes;
	public ButtonGroup bgNodes;
	
	protected String parentId; 
	protected String listAvailable;
	
	private int iCurrent;
	
	public String getDefaultTemplate() {
		return "/taxonomy/parentForm";
	}
	
	public void init() {   
		nodes = new ArrayList();
	}
	
	public void onRequest(Event ev) {
		listAvailable="";
		TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
		nodes = mod.formAdminTaxonomyTree(null);
		
		int iTotalNodes = mod.getNodesTotal(-1);
		radioButtons = new Radio[iTotalNodes];
		iCurrent = 0;
		bgNodes = new ButtonGroup("nodes");
		addChild(bgNodes);
		
		if (nodes!=null && nodes.size()>0) {
			listAvailable="true";
			for (Iterator i=nodes.iterator();i.hasNext();) {
				TaxonomyNode node = (TaxonomyNode)i.next();
				radioButtons[iCurrent] = new Radio("radio"+iCurrent);
				radioButtons[iCurrent].setValue(node.getTaxonomyId());
				bgNodes.addButton(radioButtons[iCurrent]);
				addChild(radioButtons[iCurrent]);
				iCurrent++;
				
				addRadioButton(node);
			}
		
			btnEdit = new Button("btnEdit",Application.getInstance().getMessage("taxonomy.label.edit","Edit"));
			addChild(btnEdit);
			btnMove = new Button("btnMove",Application.getInstance().getMessage("taxonomy.label.move","Move Taxonomy"));
			addChild(btnMove);
		}
		btnSubmit = new Button("btnSubmit",Application.getInstance().getMessage("taxonomy.label.addParent","Add Parent"));
		btnChild = new Button("btnChild",Application.getInstance().getMessage("taxonomy.label.addChild","Add Child"));
		addChild(btnSubmit);
		addChild(btnChild);
	}

    public void addRadioButton(TaxonomyNode node) {

        if(node.getChildNodes() != null){
            for (Iterator child=node.getChildNodes().iterator();child.hasNext();) {
                TaxonomyNode childNode = (TaxonomyNode)child.next();
                radioButtons[iCurrent] = new Radio("radio"+iCurrent);
                radioButtons[iCurrent].setValue(childNode.getTaxonomyId().toString());

                bgNodes.addButton(radioButtons[iCurrent]);
                addChild(radioButtons[iCurrent]);
                iCurrent++;

                addRadioButton(childNode);
            }
        }
    }
	public Forward onValidate(Event ev) {
		parentId = "";
		Forward forward = new Forward();
		
		if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
			forward = new Forward("parent");
		}
		else if (btnChild.getAbsoluteName().equals(findButtonClicked(ev))) {
			String checkedId = "";
			for (int i=0;i<radioButtons.length;i++) {
				if (radioButtons[i].isChecked()) {
					checkedId = (String)radioButtons[i].getValue();
				}
			}
			if (checkedId==null || checkedId.equals("")) {
				return new Forward("errorParent");
			}
			parentId = checkedId;
			forward = new Forward("child");
		}
		else if (btnEdit.getAbsoluteName().equals(findButtonClicked(ev))) {
			String checkedId = "";
			for (int i=0;i<radioButtons.length;i++) {
				if (radioButtons[i].isChecked()) {
					checkedId = (String)radioButtons[i].getValue();
				}
			}
			if (checkedId==null || checkedId.equals("")) {
				return new Forward("errorId");
			}
			parentId=checkedId;
			forward = new Forward("edit");
		}
		else if (btnMove.getAbsoluteName().equals(findButtonClicked(ev))) {
			String checkedId = "";
			for (int i=0;i<radioButtons.length;i++) {
				if (radioButtons[i].isChecked()) {
					checkedId = (String)radioButtons[i].getValue();
				}
			}
			if (checkedId==null || checkedId.equals("")) {
				return new Forward("errorId");
			}
			parentId=checkedId;
			forward = new Forward("move");
		}
		return forward;
	}

	public Button getBtnChild() {
		return btnChild;
	}

	public void setBtnChild(Button btnChild) {
		this.btnChild = btnChild;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public Collection getNodes() {
		return nodes;
	}

	public void setNodes(Collection nodes) {
		this.nodes = nodes;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Radio[] getRadioButtons() {
		return radioButtons;
	}

	public void setRadioButtons(Radio[] radioButtons) {
		this.radioButtons = radioButtons;
	}

	public Button getBtnEdit() {
		return btnEdit;
	}

	public void setBtnEdit(Button btnEdit) {
		this.btnEdit = btnEdit;
	}

	public String getListAvailable() {
		return listAvailable;
	}
	
	public void setBtnMove(Button btnMove) {
		this.btnMove = btnMove;
	}
	
	public Button getBtnMove() {
		return btnMove;
	}

	public void setListAvailable(String listAvailable) {
		this.listAvailable = listAvailable;
	}
	
	public void setBgNodes(ButtonGroup bgNodes) {
		this.bgNodes = bgNodes;
	}
	
	public ButtonGroup getBgNodes() {
		return this.bgNodes;
	}
	
	

}
