package com.tms.crm.helpdesk.ui;

import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.Log;
import com.tms.crm.helpdesk.Product;
import com.tms.crm.helpdesk.HelpdeskHandler;
import com.tms.crm.helpdesk.HelpdeskException;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class ProductOpen extends ProductForm
{
	public ProductOpen()
	{
		super();
	}

	public ProductOpen(String s)
	{
		super(s);
	}

	public void onRequest(Event event)
	{
		super.onRequest(event);
		if(!isKeyEmpty())
		{
			try
			{
				HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
				Product product = handler.getProduct(productId);
				if(product != null)
				{
					productName.setValue(product.getProductName());
					description.setValue(product.getDescription());
					features.setValue(product.getProductFeatures());
					//Populating Users
					Map options = new HashMap();
					for (Iterator i = product.getOwners().iterator(); i.hasNext();)
					{
						User user = (User) i.next();
						options.put(user.getId(), user.getName());
					}
					owners.setOptionMap(options);
				}
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error while retrieving product " + productId, e);
			}
		}
	}

	public Forward onValidate(Event event)
	{
		Forward forward = super.onValidate(event);
		try
		{
			Product product = generateProduct();
			HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
			handler.updateProduct(product);
			forward = new Forward(FORWARD_SUCCESS);
			init();
		}
		catch (HelpdeskException e)
		{
			Log.getLog(getClass()).error("Error while updating product " + productId, e);
			forward = new Forward(FORWARD_FAILED);
		}
		return forward;
	}

	protected boolean isKeyEmpty()
	{
		if(productId == null || "".equals(productId))
			return true;
		return false;
	}

	public void setProductId(String productId)
	{
		super.setProductId(productId);
		init();
	}
}
