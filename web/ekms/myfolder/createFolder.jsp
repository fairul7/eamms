<%@include file="/common/header.jsp"%>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
    <page name="createFolderPg">
        <com.tms.collab.myfolder.ui.FolderForm name="createFolder"/>
    </page>
</x:config>

<!-- selectedFolder is a session scope data set in myFolder.jsp  -->
<x:set name="createFolderPg.createFolder" property="parentId" value="${selectedFolder}"/>

<c:if test="${forward.name == 'mainPage'}">
    <script>
        window.location="myFolder.jsp";
    </script>
</c:if>

<c:set var="bodyTitle" scope="request"><fmt:message key="mf.label.myFolder"/> > <fmt:message key="mf.label.createFolder"/>  </c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="createFolderPg.createFolder"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>