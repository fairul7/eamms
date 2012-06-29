<%@ include file="/common/header.jsp" %>
<c:set var="path" value="${widget}"/>
<c:set var="ddo" value="${path.ddo}"/>
<c:set var="dido" value="${path.dido}"/>
&nbsp;<a class="path" href="digestIssue.jsp"><b><fmt:message key='digest.label.digestIssueList'/></b>
</a> > <a class="path" href="digest.jsp?digestIssueId=<c:out value="${dido.digestIssueId}"/>"><b><c:out value="${dido.digestIssueName}"/></b>     
</a> > <a class="path" href="content.jsp?digestId=<c:out value="${ddo.digestId}"/>"><b><c:out value="${ddo.digestName}"/></b></a> 