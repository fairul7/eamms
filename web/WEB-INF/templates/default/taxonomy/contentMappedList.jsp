<%@ include file="/common/header.jsp" %>

<c:set var="w" value="${widget}"/>


<table width="100%" cellpadding="0" cellspacing="0">
<% int i=0; %>
<c:forEach var="selectedNode" items="${w.list}">
    <% String s=""+i; %>
    <c-rt:set var="i" value="<%=s%>"/>
    <tr>
        <td width="100%">
        <c:forEach var="child" items="${w.mappedList[i]}">
            <c:if test="${child.taxonomyId!=selectedNode.taxonomyId}">
            <c:out value="${child.taxonomyName}"/>&nbsp;&gt;&nbsp;
            </c:if>
            <c:if test="${child.taxonomyId==selectedNode.taxonomyId}">
            <b><c:out value="${child.taxonomyName}"/></b>
            </c:if>
        </c:forEach>
        </td>
    </tr>
    <% i++; %>
</c:forEach>
    <c:if test="${w.permissionMap['Taxonomy']}">
    <tr>
        <td>[ <a href="contentMapping.jsp"><fmt:message key="message.taxonomy.changeTaxonomy"/></a> ]</td>
    </tr>
    </c:if>    
</table>

