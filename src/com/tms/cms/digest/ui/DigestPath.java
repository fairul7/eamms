package com.tms.cms.digest.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.cms.digest.model.DigestDataObject;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;

public class DigestPath extends Widget{
	private String digestId;
	private DigestIssueDataObject dido;
	private DigestDataObject ddo;

	public DigestDataObject getDdo() {
		return ddo;
	}

	public void setDdo(DigestDataObject ddo) {
		this.ddo = ddo;
	}

	public String getDigestId() {
		return digestId;
	}

	public void setDigestId(String digestId) {
		this.digestId = digestId;
	}

	public DigestIssueDataObject getDido() {
		return dido;
	}

	public void setDido(DigestIssueDataObject dido) {
		this.dido = dido;
	}

	public void init() {
    }

    public String getDefaultTemplate() {
        return "digest/digestPath";
    }

    public void onRequest(Event evt) {
        DigestModule dm = (DigestModule)Application.getInstance().getModule(DigestModule.class);
        ddo= new DigestDataObject();
    	dido=new DigestIssueDataObject();
        if (digestId != null) {
        	try {
    			DaoQuery query= new DaoQuery();
    			query.addProperty(new OperatorEquals("cms_digest.digestId", digestId, DaoOperator.OPERATOR_AND));
    			Collection digest = dm.getDigestMain(query);
    			
                
    	        for (Iterator i = digest.iterator(); i.hasNext();)
    	        {
    	        	DigestDataObject ddobj = (DigestDataObject) i.next();
    	        	ddo=ddobj;
    	        	dido=dm.getDigestIssueDo(ddo.getDigestIssueId());
    	        }
    		} catch (DigestException e) {
    			// TODO Auto-generated catch block
    			Log.getLog(getClass()).error(e.toString(), e);
    		}             	
        }
    }

    public Forward actionPerformed(Event evt) {
        Forward forward = super.actionPerformed(evt);        
        return forward;
    }
    
}

