<%--<%@include file="/common/header.jsp"%>--%>
<tr>
    <td valign="top">No</td>
    <c:forEach var="col" items="${f.columnList}" varStatus="colStatus">
        <td valign="top"><c:out value="${col.header}" default="-n/a-" /></td>
    </c:forEach>
</tr>