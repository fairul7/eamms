<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
	<table border="0" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td>&nbsp;</td>
			<td><fmt:message key='sfa.message.member'/></td>
			<td><fmt:message key='sfa.message.percent'/></td>
		</tr>
		<c:forEach var="i" begin="0" end="2">
		<tr>
			<td><x:display name="${form.absoluteName}.lbNum_${i}"/>&nbsp;</td>
			<td><x:display name="${form.absoluteName}.sel_UserID_${i}"/>&nbsp;</td>
			<td><x:display name="${form.absoluteName}.tf_DistributionPercentage_${i}"/>&nbsp;</td>
		</tr>
		</c:forEach>
		<tr>
			<td colspan="3"><x:display name="${form.absoluteName}.submit"/></td>
		</tr>
	</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
