<%@include file="includes/taglib.jsp" %>
<%@ page import="com.tms.collab.messaging.ui.FolderTable"%>
<x:config>
    <page name="folderListPage">
        <com.tms.collab.messaging.ui.FolderTable name="table"/>
        <com.tms.collab.messaging.ui.FolderTable name="special" specialFolders="true" paging="false" />
    </page>
</x:config>
<c:if test="${param.cn == 'folderListPage.table' && !empty param.folderId}">
    <c:redirect url="editFolder.jsp?folderId=${param.folderId}" />
</c:if>
<c-rt:set var="forwardNewFolder" value="<%= FolderTable.FORWARD_NEW_FOLDER %>"/>
<c:if test="${forward.name eq forwardNewFolder}">
	<c:redirect url="newFolder.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr valign="middle">
		<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='messaging.label.options'/> > <fmt:message key='messaging.label.managerFolder'/></font></b></td>
		<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
	<tr>
		<td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			<x:display name="folderListPage.table" />

            <x:display name="folderListPage.special" />
		</td>
	</tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<%@include file="includes/footer.jsp" %>
