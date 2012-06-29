<%@ page import="kacang.Application,
				 kacang.services.security.SecurityService"%>
                 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="userId" value="${widget.userId}"/>
 
	<% 
    	Application app = Application.getInstance();
		SecurityService service = (SecurityService) app.getService(SecurityService.class);
		String userId = (String) pageContext.getAttribute("userId");		
		String userName = "-";
		if(userId != null)
			userName = service.getUser(userId).getName();
	%>
	
	<jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>
	<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			
			<tr><td class="profileRow" valign="top" align="right" width="15%">
				<b><fmt:message key='fms.facility.label.assignmentId'/></b></td>
			<td class="profileRow" valign="top" width="40%">
				<c:out value="${form.assignmentId}"/> </td>
			</tr>
			<tr><td class="profileRow" valign="top" align="right" width="15%">
				<b><fmt:message key='fms.label.driverName'/></b></td>
			<td class="profileRow" valign="top" width="40%">
				<%=userName%></td>
			</tr>
			<tr><td class="profileRow" valign="top" align="right" width="15%">
				<b><fmt:message key='fms.label.vehiclesAssigned'/></b></td>
			<td class="profileRow" valign="top" width="40%">
				<x:display name="${form.groupVehicles.absoluteName}"/></td>
			</tr>
			<tr>
			  <td class="classRow" valign="top" width="40%">&nbsp;</td>
			  <td class="classRow" valign="top" width="40%">&nbsp;</td>
			</tr>
			<tr>
			  <td class="classRow" valign="top" width="40%">&nbsp;</td>
			  <td class="classRow" valign="top" width="40%">
			     <x:display name="${form.submitButton.absoluteName}" />
			     <x:display name="${form.cancelButton.absoluteName}" />
			  </td>
			</tr>
			
	</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
