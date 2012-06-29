<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top" width="25%"><b><fmt:message key='sfa.message.contactPerson'/></b></td>
			<td valign="top" width="75%"><b><fmt:message key='sfa.message.contacttype'/></b></td>
		</tr>
		
		<c:set var="numContacts" value="${form.numContacts}"/>
		<%
			String numContacts = (String) pageContext.getAttribute("numContacts");
			int end = Integer.parseInt(numContacts) - 1;
			pageContext.setAttribute("end", String.valueOf(end));
		%>
		<c:forEach var="i" begin="0" end="${end}">
		<tr>
			<td valign="top"><x:display name="${form.absoluteName}.lbContactNames_${i}"/>&nbsp;</td>
			<td valign="top"><x:display name="${form.absoluteName}.sel_ContactTypes_${i}"/>&nbsp;</td>
		</tr>
		</c:forEach>
		
		<tr>
			<td align="center" colspan="2"><x:display name="${form.absoluteName}.submit"/></td>
		</tr>
	</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
