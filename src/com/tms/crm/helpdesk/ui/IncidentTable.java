package com.tms.crm.helpdesk.ui;

import com.tms.crm.helpdesk.HelpdeskException;
import com.tms.crm.helpdesk.HelpdeskHandler;
import com.tms.crm.helpdesk.Product;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.DaoOperator;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;
import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.text.DecimalFormat;

public class IncidentTable extends Table
{
	public static final String FORWARD_ADD = "add";
	public static final String FORWARD_DELETE = "delete";

	protected IncidentTableModel model;
    private String companyId;
    private String contactId;

	public IncidentTable()
	{
		super();
	}

	public IncidentTable(String s)
	{
		super(s);
	}

    public String getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(String companyId)
    {
        this.companyId = companyId;
    }

    public String getContactId()
    {
        return contactId;
    }

    public void setContactId(String contactId)
    {
        this.contactId = contactId;
    }

	public void init()
	{
		super.init();
		setWidth("100%");
		setMultipleSelect(true);
		model = new IncidentTableModel();
		setModel(model);
	}

	public void onRequest(Event event)
	{
		super.onRequest(event);
		model.refresh();
	}

	public class IncidentTableModel extends TableModel
	{
		TableColumn colCode;
		TableColumn colSubject;
		TableColumn colCreated;
		TableColumn colProduct;
		TableColumn colResolved;
		TableColumn colSeverity;

		public IncidentTableModel()
		{
			super();
            Application app = Application.getInstance();
			//Table columns
			colCode = new TableColumn("incidentCode", app.getMessage("helpdesk.label.incidentNo"));
            colCode.setFormat(new TableFormat() {
                DecimalFormat df = new DecimalFormat("000");
                public String format(Object value) {
                    try {
                        value = df.format(value);
                    }
                    catch (Exception e) {
                        // ignore
                    }
                    return "<div align=\"right\">#" + value + "</div>";
                }
            });
            colSubject = new TableColumn("subject", app.getMessage("helpdesk.label.subject"));
			colCreated = new TableColumn("created", app.getMessage("helpdesk.label.dateCreated"));
            TableDateFormat dateFormat = new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
            colCreated.setFormat(dateFormat);
			colProduct = new TableColumn("productName", app.getMessage("helpdesk.label.product"));

            colResolved = new TableColumn("resolved", app.getMessage("helpdesk.label.resolved"));
            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat resolvedFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
			colResolved.setFormat(resolvedFormat);

			colSeverity = new TableColumn("severity", app.getMessage("helpdesk.label.severity"));

            addColumn(colCode);
            addColumn(colSubject);
			addColumn(colCreated);
			addColumn(colProduct);
			addColumn(colResolved);
			addColumn(colSeverity);
			//Actions
			User user = getWidgetManager().getUser();
            SecurityService service = (SecurityService) app.getService(SecurityService.class);
			try
			{
				if(service.hasPermission(user.getId(), HelpdeskHandler.PERMISSION_HELPDESK_USER, HelpdeskHandler.class.getName(), null))
				{
//					addAction(new TableAction("add", "Add"));
					colCode.setUrlParam("incidentId");
				}
				if(service.hasPermission(user.getId(), HelpdeskHandler.PERMISSION_HELPDESK_ADMIN, HelpdeskHandler.class.getName(), null))
				{
				    TableAction action = new TableAction("delete", app.getMessage("general.label.delete"));
					action.setMessage(app.getMessage("helpdesk.message.confirmDeletion"));
					addAction(action);
				}
			}
			catch (kacang.services.security.SecurityException e)
			{
				Log.getLog(getClass()).error("", e);
			}
			//Filters
            TableFilter search = new TableFilter("search");
			TableFilter products = new TableFilter("products");
			SelectBox filterProducts = new SelectBox("filterProducts");
			filterProducts.addOption("-1", app.getMessage("helpdesk.label.allProducts"));
			filterProducts.setSelectedOption("-1");
			products.setWidget(filterProducts);

			TableFilter status = new TableFilter("status");
			SelectBox filterStatus = new SelectBox("filterStatus");
			filterStatus.addOption("-1", app.getMessage("helpdesk.label.allStates"));
			filterStatus.addOption("0", app.getMessage("helpdesk.label.unresolved"));
			filterStatus.addOption("1", app.getMessage("helpdesk.label.resolved"));
			filterStatus.setSelectedOption("-1");
            status.setWidget(filterStatus);

			TableFilter severity = new TableFilter("severity");
			SelectBox filterSeverity = new SelectBox("filterSeverity");
            severity.setWidget(filterSeverity);

			addFilter(search);
			addFilter(products);
			addFilter(status);
			addFilter(severity);
		}

		public Collection getTableRows()
		{
            Collection list = null;
			try
			{
				HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
				list = handler.getIncidents(generateQuery(), getStart(), getRows(), getSort(), isDesc());
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error while retrieving incidents", e);
			}
			return list;
		}

		public int getTotalRowCount()
		{
			int count = 0;
			try
			{
				HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
				count = handler.getIncidentsCount(generateQuery());
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error while retrieving incident count", e);
			}
			return count;
		}

		protected void refresh()
		{
			try
			{
                Application app = Application.getInstance();
				HelpdeskHandler handler = (HelpdeskHandler) app.getModule(HelpdeskHandler.class);
				Collection list = handler.getProducts();
				Map options = new SequencedHashMap();
				options.put("-1", app.getMessage("helpdesk.label.allProducts"));
				for (Iterator i = list.iterator(); i.hasNext();)
				{
					Product product = (Product) i.next();
                    options.put(product.getProductId(), product.getProductName());
				}
				((SelectBox) getFilter("products").getWidget()).setOptionMap(options);

				Map severityMap = null;
				try
				{
					severityMap = handler.getSeverityOptions();
					TableStringFormat severityFormat = new TableStringFormat(severityMap);
					colSeverity.setFormat(severityFormat);

					Map optionMap = new SequencedHashMap();
					optionMap.put("-1", app.getMessage("helpdesk.label.allSeverities"));
					optionMap.putAll(severityMap);
					SelectBox filterSeverity = (SelectBox) getFilter("severity").getWidget();
					filterSeverity.setOptionMap(optionMap);
					filterSeverity.setSelectedOption("-1");
				}
				catch (HelpdeskException e) {
					// ignore, module already logs message
				}
			}
			catch (HelpdeskException e)
			{
				Log.getLog(getClass()).error("Error while retrieving products", e);
			}
		}

		public Forward processAction(Event event, String action, String[] selectedKeys)
		{
			Forward forward = super.processAction(event, action, selectedKeys);
			if("add".equals(action))
				forward = new Forward(FORWARD_ADD);
			else if("delete".equals(action))
			{
				SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
				HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
				User user = getWidgetManager().getUser();
				try
				{
					if(service.hasPermission(user.getId(), HelpdeskHandler.PERMISSION_HELPDESK_ADMIN, HelpdeskHandler.class.getName(), null))
					{
					    for(int i = 0; i < selectedKeys.length; i++)
							handler.deleteIncident(selectedKeys[i]);
					}
				}
				catch (Exception e)
				{
					Log.getLog(getClass()).error("Error while deleting incidents", e);
				}
				forward = new Forward(FORWARD_DELETE);
			}
			return forward;
		}

		public DaoQuery generateQuery()
		{
			DaoQuery query = new DaoQuery();

            // check company id
            String companyId = getCompanyId();
            if (companyId != null && companyId.trim().length() > 0)
            {
                query.addProperty(new OperatorEquals("companyId", companyId, DaoOperator.OPERATOR_AND));
            }

            // check contact id
            String contactId = getContactId();
            if (contactId != null && contactId.trim().length() > 0)
            {
                query.addProperty(new OperatorEquals("contactId", contactId, DaoOperator.OPERATOR_AND));
            }

            // get filter values
			String search = (String) getFilterValue("search");
			String products = (String) ((Collection) getFilterValue("products")).iterator().next();
			String status = (String) ((Collection) getFilterValue("status")).iterator().next();
            String severity = (String) ((Collection) getFilterValue("severity")).iterator().next();

			if(!(search == null || "".equals(search)))
			{
				OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
				parenthesis.addOperator(new OperatorLike("subject", search, null));
				parenthesis.addOperator(new OperatorLike("description", search, DaoOperator.OPERATOR_OR));
                parenthesis.addOperator(new OperatorLike("resolution", search, DaoOperator.OPERATOR_OR));
				query.addProperty(parenthesis);
			}
            if(!("-1".equals(products)))
				query.addProperty(new OperatorEquals("productId", products, DaoOperator.OPERATOR_AND));
			if(!("-1".equals(status)))
				query.addProperty(new OperatorEquals("resolved", status, DaoOperator.OPERATOR_AND));
            if(!("-1".equals(severity)))
				query.addProperty(new OperatorEquals("severity", severity, DaoOperator.OPERATOR_AND));
			return query;
		}

		public String getTableRowKey()
		{
			return "incidentId";
		}
	}
}
