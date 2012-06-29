<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>

<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
	<tr>
		<td class="classRowLabel" valign="top" align="right" width="15%">
			<fmt:message key='fms.facility.label.revalidateBooking'/>&nbsp;
		</td>
		<td class="classRow" valign="top" style="line-height: 30px">
			<x:display name="${form.absoluteName}.btnSubmit"/><br />
			(<fmt:message key='fms.facility.label.revalidateBookingInfo'/>)
		</td>
	</tr>
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>