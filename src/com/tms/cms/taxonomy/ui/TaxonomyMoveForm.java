package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Form;
import kacang.stdui.Radio;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class TaxonomyMoveForm extends Form {
	
	//public Radio[] radioButtons;
	public String[] shown;
	public Collection nodes;
	public Collection selectedNodes;
	//public Button btnMove;
	public Button btnCancel;
	//public ButtonGroup bgNodes;
	public TaxonomyNode selectedNode;
	
	public String id;
	public boolean selectId;
	private int iCurrent;
	
	public void init(){
		nodes=new ArrayList();
	}
	
	public String getDefaultTemplate() {
		return "taxonomy/moveForm";
	}
	
	public void onRequest(Event ev) {
		selectId=true;
		selectedNode = new TaxonomyNode();
		if (id==null||id.equals("")) {
			selectId=false;
		}
		else {
			TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
			selectedNode = mod.getNode(id);
			
			nodes = mod.formAdminTaxonomyTree(null);
			selectedNodes = mod.getNodesByParent(id, -1);
			
			Collection col = new ArrayList();
			
			for (Iterator i=selectedNodes.iterator(); i.hasNext();) {
				TaxonomyNode nd = (TaxonomyNode)i.next();
				col.add(nd.getTaxonomyId());
			}
			col.add(id);
			
			int iTotalNodes = mod.getNodesTotal(-1);
			//radioButtons = new Radio[iTotalNodes];
			shown = new String[iTotalNodes];
			iCurrent = 0;
			//bgNodes = new ButtonGroup("nodes");
			//addChild(bgNodes);
			
			if (nodes!=null && nodes.size()>0) {
			
				for (Iterator i=nodes.iterator();i.hasNext();) {
					TaxonomyNode node = (TaxonomyNode)i.next();
					shown[iCurrent] = "1";
					//radioButtons[iCurrent] = new Radio("radio"+iCurrent);
					//radioButtons[iCurrent].setValue(node.getTaxonomyId());
					if (col.contains(node.getTaxonomyId())) {
						shown[iCurrent]="0";
						//radioButtons[iCurrent].setHidden(true);
					}
					//bgNodes.addButton(radioButtons[iCurrent]);
					//addChild(radioButtons[iCurrent]);
					iCurrent++;
				
					addRadioButton(node,col);
				}
			}
			//btnMove = new Button("btnMove",Application.getInstance().getMessage("taxonomy.label.move","Move Taxonomy"));
			//addChild(btnMove);
			btnCancel= new Button("btnCancel",Application.getInstance().getMessage("taxonomy.label.cancel","Cancel"));
			addChild(btnCancel);
		}
	}
	
    public void addRadioButton(TaxonomyNode node, Collection col) {

        if(node.getChildNodes() != null){
            for (Iterator child=node.getChildNodes().iterator();child.hasNext();) {
                TaxonomyNode childNode = (TaxonomyNode)child.next();
                //radioButtons[iCurrent] = new Radio("radio"+iCurrent);
                //radioButtons[iCurrent].setValue(childNode.getTaxonomyId().toString());
                shown[iCurrent]="1";
                
                if (col.contains(childNode.getTaxonomyId())) {
                	//radioButtons[iCurrent].setHidden(true);
                	shown[iCurrent]="0";
                }
                //bgNodes.addButton(radioButtons[iCurrent]);
                //addChild(radioButtons[iCurrent]);
                iCurrent++;

                addRadioButton(childNode, col);
            }
        }
    }
	
	public Forward onValidate(Event ev) {
		Forward forward = new Forward();
		/*
		if (btnMove.getAbsoluteName().equals(findButtonClicked(ev))) {
			String checkedId = "";
			for (int i=0;i<radioButtons.length;i++) {
				if (radioButtons[i].isChecked()) {
					checkedId = (String)radioButtons[i].getValue();
				}
			}
			if (checkedId==null || checkedId.equals("")) {
				return new Forward("errorId");
			}
			
			// perform move action
			if (id!=null && !id.equals("")) {
				TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
				TaxonomyNode node = mod.getNode(id);
				//TaxonomyNode parentNode = mod.getNode(checkedId);
				node.setParentId(checkedId);
				mod.updateNode(node);
			}
			forward = new Forward("success");
		}
		*/
		return forward;
	}

	/*
	public ButtonGroup getBgNodes() {
		return bgNodes;
	}

	public void setBgNodes(ButtonGroup bgNodes) {
		this.bgNodes = bgNodes;
	}
	*/

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}

	/*
	public Button getBtnMove() {
		return btnMove;
	}

	public void setBtnMove(Button btnMove) {
		this.btnMove = btnMove;
	}
	*/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection getNodes() {
		return nodes;
	}

	public void setNodes(Collection nodes) {
		this.nodes = nodes;
	}

	/*
	public Radio[] getRadioButtons() {
		return radioButtons;
	}

	public void setRadioButtons(Radio[] radioButtons) {
		this.radioButtons = radioButtons;
	}
	*/

	public TaxonomyNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TaxonomyNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public Collection getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(Collection selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public boolean isSelectId() {
		return selectId;
	}

	public void setSelectId(boolean selectId) {
		this.selectId = selectId;
	}
	
	public String[] getShown() {
		return shown;
	}
	
	

}
