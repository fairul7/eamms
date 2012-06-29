<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_assignmentDetailsPage">
    <com.tms.fms.engineering.ui.AssignmentDetails name="details" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="id" value="${param.id}"/>
  </c:when>
  <c:otherwise>
    <c:set var="id" value="${widgets['fms_assignmentDetailsPage.details'].id}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_assignmentDetailsPage.details" property="id" value="${id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b>
			<font color="#FFCF63" class="contentTitleFont">
        		<fmt:message key="fms.label.assignmentDetails"/>
			</font></b>
		</td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_assignmentDetailsPage.details"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>