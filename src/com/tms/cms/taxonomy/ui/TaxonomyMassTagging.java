package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.core.ui.ContentPopupSelectBox;
import com.tms.cms.taxonomy.model.TaxonomyMap;
import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;


public class TaxonomyMassTagging extends Form {

	protected CheckBox[] cbNodes;
	protected ContentPopupSelectBox contentSelectBox;
	protected Button btnSubmit;
	protected Button btnRemove;
	
	protected Collection nodes;
	protected int iCurrent;
	protected String[] taxonomyName;
	
	public void init(){
	}
	
	public void onRequest(Event ev) {
		iCurrent=0;
		contentSelectBox = new ContentPopupSelectBox("contentSelectBox");
		
		btnSubmit = new Button("btnSubmit",Application.getInstance().getMessage("taxonomy.button.add","Add"));
		btnRemove = new Button("btnRemove",Application.getInstance().getMessage("taxonomy.button.remove","Remove"));
		
		addChild(contentSelectBox);
		addChild(btnSubmit);
		addChild(btnRemove);
		contentSelectBox.init();
		
		TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
		nodes = mod.formTaxonomyTree(null);
		int iTotalNodes = mod.getNodesTotal(0)+mod.getNodesTotal(1);
		
		cbNodes = new CheckBox[iTotalNodes];
		taxonomyName = new String[iTotalNodes];
		if (nodes!=null && nodes.size()>0) {
			for (Iterator i=nodes.iterator();i.hasNext();) {
				TaxonomyNode node = (TaxonomyNode)i.next();
				cbNodes[iCurrent] = new CheckBox("cbNodes"+iCurrent);
				cbNodes[iCurrent].setValue(node.getTaxonomyId());
				taxonomyName[iCurrent] = new String();
				taxonomyName[iCurrent] = node.getTaxonomyName();
				
				addChild(cbNodes[iCurrent]);
				iCurrent++;
				addCheckBox(node);
			}
		}
		
	}
	
	private void addCheckBox(TaxonomyNode node) {
		if (node.getChildNodes()!=null) {
			for (Iterator i=node.getChildNodes().iterator();i.hasNext();) {
				TaxonomyNode childNode = (TaxonomyNode)i.next();
				cbNodes[iCurrent] = new CheckBox("cbNodes"+iCurrent);
				cbNodes[iCurrent].setValue(childNode.getTaxonomyId());
				taxonomyName[iCurrent] = new String();
				taxonomyName[iCurrent] = childNode.getTaxonomyName();
				
				addChild(cbNodes[iCurrent]);
				iCurrent++;
				addCheckBox(childNode);
			}
		}
	}
	
	public Forward onValidate(Event ev) {
		Forward forward = new Forward("");
		
		String[] selectedContentIds = contentSelectBox.getIds();
		if (selectedContentIds==null || selectedContentIds.length<=0) {
			forward = new Forward("errorContent");
		}
		else {
			boolean isCheck = false;
			Collection col = new ArrayList();
			for (int i=0; i<cbNodes.length;i++) {
				if (cbNodes[i].isChecked()) {
					isCheck=true;
					col.add(cbNodes[i].getValue());
				}
			}
			if (!isCheck) {
				forward = new Forward("errorTaxonomy");
			}
			else {
				TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
				if (btnSubmit.getAbsoluteName().equals(findButtonClicked(ev))) {
					// perform insert / update 
					if (col!=null && col.size()>0) {
						for (Iterator i=col.iterator();i.hasNext();) {
							String s = (String)i.next();
							TaxonomyMap map = new TaxonomyMap();
							map.setTaxonomyId(s);
							map.setMapBy(getWidgetManager().getUser().getId());
							map.setMapDate(Calendar.getInstance().getTime());
							for (int j=0; j<selectedContentIds.length; j++) {
								map.setContentId(selectedContentIds[j]);
								mod.addMapping(map);
							}
						}
					}
					forward = new Forward("added");
					
				}
				else if (btnRemove.getAbsoluteName().equals(findButtonClicked(ev))) {
					// perform delete 
					if (col!=null && col.size()>0) {
						for (Iterator i=col.iterator();i.hasNext();) {
							String s = (String)i.next();
							TaxonomyMap map = new TaxonomyMap();
							map.setTaxonomyId(s);
							map.setMapBy(getWidgetManager().getUser().getId());
							map.setMapDate(Calendar.getInstance().getTime());
							for (int j=0; j<selectedContentIds.length; j++) {
								map.setContentId(selectedContentIds[j]);
								mod.deleteMapping(map);
							}
						}
					}
					forward = new Forward("removed");
				}
				
			}
			
		}
		return forward;
	}
	
	public String getDefaultTemplate() {
		return "taxonomy/massTagging";
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void setBtnSubmit(Button btnSubmit) {
		this.btnSubmit = btnSubmit;
	}

	public CheckBox[] getCbNodes() {
		return cbNodes;
	}

	public void setCbNodes(CheckBox[] cbNodes) {
		this.cbNodes = cbNodes;
	}

	public ContentPopupSelectBox getContentSelectBox() {
		return contentSelectBox;
	}

	public void setContentSelectBox(ContentPopupSelectBox contentSelectBox) {
		this.contentSelectBox = contentSelectBox;
	}

	public String[] getTaxonomyName() {
		return taxonomyName;
	}

	public void setTaxonomyName(String[] taxonomyName) {
		this.taxonomyName = taxonomyName;
	}

	public Collection getNodes() {
		return nodes;
	}

	public void setNodes(Collection nodes) {
		this.nodes = nodes;
	}
	
	public void setBtnRemove(Button btnRemove) {
		this.btnRemove=btnRemove;
	}
	
	public Button getBtnRemove() {
		return this.btnRemove;
	}
	
	
}
