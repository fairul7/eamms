<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<!-- JSON -->
<script type='text/javascript' src="<c:out value='${pageContext.request.contextPath}/common/ajaxtoolkit/jsonrpc.js'/>"></script>
<!-- End of JSON -->
<jsp:include page="../../form_header.jsp" flush="true"/>

		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key="fms.facility.form.checkOutBy*"/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.psusbCheckOutBy"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
				<fmt:message key="fms.facility.table.takenBy"/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.takenBy.absoluteName}"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%" height="25">
				<fmt:message key="fms.facility.table.preparedBy"/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.preparedBy.absoluteName}"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.purpose'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfPurpose"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.assignmentLocation'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfLocation"/></td>
			</tr>
			<c:forEach var="i" begin="0" end="${form.count-1}">
				<tr><td class="classRowLabel" valign="top" align="right" width="15%">
					<fmt:message key='fms.facility.form.checkOutItem'/>&nbsp;<c:out value="${i+1}"/></td>
				<td class="classRow" valign="top" width="40%">
					<x:display name="${form.absoluteName}.tfItem${i}"/>
					<span id="labelItemName${i+1}">&nbsp;</span></td>
						
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
<script type='text/javascript'>

function displayItem(fldobj,nbox, label) {
	var itemCode=document.getElementsByName(nbox)[0].value;
	
	jsonrpc = new JSONRpcClient("/JSON-RPC");
	var result = jsonrpc.facilityModule.itemNameByBarcode(itemCode);
	var el = document.getElementById(label);
	el.firstChild.data=result.item;

}

</script>
<jsp:include page="../../form_footer.jsp" flush="true"/>