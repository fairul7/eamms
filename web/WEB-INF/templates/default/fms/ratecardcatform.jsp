 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.rateCardCategoryName'/>&nbsp;* <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.categoryName.absoluteName}" ></x:display>
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.facilityItems'/>&nbsp;*</td>
        <td class="classRow" valign="top">
			<div style="width:50%">
            	<x:display name="${form.fpsbFacility.absoluteName}" ></x:display>
			</div>
        </td>
    </tr>
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.submitButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
 
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>