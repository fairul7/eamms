<%@ page import="java.util.*,
				 com.tms.crm.sales.misc.DateUtil,
				 com.tms.collab.timesheet.model.TimeSheetModule,
				 kacang.Application,
				 kacang.ui.*,
				 java.text.SimpleDateFormat,
				 com.tms.collab.timesheet.model.TimeSheet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:config>
	<page name="myTimesheet">
		<com.tms.collab.timesheet.ui.TimeSheetPortlet name="portlet"/>
	</page>
</x:config>

<c:set var="selectedDate" value="${widgets['myTimesheet.portlet'].selectedDate}" /> 
<%
	Date selectedDate = DateUtil.getDateOnly(new Date());
	selectedDate = (Date) pageContext.getAttribute("selectedDate");
	Date today = DateUtil.getDateOnly(new Date());
	Date sundayDate = getFirstDay(selectedDate);
	Date saturdayDate = DateUtil.dateAdd(sundayDate, Calendar.DATE, 6);
	
	WidgetManager wm = WidgetManager.getWidgetManager(request);
	String userId = wm.getUser().getId();
	HashMap[] hmArray = getArray(sundayDate, saturdayDate, userId);
%>

<% 
	String dateRow = "";
	String duraRow = "";
	SimpleDateFormat sdf = new SimpleDateFormat("EEE dd");
	boolean currWeek = false;
	for (int i = 0; i < hmArray.length; i++) {
		HashMap hm = hmArray[i];
		Date date = (Date) hm.get("date");
		String dateStr = sdf.format(date);
		String duraStr = hm.get("duration").toString();
		if (date.equals(today)) {
			dateStr = "<font color=\"red\">" + dateStr + "</font>";
			currWeek = true;
		}
		dateRow = dateRow + "<td>" + dateStr + "</td>";
		duraRow = duraRow + "<td>" + duraLink(duraStr, date) + "</td>";
	}
	
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
	String dateRange = sdf2.format(sundayDate) + " - " + sdf2.format(saturdayDate);
%>
<table border="0" cellpadding="2" cellspacing="1">
<tr align="center">
	<td><a href="?cn=myTimesheet.portlet&button*myTimesheet.portlet.prevButton=">&lt;&lt;</a></td>
	<td colspan="5"><b><%= dateRange %></b></td>
	<td><% if (!currWeek) { %><a href="?cn=myTimesheet.portlet&button*myTimesheet.portlet.nextButton=">&gt;&gt;</a><% } else { %>&nbsp;<% } %></td>
</tr>
<tr align="center" class="tableHeader"><%= dateRow %></tr>
<tr align="center"><%= duraRow %></tr>
<tr align="center"><td colspan="7">
	<form>
		<input class="button" type="button" value="Add Time Sheet" onclick="location='timesheet/TimeSheetPForm.jsp'">
		<% if (!currWeek) { %>
		<input class="button" type="button" value="View Current Week" onclick="location='?cn=myTimesheet.portlet&button*myTimesheet.portlet.todayButton='">
		<% } %>
	</form>
</td></tr>
</table>

<%!
	public Date getFirstDay(Date date) {
		int dow = DateUtil.getDatePart(date, Calendar.DAY_OF_WEEK);
		Date sundayDate;
		if (dow == Calendar.SUNDAY) {
			sundayDate = date;
		} else {
			sundayDate = DateUtil.dateAdd(date, Calendar.DATE, 1 - dow);
		}
		return DateUtil.getDateOnly(sundayDate);
	}
	
	public String duraLink(String text, Date date) {
		return "<a href=\"timesheet/TimeSheetTableView.jsp?date="+ DateUtil.getDateString(date) +"\">" + text + "</a>";
	}
	
	public HashMap[] getArray(Date start, Date end, String userId) {
		TimeSheetModule module = (TimeSheetModule) Application.getInstance().getModule(TimeSheetModule.class);
		Collection col = module.getListByTimesheet(start, end, userId, null, false, 0, -1);
		
		HashMap[] oArray = new HashMap[7];
		Date date = start;
		
		for (int i = 0; i < oArray.length; i++) {
			HashMap hm = new HashMap();
			hm.put("date", date);
			hm.put("duration", new Double(0.0));
			oArray[i] = hm;
			
			date = DateUtil.dateAdd(date, Calendar.DATE, 1);
		}
		
		if (col != null) {
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				TimeSheet ts = (TimeSheet) iterator.next();
				int index = DateUtil.getDatePart(ts.getTsDate(), Calendar.DAY_OF_WEEK) - 1;
				double duration = ((Double) oArray[index].get("duration")).doubleValue();
				duration = duration + ts.getDuration();
				oArray[index].put("duration", new Double(duration));
			}
		}
		return oArray;
	}
%>