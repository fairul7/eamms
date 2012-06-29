<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="kacang.tld" prefix="x"%>

<c:set var="w" scope="request" value="${widget}" />

<br />
<table width="100%" cellpadding="1" cellspacing="1">
	<tr>
		<td class="tableHeader">
			&nbsp;
		</td>
		<td class="tableHeader">
			User
		</td>
		
	</tr>
	<c:set var="counter" value="${1}"/>
	<c:forEach var="user" items="${w.results}">
	<tr>
		<td>
			<c:out value="${counter}"/>
		</td>
		<td>
			<c:out value="${user.userName}"/>
		</td>
	</tr>
	<c:set var="counter" value="${counter+1}"/>
	</c:forEach>
	
</table>
