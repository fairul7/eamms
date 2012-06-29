package com.tms.crm.helpdesk.ui;

import com.tms.crm.helpdesk.HelpdeskException;
import com.tms.crm.helpdesk.HelpdeskHandler;
import com.tms.util.FormatUtil;
import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.*;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.Log;

import java.util.Collection;
import java.util.Map;
import java.text.DecimalFormat;

public class IncidentSearchTable extends Table {
    public static final String FORWARD_ADD = "add";
    public static final String FORWARD_DELETE = "delete";

    protected IncidentSearchTableModel model;
    protected IncidentSearchCriteriaForm incidentSearchForm;
    private String companyId;
    private String contactId;

    public IncidentSearchTable() {
        super();
    }

    public IncidentSearchTable(String s) {
        super(s);
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public IncidentSearchCriteriaForm getIncidentSearchForm() {
        return incidentSearchForm;
    }

    public void setIncidentSearchForm(IncidentSearchCriteriaForm incidentSearchForm) {
        this.incidentSearchForm = incidentSearchForm;
    }

    public void init() {
        super.init();
        setWidth("100%");
        setMultipleSelect(true);
        setShowPageSize(false);
        model = new IncidentSearchTableModel();
        setModel(model);
    }

    public void onRequest(Event event) {
        super.onRequest(event);
    }

    public class IncidentSearchTableModel extends TableModel {
        public IncidentSearchTableModel() {
            super();
            Application app = Application.getInstance();
            //Table columns
            TableColumn colCode = new TableColumn("incidentCode", app.getMessage("helpdesk.label.incidentNo"));
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
            TableColumn colSubject = new TableColumn("subject", app.getMessage("helpdesk.label.subject"));
            TableColumn colCreated = new TableColumn("created", app.getMessage("helpdesk.label.dateCreated"));
            TableDateFormat dateFormat = new TableDateFormat(FormatUtil.getInstance().getLongDateTimeFormat());
            colCreated.setFormat(dateFormat);
            TableColumn colProduct = new TableColumn("productName", app.getMessage("helpdesk.label.product"));

            TableColumn colResolved = new TableColumn("resolved", app.getMessage("helpdesk.label.resolved"));
            String contextPath = (String) getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat resolvedFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            colResolved.setFormat(resolvedFormat);

            TableColumn colSeverity = new TableColumn("severity", app.getMessage("helpdesk.label.severity"));
            HelpdeskHandler handler = (HelpdeskHandler) app.getModule(HelpdeskHandler.class);
            Map severityMap = null;
            try {
                severityMap = handler.getSeverityOptions();
                TableStringFormat severityFormat = new TableStringFormat(severityMap);
                colSeverity.setFormat(severityFormat);
            }
            catch (HelpdeskException e) {
                // ignore, module already logs message
            }

            addColumn(colCode);
            addColumn(colSubject);
            addColumn(colCreated);
            addColumn(colProduct);
            addColumn(colResolved);
            addColumn(colSeverity);
            //Actions
            User user = getWidgetManager().getUser();
            SecurityService service = (SecurityService) app.getService(SecurityService.class);
            try {
                if (service.hasPermission(user.getId(), HelpdeskHandler.PERMISSION_HELPDESK_USER, HelpdeskHandler.class.getName(), null)) {
//					addAction(new TableAction("add", "Add"));
                    colCode.setUrlParam("incidentId");
                }
                if (service.hasPermission(user.getId(), HelpdeskHandler.PERMISSION_HELPDESK_ADMIN, HelpdeskHandler.class.getName(), null)) {
                    TableAction action = new TableAction("delete", app.getMessage("general.label.delete"));
                    action.setMessage(app.getMessage("helpdesk.message.confirmDeletion"));
                    addAction(action);
                }
            }
            catch (kacang.services.security.SecurityException e) {
                Log.getLog(getClass()).error("", e);
            }

            // filters
            TableFilter filter = new TableFilter("incidentSearchForm");
            incidentSearchForm = new IncidentSearchCriteriaForm("incidentSearchForm");
            incidentSearchForm.init();
            filter.setWidget(incidentSearchForm);
            addFilter(filter);

        }

        public Collection getTableRows() {
            Collection list = null;
            try {
                HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
                list = handler.getIncidents(generateQuery(), getStart(), getRows(), getSort(), isDesc());
            }
            catch (HelpdeskException e) {
                Log.getLog(getClass()).error("Error while retrieving incidents", e);
            }
            return list;
        }

        public int getTotalRowCount() {
            int count = 0;
            try {
                HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
                count = handler.getIncidentsCount(generateQuery());
            }
            catch (HelpdeskException e) {
                Log.getLog(getClass()).error("Error while retrieving incident count", e);
            }
            return count;
        }

        public Forward processAction(Event event, String action, String[] selectedKeys) {
            Forward forward = super.processAction(event, action, selectedKeys);
            if ("add".equals(action))
                forward = new Forward(FORWARD_ADD);
            else if ("delete".equals(action)) {
                SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
                HelpdeskHandler handler = (HelpdeskHandler) Application.getInstance().getModule(HelpdeskHandler.class);
                User user = getWidgetManager().getUser();
                try {
                    if (service.hasPermission(user.getId(), HelpdeskHandler.PERMISSION_HELPDESK_ADMIN, HelpdeskHandler.class.getName(), null)) {
                        for (int i = 0; i < selectedKeys.length; i++)
                            handler.deleteIncident(selectedKeys[i]);
                    }
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error("Error while deleting incidents", e);
                }
                forward = new Forward(FORWARD_DELETE);
            }
            return forward;
        }

        public DaoQuery generateQuery() {
            DaoQuery query = new DaoQuery();

            // check company id
            String companyId = getCompanyId();
            if (companyId != null && companyId.trim().length() > 0) {
                query.addProperty(new OperatorEquals("companyId", companyId, DaoOperator.OPERATOR_AND));
            }

            // check contact id
            String contactId = getContactId();
            if (contactId != null && contactId.trim().length() > 0) {
                query.addProperty(new OperatorEquals("contactId", contactId, DaoOperator.OPERATOR_AND));
            }

            // get filter values
            IncidentSearchCriteriaForm searchForm = getIncidentSearchForm();

            boolean searchByIncidentCode = true;
            int incidentNo = -1;
            String incidentCode = searchForm.getIncidentCode();
            try {
                incidentNo = Integer.parseInt(incidentCode);
            }
            catch (Exception e) {
                searchByIncidentCode = false;
            }

            if (searchByIncidentCode) {
                query.addProperty(new OperatorEquals("incidentCode", new Integer(incidentNo), DaoOperator.OPERATOR_AND));

            }
            else {

                String search = searchForm.getSearchText();
                boolean searchSubject = searchForm.getSearchSubject();
                boolean searchDescription = searchForm.getSearchDescription();
                boolean searchResolution = searchForm.getSearchResolution();
                boolean performTextSearch = searchSubject || searchDescription || searchResolution;
                if (performTextSearch && !(search == null || "".equals(search))) {
                    OperatorParenthesis parenthesis = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
                    String operator = null;
                    if (searchSubject) {
                        parenthesis.addOperator(new OperatorLike("subject", search, operator));
                        operator = DaoOperator.OPERATOR_OR;
                    }
                    if (searchDescription) {
                        parenthesis.addOperator(new OperatorLike("description", search, operator));
                        operator = DaoOperator.OPERATOR_OR;
                    }
                    if (searchResolution) {
                        parenthesis.addOperator(new OperatorLike("resolution", search, operator));
                    }
                    query.addProperty(parenthesis);
                }

                if (searchForm.getResolved() != null) {
                    query.addProperty(new OperatorEquals("resolved", searchForm.getResolved(), DaoOperator.OPERATOR_AND));
                }

                if (searchForm.getSeverity() != null) {
                    query.addProperty(new OperatorEquals("severity", searchForm.getSeverity(), DaoOperator.OPERATOR_AND));
                }

                if (searchForm.getIncidentType() != null) {
                    query.addProperty(new OperatorEquals("incidentType", searchForm.getIncidentType(), DaoOperator.OPERATOR_AND));
                }

                if (searchForm.getDateCreatedFrom() != null) {
                    query.addProperty(new OperatorGreaterThanEquals("created", searchForm.getDateCreatedFrom(), DaoOperator.OPERATOR_AND));
                }

                if (searchForm.getDateCreatedTo() != null) {
                    query.addProperty(new OperatorLessThanEquals("created", searchForm.getDateCreatedTo(), DaoOperator.OPERATOR_AND));
                }

                if (searchForm.getDateModifiedFrom() != null) {
                    query.addProperty(new OperatorGreaterThanEquals("lastModified", searchForm.getDateModifiedFrom(), DaoOperator.OPERATOR_AND));
                }

                if (searchForm.getDateModifiedTo() != null) {
                    query.addProperty(new OperatorLessThanEquals("lastModified", searchForm.getDateModifiedTo(), DaoOperator.OPERATOR_AND));
                }
            }

            return query;
        }

        public String getTableRowKey() {
            return "incidentId";
        }
    }

    public class OperatorGreaterThanEquals extends OperatorBase {

        public OperatorGreaterThanEquals() {
        }

        public OperatorGreaterThanEquals(String property, Object value, String operator) {
            super(property, value, operator);
        }

        public String getQueryString() {
            return " " + getOperator() + " (" + getProperty() + " >= ?)";
        }
    }

    public class OperatorLessThanEquals extends OperatorBase {

        public OperatorLessThanEquals() {
        }

        public OperatorLessThanEquals(String property, Object value, String operator) {
            super(property, value, operator);
        }

        public String getQueryString() {
            return " " + getOperator() + " (" + getProperty() + " <= ?)";
        }

    }

}
