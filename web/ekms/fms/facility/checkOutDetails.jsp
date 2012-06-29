<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_checkOutDetailsFormPage">
    <com.tms.fms.facility.ui.CheckOutDetailsForm name="form" width="100%"/>
  </page>
</x:config>
<c:if test="${!empty param.groupId}">
	<c:set var="groupId" value="${param.groupId}"/>
  	<x:set name="fms_checkOutDetailsFormPage.form" property="groupId" value="${param.groupId}"/>
</c:if>

<c:choose>
	<c:when test="${forward.name == 'UPDATED'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckOutUpdatedSuccessful'/>");document.location = "<c:url value="checkOutListing.jsp"/>"</script>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.table.checkOutDetails"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_checkOutDetailsFormPage.form"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>