package com.tms.cms.digest.ui;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.digest.model.DigestException;
import com.tms.cms.digest.model.DigestIssueDataObject;
import com.tms.cms.digest.model.DigestModule;

import kacang.Application;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;

public class DigestIssuePath extends Widget{
	private String digestIssueId;
	private DigestIssueDataObject dido;

	public String getDigestIssueId() {
		return digestIssueId;
	}

	public void setDigestIssueId(String digestIssueId) {
		this.digestIssueId = digestIssueId;
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
        return "digest/digestIssuePath";
    }

    public void onRequest(Event evt) {
        DigestModule dm = (DigestModule)Application.getInstance().getModule(DigestModule.class);
        dido= new DigestIssueDataObject();
        if (digestIssueId != null) {
        	try {
				dido=dm.getDigestIssueDo(digestIssueId);
			} catch (DigestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public Forward actionPerformed(Event evt) {
        Forward forward = super.actionPerformed(evt);        
        return forward;
    }
    
}
