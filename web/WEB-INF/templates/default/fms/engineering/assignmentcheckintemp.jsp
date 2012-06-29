<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key="fms.facility.form.checkInBy*"/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.psusbCheckInBy"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.checkInDate'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.dpfCheckInDate"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.checkInTime'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tifCheckInTime"/></td>
			</tr>
			<c:forEach var="i" begin="0" end="${form.count-1}">
				<tr><td class="classRowLabel" valign="top" align="right" width="15%">
					<fmt:message key='fms.facility.form.checkInItem'/>&nbsp;<c:out value="${i+1}"/></td>
				<td class="classRow" valign="top" width="40%">
					<x:display name="${form.absoluteName}.tfItem${i}"/>&nbsp;&nbsp;
					<x:display name="${form.absoluteName}.cbDamageItem${i}"/></td>
				</tr>
			</c:forEach>
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