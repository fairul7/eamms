<%--<%@ include file="/common/header.jsp" %>--%>
<tr>
    <td valign="top"><b>No</b></td>
    <c:forEach var="col" items="${f.columnList}" varStatus="colStatus">
        <td valign="top"><b><c:out value="${col.header}" default="-n/a-" /></b></td>
    </c:forEach>
    <td id="tableAction0">&nbsp;</td>
</tr>