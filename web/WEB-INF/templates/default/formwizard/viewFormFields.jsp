<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>


<jsp:include page="../form_header.jsp" flush="true"/>
<table>


<tr>
<td valign="top" align="left" colspan="2"><B><fmt:message key='formWizard.label.fieldNames'/></B></td>
</tr>
<tr>
<td colspan="2">
    <x:display name="${form.fields.absoluteName}"/>
</td>
</tr>

<tr>
<td valign="top"  align="center">
<x:display name="${form.submit.absoluteName}"/>
<x:display name="${form.cancel.absoluteName}"/>
</td>
</tr>

</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
