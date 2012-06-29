<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<c:choose>
<c:when test="${form.type == 'View'}">
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top" width="25%"><b><fmt:message key='sfa.label.category'/>:</b></td>
			<td valign="top" width="75%"><x:display name="${form.absoluteName}.lbCategory"/></td>
		</tr>
		<tr>
			<td valign="top" width="25%"><b><fmt:message key='sfa.message.product&Name'/>:</b></td>
			<td valign="top" width="75%"><x:display name="${form.absoluteName}.lbProductID"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.value'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpValue"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.description'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpDesc"/>&nbsp;</td>
		</tr>
	</table>
</c:when>
<c:when test="${form.type == 'Edit' or form.type == 'Add'}">
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top" width="25%"><b><fmt:message key='sfa.label.category'/>:&nbsp; *</b></td>
			<td valign="top" width="75%"><x:display name="${form.category.absoluteName}"/></td>
		</tr>
		<tr>
			<td valign="top" width="25%"><b><fmt:message key='sfa.message.product&Name'/>:&nbsp; *</b></td>
			<td valign="top" width="75%"><x:display name="${form.absoluteName}.sel_ProductID"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.value'/>:&nbsp;*</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.tf_OpValue"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.description'/>:&nbsp;</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.tf_OpDesc"/></td>
		</tr>
		<tr>
            <td>&nbsp;</td>
			<td align="left" ><x:display name="${form.absoluteName}.submit"/>
                <x:display name="${form.cancel.absoluteName}" />
            </td>
		</tr>
	</table>
</c:when>
</c:choose>
<jsp:include page="../form_footer.jsp" flush="true"/>
