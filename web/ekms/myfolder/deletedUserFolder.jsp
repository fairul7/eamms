<%@include file="/common/header.jsp"%>

<x:permission var="isAuthorized" module="com.tms.collab.myfolder.model.MyFolderModule" permission="com.tms.collab.myfolder.deletedUser"/>
<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
    <page name="mainPg">
        <com.tms.collab.myfolder.ui.DeletedUserFolderTree name="folderTree"/>
        <com.tms.collab.myfolder.ui.DeletedUserFileFolderListTable name="table"/>
    </page>
</x:config>

<c:if test="${forward.name == 'folderNavi'}">
    <x:set name="mainPg.table" property="mfId" value="${param.id}"/>
</c:if>

<c:if test="${forward.name == 'move'}">
    <c:if test="${folderId != null}">
        <x:set name="mainPg.folderTree" property="selectedId" value="${folderId}"/>
    </c:if>
</c:if>

<c:if test="${forward.name == 'editFolder'}">
    <c:if test="${folderId != null}">
        <script>
            window.location = "editDel.jsp?id=<c:out value='${folderId}'/>";
        </script>
    </c:if>
</c:if>

<c:if test="${forward.name == 'invalid'}">
    <script>
        alert("<fmt:message key='mf.message.cannotEdit'/>");
    </script>
</c:if>

<c:if test="${!empty param.mfId}">
    <script>
        window.location = "viewDel.jsp?id=<c:out value='${param.mfId}'/>&from=del";
    </script>
</c:if>

<c:set var="bodyTitle" scope="request"><fmt:message key="mf.label.myFolder"/> > <fmt:message key="mf.label.deletedUserFolder"/>  </c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<p>&nbsp;</p>
<table width="100%">
    <tr>
        <td width="20%" valign="top">
            <table cellpadding="1" cellspacing="1" border="0" style="text-align: left; width: 100%; border:1px solid gray">
                <tr><td></td></tr>
                <tr>
                    <td><x:display name="mainPg.folderTree"/></td>
                </tr>
            </table>
        </td>
        <td width="80%"  style="vertical-align:top">
            <x:display name="mainPg.table"/>
        </td>
    </tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>