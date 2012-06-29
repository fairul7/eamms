<c:if test="${param.cn=='abFolderTree.tree'}">
    <x:set name="abContactList.table" property="selectedFolder" value="${param.id}" />
    <c:redirect url="abContactList.jsp" />
</c:if>
<c:if test="${param.cn=='bdFolderTree.tree'}">
    <x:set name="bdContactList.table" property="selectedFolder" value="${param.id}" />
    <x:set name="bdContactList.table" property="selectedCompany" value="${param.companyId}" />
    <c:redirect url="bdContactList.jsp" />
</c:if>
