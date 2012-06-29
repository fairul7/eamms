<%@page import="kacang.Application,
				com.tms.cms.medialib.model.MediaModule"
%>
<%@include file="/common/header.jsp"%>

<%
	if(request.getParameter("id") != null &&
		! "".equals(request.getParameter("id"))) {
		Application app = Application.getInstance();
		MediaModule module = (MediaModule) app.getModule(MediaModule.class);
		
		// Determine if user is accessible to this media
		boolean isAccessible = module.isAccessible(request.getParameter("id"));
		if(isAccessible) {
			request.setAttribute("isAccessible", "true");
		}
		else {
			request.setAttribute("isAccessible", "false");
		}
		
		if(isAccessible) {
			if (module.isContributor(request.getParameter("id"))) {
				request.setAttribute("isEditable", "true");
			}
			else {
				request.setAttribute("isEditable", "false");
			}
		}
	}
%>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="mediaListing.jsp"/>
</c:if>

<c:if test="${isEditable eq 'false'}">
	<c:redirect url="viewMedia.jsp?id=${param.id}"/>
</c:if>

<x:config>
	<page name="editMedia">
		<com.tms.cms.medialib.ui.MediaFormEdit name="mediaFormEdit" />
	</page>
</x:config>

<c:if test="${forward.name eq 'success'}">
	<c:set var="redirectedForward" value="success" scope="session"/>
	<c:redirect url="editMedia.jsp"/>
</c:if>
<c:if test="${forward.name eq 'failure'}">
	<c:set var="redirectedForward" value="failure" scope="session"/>
	<c:redirect url="editMedia.jsp"/>
</c:if>

<c:if test="${!empty param.id}">
	<x:set name="editMedia.mediaFormEdit" property="mediaId" value="${param.id}" />
	<x:set name="editMedia.mediaFormEdit" property="ip" value="${pageContext.request.remoteAddr}" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/mediaListing.jsp";
		return false;
	}
</script>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
	<c:if test="${!empty redirectedForward}">
   		<c:if test="${redirectedForward eq 'success'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.mediaUpdateSuccess'/>");
			</script>
   		</c:if>
   		<c:if test="${redirectedForward eq 'failure'}">
   			<c:set var="redirectedForward" value="" scope="session"/>
   			<script language="JavaScript">
				alert("<fmt:message key='medialib.message.updateFailure'/>");
			</script>
   		</c:if>
   	</c:if>
   	
	<tr>
		<td>
			<x:display name="editMedia.mediaFormEdit" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>