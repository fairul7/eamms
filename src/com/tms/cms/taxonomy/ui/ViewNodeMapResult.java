package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;

import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.taxonomy.model.TaxonomyMap;
import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class ViewNodeMapResult extends Widget {
    private TaxonomyMap[] mapResult;
    private String taxonomyId;
    private TaxonomyNode selectedNode;
    private String page;
    private int iPage;
    private int RESULT_PER_PAGE=10;
    private Collection pageMap;

    public void init() {
        String resultPerPage = Application.getInstance().getProperty("TaxonomyMappedResultPerPage");
        if (resultPerPage!=null && !resultPerPage.equals("")) {
            try {
                RESULT_PER_PAGE = Integer.parseInt(resultPerPage);
            }
            catch(Exception e) { }
        }

    }

    public String getDefaultTemplate() {
        return "taxonomy/viewNodeMapResult";
    }

    public void initMapResult() {
        mapResult=null;
        iPage=0;
        if (page!=null && !page.equals("")) {
            try {
                iPage = Integer.parseInt(page);
            }
            catch(Exception e) {

            }
        }
        else {
            page="1";
        }
        
        User user = getWidgetManager().getUser();
        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        //mapResult = mod.getMappingByNode(iTaxonomyId,user);
        mapResult = mod.getMappingForPublic(taxonomyId,user,1,(iPage-1)*RESULT_PER_PAGE,RESULT_PER_PAGE);
        selectedNode = mod.getNode(taxonomyId);

        int iTotalRecord = mod.getMappingTotalForPublic(taxonomyId, new Integer(1));
        pageMap = new ArrayList();
        if (iTotalRecord>RESULT_PER_PAGE) {
            int totalPageCount = iTotalRecord/RESULT_PER_PAGE;
            int pageLeft = iTotalRecord%RESULT_PER_PAGE;
            if (pageLeft>0) {
                totalPageCount++;
            }
            for(int i=0;i<totalPageCount;i++) {
                pageMap.add(""+(i+1));
            }
        }
    }
    
    //this action only trigger in cmsadmin to link the contentId to the contentView.jsp
    public Forward actionPerformed(Event evt){
    	Forward forward = new Forward("failure");
    	if(evt.getType().equals("sel")){
	    	String id = evt.getRequest().getParameter("contentId");
	        ContentHelper.setId(evt, id);
	        forward = new Forward("selection");
    	}
        return forward;
    }

    public void setMapResult(TaxonomyMap[] mapResult) {
        this.mapResult = mapResult;
    }

    public TaxonomyMap[] getMapResult() {
        return mapResult;
    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId=taxonomyId;
        initMapResult();
    }

    public String getTaxonomyId() {
        return taxonomyId;
    }

    public TaxonomyNode getSelectedNode()
    {
        return selectedNode;
    }

    public void setSelectedNode(TaxonomyNode selectedNode)
    {
        this.selectedNode = selectedNode;
    }

    public void setPage(String page) {
        this.page = page;
        initMapResult();
    }

    public String getPage() {
        return this.page;
    }

    public Collection getPageMap() {
        return pageMap;
    }
}

