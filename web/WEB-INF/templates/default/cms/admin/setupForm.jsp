
<%@ page import="kacang.ui.Widget,
				 kacang.stdui.Form,
				 java.util.*,
				 java.awt.*,
                 kacang.stdui.Hidden"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="setupForm" value="${widget}"/>
<c:set var="form" value="${widget}"/>


<jsp:include page="../../form_header.jsp" flush="true" />

<%-- 
<jsp:include page="../../form.jsp" flush="true" />
--%>


<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
				

<c:forEach var="child" items="${form.textFields}" varStatus="stat">
<c:set var="i" value="${form.labels[stat.index].absoluteName}" />
<% if (((String)pageContext.getAttribute("i")).startsWith("system")) { %>
<tr>
	<td width="200px" class="classRowLabel" align="right">
		<x:display name="${form.labels[stat.index].absoluteName}" />
	</td>
	<td class="classRow">
		<x:display name="${form.textFields[stat.index].absoluteName}" />
	</td>
</tr> 
<%} %> 
</c:forEach>
<tr>
<td class="classRowLabel"></td>
<td class="classRow"><x:display name="${form.button.absoluteName}" /></td>
</tr>

<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>

<jsp:include page="../../form_footer.jsp" flush="true" />


