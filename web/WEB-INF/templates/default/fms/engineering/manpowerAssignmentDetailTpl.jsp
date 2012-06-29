<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
	<tr>
        <td width="20%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="80%" class="profileRow"><x:display name="${form.absoluteName}.reqTitle"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.label.requiredDate"/></b>&nbsp;</td>
        <td width="80%" class="profileRow"><x:display name="${form.absoluteName}.reqDate"/></td>
    </tr>
    <tr>
        <td width="20%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.program"/></b>&nbsp;</td>
        <td width="80%" class="profileRow"><x:display name="${form.absoluteName}.reqProgram"/></td>
    </tr>
</table>
<jsp:include page="../../form_footer.jsp" flush="true"/>