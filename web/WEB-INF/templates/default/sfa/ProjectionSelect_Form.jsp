<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
	<table border="0" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td><x:display name="${form.absoluteName}.lb1"/></td>
			<td><x:display name="${form.absoluteName}.sel_AccountManager"/></td>
		</tr>
		<tr>
			<td><x:display name="${form.absoluteName}.lb2"/>&nbsp;</td>
			<td><x:display name="${form.absoluteName}.sel_Year"/>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2"><x:display name="${form.absoluteName}.submit"/></td>
		</tr>
	</table>
<jsp:include page="../form_footer.jsp" flush="true"/>