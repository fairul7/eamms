<%@include file="/common/header.jsp" %>

<x:config>
    <page name="mFolderTree">
        <com.tms.collab.messaging.ui.FolderTree name="tree" />
    </page>
</x:config>

<x:display name="mFolderTree.tree" body="custom">
    <c:set var="tree" scope="request" value="${widget}"/>
    <c:set var="root" scope="request" value="${tree.model}"/>

    <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">

    <table width="5" align="left"><tr><td></td></tr></table>

    <script language="javascript" src="<%= request.getContextPath() %>/common/tree/tree.js">
    </script>

    <jsp:include page="folderTreeRecursion.jsp" flush="true"/>

    <div> &nbsp;&nbsp;&nbsp; [<a href="newFolder.jsp" class="menuLink"><fmt:message key='messaging.label.newFolder'/></a>] </div>

    <p>
    </c:if>
</x:display>
