<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_vtrEditPage">
    	<com.tms.fms.engineering.ui.VtrView name="edit" width="100%"/>
  </page>
</x:config>

<c:set var="type" value="View"/>
<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="id" value="${param.id}"/>
  </c:when>
  <c:otherwise>
    <c:set var="id" value="${widgets['fms_vtrEditPage.edit'].id}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_vtrEditPage.edit" property="id" value="${id}"/>
<x:set name="fms_vtrEditPage.edit" property="type" value="${type}"/>


<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.msg.vtrBooking"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_vtrEditPage.edit"/>
    
    </td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
