/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import kacang.stdui.Table;

public class ClaimFormIndexGenericTable extends Table  
{

	protected String userId;

	public void init() 
	{
        setMethod("POST");
		userId = getWidgetManager().getUser().getId();
	}

}
