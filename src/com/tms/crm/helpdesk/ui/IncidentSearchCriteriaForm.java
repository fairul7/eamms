package com.tms.crm.helpdesk.ui;

import kacang.stdui.*;
import kacang.Application;
import kacang.ui.Event;
import com.tms.crm.helpdesk.HelpdeskHandler;
import com.tms.crm.helpdesk.HelpdeskException;

import java.util.Map;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;

import org.apache.commons.collections.SequencedHashMap;

public class IncidentSearchCriteriaForm extends Form {

    protected TextField tfIncidentCode;
    protected TextField tfSearchText;
    protected CheckBox cbSearchSubject;
    protected CheckBox cbSearchDescription;
    protected CheckBox cbSearchResolution;
    protected SelectBox sbResolved;
    protected SelectBox sbSeverity;
    protected SelectBox sbIncidentType;
    protected DatePopupField dateCreatedFrom;
    protected DatePopupField dateCreatedTo;
    protected DatePopupField dateModifiedFrom;
    protected DatePopupField dateModifiedTo;

    public IncidentSearchCriteriaForm() {
        super();
    }

    public IncidentSearchCriteriaForm(String name) {
        super(name);
    }
    
    public String getDefaultTemplate() {
    	return "helpdesk/incidentSearch";
    }

    public void init() {
        Application app = Application.getInstance();
        setColumns(2);
        setMethod("POST");

        // create fields
        tfIncidentCode = new TextField("tfIncidentCode");
        tfIncidentCode.setSize("6");
        tfSearchText = new TextField("tfSearchText");
        cbSearchSubject = new CheckBox("cbSearchSubject", app.getMessage("helpdesk.label.subject"), true);
        cbSearchDescription = new CheckBox("cbSearchDescription", app.getMessage("helpdesk.label.description"), true);
        cbSearchResolution = new CheckBox("cbSearchResolution", app.getMessage("helpdesk.label.resolution"), true);
        sbResolved = new SelectBox("resolved");
        sbResolved.addOption("", app.getMessage("helpdesk.label.pleaseSelect"));
        sbResolved.addOption("false", app.getMessage("helpdesk.label.unresolved"));
        sbResolved.addOption("true", app.getMessage("helpdesk.label.resolved"));
        sbSeverity = new SelectBox("severity");
        sbIncidentType = new SelectBox("incidentType");
        dateCreatedFrom = new DatePopupField("dateCreatedFrom");
        dateCreatedFrom.setOptional(true);
        dateCreatedTo = new DatePopupField("dateCreatedTo");
        dateCreatedTo.setOptional(true);
        dateModifiedFrom = new DatePopupField("dateModifiedFrom");
        dateModifiedFrom.setOptional(true);
        dateModifiedTo = new DatePopupField("dateModifiedTo");
        dateModifiedTo.setOptional(true);

        // add and layout fields
        addChild(new Label("l1", app.getMessage("helpdesk.label.incidentNo") + " #"));
        addChild(tfIncidentCode);

        addChild(new Label("l2", app.getMessage("helpdesk.label.textSearch")));
        addChild(tfSearchText);

        addChild(new Label("l3", app.getMessage("helpdesk.label.queryFields")));
        Panel queryPanel = new Panel("queryPanel");
        queryPanel.addChild(cbSearchSubject);
        queryPanel.addChild(cbSearchDescription);
        queryPanel.addChild(cbSearchResolution);
        addChild(queryPanel);

        addChild(new Label("l4", app.getMessage("helpdesk.label.resolved")));
        addChild(sbResolved);

        addChild(new Label("l5", app.getMessage("helpdesk.label.severity")));
        addChild(sbSeverity);

        addChild(new Label("l6", app.getMessage("helpdesk.label.incidentType")));
        addChild(sbIncidentType);

        addChild(new Label("l7", app.getMessage("helpdesk.label.dateCreated")));
        Panel createdPanel = new Panel("createdPanel");
        createdPanel.setColspan(2);
        createdPanel.addChild(dateCreatedFrom);
        createdPanel.addChild(dateCreatedTo);
        addChild(createdPanel);
        addChild(dateCreatedFrom);
        addChild(dateCreatedTo);

        addChild(new Label("l8", app.getMessage("helpdesk.label.dateModified")));
        Panel modifiedPanel = new Panel("modifiedPanel");
        modifiedPanel.setColspan(2);
        modifiedPanel.addChild(dateModifiedFrom);
        modifiedPanel.addChild(dateModifiedTo);
        addChild(modifiedPanel);
        addChild(dateModifiedFrom);
        addChild(dateModifiedTo);
    }

    public void onRequest(Event evt) {
        Application app = Application.getInstance();
        HelpdeskHandler handler = (HelpdeskHandler)app.getModule(HelpdeskHandler.class);

        // load severity
        try
        {
            Map severityMap = new SequencedHashMap();
            severityMap.put("", app.getMessage("helpdesk.label.pleaseSelect"));
            severityMap.putAll(handler.getSeverityOptions());
            sbSeverity.setOptionMap(severityMap);
        }
        catch (HelpdeskException e) {
            // ignore, module already logs message
        }

        // load issue type
        try
        {
            Map incidentTypeMap = new SequencedHashMap();
            incidentTypeMap.putAll(handler.getIncidentTypeOptions());
            sbIncidentType.setOptionMap(incidentTypeMap);
        }
        catch (HelpdeskException e) {
            // ignore, module already logs message
        }

    }

    public String getIncidentCode() {
        String code = (String)tfIncidentCode.getValue();
        if (code != null && code.startsWith("#")) {
            code = code.substring(1);
        }
        return code;
    }

    public String getSearchText() {
        return (String)tfSearchText.getValue();
    }

    public boolean getSearchSubject() {
        return cbSearchSubject.isChecked();
    }

    public boolean getSearchDescription() {
        return cbSearchDescription.isChecked();
    }

    public boolean getSearchResolution() {
        return cbSearchResolution.isChecked();
    }

    public Boolean getResolved() {
        Boolean result = null;
        Collection selected = (Collection)sbResolved.getValue();
        if (selected != null && selected.size() > 0) {
            String val = selected.iterator().next().toString();
            if (val.trim().length() > 0) {
                result = Boolean.valueOf(val);
            }
        }
        return result;
    }

    public String getSeverity() {
        String result = null;
        Collection selected = (Collection)sbSeverity.getValue();
        if (selected != null && selected.size() > 0) {
            String val = selected.iterator().next().toString();
            if (val.trim().length() > 0) {
                result = val;
            }
        }
        return result;
    }

    public String getIncidentType() {
        String result = null;
        Collection selected = (Collection)sbIncidentType.getValue();
        if (selected != null && selected.size() > 0) {
            String val = selected.iterator().next().toString();
            if (val.trim().length() > 0 && !"-1".equals(val)) {
                result = val;
            }
        }
        return result;
    }

    public Date getDateCreatedFrom() {
        return cleanDate(dateCreatedFrom.getDate(), true);
    }

    public Date getDateCreatedTo() {
        return cleanDate(dateCreatedTo.getDate(), false);
    }

    public Date getDateModifiedFrom() {
        return cleanDate(dateModifiedFrom.getDate(), true);
    }

    public Date getDateModifiedTo() {
        return cleanDate(dateModifiedTo.getDate(), false);
    }

    public Date cleanDate(Date date, boolean start) {
        if (date == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (start) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        }
        else {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
        }
        return cal.getTime();
    }

}
