package com.tms.crm.helpdesk.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.util.Log;
import com.tms.crm.helpdesk.Product;
import com.tms.crm.helpdesk.HelpdeskHandler;
import com.tms.crm.helpdesk.HelpdeskException;

public class ProductAdd extends ProductForm
{
	public ProductAdd()
	{
		super();
	}

	public ProductAdd(String s)
	{
		super(s);
	}

	public Forward onValidate(Event event)
	{
		Forward forward = super.onValidate(event);
		try
		{
			Product product = generateProduct();
			HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
			handler.addProduct(product);
			forward = new Forward(FORWARD_SUCCESS);
			init();
		}
		catch (HelpdeskException e)
		{
			Log.getLog(getClass()).error("Error while adding new product " + productName.getValue(), e);
			forward = new Forward(FORWARD_FAILED);
		}
		return forward;
	}
}
