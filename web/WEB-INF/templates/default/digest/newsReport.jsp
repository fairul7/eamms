<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="kacang.tld" prefix="x"%>

<c:set var="w" scope="request" value="${widget}" />

<jsp:include page="../form_header.jsp" flush="true" />


<table border=0 cellspacing=2 cellpadding=1>
	<tr>
		<td>Date</td>
		<td>From <x:display name="${w.startDate.absoluteName}" /> To <x:display
			name="${w.endDate.absoluteName}" /></td>
	</tr>
	<tr>
		<td>&nbsp;
		</td>
		<td>
			<x:display name="${w.submitBtn.absoluteName}" />
		</td>
	</tr>
</table>
<br />
<table width="100%" cellpadding="4" cellspacing="1">
	<tr>
		<td class="tableHeader">
		<fmt:message key='digest.label.mailingListName'/>
		</td>
		<td class="tableHeader">
		<fmt:message key='digest.label.emailFormat'/>
		</td>
		<td class="tableHeader">
		<fmt:message key='digest.label.recipientsName'/>
		</td>
		<td class="tableHeader">
		<fmt:message key='digest.label.createdBy'/>
		</td>
		<td class="tableHeader">
		<fmt:message key='digest.label.dateSend'/>
		</td>
		<td class="tableHeader">
		<fmt:message key='digest.label.digestIssue'/>
		</td>
		<td class="tableHeader">
		<fmt:message key='digest.label.digest'/>
		</td>
		<td class="tableHeader">
		<fmt:message key='digest.label.content'/>
		</td>
	</tr>
	<c:forEach var="v" items="${w.results}">
	<c:set var="count" value="0"/>
	<tr>
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>"><c:out value="${v.mailingListName}"/>
		</td>
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>"><c:out value="${v.mailFormat}"/>
		</td>
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>">
		<c:forEach var="receipt" items="${v.recipients}">
		<c:out value="${receipt.recipientName}"/><br>		
		</c:forEach>
		</td>
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>"><c:out value="${v.createdBy}"/>
		</td>
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>"><fmt:formatDate value="${v.dateCreate}" pattern="dd MMM yyyy hh:mm:ss a"/>
		</td>
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>"><c:out value="${v.digestIssueName}"/>
		</td>
		<c:forEach var="dig" items="${v.digest}">	
		
		<c:set var="count" value="${count+1}"/>
		<td class="tableRow" valign="top" ><c:out value="${dig.digestName}"/>
		</td>
		<td class="tableRow" valign="top" >
		<c:forEach var="cont" items="${dig.contents}">	
		<c:out value="${cont.name}"/><br>
		</c:forEach>	
		</td>
		<c:if test="${count==v.digestSize}">
		</tr>	
		</c:if>
		<c:if test="${count!=v.digestSize}">
		</tr>
		<tr>
		</c:if>
		</c:forEach>
		<c:if test="${count=='0'}">
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>"></td>
		<td class="tableRow" valign="top" rowspan="<c:out value="${v.digestSize}"/>"></td></tr>
		</c:if>
		
	</c:forEach>
</table>

<jsp:include page="../form_footer.jsp" flush="true" />
