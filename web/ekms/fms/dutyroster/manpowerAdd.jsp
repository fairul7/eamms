<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.hr.competency.ui.UserCompetencyForm"%>

<x:config>
  <page name="fms_manpowerPage">
    <com.tms.fms.facility.ui.UserCompetencyForm name="add" width="100%"/>
  </page>
</x:config>

<c-rt:set var="forward_competency_add" value="<%= UserCompetencyForm.FORWARD_ADD %>"/>
<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.manpowerAdded"/>'); 
    document.location = "<c:url value="manpowerListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.manpowerExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.manpowerNotAdded"/>');</script>
  </c:when>
</c:choose>
<c:if test="${forward.name == forward_competency_add}">
    <script>
        window.open("addUserCompetency.jsp?redirectTo=fms_manpowerPage.add", "profileWindow", "height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.menu.manpowerSetup"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_manpowerPage.add"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>