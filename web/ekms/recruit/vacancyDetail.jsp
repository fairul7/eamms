<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>

<x:config >
	<page name="vacancy">
		<com.tms.hr.recruit.ui.VacancyViewPopUp name="View"/>
    </page>
</x:config>

<!-- Handle Events -->
<c:if test="${!empty param.vacancyCode}">
	<x:set name="vacancy.View" property="vacancyCode" value="${param.vacancyCode}"/>	
</c:if>

<script type="text/javascript">
<!--
function toggle_visibility(id) {
var e = document.getElementById(id);
if(e.style.display == 'none')
	e.style.display = 'block';
else
	e.style.display = 'none';
}
//-->
</script>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

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
    	 <fmt:message key="recruit.menu.label.vacancyDetail"/> 
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
  
     <x:display name="vacancy.View"/>
  
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
</table>
</body>
</html>
