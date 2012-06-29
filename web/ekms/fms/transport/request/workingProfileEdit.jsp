<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_workingProfilePage">
    <com.tms.fms.facility.ui.WorkingProfileForm name="edit" width="100%"/>
  </page>
</x:config>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.workingProfileId)}">
    <c:set var="workingProfileId" value="${param.workingProfileId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="workingProfileId" value="${widgets['elearning_programmeFormPage.programmeEdit'].workingProfileId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_workingProfilePage.edit" property="workingProfileId" value="${workingProfileId}"/>
<x:set name="fms_workingProfilePage.edit" property="type" value="${type}"/>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileUpdated"/>'); 
    document.location = "<c:url value="workingProfileListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.workingProfileExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.fms.facility.msg.workingProfileNotUpdate"/>');</script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.msg.workingProfileSetup"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_workingProfilePage.edit"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>