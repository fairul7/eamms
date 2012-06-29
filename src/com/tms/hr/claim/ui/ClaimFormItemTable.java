/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import kacang.stdui.Table;
import kacang.ui.Event;

public class ClaimFormItemTable extends Table  
{

	protected String formId ;
    protected String cn;


    public void setFormId(String formId)
	{ this.formId = formId;}
	public String getFormId()
	{ return this.formId;}

	public void setId(String id)
	{ this.formId = id;}
	
	public void init() 
	{
	//	setModel(new ClaimFormItemTableModel());
        setShowPageSize(false);
	}

	public void onRequest(Event evt)
	{
        setMethod("POST");
        setModel(new ClaimFormItemTableModel(formId, getCn()));
	}

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

}
