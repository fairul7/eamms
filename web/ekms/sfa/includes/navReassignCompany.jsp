<%@ page import="kacang.Application"%>
<%@ include file="/common/header.jsp" %>

<%
	int navSelected = 0;
	int navCutOff   = 4;
	String[] navArray = new String[] {
							Application.getInstance().getMessage("sfa.message.selectCompany","Select Company"),
							Application.getInstance().getMessage("sfa.message.selectContact","Select Contacts")
						};
	
	try {
		String navSel = (String) request.getAttribute("navSelected");
		navSelected = Integer.parseInt(navSel);
	} catch (Exception e) {
	}
%>

<font color="#999999" size="1" face="Verdana, Arial, Helvetica, sans-serif"><% for (int i=0; i<navArray.length; i++) { %>
		<% if (i == navCutOff) { %><br><% } %>
		<% if (i == navSelected) { %><font color="#FF0000"><strong><% } %>
		&gt; <%= navArray[i] %>
		<% if (i == navSelected) { %></strong></font><% } %>
	<% } %>
</font>
<br><br>
