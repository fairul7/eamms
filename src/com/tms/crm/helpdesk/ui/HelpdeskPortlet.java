package com.tms.crm.helpdesk.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorGreaterThan;
import kacang.model.operator.OperatorLessThan;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.util.Log;

import com.tms.crm.helpdesk.HelpdeskException;
import com.tms.crm.helpdesk.HelpdeskHandler;

public class HelpdeskPortlet extends Widget{
	
	private Collection recentIncident;
	private Collection oldestUnresolvedIncident;
	private Collection latestUnresolvedIncident;
	private Collection oldestUnresolvedIncidentByMe;
	private int numOfIncidentToday;
	
	public static final String DEFAULT_TEMPLATE = "helpdesk/helpdeskPortlet";
	
	public HelpdeskPortlet(){		
	}
	
	public HelpdeskPortlet(String name){
		super(name);
	}
	
	public void init(){
		Application app = Application.getInstance();
		HelpdeskHandler handler = (HelpdeskHandler)app.getModule(HelpdeskHandler.class);
		
		Properties properties = app.getProperties();
		
		int mri = Integer.parseInt(properties.getProperty("helpdesk.portlet.mri")); //most recent incidnet
		int oui = Integer.parseInt(properties.getProperty("helpdesk.portlet.oui")); //oldest unsolved incident
		int lui = Integer.parseInt(properties.getProperty("helpdesk.portlet.lui")); //latest unresolved incident
		int ouirbm = Integer.parseInt(properties.getProperty("helpdesk.portlet.ouirbm")); //oldest unresolved incident reported by me
		
		try{
		
			recentIncident = handler.getIncidents(new DaoQuery(), 0, mri, "created", true);
			oldestUnresolvedIncident = handler.getIncidents(generateUnresolvedIncidentQuery(), 0, oui, "created", false);
			latestUnresolvedIncident = handler.getIncidents(generateUnresolvedIncidentQuery(), 0, lui, "created", true);
			oldestUnresolvedIncidentByMe = handler.getIncidents(generateUnresolvedIncidentReportedByMeQuery(), 0, ouirbm, "created", false);
			numOfIncidentToday = handler.getIncidentsCount(generateIncidentReportedToday());
			
			
		}
		catch (HelpdeskException e){
			Log.getLog(getClass()).error("Error while retrieving incidents", e);
		}
		
		
	}
	
	public void onRequest(Event evt){
		init();
	}
	
	public Forward actionPerformed(Event evt){
		
		return new Forward("something");
	}
	
	public DaoQuery generateEmptyQuery(){
		DaoQuery query = new DaoQuery();
		return query;
	}
	
	public DaoQuery generateUnresolvedIncidentQuery(){
		DaoQuery query = new DaoQuery();
		query.addProperty(new OperatorEquals("resolved", "0", DaoOperator.OPERATOR_AND));
		return query;
	}
	
	public DaoQuery generateUnresolvedIncidentReportedByMeQuery(){
		DaoQuery query = new DaoQuery();
		query.addProperty(new OperatorEquals("resolved", "0", DaoOperator.OPERATOR_AND));
		query.addProperty(new OperatorLike("createdBy", Application.getInstance().getCurrentUser().getId().substring(0, 50), DaoOperator.OPERATOR_AND));
		return query;
	}
	
	public DaoQuery generateIncidentReportedToday(){
		DaoQuery query = new DaoQuery();
		
		StringBuffer dateParam = new StringBuffer();
		Date nowStart = new Date();
		Date nowEnd = new Date();
		
		nowStart.setHours(0);
		nowStart.setMinutes(0);
		
		nowEnd.setHours(23);
		nowEnd.setMinutes(59);
		
		query.addProperty(new OperatorGreaterThan("created", nowStart, DaoOperator.OPERATOR_AND));
		query.addProperty(new OperatorLessThan("created", nowEnd, DaoOperator.OPERATOR_AND));
		return query;
	}
	
	public String getDoubleDigitString(int num){
		StringBuffer param = new StringBuffer("0");
		if(num < 10){
			param.append(num);
			return param.toString();
		}else{
			return String.valueOf(num);
		}
	}
	
	public String getDefaultTemplate(){
        return DEFAULT_TEMPLATE;
    }
	
	public Collection getLatestUnresolvedIncident() {
		return latestUnresolvedIncident;
	}
	public void setLatestUnresolvedIncident(Collection latestUnresolvedIncident) {
		this.latestUnresolvedIncident = latestUnresolvedIncident;
	}
	public int getNumOfIncidentToday() {
		return numOfIncidentToday;
	}
	public void setNumOfIncidentToday(int numOfIncidentToday) {
		this.numOfIncidentToday = numOfIncidentToday;
	}
	public Collection getOldestUnresolvedIncident() {
		return oldestUnresolvedIncident;
	}
	public void setOldestUnresolvedIncident(Collection oldestUnresolvedIncident) {
		this.oldestUnresolvedIncident = oldestUnresolvedIncident;
	}
	public Collection getOldestUnresolvedIncidentByMe() {
		return oldestUnresolvedIncidentByMe;
	}
	public void setOldestUnresolvedIncidentByMe(
			Collection oldestUnresolvedIncidentByMe) {
		this.oldestUnresolvedIncidentByMe = oldestUnresolvedIncidentByMe;
	}
	public Collection getRecentIncident() {
		return recentIncident;
	}
	public void setRecentIncident(Collection recentIncident) {
		this.recentIncident = recentIncident;
	}
	
	
	

}
