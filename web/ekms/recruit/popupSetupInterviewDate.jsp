<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="setupInterviewDateTime">
		<com.tms.hr.recruit.ui.PopupSetupInterviewDateTime name="form" />
    </page>
</x:config>

<!-- Handle Events -->
<c:if test="${!empty param.applicantId}">
    <x:set name="setupInterviewDateTime.form" property="applicantId" value="${param.applicantId}"/>   
</c:if>

<c:if test="${forward.name=='cancel_form_action'}">
    <script type="text/javascript">
       window.close();    
    </script>
</c:if>

<c:if test="${forward.name=='submit'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancy.alert.interviewDateTimeAdded"/>");
        window.opener.location="setupInterview.jsp";
        window.close(); 
    </script>
</c:if>

<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus()">
<c:set var="type" value="${param.type}" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
     
     <fmt:message key='recruit.menu.label.setupIntervieweeDate'/>
 
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
   <x:display name="setupInterviewDateTime.form"/>
     <%--
        <c:out value="${widgets['vacancy.View'].vacancyObj.responsibilities}" escapeXml="false"/>
    --%>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
</table>
</body>
</html>


 
