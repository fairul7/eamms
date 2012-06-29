<%@include file="/common/header.jsp"%>

<c:if test="${forward.name == 'download'}">
	<c:if test="${path != null}">
			<script>
				window.open('<c:out value='${path}'/>', 'download', 'toolbar=yes, directories=no, location=no, status=yes, menubar=no,  resizable=yes, scrollbars=yes, width=500, height=400');
				//window.location = "<c:out value='${path}'/>";
			</script>
	</c:if>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewPg">
		<com.tms.collab.myfolder.ui.FileFolderViewWidget name="view"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
    <x:set name="viewPg.view" property="mfId" value="${param.id}"/>
</c:if>

<c:set var="bodyTitle" scope="request"><fmt:message key="mf.label.detail"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<script>
function goBack(){
	window.location = "deletedUserFolder.jsp";
}

function edit(){
	window.location ="editDel.jsp?id=<c:out value='${param.id}'/>";
}
</script>

<x:display name="viewPg.view"/><br/>

<br/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>