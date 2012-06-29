<%@ page import="java.util.Calendar,
				 java.util.Iterator,
				 com.tms.ekms.statistics.ui.ScoringSummary,
				 kacang.Application"%>
<%@ include file="/common/header.jsp" %>
<%
	Application app = Application.getInstance();
	Calendar calendar = Calendar.getInstance();
	int year = calendar.get(Calendar.YEAR);
    String selectedMonth;
	String selectedYear;

	selectedMonth = request.getParameter("scoringSummaryMonth");
	if(selectedMonth == null || "".equals(selectedMonth))
		selectedMonth = Integer.toString(calendar.get(Calendar.MONTH));
	selectedYear = request.getParameter("scoringSummaryYear");
	if(selectedYear == null || "".equals(selectedYear))
		selectedYear = Integer.toString(calendar.get(Calendar.YEAR));
%>
<c:set var="widget" value="${requestScope.statistics_summary}"/>
<table cellpadding="1" cellspacing="1" width="80%" class="classRow" align="center">
	<form method="post" action="?">
		<tr>
			<td>
				<select name="<%= ScoringSummary.FIELD_MONTH %>">
					<option value="0" <%= selectedMonth.equals("0") ? "SELECTED":""%>><%= app.getMessage("general.label.jan") %></option>
					<option value="1" <%= selectedMonth.equals("1") ? "SELECTED":""%>><%= app.getMessage("general.label.feb") %></option>
					<option value="2" <%= selectedMonth.equals("2") ? "SELECTED":""%>><%= app.getMessage("general.label.mar") %></option>
					<option value="3" <%= selectedMonth.equals("3") ? "SELECTED":""%>><%= app.getMessage("general.label.apr") %></option>
					<option value="4" <%= selectedMonth.equals("4") ? "SELECTED":""%>><%= app.getMessage("general.label.may") %></option>
					<option value="5" <%= selectedMonth.equals("5") ? "SELECTED":""%>><%= app.getMessage("general.label.jun") %></option>
					<option value="6" <%= selectedMonth.equals("6") ? "SELECTED":""%>><%= app.getMessage("general.label.jul") %></option>
					<option value="7" <%= selectedMonth.equals("7") ? "SELECTED":""%>><%= app.getMessage("general.label.aug") %></option>
					<option value="8" <%= selectedMonth.equals("8") ? "SELECTED":""%>><%= app.getMessage("general.label.sep") %></option>
					<option value="9" <%= selectedMonth.equals("9") ? "SELECTED":""%>><%= app.getMessage("general.label.oct") %></option>
					<option value="10" <%= selectedMonth.equals("10") ? "SELECTED":""%>><%= app.getMessage("general.label.nov") %></option>
					<option value="11" <%= selectedMonth.equals("11") ? "SELECTED":""%>><%= app.getMessage("general.label.dec") %></option>
				</select>
				<select name="<%= ScoringSummary.FIELD_YEAR %>">
					<%
						for(int i=(year - 5); i<=year; i++)
						{
							%><option value="<%= i %>" <%= selectedYear.equals(Integer.toString(i)) ? "SELECTED":"" %>><%= i %></option><%
						}
					%>
				</select>
				<input type="submit" value="View" name="action" class="button">
			</td>
		</tr>
	</form>
</table>
<br>
<table cellpadding="1" cellspacing="1" width="90%" class="classRow" align="center">
	<tr><td colspan="3"  class="classRowLabel">Most Active Users:</td></tr>
	<tr>
		<td width="5" class="classRowLabel">&nbsp;</td>
		<td class="classRowLabel">User</td>
		<td class="classRowLabel" nowrap align="right">Score</td>
	</tr>
	<c:forEach items="${widget.users}" var="user" varStatus="status">
		<tr>
			<td width="1" nowrap align="right" class="classRow"><c:out value="${status.count}"/>.</td>
			<td class="classRow"><c:out value="${user.key.name}"/></td>
			<td class="classRow" nowrap align="right"><c:out value="${user.value}"/></td>
		</tr>
	</c:forEach>
    <tr><td colspan="3"  class="classRow">&nbsp;</td></tr>
	<tr><td colspan="3"  class="classRowLabel">Most Popular Feature:</td></tr>
	<tr>
		<td width="3" class="classRowLabel">&nbsp;</td>
		<td class="classRowLabel">Feature</td>
		<td class="classRowLabel" nowrap align="right">Uses</td>
	</tr>
	<c:forEach items="${widget.features}" var="feature" varStatus="status">
		<tr>
			<td width="1" nowrap align="right" class="classRow"><c:out value="${status.count}"/>.</td>
			<td class="classRow"><c:out value="${feature.key}"/></td>
			<td class="classRow" nowrap align="right"><c:out value="${feature.value}"/></td>
		</tr>
	</c:forEach>
	<tr><td colspan="3"  class="classRow">&nbsp;</td></tr>
</table>
