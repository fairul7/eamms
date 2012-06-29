<%@page import="java.util.Date"%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<table width="100%" border="0" cellspacing="0" cellpadding="5" >
	<tr>
		<table width="100%" border="0" cellspacing="0" cellpadding="5" >
			<tr>
				<td width="30%" valign="top">
					<fmt:message key="medialib.label.library"/>
				</td>
				<td>
					<a href="viewLibrary.jsp?id=<c:out value="${w.album.libraryId}"/>"><c:out value="${w.album.libraryName}"/></a>
				</td>
			</tr>
			<tr>
				<td width="30%" valign="top">
					<fmt:message key="medialib.label.name"/>
				</td>
				<td>
					<c:out value="${w.album.name}"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<fmt:message key="medialib.label.description"/>
				</td>
				<td>
					<c:out value="${w.album.description}"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<fmt:message key="medialib.label.eventDate"/>
				</td>
				<td>
					<fmt:formatDate value="${w.album.eventDate}" pattern="${globalDatetimeLong}"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<fmt:message key="medialib.label.createdBy"/>
				</td>
				<td>
					<c:out value="${w.createdBy}"/>
				</td>
			</tr>
		</table>
	</tr>
</table>