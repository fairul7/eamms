<%@page import="kacang.Application,
				kacang.services.security.SecurityService,
				com.tms.cms.medialib.model.LibraryModule"
%>
<%@include file="/common/header.jsp"%>

<x:permission module="com.tms.cms.medialib.model.LibraryModule" permission="<%= LibraryModule.PERMISSION_CREATE_LIBRARY %>" url="home.jsp" />

<%
	if(request.getParameter("id") != null &&
		!"".equals(request.getParameter("id"))) {
		Application app = Application.getInstance();
		LibraryModule module = (LibraryModule) app.getModule(LibraryModule.class);
		
		// Determine if user is accessible to this library
		boolean isAccessible = module.isAccessible(request.getParameter("id"));
		if(isAccessible) {
			request.setAttribute("isAccessible", "true");
		}
		else {
			request.setAttribute("isAccessible", "false");
		}
		
		if(isAccessible) {
			// Application level security
			SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
			String userId = securityService.getCurrentUser(request).getId();
			
			// Determine permisson at application level
			boolean createLibrary = securityService.hasPermission(userId, LibraryModule.PERMISSION_CREATE_LIBRARY, null, null);
			if (createLibrary) {
				request.setAttribute("isEditable", "true");
			}
			else {
				request.setAttribute("isEditable", "false");
			}
			
			// If user is not granted permission at application level, 
			// then try to check for library level
			if(! createLibrary) {
				// Check if user is the manager of requested library
				if(module.isManager(request.getParameter("id"))) {
					request.setAttribute("isEditable", "true");
				}
				else {
					request.setAttribute("isEditable", "false");
				}
			}
		}
	}
%>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="libraryListing.jsp"/>
</c:if>

<c:if test="${isEditable eq 'false'}">
	<c:redirect url="viewLibrary.jsp?id=${param.id}"/>
</c:if>

<x:config>
	<page name="editLibrary">
		<com.tms.cms.medialib.ui.LibraryFormEdit name="libraryFormEdit" />
	</page>
</x:config>

<c:if test="${forward.name eq 'success'}">
	<c:set var="redirectedForward" value="success" scope="session"/>
	<c:redirect url="editLibrary.jsp"/>
</c:if>
<c:if test="${forward.name eq 'failure'}">
	<c:set var="redirectedForward" value="failure" scope="session"/>
	<c:redirect url="editLibrary.jsp"/>
</c:if>

<c:if test="${!empty param.id}">
	<x:set name="editLibrary.libraryFormEdit" property="libraryId" value="${param.id}" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/libraryListing.jsp";
		return false;
	}
</script>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<c:if test="${!empty redirectedForward}">
   		<c:if test="${redirectedForward eq 'success'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.libraryUpdateSuccess'/>");
			</script>
   		</c:if>
   		<c:if test="${redirectedForward eq 'failure'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
   			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.updateFailure'/>");
			</script>
   		</c:if>
   	</c:if>
   	
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.editLibrary"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="editLibrary.libraryFormEdit" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>