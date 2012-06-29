<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>

		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key="fms.facility.form.categoryName*"/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tfName"/>				
				<x:display name="${form.absoluteName}.tfNamelbl"/>
				<x:display name="${form.absoluteName}.hdName"/>	
			</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.categoryDescription'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.tbDescription"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.department'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbDepartment"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.unit'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbUnit"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.parentCategory'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.pnParent"/></td>
			</tr>
			<tr id="category" name="category"><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.facility.form.selectParentCategory'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.sbCategory"/></td>
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