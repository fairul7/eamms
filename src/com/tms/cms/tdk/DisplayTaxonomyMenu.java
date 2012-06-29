package com.tms.cms.tdk;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;
import kacang.util.Log;

import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class DisplayTaxonomyMenu extends LightWeightWidget{
	
	protected TaxonomyNode root;
	protected String contentId;
	private int levels;
	
	public void onRequest(Event ev) {
        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        root = new TaxonomyNode();
        root.setId("0");
        root.setTaxonomyName("root");
        
        Collection parent = mod.getNodesByParent("0", 1);
        
        for(Iterator i = parent.iterator(); i.hasNext(); ){
        	TaxonomyNode tax = (TaxonomyNode)i.next();
        	tax.setChildNodes(mod.getNodesByParent(tax.getTaxonomyId(), 1));
        	Collection col = root.getChildNodes();
        	if(col == null){
        		col = new ArrayList();
        		col.add(tax);
        		root.setChildNodes(col);
        	}else{
        		root.getChildNodes().add(tax);
        	}
        }
    }
	
	public String getDefaultTemplate() {
        return "cms/tdk/displayTaxonomyMenu";
    }
	
	public TaxonomyNode getRoot() {
		return root;
	}

	public void setRoot(TaxonomyNode root) {
		this.root = root;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

}
