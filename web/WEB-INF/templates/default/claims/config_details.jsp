<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" >
<tr>
    <td align="right" valign="top" width="30%"><b><fmt:message key="claims.label.defaultassessors"/>:</b></td>
    <td >
    <c:forEach var="assessor" items="${w.assessor}">
    <c:out value="${assessor}"/>
    </c:forEach>
    </td>
</tr>
<tr>
    <td align="right" valign="top" width="30%"><b><fmt:message key="claims.label.mileage"/>:</b></td>
    <td >
    <c:out value="${w.mileage}"/>
    </td>
</tr>
<tr>
    <td align="right" valign="top" width="30%"><b><fmt:message key="claims.label.approvallogic"/>:</b></td>
    <td >
    <c:forEach var="logic" items="${w.approvalLogic}">
    <c:out value="${logic}"/>
    </c:forEach>
    </td>
</tr>
</table>
