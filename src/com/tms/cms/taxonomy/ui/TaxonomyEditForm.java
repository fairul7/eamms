package com.tms.cms.taxonomy.ui;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class TaxonomyEditForm extends Form{

	
	private TextField node;
	private TextBox nodeDescription;
	private TextBox nodeSynonym;
	private Button btnSubmit;
	private Button btnCancel;
	private Button btnDelete;     
	private CheckBox cbShown;
	private Button btnDeleteChild;
	
	private String parentId;
	private TaxonomyNode parentNode;
	private String taxonomyId;
	
	public void init() {
		node = new TextField("node");
		node.addChild(new ValidatorNotEmpty("vne"));
		nodeDescription = new TextBox("nodeDescription");
		nodeDescription.addChild(new ValidatorNotEmpty("vne1"));
		nodeSynonym = new TextBox("nodeSynonym");
		cbShown = new CheckBox("cbShown");
		
		btnSubmit = new Button("submit",Application.getInstance().getMessage("taxonomy.label.submit","Submit"));
		btnCancel = new Button("cancel",Application.getInstance().getMessage("taxonomy.label.cancel","Cancel"));
		btnDelete = new Button("delete",Application.getInstance().getMessage("taxonomy.label.delete","Delete"));
		btnDeleteChild = new Button("deleteChild", Application.getInstance().getMessage("taxonomy.label.deleteChild","Delete Child"));
		btnDeleteChild.setOnClick("javascript:alert('"+Application.getInstance().getMessage("taxonomy.label.alertDeleteChild","All Child Nodes will be deleted.")+"');");
		
		addChild (node);        
		addChild(nodeDescription);
		addChild(nodeSynonym);
		addChild(cbShown);
		addChild(btnSubmit);
		addChild(btnCancel);
		addChild(btnDelete);
		addChild(btnDeleteChild);
		
	}  
	
	
	public void onRequest(Event ev) {
		node.setValue("");
		nodeDescription.setValue("");
		nodeSynonym.setValue("");
		cbShown.setChecked(false);
		
		if (taxonomyId!=null && !taxonomyId.equals("")) {
			TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
			TaxonomyNode txyNode = mod.getNode(taxonomyId);
			node.setValue(txyNode.getTaxonomyName());
			nodeDescription.setValue(txyNode.getDescription());
			nodeSynonym.setValue(txyNode.getNodeSynonym());
			if (txyNode.getShown()>0) {
				cbShown.setChecked(true);
			}else {
				cbShown.setChecked(false);
			}
			if (!txyNode.getParentId().equals("0")) {
				parentNode = mod.getNode(txyNode.getParentId());
			}
		}
		else {
			node.setValue("");
			nodeDescription.setValue("");
			nodeSynonym.setValue("");
			cbShown.setChecked(false);
		}
		
	}
	
	public String getDefaultTemplate(){
		return "taxonomy/editform";
	}
	
		
	public Forward onValidate(Event ev) {
		Forward forward = new Forward();
		if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
			TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
			
			String taxonomyName = (String)node.getValue();
			String taxonomyDescription = (String)nodeDescription.getValue();
			String taxonomySynonym = (String)nodeSynonym.getValue();
			boolean show=false;
			if (cbShown.isChecked()) {
				show=true;
			}
			TaxonomyNode txyNode = new TaxonomyNode();
			txyNode = mod.getNode(taxonomyId); //.setTaxonomyId(taxonomyId);
			
			txyNode.setTaxonomyName(taxonomyName);
			txyNode.setDescription(taxonomyDescription);
			txyNode.setNodeSynonym(taxonomySynonym);
			if (show) {
				txyNode.setShown(1);
			}
			else {
				txyNode.setShown(0);
			}
			
			mod.updateNode(txyNode);
			
			taxonomyId="";
			parentId="";
			forward = new Forward("success");
			
		}
		else if (btnDelete.getAbsoluteName().equals(findButtonClicked(ev))) {
			forward = new Forward("delete");
		}
		else if (btnDeleteChild.getAbsoluteName().equals(findButtonClicked(ev))) {
			forward = new Forward("deleteChild");
		}
		else if (btnCancel.getAbsoluteName().equals(findButtonClicked(ev))) {
			forward = new Forward("cancel");
		}
		
		return forward;
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


	public TextField getNode() {
		return node;
	}


	public void setNode(TextField node) {
		this.node = node;
	}


	public TextBox getNodeDescription() {
		return nodeDescription;
	}


	public void setNodeDescription(TextBox nodeDescription) {
		this.nodeDescription = nodeDescription;
	}


	public TextBox getNodeSynonym() {
		return nodeSynonym;
	}


	public void setNodeSynonym(TextBox nodeSynonym) {
		this.nodeSynonym = nodeSynonym;
	}


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public TaxonomyNode getParentNode() {
		return parentNode;
	}


	public void setParentNode(TaxonomyNode parentNode) {
		this.parentNode = parentNode;
	}

	public CheckBox getCbShown() {
		return cbShown;
	}


	public void setCbShown(CheckBox cbShown) {
		this.cbShown = cbShown;
	}


	public String getTaxonomyId() {
		return taxonomyId;
	}


	public void setTaxonomyId(String taxonomyId) {
		this.taxonomyId = taxonomyId;
	}
	
	public Button getBtnDelete() {
		return btnDelete;
	}
	
	public void setBtnDelete(Button btnDelete) {
		this.btnDelete=btnDelete;
	}

	public Button getBtnDeleteChild() {
		return btnDeleteChild;
	}
	
	public void setBtnDeleteChild(Button btnDeleteChild) {
		this.btnDeleteChild = btnDeleteChild;
	}
	
	

}
