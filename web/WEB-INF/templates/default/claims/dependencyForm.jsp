<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" >
<tr>
    <td align="right" valign="top" width="30%"><b>Category Name</b></td>
    <td ><c:out value="${w.categoryName}"/></td>
</tr>
<tr>
    <td align="right" valign="top" width="30%"><b>Dependent Department</b></td>
    <td >
    <% int i=0;%>
    <c:forEach var="name" items="${w.attachedDepartment}">
        <% i++;%>
        <c:out value="${name}"/><br>
    </c:forEach>
    <% if (i<=0) { %>
        --
    <% } %>
    </td>
</tr>
<tr>
    <td align="right" valign="top" width="30%"><b>Add Dependencies</b></td>
    <td ><x:display name="${w.combo.absoluteName}"/></td>
</tr>
<tr>
    <td></td>
    <td>
        <x:display name="${w.bnSubmit.absoluteName}"/>
        <x:display name="${w.bnCancel.absoluteName}"/>
    </td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>