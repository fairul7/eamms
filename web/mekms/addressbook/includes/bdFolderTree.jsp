<%@include file="/common/header.jsp" %>

<x:config>
    <page name="bdFolderTree">
        <com.tms.collab.directory.ui.FolderTree name="tree" type="DirectoryModule" />
    </page>
</x:config>

<x:display name="bdFolderTree.tree" body="custom">
    <c:set var="tree" scope="request" value="${widget}"/>
    <c:set var="root" scope="request" value="${tree.model}"/>

    <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">

    <script language="javascript" src="<%= request.getContextPath() %>/common/tree/tree.js">
    </script>

    <table border=0 cellspacing=2 cellpadding=1>
    <tr>
        <td valign=top>
            <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">
                <a class="contentTreeNode" href="#" onclick="treeToggle('bd<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>'); return false"><span id="icon_bd<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>">+</span></a>
            </c:if>
            <c:if test="${empty root[tree.childrenProperty] || empty root[tree.childrenProperty][0]}">
                <span class="contentTreeNode">-</span>
            </c:if>
        </td>
        <td>
            <span class="contentTreeLinkLite"><fmt:message key='addressbook.label.folders'/></span>
            <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">
                <span id="bd<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>" style="display: block">
                    <script>
                    <!--
                        treeLoad('bd<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>');
                    //-->
                    </script>
                        <c:set var="orig" scope="page" value="${root}"/>
                        <c:catch var="ie">
                            <jsp:include page="folderTreeRecursion.jsp" flush="true"/>
                        </c:catch>
                        <c:set var="root" scope="request" value="${orig}"/>
                </span>
            </c:if>
        </td>
    </tr>
    </table>
<%--    <img src="<c:url value="/ekms/"/>images/blank.gif" width="0" height="1" border="0">--%>

    </c:if>
</x:display>
