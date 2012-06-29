<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<%-- 
<jsp:include page="../../form_header.jsp" flush="true"/>
<table width="<c:out value="${form.width}"/>">
--%>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
			
			
			
			
			


<tr>
    <td class="classRowLabel" align="right" valign="top" width="150px"><fmt:message key='general.label.title'/>&nbsp; *</td>
    <td class="classRow">
        <x:display name="${form.childMap.title.absoluteName}"/>
    </td>
</tr>

<tr>
<td class="classRowLabel"  align="right" valign="top"><fmt:message key='general.label.startDate'/></td>
<td class="classRow">
<x:display name="${form.childMap.startDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" align="right" valign="top"><fmt:message key='general.label.endDate'/></td>
<td class="classRow">
<x:display name="${form.childMap.endDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" align="right" valign="top"><fmt:message key='general.label.startTime'/></td>
<td class="classRow">
<x:display name="${form.childMap.startTime.absoluteName}"/>
</td>
</tr>

<tr>
<td  class="classRowLabel" align="right" valign="top"><fmt:message key='general.label.endTime'/></td>
<td class="classRow">
    <x:display name="${form.childMap.endTime.absoluteName}"/>
</td>
</tr>

<tr>
    <td  class="classRowLabel" align="right" valign="top"><fmt:message key='general.label.location'/></td>
    <td class="classRow">
        <x:display name="${form.childMap.location.absoluteName}"/>
    </td>
</tr>

<tr>
    <td class="classRowLabel" align="right" valign="top"><fmt:message key='general.label.description'/></td>
    <td class="classRow">
        <x:display name="${form.childMap.description.absoluteName}"/>
    </td>
</tr>

<tr>
    <td class="classRowLabel" align="right" valign="top">&nbsp;</td>
    <td class="classRow">
        <x:display name="${form.childMap.submitButton.absoluteName}"/>
    </td>
</tr>






<jsp:include flush="true" page="../../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>




<%-- 
</table>

<jsp:include page="../../form_footer.jsp" flush="true"/>
--%>