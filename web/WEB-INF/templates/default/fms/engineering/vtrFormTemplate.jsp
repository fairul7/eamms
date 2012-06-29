<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ page import="kacang.Application,com.tms.fms.engineering.model.EngineeringModule"%>

<c:set var="form" value="${widget}"/>
<script type="text/javascript">

	onload = function() {
		populateClientName();
	}	
</script>
<jsp:include page="../../form_header.jsp" flush="true"/>

<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
				
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.facility'/>*</td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.facilitySelectBox.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.serviceParticulars'/>*</td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.serviceParticulars.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.blockBooking'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.pnBB.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.requiredFrom'/>*</td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.requiredDate.absoluteName}" /></td>
	</tr>
	
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.requiredTo'/>*</td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.requiredDateTo.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.requiredTime'/>*</td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.timePanel1.absoluteName}" /></td>
	</tr>
	<tr id="format" <c:if test="${(widget.lbService == '3') or (widget.lbService == '4') or (widget.lbService == '5')}">style="display:none"</c:if>>
	<td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.format'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.timePanel2.absoluteName}" /></td>
	</tr>
	<tr id="formatIngest" <c:if test="${(widget.lbService != '3') or (widget.lbService != '4') or (widget.lbService != '5')}">style="display:none"</c:if>>
	<td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.format'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.timePanel4.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.conversion'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.timePanel3.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.duration'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.duration.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.noOfCopies'/>*</td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.noOfCopies.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.label.attachment'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.attachmentPanel.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.facility.table.location'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.childMap.location.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		<fmt:message key='fms.request.label.remarks'/></td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.remarks.absoluteName}" /></td>
	</tr>
	<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
		&nbsp;</td>
	<td class="classRow" valign="top" width="40%">
		<x:display name="${form.buttonPanel.absoluteName}" /></td>
	</tr>
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>

