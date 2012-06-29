package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.cms.taxonomy.model.TaxonomyMap;
import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class TaxonomyMappingAdd extends Form {

    protected Collection parentNode;
    protected String contentId;
    protected CheckBox[] cbNodes;
    protected Button btnSubmit;
    protected Button btnCancel;

    private int iCurrent;
    
    protected Map nodeWithMap;
    
    public TaxonomyMappingAdd(){
    }
    
    public TaxonomyMappingAdd(String name){
    	super(name);
    }
    
    public void init() {
        
    	TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        

        parentNode = new ArrayList();
    	
        //original code
//        Collection col = mod.getNodesByParent(0,null);
//        for (Iterator i=col.iterator();i.hasNext();) {
//            TaxonomyNode node = (TaxonomyNode)i.next();
//            node = mod.getTaxonomyTree(node.getTaxonomyId().intValue(),null);
//            parentNode.add(node);
//        }
        
        //new code
        parentNode = mod.formTaxonomyTree(null);
    }
    
    public void onRequest(Event ev) {
        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);

        parentNode = new ArrayList();
        parentNode = mod.formTaxonomyTree(null);
        
        int iTotalNodes = mod.getNodesTotal(-1);
        cbNodes = new CheckBox[iTotalNodes];
        
        //new code
        nodeWithMap = new SequencedHashMap();
        nodeWithMap = mod.getNodesWithMapping(contentId);
        
        iCurrent=0;
        for (Iterator iterator=parentNode.iterator();iterator.hasNext();) {
            TaxonomyNode node = (TaxonomyNode)iterator.next();
            cbNodes[iCurrent] = new CheckBox("cbNodes"+iCurrent);
            cbNodes[iCurrent].setValue(node.getTaxonomyId().toString());
            
            //original code
//            if (mod.isMapped(node.getTaxonomyId().intValue(),contentId)) {
//                cbNodes[iCurrent].setChecked(true);
//            }
            
            //new code
            if(nodeWithMap != null || nodeWithMap.size() > 0){
	            TaxonomyNode tempNode = (TaxonomyNode)nodeWithMap.get(node.getTaxonomyId());
	            if(tempNode != null){
	            	cbNodes[iCurrent].setChecked(true);
	            }
            }
            
            addChild(cbNodes[iCurrent]);
            iCurrent++;
            
            addCheckBox(node,mod);
        }

        btnSubmit = new Button("btnSubmit",Application.getInstance().getMessage("taxonomy.label.submit","Submit"));
        addChild(btnSubmit);
        btnCancel = new Button("btnCancel",Application.getInstance().getMessage("taxonomy.label.cancel","Cancel"));
        addChild(btnCancel);

    }

    public void addCheckBox(TaxonomyNode node, TaxonomyModule mod) {
    	
    	if(node.getChildNodes() != null){
	        for (Iterator child=node.getChildNodes().iterator();child.hasNext();) {
	            TaxonomyNode childNode = (TaxonomyNode)child.next();
	            cbNodes[iCurrent] = new CheckBox("cbNodes"+iCurrent);
	            cbNodes[iCurrent].setValue(childNode.getTaxonomyId().toString());
	            
	            //original code
//	            if (mod.isMapped(childNode.getTaxonomyId().intValue(),contentId)) {
//	                cbNodes[iCurrent].setChecked(true);
//	            }
	            
	            //new code
	            if(nodeWithMap != null || nodeWithMap.size() > 0){
		            TaxonomyNode tempNode = (TaxonomyNode)nodeWithMap.get(childNode.getTaxonomyId());
		            if(tempNode != null){
		            	cbNodes[iCurrent].setChecked(true);
		            }
	            }
	            
	            addChild(cbNodes[iCurrent]);
	            iCurrent++;
	
	            addCheckBox(childNode,mod);
	        }
    	}
        
    }

    public String getDefaultTemplate() {
        return "taxonomy/mappingAddForm";
    }

    public Forward onValidate(Event event) {
        if (btnSubmit.getAbsoluteName().equals(findButtonClicked(event))) {
            Collection col = new ArrayList();
            for (int i=0; i<cbNodes.length;i++) {
                 if (cbNodes[i].isChecked()) {
                     String sId = (String)cbNodes[i].getValue();
                     col.add(sId);
                 }
            }


            if (col!=null && col.size()>0) {
                TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
                for (Iterator i=col.iterator();i.hasNext();) {
                    String s = (String)i.next();
                    TaxonomyMap map = new TaxonomyMap();
                    map.setContentId(contentId);
                    map.setMapBy(getWidgetManager().getUser().getId());
                    map.setMapDate(Calendar.getInstance().getTime());
                    map.setTaxonomyId(s);

                    mod.addMapping(map);
                }
            }

            return new Forward("added");
        }
        else if (btnCancel.getAbsoluteName().equals(findButtonClicked(event))) {
            return new Forward("cancel");
        }
        return null;
    }

    public Collection getParentNode() {
        return parentNode;
    }

    public void setParentNode(Collection parentNode) {
        this.parentNode = parentNode;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public CheckBox[] getCbNodes() {
        return cbNodes;
    }

    public void setCbNodes(CheckBox[] cbNodes) {
        this.cbNodes = cbNodes;
    }

    public Button getBtnSubmit() {
        return btnSubmit;
    }

    public void setBtnSubmit(Button btnSubmit) {
        this.btnSubmit = btnSubmit;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }
}
