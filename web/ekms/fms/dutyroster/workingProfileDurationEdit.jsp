<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_workingProfileDurationPage">
    <com.tms.fms.facility.ui.WorkingProfileDurationForm name="edit" width="100%"/>
  </page>
</x:config>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.workingProfileDurationId)}">
    <c:set var="workingProfileDurationId" value="${param.workingProfileDurationId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="workingProfileDurationId" value="${widgets['fms_workingProfileDurationPage.edit'].workingProfileDurationId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_workingProfileDurationPage.edit" property="workingProfileDurationId" value="${workingProfileDurationId}"/>
<x:set name="fms_workingProfileDurationPage.edit" property="type" value="${type}"/>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileDurationUpdated"/>'); 
    document.location = "<c:url value="workingProfileDuration.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileDurationExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileDurationNotUpdate"/>');</script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.msg.workingProfileDurationSetup"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_workingProfileDurationPage.edit"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>