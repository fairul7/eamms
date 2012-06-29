package com.tms.crm.helpdesk.ui;

import com.tms.crm.helpdesk.HelpdeskException;
import com.tms.crm.helpdesk.HelpdeskHandler;
import kacang.Application;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorLike;
import kacang.stdui.*;
import kacang.util.Log;

import java.util.Collection;
import java.util.ArrayList;

public class ProductTable extends Table
{
	public static final String FORWARD_ADD = "add";
    public static final String FORWARD_DELETE = "delete";

	public ProductTable()
	{
		super();
	}

	public ProductTable(String s)
	{
		super(s);
	}

	public void init()
	{
		super.init();
		setModel(new ProductTableModel());
	}

	public class ProductTableModel extends TableModel
	{
		public ProductTableModel()
		{
			super();

            Application application = Application.getInstance();
			setWidth("100%");
			TableColumn colName = new TableColumn("productName", application.getMessage("helpdesk.label.productName"));
			addColumn(colName);

			User user = getWidgetManager().getUser();
            SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
			try
			{
				if(service.hasPermission(user.getId(), HelpdeskHandler.PERMISSION_HELPDESK_ADMIN, HelpdeskHandler.class.getName(), null))
				{
					addAction(new TableAction("add", application.getMessage("helpdesk.label.productAdd")));
					addAction(new TableAction("delete", application.getMessage("helpdesk.label.productDelete")));
					colName.setUrlParam("productId");
				}
			}
			catch (SecurityException e)
			{
				Log.getLog(getClass()).error("", e);
			}

			TableFilter search = new TableFilter("search");
			addFilter(search);
		}

		public Collection getTableRows()
		{
			Collection list = new ArrayList();
			try
			{
				HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
				list = handler.getProducts(generateQuery(), getStart(), getRows(), getSort(), isDesc());
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error while retrieving products", e);
			}
			return list;
		}

		public int getTotalRowCount()
		{
			int count = 0;
			try
			{
				HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
				count = handler.getProductsCount(generateQuery());
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error while retrieving product count", e);
			}
			return count;
		}

		private DaoQuery generateQuery()
		{
            DaoQuery query = new DaoQuery();
			if(!("".equals(getFilterValue("search"))))
            {
				query.addProperty(new OperatorLike("productName", getFilterValue("search"), DaoOperator.OPERATOR_AND));
				query.addProperty(new OperatorLike("description", getFilterValue("search"), DaoOperator.OPERATOR_OR));
				query.addProperty(new OperatorLike("productFeatures", getFilterValue("search"), DaoOperator.OPERATOR_OR));
			}
			return query;
		}

		public String getTableRowKey()
		{
			return "productId";
		}

		public Forward processAction(Event event, String action, String[] selectedKeys)
		{
			Forward forward = super.processAction(event, action, selectedKeys);
			if("add".equals(action))
				forward = new Forward(FORWARD_ADD);
			else if("delete".equals(action))
			{
				HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
				try
				{
					for(int i = 0; i < selectedKeys.length; i++)
						handler.deleteProduct(selectedKeys[i]);
				}
				catch(Exception e)
				{
					Log.getLog(getClass()).error(e);
				}
				forward = new Forward(FORWARD_DELETE);
			}
			return forward;
		}
	}
}
