<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>


<jsp:include page="../../form_header.jsp" flush="true"/>
<table width="<c:out value="${form.width}"/>">

<tr>
    <td valign="top"><fmt:message key='general.label.title'/>&nbsp; *</td>
    <td>
        <x:display name="${form.childMap.title.absoluteName}"/>
    </td>
</tr>

<tr>
<td valign="top"><fmt:message key='general.label.startDate'/></td>
<td>
<x:display name="${form.childMap.startDate.absoluteName}"/>
</td>
</tr>

<tr>
<td valign="top"><fmt:message key='general.label.endDate'/></td>
<td>
<x:display name="${form.childMap.endDate.absoluteName}"/>
</td>
</tr>

<tr>
<td valign="top"><fmt:message key='general.label.startTime'/></td>
<td>
<x:display name="${form.childMap.startTime.absoluteName}"/>
</td>
</tr>

<tr>
<td valign="top"><fmt:message key='general.label.endTime'/></td>
<td>
    <x:display name="${form.childMap.endTime.absoluteName}"/>
</td>
</tr>

<tr>
    <td valign="top"><fmt:message key='general.label.location'/></td>
    <td>
        <x:display name="${form.childMap.location.absoluteName}"/>
    </td>
</tr>

<tr>
    <td valign="top"><fmt:message key='general.label.description'/></td>
    <td>
        <x:display name="${form.childMap.description.absoluteName}"/>
    </td>
</tr>

<tr>
    <td valign="top">&nbsp;</td>
    <td>
        <x:display name="${form.childMap.submitButton.absoluteName}"/>
    </td>
</tr>

</table>


<jsp:include page="../../form_footer.jsp" flush="true"/>
