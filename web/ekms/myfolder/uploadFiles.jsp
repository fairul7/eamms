<%@include file="/common/header.jsp"%>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="uploadPg">
		<com.tms.collab.myfolder.ui.UploadFilesForm name="uploadFileForm" />
		<com.tms.collab.myfolder.ui.FolderPathWidget name="folderPath"/>
	</page>
</x:config>

<%-- <x:set name="uploadPg.uploadFileForm" property="parentId" value="${selectedFolder}"/> 
<x:set name="uploadPg.folderPath" property="mfId" value="${selectedFolder}"/> --%>

<c:if test="${forward.name == 'done'}">
<%--	<c:redirect url="myFolder.jsp"/>  --%>
<script>
	window.location = "myFolder.jsp";
</script>
</c:if>

<c:set var="bodyTitle" scope="request"><fmt:message key="mf.label.myFolder"/> > <fmt:message key="mf.label.uploadFiles"/> </c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<strong>
<fmt:message key="mf.message.uploadFiles1"/>
<c:if test="${widgets['uploadPg.uploadFileForm'].fileLimit != null}">
	<c:out value="${widgets['uploadPg.uploadFileForm'].fileLimit}"/>
</c:if>
<fmt:message key="mf.message.uploadFiles2"/>
</strong>
<br/>
<ul>
<c:if test="${message1 != null}">
<li>	<c:out value="${message1}"/>	</li>
</c:if>
<c:if test="${message2 != null}">
<li>	<c:out value="${message2}"/>	</li>
</c:if>
<c:if test="${message3 != null}">
<li>	<c:out value="${message3}"/>	</li>
</c:if>
<c:if test="${message != null}">
<li>	<c:out value="${message}"/>	</li>
</c:if>
</ul>

<br/>

<x:display name="uploadPg.uploadFileForm"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>