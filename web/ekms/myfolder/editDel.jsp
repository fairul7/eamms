<%@include file="/common/header.jsp"%>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="editPg">
		<com.tms.collab.myfolder.ui.FileFolderEditForm name="edit"/>
	</page>
</x:config>

<c:if test="${forward.name == 'save'}">
	<script>
		window.alert("<fmt:message key='mf.message.saved'/>");
		window.location = "deletedUserFolder.jsp";
	</script>
</c:if>

<c:if test="${!empty param.id}">
    <x:set name="editPg.edit" property="mfId" value="${param.id}"/>
</c:if>

<c:if test="${forward.name == 'cancel'}">
	<script>
//		window.location = "viewDel.jsp?id=<c:out value='${param.id}' />";
		history.go(-2);
	</script>
</c:if>

<c:set var="bodyTitle" scope="request"><fmt:message key="mf.label.edit"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="editPg.edit"/>
<br/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>