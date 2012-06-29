<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<x:display name="${widget.panel.absoluteName}" body="custom">
<br>
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
	<tr>
		<th>aa</th><th>bb</th>
	</tr>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right">&nbsp;</td>
        <td width="70%" class="profileRow"></td>
    </tr>
    <tr><td class="profileFooter" colspan="2"><x:display name="${widget.add.absoluteName}"/></td></tr>
</table><br>
</x:display>
<jsp:include page="../../form_footer.jsp" flush="true"/>

