<%@include file="/common/header.jsp" %>

<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" scope="request" value="${widget}"/>





<c:set var="w" value="${widget}" />
<c:set var="form" value="${w}" />

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">


	<tr>
		<td class="classRowLabel" align="right" valign="top"><fmt:message key="general.label.name" /></td>
		<td class="classRow"><x:display name="${form.childMap.tfName.absoluteName}" /></td>
	</tr>  
	<tr>
		<td class="classRowLabel" align="right" valign="top"><fmt:message key="general.label.description" /></td>
		<td class="classRow"><x:display name="${form.childMap.tfDescription.absoluteName}" /></td>
	</tr>
	<tr>
		<td class="classRowLabel" align="right" valign="top">&nbsp;</td>
		<td class="classRow"><x:display name="${form.childMap.p1.absoluteName}" />
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
	
	


