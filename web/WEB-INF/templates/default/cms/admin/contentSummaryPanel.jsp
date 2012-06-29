<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" value="${widget}"/>

<table width="100%">
<tr>
<td width="33%" valign="top" nowrap>
<fmt:message key='cms.label.contentCheckedOut'/>: <font color="red"><c:out value="${panel.checkedOutCount}"/></font>
</td>
<td valign="top">
&nbsp;&nbsp;|&nbsp;&nbsp;
</td>
<td width="33%" valign="top" nowrap>
<fmt:message key='cms.label.contentToApprove'/>: <font color="red"><c:out value="${panel.submittedCount}"/></font>
</td>
<td valign="top">
&nbsp;&nbsp;|&nbsp;&nbsp;
</td>
<td width="33%" valign="top" nowrap>
<fmt:message key='cms.label.contentToPublish'/>: <font color="red"><c:out value="${panel.approvedCount}"/></font>
</td>
</tr>
</table>
