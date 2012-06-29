<%@page import="kacang.Application,
				kacang.services.security.SecurityService,
				com.tms.cms.medialib.model.LibraryModule"
%>
<%@include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
	String userId = securityService.getCurrentUser(request).getId();
	
	// Determine permisson
	boolean createLibrary = securityService.hasPermission(userId, LibraryModule.PERMISSION_CREATE_LIBRARY, null, null);
	if (createLibrary) {
		request.setAttribute("canCreateLibrary", "true");
	}
	else {
		request.setAttribute("canCreateLibrary", "false");
	}
%>

<c:if test="${canCreateLibrary eq 'false'}">
	<c:redirect url="index.jsp"/>
</c:if>

<c:if test="${!empty forward.name}">
	<c:if test="${forward.name != 'failure'}">
		<c:set var="redirectedForward" value="success" scope="session"/>
		<c:redirect url="editLibrary.jsp?id=${forward.name}"/>
	</c:if>
</c:if>

<x:config>
	<page name="newLibrary">
		<com.tms.cms.medialib.ui.LibraryFormAdd name="libraryFormAdd" />
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/libraryListing.jsp";
		return false;
	}
</script>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
   	<c:if test="${!empty forward.name}">
   		<c:if test="${forward.name eq 'success'}">
			<script language="JavaScript">
				alert("New library was successfully updated");
			</script>
   		</c:if>
   		<c:if test="${forward.name eq 'failure'}">
   			<script language="JavaScript">
				alert("Sorry, an unexpected runtime exception has occured. The requested new library was not created.");
			</script>
   		</c:if>
   	</c:if>
	
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
            <fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.newLibrary"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="newLibrary.libraryFormAdd" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>