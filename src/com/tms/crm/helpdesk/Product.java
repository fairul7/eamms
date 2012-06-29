package com.tms.crm.helpdesk;

import kacang.model.DefaultDataObject;
import kacang.services.security.User;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Iterator;

public class Product extends DefaultDataObject
{
	public static final String DEFAULT_DELIMITER = "\n";

	private String productId;
	private String productName;
	private String description;
	private Collection features;
	private Collection owners;

	public Product()
	{
		features = new ArrayList();
		owners = new ArrayList();
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Collection getFeatures()
	{
		return features;
	}

	public void setFeatures(Collection features)
	{
		this.features = features;
	}

	public Collection getOwners()
	{
		return owners;
	}

	public void setOwners(Collection owners)
	{
		this.owners = owners;
	}

	public void addOwner(User user)
	{
		owners.add(user);
	}

	/**
	 * Facade for the HelpdeskDao
	 * @return a \n delimited string of product features
	 */
	public String getProductFeatures()
	{
		String value = "";
		for (Iterator i = features.iterator(); i.hasNext();)
		{
			String feature = (String) i.next();
			if(!("".equals(value)))
				value += DEFAULT_DELIMITER;
            value += feature;
		}
		return value;
	}

	/**
	 * Facade for the HelpdeskDao
	 * @param productFeatures
	 */
	public void setProductFeatures(String productFeatures)
	{
		StringTokenizer tokenizer = new StringTokenizer(productFeatures, DEFAULT_DELIMITER);
		features = new ArrayList();
		while(tokenizer.hasMoreTokens())
			features.add(tokenizer.nextToken());
	}
}
