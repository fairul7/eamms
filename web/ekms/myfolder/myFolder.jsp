<%@include file="/common/header.jsp"%>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="mainPg">
		<com.tms.collab.myfolder.ui.FolderTree name="folderTree"/>
		<com.tms.collab.myfolder.ui.QuotaStatusWidget name="quotaStatus"/>
		<com.tms.collab.myfolder.ui.FileFolderListTable name="fileFolderList"/>
	</page>
</x:config>

<c:if test="${forward.name == 'upload'}">
<%--	<c:if test="${folderId != null}"> --%>
<%--			<c:set var="selectedFolder" value="${folderId}" scope="session"/> --%>
<%--	</c:if> --%>
    <script>document.location="/ekms/myfolder/uploadFiles.jsp";</script>
</c:if>

<c:if test="${forward.name == 'folderNavi'}">
	<x:set name="mainPg.fileFolderList" property="mfId" value="${param.id}"/>
	<c:set var="selectedFolder" value="${param.id}" scope="session"/>
</c:if>

<c:if test="${forward.name == 'move'}">
	<c:if test="${folderId != null}">
			<x:set name="mainPg.folderTree" property="selectedId" value="${folderId}"/>
	</c:if>
</c:if>

<c:if test="${forward.name == 'editFolder'}">
	<c:if test="${folderId != null}">
		<script>
			window.location = "edit.jsp?id=<c:out value='${folderId}'/>";
		</script>
	</c:if>
</c:if>

<c:if test="${forward.name == 'invalid'}">
	<script>
			alert("<fmt:message key='mf.message.cannotEdit'/>");
	</script>
</c:if>


<c:if test="${!empty param.mfId}">
	window.location = "view.jsp?id=<c:out value='${param.mfId}'/>";
</c:if>

<c:set var="fileName" value="${widgets['mainPg.fileFolderList'].fileName}"/>

<c:set var="bodyTitle" scope="request"><fmt:message key="mf.label.myFolder"/><c:out value="${fileName}" escapeXml="false"/><c:out value="${childFileName}" escapeXml="false"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>
<p>&nbsp;</p>

<table width="100%">
	<tr>
		<td width="20%" valign="top">
			<table cellpadding="1" cellspacing="1" border="0" style="text-align: left; width: 100%; border:1px solid gray">
				<tr style="text-align:center; background: #B9D3EE">
					<td> <strong><fmt:message key='mf.label.quotaStatus'/></strong></td>
				</tr>
				
				<tr>
				<td>
			   <x:display name="mainPg.quotaStatus"/>
			    </td>
			    </tr>
			</table>
			<br/>
			
			<table cellpadding="1" cellspacing="1" border="0" style="text-align: left; width: 100%; border:1px solid gray">
				<tr><td></td></tr>
				<tr>
					<td><x:display name="mainPg.folderTree"/></td>
				</tr>
			</table>
			
		</td>
		<td width="80%" style="vertical-align:top">
			<x:display name="mainPg.fileFolderList"/>
		</td>
	</tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>