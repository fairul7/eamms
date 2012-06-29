<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<table width="100%" border="0" cellspacing="0" cellpadding="5" >
	<tr>
		<table width="100%" border="0" cellspacing="0" cellpadding="5" >
			<tr>
				<td width="30%" valign="top">
					<fmt:message key="medialib.label.name"/>
				</td>
				<td>
					<c:out value="${w.library.name}"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<fmt:message key="medialib.label.description"/>
				</td>
				<td>
					<c:out value="${w.library.description}"/>
				</td>
			</tr>
			<c:if test="${w.highestRole eq 'manager' || w.highestRole eq 'contributor'}">
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.maxWidthPixel"/>
					</td>
					<td>
						<c:out value="${w.library.maxWidth}"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<fmt:message key="medialib.label.publishingOptions"/>
					</td>
					<td>
						<c:if test="${w.library.approvalNeeded eq 'Y'}">
							Approval needed before publishing
						</c:if>
						<c:if test="${w.library.approvalNeeded eq 'N'}">
							No approval needed before publishing
						</c:if>
					</td>
				</tr>
			</c:if>
			<tr>
				<td valign="top">
					<fmt:message key="medialib.label.yourRole"/>
				</td>
				<td>
					<c:if test="${w.highestRole eq 'manager'}">
						<fmt:message key="medialib.label.manager"/>
					</c:if>
					<c:if test="${w.highestRole eq 'contributor'}">
						<fmt:message key="medialib.label.contributor"/>
					</c:if>
					<c:if test="${w.highestRole eq 'viewer'}">
						<fmt:message key="medialib.label.viewer"/>
					</c:if>
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