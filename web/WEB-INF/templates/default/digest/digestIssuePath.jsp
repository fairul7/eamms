<%@ include file="/common/header.jsp" %>
<c:set var="path" value="${widget}"/>
<c:set var="dido" value="${path.dido}"/>
&nbsp;<a class="path" href="digestIssue.jsp"><b><fmt:message key='digest.label.digestIssueList'/></b>
</a> > <a class="path" href="digest.jsp?digestIssueId=<c:out value="${dido.digestIssueId}"/>"><b><c:out value="${dido.digestIssueName}"/></b></a>     