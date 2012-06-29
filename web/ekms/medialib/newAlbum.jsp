<%@page import="kacang.Application,
				kacang.services.security.SecurityService,
				com.tms.cms.medialib.model.AlbumModule"
%>
<%@include file="/common/header.jsp"%>

<%
	Application app = Application.getInstance();
	AlbumModule albumModule = (AlbumModule) Application.getInstance().getModule(AlbumModule.class);
	boolean isManager = albumModule.isManager();
	
	if (isManager) {
		request.setAttribute("isAccessible", "true");
	}
	else {
		request.setAttribute("isAccessible", "false");
	}
%>

<c:if test="${isAccessible eq 'false'}">
	<c:redirect url="index.jsp"/>
</c:if>

<c:if test="${!empty forward.name}">
	<c:if test="${forward.name != 'failure'}">
		<c:set var="redirectedForward" value="success" scope="session"/>
		<c:redirect url="editAlbum.jsp?id=${forward.name}&page=1"/>
	</c:if>
</c:if>

<x:config>
	<page name="newAlbum">
		<com.tms.cms.medialib.ui.AlbumFormAdd name="albumFormAdd" />
	</page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>

<script language="JavaScript">
	function back() {
		document.location.href = "<c:out value="${pageContext.request.contextPath}"/>/ekms/medialib/albumListing.jsp";
		return false;
	}
</script>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
   	<c:if test="${!empty forward.name}">
   		<c:if test="${forward.name eq 'success'}">
			<script language="JavaScript">
				alert("New album was successfully updated");
			</script>
   		</c:if>
   		<c:if test="${forward.name eq 'failure'}">
   			<script language="JavaScript">
				alert("Sorry, an unexpected runtime exception has occured. The requested new album was not created.");
			</script>
   		</c:if>
   	</c:if>
	
	<tr valign="middle">
		<td height="22" class="contentTitleFont">
			<fmt:message key="medialib.label.administration"/> > <fmt:message key="medialib.label.newAlbum"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<img src="<c:out value="${pageContext.request.contextPath}"/>/ekms/images/blank.gif"/>
		</td>
	</tr>
	<tr>
		<td class="contentBgColor">
			<x:display name="newAlbum.albumFormAdd" />
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>