<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>

		<table border="0" cellpadding="2" cellspacing="2" class="formStyle" width="<c:out value="${form.width}"/>" >
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key="fms.tran.setup.${form.setupString}Name*"/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.namelbl"/>
				<x:display name="${form.absoluteName}.Name"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.setup.${form.setupString}Description'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.Description"/></td>
			</tr>
			<c:if test="${(form.setupString eq 'category') || (form.setupString eq 'outsourcePanel')}">
				<tr><td class="classRowLabel" valign="top" align="right" width="15%">
					<fmt:message key='fms.tran.setup.type'/></td>
				<td class="classRow" valign="top" width="40%">
					<x:display name="${form.absoluteName}.pnType"/></td>
				</tr>
			</c:if>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				<fmt:message key='fms.tran.setup.status'/></td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.IsActive"/></td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				&nbsp;</td>
			</tr>
			<tr><td class="classRowLabel" valign="top" align="right" width="15%">
				&nbsp;</td>
			<td class="classRow" valign="top" width="40%">
				<x:display name="${form.absoluteName}.buttonPanel"/></td>
			</tr>
		</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>