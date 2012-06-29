<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="left" width="100%" colspan="2">
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr class="tableHeader"><td>
						<strong><fmt:message key="fms.facility.table.item"/></strong></td>
					<td>
						<strong><fmt:message key="fms.facility.table.availability"/></strong></td>
					</tr>
					<c:forEach var="i" begin="0" end="${form.count-1}">
						<tr class="tableRow"><td>
							<x:display name="${form.absoluteName}.pnDetail.lbFacility${i}"/></td>
						<td>
							<x:display name="${form.absoluteName}.pnDetail.lbQuantity${i}"/></td>
						</tr>
					</c:forEach>
				</table>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnButton"/></td>
			</tr>
		</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>