<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle"  width="<c:out value="${form.width}"/>">
		<c:forEach var="i" begin="0" end="11">
		<tr>
			<td><x:display name="${form.absoluteName}.monthLabel_${i}"/>&nbsp;</td>
			<td><x:display name="${form.absoluteName}.tf_MonthProj_${i}"/>&nbsp;</td>
		</tr>
		</c:forEach>
		<tr>
			<td colspan="2" align="center"><x:display name="${form.absoluteName}.submit"/>
                <x:display name="${form.cancel.absoluteName}" />
            </td>
		</tr>
	</table>
<jsp:include page="../form_footer.jsp" flush="true"/>

