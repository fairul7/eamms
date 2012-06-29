<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_ManpowerAssignmentDetailPage">
  	<com.tms.fms.engineering.ui.ManpowerAssignmentDetailForm name="form"/>
    <com.tms.fms.engineering.ui.ManpowerAssignmentDetailTable name="listing" width="100%"/>
  </page>
</x:config>

<c:if test="${!empty param.requestId}">
	<x:set name="fms_ManpowerAssignmentDetailPage.listing" value="${param.requestId}" property="requestId"/>
	<x:set name="fms_ManpowerAssignmentDetailPage.form" value="${param.requestId}" property="requestId"/>
</c:if>
 
<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.unitHead.todayAssignment"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
		<x:display name="fms_ManpowerAssignmentDetailPage.form"/>
        <x:display name="fms_ManpowerAssignmentDetailPage.listing"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>