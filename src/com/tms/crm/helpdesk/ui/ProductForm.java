package com.tms.crm.helpdesk.ui;

import com.tms.crm.helpdesk.HelpdeskException;
import com.tms.crm.helpdesk.HelpdeskHandler;
import com.tms.crm.helpdesk.Product;
import com.tms.ekms.security.ui.UsersSelectBox;
import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorIn;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.ArrayList;

public class ProductForm extends Form
{
	public static final String FORWARD_SUCCESS = "success";
	public static final String FORWARD_FAILED = "failed";
	public static final String FORWARD_CANCEL = "cancel";

	protected TextField productName;
	protected TextBox description;
	protected TextBox features;
    protected Button submit;
	protected Button cancel;
    protected UsersSelectBox owners;

	protected ValidatorNotEmpty validName;

	protected String productId;

	public ProductForm()
	{
		super();
	}

	public ProductForm(String s)
	{
		super(s);
	}

	public String getDefaultTemplate() {
		return "helpdesk/productAdd";
	}
	
	
	public void init()
	{
		super.init();
        if(!isKeyEmpty())
		{
			setColumns(2);
			setMethod("post");

            Application application = Application.getInstance();
			productName = new TextField("productName");
			productName.setSize("35");
			productName.setMaxlength("250");
			description = new TextBox("description");
			description.setRows("7");
			description.setCols("35");
			features = new TextBox("features");
			features.setRows("15");
			features.setCols("35");
			owners = new UsersSelectBox("owners");
			submit = new Button("submit", application.getMessage("helpdesk.label.productSubmit"));
			cancel = new Button("cancel", application.getMessage("helpdesk.label.productCancel"));

			validName = new ValidatorNotEmpty("validName");
			productName.addChild(validName);

			Panel panel = new Panel("panel");
			panel.setColumns(1);
			panel.addChild(features);
			panel.addChild(new Label("labelFeatures", application.getMessage("helpdesk.message.separateOptions")));
			Panel buttons = new Panel("button");
			buttons.addChild(submit);
			buttons.addChild(cancel);

			addChild(new Label("labelName", application.getMessage("helpdesk.label.productName")+" *"));
			addChild(productName);
			addChild(new Label("labelDescription", application.getMessage("helpdesk.label.productDescription")));
			addChild(description);
			addChild(new Label("labelFeatures", application.getMessage("helpdesk.label.productFeatures")));
			addChild(panel);
			addChild(new Label("labelOwners", application.getMessage("helpdesk.label.productOwners")));
			addChild(owners);
			addChild(new Label("labelButton", ""));
			addChild(buttons);

			owners.init();
		}
	}

	public Forward actionPerformed(Event event)
	{
		Forward forward = null;
		if(!isKeyEmpty())
		{
			if(cancel.getAbsoluteName().equals(findButtonClicked(event)))
				forward = new Forward(FORWARD_CANCEL);
			else
				forward = super.actionPerformed(event);
		}
		return forward;
	}

	public void onRequest(Event event)
	{
		if(!isKeyEmpty())
		{
			super.onRequest(event);
			validName.setText(Application.getInstance().getMessage("helpdesk.message.productNameRequired"));
		}
	}

	public Forward onSubmit(Event event)
	{
		Forward forward = super.onSubmit(event);

		try
		{
			DaoQuery query = new DaoQuery();
			query.addProperty(new OperatorEquals("productName", productName.getValue(), DaoOperator.OPERATOR_AND));
			if(!(productId == null || "".equals(productId)))
				query.addProperty(new OperatorEquals("productId", productId, DaoOperator.OPERATOR_NAN));
			HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
			int count = handler.getProductsCount(query);
			if(count > 0)
			{
				setInvalid(true);
				productName.setInvalid(true);
				validName.setInvalid(true);
				validName.setText(Application.getInstance().getMessage("helpdesk.message.productNameInUse"));
			}
		}
		catch (HelpdeskException e)
		{
			Log.getLog(getClass()).error("Error while retrieving product", e);
		}

		return forward;
	}

    protected Product generateProduct()
	{
		Product product = new Product();
		if(!(productId == null || "-1".equals(productId)))
			product.setProductId(productId);
		else
			product.setProductId(UuidGenerator.getInstance().getUuid());
		product.setProductName((String) productName.getValue());
		product.setDescription((String) description.getValue());
		product.setProductFeatures((String) features.getValue());
		/* Populating owners */
		try
		{
            if (owners.getIds() != null && owners.getIds().length > 0)
            {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorIn("id", owners.getIds(), DaoOperator.OPERATOR_AND));
                product.setOwners(service.getUsers(query, 0, -1, "firstName", false));
            }
            else
            {
                product.setOwners(new ArrayList());
            }
		}
		catch (SecurityException e)
		{
			Log.getLog(getClass()).error("Error while retrieving product owners", e);
		}
		return product;
	}

	protected boolean isKeyEmpty()
	{
		return false;
	}

	/* Getters and Setters */
	public TextField getProductName()
	{
		return productName;
	}

	public void setProductName(TextField productName)
	{
		this.productName = productName;
	}

	public TextBox getDescription()
	{
		return description;
	}

	public void setDescription(TextBox description)
	{
		this.description = description;
	}

	public TextBox getFeatures()
	{
		return features;
	}

	public void setFeatures(TextBox features)
	{
		this.features = features;
	}

	public UsersSelectBox getOwners()
	{
		return owners;
	}

	public void setOwners(UsersSelectBox owners)
	{
		this.owners = owners;
	}

	public Button getSubmit()
	{
		return submit;
	}

	public void setSubmit(Button submit)
	{
		this.submit = submit;
	}

	public Button getCancel()
	{
		return cancel;
	}

	public void setCancel(Button cancel)
	{
		this.cancel = cancel;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public ValidatorNotEmpty getValidName()
	{
		return validName;
	}

	public void setValidName(ValidatorNotEmpty validName)
	{
		this.validName = validName;
	}
}
