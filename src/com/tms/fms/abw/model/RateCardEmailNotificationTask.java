package com.tms.fms.abw.model;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.transport.model.FmsNotification;

public class RateCardEmailNotificationTask extends BaseJob
{
	public static final String NAME = "RateCardEmailNotificationTask";
	public static final String GROUP = "EmailNotification";
	public static final String DESC = "Send email to ABW admin(s)";
	
	public void execute(JobTaskExecutionContext context) throws SchedulingException
	{
		Log.getLog(getClass()).info("============== RateCardEmailNotificationTask Start ===============");
		
		String emailContent = constructMailContent();
		if(emailContent != null)
		{
			EngineeringModule em = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			FmsNotification notification = new FmsNotification();
			
			String emailTo[] = em.getAbWEmails();
			String emailSubject = Application.getInstance().getMessage("fms.administration.email.emailSubject");
			notification.send(emailTo, emailSubject, emailContent);
		}
		
		Log.getLog(getClass()).info("============== RateCardEmailNotificationTask End ===============");
	}

	private String constructMailContent()
	{
		AbwModule am = (AbwModule)Application.getInstance().getModule(AbwModule.class);
		Application app = Application.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Date currentDate = new Date();
		Collection col1 = am.getNewRateCard(currentDate);
		Collection col2 = am.getUpdateRateCard(currentDate);
		
		SetupModule sm = (SetupModule)Application.getInstance().getModule(SetupModule.class);
		Collection col3 = sm.getRateCardEmailNotification(currentDate);
		if(col2 != null && col3 != null && !col3.isEmpty())
		{
			col2.addAll(col3);
			sm.clearRateCardEmailNotification(currentDate);
		}
		
		if((col1 == null || col1.isEmpty()) && (col2 == null || col2.isEmpty())) 
		{
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(app.getMessage("fms.administration.email.msg1") + ",");
		sb.append("<br>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		
		if(col1 != null && !col1.isEmpty())
		{
			sb.append(app.getMessage("fms.administration.email.msg2") + "&nbsp;" + sdf.format(currentDate));
			sb.append("<br><br>");
			sb.append("<table border=\"0\" width=\"80%\" cellspacing=\"0\" " +
					"cellpadding=\"3px\" style=\"border: solid 1px #99bb5c; font: 9pt Verdana;\" >");
			sb.append("<tr style=\"background-color: #99bb5c; color: #ffffff; font: bold 9pt Verdana;\"> " +
					"<td>" + app.getMessage("fms.administration.email.facilitiesCode") + "</td> " +
					"<td>" + app.getMessage("fms.administration.email.desc") + "</td> " +
					"<td>" + app.getMessage("fms.administration.email.rate") + "</td> " +
					"<td>" + app.getMessage("fms.administration.email.status") + "</td> </tr>");
			
			for(Iterator itr1 = col1.iterator(); itr1.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr1.next();
				String facilitiesCode = obj.getProperty("code") != null ? (String) obj.getProperty("code") : "";
				String desc = (String) obj.getProperty("name");
				String rate = obj.getProperty("internalRate") != null ? String.valueOf(obj.getProperty("internalRate")) : "";
				
				sb.append("<tr style=\"border-bottom:1px solid #99bb5c;\">" +
						"<td><b>" + facilitiesCode + "</b></td> " +
						"<td>" + desc + "</td> " +
						"<td>" + rate + "</td> " +
						"<td>" + "Active" + "</td>");
				sb.append("</tr>");
			}
			
			sb.append("</table>");
		}
		
		if(col2 != null && !col2.isEmpty())
		{
			sb.append("<br><br>");
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(app.getMessage("fms.administration.email.msg3") + "&nbsp;" + sdf.format(currentDate));
			sb.append("<br><br>");
			sb.append("<table border=\"0\" width=\"80%\" cellspacing=\"0\" " +
					"cellpadding=\"3px\" style=\"border: solid 1px #99bb5c; font: 9pt Verdana;\" >");
			sb.append("<tr style=\"background-color: #99bb5c; color: #ffffff; font: bold 9pt Verdana;\"> " +
					"<td>" + app.getMessage("fms.administration.email.facilitiesCode") + "</td> " +
					"<td>" + app.getMessage("fms.administration.email.desc") + "</td> " +
					"<td>" + app.getMessage("fms.administration.email.rate") + "</td> " +
					"<td>" + app.getMessage("fms.administration.email.status") + "</td> </tr>");
			
			for(Iterator itr2 = col2.iterator(); itr2.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr2.next();
				String facilitiesCode = obj.getProperty("code") != null ? (String) obj.getProperty("code") : "";
				String desc = (String) obj.getProperty("name");
				String rate = obj.getProperty("internalRate") != null ? String.valueOf(obj.getProperty("internalRate")) : "";
				
				sb.append("<tr style=\"border-bottom:1px solid #99bb5c;\">" +
						"<td><b>" + facilitiesCode + "</b></td> " +
						"<td>" + desc + "</td> " +
						"<td>" + rate + "</td> ");
				
				if(obj.getProperty("inactive") != null && obj.getProperty("inactive").equals("inactive"))
				{
					sb.append("<td>" + "Inactive" + "</td>");
				}
				else
				{
					sb.append("<td>" + "Active" + "</td>");
				}
				
				sb.append("</tr>");
			}
			
			sb.append("</table>");
		}
		
		sb.append("<br>");
		sb.append(app.getMessage("fms.administration.email.msg4"));
		
		return sb.toString();
	}

}
