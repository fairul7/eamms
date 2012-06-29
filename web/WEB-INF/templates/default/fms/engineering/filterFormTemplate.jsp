<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>

<table border="0" width="100%" cellspacing="0" cellpadding="2">
	<tr>
		<td class="contentOrgChartfilterbackground" width="35%">
			<table width="100%" border="0" cellspacing="0" cellpadding="1" >
				<tr>
					<td align="right">
						<x:display name="${form.childMap['pageSizeSelectBox'].absoluteName}" />
						
						<c:if test="${!empty form.childMap['tfSearchText'].absoluteName}">
							<x:display name="${form.childMap['tfSearchText'].absoluteName}" />
						</c:if>
						
						<c:if test="${!empty form.childMap['sbDepartment'].absoluteName}">
							<x:display name="${form.childMap['sbDepartment'].absoluteName}" />
						</c:if>
						
						<c:if test="${!empty form.childMap['sbStatus'].absoluteName}">
							<x:display name="${form.childMap['sbStatus'].absoluteName}" />
						</c:if>
					</td>
				</tr>

			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="1" >
				<tr>
					<c:if test="${!empty form.childMap['requiredDate'].absoluteName}">
						<td align="right" height="25"><b><fmt:message key="fms.facility.label.requiredDate" /> <fmt:message key="general.from" /></b>
							<x:display name="${form.childMap['requiredDate'].absoluteName}" />
						</td>
					</c:if>
					<c:if test="${!empty form.childMap['requiredDateTo'].absoluteName}">
						<td align="right" height="25"><b><fmt:message key="fms.facility.label.requiredDate" /> <fmt:message key="general.to" /></b>
							<x:display name="${form.childMap['requiredDateTo'].absoluteName}" />
						</td>
					</c:if>
					
				</tr>
			</table>
		</td>
		<td class="contentOrgChartfilterbackground" width="1%">
			<table width="100%" height="55" border="0" cellspacing="0" cellpadding="1" >
				<tr>
					<td align="center" valign="bottom"><x:display name="${form.childMap['filter'].absoluteName}" /></td>
				</tr>
			</table>
		</td>
	</tr>
</table>